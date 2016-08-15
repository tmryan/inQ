package ryan.tom.inq.gfx;

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
		
			getActorState().setX(getActorState().getX() + (direction.xDirection() * getActorState().getSpeed()));
		}
		
		return true;
	}
}
