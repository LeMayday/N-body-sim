import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.Timer;

// Simulation stores global variables and interfaces between computation and display

public class Simulation implements ActionListener{

	public double dt = 10.0;
	public long iters = 0;
	private final Timer timer = new Timer(1, this);
	public AppFrame frame;
	public Space space;
	private final TimePanel tPanel;
	public CelestialBodies bodies;
    //conversion exponents
	public final int SDS = 6; 	// simulation distance scale, 1 pixel (SDU) = 1e[SDS] m
	public final int SMS = 24; 	// simulation mass scale, 1 SMU = 1e[SMS] kg
	public final int STS = 1; 	// simulation time scale, 1 STU = 1e[STS] s

	private final int NUM_THREADS = 8;

	private final ExecutorService service = Executors.newFixedThreadPool(NUM_THREADS);
	private final CelestialBodies.PhysicsTask[] physicsTasks;
	//private PhysicsTask task = new PhysicsTask(this);
	
	public Simulation() {
		bodies = new CelestialBodies(this, new int[] {SDS, SMS, STS});
		space = new Space(this);
		frame = new AppFrame(this);
		space.addKeyListener(new KeyManager()); // adds key listener to space

		physicsTasks = new CelestialBodies.PhysicsTask[NUM_THREADS];
		createThreads();
		
		DataPanel dataPanel = new DataPanel(this);
		//space.add(dataPanel);

		tPanel = new TimePanel();
		space.add(tPanel);
		
		//space.addMouseListener(new MouseManager()); // adds mouse listener to space
		
		// earth moon -- SDS = 6, SMS = 24
		bodies.addBody(new double[]{500, 500, 0, 0, 5.972}, true);
		bodies.addBody(new double[]{884.4, 500, 0, (-1023 * Math.pow(10,  STS - SDS))*7.348E-2, 7.348E-2}, true);
		
		bodies.initializeAllMomenta();
	}

	private void createThreads() {
		for (int i = 0; i < NUM_THREADS; i++) {
			physicsTasks[i] = bodies.new PhysicsTask("Thread " + i);
		}
	}

	// main method for updating indices for each thread
	public void update_physics_indices() {
		// https://stackoverflow.com/a/16020807
		byte[] index_list = new byte[NUM_THREADS];
		for (byte i = 0; i < NUM_THREADS; index_list[i] = i++);
		assign_physics_threads(index_list, NUM_THREADS, 0, bodies.size);
	}

	private void assign_physics_threads(byte[] idxs, int num_threads, int start, int end) {
		// base case: if there's only one thread left to assign
		if (num_threads == 1) {
			physicsTasks[idxs[0]].assignIndices(start, end);
			return;
		}
		// needed to correctly balance assignments for odd numbers of threads
		if (num_threads % 2 == 1) {
			int end_temp = start + (end - start) / num_threads;
			physicsTasks[idxs[0]].assignIndices(start, end_temp);
			start = end_temp;
			idxs = Arrays.copyOfRange(idxs, 1, idxs.length);
			num_threads--;
		}
		// recursive call
		int midpt = start + (end - start) / 2;
		assign_physics_threads(Arrays.copyOfRange(idxs, 0, idxs.length / 2), num_threads / 2, start, midpt);
		assign_physics_threads(Arrays.copyOfRange(idxs, idxs.length / 2, idxs.length), num_threads - num_threads / 2, midpt, end);
	}

	public void start() {
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Timer){
			space.repaint();
			try {
				service.invokeAll(Arrays.asList(physicsTasks));
			} catch (InterruptedException ex) {
				throw new RuntimeException(ex);
			}
			bodies.iterate();
			iters++;
			tPanel.updateLabel(iters * dt * Math.pow(10, STS) / 3600 / 24);
		}		
	}
	
	public boolean isRunning() {
		return timer.isRunning();
	}
	
	// toggles simulation pause and manages changes in components
	public void togglePause() {
		if (isRunning()) {
			timer.stop();
			tPanel.togglePaused();
		}
		else {
			timer.start();
			tPanel.togglePaused();
		}
		tPanel.validate();
		space.repaint(tPanel.getX(), tPanel.getY(), tPanel.getWidth(), tPanel.getHeight());
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
                    byte increment = 5;
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
	


