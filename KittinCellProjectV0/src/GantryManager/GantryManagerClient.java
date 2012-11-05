package GantryManager;

import java.io.*;
import java.net.*;

public class GantryManagerClient implements Runnable
{
	GantryManager manager;
	Socket s;
	ObjectOutputStream out;
	ObjectInputStream in;
	String command;
	String commandSent;
	String serverName;
	Thread thread;
	
	public GantryManagerClient(GantryManager gm)
	{
		manager = gm;
		serverName = "localhost";
		command = "";
		commandSent = "";
		thread = new Thread(this, "GantryManagerClient_Thread");
	}
		
	public Integer connect()
	{
		try
		{
			s = new Socket(serverName, 69696);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
		}
		catch(UnknownHostException e)
		{
			System.err.println("Can't find server " + serverName);
			return -1;
		}
		catch(IOException e)
		{
			System.err.println("Can't find server " + serverName);
			return -1;
		}
		System.out.println("Connected to " + serverName);
		return 1;
	}
	
	public void run()
	{
		try
		{
			commandSent = "GantryManager";
			out.writeObject(commandSent);
			out.reset();
			command = (String)in.readObject();
			if(command.equals("Confirmed"))
			{
				commandSent = "Confirmed";
				out.writeObject(commandSent);
				out.reset();
			}
			else if(command.equals("Denied"))
			{
				System.err.println("Server not accepting this Client");
				System.exit(1);
			}
			else
			{
				System.out.println(command);
				System.exit(1);
			}
			
			commandSent = "Received";
			while(true)
			{
				manager.setGantryRobot((Gantry)in.readObject());
				out.writeObject(commandSent);
				out.reset();
			}
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public synchronized Thread getThread()
	{
		return thread;
	}
		
	public synchronized void setThread(Thread thread)
	{
		this.thread = thread;
	}
}