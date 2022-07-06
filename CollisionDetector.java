
/**
 * The class which detects collisions between objects, and manages
 * the things which happen when these objects collide.
 *
 * @author Dan Coupar
 */

public class CollisionDetector {

    private double PI = 3.1415926535;

    /**
     * Flag which indicates whether an explosion should be displayed
     * or not.
     */
    private boolean explosionExist = false;

    /**
     * Double to hold the horizontal location of the explosion.
     */
    private double xExplosionLocation;

    /**
     * Double to hold the vertical location of the explosion.
     */
    private double yExplosionLocation;

    /**
     * Flag to indicate whether a shock wave should be displayed
     * or not.
     */
    private boolean shockWaveExist = false;

    /**
     * Int to hold the radius of the shock wave if it exists
     */
    private int shockWaveRadius;

    /**
     * Double to hold the horizontal location of the shock wave.
     */
    private double xShockWaveLocation;

    /**
     * Double to hold the vertical location of the shock wave.
     */
    private double yShockWaveLocation;

    /**
     * Flag which is set to true for a very short space of time 
     * after ship collides with an object. For this time the ship
     * is immune to collisions.
     */
    private boolean shipImmune = false;

    /**
     * Int to hold a value indicating the type of explosion.
     * 1 indicates an asteroid explosion, 2 indicates an enemy ship
     * explosion, 3 indicates goodie ship explosion.
     */
    private int explosionType;

    /**
     * Int which is incremented to act like a timer whilst the
     * shipImmune flag is set to true.
     */
    private int i;

    /**
     * Constructor for the collision detector.
     */
    public CollisionDetector() {}

    /**
     * Method to check collisions between the goodie ship and the
     * asteroids, and also between the goodie laser and the asteroids.
     * If any collisions are detected, the appropriate properties are
     * updated in the relevant classes.
     * @param the goodie spaceship.
     * @param the goodie laser.
     * @param the asteroid being checked for collisions.
     * @param the game in its current state.
     */
    public void checkCollisions(SpaceShip ship, Laser goodieLaser, Asteroid ast, Game game) {
		// Set some 'shorthand' copies of variables.
		double xShip = ship.getXLocation();
		double yShip = ship.getYLocation();
		double xLaser = goodieLaser.getXLocation();
		double yLaser = goodieLaser.getYLocation();
		double xAst = ast.getXLocation();
		double yAst = ast.getYLocation();
		int radius = ast.getRadius();
		// Check the ship is not in hyperspace mode
		if (!ship.getHyperspace()) { 	
			// Check the ship has not recently been hit
			if (!getShipImmune()) {
				// Check ship and asteroid collision.
				if (collision(xShip, yShip, xAst, yAst, 4, radius)) {
					// Register ship hit
					shipHit(ship);	    
					if (radius == 12) { // Large asteroid
						// Reduce shields
						ship.setShieldsRemaining(ship.getShieldsRemaining() - 15);
						// Break the large asteroid into two smaller asteroids
						game.splitAsteroid(ast, 8);
					}
					else {
						if (radius == 8) { // Medium asteroid
							// Reduce shields
							ship.setShieldsRemaining(ship.getShieldsRemaining() - 10);
							// Break the medium asteroid into two smaller asteroid
							game.splitAsteroid(ast, 6);
						}
						else { // Small asteroid
							// Reduce shields
							ship.setShieldsRemaining(ship.getShieldsRemaining() - 5);
							// Explode the small asteroid
							ast.setExists(false);
							startExplosion(ast.getXLocation(), ast.getYLocation(), 1);
						}
					}
				}
			}
		}
		// Check if ship is firing
		if (goodieLaser.getExist()) {
			// Check collisions between the asteroid and the goodie laser.
			if (collision(xAst, yAst, xLaser, yLaser, radius, 1)) {
				// Register a laser hit
				laserHit(ship, goodieLaser);
				if (radius == 12) { // Large asteroid
					// Split into 2 medium asteroids
					game.splitAsteroid(ast, 8);
					// Increase score
					ship.setScore(ship.getScore() + 50);
				}
				else {
					if (radius == 8) { // Medium asteroid
						// Split into two small asteroids
						game.splitAsteroid(ast, 6);
						// Increase score
						ship.setScore(ship.getScore() + 100);
					}
					else { // Small asteroid
						// Increase score
						ship.setScore(ship.getScore() + 200);
						// Explode the small asteroid
						ast.setExists(false);
						startExplosion(xAst, yAst, 1);
					}
				}
			}
		}
		// Check if asteroids in radius of shock wave if ship is powered up
		if (shockWaveExist) {
			// Check shockwave and asteroid collision. (Seismic waves
			// do not affect enemy ship objects)
			if (collision(xAst, yAst, xShockWaveLocation,
				yShockWaveLocation, 0, shockWaveRadius)) {
				if (radius == 12) { // Large asteroid
					// Split into two medium asteroids
					game.splitAsteroid(ast, 8);
					// Increase score
					ship.setScore(ship.getScore() + 50);
				}
				else {
					if (radius == 8) { // Medium asteroid
						// Split into two small asteroids
						game.splitAsteroid(ast, 6);
						// Increase score
						ship.setScore(ship.getScore() + 100);
					}
					else { // Small asteroid
						// Destroy the asteroid
						ast.setExists(false);		
						startExplosion(ast.getXLocation(), ast.getYLocation(), 1);
						// Increase score
						ship.setScore(ship.getScore() + 200);
					}
				}
			}
		}	
    }

