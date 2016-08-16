package ryan.tom.inq.gfx;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.TreeMap;

// Note: Weather and time systems will go here

public class QSceneState {
	private int sceneWidth;
	private int sceneHeight;
	private int minCamX;
	private int minCamY;
	private int maxCamX;
	private int maxCamY;
	private int id;
	private QPlayerState playerState;
	private QCameraState camState;
	private TreeMap<Integer, QActorState> actorStates;
	private TreeMap<Integer, QSceneryState> sceneryStates;
	private ArrayList<QInteractableState> interactableStates;
	private QInteractableState highlightedActor;
	private QPathingMap pathingMap;
	
	public QSceneState(int width, int height, QPlayerState playerState, int minCamX, int minCamY, int maxCamX, int maxCamY) {
		sceneWidth = width;
		sceneHeight = height;
		this.minCamX = minCamX;
		this.minCamY = minCamY;
		this.maxCamX = maxCamX;
		this.maxCamY = maxCamY;
		id = QGameState.generateSceneId();
		actorStates = new TreeMap<Integer, QActorState>();
		sceneryStates = new TreeMap<Integer, QSceneryState>();
		interactableStates = new ArrayList<QInteractableState>();
		highlightedActor = null;
		
		this.playerState = playerState;
		this.camState = null;
		pathingMap = null;
		
	}
	
	public void attachCameraState(QCameraState camState) {
		camState.setMinX(minCamX);
		camState.setMinY(minCamY);
		camState.setMaxX(maxCamX);
		camState.setMaxY(maxCamY);
		this.camState = camState;
	}
	
	public void addActorState(QActorState actor) {
		actorStates.put(actor.getActorId(), actor);
	}
	
	public void addSceneryState(QSceneryState actor) {
		sceneryStates.put(actor.getActorId(), actor);
	}
	
	public void addInteractableState(QInteractableState actor) {
		interactableStates.add(actor);
		actorStates.put(actor.getActorId(), actor);
	}
	
	public void attachPathingMap(QPathingMap pathingMap) {
		this.pathingMap = pathingMap;
	}
	
	public void resolveMouseClick(int x, int y) {
		QInteractableState foundActor = findInteractableByLocation(x + camState.getX(), y + camState.getY());
			
		if(foundActor != null) {
			foundActor.onUse();
		}
	}
	
	public void resolveMousePosition(QCoords mousePos) {
		QInteractableState foundActor = findInteractableByLocation(mousePos.getX() + camState.getX(), mousePos.getY() + camState.getY());	
		
		if(foundActor != null) {
			highlightedActor = foundActor;
		} else if(highlightedActor != null) {
			highlightedActor.removeHighlight();
			highlightedActor = null;
		}
		
		if(highlightedActor != null) {
			foundActor.onHover();
		}
	}
	
	public void resolveCameraMovement(int tickTime, QDirection direction) {
		camState.onCommand(tickTime, direction);
	}
	
	// Note: This could be abstracted into game mode movement classes like side-scrolling, top-down, or isometric
	public void resolvePlayerMovement(int tickTime, QDirection direction) {
		if(direction.equals(QDirection.E) || direction.equals(QDirection.W)) {
			playerState.walk(direction);
		} else if(direction.equals(QDirection.N)) {
			playerState.jump(direction);
		} else if(direction.equals(QDirection.NE) || direction.equals(QDirection.NW)) {
			playerState.walk(direction);
			playerState.jump(direction);
		}
	}
	
	public void resolveSceneryMovement(int tickTime, QDirection direction) {
		// Note: On the lookout for a better solution to the camera+scenery movement linking problem
		if(camState.isMoving()) {
			for(int id : sceneryStates.keySet()) {
				sceneryStates.get(id).onCommand(tickTime, direction);
			}
		}
	}
	
	public void onTick(int tickTime, QDirection direction) {
		for(int id : sceneryStates.keySet()) {
			sceneryStates.get(id).onTick(tickTime, direction);
		}
		
		for(int id : actorStates.keySet()) {
			actorStates.get(id).onTick(tickTime, direction);
		}
		
		playerState.onTick(tickTime, direction);
		
		camState.onTick(tickTime, direction);
	}
	
	public QInteractableState findInteractableByLocation(int x, int y) {
		QInteractableState foundActor = null;
		
		/*
		 * Note: For now searching active area for collisions will be enough
		 * 		 Will eventually need to put colliding actors into heap and
		 * 		 return the highest click priority
		 */	
		
		for(QInteractableState interactable : interactableStates) {
			if(interactable.containsCoords(x, y)) {
				foundActor = interactable;
			}
		}
		
		return foundActor;
	}
	
	public int getSceneId() {
		return id;
	}
	
	//////////////
	// Debugging
	///////////
	public void setPathingCellType(int x, int y, QPathType pathType) {
		pathingMap.setPathingCellType(x, y, pathType);
	}
	
	public void drawPathingOverlay(Graphics2D g2) {
		pathingMap.draw(g2);
	}
}
