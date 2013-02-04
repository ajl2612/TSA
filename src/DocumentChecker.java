import java.util.Random;
import akka.actor.ActorRef;

/**
 * This actor is responsible for taking in persons, scanning their information
 * and determining if they pass or fail the document check. If they pass, they
 * are placed into one of the Queues to handle security screening. 
 * 
 * 
 * @author Chris
 * @author Carol
 * @MasterOfConcurrency Andrew Lyne
 *
 */
public class DocumentChecker extends AbstractActor{
	
	private final ActorRef stations[];
	private int currentQueue = 0;
	
	/**
	 * Creates an array of actor refrences based on the size of numStations 
	 * passed in. It then instantiates and starts these threads. 
	 */

	public DocumentChecker(ActorRef terminal, ActorRef[] queues){
		super(ActorFactory.DOC_CHECK_SPACE, terminal);
		stations = queues;
	}
	
	public void onReceive(Object message) throws Exception {
		if( message instanceof Person){
			trySendPersonToQueue( (Person)message);
		}
		else if( message instanceof EndDay ){
			for(int i = 0; i < stations.length; i++){
				stations[i].tell((EndDay)message);
			}
			System.out.println("DocumentChecker shutting down");
		}else{
			System.err.println("DocumentChecker recieved invalid message: " + 
					message.toString());
		}
	}

	public void trySendPersonToQueue(Person person){
		Random r = new Random();
		if(r.nextInt(5) == 0) {
			rejectPerson(person);
		}
		else {
			printToTerminal("Person: " + person.getPersonId() + "sent to queue " 
					+ currentQueue + ".");
			stations[currentQueue++].tell(person);
			currentQueue = currentQueue % stations.length;
		}
	}
	
	public void rejectPerson(Person person){
		printToTerminal("Person: " + person.getPersonId() 
				+ "failed Document Check and has been sent away.");
	}
}
