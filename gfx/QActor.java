package tryan.inq.gfx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import tryan.inq.overhead.QResourceManager;
import tryan.inq.state.QActorState;
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
	private BufferedImage defaultImg;
	// Note: May not need resMan in this class
	private QResourceManager resMan;
	private QActorState actorState;
	
	public QActor(BufferedImage img, QResourceManager resMan, int layer) {
		id = QGameState.generateActorId();
		this.defaultImg = img;
		x = 0;
		y = 0;
		width = img.getWidth();
		height = img.getHeight();
		// Note: Layer may eventually be used for draw order
		this.layer = layer;
		tDelta = 0;

		this.resMan = resMan;
		actorState = null;		
	}
	
	// Update view from current model state
	public void updateView(long tickTime) {
		tDelta += tickTime;
		
		x = actorState.getX();
		y = actorState.getY();
	}
		
	public void draw(Graphics2D g2) {
		if(defaultImg != null) {
			g2.drawImage(defaultImg, null, x, y);
		}
	}
	
	public void attachActorState(QActorState actorState) {
		this.actorState = actorState;
	}

	public QActorState getActorState() {
		return actorState;
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
	
	public int getTDelta() {
		return tDelta;
	}
	
	public void setTDelta(int time) {
		tDelta = time;
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
