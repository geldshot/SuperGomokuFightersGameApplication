package supergomokutest;

/*
 * Team SprGmkft
 * MatchMaking
 * ClientMain
 * CSCE320 Spring
 * 4-25-12
 * Java 7 with the most recent Java Compiler
 * Java API documentation, Dr. Hauser
 * Revision #3: Nothing major was added for this deliverable.
 */

/**
 * Controls communication between the game view and the game model.
 * @author Austin Williams
 */
class GameController {
    private GameModel model;
    private GameView view;
    /**
     * Constructor for the game controller.
     */
    public GameController(){
        
    }
    /**
     * Sets the model for the game controller.
     * @param g_model The game model for the game controller.
     */
    public void setGameModel(GameModel g_model){
        this.model = g_model;
    }
    /**
     * Sets the view for the game controller.
     * @param g_view The game view for the game controller.
     */
    public void setGameView(GameView g_view){
        this.view = g_view;
    }
        
}
