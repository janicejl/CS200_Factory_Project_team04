package server;

import java.io.*;
import java.net.*;

public class KitAssemblyProtocol implements Runnable{
	Socket s;
	ObjectOutputStream out;
	ObjectInputStream in;
	String type;
	Thread thread;
	
	public KitAssemblyProtocol(Socket _s){
		s = _s;
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
			type = (String)in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
