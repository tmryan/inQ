package ryan.tom.inq.gfx;

import java.util.ArrayList;

public class QGameState {
	private static int nextActorId;
	private QMouseManager mouseMan;
	private QKeyboardManager keyMan;
	private QPlayerState playerState;
	private QCameraState camState;
	// Note: If there are enough levels, a tree might be better for faster searching
	private ArrayList<QSceneState> sceneStates;
	private int currentScene;
	
	public QGameState(QMouseManager mouseMan, QKeyboardManager keyMan) {
		nextActorId = 1;
		this.mouseMan = mouseMan;
		this.keyMan = keyMan;
		playerState = null;
		camState = null;
		sceneStates = new ArrayList<QSceneState>();
		currentScene = 0;
	}
	
	public static int generateActorId() {
		return nextActorId++;
	}
	
	/*
	 * Note: This eventually needs to decide what sort of order to be giving based on click location.
	 * 		 So QSceneState will need to check for bounds within the grid space and see who they belong to?
	 */
	public void resolveMouseClick(int x, int y) {
		sceneStates.get(currentScene).resolveMouseClick(x, y);
	}
	
	public void resolveMouseMove(int x, int y, boolean mouseEntered) {
		// Note: Sometimes this causes an exception... index out of bounds index 0 size 0
//		sceneStates.get(currentScene).resolveMouseMove(x,  y, mouseEntered);
	}
	
	public void resolveMousePosition() {
		sceneStates.get(currentScene).resolveMousePosition(mouseMan.getCurrentMouseCoords());
	}
	
	/* 
	 * 
	 * Movement Notes!
	 * 
	 * Note: Once camera bounds reach an edge, player can move freely for the difference
	 * 	 	 so movable scenery needs to move with camera and player will move separately
	 * 
	 * Note: Need better implementation here, is murky
	 * 		 Maybe have resolveMovement() called by a keyPressed() method which controller calls
	 * 
	 * Note: Camera centered with player until bounds hit edge?
	 * 
	 * Note: Camera needs to move with player when player hits mid game resolution
	 * 
	 * Note: Once camera hits bounds, player moves freely until its bounds collide
	 */ 
	
	// Note: This system could stand to be more elegant
	public QDirection resolveKeyCommands(int tickTime) {
		QDirection direction = null;
		
		// { W=0, S=1, A=2, D=3, SPACE=4, Q=5, E=6 }
		boolean[] keyState = keyMan.getKeyboardState();
		
		if(keyState[3]) {
			if(keyState[4]) {
				direction = QDirection.NE;
			} else {
				direction = QDirection.E;
			}

			sceneStates.get(currentScene).resolvePlayerMovement(tickTime, direction);
			sceneStates.get(currentScene).resolveCameraMovement(tickTime, direction);
			sceneStates.get(currentScene).resolveSceneryMovement(tickTime, QDirection.W);
		} else if (keyState[2]) {
			if(keyState[4]) {
				direction = QDirection.NW;
			} else {
				direction = QDirection.W;
			}
			
			sceneStates.get(currentScene).resolvePlayerMovement(tickTime, direction);
			sceneStates.get(currentScene).resolveCameraMovement(tickTime, direction);
			sceneStates.get(currentScene).resolveSceneryMovement(tickTime, QDirection.E);
		} else if(keyState[4]) {
			direction = QDirection.N;
			sceneStates.get(currentScene).resolvePlayerMovement(tickTime, direction);
		}
		
		return direction;
	}
	
	public void addSceneState(QSceneState sceneState) {
		sceneStates.add(sceneState);
	}
	
	public void attachCameraState(QCameraState cameraState) {
		camState = cameraState;
	}
	
	public void addPlayerState(QPlayerState playerState) {
		this.playerState = playerState;
	}
	
	public void loadScene(int sceneID) {
		currentScene = sceneID;
		sceneStates.get(currentScene).attachCameraState(camState);
	}
	
	public void onTick(int tickTime) {
		QDirection direction = resolveKeyCommands(tickTime);
		
		resolveMousePosition();
		sceneStates.get(currentScene).onTick(tickTime, direction);
	}
}
