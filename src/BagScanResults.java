
public class BagScanResults {
	
	private final Baggage bags;
	private final boolean didPass;
	
	public BagScanResults(Baggage b, boolean status){
		bags = b;
		didPass = status;
	}
	
	public boolean getStatus(){
		return didPass;
	}

	public Person getOwner(){
		return bags.getOwner();
	}
}