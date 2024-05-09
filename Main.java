import java.util.Random;

public class Main {
	
	public static void main(String[] args) {
		// initialize main components
		Simulation sim = new Simulation();
		sim.generateRandomBodies(10000);

		//		 earth moon -- SDS = 6, SMS = 24
//		int STS = 1;
//		int SDS = 6;
//		sim.addBody(new double[]{500, 500, 0, 0, 5.972}, true);
//		sim.addBody(new double[]{884.4, 500, 0, (-1023 * Math.pow(10,  STS - SDS))*7.348E-2, 7.348E-2}, true);
//
//		sim.bodies.initializeAllMomenta();

		sim.start();
	}
	
}
