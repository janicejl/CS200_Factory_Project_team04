package kitAssemblyManager;

import java.io.*;
import java.net.*;

import server.KitAssemblyManager;
import server.KitRobot;
import server.PartsRobot;

public class KitAssemblyClient implements Runnable {
	GUIKitAssemblyManager app;
	protected Socket s;
	protected ObjectOutputStream out;
	protected ObjectInputStream in;
	protected String command;
	protected String commandSent;
	protected String serverName; //keep track of what server to connect to...default localhost
	protected Thread thread;
	
	public KitAssemblyClient(){
		serverName = "localhost";
		command = "";
		commandSent = "";
		thread = new Thread(this, "KitAssemblyClient_Thread");
	}
	
	public KitAssemblyClient(GUIKitAssemblyManager _app){
		app = _app;
		serverName = "localhost";
		command = "";
		commandSent = "Kit Assembly";
		thread = new Thread(this, "KitAssemblyClient_Thread");
	}

	public Integer connect(){
		try {
			s = new Socket(serverName, 61337); //attempt to connect to servername
			out = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream())); //output stream
			in = new ObjectInputStream(new BufferedInputStream(s.getInputStream())); //input stream
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
			out.flush();
		/*	//command = (String)in.readObject();
			if(command.equals("Confirmed")){
				//commandSent = "Confirmed";
				//out.writeObject(commandSent);
				//out.reset();
				//start
			}
			else if(command.equals("Denied")){
				System.err.println("Server not accepting this Client");
				System.exit(1);
			}
			else{
				//error connection
				System.out.println(command);
				System.exit(1);
			}
			
//**************START CODE***************************
			commandSent = "Received";*/
			/*
			while(true){
				app.setKitRobot((KitRobot)in.readObject());
				app.setKitAssemblyManager((KitAssemblyManager)in.readObject());
				out.writeObject(commandSent);
				out.reset();
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
	
	public synchronized void updateThread() {
		try {
			app.setKitRobot((KitRobot)in.readObject());
			app.setPartsRobot((PartsRobot)in.readObject());
			app.setKitAssemblyManager((KitAssemblyManager)in.readObject());
		} catch (Exception ignore){
			ignore.printStackTrace();
			System.exit(1);
		}
	}

	public synchronized void setThread(Thread thread) {
		this.thread = thread;
	}
}