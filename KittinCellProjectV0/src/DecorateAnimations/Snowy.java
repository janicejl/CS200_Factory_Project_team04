package DecorateAnimations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.*;

public class Snowy extends JPanel implements ActionListener{
	private Timer timer = new Timer(100, this);
	private double x;
	private double y = 0;
	int time=0;
	ArrayList<Snow> snowflakes;
	
	public Snowy() {
		snowflakes=new ArrayList<Snow>();
		this.setPreferredSize(new Dimension(1200,500));
		this.setMinimumSize(new Dimension(1200,500));
		this.setMaximumSize(new Dimension(1200,500));
		//this.setBackground(Color.black);
		this.setOpaque(false);
		timer.start();
	}
		
	public void actionPerformed(ActionEvent e){
		for(int i=0;i<snowflakes.size();i++){
			if(snowflakes.get(i).isLeft()){
				x=Math.random()*10;
				if(x<2){
					snowflakes.get(i).setX(snowflakes.get(i).getX()+1);
					snowflakes.get(i).setLeft(false);
				}
				else{
					snowflakes.get(i).setX(snowflakes.get(i).getX()-1);
					snowflakes.get(i).setY(snowflakes.get(i).getY()+1);
					snowflakes.get(i).setLeft(true);
				}
			}
			else{
				x=Math.random()*10;
				if(x<8){
					snowflakes.get(i).setX(snowflakes.get(i).getX()+1);
					snowflakes.get(i).setLeft(false);
				}
				else{
					snowflakes.get(i).setX(snowflakes.get(i).getX()-1);
					snowflakes.get(i).setY(snowflakes.get(i).getY()+2);
					snowflakes.get(i).setLeft(true);
				}
			}
		}
	    repaint();
		revalidate();
	    time++;
	    if(time==100)
	    	time=0;
	        
	}
		


	protected void paintComponent(Graphics g){
		super.paintComponent(g); 
		Graphics2D g2 = (Graphics2D)g;
		
		if(time==0){
			for (int i=0;i<1;i++){
				snowflakes.add(new Snow());
			}
			time=0;
		}
		for (int i=0;i<snowflakes.size();i++){
			
			g2.drawImage(snowflakes.get(i).getSnowflake(),(int)snowflakes.get(i).getX(),
					(int)snowflakes.get(i).getY(),this);
	
		}
		
	}
		
	
	public static void main(String[] args) {
		JFrame j = new JFrame();
		Snowy s = new Snowy();
		j.setSize(1200,600);
		j.setVisible(true);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.add(s);
	}

	public synchronized Timer getTimer() {
		return timer;
	}

	public synchronized void setTimer(Timer timer) {
		this.timer = timer;
	}
	
	
}
