/*
 * 0.9.2: Moved timer to Space.java
 * Changed units to simulation units with conversion factors
 */
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Sim {
	
	//some simulation parameters
	static double dt = 1.0; // simulation time-step units (STU)
	static double mass = 1.0; // simulation mass units (SMU)
	static int bodyRadius = 3, increment = 5;
	
	//conversion exponents
	static final int SMS = 27; // simulation mass scale, 1 SMU = 1e[SMS] kg
	static final int STS = 0; // simulation time scale, 1 STU = 1e[STS] s
	static final int SDS = 7; // simulation distance scale, 1 pixel (SDU) = 1e[SDS] m
	
	public static void main(String[] args) {
		
		double[] vars = new double[] {dt, mass, bodyRadius, increment};
		int[] scales = new int[] {SDS, SMS, STS};
		
		//get screen dimensions to size frame appropriately
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //gets display size
		int frameWidth = screenSize.width/8*7;
		int frameHeight = screenSize.height/8*7;
		Dimension frameSize = new Dimension(frameWidth, frameHeight);
		
		//initialize JFrame
		JFrame frame = new JFrame("N-Body Sim");
		frame.setPreferredSize(frameSize); //sets size of window
		frame.setLayout(null); //I think this allows me to add components how I want them
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//initialize main components
		CelestialBodies bodies = new CelestialBodies();
		SimCalculation simCalc = new SimCalculation(bodies, vars, scales);
		new Space(frame, frameSize, bodies, simCalc, vars, scales);
		
	}
}
