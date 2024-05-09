import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

// Space is the main panel on which the bodies are drawn

//@SuppressWarnings("serial")
public class Space extends JPanel {
	
	private final CelestialBodies bodies;
	
	// constructor initializes space panel
	public Space(Simulation s){
		bodies = s.bodies;
		this.setFocusable(true); // sets space JPanel as something that can be focused
		this.requestFocusInWindow(); // focuses on space for key listener
		this.setBackground(Color.black); 
		this.setLayout(null);
	}
	
	@Override // overrides JPanel paint method
	public void paintComponent(Graphics g){
		super.paintComponent(g); //super for JPanel
		Graphics2D g2D = (Graphics2D) g;

		for (int i = 0; i < bodies.size; i++) { // draws bodies
			g2D.setColor(Color.cyan);
			g2D.fillOval((int) (bodies.getQ1(i) - bodies.radius), (int) (bodies.getQ2(i) - bodies.radius), bodies.radius * 2, bodies.radius * 2);
			//g2D.drawLine((int)bodies.getQ1(i), (int)bodies.getQ2(i), (int)(bodies.getQ1(i) + bodies.Fq1[i]*5E5), (int)(bodies.getQ2(i) + bodies.Fq2[i]*5E5));
		}
		System.out.println("Done Render");
		// https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html
		// synchronized with Simulation.regenGraphics() to allow graphics to process after every iteration
		// otherwise, repaint() can collapse successive calls into one
	}

}
