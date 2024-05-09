import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.Callable;

// https://en.wikipedia.org/wiki/Hamiltonian_Monte_Carlo
// https://young.physics.ucsc.edu/115/leapfrog.pdf
// https://en.wikipedia.org/wiki/N-body_problem#General_formulation
// https://en.wikipedia.org/wiki/Gravitational_energy
// https://en.wikipedia.org/wiki/Leapfrog_integration

// CelestialBodies stores all information for each body and performs integration

public class CelestialBodies {
	public int size = 0;
	public int radius = 2;
	
	// arrays of body properties
	// Q is a generalized coordinate
	private double[] Q1 = new double[size];
	private double[] Q2 = new double[size];
	private double[] Pq1 = new double[size];
	private double[] Pq2 = new double[size];
	// store forces globally to prevent creating new arrays all the time
	public double[] Fq1 = new double[size];
	public double[] Fq2 = new double[size];
	//private double[] H = new double[size];
	
	private double[] masses = new double[size];
	
	private final double G;
	private double dt;
	//private final int SDS, SMS, STS, SGS; // SGS is simulation gravitational scale
		
	public CelestialBodies(int[] scales, double dt) {
		int SDS = scales[0];
		int SMS = scales[1];
		int STS = scales[2];
		int SGS = -11 - SDS*3 + SMS + STS*2;
		// derived from SI units for G (m^3/(kg*s^2)), with 1 SDU = [SDS] m, 1 SMU = [SMS] kg, 1 STU = [STS] s
		// so Gsim (SDU^3/(SMU*STU^2)) = G * (1e-SDS)^3 * 1eSMS * (1eSTS)^2
		G = 6.67 * Math.pow(10, SGS);
		this.dt = dt;
	}

	public void iterate() {
		computeVariables();
	}
	
	// add body
	// params is of the form [Q1, Q2, Pq1, Pq2, mass]
	public void addBody(double[] params, boolean initializeAll) {
		Q1 = push(Q1, params[0]);
		Q2 = push(Q2, params[1]);
		// momenta are initialized later to step - 1/2
		Pq1 = push(Pq1, params[2]);
		Pq2 = push(Pq2, params[3]);
		masses = push(masses, params[4]);
		// add placeholder to other arrays
		Fq1 = push(Fq1, 0);
		Fq2 = push(Fq2, 0);
		//H = push(H, 0);
		
		// initializes momenta for one body if they are added later
		// if bodies are all added at the beginning, want to initialize momenta together
		if (!initializeAll) {
			initializeMomenta(size);
		}
		// increment size
		size++;
	}

	public void generateRandomBodies(int numRandomBodies, int x0, int y0, double velocityScale) {
		clearAndInitialize(numRandomBodies);
		double Ri, Thi, Pxi, Pyi;
		double mass = 1.0;
		Random random = new Random();
		for (int i = 0; i < numRandomBodies; i++){ //generates random bodies scattered in a circle centered at the middle of the screen
			Ri = random.nextDouble()*900;
			Thi = random.nextDouble()*2*Math.PI;

			double xVel = random.nextDouble() * (random.nextBoolean() ? -1 : 1) * velocityScale;
			double yVel = random.nextDouble() * (random.nextBoolean() ? -1 : 1) * velocityScale;
			Pxi = mass * xVel;
			Pyi = mass * yVel;

			Q1[i] = Ri * Math.cos(Thi) + x0;
			Q2[i] = Ri * Math.sin(Thi) + y0;
			Pq1[i] = Pxi;
			Pq2[i] = Pyi;
			masses[i] = mass;
		}
	}
	
	// initializes all momenta after all bodies have been added
	public void initializeAllMomenta() {
		for (int i = 0; i < size; i++) {
			//System.out.println(Pq1[i] + " " + Pq2[i]);
			initializeMomenta(i);
			//System.out.println(Pq1[i] + " " + Pq2[i]);
		}
	}
	
	public void clear() {
		clearAndInitialize(0);
	}

	private void clearAndInitialize(int size) {
		this.size = size;
		Q1 = new double[size];
		Q2 = new double[size];
		Pq1 = new double[size];
		Pq2 = new double[size];
		Fq1 = new double[size];
		Fq2 = new double[size];
		//H = new double[size];
		masses = new double[size];
	}
	
	public double getQ1(int index) {
		return Q1[index];
	}
	
