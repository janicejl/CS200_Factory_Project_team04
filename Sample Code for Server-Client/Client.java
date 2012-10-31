package blackjack;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.*;

public class Client implements Runnable{
	
	JFrame app; //reference to app
	Settings info; //reference to info
	
	Thread thread; //thread to run this client
	Socket s;  //socket to talk to network
	String serverName; //server name stored
	ObjectOutputStream out; //output stream
	ObjectInputStream in; //input stream
	String command; //command received
	String commandSent; //command to sent
	Integer updateLoop; //counter to update the screen
	ProfileList profiles; //loaded list of profiles
	String name; //selected user name
	Integer coins; //coins for current client
	Integer id; //thread ID
	Boolean check; //verify button was pressed
	PaintMap map; //paint the screen
	
	public Client(JFrame _app, Settings _info){
		
		//initialize everything
		app = _app;
		info = _info;
		serverName = "localhost";
		thread = new Thread(this, "Client_Thread");
		updateLoop = new Integer(0);
		coins = new Integer(0);
		profiles = new ProfileList();
		name = null;
		id = new Integer(0);
		map = new PaintMap(app);
		check = new Boolean(false);
	}

	public void updateWait(TreeMap<Integer, Boolean> tempReady, TreeMap<Integer, String> names){
		for(int i = 0; i < ((Application)app).waitPanel.pNames.size(); i++){ //reset all words
			((Application)app).waitPanel.pNames.get(i).setText("");
			((Application)app).waitPanel.pStatus.get(i).setText("Waiting");
		}
		for(Map.Entry<Integer, String> iterate: names.entrySet()){ //iterate through passed treemap to determine what names to display
			((Application)app).waitPanel.pNames.get(iterate.getKey()).setText(iterate.getValue());
		}
		for(Map.Entry<Integer, Boolean> iterate : tempReady.entrySet()){ //iterate through passed treemap to determine what statuses to change
			if(iterate.getValue() == true){
				((Application)app).waitPanel.pStatus.get(iterate.getKey() - 1).setText("Ready");
			}
		}
	}
	
	public Integer connect(){
		
		try {
			s = new Socket(serverName, 51337); //attempt to connect to servername
			out = new ObjectOutputStream(s.getOutputStream()); //output stream
			in = new ObjectInputStream(s.getInputStream()); //input stream
		} catch (UnknownHostException e) {
			System.err.println("Can't find server " + serverName);
            return -1;
		} catch (IOException e) {
			System.err.println("Can't find server " + serverName);
            return -1;
		}
		System.out.println("Connected to "+ serverName);
		return 1; //successful connection
	}
	
