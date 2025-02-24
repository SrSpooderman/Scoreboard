package proyect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ScoreServer implements Runnable {
    private final HighScores highScores;
    private final int port;

    public ScoreServer(){
        this.highScores = new HighScores();
        this.port = 1234;
    }

    @Override
    public void run() {
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket;
            BufferedReader in;
            PrintWriter out;

            while (true){
                System.out.println("Esperando cliente...");
                clientSocket = serverSocket.accept();
                System.out.println("Conectando..."+ clientSocket.getInetAddress().getHostAddress());

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                processClient(in,out);

                //Close
                clientSocket.close();
                System.out.println("Cerrando cliente...");
                highScores.saveScore();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processClient(BufferedReader in, PrintWriter out){
        String line;
        boolean done  = false;
        try{
            while (!done){
                if ((line = in.readLine())== null){
                    done = true;
                }else {
                    System.out.println("Client msg: "+ line);
                    if (line.trim().equals("bye")){
                        done = true;
                    } else{
                        doRequest (line,out);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void doRequest(String line, PrintWriter out){
        if (line.trim().toLowerCase().equals("get")){
            System.out.println("Processing 'get'");
            out.println(highScores.toString());
        } else if ((line.length() >= 6) && (line.substring(0,5).toLowerCase().equals("score"))) {
            System.out.println("Processing 'score'");
            highScores.addScore(line.substring(5).trim());
        } else {
            System.out.println("Invalid request, ignoring request");
        }
    }
}
