import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import static akka.actor.Actors.actorOf;

/**
 * Stub
 * 
 * @author Chris
 * @author Carol
 *
 */
public class Security extends UntypedActor {
	
	Person person;
	boolean passCheck;
	
    List<Person> jail = new ArrayList<Person>();
    HashMap<Person, Boolean> awaiting = new HashMap<Person, Boolean>();
	
    /*
     * This will need to be reworked as actors cannot receive messages
     * simultaneously. Instead, Security will accept a message formed
     * as a struct which points to the references of the Person (sent
     * from BodyScanner) and the Person's bags (sent from BagScanner)
     */
	public void onReceive(Object message) throws Exception{
		if (message instanceof Boolean) {
			passCheck = (Boolean)message2;
		}
		
		if (message instanceof Person) {
			person = (Person)message1;
			if(awaiting.containsKey(person.getPersonId())) {
				if (awaiting.get(person.getPersonId()) && passCheck) {
					System.out.println("Person: " + person.getPersonId() + " has passed security.");
				}
				else {
					sendToJail(person);
				}
			}
			else {
				awaiting.put(person, passCheck);
			}
		}
	}
	
	public void sendToJail(Person person){
		jail.add(person);
		System.out.println("Person: " + person.getPersonId() + " has been sent to jail.");
	}
}
