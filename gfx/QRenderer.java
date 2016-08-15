package ryan.tom.inq.gfx;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class QRenderer extends JPanel {
	QGameState gameState;
	QScene scene;
	QBufferManager bufferMan;

	public QRenderer(QGameState gameState, QScene scene) {
		this.gameState = gameState;
		this.scene = scene;
		bufferMan = new QBufferManager(scene.getSceneWidth(), scene.getSceneHeight());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		// Pushing back buffer to front
		g2.drawImage(bufferMan.frontBuffer.getSubimage(scene.getCamera().getX(), scene.getCamera().getY(), getWidth(), getHeight()), null, 0, 0);
		bufferMan.swap();
	}
	
	public void onGfxTick() {
		Graphics2D bbg2 = (Graphics2D) bufferMan.getGraphics2D();
//		scene.draw(bbg2);
	}
	
	public void onTick() {
		repaint();
	}
	
	/////////////////////////
	// Buffer Manager Class
	//////////////////////
	
	private class QBufferManager {
		private BufferedImage bufferOne;
		private BufferedImage bufferTwo;
		private BufferedImage frontBuffer;
		private boolean isBufferOneBack;

		public QBufferManager(int width, int height) {
			bufferOne = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
			bufferTwo = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
			frontBuffer = bufferOne;
			isBufferOneBack = false;
		}
		
		public void swap() {
			if(isBufferOneBack == true) {
				frontBuffer = bufferOne;
			} else {
				frontBuffer = bufferTwo;
			}
			
			isBufferOneBack = !isBufferOneBack;
		}
		
		public Graphics2D getGraphics2D() {
			return (Graphics2D) frontBuffer.getGraphics();
		}		
	}
}
