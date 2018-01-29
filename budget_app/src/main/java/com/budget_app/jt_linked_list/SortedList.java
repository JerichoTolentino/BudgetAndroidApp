package com.budget_app.jt_linked_list;

import com.budget_app.error_handler.ErrorHandler;

import java.io.Serializable;

public class SortedList extends LinkedList implements Serializable
{
	
	//--------------------//
	//--- Constructors ---//
	//--------------------//

	//null constructor
	public SortedList()
	{
		super();
	}
	
	//SHALLOW copy constructor - sorts list
	public SortedList(LinkedList other)
	{
		super(other);
		sortList();
	}
	
	//SHALLOW node constructor - sorts list
	public SortedList(Node node)
	{
		super(node);
		sortList();
	}

	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//



	//-----------------------------//
	//--- Functionality Methods ---//
	//-----------------------------//
	
	//insert a node in sorted order (ascending)
	public void insertSorted(NodeItem item)
	{
		Node curr = super.getHead();
		Node prev = null;
		boolean added = false;
		
		while (curr != null && !added)
		{
			prev = curr.getPrev();
			if(curr.getItem().compare(item) > 0)
			{
				//manual insert in correct position
				if(prev != null)
				{
					Node node = NodeFactory.makeNode(item);
					
					//attach list to node
					node.setPrev(prev);
					node.setNext(curr);
					
					//attach node to list
					prev.setNext(node);
					curr.setPrev(node);
					super.setSize(getSize() + 1);
				}
				//reuse insertFront() method
				else
					super.insertFront(item);
					
				added = true;
			}
			
			//move next
			prev = curr;
			curr = curr.getNext();
		}
		
		//if all items were less than node
		if (!added)
			super.insertBack(item);
		
	}
	
	//sort all list items in ascending order
	public void sortList()
	{
		Node curr;
		Node max = null;
		LinkedList sorted = new LinkedList();
		
		for(int timesAdded = 0; timesAdded < super.getSize(); timesAdded++)
		{
			max = super.getHead();
			
			if(max != null)
			{
				curr = max.getNext();
				
				//find local max of list
				while(curr != null)
				{
					if(curr.getItem().compare(max.getItem()) > 0)
						max = curr;
					curr = curr.getNext();
				}
				
				if(max == super.getHead())
					super.setHead(super.getHead().getNext());
				
				super.detachNode(max);
				
				sorted.insertFront(max.getItem());
				
			}
			else
				ErrorHandler.printNullPointerErr("max", this, "sortList()");
			
		} //END loop timesAdded

		super.wrap(sorted);
		
	}
	
}
