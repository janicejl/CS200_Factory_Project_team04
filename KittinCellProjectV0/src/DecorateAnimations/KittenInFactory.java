package DecorateAnimations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class KittenInFactory extends JPanel implements ActionListener{
	ArrayList<Kitten> kittens;
	Timer timer;
	int time=0;
	int intime=0;
	int rand=1;
	boolean turnOn=true;
	public KittenInFactory() {
		kittens = new ArrayList<Kitten>();
		kittens.add(new Kitten(new JLabel(new ImageIcon("images/kk1.png"))));
		kittens.add(new Kitten(new JLabel(new ImageIcon("images/kk2.png"))));
		kittens.add(new Kitten(new JLabel(new ImageIcon("images/kk3.png"))));
		kittens.add(new Kitten(new JLabel(new ImageIcon("images/kk4.png"))));
		kittens.add(new Kitten(new JLabel(new ImageIcon("images/kk5.png"))));
		kittens.add(new Kitten(new JLabel(new ImageIcon("images/kk6.png"))));
		kittens.add(new Kitten(new JLabel(new ImageIcon("images/kk7.png"))));
		kittens.add(new Kitten(new JLabel(new ImageIcon("images/kk8.png"))));
		kittens.add(new Kitten(new JLabel(new ImageIcon("images/kk0.png"))));
		
		this.setVisible(true);
		this.setPreferredSize(new Dimension(1200,600));
		this.setMinimumSize(new Dimension(1200,600));
		this.setMaximumSize(new Dimension(1200,600));
		//this.setBackground(Color.green);
		this.setOpaque(false);
		
		setLayout(null);

		Insets insets = this.getInsets();
		for(int i=0;i<kittens.size();i++){
			this.add(kittens.get(i).getImage());
		}

			kittens.get(0).getImage().setBounds(0, 590, 
					kittens.get(0).getSize().width, kittens.get(0).getSize().height); //set the original location of kittens

			kittens.get(1).getImage().setBounds(1180, 590, 
					kittens.get(1).getSize().width, kittens.get(1).getSize().height); 
			
			//System.out.println(""+kittens.get(2).getSize().width);
			kittens.get(2).getImage().setBounds(0-kittens.get(2).getSize().width, 400,
					kittens.get(2).getSize().width, kittens.get(2).getSize().height); 
			
			kittens.get(3).getImage().setBounds(200, 590,
					kittens.get(3).getSize().width, kittens.get(3).getSize().height);

			kittens.get(4).getImage().setBounds(0-kittens.get(4).getSize().width, 180,
					kittens.get(4).getSize().width, kittens.get(4).getSize().height);
			
			kittens.get(5).getImage().setBounds(100, 0-kittens.get(5).getSize().height,
					kittens.get(5).getSize().width, kittens.get(5).getSize().height);
			
			kittens.get(6).getImage().setBounds(1180, 0-kittens.get(6).getSize().height,
					kittens.get(6).getSize().width, kittens.get(6).getSize().height);
			
			kittens.get(7).getImage().setBounds(800, 0-kittens.get(7).getSize().height,
					kittens.get(7).getSize().width, kittens.get(7).getSize().height);
			
			kittens.get(8).getImage().setBounds(760, 590, 
					kittens.get(8).getSize().width, kittens.get(8).getSize().height);
			
			
			this.repaint();
			this.revalidate();// update
			
			timer = new Timer(20,this);
			timer.start();
	}

	
	public static void main(String[] args) {
		JFrame j = new JFrame();
		KittenInFactory k = new KittenInFactory();
		j.setSize(1200,600);
		j.setVisible(true);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.add(k);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(turnOn){
		for(int i=0;i<9;i++){
			if(kittens.get(i).isFinished()){
				if(i!=8){
					rand=-1;
					if(time==10){
						rand=i+1;
						kittens.get(i).setFinished(false);
					}
				}
				else {
					rand=-1;
					if(time==10){
						rand=0;
						kittens.get(i).setFinished(false);
					}
				}
			}
		}
		
		if(rand==0||rand==3||rand==8){//movement of kitten1 and 4 and 9
			int condition = -1;
			int x=rand;
			if(kittens.get(x).isHide()&& !kittens.get(x).isAppeared()){
				condition=0;
			}
			else if(!kittens.get(x).isHide() && kittens.get(x).isAppeared()){
				condition=1;
			}
			else if (kittens.get(x).isHide()&& kittens.get(x).isAppeared()){
				condition=2;
			}
			else if (!kittens.get(x).isHide()&& !kittens.get(x).isAppeared()){
				condition=3;
			}
			if(condition==3){
				if (kittens.get(x).getImage().getLocation().y>(580-kittens.get(x).size.height)){
					kittens.get(x).getImage().setLocation(kittens.get(x).getImage().getLocation().x, kittens.get(x).getImage().getLocation().y-1);
				}
				else{
					intime++;
					if(intime==10){
						kittens.get(x).setAppeared(true);
						intime=0;
					}
				}
			}
			if(condition==1){
				if(kittens.get(x).getImage().getLocation().y<(600)){
					kittens.get(x).getImage().setLocation(kittens.get(x).getImage().getLocation().x, kittens.get(x).getImage().getLocation().y+1);
				}
				else{
					kittens.get(x).setHide(true);
					kittens.get(x).setAppeared(false);
					kittens.get(x).setFinished(true);
					time=0;
				}
			}
			if(condition==0){
				kittens.get(x).setHide(false);
			}
		}
		
		
		else if(rand==1){ //movement of kitten 2
			int condition = -1;
			if(kittens.get(1).isHide()&& !kittens.get(1).isAppeared()){
				condition=0;
			}
			else if(!kittens.get(1).isHide() && kittens.get(1).isAppeared()){
				condition=1;
			}
			else if (kittens.get(1).isHide()&& kittens.get(1).isAppeared()){
				condition=2;
			}
			else if (!kittens.get(1).isHide()&& !kittens.get(1).isAppeared()){
				condition=3;
			}
			if(condition==3){
				if (kittens.get(1).getImage().getLocation().y>(580-kittens.get(1).size.height)){
					kittens.get(1).getImage().setLocation(kittens.get(1).getImage().getLocation().x-1, kittens.get(1).getImage().getLocation().y-1);
				}
				else{
					intime++;
					if(intime==10){
						kittens.get(1).setAppeared(true);
						intime=0;
					}
				}
			}
			if(condition==1){
				if(kittens.get(1).getImage().getLocation().y<(600)){
					kittens.get(1).getImage().setLocation(kittens.get(1).getImage().getLocation().x+1, kittens.get(1).getImage().getLocation().y+1);
				}
				else{
					kittens.get(1).setHide(true);
					kittens.get(1).setAppeared(false);
					kittens.get(1).setFinished(true);
					time=0;
				}
			}
			if(condition==0){
				kittens.get(1).setHide(false);
			}
		}
		
		if(rand==2||rand==4){//movement of kitten 3
			int condition = -1;
			int x=rand;
			if(kittens.get(x).isHide()&& !kittens.get(x).isAppeared()){
				condition=0;
			}
			else if(!kittens.get(x).isHide() && kittens.get(x).isAppeared()){
				condition=1;
			}
			else if (kittens.get(x).isHide()&& kittens.get(x).isAppeared()){
				condition=2;
			}
			else if (!kittens.get(x).isHide()&& !kittens.get(x).isAppeared()){
				condition=3;
			}
			if(condition==3){
				if (kittens.get(x).getImage().getLocation().x<0){
					kittens.get(x).getImage().setLocation(kittens.get(x).getImage().getLocation().x+1, kittens.get(x).getImage().getLocation().y);
				}
				else{
					intime++;
					if(intime==10){
						kittens.get(x).setAppeared(true);
						intime=0;
					}
				}
			}
			if(condition==1){
				if(kittens.get(x).getImage().getLocation().x>0-kittens.get(x).getSize().width){
					kittens.get(x).getImage().setLocation(kittens.get(x).getImage().getLocation().x-1, kittens.get(x).getImage().getLocation().y);
				}
				else{
					kittens.get(x).setHide(true);
					kittens.get(x).setAppeared(false);
					kittens.get(x).setFinished(true);
					time=0;
				}
			}
			if(condition==0){
				kittens.get(x).setHide(false);
			}
		}
		
		if(rand==5||rand==7){//movement of kitten 6 and 8
			int condition = -1;
			int x=rand;
			if(kittens.get(x).isHide()&& !kittens.get(x).isAppeared()){
				condition=0;
			}
			else if(!kittens.get(x).isHide() && kittens.get(x).isAppeared()){
				condition=1;
			}
			else if (kittens.get(x).isHide()&& kittens.get(x).isAppeared()){
				condition=2;
			}
			else if (!kittens.get(x).isHide()&& !kittens.get(x).isAppeared()){
				condition=3;
			}
			if(condition==3){
				if (kittens.get(x).getImage().getLocation().y<0){
					kittens.get(x).getImage().setLocation(kittens.get(x).getImage().getLocation().x, kittens.get(x).getImage().getLocation().y+1);
				}
				else{
					intime++;
					if(intime==10){
						kittens.get(x).setAppeared(true);
						intime=0;
					}
				}
			}
			if(condition==1){
				if(kittens.get(x).getImage().getLocation().y>-kittens.get(x).getSize().height){
					kittens.get(x).getImage().setLocation(kittens.get(x).getImage().getLocation().x, kittens.get(x).getImage().getLocation().y-1);
				}
				else{
					kittens.get(x).setHide(true);
					kittens.get(x).setAppeared(false);
					kittens.get(x).setFinished(true);
					time=0;
				}
			}
			if(condition==0){
				kittens.get(x).setHide(false);
			}
		}
		
		if(rand==6){//movement of kitten 7
			int condition = -1;
			int x=rand;
			if(kittens.get(x).isHide()&& !kittens.get(x).isAppeared()){
				condition=0;
			}
			else if(!kittens.get(x).isHide() && kittens.get(x).isAppeared()){
				condition=1;
			}
			else if (kittens.get(x).isHide()&& kittens.get(x).isAppeared()){
				condition=2;
			}
			else if (!kittens.get(x).isHide()&& !kittens.get(x).isAppeared()){
				condition=3;
			}
			if(condition==3){
				if (kittens.get(x).getImage().getLocation().y<-10){
					kittens.get(x).getImage().setLocation(kittens.get(x).getImage().getLocation().x-1, kittens.get(x).getImage().getLocation().y+1);
				}
				else{
					intime++;
					if(intime==10){
						kittens.get(x).setAppeared(true);
						intime=0;
					}
				}
			}
			if(condition==1){
				if(kittens.get(x).getImage().getLocation().y>0-kittens.get(x).getSize().height){
					kittens.get(x).getImage().setLocation(kittens.get(x).getImage().getLocation().x+1, kittens.get(x).getImage().getLocation().y-1);
				}
				else{
					kittens.get(x).setHide(true);
					kittens.get(x).setAppeared(false);
					kittens.get(x).setFinished(true);
					time=0;
				}
			}
			if(condition==0){
				kittens.get(x).setHide(false);
			}
		}
		
		
		time++;
		this.revalidate();
		this.repaint();
		}
	}


	public synchronized Timer getTimer() {
		return timer;
	}


	public synchronized void setTimer(Timer timer) {
		this.timer = timer;
	}


	public synchronized boolean isTurnOn() {
		return turnOn;
	}


	public synchronized void setTurnOn(boolean turnOn) {
		this.turnOn = turnOn;
	}

	


}
