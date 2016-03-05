package edu.csc660.prog1.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Node implements Runnable {
	private static final String HOST_NAME = "localhost";
	private static final int PORT_NUMBER = 4321;

	@Override
	public void run() {
		Socket nodeSocket;
		PrintWriter out;
		
		try {
			nodeSocket = new Socket(HOST_NAME, PORT_NUMBER);
			System.out.println("Node connected");
			out = new PrintWriter(nodeSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(nodeSocket.getInputStream()));
		}
		catch (IOException e) {
			System.out.println("IOException was caused in the run() method of the Node class.");
		}
	}
}
