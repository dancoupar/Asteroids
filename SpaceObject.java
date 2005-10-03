
/**
 * The class which defines the properties of a generic object which
 * floats about the screen. All objects in the game inherit these
 * properties.
 *
 * @author Dan Coupar
 */

public class SpaceObject {

    /**
     * Value to hold the x coordinate of the object.
     */
    private double location_x;

    /**
     * Value to hold the y coordinate of the object.
     */
    private double location_y;

    /**
     * Value to hold the horizontal component of the objects velocity.
     */
    private double velocity_x;

    /**
     * Value to hold the vertical component of the objects velocity.
     */
    private double velocity_y;

    /**
     * Integer value to hold the angle of rotation of the object.
     */
    private int rotation;

    /**
     * Int to hold the speed at which the object rotates. (Random)
     * This value can be negative, indicating an anti-clockwise spinning
     * object.
     */
    private int rotateSpeed;

    /**
     * Flag to indicate whether the object should be displayed or not.
     */
    private boolean exist;

    /**
     * Constructor which creates a new generic space object.
     * @param a double holding the initial horizontal position of the object.
     * @param a double holding the initial vertical position of the object.
     */
    public SpaceObject(double startLocation_x, double startLocation_y) {

	location_x = startLocation_x;
	location_y = startLocation_y;
	exist = true;
    }

    /**
     * Accessor method to get the horizontal position of the object.
     * @return the horizontal position of the object.
     */
    public double getLocation_x() {

	return location_x;
    }

    /**
     * Mutator method to set the horizontal position of the object.
     * @param a double holding the new value for the horizontal position.
     */
    public void setLocation_x(double location_x) {

	this.location_x = location_x;
    }

    /**
     * Accessor method to get the vertical position of the object.
     * @return the vertical position of the object.
     */
    public double getLocation_y() {

	return location_y;
    }

    /**
     * Mutator method to set the vertical position of the object.
     * @param a double holding the new value for the vertical position.
     */
    public void setLocation_y(double location_y) {

	this.location_y = location_y;
    }

    /**
     * Accessor method to get the horizontal component of velocity.
     * @return the horiztonal component of velocity.
     */
    public double getVelocity_x() {

	return velocity_x;
    }

    /**
     * Mutator method to set the horizontal component of velocity.
     * @param a double holding the new value for the horizontal velocity.
     */
    public void setVelocity_x(double velocity_x) {

	this.velocity_x = velocity_x;
    }

    /**
     * Accessor method to get the vertical component of velocity.
     * @return the vertical component of velocity.
     */
    public double getVelocity_y() {

	return velocity_y;
    }

    /**
     * Mutator method to set the vertical component of velocity.
     * @param a double holding the new value for the vertical velocity.
     */
    public void setVelocity_y(double velocity_y) {

	this.velocity_y = velocity_y;
    }

    /**
     * Accessor method to get the angle at which the object is rotated.
     * @return the angle of rotation.
     */
    public int getRotation() {

	return rotation;
    }

    /**
     * Mutator method to set the angle at which the object is rotated.
     * @param an int holding the new angle of rotation.
     */
    public void setRotation(int rotation) {

	this.rotation = rotation;
    }

    /**
     * Mutator method to set the rotate speed of the object.
     * @return the rotate speed of the space object.
     */
    public void setRotateSpeed(int rotateSpeed) {

	this.rotateSpeed = rotateSpeed;
    }

    /**
     * Accessor method to get the flag indicating whether the object should
     * appear on screen.
     * @return the flag indicating whether the object should be displayed.
     */
    public boolean getExist() {

	return exist;
    }

    /**
     * Mutator method to set the flag indicating whether the object should
     * appear on screen.
     * @param a boolean holding the new value for the flag.
     */
    public void setExist(boolean exist) {

	this.exist = exist;
    }

    /**
     * Method to change the horizontal location of the object. This
     * method can be called as a means of getting the an object to
     * move across the screen.
     * Also ensures the object does not leave the screen by calling
     * the checkBounds() in this class.
     */
    public void updateLocation_x() {

	location_x = location_x + velocity_x;

	// keep object within horizontal bounds
	location_x = checkBounds(location_x);
    }

    /**
     * Method to change the vertical location of the object. Works
     * in exactly the same way as updateLocation_x().
     */
    public void updateLocation_y() {

	location_y = location_y + velocity_y;

	// keep object within vertical bounds
	location_y = checkBounds(location_y);
    }

   /**
     * Method to continually update the rotation of a space
     * object. Rotation is updated by adding the rotate speed
     * each time the method is called.
     */
    public void updateRotation() {

	rotation = rotation + rotateSpeed;

	if (rotation == 360) {

	   rotation = 0;
	}
    }

    /**
     * Method to keep a space object within the confines of the screen.
     * Can operate on vertical or horizontal locations since the screen
     * is square.
     * @param a double holding the vertical or horizontal location of the
     * object.
     * @return the new vertical or horizontal location of the object.
     */
    public double checkBounds(double location) {

	if (location > 300) {
	    
	    location = 0;
	}

	else {
		
	    if (location < 0) {
		    
		location = 300;
	    }
	}

	return location;
    }

    /**
     * Method which can be called to set some standard characteristics
     * for a random object. A Random object can only start at the edge
     * of the screen, and has a random velocity which moves the object
     * away from that edge.
     * 
     */
    public void initRandomObject() {

	// left edge
	int side = (int)(4* Math.random());

	if (side == 1) {

	    location_x = 0;
	    location_y = 300* Math.random();

	    velocity_x = Math.random();
	    velocity_y = 2* Math.random() - 1;
	}
	
	else {

	    // top edge	
	    if (side == 2) {

		location_x = 300* Math.random();
		location_y = 300;

		velocity_x = 2* Math.random() - 1;
		velocity_y = -1* Math.random();
	    }

	    else {

		// right edge
		if (side == 3) {

		    location_x = 300;
		    location_y = 300* Math.random();

		    velocity_x = -1* Math.random();
		    velocity_y = 2* Math.random() - 1;
		}

		// bottom edge
		else {

		    location_x = 300* Math.random();
		    location_y = 0;

		    velocity_x = 2* Math.random() - 1;
		    velocity_y = Math.random();
		}
	    }
	}
    }		    		
}

