package edu.csc660.prog1.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(10);
		for(int i = 0; i < 10; i++){
			pool.execute(new Node());
		}

	}

}
