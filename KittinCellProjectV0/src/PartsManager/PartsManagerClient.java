package PartsManager;

import data.Part;
import data.PartInfo;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

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
		commandSent = "Part Manager";
		thread = new Thread(this,"PartsManagerClient_Thread");
		//thread.run();
	}
	
	// set up connection with server
	public Integer connect(){
		try {
			s = new Socket(serverName, 61337); //attempt to connect to servername
			out = new ObjectOutputStream(s.getOutputStream()); //output stream
			in = new ObjectInputStream(s.getInputStream()); //input stream
		} catch (UnknownHostException e) {
			System.err.println("Can't find server " + serverName);
            return -1;
		} catch (IOException e) {
			System.err.println("Can't find server " + serverName);
            return -1;
		}
		System.out.println("Connected to "+ serverName);
		return 1; //successful connection
	}
	
	// constantly send parts data to server
	public void run(){
		try{
			out.writeObject(commandSent);
			out.reset();
			commandSent = "Update Parts";
			while(true){
				updateThread();
			}
		}catch(Exception e){
			e.getStackTrace();
		}
	}
	
	// update the parts data
	public synchronized void updateThread() {
		try {
			if(commandSent.equals("Update Parts")){
				out.writeObject(commandSent);
				out.reset();
				commandSent = "Idle";
				out.writeObject(app.getPartsList());
				out.reset();
				command = (String) in.readObject();
				if(command.equals("Received")){
					
				}
			}
			else if(commandSent.equals("Idle")){	
				out.writeObject(commandSent);
				out.reset();
			}
			
			command=(String)in.readObject();
			if(command.equals("Idle")){
				
			}
		} catch (Exception ignore){
			ignore.printStackTrace();
			System.exit(1);
		}
	}
	
	public synchronized Thread getThread(){
		return  thread;
	}
	
	public synchronized void setThread(Thread _thread){
		thread = _thread;
	}

	public String getCommandSent() {
		return commandSent;
	}

	public void setCommandSent(String commandSent) {
		this.commandSent = commandSent;
	}
	
}
