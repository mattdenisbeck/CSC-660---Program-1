package edu.csc660.prog1.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Node implements Runnable {
	private String hostname = "localhost";
	private int portNumber = 4321;

	@Override
	public void run() {
		Socket nodeSocket;
		PrintWriter out;
		try {
			nodeSocket = new Socket(hostname, 4321);
			System.out.println("Node connected");
			out = new PrintWriter(nodeSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(nodeSocket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
