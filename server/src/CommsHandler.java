package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rasheed on 3/5/16.
 * Edited by Matthew Beck on 3/9/16
 * This class handles communications to/from the client nodes
 */
public class CommsHandler implements Runnable {

	private final Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private int sourceNode, destinationNode, localtime;
	private String message;

	//Map to store all client sockets
	private static Map<Integer, Socket> nodeSockets = new HashMap<Integer, Socket>();
	private static int activeConnections = 0;

	public CommsHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			Integer node = Integer.parseInt(in.readLine()); //read initial message from client containing node name to add to sockets map.
			System.out.println(node + " connected");
			nodeSockets.put(node, this.socket); //add socket with name to map
			incrementConnections();
			System.out.println("Active Connections(after increment) = " + activeConnections);

			String inputLine;
			while((inputLine = in.readLine()) != null) {
				System.out.println("SERVER received: " + inputLine);

				/**
				 * message from client is in the following format:
				 * SOURCE:DESTINATION:MESSAGE:LOCALTIME
				 */
				//parse message from nodes
				sourceNode = Integer.parseInt(inputLine.split(":")[0]); 
				destinationNode = Integer.parseInt(inputLine.split(":")[1]);
				message = inputLine.split(":")[2];
				localtime = Integer.parseInt(inputLine.split(":")[3]);
				
				//a "FINISHED" message means the client has no more instructions to carry out
				if (message.equals("FINISHED")){
					decrementConnections();
					System.out.println("Active Connections(after decrement) = " + activeConnections);
					
					//if no more active connections, send message telling all clients to terminate
					if(activeConnections == 0){
						for(int i =0; i < 10; i++){
							out = new PrintWriter(nodeSockets.get(i).getOutputStream(), true);
							out.println("TERMINATE:" + -1 + ":" + localtime); //sending terminate message to nodes
						}
					}
				}
				else {
					out = new PrintWriter(nodeSockets.get(destinationNode).getOutputStream(), true);
					out.println(message + ":" + sourceNode + ":" + localtime); //sending message to destination
				}
			}
		}catch (IOException e) {
			System.out.println("error receiving message");
			e.printStackTrace();
		}
	}

	private static synchronized void decrementConnections() {
		activeConnections--;
	}

	private static synchronized void incrementConnections() {
		activeConnections++;
	}

}
