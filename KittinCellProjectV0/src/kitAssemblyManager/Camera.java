package kitAssemblyManager;

import java.io.Serializable;

public class Camera implements Serializable{

	private double x;					//the x position of the camera
	private double y;					//the y position of the camera. 
	
	private boolean takePicture;		//Boolean for whether a picture should be taken
	private boolean flashUp;			//Boolean for whether the flashup animation should be done. 
	private boolean flashDown;			//Boolean for whether the flashdown animation should be done. 
	
	private float opacity;				//float for controlling the opacity of the flash animation. s
	private boolean animationDone;		//Boolean for whether or not the animation has finished drawing
	
	public Camera() {
		x = 0;
		y = 0;
		takePicture = false;
		
		flashDown = false;
        flashUp = false;
        animationDone = false;
        opacity = 0.0f;
	}
	
	public Camera(double nX, double nY) {
		
		x = nX;
        y = nY;
        takePicture = false;
		
		flashDown = false;
        flashUp = false;
        animationDone = false;
        opacity = 0.0f;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double nX) {
		x = nX;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double nY) {
		y = nY;
	}

	public void setTakePicture(boolean takePicture) {
		this.takePicture = takePicture;
	}
	
	public float getOpacity(){
		return opacity;
		
	}
	
	public boolean getAnimationDone(){
		return animationDone;
	}
	
	public void setAnimationDone(boolean b){
		animationDone = b;
	}

	public void takePicture() {
		takePicture = true;
		flashUp = true;
	}
	
    public void takePicture(double nX, double nY){
        takePicture = true;
        flashUp = true;
        x = nX;
        y = nY;
    }
    
    public boolean getTakePicture(){
    	return takePicture;
    }
	
	public void update() {
        if(takePicture){
            if(flashUp){					//increment the opacity 
                opacity += 0.02;
                if(opacity >= 1){			//if reach full opacity, start flashDown. 
                	flashUp = false;
                	flashDown = true;
                }
            }
            else if(flashDown){				//decrement the opacitys
            	opacity -= 0.02;
            	if(opacity <= 0){			// if opacity becomes zero, finish flash animation
            		opacity = 0;
            		flashDown = false;
            		takePicture = false;
            		animationDone = true;
            	}	            			
            }
        }
	}
	
}
