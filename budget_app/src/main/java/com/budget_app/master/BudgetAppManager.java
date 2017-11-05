package com.budget_app.master;

import com.budget_app.jt_linked_list.*;
import com.budget_app.expenses.*;
import com.budget_app.jt_interfaces.Priceable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.budget_app.error_handler.ErrorHandler;

public class BudgetAppManager 
{
	private static final String EXPENSES_FILE = "res/data/expenses.app";
	private static final String EXPENSE_GROUPS_FILE = "res/data/expense_groups.app";
	private static final String PLANS_FILE = "res/data/plans.app";
	private static final String PURCHASE_HISTORY_FILE = "res/data/purchase_history.app";
	
	private static SortedList expenses = new SortedList();
	private static SortedList expenseGroups = new SortedList();
	private static SortedList plans = new SortedList();
	private static PurchaseHistory purchaseHistory = new PurchaseHistory();

	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//

	public static SortedList getExpenses()
	{
		return expenses;
	}
	
	public static SortedList getExpenseGroups()
	{
		return expenseGroups;
	}
	
	public static SortedList getPlans()
	{
		return plans;
	}

	public static PurchaseHistory getPurchseHistory()
	{
		return purchaseHistory;
	}
	
	//-----------------------------//
	//--- Functionality Methods ---//
	//-----------------------------//
	
