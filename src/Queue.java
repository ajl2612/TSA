import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

	final ActorRef queueBag; 
	final ActorRef queueBody; 
	
	public Queue(){
		queueBag = actorOf(BagScanner.class).start();
		queueBody = actorOf(BodyScanner.class).start();
		security = actorOf(Security.class).start();
		ScanConfigure conf = new ScanConfigure( security );
		queueBag.tell(conf);
		queueBody.tell(conf);
	}
	
	
	public void onReceive(Object message) {
		if (message instanceof Person) {
			Person p = (Person)message;
			sendToBodyScan(p);
			sendToBagScan(p.getBaggage());
		}
		else if( message instanceof EndDay){
			queueBag.tell((EndDay)message);
			queueBody.tell((EndDay)message);
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
		queueBag.tell(bags);
	}
	
	/**
	 * Sends a person object to the body scanner
	 */
	public void sendToBodyScan(Person per) {
		queueBody.tell(per);
	}
}
