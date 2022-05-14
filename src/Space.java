import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Space extends JPanel implements ComponentListener, MouseListener, KeyListener, ActionListener{
	
	private Dimension frameSize;
	private CelestialBodies bodies;
	private SimCalculation simCalc;
	private SliderPanel sliderPanel;
	private double mass, xInitial, yInitial;
	private int bodyRadius, increment;
	private final int SDS, SMS, STS;
	private Timer timer;
	
	public Space(JFrame frame, Dimension fs, CelestialBodies bs, SimCalculation sc, double[] vars, int[] scales){
		mass = vars[1];
		bodyRadius = (int)vars[2];
		increment = (int)vars[3];
		
		SDS = scales[0];
		SMS = scales[1];
		STS = scales[2];
		
		frameSize = fs;
		bodies = bs;
		simCalc = sc;
		
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null); // puts frame in center of screen
		frame.addComponentListener(this); // adds component listen to detect resize
		
		this.setFocusable(true); // sets space JPanel as something that can be focused
		this.requestFocusInWindow(); // focuses on space for key listener
		this.addKeyListener(this); // adds key listener to space
		this.addMouseListener(this); // adds mouse listener to space
		this.setBackground(Color.black); 
		this.setLayout(null);
		this.setBounds(0, 0, frameSize.width, frameSize.height); //sets size of space
		
		sliderPanel = new SliderPanel(this, frameSize, bodies, vars, scales);
		new InstructionPanel(this, scales);
		
		// test bodies
		//bodies.add(new Vector2D(500,500), new Vector2D(), 1E3, 3, new Color(107, 164, 255));
		//bodies.add(new Vector2D(700,500), new Vector2D(), 1E3, 3, new Color(107, 164, 255));
		
		// earth moon
		bodies.add(new Vector2D(500,500), new Vector2D(), 5.972E-3, 3, new Color(107, 164, 255));
		bodies.add(new Vector2D(884.4,500), new Vector2D(0, 0.001022 * Math.pow(10, STS)), 7.348E-5, 3, new Color(107, 164, 255));
		
		timer = new Timer(1, this);
		timer.start();
	}
	
	@Override // overrides JPanel paint method
	public void paint(Graphics g){
		super.paint(g); //super for JPanel
		Graphics2D g2D = (Graphics2D) g;
		
		calculate(); // do calculations
		
		for (int i = 0; i < bodies.size; i++){ // draws bodies
			Vector2D bodyPos = bodies.getPos(i);
			
			g2D.setColor(bodies.getColor(i));
			g2D.fillOval((int)(bodyPos.x - bodies.getRadius(i)), (int)(bodyPos.y - bodies.getRadius(i)), bodies.getRadius(i)*2, bodies.getRadius(i)*2);
		}
		repaint();
	}
	
	public void setVars(double dt, double m) {
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
	
	@Override
	public void keyPressed(KeyEvent e) {
		bodies.incrementPositions(e, increment); 
	}

	@Override
	public void mousePressed(MouseEvent e) {
		xInitial = e.getX(); // gets initial coordinates of mouse press
		yInitial = e.getY();
	}

	@Override
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

	@Override
	public void componentResized(ComponentEvent e) {
		frameSize = e.getComponent().getBounds().getSize();
		this.setBounds(0, 0, frameSize.width, frameSize.height); // resets size of space JPanel to be consistent with size of frame
				
		for (Component comp : this.getComponents()) {
			if (comp instanceof SliderPanel) {
				comp.setLocation(frameSize.width - comp.getWidth() - 50, frameSize.height - comp.getHeight() - 50);
			}
			else if (comp instanceof InstructionPanel) {
				comp.setLocation(50, frameSize.height - comp.getHeight() - 75);
			}
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// could have timer in SliderPanel, but I wanted it to be in a more widely used
		// file in case of future need of timer
		if (e.getSource() == timer){
			sliderPanel.actionPerformed(e);
		}
	}
	
	// unused methods

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}

	@Override
	public void componentHidden(ComponentEvent e) {}
	
}