    /**
     * Method to check collisions between the goodie ship and the
     * enemy ship, the goodie laser and the enemy ship, and the baddie
     * laser and the goodie ship. If any collisions are detected, the
     * appropriate properties are updated in the relevant classes.
     * @param the enemy ship.
     * @param the goodie laser.
     * @param the baddie laser.
     * @param the goodie spaceship.
     */
    public void checkCollisions(EnemyShip baddie, Laser goodieLaser, 
		Laser baddieLaser, SpaceShip ship) {
		double xShip = ship.getXLocation();
		double yShip = ship.getYLocation();
		double xBaddie = baddie.getXLocation();
		double yBaddie = baddie.getYLocation();
		double xGLaser = goodieLaser.getXLocation();
		double yGLaser = goodieLaser.getYLocation();
		double xBLaser = baddieLaser.getXLocation();
		double yBLaser = baddieLaser.getYLocation();
		// Check enemy laser and ship collision.
		if (!ship.getHyperspace()) {
			if (baddieLaser.getExist()) { 	
				if (collision(xBLaser, yBLaser, xShip, yShip, 0, 6)) {
					// Stop baddie laser
					baddieLaser.setExists(false);
					baddieLaser.setDistanceTravelled(0);
					// Register a ship hit
					shipHit(ship);
					// Decrease shields
					ship.setShieldsRemaining(ship.getShieldsRemaining() - 10);
				}	
			}
		}
		// If ship is firing...
		if (goodieLaser.getExist()) { 	
			// Check laser and enemy ship collision.
			if (collision(xGLaser, yGLaser, xBaddie, yBaddie, 1, 8)) {
				// Remove baddie ship
				baddie.setExists(false);
				// Explode baddie ship
				startExplosion(xBaddie, yBaddie, 2);
				// Increase score
				ship.setScore(ship.getScore() + 500);
				// Register a laser hit
				laserHit(ship, goodieLaser);
			}
		}
		if (!ship.getHyperspace()) {
			// Check ship and enemy ship collision.
			if (collision(xShip, yShip, xBaddie, yBaddie, 5, 4)) {
				// Remove baddie ship
				baddie.setExists(false);
				// Explode baddie ship
				startExplosion(xBaddie, yBaddie, 2);
				// Regiser a ship hit
				shipHit(ship);
				// Reduce shields
				ship.setShieldsRemaining(ship.getShieldsRemaining() - 10);
			}
		}
	}

	/**
	 * Method to check collisions between the goodie ship and
	 * the random powerup objects.
	 * Overloaded method.
	 * @param the goodie ship object.
	 * @param the powerup objects.
	 */
	public void checkCollisions(SpaceShip ship, PowerUp p) {
		double xShip = ship.getXLocation();
		double yShip = ship.getYLocation();
		double xPower = p.getXLocation();
		double yPower = p.getYLocation();
		if (collision(xShip, yShip, xPower, yPower, 4, 4)) {
			// Remove the powerup
			p.setExists(false);
			// Check what type of powerup was collected and act accordingly
			if (p.getPowerUpType() == 1) { // Health bonus
				ship.setShieldsRemaining(ship.getShieldsRemaining() + 20);
			}
			else {
				if (p.getPowerUpType() == 2) { // Extra emergency brake
					ship.setBrakesUsed(ship.getBrakesUsed() - 1);
				}
				else {
					if (p.getPowerUpType() == 3) { // Hyperspace bonus
						ship.setHyperspace(true);
					}
					else { // Seismic wave laser bonus
						ship.setLaserUpgraded(true);
					}
				}
			}
		}
    }

    /**
     * Method to set some standard values to attributes when
     * the goodie space ship collides with an object.
     * @param the goodie space ship.
     */
    public void shipHit(SpaceShip ship) {
		if (ship.getShieldsRemaining() != 0) {
			// Set flag to true so shields can be displayed
			ship.setShieldsDamaged(true);
			// Make ship invulnerable for short period of time
			shipImmune = true;
		}
		else { // Ship has no shields remaining
			// Game over!
			ship.setExists(false);
		}
    }

