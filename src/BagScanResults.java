/**
 * Container class for Perons's Baggage and their BagScan results.
 * 
 * @author Andrew Lyne
 */

public class BagScanResults {
	
	/**
	 * Bags associated with this scan result
	 */
	private final Baggage bags;
	
	/**
	 * results of this BaggageScan
	 */
	private final boolean didPass;
	
	/**
	 * Constructor for BaggageScanResults
	 * 
	 * @param b - b=Baggage
	 * @param status - results of Baggage scan
	 */
	public BagScanResults(Baggage b, boolean status){
		bags = b;
		didPass = status;
	}
	
	/**
	 * Accessor for status of BagScancan 
	 * @return
	 */
	public boolean getStatus(){
		return didPass;
	}

	/**
	 * Accessor for Person
	 * @return
	 */
	public Person getOwner(){
		return bags.getOwner();
	}
}