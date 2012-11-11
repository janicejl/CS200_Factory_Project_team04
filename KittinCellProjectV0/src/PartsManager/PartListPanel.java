package PartsManager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class PartListPanel extends JPanel implements ActionListener{
	ArrayList<JLabel> partLabels;
	ArrayList<JLabel> imageLabels;
	ArrayList<JButton> removeButtons;
	ArrayList<JPanel> horizPanel;
	JScrollPane scrollPane;
	JPanel basePanel, labelPanel, buttonPanel;
	public PartListPanel(){
		setLayout(new BorderLayout());
		partLabels = new ArrayList<JLabel>();
		imageLabels = new ArrayList<JLabel>();
		removeButtons = new ArrayList<JButton>();
		scrollPane = new JScrollPane();
		labelPanel = new JPanel();
		basePanel = new JPanel();
		buttonPanel = new JPanel();
		horizPanel = new ArrayList<JPanel>();
		
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));
		//basePanel.add(labelPanel);
		//basePanel.add(buttonPanel);
		basePanel.setPreferredSize(new Dimension(300, 100));
		basePanel.setMaximumSize(new Dimension(300, 100));
		basePanel.setMinimumSize(new Dimension(300, 100));
		
		scrollPane.setPreferredSize(new Dimension(300, 200));
		scrollPane.setMaximumSize(new Dimension(300, 200));
		scrollPane.setMinimumSize(new Dimension(300, 200));
		scrollPane.getViewport().add(basePanel);
		add(scrollPane, BorderLayout.CENTER);
		
	}
	
	public void addPart(String name, ImageIcon image){
		JLabel temp = new JLabel(name);
		temp.setPreferredSize(new Dimension(100, 27));
		temp.setMaximumSize(new Dimension(100, 27));
		temp.setMinimumSize(new Dimension(100, 27));
		partLabels.add(temp);
		
		JLabel imageLabel = new JLabel(image);
		imageLabel.setPreferredSize(new Dimension(100, 27));
		imageLabel.setMaximumSize(new Dimension(100, 27));
		imageLabel.setMinimumSize(new Dimension(100, 27));
		imageLabels.add(imageLabel);
		
		JButton remove = new JButton("Remove");
		remove.setPreferredSize(new Dimension(100, 27));
		remove.setMaximumSize(new Dimension(100, 27));
		remove.setMinimumSize(new Dimension(100, 27));
		removeButtons.add(remove);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setPreferredSize(new Dimension(400, 30));
		panel.setMaximumSize(new Dimension(400, 30));
		panel.setMinimumSize(new Dimension(400, 30));
		panel.add(temp);
		panel.add(imageLabel);
		panel.add(Box.createRigidArea(new Dimension(20, 0)));
		panel.add(remove);
		horizPanel.add(panel);
		basePanel.add(horizPanel.get(horizPanel.size()-1));
		basePanel.setPreferredSize(new Dimension(400, horizPanel.size()*30));
		basePanel.setMaximumSize(new Dimension(400, horizPanel.size()*30));
		basePanel.setMinimumSize(new Dimension(400, horizPanel.size()*30));
		
		//labelPanel.add(partLabels.get(partLabels.size()-1));
		//buttonPanel.add(removeButtons.get(removeButtons.size()-1));
		
		removeButtons.get(removeButtons.size()-1).addActionListener(this);
		revalidate();
		
	}
	
	public void removeAll(){
		/*for(int i=0;i<partLabels.size();i++){
			labelPanel.remove(partLabels.get(i));
		}
		for(int i=0;i<removeButtons.size();i++){
			buttonPanel.remove(removeButtons.get(i));
		}
		*/
		
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
				/*labelPanel.remove(partLabels.get(i));
				buttonPanel.remove(removeButtons.get(i));*/
				partLabels.remove(i);
				removeButtons.remove(i);
				basePanel.remove(i);
				revalidate();
				break;
			}
		}
		
	}
	
}
