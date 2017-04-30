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
    private String userName;

    public Client(String hostname, int port, String username) {
        this.hostname = hostname;
        this.port = port;
        this.userName = username;
    }

    public void start() {
        try (Socket clientSocket = new Socket(hostname, port);
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Scanner consoleInput = new Scanner(System.in)) {
            sendMessage(writer, userName);
            Thread writerThread = new Thread(() -> {
                    while (true) {
                        String message = consoleInput.nextLine();
                    sendMessage(writer, message);
                    }
                });
            writerThread.start();
            Thread readerThread = new Thread(() -> {
                while (true) {
                    try {
                        String received = reader.readLine();
                        if (received != null && !received.isEmpty()) {
                            System.out.println(received);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            readerThread.start();
            readerThread.join();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    private void sendMessage(PrintWriter writer, String message) {
        writer.println(message);
        writer.flush();
    }


    public static void main(String[] args) {
        new Client("localhost", 10513, "testUserName").start();
    }

}
