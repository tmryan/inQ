package ryan.tom.inq.gfx;

public class QSceneryState extends QActorState {

	public QSceneryState(int x, int y, int width, int height, int speed, int id) {
		super(x, y, width, height, speed, id, null);
	}
	
	@Override
	public void move(QDirection direction) {
		if(getMoverSystem().getMoverModule(QMoverType.SCENERY) != null) {
			getMoverSystem().move(QMoverType.SCENERY ,direction);
		}
	}

}
