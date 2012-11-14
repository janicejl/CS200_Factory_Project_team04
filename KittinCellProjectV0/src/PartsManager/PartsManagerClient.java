package PartsManager;

import data.Part;
import java.io.*;
import java.net.*;

public class PartsManagerClient implements Runnable{
	
	PartsManagerApp app;
	Socket s;
	String command;
	String commandSent;
	String serverName;
	Thread thread;
	ObjectOutputStream out;
	ObjectInputStream in;
	
	public PartsManagerClient(PartsManagerApp _app){
		app = _app;
		serverName = "localhost";
		command = "";
		commandSent = "";
		thread = new Thread(this,"PartsManagerClient_Thread");
		//thread.run();
	}
	
	public Integer connect(){
		try
		{
			s = new Socket(serverName, 61337);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
		}
		catch(UnknownHostException e){
			return -1;
		}
		catch(IOException e){
			return -1;
		}
		System.out.println("Connected to " + serverName);
		return 1;
	}
	
	public void run(){
		if(connect().equals(1)){
			try{
				commandSent = "Part Manager";
				out.writeObject(commandSent);
				out.reset();
				command = (String)in.readObject();
				if(command.equals("Part Manager Protocol")){
					while(true){
						//constantly send the list of part to server
						out.writeObject(app.getPartsList());
						out.reset();
					}
				}
			}catch(Exception e){
				e.getStackTrace();
			}
		}
	}
	
	
	public synchronized Thread getThread(){
		return  thread;
	}
	
	public synchronized void setThread(Thread _thread){
		thread = _thread;
	}
	
}
