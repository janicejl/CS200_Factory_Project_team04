package PartsManager;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

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
	
	public void paintComponent(Graphics g){
		revalidate();
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
