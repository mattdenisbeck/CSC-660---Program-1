import java.io.*;
import java.net.Socket;

public class Node implements Runnable {
	private static final String HOST_NAME = "localhost";
	private static final int PORT_NUMBER = 4321;
	private int nodeId;

	public Node(int nodeId) {
		this.nodeId = nodeId;

	}

	@Override
	public void run() {
		Socket nodeSocket;
		PrintWriter out;
		String line = null;
		
		try {
			nodeSocket = new Socket(HOST_NAME, PORT_NUMBER);
			System.out.println("Node number " + nodeId + " is now connected.");
			out = new PrintWriter(nodeSocket.getOutputStream(), true);

			String nodeInputFile = nodeId + "input.txt";
			/*I could not get the relative path to the text files to work. Please update the exact path to the local path of your file names.*/
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Gaurav\\Desktop\\CSC 660\\CSC-660---Program-1\\client\\input-files\\" + nodeInputFile));
			//BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Gaurav\\Desktop\\CSC 660\\CSC-660---Program-1\\client\\input-files\\0input.txt"));

			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				if (line.contains("\"")) { //this is the message line
					int receivingNode = Character.getNumericValue(line.charAt(0)); //gives us the nodeId of the receiving node.
					out.println(line);

				}
				else {
					Thread.sleep(Integer.parseInt(line));
				}
			}

		}
		catch (IOException e) {
			System.out.println("IOException was caused in the run() method of the Node class.");
		}
		catch (InterruptedException e) {
			System.out.println("InterruptedException caused from the Thread.sleep execution");
		}
	}
}