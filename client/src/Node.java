package src;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Rasheed on 3/6/16.
 * Changes by Matthew Beck 3/9/16
 * This class represents the client nodes and handles sending messages to the server.
 */
public class Node implements Runnable {

	private static final String PATH = System.getProperty("user.dir") + "/client/input-files/";
	public static final int NUMBER_OF_NODES = 10;
	protected int nodeId;
	protected LamportClock lamportLogicalClock = new LamportClock();

	public Node() {}
	
	public Node(int nodeId) {
		this.nodeId = nodeId;
	}

	public static void main(String[] args) {
		//create thread pool and start the 10 nodes
		ExecutorService pool = Executors.newFixedThreadPool(NUMBER_OF_NODES);
		for(int i = 0; i < NUMBER_OF_NODES; i++){
			pool.execute(new Node(i));
		}
		pool.shutdown(); //shutdown pool after nodes terminate
	}

	@Override
	public void run() {
		String line;
		String fileName = PATH + nodeId + "input.txt";
		
		try (
				Socket nodeSocket = new Socket("localhost", 4499);
				PrintWriter out = new PrintWriter(nodeSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(nodeSocket.getInputStream()));
				BufferedReader br = new BufferedReader(new FileReader(fileName))
		) {
			//start message handler thread for reading incoming messages
			Thread handler = new Thread(new MessageHandler(in, nodeId, lamportLogicalClock));
			handler.start();

			//send initial identifier message to Server
			out.println(nodeId);
			Thread.sleep(2000);  //pause for other nodes to connect
			
			//read through input file
			while ((line = br.readLine()) != null) {

				String[] lineAry = line.split("\"");

				if (lineAry.length > 1) {
					//message found
					int destinationNode = Integer.parseInt(lineAry[0].trim());
					String message = lineAry[1];
					System.out.println("PROCESS #" + nodeId +" Sending message '" + message + "' to PROCESS #" + destinationNode + " Local time = " + lamportLogicalClock);
					out.println(nodeId + ":" + destinationNode + ":" + message + ":" + lamportLogicalClock);
				}
				
				else {
					//no message
					int number = Integer.parseInt(line.trim());
					lamportLogicalClock.sleepClock(number);
					
					System.out.println("PROCESS #" + nodeId + " Sleeping for " + number + " miliseconds, Localtime = " + lamportLogicalClock);
					Thread.sleep(number);
				}
			}
			out.println(nodeId + ":" + -1 + ":FINISHED:"+ lamportLogicalClock);
			handler.join();
		} catch (IOException e) {
			e.printStackTrace();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
