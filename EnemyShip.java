
/**
 * The class which creates and holds properties for the baddie
 * spaceship object.
 * The baddie ship object inherits all properties from SpaceObject.class
 * but also holds characteristics defined in this class.
 *
 * @author Dan Coupar
 */

public class EnemyShip extends SpaceObject {

    private double PI = 3.1415926535;

    /**
     * Constructor for the enemy ship.
     */
    public EnemyShip() {
		// Call constructor in parent class
		super(0, 0);
		// Override exist flag which is set to true in super constructor
		super.setExist(false);
		// Initialise some basic properties in the parent class
		super.initRandomObject();
		// Set the spinning speed of the ship
		super.setRotateSpeed(8);
    }

    /**
     * Method to randomly fire the enemy ships laser at the goodie
     * ship.
     */
    public void generateFiring(Laser baddieLaser, SpaceShip ship) {
		// If baddie is not already firing and random element is true
		if ((!baddieLaser.getExist()) && (Math.random() > 0.99)) {
			// Targetting algorithm for shooting at goodie ship
			double diff_x;
			double diff_y;
			int random = (int)(20* Math.random() - 10);
			// If enemy ship is left of goodie ship
			if (ship.getLocation_x() > super.getLocation_x()) {
				// Calculate horizontal difference
				diff_x = ship.getLocation_x() - super.getLocation_x();
				// If enemy ship is above goodie ship
				if (ship.getLocation_y() > super.getLocation_y()) {
					// Calculate vertical difference
					diff_y = ship.getLocation_y() - super.getLocation_y();
					// Set rotation of laser pointing towards goodie ship
					baddieLaser.setRotation((int)((Math.atan(diff_x / diff_y)) * (360 / (2* PI))) + random);
				}
				else { // Enemy ship is below goodie ship
					// Calculate vertical difference
					diff_y = super.getLocation_y() - ship.getLocation_y();
					// Set rotation of laser pointing toward goodie ship
					baddieLaser.setRotation((int)((Math.atan(diff_y / diff_x)) * (360 / (2* PI))) +
					random + 90);
				}
			}
			else { // Enemy ship is to the right of goodie ship
				// Calculate horizontal difference
				diff_x = super.getLocation_x() - ship.getLocation_x();
				// If enemy ship is below goodie ship
				if (ship.getLocation_y() < super.getLocation_y()) {
					// Calculate vertical difference
					diff_y = super.getLocation_y() - ship.getLocation_y();
					// Set rotation of laser pointing toward goodie ship
					baddieLaser.setRotation((int)((Math.atan(diff_x / diff_y)) * (360 / (2* PI))) +
					random + 180);
				}
				else { // ship is above goodie ship
					// Calculate vertical difference
					diff_y = ship.getLocation_y() - super.getLocation_y();
					// Set rotation of laser pointing toward goodie ship
					baddieLaser.setRotation((int)((Math.atan(diff_y / diff_x)) * (360 / (2* PI))) +
					random + 270);
				}
			}
			// Set to true to indicate enemy ship is firing
			baddieLaser.setExist(true);
			// Method to set initial properties for the baddie laser  
			baddieLaser.laserFire(super.getLocation_x(), super.getLocation_y());
		}
    }
}