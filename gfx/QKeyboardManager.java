package tryan.inq.gfx;

import java.awt.event.KeyEvent;

public class QKeyboardManager {
	private boolean[] keyStates;
//	private boolean upKey;
//	private boolean downKey;
//	private boolean leftKey;
//	private boolean rightKey;
//	private boolean spaceKey;
	
	public QKeyboardManager() {
		keyStates = new boolean[7];

		for(int i = 0; i < keyStates.length; i++) {
			keyStates[i] = false;
		}
		
//		upKey = false;
//		downKey = false;
//		leftKey = false;
//		rightKey = false;
//		spaceKey = false;
	}
	
	public void setKeyState(int key, boolean isPressed) {
		switch(key) {
			case KeyEvent.VK_W:
				keyStates[0] = isPressed;
				break;
			case KeyEvent.VK_S:
				keyStates[1] = isPressed;
				break;
			case KeyEvent.VK_A:
				keyStates[2] = isPressed;
				break;
			case KeyEvent.VK_D:
				keyStates[3] = isPressed;
				break;
			case KeyEvent.VK_SPACE:
				keyStates[4] = isPressed;
				break;
			case KeyEvent.VK_Q:
				keyStates[5] = isPressed;
				break;
			case KeyEvent.VK_E:
				keyStates[6] = isPressed;
				break;
			default:
				break;
		}
		
		//for(boolean b : keyStates) {
		//	System.out.print(((b) ? 1 : 0));
		//}
		//System.out.println();
	}
	
	public boolean[] getKeyboardState() {
		return keyStates;
	}
	
//	public boolean isUpPressed() {
//		return upKey;
//	}
//	
//	public boolean isDownPressed() {
//		return downKey;
//	}
//	
//	public boolean isLeftPressed() {
//		return leftKey;
//	}
//	
//	public boolean isRightPressed() {
//		return rightKey;
//	}
//	
//	public boolean isSpacePressed() {
//		return spaceKey;
//	}
}
