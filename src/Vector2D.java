
// class represents 2D vector
public class Vector2D {
	
	public double x;
	public double y;
	
	public Vector2D() {
		x = 0;
		y = 0;
	}
	
	public Vector2D(double x_in, double y_in) {
		x = x_in;
		y = y_in;
	}
	
	// dot product
	public double dot(Vector2D v) {
		return x * v.x + y * v.y;
	}
	
	// addition
	public Vector2D plus(Vector2D v) {
		double xNew = x + v.x;
		double yNew = y + v.y;
		return new Vector2D(xNew, yNew);
	}
	
	public Vector2D plusEq(Vector2D v) {
		x += v.x;
		y += v.y;
		return this;
	}
	
	// scalar product
	public Vector2D sprod(double s) {
		double xNew = x * s;
		double yNew = y * s;
		return new Vector2D(xNew, yNew);
	}
	
	public Vector2D sprodEq(double s) {
		x *= s;
		y *= s;
		return this;
	}
	
	public double findMag() {
		return Math.sqrt(x*x + y*y);
	}
	
	public double findAngle() {
		if (y < 0) {
			return 2 * Math.PI - Math.acos(x/findMag());
		}
		return Math.acos(x/findMag());
	}
}
