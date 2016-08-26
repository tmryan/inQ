package tryan.inq.state;

import tryan.inq.event.QAreaTrigger;
import tryan.inq.event.QGameEvent;
import tryan.inq.mobility.QDirection;
import tryan.inq.mobility.QMoverModule;
import tryan.inq.mobility.QMoverSystem;
import tryan.inq.mobility.QMoverType;
import tryan.inq.mobility.QPathingMap;

public class QDynamicActorState extends QActorState {
	private QMoverSystem moverSys;
	private int speed;
	private QAnimState currentAnimState;
	private QAreaTrigger areaTrigger;

	/*
	 *  Note: Downside of this design is now all subclasses have to be altered when this constructor is altered
	 */
	public QDynamicActorState(int x, int y, int width, int height, int speed, int id) {
		super(x, y, width, height, id);
		
		this.speed = speed;
		moverSys = null;
		currentAnimState = QAnimState.IDLE;
		areaTrigger = null;
	}

	@Override
	public void onTick(long tickTime, QDirection direction) {
		/* 
		 * Note: Physics does falling right now, but I think this system needs some design
		 *
		 * Note: Shouldn't be able to issue walk commands while falling, can cause out of grid bounds exceptions
		 */
		
		if(moverSys != null) {
			moverSys.onTick(tickTime);
		}
		
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
	
	public void attachMoverSystem(QMoverSystem moverSys) {
		this.moverSys = moverSys;
	}
	
	public void attachPathingMap(QPathingMap pathingMap) {
		moverSys.attachPathingMap(pathingMap);
	}
	
	public void attachAreaTrigger(QAreaTrigger areaTrigger) {
		this.areaTrigger = areaTrigger;
	}
	
	public QGameEvent checkAreaTrigger(int x, int y, QDynamicActorState causalActor) {
		QGameEvent event = null;
		
		if(areaTrigger != null) {
			if(areaTrigger.containsCoords(x, y)) {
				areaTrigger.setCausalActor(causalActor);
				event = areaTrigger.getGameEvent();
				
				if(areaTrigger.isOneShot()) {
					areaTrigger = null;
				}
			}
		}
		
		return event;
	}
	
	public QAreaTrigger getAreaTrigger() {
		return areaTrigger;
	}
	
	public void removeAreaTrigger() {
		areaTrigger = null;
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
