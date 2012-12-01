package DecorateAnimations;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class KittenQueue extends JPanel implements MouseListener{
	ArrayList<JLabel> kittens;
	
	public KittenQueue() {
		kittens = new ArrayList<JLabel>();
		kittens.add(new JLabel(new ImageIcon("images/kkk1.png")));
		kittens.add(new JLabel(new ImageIcon("images/kkk2.png")));
		kittens.add(new JLabel(new ImageIcon("images/kkk3.png")));
		kittens.add(new JLabel(new ImageIcon("images/kkk4.png")));
		kittens.add(new JLabel(new ImageIcon("images/kkk5.png")));
		kittens.add(new JLabel(new ImageIcon("images/kkk6.png")));
		kittens.add(new JLabel(new ImageIcon("images/kkk7.png")));
		kittens.add(new JLabel(new ImageIcon("images/kkk8.png")));
		kittens.add(new JLabel(new ImageIcon("images/kkk9.png")));
		
		this.setPreferredSize(new Dimension(100,590));
		this.setMinimumSize(new Dimension(100,590));
		this.setMaximumSize(new Dimension(100,590));
		//this.setBackground(Color.green);
		this.setOpaque(false);
		
		setLayout(null);
		for(int i=0;i<kittens.size();i++){
			this.add(kittens.get(i));
			kittens.get(i).addMouseListener(this);
			Insets insets = this.getInsets();
			Dimension size = kittens.get(i).getPreferredSize();
			kittens.get(i).setBounds(insets.left-35, 65*i + insets.top,
			             size.width, size.height); //set the original location of kittens
			//kittens.get(i).setLocation(10,0);
			this.repaint();
			this.revalidate();// update
		}
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent ae) {// when mouse entered a kitten's graphic, the kitten comes out
		// TODO Auto-generated method stub
		for(int i=0;i<kittens.size();i++){
			if(ae.getSource()==kittens.get(i)){
				Insets insets = this.getInsets();
				Dimension size = kittens.get(i).getPreferredSize();
				while(kittens.get(i).getLocation().x<0){
					kittens.get(i).setLocation(kittens.get(i).getLocation().x+1, kittens.get(i).getLocation().y);
				}
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent ae) {// whenthe mouse leaves, the kitten hides back
		for(int i=0;i<kittens.size();i++){
			if(ae.getSource()==kittens.get(i)){
				Insets insets = this.getInsets();
				Dimension size = kittens.get(i).getPreferredSize();
				while(kittens.get(i).getLocation().x>-35){
					kittens.get(i).setLocation(kittens.get(i).getLocation().x-1, kittens.get(i).getLocation().y);
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
