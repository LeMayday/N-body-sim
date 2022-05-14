
public class Vector2D {
	
	public double x;
	public double y;
	public double mag;
	public double angle;
	
	public Vector2D() {
		x = 0;
		y = 0;
		findMag();
		findAngle();
	}
	
	public Vector2D(double x_in, double y_in) {
		x = x_in;
		y = y_in;
		findMag();
		findAngle();
	}
	
	//dot product
	public double dot(Vector2D v) {
		return x * v.x + y * v.y;
	}
	
	//addition
	public Vector2D plus(Vector2D v) {
		double xNew = x + v.x;
		double yNew = y + v.y;
		return new Vector2D(xNew, yNew);
	}
	
	public Vector2D plusEq(Vector2D v) {
		x += v.x;
		y += v.y;
		findMag();
		findAngle();
		return this;
	}
	
	//scalar product
	public Vector2D sprod(double s) {
		double xNew = x * s;
		double yNew = y * s;
		return new Vector2D(xNew, yNew);
	}
	
	public Vector2D sprodEq(double s) {
		x *= s;
		y *= s;
		findMag();
		findAngle();
		return this;
	}
	
	private void findMag() {
		mag = Math.sqrt(x*x + y*y);
	}
	
	private void findAngle() {
		if (y < 0) {
			angle = 2 * Math.PI - Math.acos(x/mag);
		}
		else{
			angle = Math.acos(x/mag);
		}
	}
}
