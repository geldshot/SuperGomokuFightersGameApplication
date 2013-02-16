package supergomokutest;

/*
 * Team SprGmkft
 * MatchMaking
 * ClientMain
 * CSCE320 Spring
 * 4-25-12
 * Java 7 with the most recent Java Compiler
 * Java API documentation, Dr. Hauser
 * Revision #3: Added methods for dealing with inviting another user to play a 
 * game of Gomoku. Added a logout method to take the user back to the login view
 * and restart the authentication use case so a user can login with another 
 * username and password. Also implemented methods that assist with logout.
 *
 */

import java.io.*;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Overlord Model class that is responsible for communication between the
 * Server and the application.
 */
public class OverlordModel implements Runnable {
    
    Socket sock;
    Thread worker;
    BufferedReader in;
    OutputStream out;
    ClientModel c_model;
    GameModel g_model;
    JFrame frame;
    boolean connectionCheck = true;
    String hostname;
    String serverHostName;
    int port;
    private boolean done = false;
    private String username;
    
    /**
     * Constructor for the OverlordModel. Builds the socket connection to the 
     * Server.
     * @param hostname The hostname for the socket connection.
     * @param port The port number for the socket connection.
     */
    public OverlordModel(String hostname, int port){
        this.serverHostName = hostname;
        this.port = port;
        try{
            sock = new Socket(serverHostName, port);
            in = new BufferedReader(new InputStreamReader(
                    sock.getInputStream()));
            out = sock.getOutputStream();
        }
        catch(Exception e){
            retry();
        }
    }
    /**
     * Sets the ClientModel for the OverlordModel.
     * @param cli The ClientModel for the OverlordModel.
     */
    public void setClientModel(ClientModel cli){
        this.c_model = cli;
    }
    /**
     * Sets the GameModel for the OverlordModel.
     * @param gameM The GameModel for the OverlordModel. 
     */
    public void setGameModel(GameModel gameM){
        this.g_model = gameM;
    }
    /**
     * Method to send a message to the Server.
     * @param str The message to be sent to the Server.
     */
    public void sendMessage(String str){
        byte[] bufferout; // new byte array to hold the message
        
        try{
            bufferout = str.getBytes(); //converting the message to bytes
            out.write(bufferout); // sends the message over the output stream
        }
        catch(Exception e){
            System.out.println("Failed to send the message");
            e.printStackTrace();
        }   
    }
    
    /**
     * Creates and starts a new thread that listens for messages from the 
     * Server.
     */
    public void listen(){
        worker = new Thread(this);
        worker.start();
    }
    
    /**
     * Returns the value of the Connection Check boolean variable.
     * @return True if the connection is established. False if the connection 
     * failed
     */
    public boolean getConnectionCheck(){
        return this.connectionCheck;
    }

    /**
     * Second thread that listens for responses from the Server and sends them
     * out to the different Models
     */
    @Override
    public void run() {
        String message = "";
        try{
            while(!message.equalsIgnoreCase("c success")){
                message = in.readLine();//gets the length of the message
                System.out.println("Length of message: " + message.length());

                if(message.length() > 0){
                    
                    System.out.println("Message in overlord model: " + message);
                    //handles when the server shuts down 
                    if(message.equalsIgnoreCase("bye")){
                        worker.interrupt();
                        this.c_model.swap(0);
                        this.c_model.disconnectedMessage();
                        
                    }
                    else{
                        c_model.loginResult(message);
                    }
                }
            }
            message = in.readLine();
            //handles when the server shuts down
            if(message.equalsIgnoreCase("bye")){
                worker.interrupt();
                this.c_model.swap(0);
                this.c_model.disconnectedMessage();
                
            }
            else{
                System.out.println("Second Message in overlord model: " + message);
                message = message.substring(2);
                c_model.postList(message); //sends the online list to the 
                                                        //matchmaking view.
            }
            
            while(done != true){
                message = in.readLine();//gets the length of the message
                System.out.println("MatchMakingMessage: " + message);
                if(message.length() > 0){
                    
                    //handles when the server shuts down
                    if(message.equalsIgnoreCase("bye")){
                        worker.interrupt();
                        this.c_model.swap(0);
                        this.c_model.disconnectedMessage();
                        
                    }
                    else if(message.charAt(0) == 'g'){
                        this.g_model.handleMessage(message);
                    }
                    else{
                        c_model.sendMatchMakingMessage(message);
                    }
                }
            }
            worker.interrupt();
            
        }
        catch(Exception e){
            System.out.println("Error receiving the message");
            e.printStackTrace();
            
        }
    }

    /**
     * Attempts to connect to the server if the first connection attempt is 
     * unsuccessful. If the connection fails again boolean is set to false 
     * and prevents the network aspect of the application.
     * @param hostname The hostname used in the socket connection.
     * @param port The port number used in the socket connection.
     */
    public void retry() {
        try{
            sock = new Socket(serverHostName, port);
            in = new BufferedReader(new InputStreamReader(
                    sock.getInputStream()));
            out = sock.getOutputStream();
        }
        catch(Exception e){
            connectionCheck = false;
        }
    }
    /**
     * Closes the connection to the Server. Sends "bye" which closes the 
     * ServerThread on the Server side then sets the done boolean flag to false
     * then closes the socket connection, the inputstream and the outputstream.
     * @exception IOException thrown when closing the socket, input or 
     * outputstream fails.
     */
    public void closeConnection(){
        
        this.sendMessage("bye\n");
        try {
            this.in.close();
            this.out.close();
            this.sock.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to disconnect from the "
                    + "Server.");
        }
        this.setDone();
        
    }

    /**
     * Sets the done boolean flag for when the listener thread is done listening
     * for messages from the Server.
     */
    private void setDone() {
        this.done = true;
    }

    /**
     * Called when a user logs out of the application. Closes the connection to 
     * the Server, interrupts the thread, then attempts to connect to the Server
     * again and starts the listener thread.
     */
    public void logout() {
        this.worker.interrupt();
        this.closeConnection();
        this.retry();
        this.listen();
    }

    void setUserName(String username) {
        this.username = username;
    }
    
    public String getUserName(){
        return this.username;
    }
}
