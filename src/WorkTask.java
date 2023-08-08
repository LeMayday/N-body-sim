
// WorkTask is run by threads and calls integration methods in CelestialBodies

public class WorkTask implements Runnable {

	private Simulation sim;
	
	WorkTask(Simulation s) {
		sim = s;
	}
	
	@Override
	public void run() {
		long currentTime = System.currentTimeMillis();
		
		sim.bodies.iterate();
		//System.out.println(sim.bodies.getQ1(1));
		
		long computeTime = System.currentTimeMillis() - currentTime;
		//System.out.println("Iteration took " + computeTime + " ms");
		
		sim.iters++;
		//System.out.println(sim.iters);
	}
}
