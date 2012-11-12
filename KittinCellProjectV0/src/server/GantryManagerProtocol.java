package server;

import java.io.*;
import java.net.*;

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
	}
	
	public void run()
	{
		try
		{
			command = (String)in.readObject();
			if(!command.equals("Gantry Manager"))
			{
				commandSent = "Denied";
				System.exit(1);
			}
			out.writeObject("Confirmed");
			out.reset();
			command = (String)in.readObject();
			if(command.equals("Confirmed"))
			{
				
			}
			commandSent = "Received";
			while(true)
			{
				out.writeObject(app.getGantryManager());
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