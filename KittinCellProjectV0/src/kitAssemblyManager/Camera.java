package kitAssemblyManager;

import java.io.Serializable;

public class Camera implements Serializable{

	private double x;
	private double y;
	
	private boolean takePicture;
	private boolean flashUp;
	private boolean flashDown;
	
	private float opacity;
	private boolean animationDone;
	
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
        /*if(takePicture){
            takePicture = false;
            return true;
        }
        else {
            return false;
        }*/
    	return takePicture;
    }
	
	public void update() {
        if(takePicture){
            if(flashUp){
                opacity += 0.1;
                if(opacity >= 1){
                	flashUp = false;
                	flashDown = true;
                }
            }
            else if(flashDown){
            	opacity -= 0.1;
            	if(opacity <= 0){
            		opacity = 0;
            		flashDown = false;
            		takePicture = false;
            		animationDone = true;
            	}	            			
            }
        }
	}
	
}
