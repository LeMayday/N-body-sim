import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// SliderPanel contains the interactive sliders and buttons

@SuppressWarnings("serial")
public class SliderPanel extends JPanel implements ChangeListener, ActionListener {
	
	private JSlider dtSlider, massSlider;
	private JLabel dtLabel, massLabel, bodiesLabel;
	private JButton clearButton, generateButton;
	private int numRandomBodies = 10000;
	public int width = 200, height = 250;
	private CelestialBodies bodies;
	private AppFrame frame;
	private double dt, mass;
	private final double SMS;
	
	// constructor initializes slider panel
	public SliderPanel(AppFrame af, CelestialBodies bs, double[] vars, int[] factors) {
		frame = af;
		bodies = bs;
		dt = vars[0];
		mass = vars[1];
		SMS = factors[1];
		
		this.setBackground(Color.black);
		this.setSize(new Dimension(width, height));
		this.setOpaque(false);
		
		sliders();
		labels();
		buttons();
		
		// ordering for components
		this.add(bodiesLabel);
		this.add(dtLabel);
		this.add(dtSlider);
		this.add(massLabel);
		this.add(massSlider);
		this.add(clearButton);
		this.add(generateButton);
	}

	// detects changes in sliders and updates other classes
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(dtSlider)){
			dt = Math.pow(10, dtSlider.getValue()/10.0);
			dtLabel.setText("Program Speed: " + Math.round(dt*10.0)/10.0 + " s"); // rounds to one decimal place
		}
		else if (e.getSource().equals(massSlider)){
			mass = (double)Math.pow(10, massSlider.getValue() - SMS);
			massLabel.setText("New Object Mass Exponent: 1E" + (int)(Math.log10(mass) + SMS));
		}
		frame.space.setVars(dt, mass);
		frame.space.requestFocusInWindow(); // rerequests focus on space so it can continue to receive key events
	}
	
	// detects button presses and performs respective actions
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == clearButton){
			bodies.clear();
		}
		else if (e.getSource() == generateButton){
			bodies.clear();
			double xInitial, yInitial, xVel, yVel;
			for (int i = 0; i < numRandomBodies; i++){ //generates random bodies scattered in a rectangle
				xInitial = Math.random()*1500 + 100;
				yInitial = Math.random()*700 + 100;
				xVel = Math.random()*300 - 150;
				yVel = Math.random()*300 - 150;
				bodies.add(new Vector2D(xInitial, yInitial), new Vector2D(xVel, yVel), mass, 1, new Color(107, 164, 255));
			}
		}
		frame.space.requestFocusInWindow();
	}
	
	// updates bodies label (called from Space)
	public void updateLabel() {
		bodiesLabel.setText("Bodies Remaining: " + bodies.size);
	}
	
	// these methods initialize sliders, labels, and buttons
	private void sliders() {
		//(int)Math.log10(dt)*10
		dtSlider = new JSlider(-10, 20, 0);
		dtSlider.setPreferredSize(new Dimension(200,50));
		dtSlider.setForeground(Color.white);
		dtSlider.setOpaque(false);
		dtSlider.setPaintTrack(true);
		dtSlider.setMajorTickSpacing(10);
		dtSlider.setPaintLabels(true);
		dtSlider.addChangeListener(this);
		
		int mslb = (int)SMS - 5; // mass slider lower bound
		int msub = (int)SMS + 5; // mass slider upper bound
		massSlider = new JSlider(mslb, msub, (int)SMS);
		massSlider.setPreferredSize(new Dimension(200,50));
		massSlider.setForeground(Color.white);
		massSlider.setOpaque(false);
		massSlider.setPaintTrack(true);
		massSlider.setMajorTickSpacing(1);
		massSlider.setPaintLabels(true);
		massSlider.addChangeListener(this);
	}
	
	private void labels() {
		dtLabel = new JLabel("Program Speed: " + dt + " s");
		dtLabel.setForeground(Color.white);
		
		massLabel = new JLabel("New Object Mass Exponent: 1E" + (int)(Math.log10(mass) + SMS));
		massLabel.setForeground(Color.white);
		
		bodiesLabel = new JLabel("Bodies Remaining: " + bodies.size);
		bodiesLabel.setForeground(Color.white);
	}
	
	private void buttons() {
		clearButton = new JButton("Clear");
		clearButton.addActionListener(this);
		clearButton.setLocation(0, 100);
		
		generateButton = new JButton("Generate Bodies");
		generateButton.addActionListener(this);
	}
}
