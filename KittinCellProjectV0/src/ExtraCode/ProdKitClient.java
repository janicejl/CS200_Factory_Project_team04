//package ExtraCode;
//
//import server.KitAssemblyManager;
//import server.KitRobot;
//import server.PartsRobot;
//import kitAssemblyManager.KitAssemblyClient;
//
//public class ProdKitClient extends KitAssemblyClient{
//
//	GUIProdKAM app;
//	
//	public ProdKitClient(GUIProdKAM _app){
//		app = _app;
//		commandSent = "Production Kit Client";
//	}
//	
//	public void updateThread(){
//		try {
//			app.setKitRobot((KitRobot)in.readObject());
//			app.setPartsRobot((PartsRobot)in.readObject());
//			app.setKitAssemblyManager((KitAssemblyManager)in.readObject());
//		} catch (Exception ignore){
//			ignore.printStackTrace();
//			System.exit(1);
//		}
//	}
//}
