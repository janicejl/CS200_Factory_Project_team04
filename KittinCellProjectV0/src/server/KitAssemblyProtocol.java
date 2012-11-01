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
	
	public KitAssemblyProtocol(Socket _s, Server _app){
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
		
		thread = new Thread(this, "Kit_Thread");
		thread.start();
	}
	
	public void run(){
		try {
			//confirm phase
			out.writeObject("Confirmed");
			out.reset();
			command = (String)in.readObject();
			if(command.equals("Confirmed")){
				
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
