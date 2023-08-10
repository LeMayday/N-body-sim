import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.Timer;

// Simulation stores global variables and interfaces between computation and display

public class Simulation implements ActionListener{

	public double dt = 10.0;
	public long iters = 0;
	private Timer timer = new Timer(1, this);;
	private boolean isRunning = false;
	public AppFrame frame;
	public Space space;
	public CelestialBodies bodies;
	private byte increment = 5;
	//conversion exponents
	public final int SDS = 6; 	// simulation distance scale, 1 pixel (SDU) = 1e[SDS] m
	public final int SMS = 24; 	// simulation mass scale, 1 SMU = 1e[SMS] kg
	public final int STS = 2; 	// simulation time scale, 1 STU = 1e[STS] s
	
	private ExecutorService service = Executors.newSingleThreadExecutor();
	private WorkTask task = new WorkTask(this);
	
	public Simulation() {
		bodies = new CelestialBodies(this, new int[] {SDS, SMS, STS});
		space = new Space(this);
		frame = new AppFrame(this);
		space.addKeyListener(new KeyManager()); // adds key listener to space
		//space.addMouseListener(new MouseManager()); // adds mouse listener to space
		
		// earth moon -- SDS = 6, SMS = 24
		//bodies.addBody(new double[]{500, 500, 0, 0, 5.972}, true);
		//bodies.addBody(new double[]{884.4, 500, 0, (-1023 * Math.pow(10,  STS - SDS))*7.348E-2, 7.348E-2}, true);
		
		// r1(0) = -r3(0) = (-0.97000436, 0.24308753); r2(0) = (0,0); v1(0) = v3(0) = (0.4662036850, 0.4323657300); v2(0) = (-0.93240737, -0.86473146)
		
		double m = Math.sqrt(10/6.67);
		
		bodies.addBody(new double[]{-0.97000436*100 + 600, -0.24308753*100 + 500, m*0.4662036850*Math.pow(10, 3 + STS - SDS), m*-0.4323657300*Math.pow(10, 3 + STS - SDS), m}, true);
		//bodies.addBody(new double[]{600, 500, 0, 0, m}, true);
		bodies.addBody(new double[]{600, 500, m*-0.93240737*Math.pow(10, 3 + STS - SDS), m*0.86473146*Math.pow(10, 3 + STS - SDS), m}, true);
		bodies.addBody(new double[]{0.97000436*100 + 600, 0.24308753*100 + 500, m*0.4662036850*Math.pow(10, 3 + STS - SDS), m*-0.4323657300*Math.pow(10, 3 + STS - SDS), m}, true);
		
		/*
		bodies.addBody(new double[]{500, 500, 0, 0, m}, true);
		bodies.addBody(new double[]{600, 500, 0, 0, m}, true);
		bodies.addBody(new double[]{700, 500, m*0.3471128135672417*Math.pow(10, 3 + STS - SDS), m*-0.532726851767674*Math.pow(10, 3 + STS - SDS), m}, true);
		*/
		bodies.initializeAllMomenta();
	}
	
	public void start() {
		isRunning = true;
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Timer){
			space.repaint();
			service.submit(task);
		}		
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	// toggles simulation pause and manages changes in components
	public void togglePause() {
		if (isRunning) {
			isRunning = false;
			//tPanel.togglePaused(simRunning);
		}
		else {
			isRunning = true;
			//tPanel.togglePaused(simRunning);
		}
	}
	
	private class KeyManager extends KeyAdapter {
		
		public void keyPressed(KeyEvent e) {
			// only perform key actions if sim is not paused
			switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE:
				togglePause();
				break;
			case KeyEvent.VK_I:
				//frame.toggleInstructionPanel();
				break;
			case KeyEvent.VK_O:
				//frame.toggleSliderPanel();
				break;
			default:
				if (isRunning()) {
					bodies.incrementPositions(e, increment);
				}
			}
		}
	}
	/*
	private class MouseManager extends MouseAdapter {
		
		private int xInitial, yInitial;
		
		public void mousePressed(MouseEvent e) {
			xInitial = e.getX(); // gets initial coordinates of mouse press
			yInitial = e.getY();
		}

		public void mouseReleased(MouseEvent e) {
			int xFinal = e.getX(); // gets final coordinates of mouse press
			int yFinal = e.getY();
			
			double xVel = xFinal - xInitial; // initial coordinates are subtracted to get velocity
			double yVel = yFinal - yInitial;
			double velFac = 0.002; // arbitrary value determined through experimentation
			
			int r = (int)(Math.random()*155 + 100);
			int g = (int)(Math.random()*155 + 100);
			int b = (int)(Math.random()*155 + 100);
			
			bodies.push();
		}
	}
	*/
	
}
	


