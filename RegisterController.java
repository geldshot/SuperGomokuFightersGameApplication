package supergomokutest;

/*
 * Team SprGmkft
 * MatchMaking
 * ClientMain
 * CSCE320 Spring
 * 4-25-12
 * Java 7 with the most recent Java Compiler
 * Java API documentation, Dr. Hauser
 * Revision #3: Nothing major was added for this deliverable 
 * except the error message handling method and redoing the constructor to have
 * the view and model passed in.
 */

/**
 * RegisterController class that is responsible for communication between the
 * register view and the register model.
 */
public class RegisterController {
    private RegisterView view;
    private ClientModel model;
    
    /**
     * Constructor for the RegisterController class.
     * @param model The ClientModel for the Register Controller.
     * @param view The RegisterView for the Register Controller.
     */
    public RegisterController(ClientModel model, RegisterView view){
        this.model = model;
        this.view = view;
    }
    
    /**
     * Sets the view that the controller controls.
     * @param view the register view.
     */
    public void setView(RegisterView view){
        this.view = view;
    }
    /**
     * Sets the model that controller communicates with.
     * @param model the ClientModel.
     */
    public void setModel(ClientModel model){
        this.model = model;
    }
    /**
     * Relays a message back to the model from the view.
     * @param str The message to be sent to the Server.
     */
    public void sendMessage(String str){
        System.out.println("Register controller message: " + str);
        model.sendMessage(str);
    }

    /**
     * Handles the event when the back button is clicked in the register view.
     * @param swapNum Number of the view to be switched into the main frame.
     */
    public void backButtonHandler(int swapNum) {
        this.model.swap(swapNum);
    }

    /**
     * Calls the reset method in the view to reset the text fields.
     */
    public void reset() {
        this.view.reset();
    }

    /**
     * Calls the error handling method in the view when an invalid register 
     * attempt occurs.
     */
    public void invalidRegister() {
        this.view.invalidRegisterResult();
    }

    public void storeUserName(String username) {
        this.model.setUserName(username);
    }
}
