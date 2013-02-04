import java.util.concurrent.ConcurrentMap;
import akka.actor.ActorRef;

/**
 * Stub
 * 
 * @author Chris
 * @author Carol
 *
 */
public class Security extends AbstractActor {
	
	private final ActorRef jail;
	private final int stationNumber;
	
	private int numScannersClosed;
	private ConcurrentMap<Person,Boolean> awaitingBaggage;
	private ConcurrentMap<Person,Boolean> awaitingOwners;
	
    public Security(int stationNumber, ActorRef jail, ActorRef terminal){
    	super(ActorFactory.SECURITY_SPACE, terminal);
		this.jail = jail;
		this.stationNumber = stationNumber;
		numScannersClosed = 0;
    }
    
    /*
     * This will need to be reworked as actors cannot receive messages
     * simultaneously. Instead, Security will accept a message formed
     * as a struct which points to the references of the Person (sent
     * from BodyScanner) and the Person's bags (sent from BagScanner)
     */
	public void onReceive(Object message) throws Exception{
		
		if (message instanceof BodyScanResults) {
			//do SWAG
		}else if( message instanceof BagScanResults){
			//do SWAG
		}else if( message instanceof EndDay){
			if(++numScannersClosed == 2){
				jail.tell((EndDay)message);
				getContext().stop();
			}
		}else{
			System.err.println("Security recieved invalid message: " + 
					message.toString());
		}
	}
	
	@Override
	public void postStop() {
		printToTerminal( "Security " + stationNumber + "Closed" );
	}
	
	public void sendToJail(Person person){
		
		printToTerminal("Person: " + person.getPersonId() + " has been sent to jail.");
	}
}
