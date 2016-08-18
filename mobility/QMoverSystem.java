package tryan.inq.mobility;

import java.util.HashMap;

import tryan.inq.state.QActorState;

/* 
 * Note: This class will house all mover modules as well as move related data and methods
 * 		 Each movable actor will have an instance of this class
 */
public class QMoverSystem {
	private QActorState actorState;
	private QDirection direction;
	private HashMap<QMoverType, QMoverModule> movers;
	private QPathingMap pathingMap;
	
	private QMoverType lastTimedMover;
	private QDirection lastTimedDirection;
	private boolean isBusyMoving;
//	private boolean isMovingDown;
//	private boolean isMovingLeft;
//	private boolean isMovingRight;
	private boolean isOnGround;
	
	private int tDelta;
	
	public QMoverSystem(QActorState actor) {
		this.actorState = actor;
		direction = QDirection.E;
		movers = new HashMap<QMoverType, QMoverModule>();
		pathingMap = null;
		lastTimedMover = QMoverType.WALK;
		lastTimedDirection = QDirection.E;
		isBusyMoving = false;
//		isMovingDown = false;
//		isOnGround = true;
		tDelta = 0;
	}
	
	// Note: May need to change how timings are done to add pausing effects
	// Note: Need to implement interrupts later as well
	public void onTick(int tickTime) {
		tDelta += tickTime;
	}
	
	// TIMED MOVES DON'T WORK RIGHT NOW! D:
	public boolean move(QMoverType moverType, QDirection direction) {		
		if(moverType.equals(QMoverType.SCENERY)) {
			return movers.get(moverType).move(direction);
		}
		
		if(lastTimedMover == null && movers.get(moverType).isTimed()) {
			lastTimedMover = moverType;
			lastTimedDirection = direction;
			isBusyMoving = true;
		}
		
		if(lastTimedMover != null && !moverType.equals(lastTimedMover)) {
			if(tDelta < movers.get(lastTimedMover).getTiming()) {
				return movers.get(lastTimedMover).move(lastTimedDirection);
			} else {
				tDelta = 0;
				lastTimedMover = null;
				isBusyMoving = false;
			}
		}
			
		if(lastTimedMover == null && movers.get(moverType) != null) {
			if(moverType.equals(QMoverType.FALL) && !isBusyMoving) {
				return movers.get(moverType).move(direction);
			} else {
				return movers.get(moverType).move(direction);
			}
		}
		
		return false;
	}
	
	public void attachPathingMap(QPathingMap pathingMap) {
		this.pathingMap = pathingMap;
	}
	
	public void addMoverModule(QMoverType moverType, QMoverModule moverModule) {
		movers.put(moverType, moverModule);
	}
	
	public QMoverModule getMoverModule(QMoverType moverType) {
		return movers.get(moverType);
	}
	
	public QActorState getActorState() {
		return actorState;
	}
	
	public QDirection getDirection() {
		return direction;
	}
	
	public void setDirection(QDirection direction) {
		this.direction = direction;
	}
	
	public int getXDirection() {
		return direction.xDirection();
	}
	
	public int getYDirection() {
		return direction.yDirection();
	}
	
	public QPathingMap getPathingMap() {
		return pathingMap;
	}
	
	public void setPathingMap(QPathingMap pathingMap) {
		this.pathingMap = pathingMap;
	}

	// Note: There must be a better way...
	public boolean isMovingUp() {
		return isBusyMoving;
	}
	
	public void setIsMovingUp(boolean isBusyMoving) {
		this.isBusyMoving = isBusyMoving;
	}
	
//	public boolean isMovingDown() {
//		return isMovingDown;
//	}
//	
//	public void setIsMovingDown(boolean isMovingDown) {
//		this.isMovingDown = isMovingDown;
//	}
//	
//	public boolean isMovingLeft() {
//		return isMovingLeft;
//	}
//	
//	public void setIsMovingLeft(boolean isMovingLeft) {
//		this.isMovingLeft = isMovingLeft;
//	}
//	
//	public boolean isMovingRight() {
//		return isMovingRight;
//	}
//	
//	public void setIsMovingRight(boolean isMovingRight) {
//		this.isMovingRight = isMovingRight;
//	}
}
