import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.UntypedActorFactory;
import static akka.actor.Actors.actorOf;

/**
 * Class for creating various actors and groups of actors. 
 * 
 * @author Andrew Lyne
 */

public class ActorFactory {

	/**
	 * Number of spaces to place before Document Check messages.
	 */
	public static final int DOC_CHECK_SPACE = 0;
	
	/**
	 * Number of spaces to place before Queue messages.
	 */
	public static final int QUEUE_SPACE = 1;
	
	/**
	 * Number of spaces to place before BagScan and BodyScan messages.
	 */
	public static final int SCAN_SPACE = 2;
	
	/**
	 * Number of spaces to place before Security messages.
	 */
	public static final int SECURITY_SPACE = 3;
	
	/**
	 * Number of spaces to place before Jail messages.
	 */
	public static final int JAIL_SPACE = 4;
	
	/**
	 * Facrory method for creating and starting a Terminal Actor
	 * 
	 * @return Terminal Actor 
	 */
	public static ActorRef makeTerminal(){
		ActorRef term = actorOf( Terminal.class );
		term.start();
		return term;
	}
	
	/**
	 * Factory method for creating and starting the various classes in a 
	 * SecurityStation. A SecurityStation contains a Queue, a BodyScanner, 
	 * a BagScanner and a Security guard.This method creates all actors, links 
	 * them together as needed and starts them. A TSA system can have one to 
	 * many SecurityStation. The station starts with a queue who sends messages
	 * to the two scanners who each send messages to a single security guard. 
	 * This Security guard reports to a jail actor. 
	 * 
	 * @param terminal - Terminal Actor to print messages to. 
	 * @param lineNumber - Number representing which number station
	 * @param jail - Jail actor to send people who failed security to.
	 * @return - Reference to queue at the head of the SecurityStation
	 */
	public static ActorRef makeSecurityStation(final ActorRef terminal, final int lineNumber, final ActorRef jail){
		
		final ActorRef newSecurity = actorOf( new UntypedActorFactory(){
			// Inline override of default constructor call in Security
			@Override
			public Actor create(){
				return new Security( lineNumber, jail, terminal);
			}
		});
		
		final ActorRef newBagScan = actorOf( new UntypedActorFactory(){
			// Inline override of default constructor call in BagScanner
			@Override
			public Actor create(){
				return new BagScanner( lineNumber, newSecurity, terminal);
			}
		});
		
		final ActorRef newBodyScan = actorOf( new UntypedActorFactory(){
			// Inline override of default constructor call in BodyScanner
			@Override
			public Actor create(){
				return new BodyScanner( lineNumber, newSecurity, terminal);
			}
		});
		
		final ActorRef newQueue = actorOf( new UntypedActorFactory(){
			// Inline override of default constructor call in Queue
			@Override
			public Actor create(){
				return new Queue( lineNumber, newBodyScan, newBagScan, terminal);
			}
		});
		
		newSecurity.start();
		newBodyScan.start();
		newBagScan.start();
		newQueue.start();
		return newQueue;
	}
	
	/**
	 * Facrory method for creating and starting a Jail Actor. Requires the
	 * total number of SecurityStations for creation.
	 * 
	 * @param terminal - Terminal Actor to print messages to
	 * @param numSecurityStations - Total number of security stations. 
	 * @return
	 */
	public static ActorRef makeJail(final ActorRef terminal, final int numSecurityStations){
		ActorRef newActor = actorOf( new UntypedActorFactory(){
			// Inline override of default constructor call to one implemented
			@Override
			public Actor create(){
				return new Jail(terminal, numSecurityStations);
			}
		});
		newActor.start();
		return newActor;
	}
	
	/**
	 * Facrory method for creating and starting a Document Checker Actor. 
	 * 
	 * @param terminal - Terminal Actor to print messages to
	 * @param queues - Array of queues representing the start of the security stations. 
	 * @return
	 */
	public static ActorRef makeDocumentChecker( final ActorRef terminal, final ActorRef[] queues){
		ActorRef newActor = actorOf( new UntypedActorFactory(){
			// Inline override of default constructor call to one implemented
			@Override
			public Actor create(){
				return new DocumentChecker(terminal, queues);
			}
		});
		newActor.start();
		return newActor;
	}
	
}
