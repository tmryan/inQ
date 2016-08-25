package tryan.inq.mobility;

import tryan.inq.state.QDynamicActorState;

public class QWalkMover extends QAbstractMover {
	public QWalkMover() {
		super();
		
		setPathType(QPathType.GROUND);
	}
	
	public QWalkMover(QMoverSystem moverSys) {
		super(moverSys);
		
		setPathType(QPathType.GROUND);
	}
	
	@Override
	public boolean move(QDirection direction) {
		boolean isWalking = false;
		
		if(direction != null) {
			// Note: May want actorStates to own their direction
			setDirection(direction);
			
			if(getPathingMap().isValidMoveX(getActorState().getBounds(), getPathingType(), direction)) {
				getActorState().setX(getActorState().getX() + (direction.xDirection() * ((QDynamicActorState) getActorState()).getSpeed()));
				isWalking = true;
			}
		}
		
		return isWalking;
	}
	
}
