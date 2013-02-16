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
 * game of Gomoku. Not much was done in this class regarding matchmaking except
 * for updating a few of the controller constructors and adding setters for the
 * game model.
 * NOTE: hostname is command line args[0] and the port number is args[1].
 *
 */

import javax.swing.JFrame;

/**
 * Main Class for the Client that instantiates all of the views, models, and 
 * controllers. Currently instantiates and displays all of the views.
 * 
 */
public class ClientMain {
    
    /**
     * Main Method for the Super Gomoku Fighters Application. Creates and 
     * instantiates all of the models, views, and controllers. Also sets models,
     * views and controllers for each model, view and controller. Finally adds
     * the first view to the ClientFrame and displays it to the user.
     * @param args Enter hostname as args[0] and the port number as args[1].
     */
    public static void main(String args[]){
        JFrame clientFrame = new JFrame();
        JFrame gameFrame = new JFrame();
        String hostname = "127.0.0.1";
        int port = 4445;

        //Code used in debugging. Disregard.
        // "152.117.248.58";  4445; 
               
        //Integer.parseInt(args[1]); args[0];
        Server serv = new Server(port);
        serv.listen();
        
        //Instantiate all of the views
        RegisterView reg = new RegisterView();
        LoginView login = new LoginView();
        MatchMakingView match = new MatchMakingView();
        GameView game = new GameView();
        GameOptions options = new GameOptions();
        //Instantiate all of the Models
        OverlordModel overModel = new OverlordModel(hostname, port);
        overModel.listen();
        ClientModel cliModel = new ClientModel(clientFrame, overModel);
        GameModel gameModel = new GameModel(gameFrame);
        
        //Instantiate all of the Controllers
        RegisterController regController = new 
                RegisterController(cliModel, reg);
        LoginController logController = new LoginController(cliModel, login);
        MatchMakingController matchController = new 
                MatchMakingController(cliModel, match);
        GameController gameController = new GameController();
        //set Controllers for the views
        reg.setController(regController);
        login.setController(logController);
        match.setController(matchController);
        game.setController(gameController);
        //Set Models
        overModel.setClientModel(cliModel);
        overModel.setGameModel(gameModel);
        cliModel.setLoginController(logController);
        cliModel.setMatchaMakingController(matchController);
        cliModel.setRegisterController(regController);
        gameModel.setGameController(gameController);
        gameModel.setModel(overModel);
        //add the views to the Client Model list
        cliModel.addView(login);
        cliModel.addView(reg);
        cliModel.addView(match);
        cliModel.addView(options);
        //add the login panel to the frame and make it visible
        clientFrame.setTitle("Login");
        clientFrame.add(login);
        clientFrame.repaint();
        clientFrame.pack();
        clientFrame.setVisible(true);
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
