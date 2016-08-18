package tryan.inq.state;

import tryan.inq.controls.QKeyboardManager;
import tryan.inq.mobility.QPathingMap;

public class QPlayerState extends QActorState {
	QKeyboardManager keyMan;
	
	public QPlayerState(int x, int y, int width, int height, int speed, int id, QPathingMap pathingMap, QKeyboardManager keyMan) {
		super(x, y, width, height, speed, id, pathingMap);
		this.keyMan = keyMan;
	}

}
