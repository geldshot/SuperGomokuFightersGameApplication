package supergomokutest;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * Team SprGmkft
 * MatchMaking
 * ClientMain
 * CSCE320 Spring
 * 4-25-12
 * Java 7 with the most recent Java Compiler
 * Java API documentation, Dr. Hauser
 * Revision #3: Added methods for dealing with connecting to another user. Class
 * now contains a ServerSocket if the Client was the invited user or creates
 * a new Player if the Client was the invitee. Also has a thread for when the 
 * Client is the invited to listen for responses from the other Client during 
 * game play.
 *
 */

/**
 * GameModel class that deals specifically with the game piece of the program 
 * and with connection between two Clients playing the game.
 */
public class GameModel implements Runnable {
    private OverlordModel model;
    private GameController controller;
    private JFrame frame;
    ServerSocket serverSocket;
    private Thread worker;
    NetworkPlayer player;
    private final int DEFAULT_PORT_NUM = 4446;
    
    /**
     * Constructor for the GameModel.
     * @param pane The Game Frame for the game window.
     */
    public GameModel(JFrame pane){
        this.frame = pane;
    }
    /**
     * Sets the Overlord Model for the Game Model.
     * @param model The overlord model for the Game Model.
     */
    public void setModel(OverlordModel model){
        this.model = model;
    }
    
    /**
     * Sets the Player for the Game Model to keep track of.
     * @param p The player object for the Game Model.
     */
    public void setPlayerOne(NetworkPlayer p){
        this.player = p;
    } 
    
    /**
     * Sets the Game controller for the GameModel.
     * @param control the Game Controller.
     */
    public void setGameController(GameController control){
       this.controller = control; 
    }
    /**
     * Message handler for the Game Model. For this deliverable deals mainly
     * with the Socket creation messages.
     * @param message The game message sent from the Server.
     */
    public void handleMessage(String message) {
        String[] pieces;
        //gets rid of all excess characters and splits the string into an array
        message = message.substring(2);
        pieces = message.split(":");
        
        
        if(pieces[0].equalsIgnoreCase("connect to")){
            this.connectToNetworkPlayer(pieces);
        }
    }
    
    private void connectToNetworkPlayer(String[] pieces){
        boolean check;
        pieces[1] = pieces[1].substring(1);
        try{
            Socket s = new Socket(pieces[1], DEFAULT_PORT_NUM);
            player = new NetworkPlayer(s);
        }
        catch(Exception e){
            check = this.retryConnection(pieces[1], DEFAULT_PORT_NUM);
            if(check != true){
                JOptionPane.showConfirmDialog(frame, 
                  "Failed to connect to the other player. Try inviting "
                  + "again", "Connection Error", 
                  JOptionPane.OK_CANCEL_OPTION,
                  JOptionPane.ERROR_MESSAGE);
                }
            this.model.c_model.enableInviteButton(true);
            }
    }
    
    /**
     * Attempts to create a socket to the given Hostname and Port number again 
     * when the first attempt fails. Used mainly to create a connection to 
     * another user when playing a game.
     * @param hostname The hostname for the socket to connect to.
     * @param port The port number for the socket to use.
     * @return True if the creation of the socket was successful, false if the
     * creation fails.
     */
    private boolean retryConnection(String hostname, int port) {
        System.out.println("Failed first connection. Trying again.");
        try{
            Socket s = new Socket(hostname, port);
            player = new NetworkPlayer(s);
            return true;
        }
        catch(Exception e){
            System.out.println("Failed again to connect to the Server");
            return false;
        }
    }
    
    /**
     * Attempts to create a ServerSocket in the Game Model.
     * @param port The port to create the ServerSocket on.
     * @return True if the creation was successful, false if it was 
     * unsuccessful.
     */
    public boolean createServerSocket(int port){
        try{
            serverSocket = new ServerSocket(port);
            this.listen();
            return true;
        }
        catch(Exception e){
            return this.retryServer(port);
        }
    }
    /**
     * Creates and starts a new listener thread for the ServerSocket to 
     * communicate with the other client across the network.
     */
    public void listen(){
        worker = new Thread(this);
        worker.start();
    }
    
    
    /**
     * Attempts to create a ServerSocket in the Game Model again when the first 
     * attempt fails.
     * @param port The port to create the ServerSocket on.
     * @return True if the creation was successful, false if it was 
     * unsuccessful.
     */
    private boolean retryServer(int port) {
        try{
            serverSocket = new ServerSocket(port);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    /**
     * Listener Thread for the ServerSocket that gets created when the user 
     * accepts an invite to play a game of Gomoku.
     */
    @Override
    public void run() {
        try{
            Socket s = serverSocket.accept(); // creates a new socket
            player = new NetworkPlayer(s);
        }
        catch(Exception e){
                // when the server socket is closed, interupts the thread, sets
                // boolean flag to true.
            worker.interrupt();
        }
    }

    /**
     * Calls the Client Models swap method and swaps in the Game Options 
     * placeholder view. Also displays a success pop up box indicating that 
     * connection was successful.
     * @param message The message from either the Player or the Server 
     * indicating that connection from that side was successful.
     */
    private void connected(String message) {
        this.model.c_model.swap(3);
        JOptionPane.showMessageDialog(frame, message, 
                "Win 37", JOptionPane.PLAIN_MESSAGE);
    }
    
    public void sendStats(String stats){
        this.model.retry();
        if(model.connectionCheck == false){
            this.model.retry();
        }
        this.model.sendMessage(stats);
    }
    
    
}
