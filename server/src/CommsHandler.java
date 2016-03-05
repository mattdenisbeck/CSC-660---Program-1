import java.net.Socket;

public class CommsHandler implements Runnable {

	private final Socket socket;

	CommsHandler(Socket socket) { 
		this.socket = socket; 
	}
	
	public void run() {
		// read and service request on socket
	}
}
