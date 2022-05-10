package math;

public class Point {
	public double x, y;
	public final static Point origin = new Point(0, 0);
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getIntensity() {
		return Math.sqrt(x * x + y * y);
	}
	
	public double getAngle() {
		return Math.atan2(y, x);
	}
	
	public void translate(double x, double y) {
		this.x += x;
		this.y += y;
	}
	
	public void translate(Point p) {
		this.x += p.x;
		this.y += p.y;
	}
	
	public Point scale(double scale) {
		return new Point(this.x * scale, this.y * scale);
	}
	
	public static Point add(Point p1, Point p2) {
		return new Point(p1.x + p2.x, p1.y + p2.y);
	}
	
	public static Point subtract(Point p1, Point p2) {
		return new Point(p1.x - p2.x, p1.y - p2.y);
	}
	
	public String toString() {
		return Double.toString(x) + ", " + Double.toString(y);
	}
}
