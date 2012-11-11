package server;

import java.io.*;
import java.net.*;

public class KitAssemblyProtocol implements Runnable{
	Server app;
	Socket s;
	ObjectOutputStream out;
	ObjectInputStream in;
	String command;
	String commandSent;
	String type;
	Thread thread;
	
	public KitAssemblyProtocol(Socket _s, Server _app, ObjectOutputStream _out, ObjectInputStream _in){
		app = _app;
		s = _s;
		try {
			out = new ObjectOutputStream(_out);
			out.flush();
			in = _in;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		command = "";
		commandSent = "Confirmed";
		/*try {
			out = new ObjectOutputStream(s.getOutputStream());
			out.flush();
			in = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		thread = new Thread(this, "Kit_Thread");
		thread.start();
	}
	
	public synchronized void run(){
		try {
			/*command = (String)in.readObject();
			if(!command.equals("KitAssembly")){
				commandSent = "Denied";
				System.exit(1);
			}*/
			//confirm phase
			out.writeObject(commandSent);
			out.reset();
			command = (String)in.readObject();
			if(command.equals("Confirmed")){
				//start
			}
			
			commandSent = "Idle";
			while(true){
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
