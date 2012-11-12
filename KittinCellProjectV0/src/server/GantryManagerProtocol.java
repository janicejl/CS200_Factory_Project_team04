package server;

import java.io.*;
import java.net.*;
import GantryManager.GantryManager;

public class GantryManagerProtocol implements Runnable
{
	Server app;
	Socket s;
	ObjectOutputStream out;
	ObjectInputStream in;
	String command;
	String commandSent;
	String type;
	Thread thread;
	
	public GantryManagerProtocol(Socket _s, Server _app)
	{
		System.out.println("WORKING");
		app = _app;
		s = _s;
		command = "";
		commandSent = "";
		try
		{
			out = new ObjectOutputStream(s.getOutputStream());
			out.flush();
			in = new ObjectInputStream(s.getInputStream());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		thread = new Thread(this,"GantryManagerClient_Thread");
		thread.start();
	}
	
	public void run()
	{
		try
		{
			System.out.println("HAR");
			command = (String)in.readObject();
			if(!command.equals("Gantry Manager"))
			{
				commandSent = "Denied";
				System.exit(1);
			}
			System.out.println("HERE");
			out.writeObject("Confirmed");
			out.reset();
			command = (String)in.readObject();
			if(command.equals("Confirmed"))
			{
				
			}
			commandSent = "Received";
			while(true)
			{
				System.out.println("WHERE IS D");
				out.writeObject(app.getGantryManager());
				out.reset();
				command = (String)in.readObject();
			}
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}