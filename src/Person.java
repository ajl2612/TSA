/**
 * Container class for person information. Used to pass data between actors.
 * 
 * @author Chris
 * @author Carol
 * @IRONMAN Andrew Lyne III
 *
 */
public class Person {
	private final int personId;
	private final Baggage bags;
  
    public Person(int personId, int numberOfBags) {
            this.personId = personId;
            bags = new Baggage(numberOfBags, this);
    }
    
    public int getPersonId() {
            return personId;
    }
    
    public Baggage getBaggage() {
            return bags;
    }
    
    public boolean equals( Object other){
    	if( other instanceof Person){
    		return personId == ((Person)other).getPersonId();
    	}
    	return false;
    }
}
