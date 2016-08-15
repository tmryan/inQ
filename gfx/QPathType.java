package ryan.tom.inq.gfx;

public enum QPathType {
	NOPATH(0),
	GROUND(1),
	AIR(2);
	
	private int pathType;
	
	private QPathType(int pathType) {
		this.pathType = pathType;
	}
			
	public int pathType() { return pathType; }
}
