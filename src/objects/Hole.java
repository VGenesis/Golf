package objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gfx.ImageUtils;
import math.Point;

public class Hole {
	public Point position;
	public int size;
	
	private BufferedImage image;
	private String imageFilepath = "/assets/sprites/sprHole.png";
	
	public Hole(double x, double y) {
		this.position = new Point(x, y);
	}
	
	public void init() {
		this.size = 18;
		ImageUtils iu = new ImageUtils();
		this.image = iu.loadImage(imageFilepath);
	}
	
	public void draw(Graphics g) {
		Point imageOrigin = new Point(this.position.x - image.getWidth() / 2,
				  this.position.y - image.getHeight() / 2);
		Point imageSize = new Point(image.getWidth(), image.getHeight());

		g.drawImage(image, (int) imageOrigin.x, (int) imageOrigin.y, (int) imageSize.x, (int) imageSize.y, null);
	}
	
}
