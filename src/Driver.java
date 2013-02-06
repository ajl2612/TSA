import java.util.Random;

import akka.actor.ActorRef;

/**
 * Main class for TSA system
 *
 * @author Andrew
 */
public class Driver {

	private static int numSecurityLines;
	
	private static int numPeople;
	
	private static int maxNumBags;
	
	public static void main(String[] args){	
		
		
		if( args.length < 3 || args.length > 4 ){
			System.out.println("Improper usage: <numPeople> <numSecurityLines>"
		+ " <maxBagsPerPerson> <(OPTIONAL)outputFileName>");
			System.exit(1);	
		}
		String fileName = null;
		
		numPeople = Integer.parseInt(args[0]);
		numSecurityLines = Integer.parseInt(args[1]);
		maxNumBags = Integer.parseInt(args[2]);
	
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
