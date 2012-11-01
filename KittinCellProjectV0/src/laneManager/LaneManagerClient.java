package laneManager;

import java.io.*;
import java.net.*;

public class LaneManagerClient implements Runnable {
	Socket s;
	ObjectOutputStream out;
	ObjectInputStream in;
	String command;
	String commandSent;
	String serverName; //keep track of what server to connect to...default localhost
	Thread thread;
	
	public LaneManagerClient(){
		serverName = "localhost";
		command = "";
		commandSent = "";
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
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}