package GantryManager;

import java.io.*;
import java.net.*;
import java.util.*;

//Client class for connecting to the server
public class GantryManagerClient implements Runnable
{
	Socket s;
	GantryManagerApp app;
	ObjectOutputStream out;
	ObjectInputStream in;
	String command;
	String commandSent;
	String serverName;
	Thread thread;
	
	public GantryManagerClient(GantryManagerApp _app)
	{
		app = _app;
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
		System.out.println("Gantry Manager connected to " + serverName);
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
		{
			e.printStackTrace();
		}
		catch(Exception i)
		{
			i.printStackTrace();
		}
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
			app.setGantryManager((GantryManager)in.readObject());
		}
		catch(Exception ignore){
			ignore.printStackTrace();
			System.exit(1);
		}
	}
	
	public synchronized void setThread(Thread t)
	{
		thread = t;
	}
}