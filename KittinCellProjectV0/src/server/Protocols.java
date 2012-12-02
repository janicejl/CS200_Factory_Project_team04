package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import data.Job;
import data.KitInfo;
import data.PartInfo;
import GantryManager.GantryManager;

public class Protocols implements Runnable{
	Server app; //reference to server
	Socket s;
	ObjectOutputStream out; //output stream
	ObjectInputStream in; //input stream
	String command; //command received from client
	String commandSent; //command sent from protocol
	String protocolName; //protocol type
	Thread thread;
	Vector<Boolean> v;
	boolean flag = false;
	
	public Protocols(Socket _s, Server _app){
		app = _app;
		s = _s;
		try {
			//extract output and input streams
			out = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
			out.flush();
			in = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
			command = (String)in.readObject(); //read what type of client connected
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}				
		protocolName = command; //set protocol type
		if(protocolName.equals("Lane Manager")){
			try {
				out.writeObject(app.getOverFlow());
				out.reset();
				out.flush();
			} catch (IOException e) {
				System.err.println(protocolName);
				e.printStackTrace();
				try{
					s.close();
					thread.stop();
				} catch(Exception ae){
					System.out.println("Socket Fail to Close");
				}
			}
		}
		thread = new Thread(this, "protocol thread");
		thread.start();
	}
	
