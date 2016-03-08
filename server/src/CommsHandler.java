import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rasheed on 3/5/16.
 */
public class CommsHandler implements Runnable {

	private final Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private String destinationNode;

	//Map to store all client sockets
	private static Map<String, Socket> nodeSockets = new HashMap<String, Socket>();

	public CommsHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		// while(true) {
//todo: change to try with resources
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String node = in.readLine(); //read initial message from client containing node name to add to sockets map.
			System.out.println(node);
			nodeSockets.put(node, this.socket); //add socket with name to map

			String inputLine;
			while((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);

				//todo:lines from client need to be parsed better than this

				/**
				 * message from client is in the following format:
				 * DESTINATION:MESSAGE:LOCALTIME
				 */

				destinationNode = "node" + inputLine.split(":")[0];
				System.out.println(destinationNode);

				out = new PrintWriter(nodeSockets.get(destinationNode).getOutputStream(), true);
				out.println(inputLine); //sending message to destination
			}
		}catch (IOException e) {
			System.out.println("error receiving message");
			e.printStackTrace();
			System.out.println(e);
		}
//            }
	}

}
