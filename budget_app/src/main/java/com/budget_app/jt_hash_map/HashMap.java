package com.budget_app.jt_hash_map;

public class HashMap 
{
	private static final int DEFAULT_SIZE = 100;
	
	private HashKeyValue[] hashes;
	private int size;
	
	//--------------------//
	//--- Constructors ---//
	//--------------------//

	public HashMap()
	{
		this.size = DEFAULT_SIZE;
		hashes = new HashKeyValue[size];
	}
	
	public HashMap(HashMap other)
	{
		this.size = other.size;
		hashes = new HashKeyValue[size];
	}
	
	public HashMap(int size)
	{
		this.size = size;
		hashes = new HashKeyValue[size];
	}
	
	//-------------------------//
	//--- Getters & Setters ---//
	//-------------------------//

	public HashKeyValue[] getHashes()
	{
		return this.hashes;
	}
	
	public void setHashes(HashKeyValue[] hashes)
	{
		this.hashes = hashes;
	}
	
	public int getSize()
	{
		return this.size;
	}
	
	public void setSize(int size)
	{
		this.size = size;
	}

	//-----------------------------//
	//--- Functionality Methods ---//
	//-----------------------------//

	

	//----------------------//
	//--- Helper Methods ---//
	//----------------------//
	
	private int calculateHash(HashKeyValue obj)
	{
		int hash = 0;
		String key = obj.getKey();
		char curr;
		
		//iterate over each character
		for (int i = 0; i < key.length(); i++)
		{
			//get the char
			curr = key.charAt(i);
			
			//use relative position in alphabet to build hash
			if(curr >= 'a' && curr <= 'z')
				hash += (int)(curr - 'a');
			else if (curr >= 'A' && curr <= 'Z')
				hash += (int)(curr - 'A') + 26;
			else
				hash += (int)curr;
			
		}
		
		return hash;
		
	} //END calculateHash
	
}
