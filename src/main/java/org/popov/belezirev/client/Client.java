package org.popov.belezirev.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private String hostname;
	private int port;
	private String username;

	public Client(String hostname, int port, String username) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
	}

	public void start() {
		try (Socket clientSocket = new Socket(hostname, port);
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				Scanner consoleInput = new Scanner(System.in)) {
			while (true) {
				String message = consoleInput.nextLine();
				writer.println(message);
				writer.flush();
				String received = reader.readLine();
				if (received != null && !received.isEmpty()) {
					System.out.println(received);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Client("localhost", 10513, "").start();
	}

}
