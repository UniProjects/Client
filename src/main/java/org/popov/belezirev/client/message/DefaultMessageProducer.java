package org.popov.belezirev.client.message;

import java.io.Closeable;
import java.io.IOException;
import java.util.Scanner;

public class DefaultMessageProducer implements Closeable {

    private Scanner consoleInput;

    public DefaultMessageProducer() {
        consoleInput = new Scanner(System.in);
    }

    public String readMessage() {
        return consoleInput.nextLine();
    }

    @Override
    public void close() throws IOException {
        consoleInput.close();
    }

}
