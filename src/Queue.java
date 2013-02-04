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
	
	private ConcurrentLinkedQueue<Person> personQueue;
	private ConcurrentLinkedQueue<Baggage> baggageQueue;
	
	private boolean bagScanReady;
	private boolean bodyScanReady;
	
	public Queue(int stationNumber, ActorRef bodyScan, ActorRef bagScan, ActorRef terminal){
		super(ActorFactory.QUEUE_SPACE, terminal);
		baggageScanner = bagScan;
		bodyScanner = bodyScan;
		personQueue = new ConcurrentLinkedQueue();
		baggageQueue = new ConcurrentLinkedQueue();
	}
	
	
	public void onReceive(Object message){
		if (message instanceof Person) {
			Person p = (Person)message;
			sendToBodyScan(p);
			sendToBagScan(p.getBaggage());
		}
		else if( message instanceof EndDay){
			baggageScanner.tell((EndDay)message);
			bodyScanner.tell((EndDay)message);
			System.out.println("Queue shutting down" );
		}else{
			System.err.println("Queue recieved invalid message: " + 
					message.toString());
		}
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
