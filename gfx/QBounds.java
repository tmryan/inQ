package ryan.tom.inq.gfx;

public class QBounds {
	private int x;
	private int y;
	private int width;
	private int height;
	private int unitWidth;
	private int unitHeight;
	
	public QBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		// Note: There is a better way to get unit width and height
		unitWidth = width / QGameConstants.UNIT_DISTANCE;
		this.height = height;
		unitHeight = height / QGameConstants.UNIT_DISTANCE;
	}
	
	public boolean containsCoords(int x2, int y2) {
		boolean contains = false;
		
		if(x2 > x && y2 > y && x2 < x+width && y2 < y+height) {
			contains = true;
		}
		
		return contains;
	}
	
	public boolean isContainedWithin(int x2, int y2, int x3, int y3) {
		boolean contained = false;
		
		if(x2 < x && y2 < y && x3 > x && y3 > y) {
			contained = true;
		}
		
		return contained;
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
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getUnitWidth() {
		return unitWidth;
	}
	
	public int getUnitHeight() {
		return unitHeight;
	}
}