	public void run(){
		try {			
			//A Connection Phase before Start is clicked
			command = (String) (in.readObject()); //1 connected, next read Request for data
			if(command.equals("Created")){
			}
			else{
				System.err.println("Server connection fail");
				System.exit(1);
			}
			commandSent = "Start";
			while(true){
				out.writeObject(commandSent); //2 send command
				out.reset();
				command = (String)in.readObject(); //3 receive command
				if(command.equals("Idle")){
					continue;
				}
				else if(command.equals("Starting")){
					System.out.println("Client Received: Starting");
					id = (Integer) in.readObject(); //4 set this client's id
					out.writeObject(name); //5 resend with name
					out.reset();
					out.writeObject(coins); //6 sent coins
					out.reset();
					break;
				}
			}
			
			while(true){ //restart point
				command = (String)in.readObject(); //wait for server command
				System.out.println(command);
				if(command.equals("Wait Room")){
					GridBagConstraints c = new GridBagConstraints();
					c.gridx = 1;
					c.gridy = 0;
					((Application)app).removeCenter();
					((Application)app).add(((Application)app).waitPanel, c);
					((Application)app).phase = 5; //change app phase
					((Application)app).setSize(858, 625);
					((Application)app).waitPanel.ready.setEnabled(true);
					
					//B Waiting room
					commandSent = "Update";
					while(true){
						out.writeObject(commandSent); //4 wait until player sends command ready
						out.reset();
						command = (String) in.readObject();
						if(command.equals("Idle")){
							updateLoop++;
							if(updateLoop == 2000){ //after 2000 connections, will go to update
								updateLoop = 0;
								commandSent = "Update";
							}
						}
						else if(command.equals("Updating")){
							TreeMap<Integer, Boolean> tempReady = (TreeMap<Integer, Boolean>)in.readObject(); //read player list (data.ready)
							TreeMap<Integer, String> names = (TreeMap<Integer, String>) in.readObject(); //get player names
							updateWait(tempReady, names);
							commandSent = "Idle";
							continue;
						}
						else if(command.equals("gReady")){
							commandSent = "Update";
							((Application)app).waitPanel.ready.setEnabled(false); //turn off ready button
						}		
						else if(command.equals("GStart")){ //5 wait for start, then setup game
							//create player panel
							Settings temp = (Settings)in.readObject();
							info.copy(temp);
							for(int i = 1; i <= 4; i++){
								((Application)app).players.create(i);
							}
							
							//setup printobject to print self
							map.printObject.put(0, info.getPlayers().get(0)); //put in dealer
							map.printObject.put(1, info.players.get(id));
							
							//create center
							c.gridx = 1;
							c.gridy = 0;
							((Application)app).removeCenter();
							app.setSize(new Dimension(1130, 625));
							((Application)app).add(((Application)app).bjackPanel, c);
							if(info.paused == false){
								((Application)app).stats.enableAction();
							}
							((Application)app).players.enableAction();
							((Application)app).phase = 3;
							
							//turn off permissions
							info.setDeny(true);
							break;
						}
					}
				}
				
				info.setScreenPlayer(id); //make player displayed yourself
				//C Wait until your turn
				commandSent = "Idle";
				while(true){
					out.writeObject(commandSent); //6 wait for game start
					out.reset();
					command = (String) in.readObject();
					if(command.equals("Idle")){
						info.setId((Integer)in.readObject()); //set up player panel
						updateLoop++;
						if(updateLoop == 2000){ //after 2000 connections, will go to update
							updateLoop = 0;
							commandSent = "Update";
						}
					}
					else if(command.equals("Granted")){ //7 permission to start game
						updateLoop = 0;
						info.copy((Settings)in.readObject()); //copy player info
						info.setScreenPlayer(id);
						info.setDeny(false); //releases permissions
						map.printObject.put(0, info.getPlayers().get(0));
						map.printObject.put(1, info.players.get(info.getScreenPlayer())); //paint your cards
						break;
					}
					else if(command.equals("ScreenSent")){ //6 update screen
						updateLoop = 0;
						info.players.clear();
						info.players = (TreeMap<Integer,Player>) in.readObject(); //get updated player information
						map.printObject.put(0, info.getPlayers().get(0)); //put dealer
						map.printObject.put(1, info.getPlayers().get(info.getScreenPlayer())); //display the player you wanted to see
						commandSent = "Idle";
					}
				}
				
				//D Player's turn
				//Game Bet()
				//status panel update
				//set prompt on statspanel
				((Application)app).stats.setText("Betting");
				((Application)app).stats.setTextSize(40);
				info.setPhase((Integer)in.readObject()); //update game phase
				check = true;
				while(true){
					if(check == true){
						commandSent = "Betting";
					}
					out.writeObject(commandSent);
					out.reset();
					command = (String) in.readObject();
					if(command.equals("sBetting")){
						//prompt user for bet with a dialog box
						int tempBet = 0;
						//continue loop until player enters proper number of chips
						while(true){
							//same code as before
							String input = JOptionPane.showInputDialog("How much would you like to bet?");
							if(input == null){ //edit
								((Application)app).stats.enableCont();
								commandSent = "Idle";
								check = false;
								break;
							}
							try{
								tempBet = Integer.parseInt(input);
								if(tempBet <= info.players.get(id).getChips() && tempBet >= 0){
									commandSent = "Bet";//set the bet and send back
									info.players.get(id).setBet(tempBet);
									info.players.get(id).setStatus("Playing");
									out.writeObject(commandSent); //8 send command to bet
									out.reset();
									out.writeObject(tempBet);
									out.reset();
									check = false;
									break;
								}
								else if(tempBet > info.players.get(id).getChips()){					
									JOptionPane.showMessageDialog(null, "You don't have that many chips.");
									continue;
								}
								else if(tempBet < 0){								
									JOptionPane.showMessageDialog(null, "You can't bet negative chips.");
									continue;
								}
							}
							catch(NumberFormatException e){							
								JOptionPane.showMessageDialog(null, "Please enter only numbers.");
								continue;
							}
						}
					}
					else if(command.equals("BetDone")){ //bet received
						break;
					}
					else if(command.equals("Idle")){
						info.setId((Integer)in.readObject()); //update player panel
						updateLoop++;
						if(updateLoop == 2000){ //after 2000 connections, will go to update
							updateLoop = 0;
							commandSent = "Update";
						}
					}				
					else if(command.equals("ScreenSent")){ //6 update screen
						updateLoop = 0;
						info.players.clear();
						info.players = (TreeMap<Integer,Player>) in.readObject(); //get updated player information
						map.printObject.put(0, info.getPlayers().get(0)); //put dealer
						map.printObject.put(1, info.getPlayers().get(info.getScreenPlayer())); //display the player you wanted to see
						commandSent = "Idle";
					}
				}
	
				//set prompt on statspanel
				((Application)app).stats.setText("Would you like to hit?");
				((Application)app).stats.setTextSize(125);
				
				//E Request phase, hit or stay initiated by statsPanel - button click change variable here
				commandSent = "Idle";
				while(true){
					out.writeObject(commandSent); //9 wait for player action
					out.reset();
					command = (String) in.readObject();
					if(command.equals("Idle")){ 
						updateLoop++;
						if(updateLoop == 2000){ //after 2000 connections, will go to update
							updateLoop = 0;
							commandSent = "Update";
						}
					}
					else if(command.equals("ScreenSent")){ //9 update screen
						updateLoop = 0;
						info.getPlayers().clear();
						info.setPlayers((TreeMap<Integer, Player>) in.readObject()); //get updated player information
						map.printObject.put(0, info.getPlayers().get(0)); //put dealer
						map.printObject.put(1, info.getPlayers().get(info.getScreenPlayer())); //display the player you wanted to see
						commandSent = "Idle";
					}
					else if(command.equals("Busted")){ //9 process busted
						//set prompt on statspanel
						((Application)app).stats.setText("You busted.");
						((Application)app).stats.setTextSize(70);
						info.players.get(id).setStatus("Busted"); // set status
						info.setDeny(true);
						info.getPlayers().clear();
						info.setPlayers((TreeMap<Integer, Player>) in.readObject());  //get updated player information
						break;
					}
					else if(command.equals("HitDone")){ //9 hit processed...update screen
						info.getPlayers().clear();
						info.setPlayers((TreeMap<Integer, Player>) in.readObject());  //get updated player information
						commandSent = "Update";
					}			
					else if(command.equals("StayDone")){
						info.getPlayers().clear();
						info.setPlayers((TreeMap<Integer, Player>) in.readObject()); //get updated player information
						info.setDeny(true);
						//set prompt on statspanel
						((Application)app).stats.setText("Waiting");
						((Application)app).stats.setTextSize(50);
						info.setId((Integer)in.readObject()); //set up player panel
						break;
					}
					//implment more options later
					
				}
				
				
				//F Wait for game to finish
				commandSent = "Idle";
				while(true){
					out.writeObject(commandSent); //10 wait for player action
					out.reset();
					command = (String) in.readObject();
					if(command.equals("Idle")){
						info.setId((Integer)in.readObject()); //update player panel
						updateLoop++;
						if(updateLoop == 2000){ //after 2000 connections, will go to update
							updateLoop = 0;
							commandSent = "Update";
						}
					}
					else if(command.equals("ScreenSent")){ //10 update screen
						updateLoop = 0;
						info.players.clear();
						info.players = (TreeMap<Integer,Player>) in.readObject(); //get updated player information
						map.printObject.put(0, info.getPlayers().get(0)); //put dealer
						map.printObject.put(1, info.getPlayers().get(info.getScreenPlayer())); //display the player you wanted to see
						commandSent = "Idle";
					}
					else if (command.equals("Finish")){
						((Application)app).stats.enableCont(); //pause game (create continue button)
						((Application)app).stats.setText("Results");
						((Application)app).stats.setTextSize(45);
						updateLoop = 0;
						info.players.clear();
						info.players = (TreeMap<Integer,Player>) in.readObject(); //1 get updated player information
						map.printObject.put(0, info.getPlayers().get(0)); //put dealer
						map.printObject.put(1, info.getPlayers().get(info.getScreenPlayer())); //display the player you wanted to see
						info.setPhase((Integer)in.readObject()); //2 get game phase
						info.setId((Integer)in.readObject()); //3 update player panel
						System.out.println("Phase: " + info.getPhase());
						commandSent = "Idle";
						break;
					}
				}
				
				info.setPhase(8);
				check = false;
				//G wait for all players to push continue
				command = (String)in.readObject(); //check if player ran out of coins
				if(command.equals("Game Over")){
					JOptionPane.showMessageDialog(null, "Out of Coins, Game Over");
					try {
						((Application)app).gamer.s.close();
					} catch (IOException i) {
						i.printStackTrace();
					}
					((Application)app).reset();
				}
				while(true){
					if(check == true){ //check if continue variable is adjusted by the panel to ensure that continue is sent properly
						commandSent = "Continue";
					}
					out.writeObject(commandSent);
					out.reset();
					command = (String) in.readObject();
					if(command.equals("Idle")){
						updateLoop++;
						if(updateLoop == 2000){ //after 2000 connections, will go to update
							updateLoop = 0;
							commandSent = "Update";
						}
					}
					else if(command.equals("ScreenSent")){ //10 update screen
						updateLoop = 0;
						info.getPlayers().clear();
						info.setPlayers((TreeMap<Integer,Player>) in.readObject()); //get updated player information
						map.printObject.put(0, info.getPlayers().get(0)); //put dealer
						map.printObject.put(1, info.getPlayers().get(info.getScreenPlayer())); //display the player you wanted to see
						commandSent = "Idle";
					}
					else if(command.equals("Wait")){ //continue acknowledgement
						info.setPaused(false);
						((Application)app).stats.disableCont();
						((Application)app).stats.setText("Waiting");
						((Application)app).stats.setTextSize(45);
						check = false;
						commandSent = "Idle";
					}
					else if(command.equals("Restart")){ //restart game when game is setup
						System.out.println("waiting for restart");
						((Application)app).stats.setText("Waiting");
						((Application)app).stats.setTextSize(45);
						((Application)app).stats.disableCont();
						info.copy((Settings)in.readObject()); //copy new data
						System.out.println("data copied");
						info.setScreenPlayer(id);
						map.printObject.put(0, info.getPlayers().get(0)); //put dealer
						map.printObject.put(1, info.getPlayers().get(info.getScreenPlayer())); //display the player you wanted to see
						//turn off permissions
						info.setDeny(true);
						check = false;
						commandSent = "Idle";
						break;
					}
					if(!commandSent.equals("Idle")){ //debug output
						System.out.println(commandSent);
					}
				}
				
				//save coin data
				setCoins(info.getPlayers().get(id).getChips());
				for(int i = 0; i < profiles.size(); i++){
					if(profiles.getNames().equals(getName())){
						profiles.getCoins().set(i, getCoins());
					}
				}
				System.out.println("restarting");
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Connection Lost");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Connection Lost");
			((Application)app).reset();
			return;
		}
	}
	
