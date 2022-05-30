import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

// TimePanel displays the simulation time

@SuppressWarnings("serial")
public class TimePanel extends JPanel{
	
	private int width = 200, height = 100;
	private double time = 0;
	private JLabel timeLabel;
	
	public TimePanel(AppFrame af) {
		this.setBackground(Color.black);
		this.setSize(new Dimension(width, height));
		
		timeLabel = new JLabel("Simulation time: " + time + " s");
		timeLabel.setForeground(Color.white);
		this.add(timeLabel);
	}
	
	public void updateLabel(double t) {
		time = t;
		timeLabel.setText("Simulation time: " + Math.round(time*10.0)/10.0 + " s");
	}
	
}