	//load data from disk to memory
	public static void importData()
	{
		try 
		{
			BufferedReader br;
			String line;
			
			MessageHandler.printMessage("Importing data...");
			
			//--- Import Expenses ---//
			
			br = new BufferedReader(new FileReader(EXPENSES_FILE));
			line = br.readLine();
			
			while(line != null)
			{
				FileProcessor.processExpenseLine(expenses, line);
				line = br.readLine();
			}
			
			br.close();
			
			//--- END Import Expenses ---//
			
			//--- Import Expense Groups ---//
			
			br = new BufferedReader(new FileReader(EXPENSE_GROUPS_FILE));
			line = br.readLine();
			
			while(line != null)
			{
				FileProcessor.processExpenseGroupLine(expenseGroups, expenses, line);
				line = br.readLine();
			}
			
			br.close();
			
			//--- END Import Expense Groups ---//
			
			//--- Import Plans ---//
			
			br = new BufferedReader(new FileReader(PLANS_FILE));
			line = br.readLine();
			
			while(line != null)
			{
				FileProcessor.processPlanLine(plans, line);
				line = br.readLine();
			}
			
			br.close();
			
			//--- END Import Plans ---//
			
			//--- Import Purchase History ---//
			
			br = new BufferedReader(new FileReader(PURCHASE_HISTORY_FILE));
			line = br.readLine();
			
			while(line != null)
			{
				FileProcessor.processPurchaseLine(purchaseHistory.getPurchases(), expenses, line);
				line = br.readLine();
			}
			
			br.close();
			
			//--- END Import Purchase History ---//
			
			MessageHandler.printMessage("Done!");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//write data from memory to disk
	public static void exportData()
	{
		PrintWriter pr;
		
		try {
			
			MessageHandler.printMessage("Exporting data...");
			
			//--- Export Expenses ---//
			
			pr = new PrintWriter(EXPENSES_FILE, "UTF-8");
			
			FileProcessor.writeListToFile(expenses, pr);
			
			pr.close();
			
			//--- END Export Expenses ---//
			
			//--- Export Expense Groups ---//
			
			pr = new PrintWriter(EXPENSE_GROUPS_FILE, "UTF-8");
			
			FileProcessor.writeListToFile(expenseGroups, pr);
			
			pr.close();
			
			//--- END Export Expense Groups ---//
			
			//--- Export Plans ---//
			
			pr = new PrintWriter(PLANS_FILE, "UTF-8");
			
			FileProcessor.writeListToFile(plans, pr);
			
			pr.close();
			
			//--- END Export Plans ---//
			
			//--- Export Purchase History ---//
			
			pr = new PrintWriter(PURCHASE_HISTORY_FILE, "UTF-8");
			
			FileProcessor.writeListToFile(purchaseHistory.getPurchases(), pr);
			
			pr.close();
			
			//--- END Export Purchase History ---//
			
			MessageHandler.printMessage("Done!");
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}

	
	
	//--- Expense Methods ---//
	
	//add a new Expense to the app
	public static boolean addNewExpense(String name, long price, String category, String description)
	{
		Expense expense;
		boolean added = false;
		
		if(!name.equals("") && price > 0)
		{
			expense = new Expense(name, price);
			
			if(!category.equals(""))
				expense.setCategory(category);
			
			if(!description.equals(""))
				expense.setDescription(description);
			
			if(expenses.findNode(expense) == null)
			{
				expenses.insertSorted(expense);
				added = true;
				MessageHandler.printMessage("Expense '" + name + "' successfully added!");
			}
			else
				MessageHandler.printMessage("Expense '" + name + "' already exists!");
			
		}
		else
			MessageHandler.printMessage("Both [Name] and [Price] fields must be specified. Please specify both fields.");
		
		return added;
	}

	//removes an expense from the app; cascades to expenseGroups as well
	public static boolean removeExpense(Expense expense)
	{
		boolean removed;
		Node currExpenseGroup;
		SortedList expensesInGroup;
		
		//remove expense from 'expenses' list
		removed = (expenses.removeNode(expense) != null);
		
		if(removed)
		{
			currExpenseGroup = expenseGroups.getHead();
			
			//go through each expense group
			while(currExpenseGroup != null)
			{
				if(currExpenseGroup.getItem() instanceof ExpenseGroup)
				{
					expensesInGroup = ((ExpenseGroup)currExpenseGroup.getItem()).getExpenses();
					
					//remove the expense from each expense group's list of expenses
					expensesInGroup.removeNode(expense);
				}
				else
					ErrorHandler.printFailedDowncastErr("NodeItem", BudgetAppManager.class, "removeExpense()");
				
				currExpenseGroup = currExpenseGroup.getNext();
			}
		}
		
		if(removed)
			MessageHandler.printMessage("Successfully removed Expense!");
		
		return removed;
	}
	
	//edits the data of an existing expense
	public static boolean editExpense(Expense expense, String name, long price, String category, String description)
	{
		boolean edited = false;
		Node node = expenses.findNode(expense);
		Expense actualExpense;
		
		if(node != null)
		{
			if(node.getItem() instanceof Expense)
			{
				actualExpense = (Expense)node.getItem();
				
				actualExpense.setName(name);
				actualExpense.setPrice(price);
				actualExpense.setCategory(category);
				actualExpense.setDescription(description);
				edited = true;
				MessageHandler.printMessage("Successfully edited Expense!");
			}
			else
				ErrorHandler.printFailedDowncastErr("NodeItem", BudgetAppManager.class, "editExpense()");
		}
		else
			ErrorHandler.printNullPointerErr("node", BudgetAppManager.class, "editExpense()");
		
		return edited;
	}
	
	//--- END Expense Methods ---//
	
	
	
	//--- Expense Group Methods ---//
	
	//adds an existing Expense to an ExpenseGroup
	public static boolean addExpenseToGroup(ExpenseGroup expenseGroup, Expense expense)
	{
		boolean added = false;
		ExpenseGroup actualGroup = null;
		Expense actualExpense = null;
		Node temp;
		
		//get actual ExpenseGroup
		temp = expenseGroups.findNode(expenseGroup);
		if(temp == null)
			return false;
		
		if(temp.getItem() instanceof ExpenseGroup)
			actualGroup = (ExpenseGroup)temp.getItem();
		else
			ErrorHandler.printFailedDowncastErr("NodeItem", BudgetAppManager.class, "addExpenseToGroup()");
		
		//get actual Expense
		temp = expenses.findNode(expense);
		if(temp.getItem() instanceof Expense)
			actualExpense = (Expense)temp.getItem();
		else
			ErrorHandler.printFailedDowncastErr("NodeItem", BudgetAppManager.class, "addExpenseToGroup()");
		
		//check if data actually exists in the app
		if(actualGroup != null && actualExpense != null)
		{
			//check if it already exists in the list
			if(actualGroup.getExpenses().findNode(actualExpense) == null)
			{
				//insert the expense into the list
				actualGroup.getExpenses().insertSorted(actualExpense);
				added = true;
//				MessageHandler.printMessage("Expense '" + actualExpense.getName() + "' successfully added to Expense Group '" + actualGroup.getName() +"'!");
			}
			else
				MessageHandler.printMessage("Expense '" + actualExpense.getName() + "' is already in the group '" + actualGroup.getName() + "'!");
		}
		else
		{
			ErrorHandler.printNullPointerErr("actualGroup OR actualExpense", BudgetAppManager.class, "addExpenseToGroup()");
//			MessageHandler.printMessage("Failed to add Expense to Expense Group.");
		}
		
		return added;
	}

	//add a new Expense Group to the app
	public static boolean addNewExpenseGroup(String name, String category, String description, SortedList expensesInGroup)
	{
		boolean added = false;
		ExpenseGroup expenseGroup;
		Node currExpense, actualExpense;
		
		expenseGroup = new ExpenseGroup(name, category, description);
		
		if(expenseGroups.findNode(expenseGroup) == null)
		{
			currExpense = expensesInGroup.getHead();
			
			while(currExpense != null)
			{
				//safe downcast
				if(currExpense.getItem() instanceof Expense)
				{
					//get the actual expense from list in memory
					actualExpense = expenses.findNode((Expense)currExpense.getItem());
					if(actualExpense.getItem() instanceof Expense)
					{
						//check if expense is already in list of expenses in expense group
						if(expenseGroup.getExpenses().findNode((Expense)actualExpense.getItem()) == null)
						{
							//add the expense
							expenseGroup.addExpense((Expense)actualExpense.getItem());
							added = true;
						}
						else
							MessageHandler.printMessage("Expense '" + ((Expense)actualExpense.getItem()).getName() + "' already exists in Expense Group!");
					}
					else
						ErrorHandler.printFailedDowncastErr("NodeItem", BudgetAppManager.class, "addNewExpenseGroup()");
				}
				else
					ErrorHandler.printFailedDowncastErr("NodeItem", BudgetAppManager.class, "addNewExpenseGroup()");
				
				currExpense = currExpense.getNext();
			}
			
			expenseGroups.insertSorted(expenseGroup);
		}
		
		return added;
	}
	
	//remove an expense group from the app
	public static boolean removeExpenseGroup(ExpenseGroup expenseGroup)
	{
		boolean removed;
		
		removed = (expenseGroups.removeNode(expenseGroup) != null);
		
		if(removed)
			MessageHandler.printMessage("Successfully removed Expense Group!");
		
		return removed;
	}
	
	//remove an expense from an expense group
	public static boolean removeExpenseFromGroup(ExpenseGroup expenseGroup, Expense expense)
	{
		boolean removed = false;
		ExpenseGroup actualExpenseGroup;
		Node nodeExpenseGroup;
		
		nodeExpenseGroup = expenseGroups.findNode(expenseGroup);
		
		if(nodeExpenseGroup != null)
		{
			if(nodeExpenseGroup.getItem() instanceof ExpenseGroup)
			{
				actualExpenseGroup = (ExpenseGroup)nodeExpenseGroup.getItem();
				
				//actually try to remove the expense
				removed = (actualExpenseGroup.getExpenses().removeNode(expense) != null);
			}
			else
				ErrorHandler.printFailedDowncastErr("NodeItem", BudgetAppManager.class, "removeExpenseFromGroup");
		}
		else
			ErrorHandler.printNullPointerErr("nodeExpenseGroup", BudgetAppManager.class, "removeExpenseFromGroup");
		
		return removed;
	}
	
	//--- END Expense Group Methods ---//
	
	
	
	//--- Purchase Methods ---//
	
	//make a purchase
	public static boolean makePurchase(Priceable item, int quantity)
	{
		Priceable actualItem = null;
		
		if(item instanceof Expense)
			actualItem = (Expense)expenses.findNode((Expense)item).getItem();
		else if(item instanceof ExpenseGroup)
			actualItem = (ExpenseGroup)expenseGroups.findNode((ExpenseGroup)item).getItem();
		
		purchaseHistory.addPurchase(new Purchase(actualItem, quantity));
		
		MessageHandler.printMessage("Purchase successful!");
		
		return true;
	}
	
	//remove a purchase
	public static boolean deletePurchase(Purchase purchase)
	{
		boolean found = purchaseHistory.removePurchase(purchase);
		
		if(found)
			MessageHandler.printMessage("Purchase deleted.");
		else
			MessageHandler.printMessage("Failed to delete Purchase.");
		
		return (found);
	}
	
	//remove a list of purchases; returns 0 if all removed - otherwise returns # of items not removed
	public static int deletePurchases(SortedList purchases)
	{
		int itemsNotRemoved = purchases.getSize();
		Node curr = purchases.getHead();
		
		while(curr != null)
		{
			if(curr.getItem() instanceof Purchase)
			{
				if(purchaseHistory.removePurchase((Purchase)curr.getItem()))
					itemsNotRemoved--;
			}
			else
				ErrorHandler.printFailedDowncastErr("NodeItem", BudgetAppManager.class, "deletePurchases()");
				
			curr = curr.getNext();
		}
		
		if(itemsNotRemoved == 0)
			MessageHandler.printMessage("Purchases successfully deleted.");
		else
			ErrorHandler.printErr(Integer.toString(itemsNotRemoved) + " items not removed.", BudgetAppManager.class, "deletePurchases()");
		
		return itemsNotRemoved;
	}

	//get the list of purchases from a specified date
	public static SortedList getPurchasesOn(Date date)
	{
		SortedList purchases = purchaseHistory.getPurchasesOn(date);
		
		if(purchases == null)
			MessageHandler.printMessage("No purchases found on that date.");
		
		return purchases;
	}
	
	//get the list of purchases from a specified date range
	public static SortedList getPurchasesBetween(Date min, Date max)
	{
		SortedList purchases = purchaseHistory.getPurchasesBetween(min, max);
		
		if(purchases == null)
			MessageHandler.printMessage("No purchases found between those dates.");
		
		return purchases;
	}
	
	//--- END Purchase Methods ---//
	
	
	
	//print a summary of the current state of the lists
	public static String printSummary()
	{
		String output = "";
		
		output += "--- SUMMARY ---\n\n--- EXPENSES ---\n";
		output += expenses.toString();
		output += "\n--- END EXPENSES ---\n\n--- EXPENSE GROUPS ---\n";
		output += expenseGroups.toString();
		output += "\n--- END EXPENSE GROUPS ---\n\n--- PLANS ---\n";
		output += plans.toString();
		output += "\n--- END PLANS---\n\n--- PURCHASE HISTORY ---\n";
		output += purchaseHistory.toString();
		output += "\n--- END PURCHASE HISTORY ---\n\n--- END SUMMARY ---\n";
		
		return output;
	}

	//----------------------//
	//--- Helper Methods ---//
	//----------------------//	

}

