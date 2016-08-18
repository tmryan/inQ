package tryan.inq.mobility;

public class QSimplePopMover extends QAbstractMover {
	public QSimplePopMover() {
		super();
	}
	
	public QSimplePopMover(QMoverSystem moverSys) {
		super(moverSys);
	}
	
	@Override
	public boolean move(QDirection direction) {
		getActorState().setX(50);
		getActorState().setY(50);
		
		return true;
	}

}
