package tryan.inq.mobility;

public enum QMoverType {
	WALK(0, "walk"),
	// Might end up removing RUN since WALK might suffice
	RUN(1, "run"),
	JUMP(2, "jump"),
	FALL(3, "fall"),
	FLY(4, "fly"),
	CAMERA(4, "camera"),
	SCENERY(5, "scenery");
	
	private int type;
	private String name;
	
	private QMoverType(int moverType, String moverName) {
		type = moverType;
		name = moverName;
	}
			
	public int moverType() { return type; }
	public String moverName() { return name; }
	
}
