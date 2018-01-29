package com.budget_app.jt_linked_list;

import java.io.Serializable;

public class Node implements Serializable
{

	private Node next;
	private Node prev;
	private NodeItem item;
	
	//--------------------//
	//--- Constructors ---//
	//--------------------//
	
	//null constructor
	public Node()
	{
		this.next = null;
		this.prev = null;
		this.item = null;
	}
	
	//SHALLOW copy constructor
	public Node(Node other)
	{
		this.next = other.next;
		this.prev = other.prev;
		this.item = other.item;
	}
	
	//NodeItem constructor
	public Node(NodeItem item)
	{
		this.next = null;
		this.prev = null;
		setItem(item);
	}
	
	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//
	
	public Node getNext()
	{
		return this.next;
	}
	
	public Node getPrev()
	{
		return this.prev;
	}
	
	public NodeItem getItem() 
	{
		return this.item;
	}

	public void setItem(NodeItem item) 
	{
		this.item = item;
	}

	public void setNext(Node next) 
	{
		this.next = next;
	}

	public void setPrev(Node prev) 
	{
		this.prev = prev;
	}
	
}
