package com.budget_app.error_handler;

public class ErrorHandler 
{
	
	public static void printFailedDowncastErr(String base_type, Object object, String method_name)
	{
		System.err.println("ERROR: Failed to downcast " + base_type + " in " + object.getClass().getName() + "." + 
							object.getClass().getPackage().getName() + "." + method_name);
		System.exit(-1);
	}
	
	public static void printNullPointerErr(String var_name, Object object, String method_name)
	{
		System.err.println("ERROR: " + var_name + " is a null pointer in " + object.getClass().getName() + "." + 
				object.getClass().getPackage().getName() + "." + method_name);
	}
	
	public static void printUnexpectedErr(String err_msg, Object object, String method_name)
	{
		System.err.println("UNEXPECTED ERROR: " + err_msg + " in " + object.getClass().getName() + "." + 
				object.getClass().getPackage().getName() + "." + method_name);
	}
	
	public static void printErr(String err_msg, Object object, String method_name)
	{
		System.err.println("ERROR: " + err_msg + " in " + object.getClass().getName() + "." + 
				object.getClass().getPackage().getName() + "." + method_name);
	}
	
}

//--------------------//
//--- Constructors ---//
//--------------------//



//-------------------------//
//--- Getters & Setters ---//
//-------------------------//



//---------------------------//
//--- Implemented Methods ---//
//---------------------------//



//-----------------------------//
//--- Functionality Methods ---//
//-----------------------------//



//----------------------//
//--- Helper Methods ---//
//----------------------//





//------------------------//
//--- Abstract Methods ---//
//------------------------//
