import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

// InstructionPanel has the instructions on how to use the simulation

@SuppressWarnings("serial")
public class InstructionPanel extends JPanel{
	
	private int width = 500, height = 225;
	
	// constructor initializes instruction panel and label components
	public InstructionPanel(int[] scales) {
		this.setOpaque(false); // makes panel transparent
		this.setSize(new Dimension(width, height));
		// documentation on GridBagLayouts: https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
		this.setLayout(new GridBagLayout());
		
		instructionLabel(scales[0]);
		keybindLabels();
	}
	
	// initializes label containing instructions
	private void instructionLabel(int SDS) {
		GridBagConstraints il = new GridBagConstraints();
		JLabel instructionLabel = new JLabel("<html><p style=\"font-size:14\">"
											+ "<u>Instructions:</u></p><p style=\"font-size:13\">"
											+ "To place an object, click the left mouse button. "
											+ "To give an object an initial velocity, click, drag, and then release the left mouse button. "
											+ "Each pixel represents 1e" + (int)SDS + " meters.</p></html>");
		instructionLabel.setForeground(Color.white);
		il.fill = GridBagConstraints.HORIZONTAL;
		il.gridx = 0;
		il.gridy = 0;
		il.gridwidth = 2;
		il.weightx = 1.0;
		il.ipady = 20;
		this.add(instructionLabel, il);
	}
	
	// initializes labels containing keybind information
	private void keybindLabels() {
		GridBagConstraints kl1 = new GridBagConstraints();
		JLabel keybindLabel1 = new JLabel("<html><p style=\"font-size:14\">"
										+ "<u>Keybinds:</u></p><p style=\"font-size:13\">"
										+ "W - translate camera up<br>"
										+ "A - translate camera left<br>"
										+ "I - toggle instructions visibility</p></html>");
		keybindLabel1.setForeground(Color.white);
		kl1.fill = GridBagConstraints.HORIZONTAL;
		kl1.gridx = 0;
		kl1.gridy = 1;
		kl1.weightx = 0.5;
		kl1.ipady = 20;
		this.add(keybindLabel1, kl1);
			
		GridBagConstraints kl2 = new GridBagConstraints();
		JLabel keybindLabel2 = new JLabel("<html><p style=\"font-size:13\"><br>"
										+ "S - translate camera down<br>"
										+ "D - translate camera right<br>"
										+ "O - toggle controls visibility</p></html>");
		keybindLabel2.setForeground(Color.white);
		kl2.fill = GridBagConstraints.HORIZONTAL;
		kl2.gridx = 1;
		kl2.gridy = 1;
		kl2.weightx = 0.5;
		this.add(keybindLabel2, kl2);
		
		GridBagConstraints kl3 = new GridBagConstraints();
		JLabel keybindLabel3 = new JLabel("<html><p style=\"font-size:13\">"
										+ "SPACE - pause/resume simulation</p></html>");
		keybindLabel3.setForeground(Color.white);
		kl3.fill = GridBagConstraints.HORIZONTAL;
		kl3.gridx = 0;
		kl3.gridy = 2;
		kl3.gridwidth = 2;
		kl3.weightx = 0.5;
		this.add(keybindLabel3, kl3);
	}
	
}
