import java.util.ArrayList;

// SimCalculation is where the physics is performed

public class SimCalculation implements Runnable {
	
	private CelestialBodies bodies;
	private double dt;
	private final double G;
	private final int SDS, SMS, STS, SGS; // SGS is simulation gravitational scale
	
	public SimCalculation(CelestialBodies bs, double[] vars, int[] scales) {
		bodies = bs;
		dt = vars[0];
		SDS = scales[0];
		SMS = scales[1];
		STS = scales[2];
		SGS = -11 - SDS*3 + SMS + STS*2;
		G = 6.67 * Math.pow(10, SGS);
		// derived from SI units for G (m^3/(kg*s^2)), with 1 SDU = [SDS] m, 1 SMU = [SMS] kg, 1 STU = [STS] s
		// so Gsim (SDU^3/(SMU*STU^2)) = G * (1e-SDS)^3 * 1eSMS * (1eSTS)^2
	}
	
	// sets global variables to be used in run method (which cannot accept arguments)
	public void setData(double dt) { 
		this.dt = dt;
	}
	
	@Override
	public void run() { // just does math, no collisions yet
		ArrayList<Vector2D> newPositions = new ArrayList<Vector2D>();
		ArrayList<Vector2D> newVelocities = new ArrayList<Vector2D>();
		double t = dt * Math.pow(10, STS); // convert time in seconds to STU
		
		for (int i = 0; i < bodies.size; i++) {
			// i is index of body on which force is being calculated
			Vector2D bodyPos = bodies.getPos(i); // define local variables to avoid excessive method calls
			Vector2D bodyVel = bodies.getVel(i);
			
			// using Euler-Cromer Method
			Vector2D newPos = bodyPos.plus(bodyVel.sprod(t)); // xnew = xold + v*t
			newPositions.add(newPos);
			
			Vector2D accel = new Vector2D(); // uses acceleration instead of force since a = f/m is used later
			for (int j = 0; j < bodies.size; j++){ 
				// j is index of body being used to calculate acceleration
				
				if (i != j){ //don't calculate force on same body
					Vector2D dist = bodies.getPos(j).plus(newPos.sprod(-1)); // vector from i to j (specifically j - i)
					double r = dist.findMag();
					double a = G  * bodies.getMass(j) / (r * r); // represents magnitude of force per object
					accel.plusEq(dist.sprodEq(a / r)); // adds accel component of mag a in direction of unit vector of dist
				}
			}
			
			Vector2D newVel = bodyVel.plus(accel.sprodEq(t)); // vnew = vold + a*t
			newVelocities.add(newVel);	
		}
		
		bodies.setPos(newPositions);
		bodies.setVel(newVelocities);
	}
	
}
