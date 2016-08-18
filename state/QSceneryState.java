package tryan.inq.state;

import tryan.inq.mobility.QDirection;
import tryan.inq.mobility.QMoverType;

public class QSceneryState extends QActorState {
	// Note: Need directional flag here so objects can be set to move any direction as camera moves
	
	public QSceneryState(int x, int y, int width, int height, int speed, int id) {
		super(x, y, width, height, speed, id, null);
	}
	
	@Override
	public void move(QDirection direction) {
		if(getMoverSystem().getMoverModule(QMoverType.SCENERY) != null) {
			getMoverSystem().move(QMoverType.SCENERY ,direction);
		}
	}

}