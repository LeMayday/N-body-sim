import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

// CelestialBodies class is meant to act as an array of all bodies
public class CelestialBodies {
	public int size;
	
	// arrays of body properties
	private ArrayList<Vector2D> positions = new ArrayList<Vector2D>();
	private ArrayList<Vector2D> velocities = new ArrayList<Vector2D>();
	private ArrayList<Double> masses = new ArrayList<Double>();
	private ArrayList<Double> volumes = new ArrayList<Double>();
	private ArrayList<Color> colors = new ArrayList<Color>();
	private ArrayList<Boolean> collisionsDelete = new ArrayList<Boolean>();
	private ArrayList<Boolean> boundsDelete = new ArrayList<Boolean>();
	
	public CelestialBodies() {
		size = 0;
	}
	
	// add body
	public void add(Vector2D pos, Vector2D vel, double mass, int radius, Color color){
		positions.add(pos);
		velocities.add(vel);
		masses.add(mass);
		volumes.add( (4/3.0)*Math.PI*Math.pow(radius, 4) ); //volume done with power of 4 to further decrease rate at which particles gain size
		colors.add(color);
		collisionsDelete.add(false);
		boundsDelete.add(false);
		size++;
	}
	
	// removes all bodies
	public void clear() {
		while (size > 0){
			positions.remove(0);
			velocities.remove(0);
			masses.remove(0);
			volumes.remove(0);
			colors.remove(0);
			collisionsDelete.remove(0);
			boundsDelete.remove(0);
			size--;
		}
	}
	
	// set functions to update new positions and velocities 
	public void setPos(ArrayList<Vector2D> newPos) {
		positions = newPos; //shallow copy since newPos is a new ArrayList made in SimCalculation
	}
	
	public void setVel(ArrayList<Vector2D> newVel) {
		velocities = newVel;
	}
	
	// get functions are meant to fetch data for one particular body
	public Vector2D getPos(int index) {
		return positions.get(index);
	}
	
	public Vector2D getVel(int index) {
		return velocities.get(index);
	}
	
	public double getMass(int index) {
		return masses.get(index);
	}
	
	public double getVolume(int index) {
		return volumes.get(index);
	}
	
	public int getRadius(int index) {
		int radius = (int)Math.pow(getVolume(index)/(Math.PI*4.0/3), 1.0/4);
		return radius;
	}
	
	public Color getColor(int index) {
		return colors.get(index);
	}
	
	// moves bodies all uniformly in direction dictated by WASD keys
	public void incrementPositions(KeyEvent e, double incrementFactor){
		for(int i = 0; i < size; i++) {
			switch(e.getKeyCode()){
			case KeyEvent.VK_W:
				positions.get(i).plusEq(new Vector2D(0, incrementFactor));
				break;
			case KeyEvent.VK_S:
				positions.get(i).plusEq(new Vector2D(0, -incrementFactor));
				break;
			case KeyEvent.VK_D:
				positions.get(i).plusEq(new Vector2D(-incrementFactor, 0));
				break;
			case KeyEvent.VK_A:
				positions.get(i).plusEq(new Vector2D(incrementFactor, 0));
				break;
			}
		}
		
	}
}