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
			Thread.sleep(20);
			String t = "0";
			commandSent = "Received";
			t = (String)in.readObject();
			int h = (Integer)in.readObject();
			if(!t.equals("null") && !t.equals("0"))
			{
				System.out.println(t);
				gui.manager.getGantry().setState(t);
			}
			System.out.println(h);
			gui.manager.getGantry().setFeed(h);
			gui.manager.update();
			//GantryManager temp = (GantryManager)in.readObject();
			//System.out.println(temp.getGantry().getState());
			//gui.manager.getGantry().setState(temp.getGantry().getState());
			//gui.manager.getGantry().setFeed(temp.getGantry().getFeed());
		}
		catch(Exception ignore){}
	}
	
	public synchronized void setThread(Thread t)
	{
		thread = t;
	}
}