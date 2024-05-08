
// PhysicsTask is run by threads and calls integration methods in CelestialBodies

import java.util.concurrent.Callable;

//public class PhysicsTask implements Callable<Void> {
//
//	private final Simulation sim;
//	private final String name;
//	private int ibegin;
//	private int iend;
//
//	PhysicsTask(Simulation s, String name) {
//		sim = s;
//		this.name = name;
//	}
//
//	public void assignIndices (int ibegin, int iend) {
//		this.ibegin = ibegin;
//		this.iend = iend;
//	}
//
//	@Override
//	public Void call() {
//		long currentTime = System.currentTimeMillis();
//
//		sim.bodies.computeForces(ibegin, iend);
//		//System.out.println(sim.bodies.getQ1(1));
//
//		long computeTime = System.currentTimeMillis() - currentTime;
//		//System.out.println("Iteration took " + computeTime + " ms");
//
//		sim.iters++;
//		//System.out.println(sim.iters);
//		return null;
//	}
//}
