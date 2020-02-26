package Server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {

        ServerSocket serverSocket;

        Scanner listen = new Scanner(System.in);
        int portNumber = 0;

        System.out.print("What port I will listen? ");
        portNumber = listen.nextInt();

        try {
            serverSocket = new ServerSocket(portNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread client = new Thread(new ClientSocket(serverSocket, clientSocket));
                client.start();

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


}

