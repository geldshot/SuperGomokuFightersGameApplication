/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package supergomokutest;

/*
 * Team SprGmkft
 * MatchMaking
 * ClientMain
 * CSCE320 Spring
 * 4-25-12
 * Java 7 with the most recent Java Compiler
 * Java API documentation, Dr. Hauser
 * Revision #3: Added methods for dealing with connecting to another user. Class
 * contains a Socket which connects to the opened ServerSocket on the other 
 * Client. Also contains a listener thread that receives messages from the other
 * Client during game play. For this deliverable no game play options or 
 * features were implemented.
 *
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * NetworkPlayer class that contains methods for Socket creation and connection. 
 * @author Austin Williams
 */
public class NetworkPlayer extends Player implements Runnable {

    JFrame frame;
    int port;
    String hostname;
    Socket sock;
    InputStream in;
    OutputStream out;
    Thread listener;
    boolean check;
    /**
     * Constructor for the NetworkPlayer class. Takes in the server ipaddress and 
     * port number and calls the connect method which starts the Socket creation
     * and connection.
     * @param playerHostName The ipaddress of the ServerSocket
     * @param port The port number the ServerSocket was created on.
     */
    public NetworkPlayer(Socket s){
        sock = s;
    }
    /**
     * Starts Socket creation and connection. Also contains the pop up messages
     * to deal with failed Socket creation.
     */
//    @Override
//    void connect() {
//        //check = this.createConnection(this.hostname, this.port);
//        if(check == false){
//                int confirm = JOptionPane.showConfirmDialog(frame, 
//                        "Failed to connect to the other player. Try again?",
//                        "Connection Error", JOptionPane.OK_CANCEL_OPTION,
//                        JOptionPane.ERROR_MESSAGE);
//                if(confirm == 0){
//                   //check = this.createConnection(this.hostname, this.port);
//                   if(check == false){
//                       JOptionPane.showConfirmDialog(frame, 
//                        "Failed to connect to the other player. Try inviting "
//                               + "again", "Connection Error", JOptionPane.OK_CANCEL_OPTION,
//                               JOptionPane.ERROR_MESSAGE);
//                       
//                   }
//                }
//            }
//    }
    
    @Override
    public void update(){
        
    }
    
    /**
     * Attempts to create a socket to the given Hostname and Port number. Used
     * mainly to create a connection to another user when playing a game.
     * @param hostname The hostname for the socket to connect to.
     * @param port The port number for the socket to use.
     * @return True if the creation of the socket was successful, false if the
     * creation fails.
     */
    /*public boolean createConnection(String hostname, int port){
        try{
            sock = new Socket(hostname, port);
            in = sock.getInputStream();
            out = sock.getOutputStream();
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return this.retryConnection(hostname, port);
        }
    }*/
    

    
    /**
     * Sends messages from this Client to the other Client during game play.
     * @param message The message for the other Client.
     */
    public void sendMessage(String message){
        byte[] bufferout;
         try{
            bufferout = message.getBytes();
            out.write(bufferout); // sends the message over the output stream
        }
        catch(Exception e){
            System.out.println("Failed to send the message");
            e.printStackTrace();
        }   
        
        
    }
    /**
     * Creates and starts the listener thread for this Player.
     * @return True if the listener was created successfully. False if not 
     * created successfully.
     */
    public boolean listen(){
        if(check == true){
            this.listener = new Thread(this);
            listener.start();
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Listener Thread for the NetworkPlayer class. NOTE: Not implemented for this 
     * deliverable.
     */
    @Override
    public void run() {
        
    }
    
    
}
