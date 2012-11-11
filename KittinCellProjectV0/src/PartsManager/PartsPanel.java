package PartsManager;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;

public class PartsPanel extends JPanel implements ActionListener{
	
	PartsManagerApp app;
	ManagePanel managePanel;
	PartListPanel partListPanel;
	
	public PartsPanel(PartsManagerApp _app){
		app = _app;
		managePanel = new ManagePanel();
		partListPanel = new PartListPanel();
		
		setLayout(new GridLayout(2,1));
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(managePanel);
		add(partListPanel);
		
		//add all the buttons on managePanel to ActionListener
		for(int i=0;i<managePanel.manageButtons.size();i++){
			managePanel.manageButtons.get(i).addActionListener(this);
		}
	}
	
	public void actionPerformed(ActionEvent ae){
		
		if(ae.getSource()==managePanel.manageButtons.get(0)){
			//create button
			partListPanel.addPart(managePanel.nameField.getText());
			managePanel.nameField.setText("");
		}
		else if(ae.getSource()==managePanel.manageButtons.get(1)){
			//clear button
			managePanel.nameField.setText("");
		}
		else if(ae.getSource()==managePanel.manageButtons.get(2)){
			//remove all button
			partListPanel.removeAll();
		}
	}
	
	public class ManagePanel extends JPanel{
		
		Vector<ImageIcon> partImages;
		ArrayList<JButton> manageButtons;
		JTextField nameField;
		JComboBox imagesSelectBox;
		ComboBoxRenderer renderer;
		
		public ManagePanel(){
			manageButtons = new ArrayList<JButton>();
			nameField =  new JTextField();
			nameField.setPreferredSize(new Dimension(175, 25));
			nameField.setMaximumSize(new Dimension(175, 25));
			nameField.setMinimumSize(new Dimension(175, 25));
			partImages = new Vector<ImageIcon>();
			for(int i=0;i<10;i++){
				partImages.add(new ImageIcon("images/"+i+".png"));
			}
			imagesSelectBox = new JComboBox(partImages);
			imagesSelectBox.setPreferredSize(new Dimension(175, 25));
			imagesSelectBox.setMaximumSize(new Dimension(175, 25));
			imagesSelectBox.setMinimumSize(new Dimension(175, 25));
			
			createButtons();
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			//c.weightx = 1.0;
			//c.weighty = 0.1;
			c.insets = new Insets(20, 30, 20, 30);
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 3;
			add(imagesSelectBox, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 3;
			add(nameField, c);
			
			c.gridy = 0;
			c.gridx = 3;
			c.gridwidth = 1;
			c.gridheight = 2;
			add(manageButtons.get(0), c);
			
			//c.weightx = 1.0;
			//c.weighty = 0.1;
			c.insets = new Insets(20, 50, 20, 15);
			c.gridx = 1;
			c.gridy = 2;
			c.gridheight = 1;
			add(manageButtons.get(1), c);
			
			c.insets = new Insets(20, 30, 20, 30);
			c.gridy = 2;
			c.gridx = 3;
			add(manageButtons.get(2), c);
			
		}
		
		private void createButtons(){
			manageButtons.add(new JButton("Create"));
			manageButtons.add(new JButton("Clear"));
			manageButtons.add(new JButton("Remove All"));
			for(int i=1;i<3;i++){
				manageButtons.get(i).setPreferredSize(new Dimension(125, 40));
				manageButtons.get(i).setMaximumSize(new Dimension(125, 40));
				manageButtons.get(i).setMinimumSize(new Dimension(125, 40));
			}
			manageButtons.get(0).setPreferredSize(new Dimension(125, 50));
			manageButtons.get(0).setMaximumSize(new Dimension(125, 50));
			manageButtons.get(0).setMinimumSize(new Dimension(125, 50));
		}
		
		public class ComboBoxRenderer extends JLabel implements ListCellRenderer{

			
			public Component getListCellRendererComponent(JList arg0, Object arg1,
					int arg2, boolean arg3, boolean arg4) {
				// TODO Auto-generated method stub
				return null;
			}
			
		}
	}
	
	
	public class PartListPanel extends JPanel implements ActionListener{
		ArrayList<JLabel> partLabels;
		ArrayList<JButton> removeButtons;
		JScrollPane scrollPane;
		JPanel basePanel, labelPanel, buttonPanel;
		public PartListPanel(){
			partLabels = new ArrayList<JLabel>();
			removeButtons = new ArrayList<JButton>();
			scrollPane = new JScrollPane();
			labelPanel = new JPanel();
			basePanel = new JPanel();
			buttonPanel = new JPanel();
			
			labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
			buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
			basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.X_AXIS));
			basePanel.add(labelPanel);
			basePanel.add(buttonPanel);
			
			scrollPane.add(basePanel);
			add(basePanel);
			
		}
		
		public void addPart(String name){
			partLabels.add(new JLabel(name));
			removeButtons.add(new JButton("Remove"));
			labelPanel.add(partLabels.get(partLabels.size()-1));
			buttonPanel.add(removeButtons.get(removeButtons.size()-1));
			
			removeButtons.get(removeButtons.size()-1).setActionCommand("remove"+(removeButtons.size()-1));
			removeButtons.get(removeButtons.size()-1).addActionListener(this);
			revalidate();
			
		}
		
		public void removeAll(){
			for(int i=0;i<partLabels.size();i++){
				labelPanel.remove(partLabels.get(i));
			}
			for(int i=0;i<removeButtons.size();i++){
				buttonPanel.remove(removeButtons.get(i));
			}
			
			partLabels.clear();
			removeButtons.clear();
			revalidate();
		}
		
		public void actionPerformed(ActionEvent ae){
			
			for(int i=0;i<removeButtons.size();i++){
				if(("remove"+i).equals(ae.getActionCommand())){
					labelPanel.remove(partLabels.get(i));
					buttonPanel.remove(removeButtons.get(i));
					partLabels.remove(i);
					removeButtons.remove(i);
					revalidate();
					break;
				}
			}
			
		}
		
	}
	
	
}