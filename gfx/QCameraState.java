package ryan.tom.inq.gfx;

public class QCameraState extends QActorState {
	// These camera bounds constraints will be set by the scene state as a new scene is loaded
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;
	private boolean isMoving;
	
	public QCameraState(int x, int y, int speed, int id) {
		super(x, y, QGameSettings.getWinWidth(), QGameSettings.getWinHeight(), speed, id, null);
		
		minX = 0;
		minY = 0;
		maxX = 0;
		maxY = 0;
		isMoving = false;
	}

	@Override
	public void onTick(int tickTime, QDirection direction) {
		// Do nothing for now
	}
	
	@Override
	public void onCommand(long tickTime, QDirection direction) {
		move(direction);
	}
	
	@Override
	public void move(QDirection direction) {
		if(getMoverModule(QMoverType.CAMERA) != null) {
			getMoverModule(QMoverType.CAMERA).move(direction);
		}
	}
	
	public int getMinX() {
		return minX;
	}
	
	public void setMinX(int x) {
		minX = x;
	}
		
	public int getMinY() {
		return minY;
	}
	
	public void setMinY(int y) {
		minY = y;
	}
	
	public int getMaxX() {
		return maxX;
	}
	
	public void setMaxX(int x) {
		maxX = x;
	}
		
	public int getMaxY() {
		return maxY;
	}
	
	public void setMaxY(int y) {
		maxY = y;
	}
	
	public boolean isMoving() {
		return isMoving;
	}
	
	public void setIsMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}
}
