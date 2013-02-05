/**
 * Container class for person information. Used to pass data between actors.
 * 
 * @author Chris
 * @author Carol
 * @autor Andrew Lyne III
 *
 */
public class Person {
	/**
	 * Unique int ID associated with this Person.
	 */
	private final int personId;
	/**
	 * Container class representing the number of bags this person has. 
	 */
	private final Baggage bags;
  
	/**
	 * Constructor for Person. 
	 * 
	 * @param personId - Unique ID associated with this person.
	 * @param numberOfBags - Number of bags this Person has.
	 */
    public Person(int personId, int numberOfBags) {
            this.personId = personId;
            bags = new Baggage(numberOfBags, this);
    }
    
    /**
     * Accessor for PersonID
     * @return peronId
     */
    public int getPersonId() {
            return personId;
    }
    
    /**
     * Accessor for Baggage
     * @return Baggage associated with this Person
     */
    public Baggage getBaggage() {
            return bags;
    }
   
    /**
     * Function for comparing if two People are the same. Two people are 
     * considered the sameif they have the same personId.
     * 
     * @return True if people are equal, false otherwise.
     */
    public boolean equals( Object other){
    	if( other instanceof Person){
    		return personId == ((Person)other).getPersonId();
    	}
    	return false;
    }
}