    /**
     * Method to set some standard values to attributes when
     * the goodie laser hits an object.
     * @param the goodie ship object.
     * @param the goodie laser object.
     */
    public void laserHit(SpaceShip ship, Laser laser) {
		// Stop goodie laser
		laser.setExists(false);
		// Reset the distance travelled by the goodie laser.
		laser.setDistanceTravelled(0);
		// If laser has been upgraded...
		if (ship.getLaserUpgraded()) {
	    	// Start a shock wave
	    	startShockWave(laser.getXLocation(), laser.getYLocation());
		}
    }

    /**
     * Method which sets the properties of an explosion, rendered
     * in AstApplication.class
     * @param an int holding the horizontal location of the centre
     * of the explosion.
     * @param an int holding the vertical location of the centre of
     * the explosion.
     * @param an int holding a value which represents the type of
     * the explosion.
     */
    public void startExplosion(double xCentre, double yCentre, int type) {
		explosionType = type;
		xExplosionLocation = xCentre;
		yExplosionLocation = yCentre;
		explosionExist = true;
    }

    /**
     * Method to stop an explosion from continuing to be rendered
     * by setting its exist flag to false.
     */    
    public void stopExplosion() {
		explosionExist = false;
    }

    /**
     * Accessor method to get the flag indicating whether the
     * explosion is in existence or not.
     * @return the flag indicating whether the explosion is in
     * existence.
     */
    public boolean getExplosionExist() {
		return explosionExist;
    }

    /**
     * Accessor method to get the horizontal location of the
     * explosion.
     * @return the horizontal location of the explosion.
     */
    public double getXExplosionLocation() {
		return xExplosionLocation;
    }

    /**
     * Accessor method to get the vertical location of the
     * explosion.
     * @return the vertical location of the explosion.
     */
    public double getYExplosionLocation() {
		return yExplosionLocation;
    }

    /**
     * Accessor method to get the type of explosion.
     * @return an int value indicating the type of the explosion.
     */
    public int getExplosionType() {
		// 1: small, blue explosion
		// 2: small, red explosion
		// 3: large, red explosion
		return explosionType;
    }

    /**
     * Method to set some properties for a shockwave which is
     * rendered in AstApplication.class.
     * Shockwave is produced when the laser hits an object and
     * when the ship has aquired the seismic laser upgrade.
     * @param a double containing the horizontal location of the
     * centre of the shockwave origin.
     * @param a double containing the vertical location of the
     * centre of the shockwave origin.
     */
    public void startShockWave(double xCentre, double yCentre) {
		xShockWaveLocation = xCentre;
		yShockWaveLocation = yCentre;
		shockWaveExist = true;
    }

    /**
     * Method to stop a shockwave being rendered by setting its
     * exist flag to false.
     */
    public void stopShockWave() {
		shockWaveExist = false;
    }

    /**
     * Accessor method to get the flag indicating whether a
     * shockwave should be shown or not.
     * @return the flag inidcating the shockwaves existence.
     */
    public boolean getShockWaveExist() {
		return shockWaveExist;
    }

    /**
     * Accessor method to get the horizontal location of the
     * shockwave.
     * @return the horizontal location of the shockwave.
     */
    public double getXShockWaveLocation() {
		return xShockWaveLocation;
    }

    /**
     * Accessor method to get the vertical location of the
     * shockwave.
     * @return the vertical location of the shockwave.
     */
    public double getYShockWaveLocation() {
		return yShockWaveLocation;
    }

    /**
     * Accessor method to get the current radius of the
     * shockwave.
     * @return an int holding the current radius of the
     * shockwave.
     */
    public int getShockWaveRadius() {
		return shockWaveRadius;
    }

    /**
     * Mutator method to update the radius of the shockwave.
     * @param an int holding the new radius of the shockwave.
     */
    public void setShockWaveRadius(int shockWaveRadius) {
		this.shockWaveRadius = shockWaveRadius;
    }

    /**
     * Accessor method to get the flag indicating whether the
     * ship is immune to asteroid hits or not. (Ship is immune
     * to asteroid hits for a brief period of time after colliding
     * with an object to prevent several hits occuring at exactly
     * the same time)
     */
    public boolean getShipImmune() {
		// Increment timer	
		i++;
		// If timer exceeds limit...
		if (i > 15) {
			// Ship no longer immune
			shipImmune = false;
			// Reset timer
			i = 0;
		}
		return shipImmune;
    }

    /**
     * Method to check if two objects occupy the same space on the
     * screen.
     * @param the horizontal location of the first object.
     * @param the vertical location of the first object.
     * @param the horizontal location of the second object.
     * @param the vertical location of the second object.
     * @param the radius of the first object.
     * @param the radius of the second object.
     * @return true if the two objects occupy the same space.
     * @return false if the two objects do not occupy the same space.
     */
    public boolean collision(double ax, double ay, double bx, double by, int rada, int radb) {
		if ((ax - rada < bx + radb + 1) && (bx - radb - 1 < ax + rada)) {
			if ((ay - rada < by + radb) && (by - radb < ay + rada)) {
				return true;
			}
		}
		return false;
    }
}