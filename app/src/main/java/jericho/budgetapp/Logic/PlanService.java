package jericho.budgetapp.Logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jericho.budgetapp.Model.PeriodicBudget;
import jericho.budgetapp.Model.Plan;
import jericho.budgetapp.Persistence.Interfaces.PeriodicBudgetPersistence;
import jericho.budgetapp.Persistence.Interfaces.PlanPBPersistence;
import jericho.budgetapp.Persistence.Interfaces.PlanPersistence;

public class PlanService
{

    private PlanPersistence m_planPersistence;
    private PlanPBPersistence m_bridgePersistence;
    private PeriodicBudgetPersistence m_pbPersistence;

    public PlanService(
            PlanPersistence planPersistence,
            PlanPBPersistence bridgePersistence,
            PeriodicBudgetPersistence pbPersistence)
    {
        m_planPersistence = planPersistence;
        m_bridgePersistence = bridgePersistence;
        m_pbPersistence = pbPersistence;
    }

    /**
     * Gets the {@link Plan} with the specified ID.
     * @param id The ID of the {@link Plan} to get.
     * @return The {@link Plan} with the specified ID.
     */
    public Plan getPlan(long id)
    {
        Plan plan = m_planPersistence.getPlan(id);

        List<Long> budgetIds = m_bridgePersistence.getPeriodicBudgetsIn(id);
        Collections.sort(budgetIds);

        List<PeriodicBudget> budgets = new ArrayList<>(budgetIds.size());
        for (long budgetId : budgetIds)
        {
            PeriodicBudget budget = m_pbPersistence.getPeriodicBudget(budgetId);
            budgets.add(budget);
        }

        // TODO: Redo how daily budgets are stored
        PeriodicBudget[] budgetsArr = new PeriodicBudget[7];
        plan.setDailyBudgets(budgets.toArray(budgetsArr));

        return plan;
    }

    /**
     * Gets the {@link Plan} with the specified name.
     * @param name The name of the {@link Plan} to get.
     * @return The {@link Plan} with the specified name.
     */
    public Plan getPlan(String name)
    {
        Plan plan = m_planPersistence.getPlan(name);

        List<Long> budgetIds = m_bridgePersistence.getPeriodicBudgetsIn(plan.getId());
        Collections.sort(budgetIds);

        List<PeriodicBudget> budgets = new ArrayList<>(budgetIds.size());
        for (long budgetId : budgetIds)
        {
            PeriodicBudget budget = m_pbPersistence.getPeriodicBudget(budgetId);
            budgets.add(budget);
        }

        // TODO: Redo how daily budgets are stored
        PeriodicBudget[] budgetsArr = new PeriodicBudget[7];
        plan.setDailyBudgets(budgets.toArray(budgetsArr));

        return plan;
    }

    /**
     * Adds the {@link Plan} to persistence.
     * @param plan The {@link Plan} to add.
     * @return The new ID if addition was successful, -1 otherwise.
     */
    public long addPlan(Plan plan)
    {
        long id = m_planPersistence.addPlan(plan);

        createBudgets(Arrays.asList(plan.getDailyBudgets()));
        addBudgetsToPlan(plan);

        return id;
    }

    /**
     * Updates the specified {@link Plan}.
     * @param plan The {@link Plan} to update.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updatePlan(Plan plan)
    {
        boolean success = m_planPersistence.updatePlan(plan);

        removeBudgetsFromPlan(plan);
        deleteBudgets(Arrays.asList(plan.getDailyBudgets()));

        createBudgets(Arrays.asList(plan.getDailyBudgets()));
        addBudgetsToPlan(plan);

        return success;
    }

    /**
     * Deletes the {@link Plan} with the specified ID.
     * @param id The ID of the {@link Plan} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deletePlan(long id)
    {
        List<Long> budgetIds = m_bridgePersistence.getPeriodicBudgetsIn(id);
        for (long budgetId : budgetIds)
            m_pbPersistence.deletePeriodicBudget(budgetId);

        m_bridgePersistence.deletePlan(id);
        return m_planPersistence.deletePlan(id);
    }

    /**
     * Deletes the {@link Plan} with the specified name.
     * <p>
     *     Note: Deleting by ID has better performance.
     * </p>
     * @param name The name of the {@link Plan} to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deletePlan(String name)
    {
        Plan plan = m_planPersistence.getPlan(name);

        return deletePlan(plan.getId());
    }

    /**
     * Creates the {@link PeriodicBudget}s in persistence and updates their IDs.
     * @param budgets The {@link PeriodicBudget}s to create.
     * @return The number of budgets created.
     */
    private int createBudgets(Iterable<PeriodicBudget> budgets)
    {
        int numAdded = 0;

        for (PeriodicBudget budget : budgets)
        {
            long id = m_pbPersistence.addPeriodicBudget(budget);
            budget.setId(id);
            if (id > -1)
                numAdded++;
        }

        return numAdded;
    }

    /**
     * Deletes the {@link PeriodicBudget}s from persistence and resets their IDs to -1.
     * @param budgets The {@link PeriodicBudget}s to delete.
     * @return The number of budgets deleted.
     */
    private int deleteBudgets(Iterable<PeriodicBudget> budgets)
    {
        int numDeleted = 0;

        for (PeriodicBudget budget : budgets)
        {
            boolean deleted = m_pbPersistence.deletePeriodicBudget(budget.getId());
            budget.setId(-1);
            if (deleted)
                numDeleted++;
        }

        return numDeleted;
    }

    /**
     * Associates the {@link PeriodicBudget}s in the {@link Plan} with the {@link Plan}.
     * @param plan The {@link Plan} to create associations for.
     * @return The number of associations made.
     */
    private int addBudgetsToPlan(Plan plan)
    {
        int numAdded = 0;
        long planId = plan.getId();
        for (PeriodicBudget budget : plan.getDailyBudgets())
        {
            boolean added = m_bridgePersistence.addPeriodicBudgetTo(budget.getId(), planId);
            if (added)
                numAdded++;
        }

        return numAdded;
    }

    /**
     * Dissociates the {@link PeriodicBudget}s in the {@link Plan} from the {@link Plan}.
     * @param plan The {@link Plan} to remove associations for.
     * @return The number of associations removed.
     */
    private int removeBudgetsFromPlan(Plan plan)
    {
        int numRemoved = 0;
        long planId = plan.getId();
        for (PeriodicBudget budget : plan.getDailyBudgets())
        {
            boolean removed = m_bridgePersistence.removePeriodicBudgetFrom(budget.getId(), planId);
            if (removed)
                numRemoved++;
        }

        return numRemoved;
    }

}
