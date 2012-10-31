package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import blackjack.Player;
import blackjack.Settings;

public class ServerProtocol implements Runnable{

	Thread t; //thread for this class
	Socket proSocket; //reference to socket
	Server data; //reference to server
	ObjectOutputStream out; //output stream
	ObjectInputStream in; //input stream
	Integer wait; //counter to see if screen needs updating
	Integer threadID; //this thread's id number on server
	String command; //command received by client
	Boolean addedCont; //used to determine whether continue was clicked
	Boolean addedDone; //used to determine whether player is done
	
	public ServerProtocol(Server _data, Socket s){
		//initialize variables
		t = new Thread(this, "Protocol_Thread");
		proSocket = s;
		data = _data;
		threadID = new Integer(0);
		addedCont = new Boolean(false);
		addedDone= new Boolean(false);
		
		//create output and input streams
		try {
			out = new ObjectOutputStream(s.getOutputStream());
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			in = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		wait = new Integer(0);
		
		//start thread
		t.start();
	}
	
	public void run() {
		try {			
			//A Connection Phase before Start is clicked
			out.writeObject(new String("Created")); //1 command client connected
			System.out.println("Server Sent: Created");
			out.reset();
			//obtain thread id
			while(true){
				command = (String) in.readObject(); //2 receive command
				if(command.equals("Idle")){
					out.writeObject("Idle"); //3 send command
					out.reset();
					continue;
				}
				else if(command.equals("Start")){
					threadID = data.drawSpace(); 
					out.writeObject("Starting");
					out.reset();
					System.out.println("Server Sent: Starting");
					out.writeObject(threadID); //4 set client's id
					out.reset(); 
					data.getInfo().getPlayers().put(threadID, new Player()); //add player to game
					data.getInfo().getPlayers().get(threadID).setId(threadID); //set id
					data.getInfo().getPlayers().get(threadID).setChips(data.getInfo().getChips()); //set chips
					data.getInfo().getPlayers().get(threadID).setName((String)in.readObject()); //5 get player name
					
					int tempCoin = (Integer)in.readObject(); //6 receive chips
					if(tempCoin != 0){
						data.getInfo().getPlayers().get(threadID).setChips(tempCoin); //reset chips
					}
					
					break;
				}
			}
			while(true){ //restart point
				if(!data.getReadyPlayers().equals(data.getNumPlayers())){
					out.writeObject("Wait Room");
					out.reset();
					//B Waiting room
					System.out.println("SP: B");
					while(true){
						command = (String) in.readObject(); //4 wait for start
						if(command.equals("Idle")){
							if(data.getReadyPlayers().equals(data.getNumPlayers())){
								out.writeObject("GStart");// 5 command game start
								out.reset();
								break;						
							}
							out.writeObject("Idle");
							out.reset();
						}
						else if(command.equals("Update")){
							out.writeObject("Updating");
							out.reset();
							out.writeObject(data.getReady()); //send treemap of ready players
							out.reset();
							TreeMap<Integer, String> names = data.info.getNames();//send array of server player names
							out.writeObject(names); //send player names
							out.reset();
						}
						else if(command.equals("Ready")){
							data.addReadyPlayers(); //to update readyPlayers with lock
							System.out.println("SP waiting ready players: " + data.getReadyPlayers());
							data.getReady().put(threadID, true);
							out.writeObject("gReady");
							out.reset();
						}
					}
				
					//GStart setup
					//create playerpanel by updating clients treemap
					System.out.println("Loop 1a: " + data.getLoop1());
					while((data.getLoop1())){
						//block loop until server has finished processing data
					}
					data.addAccessPlayer();
					if(data.getAccessPlayer().equals(data.getNumPlayers() - data.getTempPlayers())){
						data.setLoop1(true);
						data.setAccessPlayer(0);
						System.out.println("Loop 1b: " + data.getLoop1());
					}
					out.writeObject(data.getInfo());
					out.reset();
					//client creates statspanel
					//adjust center screen
					
					//turn off permissions
				}
				else{
					out.writeObject("No Wait");
					System.out.println("Loop 1a: " + data.getLoop1());
					while((data.getLoop1())){
						//block loop until server has finished processing data
					}
					data.addAccessPlayer();
					if(data.getAccessPlayer().equals(data.getNumPlayers() - data.getTempPlayers())){
						data.setLoop1(true);
						data.setAccessPlayer(0);
						System.out.println("Loop 1b: " + data.getLoop1());
					}
				}
				
				//C Wait until your turn
				System.out.println("SP: C");
				while(true){ //while thread is not being processed
					command = (String) in.readObject();
					if(command.equals("Idle")){ //6 idle wait
						if(threadID.equals(data.getInfo().getId())){ //must return to idle state in order to get permission and continue game
							//grant permission
							data.getInfo().setScreenPlayer(threadID);
							out.writeObject("Granted"); //7 player's turn
							out.reset();
							out.writeObject(data.getInfo());
							out.reset();
							break;
						}
						out.writeObject("Idle"); //respond with idle if still waiting
						out.reset();
						out.writeObject(data.getInfo().getId()); //send id
						out.reset();
					}
					else if(command.equals("Update")){
						out.writeObject("ScreenSent");
						out.reset();
						out.writeObject(data.getInfo().getPlayers()); //send updated players
						out.reset();
					}
					//implement more commands later
				}
				
				out.writeObject(data.getInfo().getPhase()); //send game phase (3)
				//D Player's turn
				System.out.println("SP: D");
				while(true){
					command = (String) in.readObject(); //waits until player sets bet
					if(command.equals("Bet")){ //8 if player entered bet
						data.info.getPlayers().get(threadID).setBet((Integer) in.readObject()); //update with player's bet
						//update status to playing
						data.getInfo().getPlayers().get(threadID).setStatus("Playing");
						data.getInfo().setPhase(3); //PROGRESS_MARK go to request()
						data.getBjack().run(); //run request
						out.writeObject("BetDone");
						out.reset();
						break;
					}
					else if(command.equals("Betting")){
						out.writeObject("sBetting");
						out.reset();
					}
					else if(command.equals("Idle")){
						out.writeObject("Idle"); //respond with idle if still waiting
						out.reset();
						out.writeObject(data.getInfo().getId()); //send id to update player panel
						out.reset();
					}
					else if(command.equals("Update")){
						out.writeObject("ScreenSent");
						out.reset();
						out.writeObject(data.getInfo().getPlayers()); //send updated players
						out.reset();
					}
				}
				
				//E Request Phase
				System.out.println("SP: E");
				while(true){ //while thread is not being processed
					command = (String) in.readObject();
					if(command.equals("Idle")){ //9 idle wait
						out.writeObject("Idle"); //respond with idle if still waiting
						out.reset();
					}
					else if(command.equals("Hit")){ //player wants to hit
						data.getInfo().setPhase(10);
						data.bjack.run();
						if(data.getInfo().getPlayers().get(threadID).getBust() == true){
							out.writeObject("Busted"); //9 tell player they busted
							out.reset();
							out.writeObject(data.getInfo().getPlayers()); //send back updated data (for screen update)
							out.reset();
							data.getInfo().setPhase(11);
							data.getBjack().run();
							break;
						}
						out.writeObject("ScreenSent"); //9 confirm that hit was processed (update screen)
						out.reset();
						out.writeObject(data.getInfo().getPlayers()); //send back updated players
						out.reset();
					}
					else if(command.equals("Stay")){ //player wants to hit
						data.getInfo().setPhase(11);
						data.getBjack().run();
						out.writeObject("StayDone"); //9 confirm that hit was processed
						out.reset();
						out.writeObject(data.getInfo().getPlayers()); //send back updated data (for screen update)
						out.reset();
						out.writeObject(data.getInfo().getId()); //send id
						out.reset();
						break;
					}
					else if(command.equals("Update")){
						out.writeObject("ScreenSent");
						out.reset();
						out.writeObject(data.getInfo().getPlayers()); //send updated players
						out.reset();
					}
					//implement more commands later
				}
				data.addDonePlayers();
				addedDone = true;
				
				
				//F Wait for game to finish 
				System.out.println("SP: F");
				data.setLoop2(true);
				while(true){ //while thread is not being processed
					command = (String) in.readObject();
					if(command.equals("Idle")){ //10 idle wait
						if(data.getDonePlayers().equals(data.getNumPlayers())){
							while(data.getLoop2()){
							 //pause until game is done
							}
							data.addAccessPlayer();
							if(data.getAccessPlayer().equals(data.getNumPlayers() - data.getTempPlayers())){
								data.setLoop2(true);
								data.setAccessPlayer(0);
							}
							out.writeObject("Finish");
							out.reset();
							break;
						}
						out.writeObject("Idle"); //respond with idle if still waiting
						out.reset();
						out.writeObject(data.getInfo().getId()); //set player panel
						out.reset();
					}
					else if(command.equals("Update")){
						out.writeObject("ScreenSent");
						out.reset();
						out.writeObject(data.getInfo().getPlayers()); //send updated players
						out.reset();
					}
				}
				
				
				out.writeObject(data.getInfo().getPlayers()); //1 get updated player information
				out.reset();
				out.writeObject(data.getInfo().getPhase()); //2 update game phase
				out.reset();
				out.writeObject(data.getInfo().getId()); //3 send id to update player panel
				out.reset();
				
				
				//G wait for all players to continue
				System.out.println("SP: G");
				data.setLoop3(true);
				if(data.getInfo().getPlayers().get(threadID).getChips() <= 0){
					out.writeObject("Game Over");
					out.reset();
					while(true){
						//pause server until connection gone
					}
				}
				else{
					out.writeObject("Playing");
				}
				while(true){
					command = (String) in.readObject();
					if(command.equals("Idle")){
						if(data.getRestarting()){
							/*
							 * if(addCont == true){
							 */
							while((data.getLoop3())){
								//block loop until server has finished processing data
							}
							data.addAccessPlayer();
							if(data.getAccessPlayer().equals(data.getNumPlayers() - data.getTempPlayers())){
								data.setRestarting(false);
								data.setAccessPlayer(0);
							}
							out.writeObject("Restart");
							out.reset();
							out.writeObject(data.getInfo());
							out.reset();
							
							break;
						}
						out.writeObject("Idle");
						out.reset();
						
					}
					else if(command.equals("Update")){
						if(!data.getReadyPlayers().equals(data.getNumPlayers())){
							if(addedCont == true){ //only send this command if player has pushed continue
								out.writeObject("Restart");
								out.reset();
								System.out.println("Update: Restart");
								while((data.getLoop3())){
									
								}
								data.addAccessPlayer();
								if(data.getAccessPlayer().equals(data.getNumPlayers()-data.getTempPlayers())){
									data.setLoop3(true);
									data.setRestarting(false);
									data.setAccessPlayer(0);
								}
								System.out.println("writing data");
								out.writeObject(data.getInfo());
								out.reset();
								break;
							}
						}
						out.writeObject("ScreenSent");
						out.reset();
						out.writeObject(data.getInfo().getPlayers()); //send updated players
						out.reset();
					}
					else if(command.equals("Continue")){
						data.addContPlayers();
						addedCont = true;
						if(!data.getReadyPlayers().equals(data.getNumPlayers())){
							out.writeObject("Restart");
							out.reset();
							while((data.getLoop3())){
								
							}
							data.addAccessPlayer();
							if(data.getAccessPlayer().equals(data.getNumPlayers() - data.getTempPlayers())){
								data.setLoop3(true);
								data.setRestarting(false);
								data.setAccessPlayer(0);
							}
							System.out.println("writing data");
							out.writeObject(data.getInfo());
							out.reset();
							break;
						}
						out.writeObject("Wait");
						out.reset();
					}
				}
				addedCont = false;
				addedDone = false;
				System.out.println("restarting");
			}
		} catch (IOException e) { //handle disconnection
			if(!data.getGameStarted()){ //if first round never started
				System.out.println("First choice");
				data.subFirstStart();
				data.getInfo().getPlayers().remove(threadID);
				if(data.getReady().get(threadID) == true){
					data.subReadyPlayers();
				}
				data.getReady().put(threadID, false); //set waiting status
				data.getOpenSpaces().put(threadID, true); //open up space
				System.out.println("SP catch readyplayers: " + data.getReadyPlayers());
			}
			else{
				System.out.println("Second choice");
				data.subReadyPlayers(); //take out ready player
				if(!addedCont){ //if you haven't pushed continue
					data.addContPlayers(); //add continue
				}
				if(!addedDone){
					data.addDonePlayers();
				}
				data.getInfo().getPlayers().get(threadID).setTemp(true);
				data.addTempPlayers();
				
			}
			System.out.println("SP catch contplayers: " + data.getContPlayers());
			System.out.println("SP catch doneplayers: " + data.getDonePlayers());
			System.out.println("SP catch readyplayers: " + data.getReadyPlayers());
			System.err.println("Client disconnected, exiting...");
			e.printStackTrace();
			return;
		} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (ConcurrentModificationException c){
			JOptionPane.showMessageDialog(null, "Thread Concurrence");
			c.printStackTrace();
			System.exit(1);
		}
	}
}
