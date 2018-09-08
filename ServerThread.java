import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.text.*;
import java.util.*;

public class ServerThread {
   public static void main (String[] args) throws IOException {
      ServerSocket serverSocket = new ServerSocket(5056);
      System.out.println("Server started");

      while (true) {
         Socket clientSocket = null;
         try {
            System.out.println("Waiting for a client");
            clientSocket = serverSocket.accept();
            System.out.println("Client accepted");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out= new PrintWriter(clientSocket.getOutputStream(), true);

            Thread t = new ClientHandler(clientSocket, in, out);
            t.start();
         }
         catch (Exception e) {
            clientSocket.close();
            System.out.println(e);
         }
      }
   }
}

class ClientHandler extends Thread {
   DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
   DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
   final BufferedReader in;
   final PrintWriter out;
   final Socket s;
	
   public ClientHandler(Socket s, BufferedReader in, PrintWriter out) {
      this.s = s;
      this.in = in;
      this.out = out;
   }

   @Override
   public void run() {
      String received;
      String toSend;
      while (true) {
         try {
            out.println("What do you want?[Date | Time]");
            received = in.readLine();
            if (received.equals("Exit")) {
               this.s.close();
               break;
            }

            Date date = new Date();

            if (received.equals("Date")) {
               toSend = fordate.format(date);
               out.println(toSend);
            }
            else if (received.equals("Time")) {
               toSend = fortime.format(date);
               out.println(toSend);
            }
            else {
               out.println("Invalid input");
            }
         }
         catch (IOException e) {
			   System.out.println(e);
         }
      }
      try { 
		   this.in.close();
		   this.out.close();
      }
      catch (IOException e) {
         System.out.println(e);
      }
   }
}

