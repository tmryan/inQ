package tryan.inq.state;

import tryan.inq.mobility.QDirection;
import tryan.inq.mobility.QMoverModule;
import tryan.inq.mobility.QMoverSystem;
import tryan.inq.mobility.QMoverType;
import tryan.inq.mobility.QPathingMap;

public class QDynamicActorState extends QActorState {
	private QMoverSystem moverSys;
	private int speed;
	private QAnimState currentAnimState;

	public QDynamicActorState(int x, int y, int width, int height, int speed, int id, QPathingMap pathingMap) {
		super(x, y, width, height, id);
		
		this.speed = speed;
		moverSys = new QMoverSystem(this);
		moverSys.attachPathingMap(pathingMap);
		
		currentAnimState = QAnimState.IDLE;
	}

	@Override
	public void onTick(long tickTime, QDirection direction) {
		/* 
		 * Note: Physics does falling right now, but I think this system needs some design
		 *
		 * Note: Shouldn't be able to issue walk commands while falling, can cause out of grid bounds exceptions
		 */
		moverSys.onTick(tickTime);
		
		if(direction == null) {
			currentAnimState = QAnimState.IDLE;
		}
	}
	
	public boolean containsCoords(int x2, int y2) {
		return getBounds().containsCoords(x2, y2);
	}
	
	public boolean isContainedWithin(int x2, int y2, int x3, int y3) {
		return getBounds().isContainedWithin(x2, y2, x3, y3);
	}
	
	public boolean physics() {return false;}
	
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
