package tryan.inq.mobility;

public class QSimpleJumpMover extends QAbstractMover {
	/* 
	 * Note: This needs to be moved to double jump mover at a later time
	 * 		 Also, it's not working right now...
	 */
	private boolean jumpedTwice;
	
	public QSimpleJumpMover() {
		super();
		
		// Note: Should be called timer?
		// Note: Testing jump timing at 200ms
		setTiming(200);
		
		jumpedTwice = false;
		setIsTimed(true);
	}
	
	public QSimpleJumpMover(QMoverSystem moverSys) {
		super(moverSys);
		
		// Note: Testing jump timing at 200ms
		setTiming(200);
		
		jumpedTwice = false;
	}
	
	@Override
	public boolean move(QDirection direction) {
		boolean isJumping = false;
		
		if(direction != null) {
			if(getPathingMap().isValidMoveY(getActorState().getBounds(), getPathingType(), direction)) {
				getActorState().setY(getActorState().getY() - 15);
				
				isJumping = true;
			}
		}
		
		return isJumping;
	}
}
