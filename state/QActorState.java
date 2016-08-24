package tryan.inq.state;

import tryan.inq.mobility.QBounds;
import tryan.inq.mobility.QDirection;
import tryan.inq.mobility.QMoverModule;
import tryan.inq.mobility.QMoverSystem;
import tryan.inq.mobility.QMoverType;
import tryan.inq.mobility.QPathingMap;

/*
 * Note: QActorState coordinates should be in game space units
 */

public class QActorState {
	private int id;
	private int speed;
	private QMoverSystem moverSys;
	private QBounds bounds;
	private QAnimState currentAnimState;
	
	// Note: Taking pathingMap here temporarily?
	public QActorState(int x, int y, int width, int height, int speed, int id, QPathingMap pathingMap) {
		this.id = id;
		this.speed = speed;
		moverSys = new QMoverSystem(this);
		moverSys.attachPathingMap(pathingMap);
		bounds = new QBounds(x, y, width, height);
		currentAnimState = QAnimState.IDLE;
	}

	public void onTick(long tickTime, QDirection direction) {
		// Note: Physics does falling right now, but I think this system needs some redesign
		// Note: Shouldn't be able to issue walk commands while falling, can cause out of grid bounds exceptions
		// Note: AnimStates are a mess. Fix it!
		
		moverSys.onTick(tickTime);
			
		if(direction == null) {
			currentAnimState = QAnimState.IDLE;
		}
	}
	
	public void onCommand(long tickTime, QDirection direction) {
		move(direction);
	}
	
	public void onUse() {}
	
	public void onHover() {}
	
	public void move(QDirection direction) {}
	
	public void walk(QDirection direction) {}
	
	public void jump(QDirection direction) {}
	
	public boolean physics() {return false;}
	
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
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public QAnimState getCurrentAnimState() {
		return currentAnimState;
	}

	public void setCurrentAnimState(QAnimState currentAnimState) {
		this.currentAnimState = currentAnimState;
	}

	public int getActorId() {
		return id;
	}
	
	// Note: Need a better solution for the bounds problem
	public QBounds getBounds() {
		return bounds;
	}
	
	public QDirection getDirection() {
		return moverSys.getDirection();
	}
	
	public QMoverSystem getMoverSystem() {
		return moverSys;
	}
	
	public QMoverModule getMoverModule(QMoverType moverType) {
		return moverSys.getMoverModule(moverType);
	}
	
	public void addMoverModule(QMoverType moverType, QMoverModule mover) {
		mover.attachMoverSystem(moverSys);
		moverSys.addMoverModule(moverType, mover);
	}

}
