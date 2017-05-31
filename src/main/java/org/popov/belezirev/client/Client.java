package org.popov.belezirev.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.popov.belezirev.client.message.DefaultMessageProducer;

public class Client {
	private static final String CONSOLE_CLIENT_DEFAULT_TYPE = "console";
	private String hostname;
	private int port;
	private String userName;
	private DefaultMessageProducer messageProducer;

	public Client(String hostname, int port, String username) {
		this(hostname, port, username, new DefaultMessageProducer());
	}

	public Client(String hostname, int port, String username, DefaultMessageProducer messageProducer) {
		this.hostname = hostname;
		this.port = port;
		this.userName = username;
		this.messageProducer = messageProducer;
	}

	public void start() throws IOException {
		try (Socket clientSocket = new Socket(hostname, port);
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
			sendMessage(writer, userName);
			sendMessage(writer, CONSOLE_CLIENT_DEFAULT_TYPE);
			Thread writerThread = getWriterThread(writer);
			writerThread.start();
			Thread readerThread = getReaderThread(reader);
			readerThread.start();
			readerThread.join();

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} finally {
			messageProducer.close();
		}
	}

	private Thread getReaderThread(BufferedReader reader) {
		Thread readerThread = new Thread(() -> {
			while (true) {
				try {
					String received = reader.readLine();
					processReceivedMessage(received);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return readerThread;
	}

	protected void processReceivedMessage(String receivedMessage) {
		if (receivedMessage != null && !receivedMessage.isEmpty()) {
			System.out.println(receivedMessage);
		}
	}

	private Thread getWriterThread(PrintWriter writer) {
		Thread writerThread = new Thread(() -> {
			while (true) {
				String message = getMessage();
				if (message != null) {
					sendMessage(writer, message);
				}
			}
		});
		return writerThread;
	}

	protected String getMessage() {
		String readMessage = messageProducer.readMessage();
		if (readMessage != null && !readMessage.isEmpty()) {
			return readMessage;
		}
		return null;
	}

	private void sendMessage(PrintWriter writer, String message) {
		writer.println(message);
		writer.flush();
	}

	public static void main(String[] args) throws IOException {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Please enter your username: ");
			String username = scanner.nextLine();
			new Client("localhost", 10513, username).start();
		}
	}

}
