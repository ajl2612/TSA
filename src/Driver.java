import java.util.Random;

import akka.actor.ActorRef;

/**
 * Main class for TSA system
 *
 * @author Andrew
 */
public class Driver {

	
	private static int numPeople;
	
	private static final int MAX_PEOPLE = 10000;
	
	private static int numSecurityLines;
	
	private static final int MAX_SECURITY_LINES = 1000;
	
	private static int maxNumBags;
	
	private static final int MAX_BAGS_PER_PERSON = 10;
	
	public static void main(String[] args){	
		
		
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
	
		if( args.length == 4)
			fileName = args[3];
		
		ActorRef terminal = ActorFactory.makeTerminal( fileName );
		
		ActorRef jail = ActorFactory.makeJail(terminal, numSecurityLines);
		
		ActorRef[] queues = new ActorRef[numSecurityLines];
		for(int i=1; i<=numSecurityLines; i++){
			queues[i-1] = ActorFactory.makeSecurityStation(terminal, i, jail);
		}
		ActorRef docCheck = ActorFactory.makeDocumentChecker(terminal, queues);
		docCheck.start();
		
		Random r = new Random();
		for( int i = 1; i <= numPeople; i++){
			docCheck.tell(new Person(i,r.nextInt(maxNumBags+1)));
		}
		
		docCheck.tell( new EndDay());
		
		
	}
	
}
