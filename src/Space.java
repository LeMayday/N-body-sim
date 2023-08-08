import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

// Space is the main panel on which the bodies are drawn

@SuppressWarnings("serial")
public class Space extends JPanel {
	
	private CelestialBodies bodies;
	
	// constructor initializes space panel
	public Space(Simulation s){
		bodies = s.bodies;
		
		this.setFocusable(true); // sets space JPanel as something that can be focused
		this.requestFocusInWindow(); // focuses on space for key listener
		
		//this.addMouseListener(new MouseManager()); // adds mouse listener to space
		this.setBackground(Color.black); 
		this.setLayout(null);
		
		// test bodies
		//bodies.add(new Vector2D(500,500), new Vector2D(), 1E3, 3, new Color(107, 164, 255));
		//bodies.add(new Vector2D(700,500), new Vector2D(), 1E3, 3, new Color(107, 164, 255));
		
	}
	
	@Override // overrides JPanel paint method
	public void paintComponent(Graphics g){
		super.paintComponent(g); //super for JPanel
		Graphics2D g2D = (Graphics2D) g;
		
		for (int i = 0; i < bodies.size; i++){ // draws bodies
			g2D.setColor(Color.cyan);
			g2D.fillOval((int)(bodies.getQ1(i) - bodies.radius), (int)(bodies.getQ2(i) - bodies.radius), bodies.radius*2, bodies.radius*2);
		}
	}

}
