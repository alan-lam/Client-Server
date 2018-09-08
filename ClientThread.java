import java.net.Socket;
import java.io.*;

public class ClientThread {
   
   public static void main (String[] args) throws IOException {
      try {
         Socket s = new Socket("localhost", 5056);
         BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
         BufferedReader sys_in = new BufferedReader(new InputStreamReader(System.in));
         PrintWriter out = new PrintWriter(s.getOutputStream(), true);

         while (true) {
            System.out.println(in.readLine());
            String toSend = sys_in.readLine();
            out.println(toSend);
            if (toSend.equals("Exit")) {
               s.close();
               break;
            }
            String received = in.readLine();
            System.out.println(received);
         }

         in.close();
         sys_in.close();
         out.close();
      }
      catch (Exception e) {
         System.out.println(e);
      }
   }
}

