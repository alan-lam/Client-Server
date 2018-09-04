import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

public class Server {

   public static void main (String[] args) throws IOException {

      try (
         ServerSocket serverSocket = new ServerSocket(9999);
         Socket clientSocket = serverSocket.accept();
         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      ) {
         out.println("Server: This is an echo bot");
         String fromClient = null;
         while ((fromClient = in.readLine()) != null) {
            if (fromClient.equals("Bye.")) {
               out.println("Server: Good Day To You");
               break;
            }
            out.println("Server: " + fromClient);
         }
      }
   }
}
      
