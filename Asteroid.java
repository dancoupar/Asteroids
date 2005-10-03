
/**
 * The class which defines the blueprint for an asteroid object.
 * Asteroid objects inherit all properties from SpaceObject.class
 * but also hold characteristics defined in this class.
 *
 * @author Dan Coupar
 */

public class Asteroid extends SpaceObject {

    /**
     * Int to hold the radius of the asteroid since they vary in size.
     */
    private int radius;

    /**
     * Constructor for an asteroid. Randomly sets velocity and rotate speed.
     * @param an int holding the radius of the asteroid to be created.
     * @param the horizontal start location of the asteroid to be created.
     * @param the vertical start location of the asteroid to be created.
     */
    public Asteroid(int radius, double startLocation_x, double startLocation_y) {

	// Call constructor in parent class
	super(startLocation_x, startLocation_y);

	// Set velocity (property held in parent class)
	super.setVelocity_x(4* Math.random() - 2);
	super.setVelocity_y(4* Math.random() - 2);

	// Set rotate speed
	super.setRotateSpeed((int)(20* Math.random() - 10));
	this.radius = radius;
    }

    /**
     * Accessor method to get the radius of the asteroid.
     * @return the radius of the asteroid
     */
    public int getRadius() {

	return radius;
    }
}