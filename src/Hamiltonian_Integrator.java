/*
public class Hamiltonian_Integrator {
	
	private int numBodies;
	private double dt;
	private Celestial_Bodies bodies;
	private final double G;
	private final int SDS, SMS, STS, SGS; // SGS is simulation gravitational scale
	private boolean flag = true;
	
	public Hamiltonian_Integrator(Celestial_Bodies bs, double t, int[] scales) {
		dt = t;
		bodies = bs;
		SDS = scales[0];
		SMS = scales[1];
		STS = scales[2];
		SGS = -11 - SDS*3 + SMS + STS*2;
		G = 6.67 * Math.pow(10, SGS);
		// derived from SI units for G (m^3/(kg*s^2)), with 1 SDU = [SDS] m, 1 SMU = [SMS] kg, 1 STU = [STS] s
		// so Gsim (SDU^3/(SMU*STU^2)) = G * (1e-SDS)^3 * 1eSMS * (1eSTS)^2
	}
	
	public void update(double t) {
		numBodies = bodies.size;
		dt = t;
	}
	
	public void iterate() {
		for (int i = 0; i < bodies.size; i++) {
			computeForcesOnBody(i);
		}
		computeVariables();
	}
	
	public void initializeMomenta(int index) {
		// https://young.physics.ucsc.edu/115/leapfrog.pdf pg 3 (bottom)
		computeForcesOnBody(index);
		bodies.Pq1_minushalf[index] = bodies.Pq1_0[index] - 0.5 * bodies.Fq1[index] * dt;
		bodies.Pq2_minushalf[index] = bodies.Pq2_0[index] - 0.5 * bodies.Fq2[index] * dt;
	}
	
	// mA(x) = GMm/r^3 * distX (or distY)
	// p(i+1/2) = p(i-1/2) + mA(x)dt
	// x(i+1) = x(i) + p(i+1/2)/m dt
	
	// q1 is x, q2 is y
	
	private void computeForcesOnBody(int index) {
		// contribution from body j
		for (int j = 0; j < numBodies; j++) {
			if (index != j) {
				double distQ1 = bodies.Q1_0[j] - bodies.Q1_0[index];
				double distQ2 = bodies.Q2_0[j] = bodies.Q2_0[index];
				double r = Math.sqrt(distQ1*distQ1 + distQ2*distQ2);
				
				double gradUq1 = G * bodies.masses[j] * bodies.masses[index] / (r * r * r) * distQ1;
				double gradUq2 = G * bodies.masses[j] * bodies.masses[index] / (r * r * r) * distQ2;
				
				bodies.Fq1[index] = gradUq1;
				bodies.Fq2[index] = gradUq2;
			}
		}
	}
	
	private void computeVariables() {
		for (int i = 0; i < numBodies; i++) {
			bodies.Pq1_plushalf[i] = bodies.Pq1_minushalf[i] + bodies.Fq1[i] * dt;
			bodies.Pq2_plushalf[i] = bodies.Pq2_minushalf[i] + bodies.Fq2[i] * dt;
			bodies.Q1_1[i] = bodies.Q1_0[i] + bodies.Pq1_plushalf[i] / bodies.masses[i] * dt;
			bodies.Q2_1[i] = bodies.Q2_0[i] + bodies.Pq2_plushalf[i] / bodies.masses[i] * dt;
			
			bodies.Pq1_minushalf[i] = bodies.Pq1_plushalf[i];
			bodies.Pq2_minushalf[i] = bodies.Pq2_plushalf[i];
			bodies.Q1_0[i] = bodies.Q1_1[i];
			bodies.Q2_0[i] = bodies.Q2_1[i];
		}
	}
		
}
*/