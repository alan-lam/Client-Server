import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

public class Client {

   public static void main (String[] args) throws IOException {

      try (
         Socket socket = new Socket("localhost", 9999);
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         BufferedReader sys_in = new BufferedReader(new InputStreamReader(System.in));
      ) {
         String fromServer = null;
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
   }
}

