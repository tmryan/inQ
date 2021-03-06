package tryan.inq.state;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.TreeMap;

import tryan.inq.event.QAreaTrigger;
import tryan.inq.event.QGameEventManager;
import tryan.inq.event.QTimedEvent;
import tryan.inq.mobility.QCoords;
import tryan.inq.mobility.QDirection;
import tryan.inq.mobility.QPathType;
import tryan.inq.mobility.QPathingMap;

// Note: Weather and time systems will belong to QSceneState

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
	private QGameEventManager eventMan;
	
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
		eventMan = new QGameEventManager();
	}
	
	public void onTick(long tickTime, QDirection direction) {
		for(int id : sceneryStates.keySet()) {
			sceneryStates.get(id).onTick(tickTime, direction);
		}
		
		for(int id : actorStates.keySet()) {
			actorStates.get(id).onTick(tickTime, direction);
		}
		
		playerState.onTick(tickTime, direction);
		
		camState.onTick(tickTime, direction);
		
		resolvePhysics();
		
		// Note: Check all area triggers and timed events here
		eventMan.checkAreaTriggers(playerState);
		eventMan.checkTimedEvents(tickTime);
		
		for(QInteractableState interactable : interactableStates) {
			eventMan.addGameEvent(interactable.checkAreaTrigger(playerState.getX(), playerState.getY(), playerState));
		}
		
		// Play queued game events
		playNextEvent();
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
	
	public void addAreaTrigger(QAreaTrigger areaTrigger) {
		eventMan.addAreaTrigger(areaTrigger);
	}
	
	public void addTimedEvent (QTimedEvent timedEvent) {
		eventMan.addTimedEvent(timedEvent);
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
	
	public void resolveCameraMovement(long tickTime, QDirection direction) {
		camState.onCommand(tickTime, direction);
	}
	
	// Note: This could be abstracted into game mode movement classes like side-scrolling, top-down, or isometric
	public void resolvePlayerMovement(long tickTime, QDirection direction) {
		if(direction.equals(QDirection.E) || direction.equals(QDirection.W)) {
			playerState.walk(direction);
		} else if(direction.equals(QDirection.N)) {
			playerState.jump(direction);
		} else if(direction.equals(QDirection.NE) || direction.equals(QDirection.NW)) {
			playerState.walk(direction);
			playerState.jump(direction);
		}
	}
	
	public void resolveSceneryMovement(long tickTime, QDirection direction) {
		// Note: On the lookout for a better solution to the camera+scenery movement linking problem
		if(camState.isMoving()) {
			for(int id : sceneryStates.keySet()) {
				sceneryStates.get(id).onCommand(tickTime, direction);
			}
		}
	}
	
	public void resolvePhysics() {
		if(playerState.fall()){
			camState.move(QDirection.S);
		}
	}
	
	public void playNextEvent() {
		// Note: Should these events be executed one per tick or all? Set a limit? Use greedy?
		if(eventMan.hasNextEvent()) {
			eventMan.getNextEvent().playEvent();
		}
	}
	
	public QInteractableState findInteractableByLocation(int x, int y) {
		QInteractableState foundActor = null;
		
		/*
		 * Note: For now searching active area for collisions will be enough
		 * 		 Will eventually need to put colliding actors into heap and
		 * 		 return the highest click priority
		 * 
		 * Note: Limiting this search to hoverable actors for now, will fix logic later
		 */	
		
		for(QInteractableState interactable : interactableStates) {
			if(interactable.containsCoords(x, y) && interactable.isHighlightable()) {
				foundActor = interactable;
			}
		}
		
		return foundActor;
	}
	
	public int getSceneId() {
		return id;
	}
	
	public int getWidth() {
		return sceneWidth;
	}
	
	public int getHeight() {
		return sceneHeight;
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
