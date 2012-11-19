package fire;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import javax.swing.*;


public class Fire extends JPanel implements ActionListener{
	BufferedImage img = null;			// Image for drawing fire off-screen
	Graphics g2;		// Graphics object for painting on img
	Random rng;			// Random number generator
	Timer timer, t2, t3;		// A timer to tick every 3 ms
	Color colors[];		// Color gradient array
	int values[][];		// 2D array of fire color indices
	int color1, color2;
	int height, width;
	int res;			// fire resolution
	int fireLevel;
	int rows, cols;

	// Initialize applet
	public Fire(int w, int h){
		// RANDOM NUMBER GENERATOR
		rng = new Random();

		height = h;
		width = w;
		res = 2;
		fireLevel = 0;
		rows = (height / res) - 1;
		cols = (width / res) - 1;
		
		
		this.setSize(width,height);
		this.setMinimumSize(getSize());
		this.setMaximumSize(getSize());
		this.setPreferredSize(getSize());
		color1 = 1;
		color2 = 99; // The default
		
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		//img = createImage(width, height);
		g2 = img.getGraphics();		

		// COLOR GRADIENT
		init_colors_red();

		// 2D FIRE ARRAY
		init_values();
		// ANIMATION TIMER
		
		timer = new Timer(10, this); // update timer
		timer.start();
		t2 = new Timer(20, this); // display timer
		t2.start();
		t3 = new Timer(50, this); // fire rise timer
		t3.start();
		
	}
	
	// Initialize array for color gradient: white - orange - red - black
	public void init_colors_red(){	
		Color c1 = new Color(0,0,0);		// BLACK		
		Color c2 = new Color(255,0,0);	// RED
		Color c3 = new Color(255,170,0);	// ORANGE
		Color c4 = new Color(255,255,255);	// WHITE
		
		init_colors(c1,c2,c3,c4);
	}

	// Initialize color array with gradient from color c1 to c2 to c3 to c4
	public void init_colors(Color c1,Color c2,Color c3,Color c4){
		colors = new Color [100];
		for (int i = 0; i < 33; i++){
			double f = ((double)i) / 32;
			colors[i] = new Color(
				clamp((int)( c1.getRed() + (f * (c2.getRed() - c1.getRed())) )),
				clamp((int)( c1.getGreen() + (f * (c2.getGreen() - c1.getGreen())) )),
				clamp((int)( c1.getBlue() + (f * (c2.getBlue() - c1.getBlue())) ))
			);
		}
		for (int i = 33; i < 66; i++){
			double f = ((double)(i - 33)) / 32;
			colors[i] = new Color(
				clamp((int)( c2.getRed() + (f * (c3.getRed() - c2.getRed())) )),
				clamp((int)( c2.getGreen() + (f * (c3.getGreen() - c2.getGreen())) )),
				clamp((int)( c2.getBlue() + (f * (c3.getBlue() - c2.getBlue())) ))
			);
		}
		for (int i = 66; i < 100; i++){
			double f = ((double)(i - 66)) / 32;
			colors[i] = new Color(
				clamp((int)( c3.getRed() + (f * (c4.getRed() - c3.getRed())) )),
				clamp((int)( c3.getGreen() + (f * (c4.getGreen() - c3.getGreen())) )),
				clamp((int)( c3.getBlue() + (f * (c4.getBlue() - c3.getBlue())) ))
			);
		}
	}

	// Clamp input to range [0,..,255]
	public int clamp(int i)	{
		if (i < 0) return(0);
		if (i > 255) return(255);
		return(i);
	}

	// Initialize 2D array of color indices for fire calculations
	public void init_values(){
		int rows = (height / res);
		int cols = (width / res);
		values = new int [rows][cols];
		for (int r = 0; r < rows; r++){
			for (int c = 0; c < cols; c++){
				values[r][c] = 0;
			}
		}	
	}

	public void actionPerformed(ActionEvent e)	{
		if (e.getSource() == timer){	
			speckle();		// Modify the bottom row for the fire
			disperse();		// Apply the simple fire algorithm everywhere else
		}
		if (e.getSource() == t2){
			this.getGraphics().drawImage(img,0,0,this);
			
		}
		if (e.getSource() == t3){
			fireLevel++;
			System.out.println(fireLevel);
			for (int r = 0; r < rows; r++){
				if(r != rows - 1){
					for (int c = 1; c < cols; c++){
						values[r][c] = values[r+1][c];
					}					
				}
				else {
					for (int c = 1; c < cols; c++){
						values[r][c] = values[r-1][c];
					}
				}				
			}			
		}
	}

	// Modify the bottom row of pixels for the fire
	public void speckle() {
		int r = (height / res) - 1;
		int c = (width / res);
		for (int i = 0; i < c; i++)		{
			int x = rng.nextInt(25);			
			if (x == 0) { // 1/25 chance of changing bottom row pixel to black	       
				values[r][i] = color1;				
			}
			else if (x == 1) { // 1/25 chance of changing bottom row pixel to white
				values[r][i] = color2;
			}
			// 23/25 chance of leaving bottom pixel alone
		}
	}

	// Apply fire algorithm everywhere except bottom row and side columns
	public void disperse() {		
		for (int r = 0; r < rows; r++){
			for (int c = 1; c < cols; c++){
				int sum = values[r+1][c-1] + values[r+1][c] + values[r+1][c+1];
				if(r < rows - fireLevel + 14){
					values[r][c] = (sum / 3) - 1;
				}
				else {
					values[r][c] = (int)((sum / 3.0) - 0.0); 
				}
				
				if (values[r][c] < 0) values[r][c] = 0;
				g2.setColor(colors[values[r][c]]);
				g2.fillRect(c*res,r*res,res,res);
			}
		}
	}
}