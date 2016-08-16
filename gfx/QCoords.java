package tryan.inq.gfx;

public class QCoords implements Comparable<QCoords> {
	private int x;
	private int y;
	
	public QCoords(int x, int y) {
		this.x = x;
		this.y = y;
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

	public int compareTo(QCoords o) {
		int result = 0;
		
		if(o.getX() > x && o.getY() > y) {
			result = 2;
		} else if(o.getX() > x && o.getY() < y) {
			result = 1;
		} else if((o.getX() < x && o.getY() > y)) {
			result = -1;
		} else if(o.getX() < x && o.getY() < y) {
			result = -2;
		}
		
		return result;
	}

}
