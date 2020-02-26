package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 * Created by codecadet on 25/02/2020.
 */
public class ClientSocket implements Runnable{

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private String notFound = "resources/www/notFound.html";

    public ClientSocket(ServerSocket serverSocket, Socket clientSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
    receive();
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String sentence = in.readLine();
            if (sentence != null) {
                String[] request = sentence.split(" ");

                System.out.println(sentence);

                if (request[0].equals("GET")) {
                    if (request[1].equals("/")) {
                        sendError(notFound);
                        in.close();
                    } else {
                        String path = "resources/www" + request[1];
                        sendHeader(path);
                        in.close();
                    }
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getHeader(String path) {
        String headerReturn;
        if (path.contains("notFound")) {
            headerReturn = "HTTP/1.0 404 Not Found\r\n";
        } else {
            headerReturn = "HTTP/1.0 200 Document Follows\r\n";
        }
        return headerReturn;
    }

    private String getFileType(String path) throws IOException {
        String type;
        if (path.contains("videogato.mp4")) {
            type = "video/mp4";
        } else {
            type = new URL("file://" + path).openConnection().getContentType();
        }
        return type;
    }

    private  void sendHeader(String path) throws IOException {
        try {
            File file = new File(path);
            FileInputStream readFile = new FileInputStream(file);

            String header = getHeader(getFileType(path)) +
                    "Content-Type: " + getFileType(path) + "\r\n" +
                    "Content-Lenght " + file.length() + " \r\n" +
                    "\r\n";

            OutputStream out = clientSocket.getOutputStream();

            out.write(header.getBytes());
            out.flush();

            sendFile(readFile, out);

            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            sendError(notFound);
        }
    }

    private void sendFile(FileInputStream readFile, OutputStream out) throws IOException {
        int finish = 0;
        byte[] buffer = new byte[1024];
        BufferedInputStream lineFile = new BufferedInputStream(readFile);

        while ((finish = lineFile.read(buffer)) > 0) {
            out.write(buffer, 0, buffer.length);
            out.flush();

        }
    }

    private void sendError(String path) throws IOException {
        File file = new File(path);
        FileInputStream readFile = new FileInputStream(file);


        String header = getHeader(getFileType(path)) +
                "Content-Type: " + getFileType(path) + "\r\n" +
                "Content-Lenght " + file.length() + " \r\n" +
                "\r\n";

        OutputStream out = clientSocket.getOutputStream();

        out.write(header.getBytes());
        out.flush();

        int finish = 0;
        byte[] buffer = new byte[1024];
        BufferedInputStream lineFile = new BufferedInputStream(readFile);

        while ((finish = lineFile.read(buffer)) > 0) {
            out.write(buffer, 0, buffer.length);
            out.flush();
            out.close();
        }

    }
}
