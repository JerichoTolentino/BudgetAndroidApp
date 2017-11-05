package com.budget_app.jt_linked_list;

import com.budget_app.error_handler.*;

public class NodeFactory 
{
	
	//-----------------------------//
	//--- Functionality Methods ---//
	//-----------------------------//
	
	public static Node makeNode(NodeItem item)
	{
		return new Node(item);
	}
	
	public static Node makeStringNode(String data)
	{
		return new Node(new StringItem(data));
	}
	
	public static Node copyStringNode(Node node)
	{
		NodeItem temp = node.getItem();
		Node copy;
		
		if(temp instanceof StringItem)
		{
			copy = new Node(new StringItem(((StringItem)temp).getData()));
		}
		else
		{
			ErrorHandler.printFailedDowncastErr("NodeItem", NodeFactory.class, "copyStringNode()");
			copy = null;
		}
		
		return copy;
	}
	
	public static Node makeIntNode(int data)
	{
		return new Node(new IntItem(data));
	}
	
	public static Node copyIntNode(Node node)
	{
		NodeItem temp = node.getItem();
		Node copy;
		
		if(temp instanceof IntItem)
		{
			copy = new Node(new IntItem(((IntItem)temp).getData()));
		}
		else
		{
			ErrorHandler.printFailedDowncastErr("NodeItem", NodeFactory.class, "copyIntNode()");
			copy = null;
		}
		
		return copy;
	}
	
}
