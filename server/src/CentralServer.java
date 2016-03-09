import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Rasheed on 3/5/16.
 */
public class CentralServer implements Runnable {
	private final ServerSocket serverSocket;
	private final ExecutorService pool;

	public CentralServer(int port, int poolSize) throws IOException {
		serverSocket = new ServerSocket(port);
		pool = Executors.newFixedThreadPool(poolSize);

	}
	public static void main(String[] args) {

		try {

			CentralServer centralServer = new CentralServer(4499, 10);
			Thread thread = new Thread(centralServer);
			thread.start();

			System.out.println("Server started");

		}catch(IOException e) {
			System.out.println("Could not start server");
			e.printStackTrace();
		}
	}
	public void run() {

		try {
			while (true) {
				//todo:consider adding DOS attack protection
				pool.execute(new CommsHandler(serverSocket.accept()));
			}
		}catch(IOException ex) {
			pool.shutdown();
		}
	}
}
