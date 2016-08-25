package tryan.inq.mobility;

import tryan.inq.state.QDynamicActorState;

public class QSceneryMover extends QAbstractMover {
	public QSceneryMover() {
		super();
	}
	
	public QSceneryMover(QMoverSystem moverSys) {
		super(moverSys);
	}
	
	@Override
	public boolean move(QDirection direction) {		
		if(direction != null) {
			setDirection(direction);
		
			getActorState().setX(getActorState().getX() + (direction.xDirection() * ((QDynamicActorState) getActorState()).getSpeed()));
		}
		
		return true;
	}
}
