package tryan.inq.gfx;

public enum QDirection {
	N(0,-1),
	NE(1,-1),
	NW(-1,-1),
	S(0,1),
	SE(-1,1),
	SW(1,1),
	E(1,0),
	W(-1,0);
	
	private final int xDir;
	private final int yDir;
	
	private QDirection(int xDir, int yDir) {
		this.xDir = xDir;
		this.yDir = yDir;
	}
			
	public int xDirection() { return xDir; }
	public int yDirection() { return yDir; }
}
