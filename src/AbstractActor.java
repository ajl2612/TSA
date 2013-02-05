import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * This class contains all the basic functions needed for TSA actors. These 
 * functions are aimed at simplifying printing messages to a log for easy 
 * readability.
 * 
 * @author Andrew Lyne
 */
public abstract class AbstractActor extends UntypedActor{
	
	/**
	 * Actor Reference to a Terminal actor for printing log messages.  
	 */
	protected ActorRef terminal;
	
	/**
	 * Integer representing the number of spaces to indent this Actor's 
	 * messages over in the log. 
	 */
	protected int terminalSpacing;
	
	/**
	 * Constructor for AbstractActor. 
	 *  
	 * @param termSpaces - Number of spaces to indent messages by. 
	 * @param term - Actor Refrence to Terminal Actor for printing messages.
	 */
	public AbstractActor( int termSpaces, ActorRef term){
		terminalSpacing = termSpaces;
		terminal = term;
	}
	
	/**
	 * Wraps the passed string value into a Message object and sends it to
	 * the Terminal Actor to be printed.
	 * 
	 * @param content - String to be sent to Terminal Actor to be printed out.
	 */
	public void printToTerminal( String content){
		Message message = new Message( content, terminalSpacing );
		terminal.tell( message );
	}

	/**
	 * Returns the number of spaces to place before messages for this instance 
	 * of Abstract Actor;
	 * 
	 * @return - the number of spaces
	 */
	public int getSpacingSize(){
		return terminalSpacing;
	}
	
}
