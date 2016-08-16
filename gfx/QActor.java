package tryan.inq.gfx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

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
	private BufferedImage defaultImg;
	// Note: May not need resMan in this class
	private QResourceManager resMan;
	private QActorState actorState;
	private QAnimState animState;
	private QAnimMap anims;
	private QAnimation.AnimMapIterator animIter;

	public QActor(int x, int y, BufferedImage img, QResourceManager resMan, int layer) {
		id = QGameState.generateActorId();
		this.defaultImg = img;
		this.x = x;
		this.y = y;
		width = img.getWidth();
		height = img.getHeight();
		// Note: Layer may eventually be used for draw order
		this.layer = layer;
		this.resMan = resMan;
		actorState = null;
		animState = QAnimState.IDLE;
		anims = null;
		animIter = null;
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
		this.resMan = resMan;
		actorState = null;
		animState = QAnimState.IDLE;
		anims = null;
		animIter = null;
	}
	
	// Update view from current model state
	public void updateView(long tickTime) {
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
		if(getAnimIterator() != null && animIter.hasNextFrame()) {
			if(actorState.getDirection().equals(QDirection.E) || actorState.getDirection().equals(QDirection.NE)) {
				g2.drawImage(animIter.getNextFrame(), null, x, y);
			} else {
				g2.drawImage(QAnimUtils.flipImgHorizontal(animIter.getNextFrame()), null, x, y);
			}
			
		} else if(defaultImg != null) {
			if(actorState.getDirection().equals(QDirection.E)) {
				g2.drawImage(defaultImg, null, x, y);
			} else {
				g2.drawImage(QAnimUtils.flipImgHorizontal(defaultImg), null, x, y);
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
