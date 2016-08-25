package tryan.inq.mobility;

import tryan.inq.state.QCameraState;
import tryan.inq.state.QDynamicActorState;
import tryan.inq.state.QPlayerState;

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
		boolean moveX = false;
		boolean moveY = false;
		
		if(direction != null) {
			if(direction.xDirection() != 0) {
				moveX(direction);
				moveX = true;
			}
			
			if(direction.yDirection() == 1) {
				moveYNeg(direction);
				moveY = true;
			} else if(direction.yDirection() == -1) {
				moveYPos(direction);
				moveY = true;
			}
		}
		
		return (moveX | moveY);
	}
	
	public boolean moveX(QDirection direction) {
		if(direction != null) {
			setDirection(direction);
			// Note: On the lookout for a better solution to the camera+scenery movement linking problem
			((QCameraState)getActorState()).setIsMoving(false);
			
			if(direction.xDirection() == 1 && (getActorState().getX() + getActorState().getWidth()) < ((QCameraState)getActorState()).getMaxX()) {
				if(playerState.getX() + (playerState.getWidth() / 2) > getActorState().getX() + (getActorState().getWidth() / 2)) {
					getActorState().setX(getActorState().getX() + ((QDynamicActorState) getActorState()).getSpeed());
					((QCameraState)getActorState()).setIsMoving(true);
				} else {
					((QCameraState)getActorState()).setIsMoving(false);
				}
			} else if(direction.xDirection() == -1 && (getActorState().getX()) > ((QCameraState)getActorState()).getMinX()) {
				if(playerState.getX() + (playerState.getWidth() / 2) < getActorState().getX() + (getActorState().getWidth() / 2)) {
					getActorState().setX(getActorState().getX() - ((QDynamicActorState) getActorState()).getSpeed());
					((QCameraState)getActorState()).setIsMoving(true);
				} else {
					((QCameraState)getActorState()).setIsMoving(false);
				}
			}
		}
		
		return true;
	}
	
	public boolean moveYPos(QDirection direction) {
		if(direction != null) {
			setDirection(direction);
			// Note: On the lookout for a better solution to the camera+scenery movement linking problem
			((QCameraState)getActorState()).setIsMoving(false);
			
			if(direction.yDirection() == -1 && (getActorState().getY() + playerState.getSpeed()) > ((QCameraState)getActorState()).getMinY()) {
				if(playerState.getY() + (playerState.getHeight() / 2) < getActorState().getY() + (getActorState().getHeight() / 2)) {
					getActorState().setY(getActorState().getY() - playerState.getSpeed());
					((QCameraState)getActorState()).setIsMoving(true);
				} else {
					((QCameraState)getActorState()).setIsMoving(false);
				}
			}
		}
		
		return true;
	}
	
	public boolean moveYNeg(QDirection direction) {
		if(direction != null) {
			setDirection(direction);
			// Note: On the lookout for a better solution to the camera+scenery movement linking problem
			((QCameraState)getActorState()).setIsMoving(false);

			if(direction.yDirection() == 1 && (getActorState().getY() + getActorState().getHeight() + QPhysics.GRAVITY) < ((QCameraState)getActorState()).getMaxY()) {	
				if(playerState.getY() + (playerState.getHeight() / 2) > getActorState().getY() + (getActorState().getHeight() / 2)) {
					getActorState().setY(getActorState().getY() + QPhysics.GRAVITY);
					((QCameraState)getActorState()).setIsMoving(true);
				} else {
					((QCameraState)getActorState()).setIsMoving(false);
				}
			}
		}
		
		return true;
	}
}
