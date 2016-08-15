package ryan.tom.inq.gfx;

public class QMouseManager {
	private QCoords lastMousePos;
	private QCoords currentMousePos;
	
	public QMouseManager() {
		lastMousePos = new QCoords(0, 0);
		currentMousePos = new QCoords(0, 0);
	}
	
	public QCoords getLastPressedCoords() {
		return lastMousePos;
	}
	
	public void setLastMousePosition(int x, int y) {
		lastMousePos.setX(x);
		lastMousePos.setY(y);
	}
	
	public QCoords getCurrentMouseCoords() {
		return currentMousePos;
	}
	
	public void setCurrentMousePosition(int x, int y) {
		currentMousePos.setX(x);
		currentMousePos.setY(y);
	}
}
