package server;

import java.io.*;
import java.net.*;

public class PartsRobotProtocol implements Runnable{
	Server app;
	Socket s;
	ObjectOutputStream out;
	ObjectInputStream in;
	String command;
	String commandSent;
	String type;
	Thread thread;
	
	public PartsRobotProtocol(Socket _s, Server _app){
		app = _app;
		s = _s;
		command = "";
		commandSent = "";
		try {
			out = new ObjectOutputStream(s.getOutputStream());
			out.flush();
			in = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		thread = new Thread(this, "PartsRobot_Thread");
		thread.start();
	}
	
	public void run(){
		try {
			//confirm phase
			out.writeObject("Confirmed");
			out.reset();
			command = (String)in.readObject();
			if(command.equals("Confirmed")){
				//start
			}
			
			commandSent = "Idle";
			while(true){
				out.writeObject(app.getPartsRobot());
				out.reset();
				out.writeObject(app.getKitAssemblyManager());
				out.reset();
				command = (String)in.readObject();
				if(command.equals("Received")){
					
				}
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
