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
 * game of Gomoku. Added a method to start the ServerSocket if the user in the 
 * one who has been invited to the game. Added a disconnected message method to 
 * deal with unexpected Server disconnects. Updated serveral of the other 
 * methods with things to deal with the current phase of the application. Added
 * a state variable to distinguish between a successful login and a newly
 * registered user.
 *
 */


import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 *
 * ClientModel class that deals with user data. Handles login and
 * register controllers and communicates back with the overlord model
 */
public class ClientModel {
    RegisterController regController;
    LoginController logController;
    MatchMakingController matchController;
    private OverlordModel model;
    private JFrame frame;
    ArrayList<JPanel> list;
    final String INVITE_REQUEST = "c invite";
    private final int DEFAULT_PORT_NUM = 4446;
    int state;
    
    /**
     * Constructor for the ClientModel.
     * @param mainFrame The JFrame that the views will be loaded into.
     */
    public ClientModel(JFrame mainFrame, OverlordModel model){
        list = new ArrayList<JPanel>();
        frame = mainFrame;
        this.model = model;
    }
    /**
     * Adds a view to the list of views.
     * @param pane The JPanel to be added to the list.
     */
    public void addView(JPanel pane){
        list.add(pane);
    }
    
    /**
     * Sets the Overlord Model for the ClientModel.
     * @param model the Overlord Model.
     */    
    public void setModel(OverlordModel model){
        this.model = model;
    }
    
    /**
     * Sets the Register controller for the ClientModel.
     * @param control the register controller.
     */
    public void setRegisterController(RegisterController control){
        this.regController = control;
    }
    /**
     * Sets the Login controller for the ClientModel.
     * @param control the login controller.
     */    
    public void setLoginController(LoginController control){
        this.logController = control;
    }
    /**
     * Sets the MatchMaking Controller for the ClientModel.
     * @param control The matchmaking controller.
     */
    public void setMatchaMakingController(MatchMakingController control){
        this.matchController = control;
    }
    
    /**
     * Relays a message sent from a controller back to the overlord model.
     * @param message The message to be sent to the model.
     */
    public void sendMessage(String message){
        model.sendMessage(message);
    }
    /**
     * Returns the value of the initial connection attempt. 
     * @return True if the connection was successful. False if the connection 
     * was unsuccessful.
     */
    public boolean getConnectionCheck(){
        return model.getConnectionCheck();
    }
    /**
     * Handles the result of a login attempt. If the attempt is successful the 
     * current view is swapped with the matchmaking view. If unsuccessful a 
     * login failed message is shown.
     * @param message The result of the login from the server. Either success
     * or failed.
     */
    public void loginResult(String message){
        message = message.substring(2);
        if(message.charAt(0) == 's'){
            this.swap(2);
        }
        else{
            if(this.state == 1){
                this.model.setUserName("");
                this.logController.reset();
                this.logController.invalidLogin();
            }else{
                this.model.setUserName("");
                this.regController.reset();
                this.regController.invalidRegister();
            }    
        }
    }
    /**
     * Sends the list of online players to the MatchMaking Controller.
     * @param message The list of online players from the server as a string.
     */
    public void postList(String message){
        matchController.postList(message.substring(2));
    }
    /**
     * Deals with various matchmaking messages that are sent from the Server.
     * @param message The message from the Server.
     */
    public void sendMatchMakingMessage(String message){
        message = message.substring(2);
        //Handles the Invite Denied message
        if(message.equalsIgnoreCase("deny")){
            this.enableInviteButton(true);
            JOptionPane.showMessageDialog(frame, "Invitation Denied.", 
                    "Invitation Warning", JOptionPane.WARNING_MESSAGE);
        }
        //Handles the general failure of a message due to syntax.
        else if(message.equalsIgnoreCase("fail")){
            
        }
        //Handles the Invite case and the request for the Users IP address.
        else if(message.charAt(0) == 'I'){
            String[] pieces = message.split(":");
            if(pieces[0].equalsIgnoreCase("invite")){
                this.invitation(pieces[1]);
            }
            else if(pieces[0].equalsIgnoreCase("ipaddress")){
                this.model.hostname = pieces[1];
            }
        }
        //Handles the list response message from the server after a request for
        //an updated list is sent.
        else if(message.charAt(0) == 'L'){
            this.postList(message);
        }
    }
    
    public void enableInviteButton(boolean flag){
        this.matchController.setInviteButtonEnable(flag);
    }
    /**
     * Swaps the current view with a view from the ArrayList of views. 
     * @param swapNum The number of the view to be swapped into the main frame.
     */
    public void swap(int swapNum){
        frame.removeAll();
        frame.setVisible(false);
        frame = new JFrame();
        if(swapNum == 0){
            frame.setTitle("Login");
        }
        else if(swapNum == 1){
            frame.setTitle("Register");
        }
        else if(swapNum == 2){
            frame.setTitle(this.model.getUserName() + " Matchmaking");
        }
        else if(swapNum == 3){
            frame.setTitle("Game Options");
        }
        frame.add(this.list.get(swapNum));
        if(swapNum == 0){
            this.logController.reset();
        }
        else if(swapNum == 1){
            this.regController.reset();
        }
        frame.repaint();
        frame.pack();        
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    /**
     * Creates a new game invitation and displays it to the user with the name of the
     * other user who sent the game invitation.
     * @param message The name of the user who sent the game invitation.
     */
    private void invitation(String message) {
        UserInvitation invite = new UserInvitation();
        invite.setTitle("Invitation");
        invite.setModel(this);
        invite.postInvitation(message);
        invite.repaint();
        invite.pack();
        invite.setVisible(true);
    }

    /**
     * Starts the ServerSocket when the Client accepts a game invitation.
     */
    public void gameStartServerForClient() {
        boolean check = this.model.g_model.createServerSocket(DEFAULT_PORT_NUM);
        if(check != true){
            String port = JOptionPane.showInputDialog(frame, "Default port not "
                    + "available. Please enter a new port to use:");
            this.model.g_model.createServerSocket(Integer.parseInt(port));
        }
    }
    /**
     * Sets the state of the Client. Mainly used for differentiating between
     * login and register views which assists in resetting the views after an 
     * unsuccessful login or register attempt.
     * @param newState The new state of the client.
     */
    public void setState(int newState){
        this.state = newState;
    }
    /**
     * Error message shown when the Server unexpectedly shuts down.
     */
    public void disconnectedMessage() {
        JOptionPane.showMessageDialog(frame, "You have been disconnected.", 
                "Error 32", JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Called from the MatchMakingView to log out a user from the application.
     * Calls the logout method in the OverlordModel.
     */
    public void logout() {
        this.model.logout();
    }

    public void setUserName(String username) {
        this.model.setUserName(username);
    }
    
    
    
}
