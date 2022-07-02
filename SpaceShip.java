
/**
 * The class which creates and holds properties for the goodie
 * spaceship object.
 * The spaceship object inherits all properties from SpaceObject.class
 * but also holds characteristics defined in this class.
 *
 * @author Dan Coupar
 */

public class SpaceShip extends SpaceObject {

    private double PI = 3.1415926535;

    /**
     * Flag to indicate whether the ship is accelerating or not.
     * Ship is accelerating if the player is pressing up.
     */
    private boolean accelerating;

    /**
     * Flag to indicate whether the ship has reached the maximum
     * speed allowed. If the flag is true the ship is not allowed
     * to increase speed.
     */
    private boolean maxSpeedReached;

    /**
     * Holds the overall speed the ship object has reached. If this
     * value reaches a certain amount the maxSpeedReached flag is
     * set to true.
     */
    private double speed;

    /**
     * Holds a value which indicates the rotating direction of the
     * ship if the player is pressing left or right.
     * Holds -1 for left, 1 for right and 0 for no rotation.
     */
    private int rotateDirection;

    /**
     * Int to hold the amount of shields the ship has remaining.
     * Decreases when ship is hit by another object.
     */
    private int shieldsRemaining = 100;

    /**
     * Flag to indicate if the shields have been recently damaged
     * or not. If the flag is true, the shields are rendered in
     * AstApplication.class
     */
    private boolean shieldsDamaged;

    /**
     * Flag to indicate if the ship is in hyperspace or not. Ship
     * is only in hyperspace if the relevant powerup has been
     * collected, and only lasts for a limited period of time.
     */
    private boolean hyperspace;

    /**
     * Flag to indicate if the ships lasers have been upgraded or
     * not. Lasers are only upgraded once the player has collected
     * the seismic laser upgrade, and only eight shots are allowed. 
     */
    private boolean laserUpgraded;

    /**
     * Integer to hold the number of shots the ship has remaining
     * when the seismic laser upgrade has been obtained.
     */
    private int shotsRemaining;

    /**
     * Int to hold the number of emergency brakes the player has
     * used.
     */
    private int brakesUsed;

    /**
     * Int to hold the total score the player has achieved.
     */
    private int score;

    /**
     * Integer which acts as a counter. Is incremented constantly
     * when ship is in hyperspace mode until a preset amount is
     * reached, when the hyperspace flag is set to false. 
     */
    private int i;

    /**
     * Constructor for the spaceship object. Simply calls the
     * constructor in the parent class which sets the starting
     * position of the ship.
     */
    public SpaceShip(double startLocation_x, double startLocation_y) {
	    super(startLocation_x, startLocation_y);	
    }

    /**
     * Accessor method to get the accelerating flag.
     * @return the flag indicating whether the ship is accelerating
     * or not.
     */
    public boolean getAccelerating() {
    	return accelerating;
    }

    /**
     * Mutator method to set the accelerating flag.
     * @param a boolean which holds the new value for the flag.
     */  
    public void setAccelerating(boolean accelerating) {
	    this.accelerating = accelerating;
    }

    /**
     * Accessor method to get the amount of shields left remaining.
     * @return the amount of shields left remaining.
     */
    public int getShieldsRemaining() {
    	return shieldsRemaining;
    }

    /**
     * Mutator method to set the amount of shields remaining.
     * @param a boolean which holds the new value for the shields.
     */ 
    public void setShieldsRemaining(int shieldsRemaining) {
	    this.shieldsRemaining = shieldsRemaining;
        // Make sure shields cannot be less than 0
        if (shieldsRemaining <= 0) {

            this.shieldsRemaining = 0;
        }
        // Make sure shields do not exceed 100
        if (shieldsRemaining > 100) {

            this.shieldsRemaining = 100;
        }
    }

    /**
     * Accessor method to get the flag indicating shield damage.
     * @return the boolean flag indicating shield damage.
     */
    public boolean getShieldsDamaged() {
	    return shieldsDamaged;
    }

    /**
     * Mutator method to set the shields damaged flag.
     * @param a boolean holding the new value for the shield
     * damage indicator.
     */
    public void setShieldsDamaged(boolean shieldsDamaged) {
	    this.shieldsDamaged = shieldsDamaged;
    }

    /**
     * Accessor method to get the flag indicating whether the ship
     * is in hyperspace or not.
     * @return the boolean indicating hyperspace or no hyperspace.
     */
    public boolean getHyperspace() {
        // Increment timer
        i++;
        // If timer exceeds limit...
        if (i > 5000) {
            // Take ship out of hyperspace
            hyperspace = false;
            i = 0;
        }
        return hyperspace;
    }

    /**
     * Mutator method to set the hyperspace flag.
     * @param a boolean holding the new value for the hyperspace
     * flag.
     */
    public void setHyperspace(boolean hyperspace) {
	    this.hyperspace = hyperspace;
    }

    /**
     * Accessor method to get the flag indicating laser upgrade.
     * @return the flag indicating whether the lasers have been
     * upgraded or not.
     */
    public boolean getLaserUpgraded() {
	    return laserUpgraded;
    }

    /**
     * Mutator method to set the flag indicating laser upgrade.
     * @return a boolean holding the new value for the laser
     * upgrade flag.
     */
    public void setLaserUpgraded(boolean laserUpgraded) {
	    this.laserUpgraded = laserUpgraded;
        // If the seismic upgrade has been collected...
        if (laserUpgraded) {
            // Set the seismic shots remaining to 8
            shotsRemaining = 8;
        }
    }

