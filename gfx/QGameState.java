package tryan.inq.gfx;

import java.util.HashMap;

public class QGameState {
	private static int nextActorId;
	private static int nextSceneid;
	private QMouseManager mouseMan;
	private QKeyboardManager keyMan;
	private QPlayerState playerState;
	private QCameraState camState;
	private HashMap<Integer, QSceneState> sceneStates;
	private int currentScene;
	
	public QGameState(QMouseManager mouseMan, QKeyboardManager keyMan) {
		nextActorId = 1;
		nextSceneid = 1;
		this.mouseMan = mouseMan;
		this.keyMan = keyMan;
		playerState = null;
		camState = null;
		sceneStates = new HashMap<Integer, QSceneState>();
		currentScene = 0;
	}
	
	// Note: These may end up in some utility class
	public static int generateActorId() {
		return nextActorId++;
	}
	
	public static int generateSceneId() {
		return nextSceneid++;
	}
	
	/*
	 * Note: This eventually needs to decide what sort of order to be giving based on click location.
	 * 		 So QSceneState will need to check for bounds within the grid space and see who they belong to?
	 */
	public void resolveMouseClick(int x, int y) {
		sceneStates.get(currentScene).resolveMouseClick(x, y);
	}
	
	public void resolveMousePosition() {
		sceneStates.get(currentScene).resolveMousePosition(mouseMan.getCurrentMouseCoords());
	}
		
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
		System.out.println(sceneState.getSceneId());
		sceneStates.put(sceneState.getSceneId(), sceneState);
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
	
	public int getCurrentSceneID() {
		return sceneStates.get(currentScene).getSceneId();
	}
	
	public void onTick(int tickTime) {
		QDirection direction = resolveKeyCommands(tickTime);
		
		resolveMousePosition();
		sceneStates.get(currentScene).onTick(tickTime, direction);
	}
}
