package PartsManager;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class PartsPanel extends JPanel implements ActionListener{
	
	PartsManagerApp app;
	JComboBox imagesSelectBox;
	ArrayList<JButton> removeButtons;
	JButton removeAllButton, clearButton, createButton;
	JTextField nameField;
	
	public PartsPanel(PartsManagerApp _app){
		app = _app;
		
	}
	public void actionPerformed(ActionEvent e){
		
	}
}
