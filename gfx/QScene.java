package tryan.inq.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.TreeMap;

import tryan.inq.overhead.QResourceManager;
import tryan.inq.state.QSceneState;

public class QScene {
	private int sceneWidth;
	private int sceneHeight;
	private QSceneState sceneState;
	private QScenery backGroundImg;
	private QCameraActor camera;
	
	// Note: Need priority queue for discerning draw order?
	// 		 Or separate player and other important actors from actor map and draw last?
	//		 May only need priority for actors that can change their order
	TreeMap<Integer, QSceneryActor> scenery;
	TreeMap<Integer, QActor> actors;
	QActor player;
	
	// Temp debug states
	boolean isPathingOverlayEnabled;
	boolean ispBoundsEnabled;
	
	public QScene(int width, int height, QResourceManager resMan, BufferedImage bgImg) {
		sceneState = null;
		backGroundImg = new QScenery(0, 0, bgImg, 0);
		sceneWidth = width;
		sceneHeight = height;
		player = null;
		actors = new TreeMap<Integer, QActor>();
		scenery = new TreeMap<Integer, QSceneryActor>();
		camera = null;
		
		// Debugging
		isPathingOverlayEnabled = false;
		ispBoundsEnabled = false;
	}
	
	///////////////
	// updateView()
	////////////
	
	public void updateView(long tickTime) {	
		camera.updateView(tickTime);
		
		for(Integer id : scenery.keySet()) {
			scenery.get(id).updateView(tickTime);
		}
		
		for(int id : actors.keySet()) {
			actors.get(id).updateView(tickTime);
		}
		
		player.updateView(tickTime);
	}
	
	/////////
	// draw()
	//////
	
	public void draw(Graphics2D g2) {
		// Drawing background first
		backGroundImg.draw(g2);
		
		// Drawing mid ground scenery
		for(Integer id : scenery.keySet()) {
			scenery.get(id).draw(g2);
		}
		
		// Drawing actors
		for(Integer id : actors.keySet()) {
			actors.get(id).draw(g2);
		}
		
		// Drawing player
		player.draw(g2);
		
		// Drawing debug overlays last, temporary solution
		if(isPathingOverlayEnabled) {
			sceneState.drawPathingOverlay(g2);
		}
		
		if(ispBoundsEnabled) {
			player.drawActorBounds(g2);
			
			for(Integer id : actors.keySet()) {
				actors.get(id).drawActorBounds(g2);
			}
		}
	}
	
	/////////////////
	// Other Methods
	//////////////
	
	public void attachSceneState(QSceneState sceneState) {
		this.sceneState = sceneState;
	}
	
	// Note: Need another method later for mass add from editor or database
	public void addActor(QActor actor) {
		actors.put(actor.getActorId(), actor);
	}
	
	public void addPlayer(QActor player) {
		this.player = player;
	}
	
	public void addScenery(QSceneryActor actor) {
		scenery.put(actor.getActorId(), actor);
	}
		
	public void addCamera(QCameraActor cameraActor) {
		camera = cameraActor;
	}
		
	public void setBackgroundImg(BufferedImage backGroundImg) {
		this.backGroundImg = new QScenery(0, 0, backGroundImg, 0);
	}
	
	public int getSceneWidth() {
		return sceneWidth;
	}
	
	public int getSceneHeight() {
		return sceneHeight;
	}
	
	public QCameraActor getCamera() {
		return camera;
	}
			
	/////////////
	// Debugging
	//////////
	
	public void enablePathingOverlay(boolean isPathingOverlayEnabled) {
		this.isPathingOverlayEnabled = isPathingOverlayEnabled;
	}
	
	public void enableBoundsOverlay(boolean ispBoundsEnabled) {
		this.ispBoundsEnabled = ispBoundsEnabled;
	}
	
}
