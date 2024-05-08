import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

// TimePanel displays the simulation time

@SuppressWarnings("serial")
public class DataPanel extends JPanel{
	
	private int width = 200, height = 50;
	private double time = 0;
	private Simulation sim;
	private JLabel timeLabel;
	private JLabel bodiesLabel;
	private JLabel pausedLabel;
	
	// constructor initializes time panel and label components
	public DataPanel(Simulation s) {
		this.setSize(new Dimension(width, height));
		this.setOpaque(false);
		
		timeLabel = new JLabel("Simulation time: " + s.iters*s.dt + " s");
		timeLabel.setForeground(Color.white);
		this.add(timeLabel);
		
		bodiesLabel = new JLabel("Bodies: " + s.bodies.size);
		bodiesLabel.setForeground(Color.white);
		this.add(bodiesLabel);
		
		// only shown when simulation is paused (not by default)
		pausedLabel = new JLabel("Simulation Paused");
		pausedLabel.setForeground(Color.white);
	
	}
	
	// updates time label (called from Space)
	public void updateTime(double t) {
		time = t;
		timeLabel.setText("Simulation time: " + Math.round(time*10.0)/10.0 + " s");
	}
	
	// toggles pausedLabel visibility (called from AppFrame)
	public void togglePaused(boolean simRunning) {
		if(!simRunning) {
			this.add(pausedLabel);
			simRunning = false;
		}
		else {
			this.remove(pausedLabel);
			simRunning = true;
		}
	}
	
}
