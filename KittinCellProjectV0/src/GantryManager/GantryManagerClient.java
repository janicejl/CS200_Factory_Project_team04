package GantryManager;

import java.io.*;
import java.net.*;
import java.util.*;

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
	
	public Integer connect()
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
	
	public void run()
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
	
	public synchronized void send()
	{
		try
		{
			out.writeObject(gui.manager);
			out.reset();
		}
		catch(Exception e){}
	}
	
	public synchronized void update()
	{
		try
		{
			GantryManager temp = (GantryManager)in.readObject();
			if(gui.getGantryManager().getGantry().getState().equals("free"))
			{
				gui.getGantryManager().getGantry().setState(temp.getGantry().getState());
				gui.getGantryManager().getGantry().setFeed(temp.getGantry().getFeed());
			}
			//gui.setGantryManager((GantryManager)in.readObject());
		}
		catch(Exception ignore){}
	}
	
	public synchronized void setThread(Thread t)
	{
		thread = t;
	}
}