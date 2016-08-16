package ryan.tom.inq.gfx;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.TreeMap;

import javax.swing.JPanel;

/*
 * Note: This will need to be split; need a renderer and a scene
 */

@SuppressWarnings("serial")
public class QScene extends JPanel {
	private int sceneWidth;
	private int sceneHeight;
	private QResourceManager resMan;
	private QSceneState sceneState;
	private QMouseManager mouseMan;
	private QScenery backGroundImg;
	
	// Note: Need priority queue for discerning draw order?
	// 		 or separate player and other important actors from actor map and draw last?
	//		 Only need priority for actors that can change their order...
	TreeMap<Integer, QSceneryActor> scenery;
	TreeMap<Integer, QActor> actors;
	QActor player;
	
	// Temp debug states
	boolean isPathingOverlayEnabled;
	boolean ispBoundsEnabled;
	
	public QScene(QResourceManager resMan, QMouseManager mouseMan) {
		this.resMan = resMan;
		this.mouseMan = mouseMan;
		sceneState = null;
		backGroundImg = new QScenery(0, 0, resMan.getImage("hills2.png"));
		sceneWidth = backGroundImg.getWidth();
		sceneHeight = backGroundImg.getHeight();
		player = null;
		actors = new TreeMap<Integer, QActor>();
		scenery = new TreeMap<Integer, QSceneryActor>();
		
		// Debugging
		isPathingOverlayEnabled = false;
		ispBoundsEnabled = false;
	}
		
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
			
	public void draw(Graphics2D bbg2) {
		// Draw background first
		backGroundImg.draw(bbg2);
		
		// Draw mid ground scenery next
		for(Integer id : scenery.keySet()) {
			scenery.get(id).draw(bbg2);
		}
		
		// Draw actors
		for(Integer id : actors.keySet()) {
			actors.get(id).draw(bbg2);
		}
		
		player.draw(bbg2);
		
		// Debugging overlays
		if(isPathingOverlayEnabled) {
			/* 
			 * Note: Pass camera bounds x, y, width, height to debug so it draws less
			 *		 Will speed things up a lot
			 *		 Might even want to draw the overlay with g2 directly
			 *		 ^ No, drawing with bbg2 will maintain alignment
			 */
			sceneState.drawPathingOverlay(bbg2);
		}
		
		if(ispBoundsEnabled) {
			player.drawActorBounds(bbg2);
			
			for(Integer id : actors.keySet()) {
				actors.get(id).drawActorBounds(bbg2);
			}
		}
	}
	
	public void setBackgroundImg(BufferedImage backGroundImg) {
		this.backGroundImg = new QScenery(0, 0, backGroundImg);
	}
	
	public int getSceneWidth() {
		return sceneWidth;
	}
	
	public int getSceneHeight() {
		return sceneHeight;
	}
		
	//////////////
	// updateView
	///////////
	
	public void updateView(long tickTime) {	
		for(Integer id : scenery.keySet()) {
			scenery.get(id).updateView(tickTime);
		}
		
		for(int id : actors.keySet()) {
			actors.get(id).updateView(tickTime);
		}
		
		player.updateView(tickTime);
				
		repaint();
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
