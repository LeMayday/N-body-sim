import java.util.Random;

public class Main {
	
	public static void main(String[] args) {
		// initialize main components
		Simulation sim = new Simulation();
		sim.start();
		
		//sim.bodies.clear();
		double Ri, Thi, Pxi, Pyi;
		double mass = 1.0;
//		for (int i = 0; i < 10; i++){ //generates random bodies scattered in a circle centered at the middle of the screen
//			int x0 = sim.frame.getFrameSize().width/2;
//			int y0 = sim.frame.getFrameSize().height/2;
//
//			Random random = new Random();
//
//			Ri = random.nextDouble()*300;
//			Thi = random.nextDouble()*2*Math.PI;
//
//			double xVel = random.nextDouble() * (random.nextBoolean() ? -1 : 1) * Math.pow(10,  sim.STS - sim.SDS + 3);
//			double yVel = random.nextDouble() * (random.nextBoolean() ? -1 : 1) * Math.pow(10,  sim.STS - sim.SDS + 3);
//			Pxi = mass * xVel;
//			Pyi = mass * yVel;
//			sim.bodies.addBody(new double[]{Ri * Math.cos(Thi) + x0, Ri * Math.sin(Thi) + y0, Pxi, Pyi, mass}, true);
//		}
		
	}
	
}
