package com.budget_app.controllers;

import com.budget_app.expenses.Expense;
import com.budget_app.jt_interfaces.ExpenseListener;
import com.budget_app.jt_interfaces.ExpenseView;

public class ExpenseController extends BaseController<Expense, ExpenseView> implements ExpenseListener
{

    public ExpenseController(Expense expense, ExpenseView view)
    {
        super(expense, view);
    }

    //region Public Methods

    public void changeID(long id)
    {
        getModel().setId(id);
        idChanged();
    }

    public void changeName(String name)
    {
        getModel().setName(name);
        nameChanged();
    }

    public void changePrice(long price)
    {
        getModel().setPrice(price);
        priceChanged();
    }

    public void changeCategory(String category)
    {
        getModel().setCategory(category);
        categoryChanged();
    }

    public void changeDescription(String description)
    {
        getModel().setDescription(description);
        descriptionChanged();
    }

    //endregion

    //region BaseController Methods

    @Override
    protected void updateViews()
    {
        for (ExpenseView v : getViews())
        {
            v.updateID(getModel().getId());
            v.updateName(getModel().getName());
            v.updatePrice(getModel().getPrice());
            v.updateCategory(getModel().getCategory());
            v.updateDescription(getModel().getDescription());
        }
    }

    //endregion

    //region ExpenseListener Methods

    @Override
    public void idChanged() {
        for (ExpenseView v : getViews())
            v.updateID(getModel().getId());
    }

    @Override
    public void priceChanged() {
        for (ExpenseView v : getViews())
            v.updatePrice(getModel().getPrice());
    }

    @Override
    public void nameChanged() {
        for (ExpenseView v : getViews())
            v.updateName(getModel().getName());
    }

    @Override
    public void categoryChanged() {
        for (ExpenseView v : getViews())
            v.updateCategory(getModel().getCategory());
    }

    @Override
    public void descriptionChanged() {
        for (ExpenseView v : getViews())
            v.updateDescription(getModel().getDescription());
    }

    //endregion

}
