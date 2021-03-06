import java.util.ArrayList;
import java.util.Collections;

import akka.actor.ActorRef;

/**
 * Jail serves a double purpose. First it acts as the collector for all people
 * who failed security and lists them at the end of the day. Second it must 
 * determine when the end of the day is based on when different Actors check 
 * in with it and terminalte the program accordingly. 
 * 
 * @author Andrew
 *
 */

public class Jail extends AbstractActor {
	
	/**
	 * Collection of people who failed security
	 */
	private ArrayList<Person> jailed;
	
	/**
	 * Number of security stations in TSA system
	 */
	private final int numSecurityStations;
	
	/**
	 * Number of closed security stations in TSA system
	 */
	private int numStationsClosed;
	
	/**
	 * Constructor for Jail
	 * 
	 * @param terminal - Terminal Actor to send log messages to
	 * @param numSecurityStations - total number of security stations in 
	 * 		TSA system
	 */
	public Jail(ActorRef terminal, int numSecurityStations){
    	super(ActorFactory.JAIL_SPACE, terminal);
		jailed = new ArrayList<Person>();
		this.numSecurityStations = numSecurityStations;
		numStationsClosed = 0;
    }
	
	/**
	 * Redefinition of OnRecieve method from Actor. This class handles messages
	 * of EndDay  and Person types.  
	 */	
	public void onReceive(Object message) throws Exception{
		/*
		 * If Person, add them to the list of jailed people. 
		 */
		if (message instanceof Person) {
			Person p = (Person)message;
			printToTerminal("Person " + p.getPersonId() 
					+ " arrives at jail.");
			//System.err.println("Person " + p.getPersonId() 
			//		+ " arrives at jail.");
			jailed.add(p);
			printToTerminal("Person " + p.getPersonId() 
					+ " placed in cell.");
		}
		/*
		 * When EndDay received, increment the number of stations that have 
		 * shut down. When the number of stations shut down has reached the 
		 * number of stations. Print the List of all people in the jail and 
		 * shutdown all Actors
		 */
		else if( message instanceof EndDay){
			numStationsClosed++;
			printToTerminal("Jail recieved end of day message " + numStationsClosed);
			if(numStationsClosed == numSecurityStations){
				printJailed();
				printToTerminal( "Jail Closed" );
				getContext().stop();
			}
		}
		/*
		 * All other messages are errors. Message printed here for debugging
		 * purposes.
		 */
		else{
			System.err.println("Security recieved invalid message: " + 
					message.toString());
		}
	}
	
	/**
	 * Override of default stop function in actor. Prints a message to 
	 * Terminal actor before shutting down.  
	 */
	@Override
	public void postStop() {
		printToTerminal("Jail sent end of day message to terminal\n\n");
		terminal.tell(new EndDay());
	}
	
	/**
	 * Override of default start function in actor. Prints a message to 
	 * Terminal Actor out upon start up.  
	 */
	@Override
	public void preStart() {
		printToTerminal("Jail Online");
	}
	
	
	/**
	 * Lists the people in Jail in a formatted list. 
	 */
	public void printJailed(){
		Collections.sort(jailed);
		String manifest = "\nPassengers Detained\r\n";
		String spacing = "";
		for(int i=0; i<terminalSpacing; i++){
			spacing  = spacing.concat(" ");
		}
		String nextLine = ""; 
		for( Person p : jailed){
			nextLine = spacing.concat(" - Person " + p.getPersonId() + "\r\n");
			manifest = manifest.concat(nextLine);
		}
		printToTerminal(manifest);
	}

}
