package tryan.inq.overhead;

import tryan.inq.controls.QGameController;

/**
 * This class serves as the entry point into the program. 
 * 
 * Its responsibilities include: loading resources and major systems, presenting 
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
			
		// Loading main menu
		// Note: May need to abstract further since returning to the main menu cannot be done from outside of this class
		
		// Loading current game state
		QStateManager stateMan = new QStateManager();
		
		/* 
		 * Note: Controller instantiation needs to happen once QGraphics is loaded for menu UI to draw
		 * 		 Maybe instantiate QGraphics and QGameController here, then pass gfx reference to controller?
		 */
		// Creating game controller
		new QGameController(resMan, new QGameSettings());
	}
	
}
