package tryan.inq.state;

import tryan.inq.mobility.QDirection;

public interface QControllable {
	public void onCommand(long tickTime, QDirection direction);
}
