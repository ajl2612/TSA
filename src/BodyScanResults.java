/**
 * Container class for Person and their BodyScan results.
 * 
 * @author Andrew Lyne
 */
public class BodyScanResults {
	
	/**
	 * Person who has just completed BodyScan
	 */
	private final Person guest;
	
	/**
	 * Results of BodyScan
	 */
	private final boolean didPass;
	
	/**
	 * Constructor for BodyScanResults
	 * 
	 * @param p - Person whose results are contained here
	 * @param status - results of the body scan
	 */
	public BodyScanResults(Person p, boolean status){
		guest = p;
		didPass = status;
	}
	
	/**
	 * Accessor for status of BodyScancan 
	 * @return
	 */
	public boolean getStatus(){
		return didPass;
	}

	/**
	 * Accessor for Person
	 * @return
	 */
	public Person getPerson(){
		return guest;
	}
}
