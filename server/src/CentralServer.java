package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Rasheed on 3/5/16.
 */
public class CentralServer {
	private final ServerSocket serverSocket;
	private final ExecutorService pool;

	public CentralServer(int port, int poolSize) throws IOException {
		serverSocket = new ServerSocket(port);
		pool = Executors.newFixedThreadPool(poolSize);
	}
	
	public static void main(String[] args) {
		CentralServer centralServer;
		try {
			centralServer = new CentralServer(4499, 10);
			System.out.println("Server started");
			for(int i = 0; i < 10; i++){
				centralServer.pool.execute(new CommsHandler(centralServer.serverSocket.accept()));
			}
			centralServer.pool.shutdown(); //shutdown pool after all messages are delivered

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
