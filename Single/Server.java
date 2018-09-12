import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

public class Server {

   private ServerSocket serverSocket = null;
   private Socket clientSocket = null;
   private PrintWriter out = null;
   private BufferedReader in = null;

   public Server (int port) {
      try {
         serverSocket = new ServerSocket(port);

         System.out.println("Server started");
         System.out.println("Waiting for a client");
         clientSocket = serverSocket.accept();
         System.out.println("Client accepted");

         out = new PrintWriter(clientSocket.getOutputStream(), true);
         in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         
         out.println("Server: This is an echo bot");
         String fromClient = null;
         try {
            while ((fromClient = in.readLine()) != null) {
               if (fromClient.equals("Bye.")) {
                  out.println("Server: Good Day To You");
                  break;
               }
               out.println("Server: " + fromClient);
            }
         }
         catch (Exception e) {
            System.out.println(e);
         }
      }
      catch (Exception e) {
         System.out.println(e);
      }
      try {
         serverSocket.close();
         clientSocket.close();
         out.close();
         in.close();
      }
      catch (Exception e) {
         System.out.println(e);
      } 
   }

   public static void main (String[] args) throws IOException {
      Server server = new Server(9999);
   }
}

