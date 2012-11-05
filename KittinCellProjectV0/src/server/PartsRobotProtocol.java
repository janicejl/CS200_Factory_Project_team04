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
			command = (String)in.readObject();
			if(!command.equals("PartsRobot")){
				commandSent = "Denied";
				System.exit(1);
			}			
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
				if(app.getPartsRobot().getMsg().equals(true)){
					app.getPartsRobotAgent().msgAnimationDone();
					app.getPartsRobot().setMsg(false);
				}
				out.writeObject(app.getKitAssemblyManager());
				out.reset();
				command = (String)in.readObject();
				if(command.equals("Received")){
				}
				else if(command.equals("Update")){
					app.getPartsRobot().setTakePicture(false);
					for(int i = 0; i<4; i++){
						app.getVisions().get(i).msgAnimationDone();
					}
				}
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
