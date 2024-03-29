package productionManager;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import laneManager.Nest;
import Feeder.Feeder;
import GantryManager.GantryManager;

import server.KitAssemblyManager;
import server.KitRobot;
import server.Lane;
import server.PartsRobot;

import data.Job;
import data.KitInfo;


public class GUIProductionClient implements Runnable{
	Socket s;
	GUIProductionManager app; //reference to production manager
	ObjectOutputStream out; //output stream
	ObjectInputStream in; //input stream
	String command; //command received from server
	String commandSent; //comand sent to server
	String serverName; //keep track of what server to connect to...default localhost
	Thread thread;
	
	 //data received from server
	
	public GUIProductionClient(GUIProductionManager _app){
		serverName = "localhost";
		command = "";
		commandSent = "GUI Production Manager";
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
		System.out.println("GUI Production Manager connected to "+ serverName);
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
	public synchronized void updateThread() {
		try {
			app.setKitRobot((KitRobot)in.readObject());
			app.setPartsRobot((PartsRobot)in.readObject());
			app.setKitAssemblyManager((KitAssemblyManager)in.readObject());
			app.setLanes((Vector<Lane>)in.readObject());
			app.setFeeders((Vector<Feeder>)in.readObject());
			app.setNests((CopyOnWriteArrayList<Nest>)in.readObject());
			app.setGantryManager((GantryManager)in.readObject());
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
