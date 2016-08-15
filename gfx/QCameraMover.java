package ryan.tom.inq.gfx;

public class QCameraMover extends QAbstractMover {
	private QPlayerState playerState;
	
	public QCameraMover(QPlayerState playerState) {
		super();
		
		this.playerState = playerState;
	}
	
	public QCameraMover(QMoverSystem moverSys, QPlayerState playerState) {
		super(moverSys);
		
		this.playerState = playerState;
	}
	
	@Override
	public boolean move(QDirection direction) {		
		if(direction != null) {
			setDirection(direction);
			// Note: On the lookout for a better solution to the camera+scenery movement linking problem
			((QCameraState)getActorState()).setIsMoving(false);
			
			if(direction.xDirection() == 1 && getActorState().getX() + getActorState().getWidth() < ((QCameraState)getActorState()).getMaxX()) {
				if(playerState.getX() + (playerState.getWidth() / 2) > getActorState().getX() + (getActorState().getWidth() / 2)) {
					getActorState().setX(getActorState().getX() + getActorState().getSpeed());
					((QCameraState)getActorState()).setIsMoving(true);
				} else {
					((QCameraState)getActorState()).setIsMoving(false);
				}
			} else if(direction.xDirection() == -1 && getActorState().getX() > ((QCameraState)getActorState()).getMinX()) {
				if(playerState.getX() + (playerState.getWidth() / 2) < getActorState().getX() + (getActorState().getWidth() / 2)) {
					getActorState().setX(getActorState().getX() - getActorState().getSpeed());
					((QCameraState)getActorState()).setIsMoving(true);
				} else {
					((QCameraState)getActorState()).setIsMoving(false);
				}
			}
		}
		
		return true;
	}
}
