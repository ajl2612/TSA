import java.util.Random;

import akka.actor.ActorRef;

/**
 * Main class for TSA system
 *
 * @author Andrew
 */
public class Driver {

	/**
	 * Maximum number people the system can run with. All values input above 
	 * this will be defaulted to this value.  
	 */
	private static final int MAX_PEOPLE = 10000;
	
	/**
	 * Maximum number of security lines the system can run with. All values 
	 * input above this will be defaulted to this value.  
	 */
	private static final int MAX_SECURITY_LINES = 1000;
	
	/**
	 * Maximum number of bags per person the system can run with. All values 
	 * input above this will be defaulted to this value.  
	 */
	private static final int MAX_BAGS_PER_PERSON = 10;
	
	/*
	 * Local variables
	 */
	private static int numPeople;
	private static int numSecurityLines;
	private static int maxNumBags;
	
	public static void main(String[] args){	
		
		/*
		 * Check input provided by user for validity. System exits unnless 
		 * correct number of arguments present and invalid arguments are 
		 * defaulted to the maximum value for the system.
		 */
		if( args.length < 3 || args.length > 4 ){
			System.out.println("Improper usage: <numPeople> <numSecurityLines>"
		+ " <maxBagsPerPerson> <(OPTIONAL)outputFileName>");
			System.exit(1);	
		}
		String fileName = null;
		
		try{
			numPeople = Integer.parseInt(args[0]);
			if( numPeople > MAX_PEOPLE){
				throw new NumberFormatException("Too many people, using " +
						"maximum value of " + MAX_PEOPLE);
			}
		}
		catch( NumberFormatException e){
			System.err.println(e.getMessage());
			numPeople = MAX_PEOPLE;
		}
		try{
			numSecurityLines = Integer.parseInt(args[1]);
			if(numSecurityLines > MAX_SECURITY_LINES){
				throw new NumberFormatException("Too many security lines, using " +
					"maximum value of " + MAX_SECURITY_LINES);
			}
		}
		catch( NumberFormatException e){
			System.err.println(e.getMessage());
			numSecurityLines = MAX_SECURITY_LINES;	
		}
		try{
			maxNumBags = Integer.parseInt(args[2]);
			if(maxNumBags > MAX_BAGS_PER_PERSON){
				throw new NumberFormatException("Too many bags per person, using " +
					"maximum value of " + MAX_BAGS_PER_PERSON);
			}
		}
		catch( NumberFormatException e){
			System.err.println( e.getMessage());
			maxNumBags = MAX_BAGS_PER_PERSON;
			
		}
	
		/*
		 * Set file name for output if present. Otherwise STD_OUT will be used. 
		 */
		if( args.length == 4)
			fileName = args[3];
		// Create Terminal actor for printing. 
		ActorRef terminal = ActorFactory.makeTerminal( fileName );
		String toSend = "TSA running with " + numPeople + " People and " + numSecurityLines + " security lines with a maximum of " + maxNumBags + " bags per person"; 
		Message mess1 = new Message(toSend, 0);
		terminal.tell( mess1 );
		
		// Create jail.
		ActorRef jail = ActorFactory.makeJail(terminal, numSecurityLines);
		
		// Create list of security lines.
		ActorRef[] queues = new ActorRef[numSecurityLines];
		for(int i=1; i<=numSecurityLines; i++){
			queues[i-1] = ActorFactory.makeSecurityStation(terminal, i, jail);
		}
		// Create document checker. 
		ActorRef docCheck = ActorFactory.makeDocumentChecker(terminal, queues);
		docCheck.start();
		
		Random r = new Random();
		
		// Send people to the document checker to be processed
		for( int i = 1; i <= numPeople; i++){
			docCheck.tell(new Person(i,r.nextInt(maxNumBags+1)));
		}
		
		// Send shutdown message to system.
		docCheck.tell( new EndDay());
		
		
	}
	
}
