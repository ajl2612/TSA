import java.util.ArrayList;
import akka.actor.ActorRef;
import akka.actor.Actors;


public class Jail extends AbstractActor {
	
	private ArrayList<Person> jailed;
	private final int numSecurityStations;
	
	private int numStationsClosed;
	
	public Jail(ActorRef terminal, int numSecurityStations){
    	super(ActorFactory.JAIL_SPACE, terminal);
		jailed = new ArrayList<Person>();
		this.numSecurityStations = numSecurityStations;
		numStationsClosed = 0;
    }
	
public void onReceive(Object message) throws Exception{
		
		if (message instanceof Person) {
			jailed.add((Person)message);
		}else if( message instanceof EndDay){
			if(++numStationsClosed == numSecurityStations){
				printJailed();
				Actors.registry().shutdownAll();
			}
		}else{
			System.err.println("Security recieved invalid message: " + 
					message.toString());
		}
	}
	
	@Override
	public void postStop() {
		printToTerminal( "Jail Closed" );
	}
	
	public void printJailed(){
		String manifest = "Passengers Detained\n";
		String spacing = "";
		for(int i=0; i<terminalSpacing; i++){
			spacing.concat("");
		}
		String nextLine = ""; 
		for( Person p : jailed){
			nextLine = spacing + "- Person" + p.getPersonId() + "\n";
			manifest.concat(nextLine);
		}
		printToTerminal(manifest);
	}

}
