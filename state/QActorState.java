package tryan.inq.state;

import tryan.inq.mobility.QBounds;
import tryan.inq.mobility.QDirection;

/*
 * Note: QActorState coordinates should be in game space units
 */

public class QActorState {
	private int id;
	private QBounds bounds;

	// Note: Taking pathingMap here temporarily?
	public QActorState(int x, int y, int width, int height, int id) {
		this.id = id;
		bounds = new QBounds(x, y, width, height);
	}

	public void onTick(long tickTime, QDirection direction) {
		// Do something every game tick
	}
	
	public boolean containsCoords(int x2, int y2) {
		return bounds.containsCoords(x2, y2);
	}
	
	public boolean isContainedWithin(int x2, int y2, int x3, int y3) {
		return bounds.isContainedWithin(x2, y2, x3, y3);
	}
	
	public int getX() {
		return bounds.getX();
	}

	public void setX(int x) {
		bounds.setX(x);
	}

	public int getY() {
		return bounds.getY();
	}

	public void setY(int y) {
		bounds.setY(y);
	}
	
	public int getWidth() {
		return bounds.getWidth();
	}

	public int getHeight() {
		return bounds.getHeight();
	}
	
	public int getActorId() {
		return id;
	}
	
	public QBounds getBounds() {
		return bounds;
	}

}
