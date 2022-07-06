
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
    private double xLocation;

    /**
     * Value to hold the y coordinate of the object.
     */
    private double yLocation;

    /**
     * Value to hold the horizontal component of the objects velocity.
     */
    private double xVelocity;

    /**
     * Value to hold the vertical component of the objects velocity.
     */
    private double yVelocity;

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
    private boolean exists;

    /**
     * Constructor which creates a new generic space object.
     * @param a double holding the initial horizontal position of the object.
     * @param a double holding the initial vertical position of the object.
     */
    public SpaceObject(double xStartLocation, double yStartLocation) {
	    this.xLocation = xStartLocation;
	    this.yLocation = yStartLocation;
	    this.exists = true;
    }

    /**
     * Accessor method to get the horizontal position of the object.
     * @return the horizontal position of the object.
     */
    public double getXLocation() {
	    return this.xLocation;
    }

    /**
     * Mutator method to set the horizontal position of the object.
     * @param a double holding the new value for the horizontal position.
     */
    public void setXLocation(double xLocation) {
	    this.xLocation = xLocation;
    }

    /**
     * Accessor method to get the vertical position of the object.
     * @return the vertical position of the object.
     */
    public double getYLocation() {
	    return this.yLocation;
    }

    /**
     * Mutator method to set the vertical position of the object.
     * @param a double holding the new value for the vertical position.
     */
    public void setYLocation(double yLocation) {
	    this.yLocation = yLocation;
    }

    /**
     * Accessor method to get the horizontal component of velocity.
     * @return the horiztonal component of velocity.
     */
    public double getXVelocity() {
	    return this.xVelocity;
    }

    /**
     * Mutator method to set the horizontal component of velocity.
     * @param a double holding the new value for the horizontal velocity.
     */
    public void setXVelocity(double xVelocity) {
	    this.xVelocity = xVelocity;
    }

    /**
     * Accessor method to get the vertical component of velocity.
     * @return the vertical component of velocity.
     */
    public double getYVelocity() {
	    return this.yVelocity;
    }

    /**
     * Mutator method to set the vertical component of velocity.
     * @param a double holding the new value for the vertical velocity.
     */
    public void setYVelocity(double yVelocity) {
	    this.yVelocity = yVelocity;
    }

    /**
     * Accessor method to get the angle at which the object is rotated.
     * @return the angle of rotation.
     */
    public int getRotation() {
	    return this.rotation;
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
    public boolean getExists() {
	    return this.exists;
    }

    /**
     * Mutator method to set the flag indicating whether the object should
     * appear on screen.
     * @param a boolean holding the new value for the flag.
     */
    public void setExists(boolean exist) {
	    this.exists = exist;
    }

    /**
     * Method to change the horizontal location of the object. This
     * method can be called as a means of getting the an object to
     * move across the screen.
     * Also ensures the object does not leave the screen by calling
     * the checkBounds() in this class.
     */
    public void updateXLocation() {
	    // keep object within horizontal bounds
	    this.xLocation = checkBounds(this.xLocation + this.xVelocity);
    }

    /**
     * Method to change the vertical location of the object. Works
     * in exactly the same way as updateXLocation().
     */
    public void updateYLocation() {
	    // keep object within vertical bounds
    	this.yLocation = checkBounds(yLocation + yVelocity);
    }

    /**
     * Method to continually update the rotation of a space
     * object. Rotation is updated by adding the rotate speed
     * each time the method is called.
     */
    public void updateRotation() {
    	this.rotation = this.rotation + this.rotateSpeed;
	    if (this.rotation == 360) {
	        this.rotation = 0;
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
    public static double checkBounds(double location) {
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
     */
    public void initRandomObject() {
        // left edge
        int side = (int)(4* Math.random());
        if (side == 1) {
            this.xLocation = 0;
            this.yLocation = 300* Math.random();
            this.xVelocity = Math.random();
            this.yVelocity = 2* Math.random() - 1;
        }
        else {
            // top edge	
            if (side == 2) {
                this.xLocation = 300* Math.random();
                this.yLocation = 300;
                this.xVelocity = 2* Math.random() - 1;
                this.yVelocity = -1* Math.random();
            }
            else {
                // right edge
                if (side == 3) {
                    this.xLocation = 300;
                    this.yLocation = 300* Math.random();
                    this.xVelocity = -1* Math.random();
                    this.yVelocity = 2* Math.random() - 1;
                }
                // bottom edge
                else {
                    this.xLocation = 300* Math.random();
                    this.yLocation = 0;
                    this.xVelocity = 2* Math.random() - 1;
                    this.yVelocity = Math.random();
                }
            }
        }
    }		    		
}

