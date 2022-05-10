package objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.Display;
import gfx.ImageUtils;
import math.Point;
import objects.Grid.Obstacle;

public class Ball {
	public Point position;
	public int size;
	private Point velocity;
	private double dampening;
	private boolean hit;
	private boolean active;
	
	public BufferedImage image;
	public final String imageFilepath = "/assets/sprites/sprBall.png";
	public double drawSize;
	
	public Ball(int x, int y) {
		this.position = new Point(x, y);
	}
	
	public void init() {
		this.size = 12;
		this.velocity = Point.origin;
		this.dampening = 0.01;
		this.hit = true;
		this.active = true;
		this.drawSize = 1;
		ImageUtils iu = new ImageUtils();
		this.image = iu.loadImage(imageFilepath);
	}
	
	public Point getVelocity() {return velocity;}

	public boolean canHit() {return hit;}
	
	public void setVelocity(double x, double y) {
		this.velocity = new Point(x, y);
		this.hit = false;
	}
	
	public void setDampening(double dampening) {
		this.dampening = dampening;
	}
	
	public void translate(double x, double y) {
		this.position.x += x;
		this.position.y += y;
	}
	
	public void move(int framerate) {
		Point vel = this.velocity.scale(1.0 / framerate);
		if(velocity.getIntensity() >= 1) {
			this.translate(vel.x, vel.y);
			double v = Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);
			double theta = Math.atan2(velocity.y, velocity.x);
			
			v *= (1 - 2 * dampening);
			velocity.x = v * Math.cos(theta);
			velocity.y = v * Math.sin(theta);
			if(velocity.getIntensity() < 1) {
				velocity = Point.origin;
				this.hit = true;
			}
		}

		Hole finish = Display.level.hole;
		Point distVec = Point.subtract(position, finish.position);
		double dist = distVec.getIntensity();
		if(dist < finish.size) this.collideWithHole(finish);
	}
	
	public void collide(Obstacle o, Grid g) {
		Point nextX = new Point(this.getNextPosX(Display.FPS), this.position.y);
		Point nextY = new Point(this.position.x, this.getNextPosY(Display.FPS));
		
		Point oStart = Point.add(o.position.scale(g.gridSize), new Point(-this.size, -this.size));
		Point oEnd = Point.add(Point.add(o.position, o.size).scale(g.gridSize), new Point(this.size, this.size));
		
		if(nextX.x > oStart.x && nextX.x < oEnd.x && nextX.y > oStart.y && nextX.y < oEnd.y) this.bounceX();
		if(nextY.x > oStart.x && nextY.x < oEnd.x && nextY.y > oStart.y && nextY.y < oEnd.y) this.bounceY();	
	}
	
	public void bounceX() {
		this.velocity.x *= -1;
	}
	
	public void bounceY() {
		this.velocity.y *= -1;
	}
	
	public double getNextPosX(int framerate) {
		return this.position.x + this.velocity.x / framerate;
	}
	
	public double getNextPosY(int framerate) {
		return this.position.y + this.velocity.y / framerate;
	}
	
	public void collideWithHole(Hole h) {
		Point distVec = Point.subtract(position, h.position);
		this.velocity = Point.add(this.velocity.scale(0.95), distVec.scale(-1));
		if(distVec.getIntensity() < 1 && this.velocity.getIntensity() < 10) {
			this.drawSize -= 0.01;
			if(drawSize < 0) 
				this.active = false;
		}
		else {
			this.drawSize += 0.01;
			this.drawSize = Math.min(this.drawSize, 1);
		}
	}
	
	public void update(int framerate) {
		if(active)this.move(framerate);
	}
	
	public void draw(Graphics g) {
		if(active) {
			Point imageOrigin = new Point(this.position.x - image.getWidth() / 2 * drawSize,
										  this.position.y - image.getHeight() / 2 * drawSize);
			Point imageSize = new Point(image.getWidth(), image.getHeight()).scale(drawSize);
			
			g.drawImage(image, (int) imageOrigin.x, (int) imageOrigin.y, (int) imageSize.x, (int) imageSize.y, null);
		}
	}
}
