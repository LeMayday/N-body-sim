
public class Main {
	
	public static void main(String[] args) {
		
		//some simulation parameters
		double dt = 1.0; 	// simulation time-step units (STU)
		double mass = 1.0; 	// simulation mass units (SMU)
		int bodyRadius = 3, increment = 5;
		
		//conversion exponents
		final int SDS = 6; 	// simulation distance scale, 1 pixel (SDU) = 1e[SDS] m
		final int SMS = 24; 	// simulation mass scale, 1 SMU = 1e[SMS] kg
		final int STS = 0; 	// simulation time scale, 1 STU = 1e[STS] s
		
		double[] vars = new double[] {dt, mass, bodyRadius, increment};
		int[] scales = new int[] {SDS, SMS, STS};
		
		// initialize main components
		CelestialBodies bodies = new CelestialBodies();
		SimCalculation simCalc = new SimCalculation(bodies, vars, scales);
		new AppFrame(bodies, simCalc, vars, scales);
		
	}
}