	public void run(){
		//choose which protocol to run based off client type
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
			else if (protocolName.equals("Production Manager")){
				runProductionProtocol();
			}
			else if (protocolName.equals("GUI Production Manager")){
				runGUIProductionProtocol();
			}
		}
	}
	
	//Gantry Protocol
	public void runGantryProtocol(){
		try
		{
			out.writeObject(app.getGantryManager());
			out.reset();
		}
		catch(Exception e)
		{
			System.err.println(protocolName);
			e.printStackTrace();
			try{
				s.close();
				thread.stop();
			} catch(Exception ae){
				System.out.println("Socket Fail to Close");
			}
		}
	}
	
	//Kit Assembly Protocol
	public void runKitProtocol(){
		try {
			//write kit robot
			out.writeObject(app.getKitRobot());
			out.reset();
			out.flush();
			//write parts robot
			out.writeObject(app.getPartsRobot());
			out.reset();
			out.flush();
			//write kit assembly
			out.writeObject(app.getKitAssemblyManager());
			out.reset();
			out.flush();
			app.getPartsRobot().setDropParts((boolean)in.readObject()); // read in for parts robot drops
			v = (Vector<Boolean>)in.readObject(); // read in for kit stand user drops
			if(v.get(0)){
				app.getKitAssemblyManager().setPartsDropped(v);
			}
		} catch (Exception e){
			System.err.println(protocolName);
			e.printStackTrace();
			try{
				s.close();
				thread.stop(); //stop protocol if any errors
			} catch(Exception ae){
				System.out.println("Socket Fail to Close");
			}
		}
	}
	
	//Lane Protocol
	public void runLaneProtocol(){
		try {
			//write lanes
			out.writeObject(app.getLanes());
			out.reset();
			out.flush();
			//write feeders
			out.writeObject(app.getFeeders());
			out.reset();
			out.flush();
			//write nests
			out.writeObject(app.getNests());
			out.reset();
			out.flush();
			//receive jammmed state from lane client
			Vector<Boolean> temp = (Vector<Boolean>)in.readObject();
			for(int i=0;i<app.getLanes().size();i++){
				//update each lane's state
				app.getLanes().get(i).setJamOn(temp.get(i));
			}
			//receive overflow state from lane client
			Vector<Boolean> tempOverflow = (Vector<Boolean>)in.readObject();
			//update each lane's overflow state
			for(int i=0;i<app.getLanes().size();i++){
				if(tempOverflow.get(i) != app.getOverFlow().get(i)){
					app.getOverFlow().set(i, tempOverflow.get(i));
					app.nests.get(i).msgToggleNestOverflow();
				}
			}
		} catch (Exception e){
			System.err.println(protocolName);
			e.printStackTrace();
			try{
				s.close();
				thread.stop();
			} catch(Exception ae){
				System.out.println("Socket Fail to Close");
			}
		}
	}
	
	//Parts Manager Protocol
	public void runPartsManagerProtocol(){
		try{
			commandSent = app.getPartsCommand();
			//read in command
			command = (String)in.readObject();
			//if updating parts
			if(command.equals("Update Parts")){	
				//update new parts list
				app.setPartsList((ArrayList<PartInfo>)in.readObject());
				//send confirmation
				out.writeObject("Received");
				out.reset();
				out.flush();
				//make kit manager update parts
				app.setKitCreateCommand("Update Parts"); 
				
				//print out parts to debug
				for(int i = 0; i < app.getPartsList().size(); i++){
					System.out.println(app.getPartsList().get(i).getName());
				}
			}
			//send serverside command
			out.writeObject(commandSent);
			out.reset();
			out.flush();			
		} catch (Exception e){
			System.err.println(protocolName);
			e.printStackTrace();
			try{
				s.close();
				thread.stop();
			} catch(Exception ae){
				System.out.println("Socket Fail to Close");
			}
		}
	}
	
	//Kit Creation Protocol
	public void runKitsManagerProtocol(){
		try{
			commandSent = app.getKitCreateCommand();
			command = (String)in.readObject(); //read in command
			//if updating kits
			if(command.equals("Update Kits")){	
				//update kit list
				app.setKitsList((ArrayList<KitInfo>)in.readObject());
				//send confirmation
				out.writeObject("Received");
				out.reset();
				out.flush();
				//make production manager update kits
				app.setProductionCommand("Update Kits"); 
			}
			
			//send server command
			//if updating parts
			if(commandSent.equals("Update Parts")){
				out.writeObject(commandSent);
				out.reset();
				out.flush();
				app.setKitCreateCommand("Idle"); //reset server command
				//send parts
				out.writeObject(app.getPartsList());
				out.reset();
				out.flush();
				//wait for confirmation
				command = (String)in.readObject();
				if(command.equals("Received")){
					
				}
				//make production manager update kits
				app.setProductionCommand("Update Kits");
			}
			else{
				out.writeObject(commandSent);
				out.reset();
				out.flush();
			}
			
		} catch (Exception e){
			System.err.println(protocolName);
			e.printStackTrace();
			try{
				s.close();
				thread.stop();
			} catch(Exception ae){
				System.out.println("Socket Fail to Close");
			}
		}
	}
	
	//Production Manager Protocol
	public void runProductionProtocol(){
		try{
			command = (String)in.readObject(); //read in command
			//if update
			if(command.equals("Update")){
				app.setJobsList((ArrayList<Job>)in.readObject());
				if(app.getJobsList().size() != 0){
					for(int i = 0; i < app.getJobsList().get(0).getKit().getParts().size(); i++){
						System.out.println(app.getJobsList().get(0).getKit().getParts().get(i).getName());
					}
				}
			}
			//if start production
			else if(command.equals("Start")){
				app.setJobsList((ArrayList<Job>)in.readObject());
				app.running = true;
				app.execute("Get Job");
			}
			commandSent = app.getProductionCommand();
			out.writeObject(commandSent);
			out.reset();
			out.flush();
			if(commandSent.equals("Update Kits")){
				app.setProductionCommand("Idle");
				out.writeObject(app.getKitsList());
				out.reset();
				out.flush();
				command = (String)in.readObject();
				if(command.equals("Received")){
					
				}
			}
			else if(commandSent.equals("Update Jobs")){
				app.setProductionCommand("Idle");
				out.writeObject(app.getJobsList());
				out.reset();
				out.flush();
				command = (String)in.readObject();
				if(command.equals("Received")){
					
				}
			}
			else if(commandSent.equals("Job Finished")){
				app.setProductionCommand("Idle");
				command = (String)in.readObject();
				if(command.equals("Received")){
					
				}
			}
		} catch (Exception e){
			System.err.println(protocolName);
			e.printStackTrace();
			try{
				s.close();
				thread.stop();
			} catch(Exception ae){
				System.out.println("Socket Fail to Close");
			}
		}
	}
	
	//Kit Assembly Protocol
	public void runGUIProductionProtocol(){
		try {
			//write kit robot
			out.writeObject(app.getKitRobot());
			out.reset();
			out.flush();
			//write parts robot
			out.writeObject(app.getPartsRobot());
			out.reset();
			out.flush();
			//write kit assmebly
			out.writeObject(app.getKitAssemblyManager());
			out.reset();
			out.flush();
			//write lanes
			out.writeObject(app.getLanes());
			out.reset();
			out.flush();
			//write feeders
			out.writeObject(app.getFeeders());
			out.reset();
			out.flush();
			//write nests
			out.writeObject(app.getNests());
			out.reset();
			out.flush();
			//update gantry manager
//			if(app.getGantryManager().getGantry().getState().equals("free") && app.getGantryWaitList().size()!=0)
//			{
//				//update gantry robot queue commands
//				app.getGantryManager().getGantry().setState(app.getGantryWaitList().get(0));
//				app.getGantryManager().getGantry().setFeed(app.getGantryFeedList().get(0));
//				app.getGantryWaitList().remove(0);
//				app.getGantryFeedList().remove(0);
//			}
			out.writeObject(app.getGantryManager());
			out.reset();
		}
		catch(Exception e)
		{
			System.err.println(protocolName);
			e.printStackTrace();
			try{
				s.close();
				thread.stop();
			} catch(Exception ae){
				System.out.println("Socket Fail to Close");
			}
		}
	}
		
}
