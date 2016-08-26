package tryan.inq.state;

import tryan.inq.mobility.QDirection;
import tryan.inq.mobility.QMoverSystem;
import tryan.inq.mobility.QMoverType;
import tryan.inq.overhead.QGameSettings;

public class QCameraState extends QDynamicActorState implements QMoveable, QControllable {
	// These camera bounds constraints will be set by the scene state as a new scene is loaded
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;
	private boolean isMoving;
	
	public QCameraState(int x, int y, int speed, int id) {
		super(x, y, QGameSettings.getWinWidth(), QGameSettings.getWinHeight(), speed, id);
		
		minX = 0;
		minY = 0;
		maxX = 0;
		maxY = 0;
		isMoving = false;
		attachMoverSystem(new QMoverSystem(this));
	}

	@Override
	public void onTick(long tickTime, QDirection direction) {
		// Do stuff
	}
	
	@Override
	public void onCommand(long tickTime, QDirection direction) {
		move(direction);
	}
	
	@Override
	public void move(QDirection direction) {
		if(getMoverModule(QMoverType.CAMERA) != null) {
			getMoverModule(QMoverType.CAMERA).move(direction);
		}
	}
	
	@Override
	public void walk(QDirection direction) {}

	@Override
	public void jump(QDirection direction) {}
	
	@Override
	public boolean fall() {return false;}
	
	public int getMinX() {
		return minX;
	}
	
	public void setMinX(int x) {
		minX = x;
	}
		
	public int getMinY() {
		return minY;
	}
	
	public void setMinY(int y) {
		minY = y;
	}
	
	public int getMaxX() {
		return maxX;
	}
	
	public void setMaxX(int x) {
		maxX = x;
	}
		
	public int getMaxY() {
		return maxY;
	}
	
	public void setMaxY(int y) {
		maxY = y;
	}
	
	public boolean isMoving() {
		return isMoving;
	}
	
	public void setIsMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

}
