package tryan.inq.overhead;

import tryan.inq.controls.QGameController;

/**
 * This class serves as the entry point into the program. 
 * 
 * Its responsibilities include loading resources and major systems, presenting 
 * the player with the main menu (?), managing load time game settings, and launching 
 * the game controller.
 * 
 * @author Thomas Ryan (June 2016)
 *
 */

public class QGameLoader {
	
	public static void main(String[] args) {
		// Creating loading screen if needed
		
		// Loading resources
		QResourceManager resMan = new QResourceManager();
		new QResourceLoader().loadResources(resMan);
					
		// Creating game controller
		new QGameController(resMan, new QGameSettings());
	}
	
}
