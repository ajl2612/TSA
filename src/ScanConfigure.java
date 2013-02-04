import akka.actor.ActorRef;

	/**
	*  Container class for TSA initilization information.
	*  Serves as container for Security guard.    
	*
	*  @ChillBroChill Andrew Lyne	
	*/

public class ScanConfigure{
	
	private final ActorRef guard;

	public ScanConfigure( ActorRef act){
		guard = act;
	}
	
	public ActorRef getSecurity(){
		return guard;
	}
	
	
}