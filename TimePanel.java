import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

// TimePanel displays the simulation time

//@SuppressWarnings("serial")
public class TimePanel extends JPanel{

    private double time = 0;
	private final JLabel timeLabel;
	private final JLabel pausedLabel;
	
	// constructor initializes time panel and label components
	public TimePanel() {
        int width = 200;
        int height = 50;
        this.setSize(new Dimension(width, height));
		this.setOpaque(false);
		
		timeLabel = new JLabel("Simulation time: " + time + " s");
		timeLabel.setForeground(Color.white);
		this.add(timeLabel);
		
		// only shown when simulation is paused (not by default)
		pausedLabel = new JLabel("Simulation Paused");
		pausedLabel.setForeground(Color.white);
	}
	
	// updates time label (called from Space)
	public void updateLabel(double t) {
		time = t;
		timeLabel.setText("Simulation time: " + Math.round(time*10.0)/10.0 + " s");
	}
	
	// toggles pausedLabel visibility (called from AppFrame)
	public void togglePaused() {
		if (pausedLabel.getParent() == null) {
			this.add(pausedLabel);
			return;
		}
		this.remove(pausedLabel);
	}
	
}
