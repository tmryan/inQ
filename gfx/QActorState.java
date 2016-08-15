package ryan.tom.inq.gfx;

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

	public void onTick(int tickTime, QDirection direction) {
		// Note: Physics does falling right now, but I think this system needs some design love
		// Note: Shouldn't be able to issue walk commands while falling, causes out of grid bounds exception 
		// Note: AnimStates are a mess! Fix it!
		
		moverSys.onTick(tickTime);
		
		physics();
		
		if(direction == null) {
			currentAnimState = QAnimState.IDLE;
		}
	}
	
	public void onCommand(long tickTime, QDirection direction) {
		move(direction);
	}
	
	public void onUse() {
		// Do stuff
	}
	
	public void onHover() {
		// Do stuff
	}
	
	public void move(QDirection direction) {
		// Note: May end up not needing this one
	}
	
	public void walk(QDirection direction) {
		if(moverSys.move(QMoverType.WALK ,direction)) {
			currentAnimState = QAnimState.WALK;
		}
	}
	
	// Note: Move methods like these need to be overridden in subclasses
	public void jump(QDirection direction) {
		if(moverSys.move(QMoverType.JUMP, direction)) {
			currentAnimState = QAnimState.JUMP;
		}
	}
	
	public void physics() {
		if(moverSys.getMoverModule(QMoverType.FALL) != null) {
			if(moverSys.move(QMoverType.FALL ,QDirection.S)) {
				// Note: This doesn't work right now because animation states...
//				currentAnimState = QAnimState.FALL;
			}
		}
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
	
	// Note: Mover system may need a redesign
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