	//save game
	public void save(String path){
		try{
			File f = new File(path);
			if(!path.equals("resetFile") && f.exists()){ //check for overwritingx
				int choice = JOptionPane.showConfirmDialog(null,  path + " Exists, Overwrite?", "Warning", JOptionPane.YES_NO_OPTION );
				if(choice == JOptionPane.NO_OPTION){
					return;
				}
			}
			FileOutputStream fileOut = new FileOutputStream(path); //write to settings.sav file (will overwrite)
	        ObjectOutputStream streamOut = new ObjectOutputStream(fileOut); //outputstream
	        streamOut.writeObject(getProfiles()); //save profiles
	        streamOut.close();
	        fileOut.close();
		}
		catch(IOException e){ //if unable to write to file or something is unserializable
			JOptionPane.showMessageDialog(null, "Failed to save game.", "Exception", JOptionPane.OK_OPTION);
			e.printStackTrace(); //print trace
			return;
		}
		if(!path.equals("resetFile")){
			JOptionPane.showMessageDialog(null, "Game saved."); //prompt user if game has successfully saved
		}
	}
	
	//load profiles
	public void load(String path){
		try{
			FileInputStream fileIn = new FileInputStream(path); //access  file
			ObjectInputStream streamIn = new ObjectInputStream(fileIn); //inputstream
			getProfiles().copy((ProfileList) streamIn.readObject()); //load profiles
			streamIn.close();
			fileIn.close();
		}
        catch(IOException i){ //if file not found
        	JOptionPane.showMessageDialog(null,"Could not find " + path +  " file.", "Exception", JOptionPane.OK_OPTION);
        	return;
        }
		catch(ClassNotFoundException e){ //if casting error
			System.out.println("Class not found");
			e.printStackTrace();
			return;
		}
		if(!path.equals("resetFile")){
			JOptionPane.showMessageDialog(null, "Game loaded.");
			((Application)app).createMenu.update();
		}
	}

