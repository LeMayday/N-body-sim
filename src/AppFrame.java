import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.Timer;

// AppFrame serves as the graphical window and a container class to store all the panels

@SuppressWarnings("serial")
public class AppFrame extends JFrame {

	public Space space;
	public SliderPanel sPanel;
	public InstructionPanel iPanel;
	public TimePanel tPanel;
	private Dimension frameSize;
	
	public AppFrame(CelestialBodies bs, SimCalculation sc, double[] vars, int[] scales) {
		
		// get screen dimensions to size frame appropriately
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // gets display size
		int frameWidth = screenSize.width/8*7;
		int frameHeight = screenSize.height/8*7;
		frameSize = new Dimension(frameWidth, frameHeight);
		
		this.setTitle("N-Body Sim");
		this.setPreferredSize(frameSize); // sets size of window
		this.setLayout(null); // no default layout manager
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		sPanel = new SliderPanel(this, bs, vars, scales);
		sPanel.setLocation(frameSize.width - sPanel.getWidth() - 50, frameSize.height - sPanel.getHeight() - 50);
		
		iPanel = new InstructionPanel(this, scales);
		iPanel.setLocation(50, frameSize.height - iPanel.getHeight() - 75);
		
		tPanel = new TimePanel(this);
		tPanel.setLocation(frameSize.width - tPanel.getWidth() - 50, 50);
		
		space = new Space(this, bs, sc, vars, scales);
		space.setBounds(0, 0, frameSize.width, frameSize.height);
		
		space.add(sPanel);
		space.add(iPanel);
		space.add(tPanel);
		
		this.add(space);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null); // puts frame in center of screen
		this.addComponentListener(new ComponentManager()); // adds component listen to detect resize
		
		Timer timer = new Timer(10, space);
		timer.start();
	}
	
	// for documentation about adapters, see https://docs.oracle.com/javase/tutorial/uiswing/events/generalrules.html
	private class ComponentManager extends ComponentAdapter {
		
		// resets size and location of sub panels to be consistent with size of frame
		public void componentResized(ComponentEvent e) {
			
			frameSize = e.getComponent().getBounds().getSize();
			space.setBounds(0, 0, frameSize.width, frameSize.height);
			sPanel.setLocation(frameSize.width - sPanel.getWidth() - 50, frameSize.height - sPanel.getHeight() - 50);
			iPanel.setLocation(50, frameSize.height - iPanel.getHeight() - 75);
			tPanel.setLocation(frameSize.width - tPanel.getWidth() - 50, 50);
		}
		
	}
	
}
