package server;
import java.io.*;
import java.net.*;

public class LaneManagerProtocol implements Runnable{
	Server app;
	Socket s;
	ObjectOutputStream out;
	ObjectInputStream in;
	String command;
	String commandSent;
	String type;
	Thread thread;

	public LaneManagerProtocol(Socket _s, Server _app){
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

		thread = new Thread(this, "Lane_Thread");
		thread.start();
	}

	public void run(){
		try {
			commandSent = "Confirmed";
			command = (String)in.readObject();
			if(!command.equals("LaneManager")){
				commandSent = "Denied";
			}			
			//confirm phase
			out.writeObject(commandSent);
			out.reset();
			command = (String)in.readObject();
			if(command.equals("Confirmed")){
				/*
				while(true){
					if(app.getStart()){ // check whether there is command from agent
						commandSent = "start";
						out.writeObject(commandSent);
						break;
					}
					else{
						commandSent = "idle";
						out.writeObject(commandSent);
						out.reset();
						command = (String)in.readObject();
					}
				}
				
				commandSent = "idle";
				//commandSent will stay idle until button click change its value
				while(true){
					command = (String)in.readObject();
					out.writeObject(commandSent);
					out.reset();
					if(command.equals("idle")){
						continue;
					}
					else if(command.equals("working")){
						
					}
				}*/
				
			}
			
			commandSent = "Received";
			while(true){
				out.writeObject(app.getLanes());
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