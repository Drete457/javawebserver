package Server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {

    private static int portNumber = 0;
    private static Scanner listen = new Scanner(System.in);
    private static ServerSocket serverSocket;
    private static Socket clientSocket;

    public static void main(String[] args) {

        System.out.print("What port I will listen? ");
        portNumber = listen.nextInt();

        try {
            serverSocket = new ServerSocket(portNumber);
           while (true) {
                clientSocket = serverSocket.accept();
                receive();

           }
        } catch (IOException e) { e.getMessage(); }
    }

    private static void receive() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String sentence = new String(in.readLine());
            String[] request = sentence.split(" ");


            System.out.println(sentence);

            if (request[0].equals("GET")) {

                if (request[1].equals("/index.html")) {
                    String send = "HTTP/1.0 200 Document Follows\\r\\n" + "Content-Type: text/html; charset=UTF-8\r\n" + "Content-Length: 203 \r\n" + "\r\n";
                    String path = "/Users/codecadet/Desktop/Exer/IDE/Webserver/src/Server/index.html";
                    send(path, send);
                    in.close();
                } else if (request[1].equals("/gato.jpg")){
                    String send = "HTTP/1.0 200 Document Follows\\r\\n" + "Content-Type: image/gato.jpg\n" + "Content-Length: 53873 \r\n" + "\r\n";
                    String path = "/Users/codecadet/Desktop/Exer/IDE/Webserver/src/Server/gato.jpg";
                    send(path, send);
                    in.close();
                }
                else {
                    String send = "HTTP/1.0 404 Not Found\r\n" + "Content-Type: text/html; charset=UTF-8\r\n" + "Content-Length: 160 \r\n" + "\r\n";
                    String path = "/Users/codecadet/Desktop/Exer/IDE/Webserver/src/Server/notFound.html";
                    send(path, send);
                    in.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void send(String path, String send) {
        try {
            File file = new File(path);
            FileInputStream readFile = new FileInputStream(file);
            BufferedInputStream lineFile = new BufferedInputStream(readFile);

            int finish = 0;
            byte[] buffer = new byte[1024];

            OutputStream out = clientSocket.getOutputStream();


            out.write(send.getBytes());
            out.flush();
            System.out.println("sending file");
            while ((finish = lineFile.read(buffer)) > 0){
                out.write(buffer, 0, buffer.length);
                out.flush();

            }
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }
}

