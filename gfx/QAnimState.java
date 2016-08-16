package tryan.inq.gfx;

public enum QAnimState {
	IDLE(0, "idle", true),
	WALK(1, "walk", true),
	RUN(2, "run", true),
	JUMP(3, "jump", true),
	FALL(4, "fall", false),
	ATTACK(5, "attack", false),
	DIE(6, "die", false),
	DEAD(7, "dead", true);
	
	private final int animState;
	private final String animName;
	private final boolean isLooping;
	
	private QAnimState(int animState, String animName, boolean isLooping) {
		this.animState = animState;
		this.animName = animName;
		this.isLooping = isLooping;
	}
	
	public int getAnimState() { return animState; }
	public String getAnimName() { return animName; }
	public boolean isLooping() { return isLooping; }
}
