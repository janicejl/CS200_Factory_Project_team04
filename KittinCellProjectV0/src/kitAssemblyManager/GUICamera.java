package kitAssemblyManager;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GUICamera {
	Camera camera;				//A reference to camera that will be passed in from the server through parts robot
	
    BufferedImage flash;		//Image file for flash
    boolean takePicture;		//Boolean for whether it is necessary to draw the animation for taking a picture. 
    boolean flashUp;			//Boolean for whether the flashing up is done. 
    boolean flashDown;			//Boolean for whether the flashing down is done.
    
    float opacity;				//Number to set the opacity of the flash in the animation.
    double x;					//x position to paint the flash animation
    double y;					//y position to paint the flash animation.
	
	public GUICamera(Camera c) {
		camera = c;
		
		opacity = 0.0f;
        x = 350;
        y = 100;
        
        try {
			flash = ImageIO.read(new File("images/flash.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setCamera(Camera c) {
		camera = c;
	}
	
	public void update() {
		x = camera.getX();
        y = camera.getY();
        opacity = camera.getOpacity();
        
        takePicture = camera.getTakePicture(); 
        
        if(takePicture){
            takePicture = true;
            flashUp = true;
        }
        
	}
	
	public void paintCamera(Graphics2D g2) {
		if(takePicture){
            try {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2.drawImage(flash,(int)x,(int)y,null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            }
            catch (Exception ignore){}
        }
	}
	
	public boolean getTakePicture() {
		return takePicture;
	}

	public void setTakePicture(boolean takePicture) {
		this.takePicture = takePicture;
	}
}
