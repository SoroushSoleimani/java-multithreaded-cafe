package cafe.logger;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class LoggerProcess {
    public static void main(String[] args) {
        System.out.println("[Logger Process] Listening on port 27805...");
        
        try (ServerSocket serverSocket = new ServerSocket(27805);
             FileWriter fileWriter = new FileWriter("cafe_system.log", true);
             PrintWriter logFile = new PrintWriter(fileWriter, true)) {

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    
                    String logLine;
                    while ((logLine = in.readLine()) != null) {
                        System.out.println(logLine);
                        logFile.println(logLine);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing log connection: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error initializing Logger Process: " + e.getMessage());
        }
    }
}