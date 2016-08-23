package tryan.inq.overhead;

public class QGameSettings {
	private static int winWidth;
	private static int winHeight;
	private static int delay;
	
	public QGameSettings() {
		winWidth = 1200;
		winHeight = 900;
		delay = 16;
	}
	
	public static int getWinWidth() {
		return winWidth;
	}
	
	public static void setWinWidth(int width) {
		winWidth = width;
	}
	
	public static int getWinHeight() {
		return winHeight;
	}
	
	public static void setWinHeight(int height) {
		winHeight = height;
	}

	public static int getDelay() {
		return delay;
	}

	public static void setDelay(int delay) {
		QGameSettings.delay = delay;
	}
		
}
