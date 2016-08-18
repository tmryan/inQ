package tryan.inq.gfx;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class QAnimUtils {
	
	private QAnimUtils() {}	
	
	public static BufferedImage performXFormation(QTransformation xFormId, BufferedImage img) {
		switch(xFormId) {
			case FLIPX:
				return flipImgHorizontal(img);
			default:
				break;
		}
		
		return null;
	}
	
	/*
	 * Note: Need to work on this
	 */
	private static BufferedImage flipImgHorizontal(BufferedImage img) {
		BufferedImage flippedImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-flippedImg.getWidth(null), 0);
		
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		flippedImg = op.filter(img, null);
		
		return flippedImg;
	}
	
	//////////////
	// XForm Enum
	///////////
	
	public enum QTransformation {
		FLIPX(1),
		FLIPY(2),
		ROTATEPOS(3),
		ROTATENEG(4);
		
		private int xFormId;
		
		QTransformation(int xFormId) {
			this.xFormId = xFormId;
		}
		
		public int xFormId() {
			return xFormId;
		}
	}
	
}
