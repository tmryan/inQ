package tryan.inq.state;

import tryan.inq.mobility.QDirection;
import tryan.inq.mobility.QMoverSystem;
import tryan.inq.mobility.QMoverType;

public class QPlayerState extends QDynamicActorState implements QMoveable, QControllable {
	public QPlayerState(int x, int y, int width, int height, int speed, int id) {
		super(x, y, width, height, speed, id);
		
		attachMoverSystem(new QMoverSystem(this));
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
