package tryan.inq.mobility;

public enum QPathType {
	NOPATH(0),
	ALL(1),
	GROUND(2),
	AIR(3);
	
	private int pathTypeId;
	
	private QPathType(int pathTypeId) {
		this.pathTypeId = pathTypeId;
	}
			
	public int pathTypeId() { return pathTypeId; }
}
