
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
		super.setExists(false);
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
		if ((!baddieLaser.getExists()) && (Math.random() > 0.99)) {
			// Targetting algorithm for shooting at goodie ship
			double xDiff;
			double yDiff;
			int random = (int)(20* Math.random() - 10);
			// If enemy ship is left of goodie ship
			if (ship.getXLocation() > super.getXLocation()) {
				// Calculate horizontal difference
				xDiff = ship.getXLocation() - super.getXLocation();
				// If enemy ship is above goodie ship
				if (ship.getYLocation() > super.getYLocation()) {
					// Calculate vertical difference
					yDiff = ship.getYLocation() - super.getYLocation();
					// Set rotation of laser pointing towards goodie ship
					baddieLaser.setRotation((int)((Math.atan(xDiff / yDiff)) * (360 / (2* PI))) + random);
				}
				else { // Enemy ship is below goodie ship
					// Calculate vertical difference
					yDiff = super.getYLocation() - ship.getYLocation();
					// Set rotation of laser pointing toward goodie ship
					baddieLaser.setRotation((int)((Math.atan(yDiff / xDiff)) * (360 / (2* PI))) +
					random + 90);
				}
			}
			else { // Enemy ship is to the right of goodie ship
				// Calculate horizontal difference
				xDiff = super.getXLocation() - ship.getXLocation();
				// If enemy ship is below goodie ship
				if (ship.getYLocation() < super.getYLocation()) {
					// Calculate vertical difference
					yDiff = super.getYLocation() - ship.getYLocation();
					// Set rotation of laser pointing toward goodie ship
					baddieLaser.setRotation((int)((Math.atan(xDiff / yDiff)) * (360 / (2* PI))) +
					random + 180);
				}
				else { // ship is above goodie ship
					// Calculate vertical difference
					yDiff = ship.getYLocation() - super.getYLocation();
					// Set rotation of laser pointing toward goodie ship
					baddieLaser.setRotation((int)((Math.atan(yDiff / xDiff)) * (360 / (2* PI))) +
					random + 270);
				}
			}
			// Set to true to indicate enemy ship is firing
			baddieLaser.setExists(true);
			// Method to set initial properties for the baddie laser  
			baddieLaser.laserFire(super.getXLocation(), super.getYLocation());
		}
    }
}