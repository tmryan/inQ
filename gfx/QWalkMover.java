package ryan.tom.inq.gfx;

public class QWalkMover extends QAbstractMover {
	public QWalkMover() {
		super();
	}
	
	public QWalkMover(QMoverSystem moverSys) {
		super(moverSys);
	}
	
	@Override
	public boolean move(QDirection direction) {
		boolean isWalking = false;
		
		if(direction != null) {
			// Note: May want actorStates to own their direction
			setDirection(direction);
			
			if(getPathingMap().isValidMoveX(getActorState().getBounds(), getPathingType(), direction)) {
				getActorState().setX(getActorState().getX() + (direction.xDirection() * getActorState().getSpeed()));
				isWalking = true;
			}
		}
		
		return isWalking;
	}
	
}
