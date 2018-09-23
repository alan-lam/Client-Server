import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
   
   public static void main (String[] args) throws IOException, UnknownHostException {

      System.out.print("Enter IP address of server: ");
      Scanner scanner = new Scanner(System.in);
      String ip = scanner.next();

      Socket s = new Socket(ip, 9563);
      BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
      BufferedReader sys_in = new BufferedReader(new InputStreamReader(System.in));
      PrintWriter out = new PrintWriter(s.getOutputStream(), true);

      Thread sendMessage = new Thread(new Runnable() {
         @Override
         public void run() {
            while (true) {
               try {
                  String message = sys_in.readLine();
                  out.println(message);
                  if (message.equals("#logout")) {
                     sys_in.close();
                     break;
                  }
               }
               catch (Exception e) {
                  e.printStackTrace();
               }
            }
         }
      });

      Thread readMessage = new Thread(new Runnable() {
         @Override
         public void run() {
            while (true) {
               try {
                  String message = in.readLine();
                  if (message == null) {
                     break;
                  }
                  System.out.println(message);
               }
               catch (Exception e) {
                  e.printStackTrace();
               }
            }
         }
      });

      sendMessage.start();
      readMessage.start();
   }
}

