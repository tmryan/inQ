package tryan.inq.gfx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import tryan.inq.gfx.QAnimUtils.QTransformation;
import tryan.inq.mobility.QDirection;
import tryan.inq.overhead.QResourceManager;
import tryan.inq.state.QActorState;
import tryan.inq.state.QAnimState;
import tryan.inq.state.QGameState;

/*
 * Note: QActor coordinates should be in screen space coordinates
 */
public class QActor {
	private int id;
	private int x;
	private int y;
	private int width;
	private int height;
	private int layer;
	private int tDelta;
	private int currentFrameLifetime;
	private QAnimation.AnimFrame currentAnimFrame;
	private BufferedImage defaultImg;
	// Note: May not need resMan in this class
	private QResourceManager resMan;
	private QActorState actorState;
	private QAnimState animState;
	private QAnimMap anims;
	private QAnimation.AnimMapIterator animIter;
	
	/* 
	 * Note: This will eventually be pulled from actor state on update and applied to animation frame timings
	 * 		 for cases where the actor speed has increased or decreased and the anim speed needs to reflect 
	 * 		 the change
	 */
	private double animSpeedMultiplier;

	public QActor(int x, int y, BufferedImage img, QResourceManager resMan, int layer) {
		id = QGameState.generateActorId();
		this.defaultImg = img;
		this.x = x;
		this.y = y;
		width = img.getWidth();
		height = img.getHeight();
		// Note: Layer may eventually be used for draw order
		this.layer = layer;
		tDelta = 0;
		currentFrameLifetime = 0;
		currentAnimFrame = null;
		this.resMan = resMan;
		actorState = null;
		animState = QAnimState.IDLE;
		anims = null;
		animIter = null;
		animSpeedMultiplier = 1.0;
	}
	
	public QActor(int x, int y, int width, int height, BufferedImage img, QResourceManager resMan, int layer) {
		id = QGameState.generateActorId();
		this.defaultImg = img;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		// Note: Layer may eventually be used for draw order
		this.layer = layer;
		tDelta = 0;
		currentFrameLifetime = 0;
		currentAnimFrame = null;
		this.resMan = resMan;
		actorState = null;
		animState = QAnimState.IDLE;
		anims = null;
		animIter = null;
		animSpeedMultiplier = 1.0;
	}
	
	// Update view from current model state
	public void updateView(long tickTime) {
		tDelta += tickTime;
		
		x = actorState.getX();
		y = actorState.getY();
		animState = actorState.getCurrentAnimState();
		
		if(animIter != null) {
			manageAnims();
		}
	}
	
	public void manageAnims() {
		if(!animIter.hasNextFrame()) {
			if(animState.isLooping()) {
				// Loop the animation
				refreshAnimIterator();
			} else {
				// Play idle animation since previous animation was not looping
				animState = QAnimState.IDLE;
				refreshAnimIterator();
			}
		}
	}
	
	public void refreshAnimIterator() {
		if(anims != null) {
			animIter = anims.getAnimSetIerator(animState.getAnimName());
		}
	}
	
	public void draw(Graphics2D g2) {
		if(animIter != null && animIter.hasNextFrame()) {
			if(tDelta < currentFrameLifetime) {
				if(actorState.getDirection().equals(QDirection.E) || actorState.getDirection().equals(QDirection.NE)) {
					g2.drawImage(currentAnimFrame.getImage(), null, x, y);
				} else {
					g2.drawImage(QAnimUtils.performXFormation(QTransformation.FLIPX, currentAnimFrame.getImage()), null, x, y);
				}
			} else {
				tDelta = 0;
				currentAnimFrame = animIter.getNextFrame();
				currentFrameLifetime = currentAnimFrame.getLifetime();
				
				if(actorState.getDirection().equals(QDirection.E) || actorState.getDirection().equals(QDirection.NE)) {
					g2.drawImage(currentAnimFrame.getImage(), null, x, y);
				} else {
					g2.drawImage(QAnimUtils.performXFormation(QTransformation.FLIPX, currentAnimFrame.getImage()), null, x, y);
				}
			}
		} else if(defaultImg != null) {
			tDelta = 0;
			currentFrameLifetime = 0;
			
			if(actorState.getDirection().equals(QDirection.E)) {
				g2.drawImage(defaultImg, null, x, y);
			} else {
				g2.drawImage(QAnimUtils.performXFormation(QTransformation.FLIPX, defaultImg), null, x, y);
			}
		}
	}
	
	public void attachActorState(QActorState actorState) {
		this.actorState = actorState;
	}

	public void addAnimMap(QAnimMap animSet) {
		anims = animSet;
		refreshAnimIterator();
	}
	
	public QActorState getActorState() {
		return actorState;
	}
	
	public QAnimation.AnimMapIterator getAnimIterator() {
		return animIter;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return actorState.getWidth();
	}
	
	public int getHeight() {
		return actorState.getHeight();
	}
	
	public int getActorId() {
		return id;
	}
	
	public int getCurrentFrameLifetime() {
		return currentFrameLifetime;
	}
	
	public void setCurrentFrameLifetime(int lifetime) {
		currentFrameLifetime = lifetime;
	}
	
	public BufferedImage getDefaultImg() {
		return defaultImg;
	}
	
	public QResourceManager getResMan() {
		return resMan;
	}
	
	public int getLayer() {
		return layer;
	}
	
	//////////////
	// Debugging
	///////////
	public void drawActorBounds(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.drawRect(x, y, width, height);
		g2.setColor(null);
	}
}
