package tryan.inq.state;

import tryan.inq.controls.QKeyboardManager;
import tryan.inq.mobility.QDirection;
import tryan.inq.mobility.QMoverType;
import tryan.inq.mobility.QPathingMap;

public class QPlayerState extends QActorState implements QMoveable, QControllable {
	QKeyboardManager keyMan;
	
	public QPlayerState(int x, int y, int width, int height, int speed, int id, QPathingMap pathingMap) {
		super(x, y, width, height, speed, id, pathingMap);
	}

	@Override
	public void onCommand(long tickTime, QDirection direction) {}
	
	@Override
	public void move(QDirection direction) {}
	
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
	public boolean fall() {
		boolean isFalling = false;
		
		if(getMoverSystem().getMoverModule(QMoverType.FALL) != null) {
			if(getMoverSystem().move(QMoverType.FALL ,QDirection.S)) {
				// Note: Could flag onGround here to reset double jump, etc
				// Note: This doesn't work right now because animation states
				isFalling = true;
				setCurrentAnimState(QAnimState.FALL);
			}
		}
		
		return isFalling;
	}
	
}
