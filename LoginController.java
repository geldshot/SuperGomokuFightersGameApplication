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
 * except the error message handling method, swapping to matchmaking view, and
 * setting the state of the application.
 */


/**
 * 
 * @author Austin Williams
 */
public class LoginController {

    private ClientModel model;
    private LoginView view;
    
     /**
     * Constructor for LoginController
     */
    LoginController(ClientModel model, LoginView view){
        this.model = model;
        this.view = view;
    }
    /**
     * Sets the model for the Login Controller.
     * @param model The model for the controller.
     */
    public void setModel(ClientModel model){
        this.model = model;
    }
    /**
     * Sets the view that the controller is responsible for.
     * @param view The view for the controller.
     */
    public void setView(LoginView view){
        this.view = view;
    }
    /**
     * Calls the swap method in the Model to swap the current login view
     * with the Register view.
     */
    public void swapLoginWithRegister(){
        this.model.swap(1);
    }
    
    /**
     * Sends the login information from the view to the ClientModel.
     * @param str the information represented as a string.
     */
    public void login(String str) {
        model.sendMessage(str);
    }
    /**
     * Returns the value of the boolean connection check that occurred when the
     * application started.
     * @return True if a connection was established with the server. False if 
     * the connection was not established.
     */
    public boolean getConnectionCheck(){
        return model.getConnectionCheck();
    }

    /**
     * Calls the reset method in the view to reset the text fields to blank.
     */
    public void reset() {
        this.view.reset();
    }
    /**
     * Calls the invalidLoginResult method in the view. Only called when the 
     * login was unsuccessful.
     */
    public void invalidLogin(){
        this.view.invalidLoginResult();
    }

    /**
     * Calls the model's swap method to swap in the matchmaking view.
     */
    public void swapLoginWithMatchMaking() {
        this.model.swap(2);
    }
    /**
     * Sets the state of the Application to 1 so the ClientModel knows that the
     * user came from the login view.
     * @param i The new state of the application.
     */
    public void setState(int i) {
        this.model.setState(i);
    }
}
