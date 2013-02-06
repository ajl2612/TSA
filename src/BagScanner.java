import java.util.Random;
import akka.actor.ActorRef;

/**
 * BagScanner responsible for receiving Baggage, processing it and assigning 
 * a value of pass or fail to each Baggage unit. Baggage with multiple bags 
 * inside are tested once per bag contained.   
 * 
 * @author Chris
 * @author Carol
 * @author Andrew Lyne
 *
 */
public class BagScanner extends AbstractActor {
	
	/**
	 * Time required to check a single bag in milliseconds.. 
	 */
	private final int CHECK_TIME = 1000;
	
	/**
	 * Percent chance out of 100 that a given bag item will fail the document
	 * check.
	 */
	private final int PERCENT_FAIL = 20;
	
	/**
	 * Integer representing what number security station this BaggageScanner 
	 * belongs to.
	 */
	private final int stationNumber;
	
	/**
	 * Security Actor to send results of scans to. 
	 */
	private ActorRef security;
	
	/**
	 * Constructor for BagScanner 
	 * 
	 * @param stationNumber - what number security station this scanner 
	 * 		belongs to 
	 * @param security - Security Actor to send results to
	 * @param terminal - Terminal Actor to send log messages to
	 */
	public BagScanner(int stationNumber, ActorRef security, ActorRef terminal){
		super(ActorFactory.SCAN_SPACE, terminal);
		this.security = security;
		this.stationNumber = stationNumber;
	}
	
	/**
	 * Redefinition of OnRecieve method from Actor. This class handles messages
	 * of EndDay and Baggage types.  
	 */
	public void onReceive(Object message){
		/*
		 * If instance of Baggage, scan the bags. Report the findings to the security guard.
		 */
		if (message instanceof Baggage){
			Baggage b = (Baggage)message;
			printToTerminal("Person " + b.getOwner().getPersonId() 
					+ "'s Baggage enters scanner");
			BagScanResults results;
			boolean didPass = false;
			try{
				didPass = checkBags(b);
			}catch( InterruptedException e){
				printToTerminal("Person " + b.getOwner().getPersonId() 
						+ "'s Bag Inspection Interrupted: Automatic Fail");
				didPass = false;
			}finally{
				results = new BagScanResults(b, didPass);
				printToTerminal("Person " + b.getOwner().getPersonId() 
						+ "'s Baggage leaves scanner");
				security.tell(results);
			}
		}
		/*
		 * If instance of EndDay pass message along to Security
		 */
		else if( message instanceof EndDay){
			printToTerminal("BodyScanner " + stationNumber + 
					" recieved and sent end of day message to security");
			security.tell((EndDay)message);
			getContext().stop();
		}
		/*
		 * All other messages are errors. Message printed here for debugging
		 * purposes.
		 */
		else{
			System.err.println("BagScan recieved invalid message: " + 
					message.toString());
		}
	}
	
	/**
	 * Override of default stop function in actor. Prints a message to 
	 * Terminal actor before shutting down.  
	 */
	@Override
	public void postStop() {
		System.out.println( "Baggage Scanner " + stationNumber + " Closed" );
	}
	
	/**
	 * Override of default start function in actor. Prints a message to 
	 * Terminal Actor out upon start up.  
	 */
	@Override
	public void preStart() {
		printToTerminal("BagScanner " + stationNumber + " Online");
	}
	
	
	/**
	 * Checks the given baggage for problems. Each bag contained in the 
	 * baggage is checked individually. If any bag in the Baggage unit fails, 
	 * the whole unit fails the check. Baggage packages with zero bags 
	 * automatically pass. 
	 * 
	 * @param bags - Baggage item to be checked
	 * @return - Boolean representing pass or fail status of Baggage package. 
	 * @throws InterruptedException
	 */
	public boolean checkBags(Baggage bags) throws InterruptedException{
		Random r  = new Random();
		boolean status = true;
		for( int i=0; i< bags.getNumBags(); i++){
			Thread.sleep(CHECK_TIME);
			if(r.nextInt(100) < PERCENT_FAIL)
				status = false;
		}
		return status;
	}
	
	/**
	 * Accessor for which security station this scanner belongs to. 
	 * @return - station number
	 */
	public int getStationNumber(){
		return stationNumber;
	}
}