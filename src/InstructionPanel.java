import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InstructionPanel extends JPanel{
	
	private int width = 500, height = 100;
	public Dimension instructionPanelSize = new Dimension(width, height);
	
	public InstructionPanel(Space s, int[] scales) {
		double SDS = scales[0];
		
		this.setBackground(Color.black);
		this.setSize(instructionPanelSize);
		Dimension frameSize = s.getSize();
		this.setLocation(50, frameSize.height - height - 75);
		
		JLabel instructionLabel = new JLabel("<html><p style=\"font-size:14\">Instructions:<br>To place an object, click the left mouse button. "
				+ "To give an object an initial velocity, click, drag, and then release the left mouse button. "
				+ "Each pixel represents 1E" + SDS + " meters. Use WASD to move around.</p> </html>");
		instructionLabel.setForeground(Color.white);
		instructionLabel.setPreferredSize(instructionPanelSize);
		this.add(instructionLabel);
		
		s.add(this);
	}
	
}
