package PartsManager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

public class PartListPanel extends JPanel implements ActionListener{
	
	PartsManagerApp app;
	ArrayList<JLabel> partLabels;
	ArrayList<JLabel> imageLabels;
	ArrayList<JButton> removeButtons;
	ArrayList<JPanel> horizPanel;
	JScrollPane scrollPane;
	JPanel basePanel, labelPanel, buttonPanel, titlePanel;
	JLabel nameLabel, imageLabel;
	
	public PartListPanel(PartsManagerApp _app){
		
		app = _app;
		setLayout(new BorderLayout());
		partLabels = new ArrayList<JLabel>();
		imageLabels = new ArrayList<JLabel>();
		removeButtons = new ArrayList<JButton>();
		scrollPane = new JScrollPane();
		labelPanel = new JPanel();
		basePanel = new JPanel();
		buttonPanel = new JPanel();
		titlePanel = new JPanel();
		horizPanel = new ArrayList<JPanel>();
		
		nameLabel = new JLabel("Name");
		nameLabel.setPreferredSize(new Dimension(50, 27));
		nameLabel.setMaximumSize(new Dimension(50, 27));
		nameLabel.setMinimumSize(new Dimension(50, 27));
		
		imageLabel = new JLabel("Image");
		imageLabel.setPreferredSize(new Dimension(50, 27));
		imageLabel.setMaximumSize(new Dimension(50, 27));
		imageLabel.setMinimumSize(new Dimension(50, 27));
		
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		 
		
		basePanel.setPreferredSize(new Dimension(300, 100));
		basePanel.setMaximumSize(new Dimension(300, 100));
		basePanel.setMinimumSize(new Dimension(300, 100));
		
		scrollPane.setPreferredSize(new Dimension(400, 200));
		scrollPane.setMaximumSize(new Dimension(400, 200));
		scrollPane.setMinimumSize(new Dimension(400, 200));
		scrollPane.getViewport().add(basePanel);
		
		titlePanel.add(Box.createRigidArea(new Dimension(20, 0)));
		titlePanel.add(nameLabel);
		titlePanel.add(Box.createRigidArea(new Dimension(70, 0)));
		titlePanel.add(imageLabel);
		
		add(titlePanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		
		TitledBorder border = new TitledBorder("Parts List");
		setBorder(border);
		
	}
	
	public void addPart(String name, ImageIcon image){
		JLabel temp = new JLabel(name);
		temp.setPreferredSize(new Dimension(70, 27));
		temp.setMaximumSize(new Dimension(70, 27));
		temp.setMinimumSize(new Dimension(70, 27));
		partLabels.add(temp);
		
		JLabel imageLabel = new JLabel(image);
		imageLabel.setPreferredSize(new Dimension(50, 27));
		imageLabel.setMaximumSize(new Dimension(50, 27));
		imageLabel.setMinimumSize(new Dimension(50, 27));
		imageLabels.add(imageLabel);
		
		JButton remove = new JButton("Remove");
		remove.setPreferredSize(new Dimension(100, 25));
		remove.setMaximumSize(new Dimension(100, 25));
		remove.setMinimumSize(new Dimension(100, 25));
		removeButtons.add(remove);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setPreferredSize(new Dimension(400, 30));
		panel.setMaximumSize(new Dimension(400, 30));
		panel.setMinimumSize(new Dimension(400, 30));
		panel.add(Box.createRigidArea(new Dimension(30, 0)));
		panel.add(temp);
		panel.add(Box.createRigidArea(new Dimension(30, 0)));
		panel.add(imageLabel);
		panel.add(Box.createRigidArea(new Dimension(70, 0)));
		panel.add(remove);
		horizPanel.add(panel);
		basePanel.add(horizPanel.get(horizPanel.size()-1));
		basePanel.setPreferredSize(new Dimension(350, horizPanel.size()*30));
		basePanel.setMaximumSize(new Dimension(350, horizPanel.size()*30));
		basePanel.setMinimumSize(new Dimension(350, horizPanel.size()*30));
		
		//labelPanel.add(partLabels.get(partLabels.size()-1));
		//buttonPanel.add(removeButtons.get(removeButtons.size()-1));
		
		removeButtons.get(removeButtons.size()-1).addActionListener(this);
		revalidate();
		
	}
	
	public void removeAll(){		
		for(int i = 0; i < horizPanel.size(); i++){
			horizPanel.get(i).removeAll();
			
		}
		horizPanel.clear();
		partLabels.clear();
		removeButtons.clear();
		basePanel.removeAll();
		
		revalidate();
	}
	
	public void paintComponent(Graphics g){
		revalidate();
	}
	
	public void actionPerformed(ActionEvent ae){
		
		for(int i=0;i<removeButtons.size();i++){
			if(ae.getSource() == removeButtons.get(i)){
				horizPanel.get(i).removeAll();
				horizPanel.remove(i);
				partLabels.remove(i);
				removeButtons.remove(i);
				basePanel.remove(i);
				revalidate();
				app.getPartsList().remove(i);
				break;
			}
		}
		app.updateEditPanel();
		
	}
	
}
