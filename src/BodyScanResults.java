
public class BodyScanResults {
	
	private final Person guest;
	private final boolean didPass;
	
	public BodyScanResults(Person p, boolean status){
		guest = p;
		didPass = status;
	}
	
	public boolean getStatus(){
		return didPass;
	}

	public Person getPerson(){
		return guest;
	}
}
