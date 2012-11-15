package server;

import java.io.*;
import java.net.*;
import java.util.Vector;

import data.Job;
import data.KitInfo;
import GantryManager.GantryManager;

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
	
	public void run(){
		while(true){
			if(protocolName.equals("Kit Assembly")){
				runKitProtocol();
			}
			else if (protocolName.equals("Lane Manager")) {
				runLaneProtocol();				
			}
			else if (protocolName.equals("Gantry Manager")){
				runGantryProtocol();
			}
			else if (protocolName.equals("Part Manager")){
				runPartsManagerProtocol();
			}
			else if (protocolName.equals("Kit Manager")){
				runKitsManagerProtocol();
			}
			else if (protocolName.equals("Production Kit Client")){
				runProdKitProtocol();
			}
			else if (protocolName.equals("Production Manager")){
				runProductionProtocol();
			}
		}
	}
	
	public void runGantryProtocol(){
		try
		{
			out.writeObject(app.getGantryManager());
			out.reset();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void runKitProtocol(){
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
		} catch (Exception e){
			System.err.println(protocolName);
			e.printStackTrace();
			try{
				s.close();
			} catch(Exception ae){
				System.out.println("Socket Fail to Close");
			}
		}
	}
	public void runLaneProtocol(){
		try {
			out.writeObject(app.getLanes());
			out.reset();
			out.writeObject(app.getFeeders());
			out.reset();
			out.writeObject(app.getNests());
			out.reset();
		} catch (Exception e){
			System.err.println(protocolName);
			e.printStackTrace();
			try{
				s.close();
			} catch(Exception ae){
				System.out.println("Socket Fail to Close");
			}
		}
	}
	
	public void runPartsManagerProtocol(){
		
	}
	
	public void runKitsManagerProtocol(){
		try{
			commandSent = app.getKitCreateCommand();
			command = (String)in.readObject();
			if(command.equals("Update Kits")){	
				app.setKitsList((Vector<KitInfo>)in.readObject());
				out.writeObject("Received");
				out.reset();
				app.setProductionCommand("Update Kits"); //make production manager update kits
			}
			out.writeObject(commandSent);
			out.reset();
			if(commandSent.equals("Update Parts")){
				app.setKitCreateCommand("Idle");
				out.writeObject(app.getPartsList());
				out.reset();
				command = (String)in.readObject();
				if(command.equals("Received")){
					
				}
			}
			
		} catch (Exception e){
			System.err.println(protocolName);
			e.printStackTrace();
			try{
				s.close();
			} catch(Exception ae){
				System.out.println("Socket Fail to Close");
			}
		}
	}
	
	public void runProductionProtocol(){
		try{
			command = (String)in.readObject();
			if(command.equals("Update")){
				app.setJobsList((Vector<Job>)in.readObject());
			}
			commandSent = app.getProductionCommand();
			out.writeObject(commandSent);
			out.reset();
			if(commandSent.equals("Update Kits")){
				app.setProductionCommand("Idle");
				out.writeObject(app.getKitsList());
				out.reset();
				command = (String)in.readObject();
				if(command.equals("Received")){
					
				}
			}
			else if(commandSent.equals("Update Jobs")){
				app.setProductionCommand("Idle");
				out.writeObject(app.getJobsList());
				out.reset();
				command = (String)in.readObject();
				if(command.equals("Received")){
					
				}
			}
		} catch (Exception e){
			System.err.println(protocolName);
			e.printStackTrace();
			try{
				s.close();
			} catch(Exception ae){
				System.out.println("Socket Fail to Close");
			}
		}
	}
	
	
	public void runProdKitProtocol(){
		try {
			out.writeObject(app.getKitRobot());
			out.reset();
			out.writeObject(app.getPartsRobot());
			out.reset();
			out.writeObject(app.getKitAssemblyManager());
			out.reset();	
		} catch (Exception e){
			System.err.println(protocolName);
			e.printStackTrace();
			try{
				s.close();
			} catch(Exception ae){
				System.out.println("Socket Fail to Close");
			}
		}
	}
		
}
