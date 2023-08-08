import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;

// AppFrame serves as the graphical window and a container class to store all the panels

@SuppressWarnings("serial")
public class AppFrame extends JFrame {

	private Space space;
	//private SliderPanel sPanel;
	//private InstructionPanel iPanel;
	//private TimePanel tPanel;
	private Dimension frameSize;
	
	// constructor initializes window and sub components and starts simulation timer
	public AppFrame(Simulation s) {
		
		// get screen dimensions to size frame appropriately
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // gets display size
		int frameWidth = screenSize.width/8*7;
		int frameHeight = screenSize.height/8*7;
		frameSize = new Dimension(frameWidth, frameHeight);
		
		this.setTitle("N-Body Sim");
		this.setPreferredSize(frameSize); // sets size of window
		this.setLayout(null); // no default layout manager
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		space = s.space;
		space.setBounds(0, 0, frameSize.width, frameSize.height);
		
		this.add(space);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null); // puts frame in center of screen
		this.addComponentListener(new ComponentManager()); // adds component listen to detect resize
	}
	
	// for documentation about adapters, see https://docs.oracle.com/javase/tutorial/uiswing/events/generalrules.html
	private class ComponentManager extends ComponentAdapter {
		
		// resets size and location of sub panels to be consistent with size of frame
		public void componentResized(ComponentEvent e) {
			frameSize = e.getComponent().getBounds().getSize();
			space.setBounds(0, 0, frameSize.width, frameSize.height);
		}
	}
	
}
