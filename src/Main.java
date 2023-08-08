import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	
	public static void main(String[] args) {
		// initialize main components
		Simulation sim = new Simulation();
		sim.start();
		//Main main = new Main();
		//main.startThreads(sim);
	}
	
	private void startThreads(Simulation sim) {
		ExecutorService service = Executors.newSingleThreadExecutor();
		
		WorkTask task = new WorkTask(sim);
		
		while (sim.isRunning()) {
			service.submit(task);
		}
		
	}

}
