package server;

import java.io.*;
import java.net.*;

public class Protocols implements Runnable{
	Server app;
	Socket s;
	ObjectOutputStream out;
	ObjectInputStream in;
	String command;
	String commandSent;
	String protocolName;
	String type;
	Thread thread;
	
	public Protocols(Socket _s, Server _app){
		app = _app;
		s = _s;
		try {
			out = new ObjectOutputStream(s.getOutputStream());
			out.flush();
			in = new ObjectInputStream(s.getInputStream());
			command = (String)in.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e){
		// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		protocolName = command;
		thread = new Thread(this, "protocol thread");
		thread.start();
	}
	
	public synchronized void run(){
		while(true){
			if(protocolName.equals("Kit Assembly")){
				runKitProtocol();
			}
			else if (protocolName.equals("Lane Manager")) {
				runLaneProtocol();				
			}
			else if (protocolName.equals("Gantry Manager")){
				runGantryProtocol();
				System.out.println("Gantry running");
			}
		}
	}
	
	public synchronized void runGantryProtocol(){
		try
		{
			out.writeObject("Confirmed");
			out.reset();
			command = (String)in.readObject();
			if(command.equals("Confirmed"))
			{
				
			}
			commandSent = "Received";
			while(true)
			{
				out.writeObject(app.getGantryManager());
				out.reset();
				command = (String)in.readObject();
				if(command.equals("Received")){
					
				}
				else{
					System.exit(1);
				}
			}
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized void runKitProtocol(){
		try {
			out.writeObject(app.getKitRobot());
			out.reset();
			if(app.getKitAssemblyManager().getMsg().equals(true)){
				app.getKitAssemblyManager().setMsg(false);
				app.getKitConveyorAgent().msgKitHasArrived();
			}
			out.writeObject(app.getPartsRobot());
			out.reset();
			if(app.getPartsRobot().getMsg().equals(true)){
				app.getPartsRobotAgent().msgAnimationDone();
				app.getPartsRobot().setMsg(false);
			}
			out.writeObject(app.getKitAssemblyManager());
			out.reset();
			if(app.getPartsRobot().getAnimationDone()){
				for(int i = 0; i<4; i++){
					app.getVisions().get(i).msgAnimationDone();
				}
				app.getPartsRobot().setAnimationDone(false);
			}		
		}
		catch(Exception ignore){}
	}
	public synchronized void runLaneProtocol(){
		try {
			out.writeObject(app.getLanes());
			out.reset();
			out.writeObject(app.getFeeders());
			out.reset();
			out.writeObject(app.getNests());
			out.reset();
		} catch (Exception ignore){}
	}
}