    /**
     * Accessor method to get the number of shots the ship has
     * remaining when the player has collected the seismic laser
     * upgrade.
     * @return the number of shots remaining.
     */
    public int getShotsRemaining() {
        // If player has no shots left...
        if (shotsRemaining == 0) {
            // Return ship to normal firing mode
            laserUpgraded = false;
        }
        return shotsRemaining;
    }

    /**
     * Mutator method to set the number of shots the ship has
     * remaining when the player has collected the seismic laser
     * upgrade.
     * @param an int holding the new value for the number of
     * shots remaining.
     */
    public void setShotsRemaining(int shotsRemaining) {
	    this.shotsRemaining = shotsRemaining;
    }

    /**
     * Accessor method to get the number of emergency brakes the
     * player has used.
     * @return the number of brakes the player has used.
     */
    public int getBrakesUsed() {
	    return brakesUsed;
    }

    /**
     * Mutator method to set the number of emergency brakes.
     * @param an int holding the new value of the number of brakes
     * used.
     */
    public void setBrakesUsed(int brakesUsed) {
	    this.brakesUsed = brakesUsed;
    }

    /**
     * Accessor method to get the players score.
     * @return the players score.
     */
    public int getScore() {
	    return score;
    }

    /**
     * Mutator method to set the players score.
     * @param an int holding the new score.
     */
    public void setScore(int score) {
	    this.score = score;
    }

    /**
     * Accessor method to get the overall speed the ship has reached.
     * @return the speed the ship has reached.
     */
    public double getSpeed() {
	    return speed;
    }

    /**
     * Method to update the horizontal location of the ship depending
     * on the current horizontal velocity. Also checks to ensure the
     * ships new velocity will not exceed the maximum allowed speed,
     * and ensures the ship cannot leave the screen.
     * Overrides the method in parent class.
     */
    public void updateLocation_x() {
        double loc_x = super.getLocation_x();
        double vel_x = super.getVelocity_x();
        double vel_y = super.getVelocity_y();
        int rot = super.getRotation();
        if (accelerating) {
            // Calculate present speed as a function of the x and y velocities
            speed = Math.sqrt(Math.abs((vel_x * vel_x) + (vel_y * vel_y))); 
            // Check speed will not exceed limit if velocity is increased
            if (speed + Math.abs(vel_x + 0.3d * Math.sin(rot * ((2 * PI) / 360))) <= 20) {
                if (!maxSpeedReached) {
                    // Increase velocity
                    super.setVelocity_x(vel_x + 0.3d* Math.sin(rot * ((2* PI) / 360)));
                }
                maxSpeedReached = false;
            }
            else {
                maxSpeedReached = true;
            }
        }
        // Update location
        super.setLocation_x(super.getLocation_x() + super.getVelocity_x());
        // Keep ship within horizontal bounds
	    super.setLocation_x(super.checkBounds(super.getLocation_x()));
    }

    /**
     * Method to update the vertical location of the ship. Works
     * in the same way as the updateLocation_x() method above.
     * Overrides method in parent class.
     */
    public void updateLocation_y() {
        double vel_x = super.getVelocity_x();
        double vel_y = super.getVelocity_y();
        int rot = super.getRotation();
        if (accelerating) {
            // Calculate present speed as a function of the x and y velocities
            speed = Math.sqrt(/*Math.abs*/((vel_x * vel_x) + (vel_y * vel_y)));
            // Check speed will not exceed limit if velocity is increased
            if (speed + Math.abs(vel_y + 0.3d * Math.cos(rot * ((2* PI) / 360))) <= 20) {
                if (!maxSpeedReached) {
                    // Increase velocity
                    super.setVelocity_y(vel_y + 0.3d * Math.cos(rot * ((2* PI) / 360)));
                }
                maxSpeedReached = false;
            }
            else {
                maxSpeedReached = true;
            }
        }
        // Update location
        super.setLocation_y(super.getLocation_y() + super.getVelocity_y());
        // Keep ship within vertical bounds
        super.setLocation_y(super.checkBounds(super.getLocation_y()));
    }

    /**
     * Accessor method to get the direction of the ships rotation.
     * Used by AstApplication.class to determine which way to
     * rotate the ship.
     * @return an int indicating the direction the ship is rotating.
     */
    public int getRotateDirection() {
	    return rotateDirection;
    }

    /**
     * Mutator method to set the direction of the ships rotation.
     * @param an int holding a value which represents the direction
     * of the ships rotation.
     */
    public void setRotateDirection(int rotateDirection) {
	    this.rotateDirection = rotateDirection;
    }

    /**
     * Method to update the rotation property in the parent class.
     * Increments angle of rotation if the ship is rotating clockwise
     * and decrements angle of rotation if the ship is rotating
     * anti-clockwise.
     * Overrides method in parent class.
     */
    public void updateRotation() {
        // Rotating clockwise
        if (rotateDirection == 1) {
            // If rotation reaches 360 reset to 0			    	    
            if (super.getRotation() == 360) {
                super.setRotation(0);
            }
            // Increment angle of rotation
            super.setRotation(super.getRotation() + 6);
        }
        else {
            // Rotating anti-clockwise
            if (rotateDirection == -1) {
                // If rotation reaches 0 reset to 360
                if (super.getRotation() == 0) {
                    super.setRotation(360);
                }
                // Decrement angle of rotation
                super.setRotation(super.getRotation() - 6);
            }
        }
    }
}