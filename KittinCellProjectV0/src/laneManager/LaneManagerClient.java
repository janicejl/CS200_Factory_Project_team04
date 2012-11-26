package laneManager;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import Feeder.Feeder;

import server.Lane;

public class LaneManagerClient implements Runnable {
	Socket s;
	LaneGraphics app;
	ObjectOutputStream out;
	ObjectInputStream in;
	String command;
	String commandSent;
	String serverName; //keep track of what server to connect to...default localhost
	Thread thread;
	
	 //data received from server
	
	public LaneManagerClient(LaneGraphics _app){
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
				app.setLanes((CopyOnWriteArrayList<Lane>)in.readObject());
				app.setFeeders((CopyOnWriteArrayList<Feeder>)in.readObject());
				app.setNests((CopyOnWriteArrayList<Nest>)in.readObject());
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
			app.setLanes((Vector<Lane>)in.readObject());
			app.setFeeders((Vector<Feeder>)in.readObject());
			app.setNests((Vector<Nest>)in.readObject());
		} catch (Exception ignore){
			ignore.printStackTrace();
			System.exit(1);
		}
	}

	public synchronized void setThread(Thread thread) {
		this.thread = thread;
	}
}