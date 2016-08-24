package tryan.inq.state;

import tryan.inq.controls.QKeyboardManager;
import tryan.inq.mobility.QDirection;
import tryan.inq.mobility.QMoverType;
import tryan.inq.mobility.QPathingMap;

public class QPlayerState extends QActorState {
	QKeyboardManager keyMan;
	
	public QPlayerState(int x, int y, int width, int height, int speed, int id, QPathingMap pathingMap) {
		super(x, y, width, height, speed, id, pathingMap);
	}

	public void walk(QDirection direction) {
		if(getMoverSystem().move(QMoverType.WALK ,direction)) {
			setCurrentAnimState(QAnimState.WALK);
		}
	}
	
	public void jump(QDirection direction) {
		if(getMoverSystem().move(QMoverType.JUMP, direction)) {
			setCurrentAnimState(QAnimState.JUMP);
		}
	}
	
	@Override
	public boolean physics() {
		boolean physics = false;
		
		if(getMoverSystem().getMoverModule(QMoverType.FALL) != null) {
			if(getMoverSystem().move(QMoverType.FALL ,QDirection.S)) {
				// Note: Could flag onGround here to reset double jump, etc
				// Note: This doesn't work right now because animation states
				physics = true;
				setCurrentAnimState(QAnimState.FALL);
			}
		}
		
		return physics;
	}
	
}
