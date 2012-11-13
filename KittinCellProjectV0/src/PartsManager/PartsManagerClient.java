package PartsManager;

import data.Part;
import java.io.*;
import java.net.*;

public class PartsManagerClient implements Runnable{
	
	Socket s;
	ObjectInputStream in;
	ObjectOutputStream out;
	PartsManagerApp app;
	
	public PartsManagerClient(PartsManagerApp _app){
		app = _app;
		
		
	}
	public void run(){
		
	}
}
