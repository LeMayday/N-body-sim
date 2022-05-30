import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

// InstructionPanel has the instructions on how to use the simulation

@SuppressWarnings("serial")
public class InstructionPanel extends JPanel{
	
	private int width = 500, height = 100;
	
	public InstructionPanel(AppFrame af, int[] scales) {
		double SDS = scales[0];
		Dimension instructionPanelSize = new Dimension(width, height);
		
		this.setBackground(Color.black);
		this.setSize(instructionPanelSize);
		
		JLabel instructionLabel = new JLabel("<html><p style=\"font-size:14\">Instructions:<br>To place an object, click the left mouse button. "
				+ "To give an object an initial velocity, click, drag, and then release the left mouse button. "
				+ "Each pixel represents 1E" + SDS + " meters. Use WASD to move around.</p> </html>");
		instructionLabel.setForeground(Color.white);
		instructionLabel.setPreferredSize(instructionPanelSize);
		this.add(instructionLabel);
	}
	
}
