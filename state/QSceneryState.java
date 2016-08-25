package tryan.inq.state;

import tryan.inq.mobility.QDirection;
import tryan.inq.mobility.QMoverType;

public class QSceneryState extends QDynamicActorState implements QMoveable, QControllable {
	/* 
	 * Note: Need directional flag here so objects can be set to move any direction as camera moves
	 * 
	 * Note: May want to rename this and have another state for static scenery
	 */
	
	public QSceneryState(int x, int y, int width, int height, int speed, int id) {
		super(x, y, width, height, speed, id, null);
	}
	
	@Override
	public void onCommand(long tickTime, QDirection direction) {
		move(direction);
	}
	
	@Override
	public void move(QDirection direction) {
		if(getMoverSystem().getMoverModule(QMoverType.SCENERY) != null) {
			getMoverSystem().move(QMoverType.SCENERY ,direction);
		}
	}

	@Override
	public void walk(QDirection direction) {}

	@Override
	public void jump(QDirection direction) {}

	@Override
	public boolean fall() {return false;}

}
