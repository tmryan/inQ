package ryan.tom.inq.gfx;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class QAnimUtils {
	
	private QAnimUtils() {}	
	
	/*
	 * Note: Need to work on this
	 */
	public static BufferedImage flipImgHorizontal(BufferedImage img) {
		BufferedImage flippedImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-flippedImg.getWidth(null), 0);
		
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		flippedImg = op.filter(img, null);
		
		return flippedImg;
	}
}
