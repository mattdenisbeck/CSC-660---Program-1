import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CommsHandler implements Runnable {

	private final Socket socket;

	CommsHandler(Socket socket) { 
		this.socket = socket; 
	}
	
	public void run() {
		// read and service request on socket
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println(in.readLine());

		}
		catch (IOException e) {
			System.out.println("IOException caused in the run() method of the CommsHandler class in the server.");
		}
	}
}
