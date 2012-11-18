package productionManager;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import data.Job;
import data.KitInfo;


public class ProductionClient implements Runnable{
	Socket s;
	ProductionManagerApp app;
	ObjectOutputStream out;
	ObjectInputStream in;
	String command;
	String commandSent;
	String serverName; //keep track of what server to connect to...default localhost
	Thread thread;
	
	 //data received from server
	
	public ProductionClient(ProductionManagerApp _app){
		serverName = "localhost";
		command = "";
		commandSent = "Production Manager";
		app = _app;
		thread = new Thread(this, "ProductionClient_Thread");
		
	}

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

	public void run(){
		try {
			out.writeObject(commandSent); //send to server identifying what client this is
			out.reset();
			commandSent = "Idle";
			/*
			command = (String)in.readObject();
			if(command.equals("Confirmed")){
				//start
				out.writeObject("Confirmed");
				out.reset();
			}
			else{
				//error connection
				System.out.println(command);
				System.exit(1);
			}
			
//**************START CODE***************************
			
			
			commandSent = "Received";*/
			/*while(true){
				app.setLanes((ArrayList<Lane>)in.readObject());
				app.setFeeders((ArrayList<Feeder>)in.readObject());
				app.setNests((ArrayList<Nest>)in.readObject());
				out.writeObject(commandSent);
				out.reset();
			}*/
			
			/*commandSent = "idle";
			while(true){
				
				out.writeObject(commandSent);
				out.reset();
				command = (String)in.readObject();
				if(command.equals("idle")){
					commandSent = "idle";
					continue;
				}
				else if(command.equals("start")){
					break;
				}
			}
			commandSent = "idle";
			
			while(true){
				out.writeObject(commandSent);
				out.reset();
				command = (String)in.readObject();
				if(command.equals("idle")){
					continue;
				}
				else if(command.equals("work")){
					
					//***** code for pass the data to graphics
					
					//***** code for graphics to update feederData
					commandSent = "working";
					
					
					
				}
			}
			*/
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public synchronized Thread getThread() {
		return thread;
	}	
	
	public synchronized void updateThread(){
		try {
			
			if(commandSent.equals("Update")){
				out.writeObject(commandSent);
				out.reset();
				commandSent = "Idle";
				out.writeObject(app.getJobs());
				out.reset();
			}
			else if(commandSent.equals("Idle")){
				out.writeObject(commandSent);
				out.reset();
			}
			command = (String)in.readObject();
			if(command.equals("Idle")){
			}
			else if(command.equals("Update Kits")){
				app.setkitsList((ArrayList<KitInfo>)in.readObject());
				out.writeObject("Received");
				out.reset();
			}
			else if(command.equals("Update Jobs")){
				app.setJobs((ArrayList<Job>)in.readObject());
				out.writeObject("Received");
				out.reset();
			}
		} catch (Exception ignore){
			ignore.printStackTrace();
			System.exit(1);
		}
	}

	public synchronized void setThread(Thread thread) {
		this.thread = thread;
	}

	public synchronized String getCommand() {
		return command;
	}

	public synchronized void setCommand(String command) {
		this.command = command;
	}

	public synchronized String getCommandSent() {
		return commandSent;
	}

	public synchronized void setCommandSent(String commandSent) {
		this.commandSent = commandSent;
	}
}
