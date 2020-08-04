package Server;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) {

        ServerSocket serverSocket;

        Scanner listen = new Scanner(System.in);
        int portNumber = 0;

        System.out.print("What port I will listen? ");
        portNumber = listen.nextInt();

        try {
            serverSocket = new ServerSocket(portNumber);
            ExecutorService connections = Executors.newFixedThreadPool(500);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                connections.submit(new ClientSocket(serverSocket, clientSocket));
                //client.start();

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


}