	public synchronized Integer getId() {
		return id;
	}

	public synchronized void setId(Integer id) {
		this.id = id;
	}

	public synchronized PaintMap getMap() {
		return map;
	}

	public synchronized void setMap(PaintMap map) {
		this.map = map;
	}

	public synchronized String getCommandSent() {
		return commandSent;
	}

	public synchronized void setCommandSent(String commandSent) {
		this.commandSent = commandSent;
	}

	public synchronized Integer getUpdateLoop() {
		return updateLoop;
	}

	public synchronized void setUpdateLoop(Integer updateLoop) {
		this.updateLoop = updateLoop;
	}	
	
	public synchronized String getName() {
		return name;
	}

	public synchronized void setName(String name) {
		this.name = name;
	}

	public synchronized ProfileList getProfiles() {
		return profiles;
	}

	public synchronized void setProfiles(ProfileList profiles) {
		this.profiles = profiles;
	}

	public synchronized String getServerName() {
		return serverName;
	}

	public synchronized void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public synchronized Integer getCoins() {
		return coins;
	}

	public synchronized void setCoins(Integer coins) {
		this.coins = coins;
	}

	public synchronized Thread getThread() {
		return thread;
	}

	public synchronized void setThread(Thread thread) {
		this.thread = thread;
	}

	public synchronized Boolean getCheck() {
		return check;
	}

	public synchronized void setCheck(Boolean check) {
		this.check = check;
	}
}
