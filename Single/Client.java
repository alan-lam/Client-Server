import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

public class Client {

   private Socket socket = null;
   private PrintWriter out = null;
   private BufferedReader in = null;
   private BufferedReader sys_in = null;

   public Client (String address, int port) {
      try {
         socket = new Socket(address, port);
         System.out.println("Connected");
         out = new PrintWriter(socket.getOutputStream(), true);
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         sys_in = new BufferedReader(new InputStreamReader(System.in));
      }
      catch (Exception e) {
         System.out.println(e);
      }

      String fromServer = null;
      try {
         while ((fromServer = in.readLine()) != null) {
            System.out.println(fromServer);
            if (fromServer.equals("Server: Good Day To You")) {
               break;
            }
            System.out.print("Client: ");
            String input = sys_in.readLine();
            out.println(input);
         }
      }
      catch (Exception e) {
         System.out.println(e);
      }
      try {
         socket.close();
         out.close();
         in.close();
         sys_in.close();
      }
      catch (Exception e) {
         System.out.println(e);
      }
   }

   public static void main (String[] args) throws IOException {
      Client client = new Client("localhost", 9999);
   }
}

