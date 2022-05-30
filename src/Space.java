import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

// Space is the main panel on which the bodies are drawn

@SuppressWarnings("serial")
public class Space extends JPanel implements ActionListener {
	
	private AppFrame frame;
	private CelestialBodies bodies;
	private SimCalculation simCalc;
	private double mass, time = 0, dt;
	private int bodyRadius, increment;
	private final int SDS, SMS, STS;
	
	// constructor initializes space panel
	public Space(AppFrame af, CelestialBodies bs, SimCalculation sc, double[] vars, int[] scales){
		frame = af;
		
		dt = vars[0];
		mass = vars[1];
		bodyRadius = (int)vars[2];
		increment = (int)vars[3];
		
		SDS = scales[0];
		SMS = scales[1];
		STS = scales[2];
		
		bodies = bs;
		simCalc = sc;
		
		this.setFocusable(true); // sets space JPanel as something that can be focused
		this.requestFocusInWindow(); // focuses on space for key listener
		this.addKeyListener(new KeyManager()); // adds key listener to space
		this.addMouseListener(new MouseManager()); // adds mouse listener to space
		this.setBackground(Color.black); 
		this.setLayout(null);
		
		// test bodies
		//bodies.add(new Vector2D(500,500), new Vector2D(), 1E3, 3, new Color(107, 164, 255));
		//bodies.add(new Vector2D(700,500), new Vector2D(), 1E3, 3, new Color(107, 164, 255));
		
		// earth moon
		bodies.add(new Vector2D(500,500), new Vector2D(), 5.972, 3, new Color(107, 164, 255));
		bodies.add(new Vector2D(884.4,500), new Vector2D(0, -0.001022 * Math.pow(10, STS)), 7.348E-2, 3, new Color(107, 164, 255));
	}
	
	@Override // overrides JPanel paint method
	public void paintComponent(Graphics g){
		super.paintComponent(g); //super for JPanel
		Graphics2D g2D = (Graphics2D) g;
		
		if (frame.simRunning()) {
			calculate(); // do calculations
			time += dt;
		}
		
		for (int i = 0; i < bodies.size; i++){ // draws bodies
			Vector2D bodyPos = bodies.getPos(i);
			
			g2D.setColor(bodies.getColor(i));
			g2D.fillOval((int)(bodyPos.x - bodies.getRadius(i)), (int)(bodyPos.y - bodies.getRadius(i)), bodies.getRadius(i)*2, bodies.getRadius(i)*2);
		}
	
	}
	
	// updates mass and dt from sliders
	public void setVars(double t, double m) {
		dt = t;
		simCalc.setData(dt);
		mass = m;
	}
	
	// method to manage threads so it is not in paint()
	private void calculate() {
		// good to note that 2 thread are running normally, one for main and one for swing
		Thread calcThread = new Thread(simCalc);
		calcThread.start();
		
		// must wait for all threads to finish before moving on so that bodies
		// are not drawn before calculations are completed
		try {
			calcThread.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//System.out.println("active threads: " + Thread.activeCount()); // print statement to check if only 2 threads are active
	}
	
	// updates labels and paints the frame based on timer
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Timer){
			frame.sPanel.updateLabel();
			frame.tPanel.updateLabel(time);
			repaint();
		}
	}
	
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
			
			bodies.add(new Vector2D(xInitial, yInitial), new Vector2D(xVel*velFac, yVel*velFac), mass, bodyRadius, new Color(r, g, b));
		}
		
	}
	
	private class KeyManager extends KeyAdapter {
		
		public void keyPressed(KeyEvent e) {
			// only perform key actions if sim is not paused
			switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE:
				frame.togglePause();
				break;
			case KeyEvent.VK_I:
				frame.toggleInstructionPanel();
				break;
			case KeyEvent.VK_O:
				frame.toggleSliderPanel();
				break;
			default:
				if (frame.simRunning()) {
					bodies.incrementPositions(e, increment);
				}
			}
		}
	
	}
	
}
