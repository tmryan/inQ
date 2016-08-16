package tryan.inq.gfx;

/*
 * Note: Need to abstract actor states soon
 * 		 For example: this doesn't need movers, just animation states
 */
public class QInteractableState extends QActorState {	
	private boolean isHightlighted;
//	private int tDelta;
	
	public QInteractableState(int x, int y, int width, int height, int id) {
		super(x, y, width, height, 0, id, null);
		
		isHightlighted = false;
//		tDelta = 0;
	}
	
	@Override
	public void onTick(int tickTime, QDirection direction) {
		// Do Stuff
	}
	
	@Override
	public void onUse() {
		System.out.println("clicked actor " + getActorId() + "!");
	}

	@Override
	public void onHover() {
		// Note: Doesn't unhighlight! D:
//		System.out.println("highlight!");
		isHightlighted = true;
	}
	
//	@Override
//	public void onMouseLeave() {
//		System.out.println("highlight!");
//		isHightlighted = true;
//	}
	
	public boolean isHighlighted() {
		return isHightlighted;
	}
	
	public void removeHighlight() {
		isHightlighted = false;
	}

}
