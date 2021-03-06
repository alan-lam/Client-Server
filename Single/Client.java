import java.net.*;
import java.io.*;

public class Client {

   private Socket socket;
   private PrintWriter out;
   private BufferedReader in;
   private BufferedReader sys_in;

   public Client (String address, int port) {
      try {
         socket = new Socket(address, port);
         System.out.println("Connected");
         out = new PrintWriter(socket.getOutputStream(), true);
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         sys_in = new BufferedReader(new InputStreamReader(System.in));
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      String fromServer;
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
         e.printStackTrace();
      }
      try {
         socket.close();
         out.close();
         in.close();
         sys_in.close();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void main (String[] args) throws IOException {
      Client client = new Client("192.168.1.14", 9563);
   }
}

