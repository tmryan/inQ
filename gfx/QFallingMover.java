package tryan.inq.gfx;

public class QFallingMover extends QAbstractMover {
	public QFallingMover() {
		super();
	}
	
	public QFallingMover(QMoverSystem moverSys) {
		super(moverSys);
	}
	
	@Override
	public boolean move(QDirection direction) {
		boolean isFalling = false;
		
		// Note: Fancy this up a bit
		// If nothing below then fall using QPhysics.GRAVITY for speed
		if(getPathingMap().isValidMoveY(getActorState().getBounds(), getPathingType(), direction)) {
			getActorState().setY(getActorState().getY() + (direction.yDirection() * QPhysics.GRAVITY));
			isFalling = true;
		}
		
		// Returning boolean to inform actor animation state
		return isFalling;
	}
	
}
