/*
 * Team SprGmkft
 * MatchMaking
 * ClientMain
 * CSCE320 Spring
 * 4-25-12
 * Java 7 with the most recent Java Compiler
 * Java API documentation, Dr. Hauser
 * Revision #3: Abstract class which outlines the methods and variables
 * for a player.
 */
package supergomokutest;



/**
 * Abstract class which outlines the methods and variables for a player.
 * @author Austin Williams
 */
public abstract class Player {
    
    String hostname;
    int port;
    
    abstract void update();
    
    
}
