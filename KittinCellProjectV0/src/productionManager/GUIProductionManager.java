package productionManager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUIProductionManager extends JPanel{
	
	ProductionManagerApp app;
	GUIProdKAM kitGraphics;
	
	public GUIProductionManager(ProductionManagerApp _app){
		app = _app;
		
		kitGraphics = new GUIProdKAM(this);
		
	}
}
