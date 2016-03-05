package edu.csc660.prog1.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
	
	public static final int NUMBER_OF_NODES = 10;

	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(NUMBER_OF_NODES);
		for(int i = 0; i < NUMBER_OF_NODES; i++){
			pool.execute(new Node());
		}
	}
}
