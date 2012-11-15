package KitCreationManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import data.Job;
import data.KitInfo;
import data.PartInfo;

import server.KitAssemblyManager;
import server.KitRobot;
import server.PartsRobot;

import kitAssemblyManager.GUIKitAssemblyManager;

public class KitCreationClient implements Runnable{
	KitCreationApp app;
	Socket s;
	ObjectOutputStream out;
	ObjectInputStream in;
	String command;
	String commandSent;
	String serverName; //keep track of what server to connect to...default localhost
	Thread thread;
	
	public KitCreationClient(KitCreationApp _app) {
		app = _app;
		serverName = "localhost";
		command = "";
		commandSent = "Kit Manager";
		thread = new Thread(this, "KitCreationClient_Thread");
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
		try{
			out.writeObject(commandSent);
			out.reset();
			commandSent = "Idle";
			while(true){
				updateThread();
			}
		}catch(Exception e){
			e.getStackTrace();
		}
	}
	
	public synchronized Thread getThread() {
		return thread;
	}
	
	public synchronized void updateThread() {
		try {
			out.writeObject(commandSent);
			out.reset();
			if(commandSent.equals("Update Kits")){
				commandSent = "Idle";
				out.writeObject(app.getKitsList());
				out.reset();
				command = (String) in.readObject();
				if(command.equals("Received")){
					
				}
			}
			else if(commandSent.equals("Idle")){
				
			}
			command=(String)in.readObject();
			if(command.equals("Update Parts")){
				app.setPartsList((Vector<PartInfo>)in.readObject());
				out.writeObject("Received");
				out.reset();
			}
			else if(command.equals("Idle")){
				
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
