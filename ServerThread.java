import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.text.*;
import java.util.*;

public class ServerThread {
   static Vector<ClientHandler> ar = new Vector<>();
   static int i = 0;

   public static void main (String[] args) throws IOException {
      ServerSocket serverSocket = new ServerSocket(5056);
      System.out.println("Server started");

      Socket clientSocket = null;

      while (true) {
         try {
            System.out.println("Waiting for a client");
            clientSocket = serverSocket.accept();
            System.out.println("Client " + i + " accepted");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out= new PrintWriter(clientSocket.getOutputStream(), true);

            ClientHandler c = new ClientHandler(clientSocket, "client " + i, in, out);
            Thread t = new Thread(c);
            ar.add(c);
            t.start();
            i++;
         }
         catch (Exception e) {
            clientSocket.close();
            System.out.println(e);
         }
      }
   }
}

class ClientHandler implements Runnable {
   private String name;
   final BufferedReader in;
   final PrintWriter out;
   final Socket s;
   boolean isLoggedIn = true;
	
   public ClientHandler(Socket s, String name, BufferedReader in, PrintWriter out) {
      this.s = s;
      this.in = in;
      this.out = out;
      this.name = name;
   }

   @Override
   public void run() {
      String received;

      out.println("Hello. You are " + this.name);

      while (true) {
         try {
            received = in.readLine();
            if (received.equals("logout")) {
               this.isLoggedIn = false;
               this.s.close();
               break;
            }

            StringTokenizer st = new StringTokenizer(received, "#");
            String toSend = st.nextToken();
            String recipient = st.nextToken();

            for (ClientHandler mc : ServerThread.ar) {
               if (mc.name.equals(recipient) && mc.isLoggedIn == true) {
                  mc.out.println(this.name + ": " + toSend);
                  break;
               }
            }
         }
         catch (Exception e) {
			   System.out.println(e);
         }
      }
      try { 
		   this.in.close();
		   this.out.close();
      }
      catch (Exception e) {
         System.out.println(e);
      }
   }
}

