package server;

import java.net.*;
import java.io.*;

public class DetermineProtocol implements Runnable {
	Socket s;
	Server app;
	ObjectOutputStream out;
	ObjectInputStream in;
	String type;
	Thread thread;
	
	public DetermineProtocol(Socket _s, Server _app){
		s = _s;
		try {
			out = new ObjectOutputStream(s.getOutputStream());
			out.flush();
			in = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		thread = new Thread(this, "Determine_Thread");
		thread.start();
	}
	
	public void typeDetermine(String _type){
		if(_type.equals("Kit Assembly")){
			app.kitPro = new KitAssemblyProtocol(s, app, out, in);
		}
		else if(_type.equals("Lane Manager")){
			app.lanePro = new LaneManagerProtocol(s, app);
		}
		else if(_type.equals("Parts Robot")){
			app.partsPro = new PartsRobotProtocol(s, app);
		}
	}
	
	public void run(){
		try {
			type = (String)in.readObject(); //read in what type of client connected
			typeDetermine(type); //create proper protocol
			return; //finished
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
