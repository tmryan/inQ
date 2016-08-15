package ryan.tom.inq.gfx;

public class QGameSettings {
	private static int winWidth;
	private static int winHeight;
	
	public QGameSettings() {
		winWidth = 1200;
		winHeight = 900;
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
}
