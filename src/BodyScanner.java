import java.util.Random;
import akka.actor.ActorRef;

/**
 * BodyScanner responsible for receiving Persons, processing them and assigning 
 * a value of pass or fail.   
 * 
 * @author Chris
 * @author Carol
 * @author Andrew Lyne
 *
 */
public class BodyScanner extends AbstractActor {
	
	/**
	 * Time required to check a single person in milliseconds.. 
	 */
	private int CHECK_TIME = 1000;
	
	/**
	 * Percent chance out of 100 that a given bag item will fail the document
	 * check.
	 */
	private final int PERCENT_FAIL = 20;
	
	/**
	 * Integer representing what number security station this BodyScanner 
	 * belongs to.
	 */
	private final int stationNumber;
	
	/**
	 * Security Actor to send results of scans to. 
	 */
	private ActorRef security;
	
	/**
	 * Constructor for BodyScanner
	 * 
	 * @param stationNumber - what number security station this scanner 
	 * 		belongs to 
	 * @param security - Security Actor to send results to
	 * @param terminal - Terminal Actor to send log messages to
	 */
	public BodyScanner(int stationNumber, ActorRef security, ActorRef terminal){
		super(ActorFactory.SCAN_SPACE, terminal);
		this.security = security;
		this.stationNumber = stationNumber;
	}
	
	/**
	 * Redefinition of OnRecieve method from Actor. This class handles messages
	 * of EndDay and Baggage types.  
	 */
	public void onReceive(Object message) throws Exception {
		/*
		 * If instance of Person, scan the person. Report the findings to the security guard.
		 */
		if (message instanceof Person){
			BodyScanResults results;
			boolean didPass = false;
			try{
				didPass = checkPerson();
			}catch( InterruptedException e){
				System.err.println("Body Inspection Interrupted: Automatic Fail");
				didPass = false;
			}finally{
				results = new BodyScanResults((Person)message, didPass);
				security.tell(results);
			}
		}
		/*
		 * If instance of EndDay pass message along to Security
		 */
		else if( message instanceof EndDay){
			printToTerminal("BodyScanner " + stationNumber + 
					" recieved and sent end of day message to security");

			getContext().stop();
		}
		/*
		 * All other messages are errors. Message printed here for debugging
		 * purposes.
		 */
		else{
			System.err.println("BodyScan recieved invalid message: " + 
					message.toString());
		}
	}
	
	/**
	 * Override of default stop function in actor. Prints a message to 
	 * Terminal actor before shutting down.  
	 */
	@Override
	public void postStop() {
		printToTerminal( "Body Scanner " + stationNumber + " Closed" );
		security.tell(new EndDay());
	}
	
	/**
	 * Override of default start function in actor. Prints a message to 
	 * Terminal Actor out upon start up.  
	 */
	@Override
	public void preStart() {
		printToTerminal("BodyScanner " + stationNumber + " Online");
	}
	
	/**
	 * Checks the person for problems 
	 * 
	 * @return - true if person passes check, false otherwise
	 * @throws InterruptedException
	 */
	public boolean checkPerson() throws InterruptedException{
		Random r = new Random();
		Thread.sleep(CHECK_TIME);
		return (r.nextInt(100) < PERCENT_FAIL);
	}
	
	/**
	 * Accessor for which security station this scanner belongs to. 
	 * @return - station number
	 */
	public int getStationNumber(){
		return stationNumber;
	}
}
