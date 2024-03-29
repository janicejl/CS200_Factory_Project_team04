package ExtraCode;
//
//import java.io.*;
//import java.net.*;
//
//import kitAssemblyManager.GUIKitAssemblyManager;
//
//import server.KitAssemblyManager;
//import server.PartsRobot;
//
//public class PartsManagerClient implements Runnable {
//	Socket s;
//	GUIKitAssemblyManager app;
//	ObjectOutputStream out;
//	ObjectInputStream in;
//	String command;
//	String commandSent;
//	String serverName; //keep track of what server to connect to...default localhost
//	Thread thread;
//	
//	public PartsManagerClient(GUIKitAssemblyManager _app){
//		app = _app;
//		serverName = "localhost";
//		command = "";
//		commandSent = "";
//		thread = new Thread(this, "PartsRobotClient_Thread");
//	}
//	
//	public Integer connect(){
//		try {
//			s = new Socket(serverName, 61337); //attempt to connect to servername
//			out = new ObjectOutputStream(s.getOutputStream()); //output stream
//			in = new ObjectInputStream(s.getInputStream()); //input stream
//		} catch (UnknownHostException e) {
//			System.err.println("Can't find server " + serverName);
//            return -1;
//		} catch (IOException e) {
//			System.err.println("Can't find server " + serverName);
//            return -1;
//		}
//		System.out.println("Connected to "+ serverName);
//		return 1; //successful connection
//	}
//	
//	public void run(){
//		try {
//			commandSent = "PartsRobot";
//			out.writeObject(commandSent); //send to server identifying what client this is
//			out.reset();
//			command = (String)in.readObject();
//			if(command.equals("Confirmed")){
//				commandSent = "Confirmed";
//				out.writeObject(commandSent);
//				out.reset();
//				//start
//			}
//			else{
//				//error connection
//				System.out.println(command);
//				System.exit(1);
//			}
//			
////**************START CODE***************************
//			commandSent = "Received";
//			/*
//			while(true){
//				app.setPartsRobot((PartsRobot)in.readObject());
//				app.setKitAssemblyManager((KitAssemblyManager)in.readObject());
//				out.writeObject(commandSent);
//				out.reset();
//				if(commandSent.equals("Update")){
//					commandSent = "Received";
//				}
//				try {
//					Thread.sleep(10);
//				} catch(Exception ignore){}
//				
//			}
//			*/
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//	}
//
//	public synchronized Thread getThread() {
//		return thread;
//	}
//	
//	public synchronized void updateThread() {
//		try {			
//			app.setPartsRobot((PartsRobot)in.readObject());
//			app.setKitAssemblyManager((KitAssemblyManager)in.readObject());
//			out.writeObject(commandSent);
//			out.reset();
//			if(commandSent.equals("Update")){
//				commandSent = "Received";
//			}
//		} catch(Exception ignore){}
//		
//	}
//
//	public synchronized void setThread(Thread thread) {
//		this.thread = thread;
//	}
//
//	public synchronized String getCommandSent() {
//		return commandSent;
//	}
//
//	public synchronized void setCommandSent(String commandSent) {
//		this.commandSent = commandSent;
//	}
//}
