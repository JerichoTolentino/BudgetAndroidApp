package com.budget_app.app_tester;

import com.budget_app.master.*;
import com.budget_app.expenses.*;
import com.budget_app.jt_linked_list.SortedList;

import java.sql.Date;
import java.time.LocalDateTime;

public class Main 
{
	
	public static void main(String args[])
	{
		BudgetAppManager.importData();

		Expense yummy = (Expense)BudgetAppManager.getExpenses().findNode(new Expense("Sushi Delivery")).getItem();
		BudgetAppManager.makePurchase(yummy, 1);
		
//		if(BudgetAppManager.removeExpenseFromGroup(new ExpenseGroup("NewExpenseGroup"), new Expense("Elephant Cage")))
//			MessageHandler.printMessage("Successfully removed Expense from Expense Group!");
//		else
//			MessageHandler.printMessage("Failed to remove Expense from Expense Group.");
		
		MessageHandler.printMessage(BudgetAppManager.printSummary());
		
		BudgetAppManager.exportData();
	}
	
}

//"Apple",67,"Fruit","A red fruit";

/*	TODO:
 * 		Make methods that directly correspond to user actions from the front-end app interface
 * 
 * 
 * 
 * 
 */
