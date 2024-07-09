import java.net.*; 
import java.io.*;
import static java.lang.System.*;
/* the class that let the server starts */
public class orderserver{

    private int port; 
    private ServerSocket server;
    public static void main(String[] args){
        orderserver newserverstart = new orderserver(7777); 
        newserverstart.runserver();
    }
    
    public orderserver (int port) { 
        this.port = port; 
        if(!startserver()) {
        err.println("Error during creation of server");}
    }
/* method to start the server,that returns that was successfully created */
    private boolean startserver() {
        try { 
          server = new ServerSocket(port); } 
        catch (IOException ex) { 
         ex.printStackTrace(); 
         return false; 
        } 
         out.println("Server successfully created"); 
        return true; 
    }
    /* method that let the clients connect and close the server */
    public void runserver() { 
        while (true) {
          try { 
           out.println("Waiting for requesters"); 
           Socket s1 = server.accept(); 
           out.println("Client connected");
     
           DataInputStream dIn = new DataInputStream(s1.getInputStream());
   
           boolean finish = false;
           while(!finish) {
           byte exitbyte = dIn.readByte();
               switch(exitbyte)
               {
               case 1: 
                   out.println(dIn.readUTF());
                   break;
               default:
                   finish = true; }}
   
           dIn.close();
           s1.close(); 
           out.println("Server Closed successfully"); }
           catch (IOException ex) {
              ex.printStackTrace(); 
            } 
         } 
     }
}