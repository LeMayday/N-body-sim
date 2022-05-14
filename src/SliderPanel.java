import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class SliderPanel extends JPanel implements ChangeListener, ActionListener{
	
	private JSlider dtSlider, massSlider;
	private JLabel dtLabel, massLabel, bodiesLabel;
	private JButton clearButton, generateButton;
	private int numRandomBodies = 10000, width = 200, height = 250;
	private CelestialBodies bodies;
	private Space space;
	private double dt, mass;
	private final double SMS;
	
	public SliderPanel(Space s, Dimension fs, CelestialBodies bs, double[] vars, int[] factors) {
		space = s;
		bodies = bs;
		dt = vars[0];
		mass = vars[1];
		SMS = factors[1];
		
		this.setBackground(Color.black);
		this.setSize(new Dimension(width, height));
		Dimension frameSize = fs;
		this.setLocation(frameSize.width - width - 50, frameSize.height - height - 50);
		
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
		
		space.add(this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(dtSlider)){
			dt = Math.pow(10, dtSlider.getValue()/10.0); //maybe make this logarithmic at some point?
			dtLabel.setText("Program Speed: " + Math.round(dt*10.0)/10.0 + " s");
		}
		else if (e.getSource().equals(massSlider)){
			mass = (double)Math.pow(10, massSlider.getValue() - SMS);
			massLabel.setText("New Object Mass Exponent: 1E" + (int)(Math.log10(mass) + SMS));
		}
		space.setVars(dt, mass);
		space.requestFocus(); //resets focus on space to continue listening to key events
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Timer){
			bodiesLabel.setText("Bodies Remaining: " + bodies.size); //updates bodies remaining count every 10 ms
		}
		if (e.getSource() == clearButton){
			bodies.clear();
		}
		if (e.getSource() == generateButton){
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
		space.requestFocus(); //resets focus on space to continue listening to key events
	}
	
	private void sliders() {
		dtSlider = new JSlider(-10, 20, (int)Math.log10(dt)*10);
		dtSlider.setPreferredSize(new Dimension(200,50));
		dtSlider.setBackground(Color.black);
		dtSlider.setForeground(Color.white);
		dtSlider.setPaintTrack(true);
		dtSlider.setMajorTickSpacing(10);
		dtSlider.setPaintLabels(true);
		dtSlider.addChangeListener(this);
		
		int mslb = (int)SMS - 5; // mass slider lower bound
		int msub = (int)SMS + 5; // mass slider upper bound
		massSlider = new JSlider(mslb, msub, (int)SMS);
		massSlider.setPreferredSize(new Dimension(200,50));
		massSlider.setBackground(Color.black);
		massSlider.setForeground(Color.white);
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
