package gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {
	public BufferedImage loadImage(String filepath) {
		try {
			return ImageIO.read(getClass().getResourceAsStream(filepath));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
