package com.budget_app.jt_linked_list;

import com.budget_app.error_handler.ErrorHandler;
import com.budget_app.jt_interfaces.Compareable;

public class LinkedList implements Compareable
{
	private Node head;
	private Node tail;
	private int size;
	
	//--------------------//
	//--- Constructors ---//
	//--------------------//
	
	//null constructor
	public LinkedList() 	
	{
		
		this.head = null;
		this.tail = null;
		this.size = 0;
		
	} //END LinkedList null constructor
	
	//SHALLOW copy constructor
	public LinkedList(LinkedList other)
	{
		
		this.head = other.head;
		this.tail = other.tail;
		this.size = other.size;
		
	} //END LinkedList copy constructor
	
	//SHALLOW node constructor - goes back prev as far as possible and creates (wraps) list starting from there
	public LinkedList(Node node)
	{
		Node curr = node;
		
		if(curr != null)
		{
			while (curr.getPrev() != null)
			{
				curr = curr.getPrev();
			}
			
			this.head = curr;
			
			while(curr != null)
			{
				curr = curr.getNext();
				this.size++;
			}
			
			this.tail = curr;
		}
		else
		{
			this.head = null;
			this.tail = null;
			this.size = 0;
		}
		
	} //END LinkedList node constructor
	
	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//
	
	public Node getHead()
	{
		return this.head;
	}
	
	public void setHead(Node head)
	{
		this.head = head;
	}
	
	public Node getTail()
	{
		return this.tail;
	}
	
	public void setTail(Node tail)
	{
		this.tail = tail;
	}
	
	public int getSize()
	{
		return this.size;
	}
	
	public void setSize(int size)
	{
		this.size = size;
	}
	
	//---------------------------//
	//--- Implemented Methods ---//
	//---------------------------//
	
	//just checks for head pointer, tail pointer, and size equality
	@Override
	public boolean equals(Compareable other) 
	{
		LinkedList temp;
		
		if(other instanceof LinkedList)
		{
			temp = (LinkedList)other;
			return (this.head == temp.getHead() && this.tail == temp.getTail() && this.size == temp.getSize());
		}
		else
			ErrorHandler.printFailedDowncastErr("Compareable", this, "equals()");
		
		return false;
	}

	//returns the difference in list sizes
	@Override
	public int compare(Compareable other) 
	{
		LinkedList temp;
		
		if (other instanceof LinkedList)
		{
			temp = (LinkedList)other;
			return (this.size - temp.getSize());
		}
		else
			ErrorHandler.printFailedDowncastErr("Compareable", this, "compare()");
		
		return 0;
	}
	
	//-----------------------------//
	//--- Functionality Methods ---//
	//-----------------------------//
	
	//insert to front of the list
	public void insertFront(NodeItem item)
	{
		
		Node node = NodeFactory.makeNode(item);
		Node prev = null;
		
		if (head == null)
		{
			this.head = node;
			this.tail = node;
			this.size = 1;
		}
		else
		{
			//check if there is a prev behind head (for partial lists that wrap a smaller portion of a larger list)
			prev = this.head.getPrev();
			if(prev != null)
			{
				prev.setNext(node);
				node.setPrev(prev);
			}
			
			node.setNext(this.head);
			this.head.setPrev(node);
			this.head = node;
			this.size++;
		}
		
	} //END insertFront
	
	//insert to back of the list
	public void insertBack(NodeItem item)
	{
		
		Node node = NodeFactory.makeNode(item);
		Node next;
		
		if (this.tail == null)
		{
			this.head = node;
			this.tail = node;
			this.size = 1;
		}
		else
		{
			//check if there is a next ahead of tail (for partial lists that wrap a smaller portion of a larger list)
			next = this.tail.getNext();
			if(next != null)
			{
				next.setPrev(node);
				node.setNext(next);
			}
			
			this.tail.setNext(node);
			node.setPrev(this.tail);
			this.tail = node;
			this.size++;
		}
		
	} //END insertBack
		
	//remove Node from front of list
	public Node removeFront()
	{
		Node remove = null;
//		Node prev;
		
		if (this.head != null)
		{
			remove = this.head;
			detachNode(remove);
//			
//			prev = this.head.getPrev();
//			if(prev != null)
//			{
//				prev.setNext(this.head.getNext());	
//			}
//			this.head = this.head.getNext();
//			remove.setNext(null);
//			this.head.setPrev(prev);
			
			size--;
		}
		
		return remove;
		
	} //END removeFront
	
	//remove Node from back of list
	public Node removeBack()
	{
		Node remove = null;
		
		if (this.tail != null)
		{
			remove = this.tail;
			detachNode(remove);
//			this.tail = this.tail.getPrev();
//			
//			remove.setPrev(null);
//			this.tail.setNext(null);
			
			size--;
		}
		
		return remove;
		
	} //END removeBack
	
	//search for Node and return it, if it exists
	public Node findNode(NodeItem search)
	{
		Node find = null;
		Node curr = head;
		
		while (curr != null && find == null)
		{
			if (curr.getItem().equals(search))				
				find = curr;
			
			curr = curr.getNext();
		}
		
		return find;
	
	} //END findNod
	
	//search for Node and remove & return it, if it exists
	public Node removeNode(NodeItem search)
	{
		Node remove = null;
		Node find = findNode(search);
		
		if(find != null)
			detachNode(find);
		
		remove = find;
		
		return remove;
	
	} //END removeNode
	
	//wrap this list around other list
	public void wrap(LinkedList other)
	{
		this.head = other.head;
		this.tail = other.tail;
		this.size = other.size;
	} //END wrap
	
	//return human-readable representation of list
	@Override
	public String toString()
	{
		String output = "";
		Node curr = this.head;
		
		while (curr != null)
		{
			output += curr.getItem().toString() + "\n";
			curr = curr.getNext();
		}
		
		return output;
				
	}
	
	//----------------------//
	//--- Helper Methods ---//
	//----------------------//
	
	//detach a Node from the list
	protected void detachNode(Node detach)
	{
		Node prev, next;
		
		if (detach != null)
		{
			//adjust links to remove Node
			prev = detach.getPrev();
			next = detach.getNext();
			
			if (prev != null)
				prev.setNext(next);
			
			if(next != null)
				next.setPrev(prev);
			
			//set head/tail if necessary
			if(detach == this.head)
				this.head = this.head.getNext();
			
			if(detach == this.tail)
				this.tail = this.tail.getPrev();
			
			//detach Node from list
			detach.setNext(null);
			detach.setPrev(null);
		}
		else
			ErrorHandler.printNullPointerErr("detach", this, "detachNode()");
		
	} //END detachNode
	
}
