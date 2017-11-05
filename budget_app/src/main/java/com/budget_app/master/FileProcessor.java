package com.budget_app.master;

import com.budget_app.expenses.Expense;
import com.budget_app.expenses.ExpenseGroup;
import com.budget_app.expenses.Purchase;
import com.budget_app.jt_interfaces.CSVExportable;
import com.budget_app.jt_interfaces.Priceable;
import com.budget_app.jt_linked_list.LinkedList;
import com.budget_app.jt_linked_list.Node;
import com.budget_app.jt_linked_list.SortedList;
import com.budget_app.jt_linked_list.StringItem;
import com.budget_app.plans.*;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import com.budget_app.error_handler.ErrorHandler;

public class FileProcessor 
{

	//processes a line containing Expense info
	public static void processExpenseLine(SortedList expenses, String line)
	{
		Expense expense;
		
		String fields[] = new String[4];
		String field = "";
		int fieldsIndex = 0;
		char curr;
		boolean inString = false;
		
		for(int i = 0; i < line.length(); i++)
		{
			curr = line.charAt(i);
			
			if(curr == '"')
			{
				inString = !inString;
				continue;
			}
			
			if(!inString)
			{
				if (curr == ',' || curr == ';')
				{
					fields[fieldsIndex] = field;
					field = "";
					fieldsIndex++;
					continue;
				}
			}
			
			field += curr;
		}
		
		expense = new Expense(fields[0], Long.parseLong(fields[1]), fields[2], fields[3]);
		
		expenses.insertSorted(expense);
		
	}
	
	//processes a line containing ExpenseGroup info
	public static void processExpenseGroupLine(SortedList expenseGroups, SortedList expenses, String line)
	{
		ExpenseGroup expenseGroup = new ExpenseGroup();
		
		LinkedList fields = new LinkedList();
		String field = "";
		char curr;
		boolean inString = false;
		
		for(int i = 0; i < line.length(); i++)
		{
			curr = line.charAt(i);
			
			if(curr == '"')
			{
				inString = !inString;
				continue;
			}
			
			if(!inString)
			{
				if (curr == ',' || curr == ';')
				{
					fields.insertBack(new StringItem(field));
					field = "";
					continue;
				}
			}
			
			field += curr;
		}
		
		populateExpenseGroup(expenseGroup, expenses, fields);
		
		expenseGroups.insertSorted(expenseGroup);
	}
	
	//helper method for processExpenseGroupLine; populates the expenseGroup's list of expenses
	private static void populateExpenseGroup(ExpenseGroup group, SortedList expenses, LinkedList fields)
	{
		String expenseGroupName;
		String expenseGroupCategory;
		String expenseGroupDescription;
		
		String expenseName;
		
		Expense temp;
		SortedList expensesInGroup = new SortedList();
		Node curr = fields.getHead();
		
		expenseGroupName = curr.getItem().toString();
		curr = curr.getNext();
		
		expenseGroupCategory = curr.getItem().toString();
		curr = curr.getNext();
		
		expenseGroupDescription = curr.getItem().toString();
		curr = curr.getNext();
		
		while(curr != null)
		{
			expenseName = curr.getItem().toString();
			temp = (Expense)expenses.findNode(new Expense(expenseName)).getItem();
			expensesInGroup.insertSorted(temp);
			
			curr = curr.getNext();
		}
		
		group.setName(expenseGroupName);
		group.setCategory(expenseGroupCategory);
		group.setDescription(expenseGroupDescription);
		group.setExpenses(expensesInGroup);
		group.updatePrice();
	}
	
	//processes a line containing Plan info
	public static void processPlanLine(SortedList plans, String line)
	{
		Plan plan;
		
		String fields[] = new String[3];
		String field = "";
		int fieldsIndex = 0;
		char curr;
		boolean inString = false;
		
		for(int i = 0; i < line.length(); i++)
		{
			curr = line.charAt(i);
			
			if(curr == '"')
			{
				inString = !inString;
				continue;
			}
			
			if(!inString)
			{
				if (curr == ',' || curr == ';')
				{
					fields[fieldsIndex] = field;
					field = "";
					fieldsIndex++;
					continue;
				}
			}
			
			field += curr;
		}
		
		plan = new Plan(fields[0], Long.parseLong(fields[1]), Long.parseLong(fields[2]));
		
		plans.insertSorted(plan);
	}
	
	//process a line containing PurchaseHistory info
	public static void processPurchaseLine(SortedList purchaseHistory, SortedList expenses, String line)
	{
		Node purchaseItem;
		Priceable item = null;
		
		String fields[] = new String[4];
		String field = "";
		int fieldsIndex = 0;
		char curr;
		boolean inString = false;
		
		for(int i = 0; i < line.length(); i++)
		{
			curr = line.charAt(i);
			
			if(curr == '"')
			{
				inString = !inString;
				continue;
			}
			
			if(!inString)
			{
				if (curr == ',' || curr == ';')
				{
					fields[fieldsIndex] = field;
					field = "";
					fieldsIndex++;
					continue;
				}
			}
			
			field += curr;
		}
		
		purchaseItem = expenses.findNode(new Expense(fields[0]));
		
		if(purchaseItem != null)
		{
			if(purchaseItem.getItem() instanceof Expense)
				item = (Expense)purchaseItem.getItem();
			else if(purchaseItem.getItem() instanceof ExpenseGroup)
				item = (ExpenseGroup)purchaseItem.getItem();
			else
				ErrorHandler.printFailedDowncastErr("NodeItem", FileProcessor.class, "processPurchaseLine()");
			
			if(item != null)
			{
				try {
					purchaseHistory.insertSorted(new Purchase(item, Integer.parseInt(fields[2]), DateFormat.getDateInstance().parse(fields[3])));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			else
				ErrorHandler.printNullPointerErr("item", FileProcessor.class, "processPurchaseLine()");
		}
		else
			ErrorHandler.printErr("Priceable item not found!", FileProcessor.class, "processPurchaseLine()");
		
	}

	//write contents of a SortedList that contains CSVExportable items to disk
	public static void writeListToFile(SortedList list, PrintWriter pr)
	{
		Node curr = list.getHead();
		String output;
		CSVExportable exportable;
		
		while(curr != null)
		{
			if(curr.getItem() instanceof CSVExportable)
			{
				exportable = (CSVExportable)curr.getItem();
				
				output = exportable.toString_CSV();
				pr.println(output);
			}
			else
				ErrorHandler.printFailedDowncastErr("NodeItem", FileProcessor.class, "writeListToFile()");
		
			curr = curr.getNext();
		}
		
	}
	
}
