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
 * game of Gomoku. Implemented the Invite, Refresh and Logout ButtonHandlers to
 * deal with the events of those buttons being clicked.
 *
 */

/**
 * Matchmaking Controller responsible for communication between the ClientModel
 * and the MatchMakingView.
 * @author Austin Williams
 */
public class MatchMakingController {

    MatchMakingView view;
    private ClientModel model;
    
    /**
     * Constructor for the MatchMaking Controller.
     * @param model The ClientModel for the MatchMakingController.
     * @param view The MatchMakingView for the MatchMakingController.
     */
    public MatchMakingController(ClientModel model, MatchMakingView view){
        this.model = model;
        this.view = view;
    }
    
    /**
     * Sets the Model for the MatchMaking Controller.
     * @param model the ClientModel for the MatchMakingController.
     */
    public void setModel(ClientModel model){
        this.model = model;
    }
    /**
     * Sets the View that the controller is responsible for.
     * @param view The MatchMakingView for the MatchMakingController.
     */
    public void setView(MatchMakingView view){
        this.view = view;
    }
    /**
     * Sends a message to the Server. Not used for anything in the current 
     * deliverable
     * @param message The message sent to the MatchMakingView.
     */
    public void sendMessage(String message) {
        this.model.sendMessage(message);
    }

    /**
     * Sends the online list to the MatchMakingView so it can be posted and/or 
     * updated.
     * @param message The list of online players from the server as a string.
     */
    public void postList(String message) {
        view.postList(message);
    }

    /**
     * Handles the event when the logout button is clicked in the matchmaking
     * view.
     * @param swapNum Number of the view to be switched into the main frame.
     */
    public void logoutButtonHandler(int swapNum) {
        this.model.logout();
        this.model.swap(swapNum);
    }
    /**
     * Handles the event when the invite button is clicked in the matchmaking 
     * view. Sends a message with the name of the user to be invited to a new 
     * game.
     * @param inviteName The name of the user to be invited to a new game.
     */
    public void inviteButtonHandler(String inviteName) {
        this.model.sendMessage(inviteName);
    }
    /**
     * Handles the event when the refresh button is clicked in the matchmaking
     * view. Sends a message to the Server requesting the updated online
     * list.
     * @param message The message "M\n" which requests an updated version of the
     * online player list.
     */
    public void refreshButtonHandler(String message) {
        this.model.sendMessage(message);
    }

    public void setInviteButtonEnable(boolean flag) {
        this.view.setInviteButtonEnableDisable(flag);
    }
    
}
