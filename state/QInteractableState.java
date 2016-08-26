package tryan.inq.state;

import tryan.inq.event.QGameEvent;
import tryan.inq.mobility.QDirection;

/*
 * Note: Need to abstract actor states soon
 * 		 For example: this doesn't need movers, just animation states
 * 
 * Note: Might be a good idea to abstract fadeable actors at some point
 */
public class QInteractableState extends QDynamicActorState implements QInteractable {
	private int tDelta;
	private QGameEvent gameEvent;
	private boolean isHightlighted;
	private boolean isHighlightable;
	private QDynamicActorState fadingAgent;
	private boolean isFaded;
	private boolean isFadeable;
	
	public QInteractableState(int x, int y, int width, int height, int id) {
		super(x, y, width, height, 0, id);
		
		tDelta = 0;
		gameEvent = null;
		isHighlightable = true;
		isHightlighted = false;
		fadingAgent = null;
		isFaded = false;
		isFadeable = false;
	}
	
	@Override
	public void onTick(long tickTime, QDirection direction) {		
		if(isFaded) {
			if(!getAreaTrigger().containsCoords(fadingAgent.getX(), fadingAgent.getY())) {
				fadingAgent = null;
				getAreaTrigger().setCausalActor(null);
				isFaded = false;
				setCurrentAnimState(QAnimState.IDLE);
			}
		}
	}
	
	@Override
	public void onUse() {
		if(!isFaded) {
			System.out.println("Clicked actor " + getActorId() + "!");
		}
	}

	@Override
	public void onHover() {
		if(isHighlightable && !isFaded) {
			isHightlighted = true;
		}
	}

	@Override
	public QGameEvent checkAreaTrigger(int x, int y, QDynamicActorState causalActor) {
		QGameEvent event = null;
		
		if(!isFaded && getAreaTrigger() != null) {
			if(getAreaTrigger().containsCoords(causalActor.getX(), causalActor.getY())) {
				getAreaTrigger().setCausalActor(causalActor);
				fadingAgent = causalActor;
				event = getAreaTrigger().getGameEvent();

				if(getAreaTrigger().isOneShot()) {
					removeAreaTrigger();
				}
			}
		}
		
		return event;
	}
	
	// Note: Need to allow for multiple events at some point
	@Override
	public void addGameEvent(QGameEvent gameEvent) {
		this.gameEvent = gameEvent;
	}
	
	@Override
	public QGameEvent getGameEvent() {
		return gameEvent;
	}
	
	public void fade() {
		isFaded = true;
		setCurrentAnimState(QAnimState.FADED);
	}
	
	public void enableFading() {
		isFadeable = true;
	}
	
	public void disableFading() {
		isFaded = false;
		isFadeable = false;
		fadingAgent = null;
	}
	
	public void disableHighlight() {
		isHighlightable = false;
	}
	
	public boolean isHighlighted() {
		return isHightlighted;
	}
	
	public void removeHighlight() {
		isHightlighted = false;
	}
	
	public boolean isHighlightable() {
		return isHighlightable;
	}
}
