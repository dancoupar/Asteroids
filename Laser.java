
/**
 * The class which creates and holds properties for the laser
 * objects which are fired by the goodie and baddie ships.
 * The spaceship object inherits all properties from SpaceObject.class
 * but also holds characteristics defined in this class.
 *
 * @author Dan Coupar
 */

public class Laser extends SpaceObject {

    private double PI = 3.1415926535;

    /**
     * Double which holds the overall distance the laser has travelled
     * across the screen.
     */ 
    private double distanceTravelled;

    /**
     * Constructor for the laser object. Calls the constructor in the
     * parent class which sets the starting location of the laser.
     */
    public Laser(double startLocation_x, double startLocation_y) {
    	super(startLocation_x, startLocation_y);
    }

    /**
     * Mutator method to set the total distance travelled by the laser.
     * @param an int holding the new value for the distance travelled.
     */
    public void setDistanceTravelled(int distanceTravelled) {
	    this.distanceTravelled = distanceTravelled;
    }

    /**
     * Initialisation method which sets some basic properties for a
     * laser which has just been fired.
     */
    public void laserFire(double location_x, double location_y) {
	    // Set starting location
	    super.setLocation_x(location_x);
	    super.setLocation_y(location_y);
	    // Set velocity (fixed)
	    super.setVelocity_x(14* Math.sin(super.getRotation() * ((2* PI) / 360)));
	    super.setVelocity_y(14* Math.cos(super.getRotation() * ((2* PI) / 360)));
    }

    /**
     * Method to check the distance travelled by the laser has not
     * exceeded its limit (roughly the distance of the screen).
     * @return true if the laser has not yet exceeded its maximum
     * distance, false otherwise.
     */
    public boolean checkDistance() {
        if (distanceTravelled < 285) {
            // Increment distance travelled by laser
            distanceTravelled = distanceTravelled + 15;
        }
        else {
            // Reset the distance travelled
            distanceTravelled = 0;
            return false;
        }
        return true;
    }
}