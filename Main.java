import java.util.Random;

public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		// initialize main components
		Simulation sim = new Simulation();
		sim.generateRandomBodies(10000);
		sim.start();
	}
	
}
