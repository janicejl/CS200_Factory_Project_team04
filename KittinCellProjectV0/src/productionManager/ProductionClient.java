package productionManager;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import data.Job;
import data.KitInfo;


public class ProductionClient implements Runnable{
	Socket s;
	ProductionManagerApp app; //reference to production manager
	ObjectOutputStream out; //output stream
	ObjectInputStream in; //input stream
	String command; //command received from server
	String commandSent; //comand sent to server
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
		System.out.println("Production Manager connected to "+ serverName);
		return 1; //successful connection
	}

	public void run(){
		try {
			out.writeObject(commandSent); //send to server identifying what client this is
			out.reset();
			//update server with jobs
			commandSent = "Update";
			updateThread();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	//update the thread, called only by certain changes in production manager
	public synchronized void updateThread(){
		try {
			//send client command
			if(commandSent.equals("Update")){
				out.writeObject(commandSent);
				out.reset();
				commandSent = "Idle";
				out.writeObject(app.getJobs());
				out.reset();
				System.out.println("Update");
			}
			else if(commandSent.equals("Start")){
				out.writeObject(commandSent);
				out.reset();
				commandSent = "Idle";
				out.writeObject(app.getJobs());
				out.reset();
				System.out.println("Start");
			}
			else if(commandSent.equals("Idle")){
				out.writeObject(commandSent);
				out.reset();
			}
			//receive command
			command = (String)in.readObject();
			if(command.equals("Idle")){
			}
			else if(command.equals("Update Kits")){
				//update kits
				app.setkitsList((ArrayList<KitInfo>)in.readObject());
				out.writeObject("Received");
				out.reset();
			}
			else if(command.equals("Update Jobs")){
				//update jobs
				app.setJobs((ArrayList<Job>)in.readObject());
				out.writeObject("Received");
				out.reset();
			}
		} catch (Exception ignore){
			ignore.printStackTrace();
			System.exit(1);
		}
	}

	public synchronized Thread getThread() {
		return thread;
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
