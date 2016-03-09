import java.io.*;
import java.net.Socket;

/**
 * Created by Rasheed on 3/6/16.
 */
public class Node implements Runnable {

	private int nodeId;
	private static final String PATH = System.getenv("PROJECT1_FILES");
	private int lamportLogicalClock;

	public Node(int nodeId) {
		this.nodeId = nodeId;
	}

	public static void main(String[] args) {

		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(2000);
				Thread thread = new Thread(new Node(i));
				thread.start();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void run() {
		String line;
		//had problems getting environment variable to work, so I'm leaving it till later.
		String fileName = "/Users/Rasheed/Google Drive/NKU/CSC660/CSC-660---Program-1/client/input-files/" + nodeId + "input.txt";
		//System.out.println(PATH);
//        String fileName = PATH + nodeId + "input.txt";
		try (
				Socket nodeSocket = new Socket("localhost", 4499);
				PrintWriter out = new PrintWriter(nodeSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(nodeSocket.getInputStream()));
				BufferedReader br = new BufferedReader(new FileReader(fileName))
		) {

			out.println("node" + nodeId);
			Thread.sleep(30000);
			while ((line = br.readLine()) != null) {

				String[] lineAry = line.split("\"");

				if (lineAry.length > 1) {
					//message found
					int destinationNode = Integer.parseInt(lineAry[0].trim());
					String message = lineAry[1];
					System.out.println("PROCESS #" + nodeId +" Sending message " + message + " to PROCESS #" + destinationNode + " Local time = " + this.lamportLogicalClock);
					out.println(destinationNode+":"+message+":"+this.lamportLogicalClock);
					//System.out.println(in.readLine());

				}
				else {
					//no message
					int number = Integer.parseInt(line.trim());
					this.lamportLogicalClock += number;
					System.out.println("PROCESS #" + nodeId + " Sleeping for " + number * 1000 + " seconds Localtime = " + this.lamportLogicalClock);
					Thread.sleep(number);
				}
			}

			String incommingMessage;
			while ((incommingMessage = in.readLine()) != null ) {
//				System.out.println(incommingMessage);
//				System.out.println(incommingMessage.split(":")[2]);
				this.lamportLogicalClock += Math.max(Integer.parseInt(incommingMessage.split(":")[2]) + 1, this.lamportLogicalClock + 1);
				String sourceNode = "";
				System.out.println("RECEIVED MESSAGE: " + incommingMessage);
//				System.out.println("PROCESS #" + nodeId + " Sleeping for " + number * 1000 + " seconds Localtime = " + this.lamportLogicalClock);
//				System.out.println("Clock: " + lamportLogicalClock);
			}

//			System.out.println("Clock: " + lamportLogicalClock);

		} catch (IOException e) {
			e.printStackTrace();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
