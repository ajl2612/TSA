import java.util.Random;
import akka.actor.ActorRef;

/**
 * This actor is responsible for taking in persons, scanning their information
 * and determining if they pass or fail the document check. If they pass, they
 * are placed into one of the Queues to handle security screening. 
 * 
 * @author Chris
 * @author Carol
 * @MasterOfConcurrency Andrew Lyne III
 *
 */
public class DocumentChecker extends AbstractActor{
	
	/**
	 * Percent chance out of 100 that a given passenger will fail the document
	 * check.
	 */
	private final int PERCENT_FAIL = 20;
	
	/**
	 * Array containing references to the queues at the head of each security station.
	 */
	private final ActorRef stations[];
	
	/**
	 * Counter representing the currently selected queue.
	 */
	private int currentQueue = 0;
	
	/**
	 * Constructor for Document checker. Calls the super class constructor
	 * then sets the passed array of queues as the local copy.  
	 * 
	 * @param terminal - Terminal Actor to print messages to
	 * @param queues - array of queues 
	 */
	public DocumentChecker(ActorRef terminal, ActorRef[] queues){
		super(ActorFactory.DOC_CHECK_SPACE, terminal);
		stations = queues;
	}
	
	/**
	 * Redefinition of OnRecieve method from Actor. This class handles messages
	 * of EndDay and Person types.  
	 */
	@Override
	public void onReceive(Object message) throws Exception {
		/*
		 * If instance of Person, attempt to queue the person into the current 
		 * queue. 
		 */
		if( message instanceof Person){
			Person p = (Person)message;
			printToTerminal("Person " + p.getPersonId() + 
					" arrives at Document Checker.");
			trySendPersonToQueue( p );
		}
		/*
		 * If instance of EndDay message, pass the message along to all queues 
		 * in the array then shut self down. 
		 */
		else if( message instanceof EndDay ){
			printToTerminal("Document Cecker recieves End of Day message");

			getContext().stop();
		}
		/*
		 * All other messages are errors. Message printed here for debugging
		 * purposes.
		 */
		else{
			System.err.println("DocumentChecker recieved invalid message: " + 
					message.toString());
		}
	}
	
	/**
	 * Override of default stop function in actor. Prints a message to 
	 * Terminal actor before shutting down.  
	 */
	@Override
	public void postStop() {
		printToTerminal("Document Check Closed" );
		for(int i = 0; i < stations.length; i++){
			printToTerminal("End of Day message sent to queue " + i );
			stations[i].tell(new EndDay());
		}
	}
	
	/**
	 * Override of default start function in actor. Prints a message to 
	 * Terminal Actor out upon start up.  
	 */
	@Override
	public void preStart() {
		printToTerminal("Document Checker Online");
	}
	
	/**
	 * Attempts to enqueue the passed person into the current security 
	 * station. A random number is generated between 0 and 100. If the number 
	 * is less than the constant PERCENT_FAIL the person has failed the 
	 * document check. Otherwise they are approved and sent to the current 
	 * security queue. The current security station is then incremented so 
	 * that new passengers are added in a cycle.  
	 * 
	 * @param person - Person to potentially be queued 
	 */
	public void trySendPersonToQueue(Person person){
		Random r = new Random();
		if(r.nextInt(100) < PERCENT_FAIL) {
			rejectPerson(person);
		}
		else {
			printToTerminal("Person: " + person.getPersonId() + "sent to queue " 
					+ currentQueue + ".");
			stations[currentQueue++].tell(person);
			currentQueue = currentQueue % stations.length;
		}
	}
	
	/**
	 * Rejecting a Person from the security queue entails sending a message to 
	 * Terminal of the failure.
	 * @param person
	 */
	public void rejectPerson(Person person){
		printToTerminal("Person: " + person.getPersonId() 
				+ "failed Document Check and has been sent away.");
	}
}
