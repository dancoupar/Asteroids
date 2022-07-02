
/**
 * The class which creates and holds properties for the powerup
 * objects which randomly appear during the course of play.
 * The powerup object inherits all properties from SpaceObject.class
 * but also holds characteristics defined in this class.
 *
 * @author Dan Coupar
 */

public class PowerUp extends SpaceObject {

    /**
     * Int to hold a value which represents the type of powerup.
     */
    private int powerUpType;

    /**
     * Constructor for the powerup object.
     */
    public PowerUp() {
	    // Call constructor in parent class
	    super(0,0);
	    // Override exist flag which is set to true in super constructor
	    setExist(false);
	    // Initialise properties for the powerup object
	    initRandomObject();
    }

    /**
     * Accessor method to get the powerup type.
     * @return an int holding a value representing the type of powerup.
     */
    public int getPowerUpType() {
	    return powerUpType;
    }

    /**
     * Mutator method to set the flag indicating whether the powerup
     * object should be shown or not.
     * Overrides method in parent class.
     */
    public void setExist(boolean exist) {
	    super.setExist(exist);
	    // Randomly set powerup type each time exist flag is set to true
	    if (super.getExist()) {
	        powerUpType = (int)(4* Math.random());
	    }
    }
}