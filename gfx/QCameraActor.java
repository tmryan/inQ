package ryan.tom.inq.gfx;

public class QCameraActor {
	private int id;
	private int x;
	private int y;
	private int width;
	private int height;
	private QCameraState camState;
	
	//TODO: abstract actors!

	public QCameraActor(int x, int y) {
		id = QGameState.generateActorId();
		this.x = x;
		this.y = y;
		width = QGameSettings.getWinWidth();
		height = QGameSettings.getWinHeight();
		camState = null;
	}
	
	/* 
	 * Is it bad to have a shared QBounds object that would always have the updated position?
	 * Then QScene could just call draw instead of update as well...
	 */
	
	// Update view from current model state
	public void updateView(long tickTime) {
		x = camState.getX();
		y = camState.getY();
	}
	
	public void attachActorState(QCameraState camState) {
		this.camState = camState;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
		
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getActorId() {
		return id;
	}
	
}
