package kitAssemblyManager;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class KitAssemblyManagerTester extends JFrame implements ActionListener{
    JPanel controlPanel;
    GridBagConstraints gbc;
    ArrayList<JButton> buttons;
    String[] nests = { "Nest 1", "Nest 2", "Nest 3", "Nest 4", "Nest 5", "Nest 6", "Nest 7", "Nest 8" };
    String[] grips = { "Gripper 1", "Gripper 2", "Gripper 3", "Gripper 4"};

    JComboBox nestList;
    JComboBox gripList;

    GUIKitAssemblyManager kamPanel;
    KitAssemblyManager kitAssemblyManager;
    KitRobot kitRobot;
    PartsRobot partsRobot;

    public KitAssemblyManagerTester(){
        kitAssemblyManager = new KitAssemblyManager();
        partsRobot = new PartsRobot();
        kitRobot = new KitRobot(kitAssemblyManager);
        setTitle("Kit Assembly Manager Tester");
        getContentPane().setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        new Thread(kitAssemblyManager).start();
        new Thread(kitRobot).start();
        new Thread(partsRobot).start();

        initControlPanel();
        initGraphicsPanel();

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(kamPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(controlPanel, gbc);

        pack();
        setVisible(true);
    }

    public void createButton(String s){
        JButton temp = new JButton(s);
        temp.addActionListener(this);
        temp.setActionCommand(s);
        buttons.add(temp);
    }

    public void initControlPanel(){
        controlPanel = new JPanel(new GridBagLayout());
        buttons = new ArrayList<JButton>();

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
        nestList.addActionListener(this);
        gripList.setSelectedIndex(0);
        gripList.addActionListener(this);

        gbc.insets = new Insets(2,2,2,2);
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridx = 1;
        int i = 0;
        for(i = 0; i < buttons.size(); i++){
            gbc.gridy = i;
            controlPanel.add(buttons.get(i), gbc);
        }
        gbc.gridy = i +1;
        controlPanel.add(nestList, gbc);
        gbc.gridy = i +2;
        controlPanel.add(gripList, gbc);
    }

    public KitRobot getKitRobot(){
        return kitRobot;
    }

    public PartsRobot getPartsRobot(){
        return partsRobot;
    }

    public KitAssemblyManager getKitAssemblyManager(){
        return kitAssemblyManager;
    }

    public void initGraphicsPanel(){
        kamPanel = new GUIKitAssemblyManager(this);
    }

    public static void main(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
           // handle exception
        }
        catch (ClassNotFoundException e) {
           // handle exception
        }
        catch (InstantiationException e) {
           // handle exception
        }
        catch (IllegalAccessException e) {
           // handle exception
        }
        KitAssemblyManagerTester frame = new KitAssemblyManagerTester();

        frame.pack();
        frame.setVisible(true);
    }

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

    public void actionPerformed(ActionEvent ae) {
        if("Load Config".equals(ae.getActionCommand())) {
            input();
        }
        else if("Spawn Kit".equals(ae.getActionCommand())) {
            for (int i = 0; i < 99; i++) {
            kitAssemblyManager.processCommand("spawn");
            }
        }
        else if("Take Picture".equals(ae.getActionCommand())) {
            partsRobot.takePicture();
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
            partsRobot.addCommand("grab," + nest + "," + grip);
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

    }
}
