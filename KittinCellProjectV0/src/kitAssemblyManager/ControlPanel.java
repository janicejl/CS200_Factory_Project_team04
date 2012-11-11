package kitAssemblyManager;

import java.util.*;
import java.awt.*;
import javax.swing.*;

import server.KitAssemblyManager;
import server.KitRobot;
import server.PartsRobot;

import java.awt.event.*;
import java.io.*;

public class ControlPanel extends JPanel implements ActionListener{
    JPanel controlPanel;
    GridBagConstraints gbc;
    ArrayList<JButton> buttons;
    String[] nests = { "Nest 1", "Nest 2", "Nest 3", "Nest 4", "Nest 5", "Nest 6", "Nest 7", "Nest 8" };
    String[] grips = { "Gripper 1", "Gripper 2", "Gripper 3", "Gripper 4"};

    JComboBox nestList;
    JComboBox gripList;


    public ControlPanel(){
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        
        controlPanel = new JPanel(new GridBagLayout());
        buttons = new ArrayList<JButton>();

        createButton("Connect KitRobot");
        createButton("Connect PartsRobot");
        createButton("Load Config");
        createButton("Spawn Kit");
        createButton("Take Picture");
        createButton("Load Stand 1");
        createButton("Load Stand 2");
        createButton("Check 1");
        createButton("Check 2");
        createButton("Remove Finished");
        createButton("Get Part");
        createButton("Load Kit 1");
        createButton("Load Kit 2");
        nestList = new JComboBox(nests);
        gripList = new JComboBox(grips);
        nestList.setSelectedIndex(0);
        //nestList.addActionListener(this);
        gripList.setSelectedIndex(0);
        //gripList.addActionListener(this);

        gbc.insets = new Insets(2,2,2,2);
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridx = 1;
        int i = 0;
        for(i = 0; i < buttons.size(); i++){
            gbc.gridy = i;
            add(buttons.get(i), gbc);
        }
        gbc.gridy = i +1;
        add(nestList, gbc);
        gbc.gridy = i +2;
        add(gripList, gbc);
    }

    public void createButton(String s){
        JButton temp = new JButton(s);
        //temp.addActionListener(this);
        temp.setActionCommand(s);
        buttons.add(temp);
    }

    /*

    public void input(){
        File config = new File("config.txt");
        try {
            Scanner scanner = new Scanner(config);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split("\\,");
                if("kam".equals(data[0])){
                    kitAssemblyManager.processCommand("spawn");
                }
                else if ("kr".equals(data[0])) {
                    kitRobot.addCommand(line.substring(line.indexOf(',')+1));
                }
                else if ("pr".equals(data[0])){
                    partsRobot.addCommand(line.substring(line.indexOf(',')+1));
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void disableButtons(){
    	for(int i = 0; i < buttons.size(); i++){
    		buttons.get(i).setEnabled(false);
    	}
    }
    */
    
    public void actionPerformed(ActionEvent ae) {
    	if("Connect KitRobot".equals(ae.getActionCommand())){
    		int i = kitClient.connect();
    		if(i == -1){
    			System.exit(1);
    		}
    		else if(i == 1){
    			kitClient.getThread().start();
    		}
    		//disableButtons();
    	}
    	else if ("Connect PartsRobot".equals(ae.getActionCommand())){
    		int i = partsClient.connect();
    		if(i == -1){
    			System.exit(1);
    		}
    		else if(i == 1){
    			partsClient.getThread().start();
    		}
    		//disableButtons();
    	}
    }
    	/*
    	else if("Load Config".equals(ae.getActionCommand())) {
            input();
        }
        else if("Spawn Kit".equals(ae.getActionCommand())) {
            for (int i = 0; i < 99; i++) {
            kitAssemblyManager.processCommand("spawn");
            }
        }
        else if("Take Picture".equals(ae.getActionCommand())) {
            partsRobot.takePicture(295, 150);
        }
        else if("Load Stand 1".equals(ae.getActionCommand())) {
            kitRobot.addCommand("load,0,1");
        }
        else if("Load Stand 2".equals(ae.getActionCommand())) {
            kitRobot.addCommand("load,0,2");
        }
        else if("Check 1".equals(ae.getActionCommand())) {
            kitRobot.addCommand("load,1,5");
        }
        else if("Check 2".equals(ae.getActionCommand())) {
            kitRobot.addCommand("load,2,5");
        }
        else if("Remove Finished".equals(ae.getActionCommand())) {
            kitRobot.addCommand("load,5,6");
        }
        else if("Get Part".equals(ae.getActionCommand())) {
            int nest = (int)nestList.getSelectedIndex();
            int grip = (int)gripList.getSelectedIndex();
            partsRobot.addCommand("grab," + nest + "," + grip + ",5" );
        }
        else if("Load Kit 1".equals(ae.getActionCommand())) {
            int nest = (int)nestList.getSelectedIndex();
            int grip = (int)gripList.getSelectedIndex();
            partsRobot.addCommand("dump,0");
        }
        else if("Load Kit 2".equals(ae.getActionCommand())) {
            int nest = (int)nestList.getSelectedIndex();
            int grip = (int)gripList.getSelectedIndex();
            partsRobot.addCommand("dump,1");
        }

    }*/
}
