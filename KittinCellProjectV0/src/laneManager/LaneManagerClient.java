package laneManager;

import java.io.*;
import java.net.*;
import java.util.*;

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
		int i = connect();
		if(i == -1){
			System.exit(1);
		}
		else if(i == 1){
			thread.start();
		}
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
			commandSent = "LaneManager";
			out.writeObject(commandSent); //send to server identifying what client this is
			out.reset();
			command = (String)in.readObject();
			if(command.equals("Confirmed")){
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
				app.setLanes((ArrayList<Lane>)in.readObject());
				out.writeObject(commandSent);
				out.reset();
			}
			
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
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}