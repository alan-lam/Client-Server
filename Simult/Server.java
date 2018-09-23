import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Server {
   static ArrayList<ClientHandler> ar = new ArrayList<>();
   static CountDownLatch x = new CountDownLatch(1);

   public static void main (String[] args) throws IOException {
     InetAddress ip = null;
      try {
         ip = InetAddress.getLocalHost();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      System.out.println("Server started. IP Address: " + ip.getHostAddress());

      startServer();
   }

   public static void startServer() throws IOException {
      final ServerSocket ss = new ServerSocket(9563);
      Socket this_s = new Socket("localhost", 9563);
      BufferedReader this_in = new BufferedReader(new InputStreamReader(System.in));
      PrintWriter this_out = new PrintWriter(System.out, true);

      ClientHandler c = new ClientHandler(this_s, this_in, this_out, x);
      Thread t = new Thread(c);
      ar.add(c);
      t.start();

      try {
         x.await();
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      (new Thread() {
         @Override
         public void run() {

            Socket s;
            while (true) {
               try {
                  s = ss.accept();
                  BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                  PrintWriter out = new PrintWriter(s.getOutputStream(), true);

                  ClientHandler c = new ClientHandler(s, in, out);
                  Thread t = new Thread(c);
                  ar.add(c);
                  t.start();
               }
               catch (Exception e) {
                  e.printStackTrace();
               }
            }
         }
      }).start();
   }
}

class ClientHandler implements Runnable {
   private String name;
   final BufferedReader in;
   final PrintWriter out;
   final Socket s;
   boolean isLoggedIn = true;
   CountDownLatch x;

   public ClientHandler (Socket s, BufferedReader in, PrintWriter out) {
      this.s = s;
      this.in = in;
      this.out = out;
   }

   public ClientHandler (Socket s, BufferedReader in, PrintWriter out, CountDownLatch x) {
      this.s = s;
      this.in = in;
      this.out = out;
      this.x = x;
   }

   private void setName(String name) {
      this.name = name;
   }

   @Override
   public void run() {
      out.println("$:Hello. Enter your name: ");
      try {
         String inputName = in.readLine();
         this.setName(inputName);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      out.println("$:Great! Your name is " + this.name);

      if (x != null) {
         x.countDown();
      }

      String received;
      while (true) {
         try {
            received = in.readLine();
            if (received == null) {
               this.isLoggedIn = false;
               break;
            }
            else if (received.equals("#logout")) {
               this.isLoggedIn = false;
               this.s.close();
               break;
            }
            else if (received.equals("#active")) {
               int i = 1;
               for (ClientHandler mc : Server.ar) {
                  if (mc.name == null) {
                     mc.isLoggedIn = false;
                  }
                  if (mc.isLoggedIn == true) {
                     out.println(i + ": " + mc.name);
                     i++;
                  }
               }
            }
            else if (received.startsWith("$:")) {
               out.println(received);
            }
            else {
               StringTokenizer st = new StringTokenizer(received, "@");
               String toSend = st.nextToken();
               String recipient = st.nextToken();

               for (ClientHandler mc : Server.ar) {
                  if (mc.name == null) {
                     mc.isLoggedIn = false;
                     continue;
                  }
                  if (mc.name.equals(recipient) && mc.isLoggedIn == true) {
                     mc.out.println(this.name + ": " + toSend);
                     break;
                  }
               }
            }
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }
      try {
         this.in.close();
         this.out.close();
         this.s.close();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}

