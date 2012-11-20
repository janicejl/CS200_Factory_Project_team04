package productionManager;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;

public class JobListPanel extends JPanel implements ActionListener{

	ProductionManagerApp app;
	ArrayList<JLabel> numLabels;
	JPanel numPanelBase;
	JScrollPane numScroll;
	
	ArrayList<JLabel> nameLabels;
	Integer nameMax;
	JPanel namePanelBase;
	JScrollPane nameScroll;
	
	ArrayList<JLabel> amtLabels;
	Integer amtMax;
	JPanel amtPanelBase;
	JScrollPane amtScroll;
	
	ArrayList<JButton> removeButtons;
	JPanel removePanelBase;
	JScrollPane removeScroll;
	
	JPanel scrollPanel;
	JScrollPane scrollPane;
	
	JPanel heading;
	JLabel jobNum;
	JLabel kitName;
	JLabel amt;	
	
	public JobListPanel(ProductionManagerApp _app){
		app = _app;
		setSize(400, 400);
		
		numLabels = new ArrayList<JLabel>();
		numPanelBase = new JPanel();
		numPanelBase.setLayout(new BoxLayout(numPanelBase, BoxLayout.Y_AXIS));
		numScroll = new JScrollPane();
		numScroll.getViewport().add(numPanelBase);		
		
		nameLabels = new ArrayList<JLabel>();
		nameMax = new Integer(0);
		namePanelBase = new JPanel();
		namePanelBase.setLayout(new BoxLayout(namePanelBase, BoxLayout.Y_AXIS));
		nameScroll = new JScrollPane();
		nameScroll.getViewport().add(namePanelBase);
		
		amtLabels = new ArrayList<JLabel>();
		amtMax = new Integer(0);
		amtPanelBase = new JPanel();
		amtPanelBase.setLayout(new BoxLayout(amtPanelBase, BoxLayout.Y_AXIS));
		amtScroll = new JScrollPane();
		amtScroll.getViewport().add(amtPanelBase);
		
		removeButtons = new ArrayList<JButton>();
		removePanelBase = new JPanel();
		removePanelBase.setLayout(new BoxLayout(removePanelBase, BoxLayout.Y_AXIS));
		removeScroll = new JScrollPane();
		removeScroll.getViewport().add(removePanelBase);
		
		scrollPanel = new JPanel();
		scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.X_AXIS));
		scrollPanel.add(numScroll);
		scrollPanel.add(nameScroll);
		scrollPanel.add(amtScroll);
		scrollPanel.add(removeScroll);
		
		scrollPane = new JScrollPane();
		scrollPane.getViewport().add(scrollPanel);
		scrollPane.setPreferredSize(new Dimension(350, 300));
		scrollPane.setMaximumSize(new Dimension(350, 300));
		scrollPane.setMinimumSize(new Dimension(350, 300));
		scrollPane.setAlignmentX(CENTER_ALIGNMENT);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		
		heading = new JPanel();
		heading.setLayout(new BoxLayout(heading, BoxLayout.X_AXIS));
		heading.setPreferredSize(new Dimension(350, 25));
		heading.setMaximumSize(new Dimension(350, 25));
		heading.setMinimumSize(new Dimension(350, 25));
		
		jobNum = new JLabel("   #");
		jobNum.setPreferredSize(new Dimension(85, 25));
		jobNum.setMaximumSize(new Dimension(85, 25));
		jobNum.setMinimumSize(new Dimension(85, 25));
		jobNum.setAlignmentX(CENTER_ALIGNMENT);
		kitName = new JLabel("Kit Name");
		kitName.setPreferredSize(new Dimension(135, 25));
		kitName.setMaximumSize(new Dimension(135, 25));
		kitName.setMinimumSize(new Dimension(135, 25));
		kitName.setAlignmentX(CENTER_ALIGNMENT);
		amt = new JLabel("Quantity");
		amt.setPreferredSize(new Dimension(80, 25));
		amt.setMaximumSize(new Dimension(80, 25));
		amt.setMinimumSize(new Dimension(80, 25));
		amt.setAlignmentX(CENTER_ALIGNMENT);
		heading.add(jobNum);
		heading.add(kitName);
		heading.add(amt);
		heading.setBorder(BorderFactory.createLineBorder(Color.black));
		add(heading, BorderLayout.NORTH);
		
		update();
	}
	
	public void create(String s, Integer num){
		JLabel nameTemp = new JLabel(s);
		int size = s.length();
		nameTemp.setPreferredSize(new Dimension(size*10, 25));
		nameTemp.setMaximumSize(new Dimension(size*10, 25));
		nameTemp.setMinimumSize(new Dimension(size*10, 25));
//		nameTemp.setAlignmentX(CENTER_ALIGNMENT);
		nameTemp.setAlignmentY(CENTER_ALIGNMENT);
		nameLabels.add(nameTemp);
		//check if new string needs to readjust max panel
		if(size*10 > nameMax){
			nameMax = size*10;
		}
		namePanelBase.add(nameTemp);
		
		JLabel amtTemp = new JLabel(num.toString());
	    size = Integer.toString(num).length();
		amtTemp.setPreferredSize(new Dimension(size*10, 25));
		amtTemp.setMaximumSize(new Dimension(size*10, 25));
		amtTemp.setMinimumSize(new Dimension(size*10, 25));
		amtTemp.setAlignmentX(CENTER_ALIGNMENT);
		amtTemp.setAlignmentY(CENTER_ALIGNMENT);
		amtLabels.add(amtTemp);
		if(size*10 > amtMax){
			amtMax = size*10;
		}
		amtPanelBase.add(amtTemp);
		
		JButton removeTemp = new JButton("Remove");
		removeTemp.setPreferredSize(new Dimension(80,21));
		removeTemp.setMaximumSize(new Dimension(80,21));
		removeTemp.setMinimumSize(new Dimension(80,21));
		removeTemp.setAlignmentX(CENTER_ALIGNMENT);
		removeTemp.setAlignmentY(CENTER_ALIGNMENT);
		removeTemp.addActionListener(this);
		removeButtons.add(removeTemp);
		removePanelBase.add(Box.createRigidArea(new Dimension(0, 2)));
		removePanelBase.add(removeTemp);
		removePanelBase.add(Box.createRigidArea(new Dimension(0, 2)));
		
		update();
	}
	
	public void removeJob(Integer num){
		nameLabels.remove((int)num);
		namePanelBase.remove((int)num);
		amtLabels.remove((int)num);
		amtPanelBase.remove((int)num);
		removeButtons.remove((int)num);
		removePanelBase.remove((int)(num*3));
		removePanelBase.remove((int)(num*3));
		removePanelBase.remove((int)(num*3));
		update();
	}
	
	public void update(){
		
		int size = nameLabels.size();
		
		//make new number labels
		numLabels.clear();
		numPanelBase.removeAll();
		
		for(int i = 0; i < size; i++){
			numLabels.add(new JLabel("  " + (i+1)));
			numLabels.get(i).setPreferredSize(new Dimension(20,25));
			numLabels.get(i).setMaximumSize(new Dimension(20,25));
			numLabels.get(i).setMinimumSize(new Dimension(20,25));
			numPanelBase.add(numLabels.get(i));
		}
		
		if(removeButtons.size() != 0 && app.getPanel().getListPanel().getStart().getText().equals("Update Production")){
			removeButtons.get(0).setEnabled(false);
		}
		
		
		size = Math.max(size, 14);
		numPanelBase.setPreferredSize(new Dimension(25,25));
		numPanelBase.setMaximumSize(new Dimension(25,25));
		numPanelBase.setMinimumSize(new Dimension(25,25));
		numScroll.setPreferredSize(new Dimension(30, size*26));
		numScroll.setMaximumSize(new Dimension(30, size*26));
		numScroll.setMinimumSize(new Dimension(30, size*26));
		
		namePanelBase.setPreferredSize(new Dimension(nameMax, size*25));
		namePanelBase.setMaximumSize(new Dimension(nameMax, size*25));
		namePanelBase.setMinimumSize(new Dimension(nameMax, size*25));
		nameScroll.setPreferredSize(new Dimension(175, size*26));
		nameScroll.setMaximumSize(new Dimension(175, size*26));
		nameScroll.setMinimumSize(new Dimension(175, size*26));
		
		amtPanelBase.setPreferredSize(new Dimension(amtMax, size*25));
		amtPanelBase.setMaximumSize(new Dimension(amtMax, size*25));
		amtPanelBase.setMinimumSize(new Dimension(amtMax, size*25));
		amtScroll.setPreferredSize(new Dimension(80, size*26));
		amtScroll.setMaximumSize(new Dimension(80, size*26));
		amtScroll.setMinimumSize(new Dimension(80, size*26));
		
		removePanelBase.setPreferredSize(new Dimension(80, size*25));
		removePanelBase.setMaximumSize(new Dimension(80, size*25));
		removePanelBase.setMinimumSize(new Dimension(80, size*25));
		removeScroll.setPreferredSize(new Dimension(90, size*26));
		removeScroll.setMaximumSize(new Dimension(90, size*26));
		removeScroll.setMinimumSize(new Dimension(90, size*26));		
		
		revalidate();
	}
	
	public void paintComponent(Graphics g){
		revalidate();
	}
	
	public void actionPerformed(ActionEvent e){
		for(int i = 0; i < removeButtons.size(); i++){
			if(e.getSource() == removeButtons.get(i)){
				removeJob(i);
				app.getClient().setCommandSent("Update");
				break;
			}
		}
	}
}
