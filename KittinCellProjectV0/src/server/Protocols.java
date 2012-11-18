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
//			out = new ObjectOutputStream(s.getOutputStream());
//			out.flush();
//			in = new ObjectInputStream(s.getInputStream());
			out = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
			out.flush();
			in = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
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
//			else if (protocolName.equals("Production Kit Client")){
//				runProdKitProtocol();
//			}
			else if (protocolName.equals("Production Manager")){
				runProductionProtocol();
			}
		}
	}
	
	public void runGantryProtocol(){
		try
		{
			if(app.getGantryManager().getGantry().getState().equals("free") && app.getGantryWaitList().size()!=0)
			{
				app.getGantryManager().getGantry().setState(app.getGantryWaitList().get(0));
				app.getGantryManager().getGantry().setFeed(app.getGantryFeedList().get(0));
				app.getGantryWaitList().remove(0);
				app.getGantryFeedList().remove(0);
			}
			out.writeObject(app.getGantryManager());
			out.reset();
			out.flush();
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
	
	public void runKitProtocol(){
		try {
			out.writeObject(app.getKitRobot());
			out.reset();
			out.flush();
			out.writeObject(app.getPartsRobot());
			out.reset();
			out.flush();
			out.writeObject(app.getKitAssemblyManager());
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
	public void runLaneProtocol(){
		try {
			out.writeObject(app.getLanes());
			out.reset();
			out.flush();
			out.writeObject(app.getFeeders());
			out.reset();
			out.flush();
			out.writeObject(app.getNests());
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
	
	public void runPartsManagerProtocol(){
		try{
			commandSent = app.getPartsCommand();
			command = (String)in.readObject();
			if(command.equals("Update Parts")){	
				app.setPartsList((ArrayList<PartInfo>)in.readObject());
				out.writeObject("Received");
				out.reset();
				out.flush();
				app.setKitCreateCommand("Update Parts"); //make kit manager update parts
				for(int i = 0; i < app.getPartsList().size(); i++){
					System.out.println(app.getPartsList().get(i).getName());
				}
			}
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
	
	public void runKitsManagerProtocol(){
		try{
			commandSent = app.getKitCreateCommand();
			command = (String)in.readObject();
			if(command.equals("Update Kits")){	
				app.setKitsList((ArrayList<KitInfo>)in.readObject());
				out.writeObject("Received");
				out.reset();
				out.flush();
				app.setProductionCommand("Update Kits"); //make production manager update kits
			}
			out.writeObject(commandSent);
			out.reset();
			out.flush();
			if(commandSent.equals("Update Parts")){
				app.setKitCreateCommand("Idle");
				out.writeObject(app.getPartsList());
				out.reset();
				out.flush();
				command = (String)in.readObject();
				if(command.equals("Received")){
					
				}
				app.setProductionCommand("Update Kits");
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
	
	public void runProductionProtocol(){
		try{
			command = (String)in.readObject();
			if(command.equals("Update")){
				app.setJobsList((ArrayList<Job>)in.readObject());
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
	
	
//	public void runProdKitProtocol(){
//		try {
//			out.writeObject(app.getKitRobot());
//			out.reset();
//			out.flush();
//			out.writeObject(app.getPartsRobot());
//			out.reset();
//			out.flush();
//			out.writeObject(app.getKitAssemblyManager());
//			out.reset();
//			out.flush();
//		} catch (Exception e){
//			System.err.println(protocolName);
//			e.printStackTrace();
//			try{
//				s.close();
//			} catch(Exception ae){
//				System.out.println("Socket Fail to Close");
//			}
//		}
//	}
		
}
