import java.awt.event.KeyEvent;

// https://en.wikipedia.org/wiki/Hamiltonian_Monte_Carlo
// https://young.physics.ucsc.edu/115/leapfrog.pdf
// https://en.wikipedia.org/wiki/N-body_problem#General_formulation
// https://en.wikipedia.org/wiki/Gravitational_energy
// https://en.wikipedia.org/wiki/Leapfrog_integration

// CelestialBodies stores all information for each body and performs integration

public class CelestialBodies {
	public int size = 0;
	public int radius = 3;
	
	// arrays of body properties
	// Q is a generalized coordinate
	private double[] Q1 = new double[size];
	private double[] Q2 = new double[size];
	private double[] Pq1 = new double[size];
	private double[] Pq2 = new double[size];
	// store forces globally to prevent creating new arrays all the time
	private double[] Fq1 = new double[size];
	private double[] Fq2 = new double[size];
	//private double[] H = new double[size];
	
	private double[] masses = new double[size];
	
	private final double G;
	private final int SDS, SMS, STS, SGS; // SGS is simulation gravitational scale
	private Simulation sim;
		
	public CelestialBodies(Simulation s, int[] scales) {
		SDS = scales[0];
		SMS = scales[1];
		STS = scales[2];
		SGS = -11 - SDS*3 + SMS + STS*2;
		// derived from SI units for G (m^3/(kg*s^2)), with 1 SDU = [SDS] m, 1 SMU = [SMS] kg, 1 STU = [STS] s
		// so Gsim (SDU^3/(SMU*STU^2)) = G * (1e-SDS)^3 * 1eSMS * (1eSTS)^2
		G = 6.67 * Math.pow(10, SGS);
		sim = s;
	}
	
	// perform a full integration step
	public void iterate() {
		for (int i = 0; i < size; i++) {
			computeForcesOnBody(i);
		}
		computeVariables();
	}
	
	// add body
	// params is of the form [Q1, Q2, Pq1, Pq2, mass]
	public void addBody(double[] params) {
		Q1 = push(Q1, params[0]);
		Q2 = push(Q2, params[1]);
		masses = push(masses, params[4]);
		// add placeholder to other arrays
		Pq1 = push(Pq1, 0);
		Pq2 = push(Pq2, 0);
		Fq1 = push(Fq1, 0);
		Fq2 = push(Fq2, 0);
		//H = push(H, 0);
		// can now access array at size since arrays are bigger
		initializeMomenta(size, params[2], params[3]);
		// increment size
		size++;
	}
	
	public void clear() {
		size = 0;
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
	
	private void initializeMomenta(int index, double Pq1_0, double Pq2_0) {
		// https://young.physics.ucsc.edu/115/leapfrog.pdf pg 3 (bottom)
		// momenta are given for t = 0, so need to do a half step of Euler to get t = -1/2
		computeForcesOnBody(index);
		Pq1[index] = Pq1_0 - 0.5 * Fq1[index] * sim.dt;
		Pq2[index] = Pq2_0 - 0.5 * Fq2[index] * sim.dt;
	}
	
	// pseudo-code:
	// mA(x) = GMm/r^3 * distX (or distY)
	// p(i+1/2) = p(i-1/2) + mA(x)dt
	// x(i+1) = x(i) + p(i+1/2)/m dt
	// q1 is x, q2 is y
	
	// compute force on body index from all other bodies and add it to forces array
	private void computeForcesOnBody(int i) {
		// contribution from body j
		for (int j = 0; j < size; j++) {
			if (i != j) {
				double distQ1 = Q1[j] - Q1[i];
				double distQ2 = Q2[j] - Q2[i];
				double r = Math.sqrt(distQ1*distQ1 + distQ2*distQ2);
				
				Fq1[i] = G * masses[j] * masses[i] / (r * r * r) * distQ1;
				Fq2[i] = G * masses[j] * masses[i] / (r * r * r) * distQ2;
				
				//H[i] = 1/(2*masses[i])*(Pq1[i]*Pq1[i] + Pq2[i]*Pq2[i]) - G*masses[j]*masses[i]/r;
			}
		}
	}
	
	// increment qi and pqi according to leapfrog method
	private void computeVariables() {
		// no need for two separate arrays with old and new values since all the force computations are done first
		for (int i = 0; i < size; i++) {
			Pq1[i] += Fq1[i] * sim.dt;
			Pq2[i] += Fq2[i] * sim.dt;
			Q1[i] += Pq1[i] / masses[i] * sim.dt;
			Q2[i] += Pq2[i] / masses[i] * sim.dt;
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
		
		for (int i = 0; i < size; i++) {
			tempArr[i] = oldArr[i];
		}
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
}
