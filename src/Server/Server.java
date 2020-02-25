package Server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {

    private static Socket clientSocket;
    private static String notFound = "resources/www/notFound.html";

    public static void main(String[] args) {

        ServerSocket serverSocket;

        Scanner listen = new Scanner(System.in);
        int portNumber = 0;

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

            String sentence = in.readLine();
            if (sentence != null) {
                String[] request = sentence.split(" ");

                System.out.println(sentence);

                if (request[0].equals("GET")) {
                    if (request[1].equals("/")) {
                        sendHeader(notFound);
                        in.close();
                    } else {
                        String path = "resources/www" + request[1];
                        sendHeader(path);
                        in.close();
                    }
                }
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private static String getHeader(String path){
        String headerReturn;
        if (path.contains("notFound")){
            headerReturn = "HTTP/1.0 404 Not Found\r\n"; }
        else {
            headerReturn = "HTTP/1.0 200 Document Follows\r\n"; }
    return headerReturn;
    }

    private static String getFileType(String path) throws IOException {
        String type;
        if(path.contains("videogato.mp4")) {
            type = "video/mp4";
        } else {
            type = new URL("file://" + path).openConnection().getContentType();
        }
        return type;
    }

    private static void sendHeader(String path) {
        try {
            File file = new File(path);
            FileInputStream readFile = new FileInputStream(file);


            String header = getHeader(getFileType(path)) +
            "Content-Type: " + getFileType(path) + "\r\n" +
            "Content-Lenght " + file.length() + " \r\n" +
            "\r\n";

            System.out.println(header);

            OutputStream out = clientSocket.getOutputStream();

            out.write(header.getBytes());
            out.flush();

            sendFile( readFile, out);

            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private static void sendFile(FileInputStream readFile, OutputStream out) throws IOException {
        int finish = 0;
        byte[] buffer = new byte[1024];
        BufferedInputStream lineFile = new BufferedInputStream(readFile);

        while ((finish = lineFile.read(buffer)) > 0){
            out.write(buffer, 0, buffer.length);
            out.flush();

        }
    }
}

