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
					out.println(destinationNode+":"+message+":"+lamportLogicalClock);
					System.out.println(in.readLine());

				}
				else {
					//no message
					int number = Integer.parseInt(line.trim());
					lamportLogicalClock += number;
					System.out.println("Simulating sequential instructions for " + number);
					Thread.sleep(number);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
