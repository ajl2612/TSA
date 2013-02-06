import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import akka.actor.ActorRef;

/**
 * Security representing the guard at the end of the security station. 
 * Receives bags and people from the scanners and places them into 
 * queues to wait for their other half to arrive. When the other half 
 * arrives, the overall status of the Person and Baggage are evaluated. 
 * If failed, they person is sent to Jail, otherwise the person is allowed 
 * to board their plane. 
 * 
 * @author Chris
 * @author Carol
 *
 */
public class Security extends AbstractActor {
	
	/**
	 * Jail to send bad people to
	 */
	private final ActorRef jail;
	
	/**
	 * Integer representing what number security station this queue belongs to.
	 */
	private final int stationNumber;
	
	/**
	 * Number of stations that have sent their closed messages to this actor
	 */
	private int numScannersClosed;
	
	/**
	 * Map of People awaiting their Baggage
	 */
	private ConcurrentMap<Person,Boolean> awaitingBaggage;
	
	/**
	 * Map of bags awaiting their People
	 */
	private ConcurrentMap<Person,Boolean> awaitingOwners;
	
	/**
	 * Constructor for Security
	 * 
	 * @param stationNumber -  what number security station this queue 
	 * 		belongs to
	 * @param jail- Jail Actor to send bad people to 
	 * @param terminal - Terminal Actor to send log messages to
	 */
    public Security(int stationNumber, ActorRef jail, ActorRef terminal){
    	super(ActorFactory.SECURITY_SPACE, terminal);
		this.jail = jail;
		this.stationNumber = stationNumber;
		awaitingBaggage = new ConcurrentHashMap<Person,Boolean>();
		awaitingOwners = new ConcurrentHashMap<Person,Boolean>();
		numScannersClosed = 0;
    }
    
	/**
	 * Redefinition of OnRecieve method from Actor. This class handles messages
	 * of EndDay, BodyScanResults and BagScanResults types.  
	 */
	@Override
	public void onReceive(Object message) throws Exception{
		/*
		 * Decouple results from Person and process
		 */
		if (message instanceof BodyScanResults) {
			receivePerson((BodyScanResults)message);
		}
		/*
		 * Decouple results from Baggage and process
		 */
		else if( message instanceof BagScanResults){
			receiveBaggage((BagScanResults)message);
		}
		/*
		 * If instance of EndDay check the number of Scanners that have 
		 * checked in. Only alert the jail of EndDay and shutdown oneself 
		 * when EndDay messages have been received from both queues. 
		 */
		else if( message instanceof EndDay){
			numScannersClosed++;
			printToTerminal("Security " + stationNumber + 
					" recieved end of day message " + numScannersClosed);
			if(numScannersClosed == 2){
				printToTerminal("Security " + stationNumber + 
						" sent end of day message to jail");
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
		printToTerminal( "Security " + stationNumber + " Closed" );
		jail.tell(new EndDay());
	}
	
	/**
	 * Override of default start function in actor. Prints a message to 
	 * Terminal Actor out upon start up.  
	 */
	@Override
	public void preStart() {
		printToTerminal("Security " + stationNumber + " Online");
	}
	
	
	/**
	 * Receives the person from BodyScanner. Checks to see if the Person's 
	 * bags are here waiting for them. If they are, collect the status from 
	 * the two scans and pass judgement on the person. Otherwise, place the 
	 * person in a queue while they wait for their bags to arrive.  
	 * 
	 * @param r - BodyScanResults from BodyScaner
	 */
	public void receivePerson(BodyScanResults r){
		Person p = r.getPerson();
		printToTerminal("BodyScan results for Person " +
				p.getPersonId() + " arrived at security");
		if(awaitingOwners.containsKey(p)){
			printToTerminal("Person "+ p.getPersonId() + 
					" has found their bags");
			boolean bagStatus = awaitingOwners.get(p);
			boolean personStatus = r.getStatus();
			awaitingOwners.remove(p);
			castJudgement(bagStatus, personStatus, p);
		}else{
			printToTerminal("Person "+ p.getPersonId() + 
					"'s bags have not yet arrived and is waiting");
			awaitingBaggage.put(p, r.getStatus());
		}
	}
	
	/**
	 * Receives the Baggage from BodyScanner. Checks to see if the Person who 
	 * owns these bags are waiting for them. If they are, collect the status 
	 * from the two scans and pass judgement on the person. Otherwise, place 
	 * the Baggage in a queue while they wait for their owner to arrive.  
	 * 
	 * @param r - BagScanResults from BagScanner
	 */
	public void receiveBaggage(BagScanResults r){
		Person p = r.getOwner();
		printToTerminal("BagScan results for Person " +
				p.getPersonId() + "'s bags arrived at security");	
		if(awaitingBaggage.containsKey(p)){
			printToTerminal("Person "+ p.getPersonId() + 
					" has found their bags");
			boolean bagStatus = awaitingBaggage.get(p);
			boolean personStatus = r.getStatus();
			awaitingBaggage.remove(p);
			castJudgement(bagStatus, personStatus, p);
		}else{
			printToTerminal("Person "+ p.getPersonId() + 
					" has not yet arrived. Bags are waiting.");
			awaitingOwners.put(p, r.getStatus());
		}
	}
	
	/**
	 * Passes judgement on the person. If they have failed either security 
	 * check, they are sent to jail. Otherwise, they are allowed to go to 
	 * their flight. 
	 * 
	 * @param bagStatus - results from BaagScanner for this person
	 * @param personStatus - Results from BodyScan for this person
	 * @param p - this person
	 */
	public void castJudgement(boolean bagStatus, boolean personStatus, Person p){
		if( bagStatus && personStatus){
			printToTerminal("Person "+ p.getPersonId() + 
					" has cleared security! Bon Voyage!");
		}
		else{
			printToTerminal("Person "+ p.getPersonId() + 
					" has failed security! To Jail!");
			jail.tell(p);
		}
	}
}
