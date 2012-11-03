package kitAssemblyManager; //this package will be renamed in v1 as the clients will be merged into the kitAssemblyManager

import java.io.*;
import java.net.*;

import server.KitAssemblyManager;
import server.KitRobot;
import server.PartsRobot;

public class PartsManagerClient implements Runnable {
	Socket s;
	KitAssemblyApp app;
	ObjectOutputStream out;
	ObjectInputStream in;
	String command;
	String commandSent;
	String serverName; //keep track of what server to connect to...default localhost
	Thread thread;
	
	public PartsManagerClient(KitAssemblyApp _app){
		app= _app;
		serverName = "localhost";
		command = "";
		commandSent = "";
		thread = new Thread(this, "PartsRobotClient_Thread");
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
			commandSent = "PartsRobot";
			out.writeObject(commandSent); //send to server identifying what client this is
			out.reset();
			command = (String)in.readObject();
			if(command.equals("Confirmed")){
				commandSent = "Confirmed";
				out.writeObject(commandSent);
				out.reset();
				//start
			}
			else{
				//error connection
				System.out.println(command);
				System.exit(1);
			}
			
//**************START CODE***************************
			commandSent = "Received";
			while(true){
				app.setPartsRobot((PartsRobot)in.readObject());
				app.setKitAssemblyManager((KitAssemblyManager)in.readObject());
				out.writeObject(commandSent);
				out.reset();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public synchronized Thread getThread() {
		return thread;
	}

	public synchronized void setThread(Thread thread) {
		this.thread = thread;
	}
}
