package server;

import java.net.*;
import java.io.*;

public class DetermineProtocol implements Runnable {
	Socket s;
	ObjectOutputStream out;
	ObjectInputStream in;
	String type;
	Thread thread;
	
	public DetermineProtocol(Socket _s){
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
	
	public void run(){
		try {
			type = (String)in.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
