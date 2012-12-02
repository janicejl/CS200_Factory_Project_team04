package laneManager;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import Feeder.Feeder;

import server.Lane;

public class LaneManagerClient implements Runnable {
	Socket s;
	LaneManagerApp app;
	ObjectOutputStream out;
	ObjectInputStream in;
	String command;
	String commandSent;
	String serverName; //keep track of what server to connect to...default localhost
	Thread thread;
	
	 //data received from server
	
	public LaneManagerClient(LaneManagerApp _app){
		serverName = "localhost";
		command = "";
		commandSent = "";
		app = _app;
		thread = new Thread(this, "LaneManagerClient_Thread");
		
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
		System.out.println("Lane Manager connected to "+ serverName);
		return 1; //successful connection
	}

	public void run(){
		try {
			commandSent = "Lane Manager";
			out.writeObject(commandSent); //send to server identifying what client this is
			out.reset();
			out.flush();
			app.setOverFlow((Vector<Boolean>)in.readObject());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch(ClassNotFoundException ce){
			ce.printStackTrace();
			System.exit(1);
		}
	}

	public synchronized Thread getThread() {
		return thread;
	}
	
	public synchronized void updateThread(){
		try {
			app.setLanes((Vector<Lane>)in.readObject());
			app.setFeeders((Vector<Feeder>)in.readObject());
			app.setNests((CopyOnWriteArrayList<Nest>)in.readObject());
			//send jammed state of lanes to server
			out.writeObject(app.getOnJammeds());
			out.reset();
			out.flush();
			//send overflow state to server
			out.writeObject(app.getOverFlow());
			out.reset();
			out.flush();
		} catch (Exception ignore){
			ignore.printStackTrace();
			System.exit(1);
		}
	}

	public synchronized void setThread(Thread thread) {
		this.thread = thread;
	}
}