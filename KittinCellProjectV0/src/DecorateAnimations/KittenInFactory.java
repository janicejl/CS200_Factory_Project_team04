package DecorateAnimations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

public class KittenInFactory extends JPanel{
	ArrayList<Kitten> kittens;// list of kittens
	//Timer timer;
	int time=0; //record time
	int intime=0; //for little delay
	int rand=1; //which kitten goes up
	boolean turnOn=true; //whether animation is on or off
	public KittenInFactory() {
		kittens = new ArrayList<Kitten>();
		kittens.add(new Kitten("images/kk1.png"));
		kittens.add(new Kitten("images/kk2.png"));
		kittens.add(new Kitten("images/kk3.png"));
		kittens.add(new Kitten("images/kk4.png"));
		kittens.add(new Kitten("images/kk5.png"));
		kittens.add(new Kitten("images/kk6.png"));
		kittens.add(new Kitten("images/kk7.png"));
		kittens.add(new Kitten("images/kk8.png"));
		kittens.add(new Kitten("images/kk0.png"));
		
		kittens.get(0).setXY(0, 600);
		kittens.get(1).setXY(1210, 600);
		kittens.get(2).setXY(0-kittens.get(2).getSize().width, 400);
		kittens.get(3).setXY(200, 600);
		kittens.get(4).setXY(-kittens.get(4).getSize().width, 180);
		kittens.get(5).setXY(100, 0-kittens.get(5).getSize().height);
		kittens.get(6).setXY(1200,0-kittens.get(5).getSize().height-20);
		kittens.get(7).setXY(800,0-kittens.get(5).getSize().height-10);
		kittens.get(8).setXY(760,600);
		//System.out.println(""+kittens.get(2).getSize().width);
		
		this.setVisible(true);
		this.setPreferredSize(new Dimension(1200,600));
		this.setMinimumSize(new Dimension(1200,600));
		this.setMaximumSize(new Dimension(1200,600));
		//this.setBackground(Color.green);
		this.setOpaque(false);

		
		//timer = new Timer(20,this);
		//timer.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g); 
		Graphics2D g2 = (Graphics2D)g;
		for (int i=0;i<kittens.size();i++){
			g2.drawImage(kittens.get(i).getImage(),(int)kittens.get(i).getX(),
					(int)kittens.get(i).getY(),this);
		}
	}

	public void update() {
		if(turnOn){
		for(int i=0;i<9;i++){
			if(kittens.get(i).isFinished()){
				if(i!=8){
					rand=-1;
					if(time==2){
						rand=i+1;
						System.out.println(""+rand);
						kittens.get(i).setFinished(false);
					}
				}
				else {
					rand=-1;
					if(time==2){
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
				if (kittens.get(x).getY()>(590-kittens.get(x).getSize().height)){
					kittens.get(x).setY(kittens.get(x).getY()-3);
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
				if(kittens.get(x).getY()<(600)){
					kittens.get(x).setY(kittens.get(x).getY()+3);
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
				if (kittens.get(1).getY()>(600-kittens.get(1).getSize().height)){
					kittens.get(1).setX(kittens.get(1).getX()-3);
					kittens.get(1).setY(kittens.get(1).getY()-3);
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
				if(kittens.get(1).getY()<(600)){
					kittens.get(1).setX(kittens.get(1).getX()+3);
					kittens.get(1).setY(kittens.get(1).getY()+3);
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
		
		if(rand==2||rand==4){//movement of kitten 3 and 5
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
				if (kittens.get(x).getX()<-10){
					kittens.get(x).setX(kittens.get(x).getX()+3);
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
				if(kittens.get(x).getX()>0-kittens.get(x).getSize().width){
					kittens.get(x).setX(kittens.get(x).getX()-3);
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
				if (kittens.get(x).getY()<0){
					kittens.get(x).setY(kittens.get(x).getY()+3);
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
				if(kittens.get(x).getY()>-kittens.get(x).getSize().height){
					kittens.get(x).setY(kittens.get(x).getY()-3);
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
				if (kittens.get(x).getY()<-20){
					kittens.get(x).setX(kittens.get(x).getX()-3);
					kittens.get(x).setY(kittens.get(x).getY()+3);
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
				if(kittens.get(x).getY()>0-kittens.get(x).getSize().height){
					kittens.get(x).setX(kittens.get(x).getX()+3);
					kittens.get(x).setY(kittens.get(x).getY()-3);
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
		}
		repaint();
	}

	public static void main(String[] args) {
		JFrame j = new JFrame();
		KittenInFactory k = new KittenInFactory();
		j.setSize(1200,600);
		j.setVisible(true);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.add(k);
	}


	public synchronized boolean isTurnOn() {
		return turnOn;
	}


	public synchronized void setTurnOn(boolean turnOn) {
		this.turnOn = turnOn;
	}

	


}
