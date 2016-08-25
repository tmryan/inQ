package tryan.inq.state;

import tryan.inq.mobility.QDirection;

public interface QMoveable {
	public void move(QDirection direction);
	public void walk(QDirection direction);
	public void jump(QDirection direction);
	public boolean fall();
	
}
