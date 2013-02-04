import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import static akka.actor.Actors.actorOf;
import akka.actor.UntypedActor;

/**
 * Stub
 * 
 * @author Chris
 * @author Carol
 * @CodeGuru Andrew Lyne
 * 
 */
public class Queue extends AbstractActor {

	//LinkedList<Person> queue = new LinkedList<Person>();
	//boolean scannerClear = true;

	final ActorRef baggageScanner; 
	final ActorRef bodyScanner;
	private final int stationNumber;
	
	private ConcurrentLinkedQueue<Person> personQueue;
	private ConcurrentLinkedQueue<Baggage> baggageQueue;
	
	private boolean bagScanReady;
	private boolean bodyScanReady;
	
	public Queue(int stationNumber, ActorRef bodyScan, ActorRef bagScan, ActorRef terminal){
		super(ActorFactory.QUEUE_SPACE, terminal);
		this.stationNumber = stationNumber;
		baggageScanner = bagScan;
		bodyScanner = bodyScan;
		personQueue = new ConcurrentLinkedQueue();
		baggageQueue = new ConcurrentLinkedQueue();
		bodyScanReady = true;
		bagScanReady = true;
	}
	
	
	public void onReceive(Object message){
		if (message instanceof Person) {
			Person p = (Person)message;
			printToTerminal("Person: " + p.getPersonId() + "enters queue " 
					+ stationNumber + ".");
			queuePersonInLine( (Person)message );
		}
		else if( message instanceof NextBag){

		}
		else if( message instanceof NextBody){
			baggageScanner.tell((EndDay)message);
			bodyScanner.tell((EndDay)message);
			getContext().stop();
		}
		else if( message instanceof EndDay){
			baggageScanner.tell((EndDay)message);
			bodyScanner.tell((EndDay)message);
			getContext().stop();
		}else{
			System.err.println("Queue recieved invalid message: " + 
					message.toString());
		}
	}

	@Override
	public void postStop() {
		printToTerminal( "Queue " + stationNumber + "Closed" );
	}
	
	public boolean queuePersonInLine(Person p){
		boolean personStatus, bagStatus = false;
		personStatus = personQueue.add(p);
		bagStatus = baggageQueue.add(p.getBaggage());
		return (personStatus && bagStatus);
	}
	
	/**
	 * Sends a person object to the bag scanner
	 */
	public void sendToBagScan(Baggage bags){
		baggageScanner.tell(bags);
	}
	
	/**
	 * Sends a person object to the body scanner
	 */
	public void sendToBodyScan(Person per) {
		bodyScanner.tell(per);
	}
}
