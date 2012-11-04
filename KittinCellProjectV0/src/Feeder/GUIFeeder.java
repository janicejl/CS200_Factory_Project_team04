package Feeder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GUIFeeder {
	
	Feeder feeder;
	BufferedImage image;
	Rectangle feederStatus;
	
	public GUIFeeder(Feeder f){
		feeder = f;
		feederStatus = new Rectangle((int)feeder.getX() + 4, 
					(int)feeder.getY() + 86,
					75, 10);
		try {
			image = ImageIO.read(new File("images/feeder.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintNest(Graphics2D g2){
		double x = feeder.getX();
		double y = feeder.getY();
		int statusWidth;
		
		g2.drawImage(image, (int)x, (int)y, null);
		g2.setColor(Color.ORANGE);
		if (feeder.getPartAmount() > 0){
			statusWidth = (int)feeder.getPartAmount()*3;
		}
		else {
			statusWidth = 0;
		}
		feederStatus.setSize(statusWidth, 10);
		g2.fill(feederStatus);
	}

	
	
}