	public double getQ2(int index) {
		return Q2[index];
	}
	
	private void initializeMomenta(int index) {
		// https://young.physics.ucsc.edu/115/leapfrog.pdf pg 3 (bottom)
		// momenta are given for t = 0, so need to do a half step of Euler to get t = -1/2
		computeForcesOnBody(index);
		Pq1[index] -= 0.5 * Fq1[index] * dt;
		Pq2[index] -= 0.5 * Fq2[index] * dt;
	}
	
	// pseudo-code:
	// mA(x) = GMm/r^3 * distX (or distY)
	// p(i+1/2) = p(i-1/2) + mA(x)dt
	// x(i+1) = x(i) + p(i+1/2)/m dt
	// q1 is x, q2 is y

	private void computeForces(int ibegin, int iend) {
		for (int i = ibegin; i < iend; i++) {
			computeForcesOnBody(i);
		}
	}
	
	// compute force on body index from all other bodies and add it to forces array
	private void computeForcesOnBody(int i) {
		// contribution from body j
		double distQ1, distQ2, r, gradUq1 = 0, gradUq2 = 0;
		for (int j = 0; j < size; j++) {
			if (i != j) {
				distQ1 = Q1[j] - Q1[i];
				distQ2 = Q2[j] - Q2[i];
				r = Math.sqrt(distQ1*distQ1 + distQ2*distQ2);
				r = Math.max(1.0, r);
				
				gradUq1 += G * masses[j] * masses[i] / (r * r * r) * distQ1;
				gradUq2 += G * masses[j] * masses[i] / (r * r * r) * distQ2;
				
				//H[i] = 1/(2*masses[i])*(Pq1[i]*Pq1[i] + Pq2[i]*Pq2[i]) - G*masses[j]*masses[i]/r;
			}
		}
		Fq1[i] = gradUq1;
		Fq2[i] = gradUq2;
		//System.out.println(i + " " + Fq1[i] + " " + Fq2[i]);
	}
	
	// increment qi and pqi according to leapfrog method
	private void computeVariables() {
		// no need for two separate arrays with old and new values since all the force computations are done first
		for (int i = 0; i < size; i++) {
			Pq1[i] += Fq1[i] * dt;
			Pq2[i] += Fq2[i] * dt;
			Q1[i] += Pq1[i] / masses[i] * dt;
			Q2[i] += Pq2[i] / masses[i] * dt;
		}
		/*
		if (size >= 2) {
			System.out.println(H[0] + H[1]);
			System.out.println(Pq1[1] + " " + Pq2[1]);
			System.out.println(Fq1[1] + " " + Fq2[1]);
		}
		*/
	}
	
	// push one element to end of oldArr
	private double[] push(double[] oldArr, double value) {
		double[] tempArr = new double[size + 1];

        if (size >= 0) System.arraycopy(oldArr, 0, tempArr, 0, size);
		tempArr[size] = value;
		
		return tempArr;
	}
	/*	
	// delete an element at a specific index
	private double[] delete(double[] oldArr, int index) {
		double[] tempArr = new double[size - 1];
		
		for (int i = 0; i < index; i++) {
			tempArr[i] = oldArr[i];
		}
		for (int i = index + 1; i < size; i++) {
			tempArr[i - 1] = oldArr[i];
		}
		
		return tempArr;
	}
	*/
	// moves bodies all uniformly in direction dictated by WASD keys
	public void incrementPositions(KeyEvent e, byte increment){
		for(int i = 0; i < size; i++) {
			switch(e.getKeyCode()){
			case KeyEvent.VK_W:
				Q2[i] += increment;
				break;
			case KeyEvent.VK_S:
				Q2[i] -= increment;
				break;
			case KeyEvent.VK_D:
				Q1[i] -= increment;
				break;
			case KeyEvent.VK_A:
				Q1[i] += increment;
				break;
			}
		}
		
	}

	// PhysicsTask is run by threads and calls integration methods in CelestialBodies
	class PhysicsTask implements Callable<Void> {

		private int ibegin;
		private int iend;

		PhysicsTask() {}

		public void assignIndices (int ibegin, int iend) {
			this.ibegin = ibegin;
			this.iend = iend;
		}

		@Override
		public Void call() {
			computeForces(ibegin, iend);
			return null;
		}

	}

}
