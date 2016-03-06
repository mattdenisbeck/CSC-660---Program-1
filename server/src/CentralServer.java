import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CentralServer implements Runnable {
	private final ServerSocket serverSocket;  // the server socket
	private final ExecutorService pool;   // ExecutorService object
	private int activeConnections;		//count active clients

	public CentralServer(int port, int poolSize) throws IOException {
		serverSocket = new ServerSocket(port);  // listen at given port
		pool = Executors.newFixedThreadPool(poolSize);  // create N threads in pool
		activeConnections = 0; 							
	}

	public static void main(String[] args) {
		try {
			System.out.println("Starting server...");
			Thread server = new Thread(new CentralServer(4321, 10));
			server.start();
		} catch (IOException e) {
			System.out.println("IOException was caused in the main method of the CentralServer class.");
		}
	}

	public void run() {         // run the service 
		try {
			while (true) {
				//System.out.println("Accepting connections...");
				pool.execute(new CommsHandler(serverSocket.accept()));  // create worker thread
				incrementConnections();
			}
		} catch (IOException ex) {
			pool.shutdown();        // shutdown the ExecutorService thread pool
		}
	}
	
	public synchronized void incrementConnections(){
		activeConnections++;
	}

}
