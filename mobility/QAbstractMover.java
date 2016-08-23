package tryan.inq.mobility;

import tryan.inq.state.QActorState;

/*
 * Trying to reference animation timing from here for now
 * if too slow then will add a hook to set it separately and
 * rely on data wrangling to match the timings
 */

public abstract class QAbstractMover implements QMoverModule {
	private QMoverSystem moverSys;
	private QPathType pathingType;
	private int timing;
	private boolean isTimed;
	
	public QAbstractMover() {
		moverSys = null;
		pathingType = QPathType.ALL;
		timing = 0;
		isTimed = false;
	}
	
	public QAbstractMover(QMoverSystem moverSys) {
		this.moverSys = moverSys;
		pathingType = QPathType.ALL;
	}
	
	public abstract boolean move(QDirection direction);
	
	public void attachMoverSystem(QMoverSystem moverSys) {
		this.moverSys = moverSys;
	}
	
	public void setPathType(QPathType pathingType) {
		this.pathingType = pathingType;
	}
	
	public QActorState getActorState() {
		return moverSys.getActorState();
	}
	
	public QDirection getDirection() {
		return moverSys.getDirection();
	}
	
	public void setDirection(QDirection direction) {
		moverSys.setDirection(direction);
	}
	
	public QPathType getPathingType() {
		return pathingType;
	}
	
	public QPathingMap getPathingMap() {
		return moverSys.getPathingMap();
	}
	
	public void setPathingMap(QPathingMap pathingMap) {
		moverSys.setPathingMap(pathingMap);
	}
	
	public int getTiming() {
		return timing;
	}
	
	public void setTiming(int timing) {
		this.timing = timing;
	}
	
	public boolean isTimed() {
		return isTimed;
	}
	
	public void setIsTimed(boolean isTimed) {
		this.isTimed = isTimed;
	}
}