package GantryManager;

import java.io.*;
import java.net.*;
import java.util.*;

//Client class for connecting to the server
public class GantryManagerClient implements Runnable
{
	Socket s;
	GUIGantryManager gui;
	ObjectOutputStream out;
	ObjectInputStream in;
	String command;
	String commandSent;
	String serverName;
	Thread thread;
	
	public GantryManagerClient(GUIGantryManager g)
	{
		gui = g;
		serverName = "localhost";
		command = "";
		commandSent = "";
		thread = new Thread(this,"GantryManagerClient_Thread");
	}
	
	public synchronized Integer connect()
	{
		try
		{
			s = new Socket(serverName, 61337);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
		}
		catch(UnknownHostException e)
		{return -1;}
		catch(IOException e)
		{return -1;}
		System.out.println("Connected to " + serverName);
		return 1;
	}
	
	public synchronized void run()
	{
		try
		{
			commandSent = "Gantry Manager";
			out.writeObject(commandSent);
			out.reset();		
		}
		catch(IOException e)
		{}
		catch(Exception i)
		{}
	}
	
	public synchronized Thread getThread()
	{
		return thread;
	}

	public synchronized void update()
	{
		try
		{
			//Reads in the new gantry manager and sets it as the applications current one
			gui.setGantryManager((GantryManager)in.readObject());
		}
		catch(Exception ignore){}
	}
	
	public synchronized void setThread(Thread t)
	{
		thread = t;
	}
}