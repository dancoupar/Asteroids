
/**
 * The class which creates and manages the array of asteroids
 * that appear during gameplay. Gameplay progresses level by
 * level, with the amount of asteroids increasing with each
 * level. Also manages the appearance of enemy ship and power
 * up objects.
 */

public class Game {

    /**
     * Holds the current level the player is on. Number of
     * starting asteroids is two, so starting level is two.
     */
    private int level = 2;

    /**
     * Holds the current background index.
     */
    private int background = 1;

    /**
     * Array to hold the asteroids.
     */
    private Asteroid[] asteroids;

    /**
     * Total number of asteroids that are held in the array.
     * Count is reset each level.
     */
    private int asteroidCount;

    /**
     * A count for the number of asteroids currently on screen.
     * When this value reaches zero, the level is increased, and
     * more asteroids generated.
     */
    private int asteroidsRemaining;    

    /**
     * Declare the 'shell' to hold a copy of the ship object which 
     * is passed into the constructor (from AstApplication.class)
     */
    private SpaceShip ship;

    /**
     * Declare the 'shell' for the collision detector object which
     * is passed into the constructor (from AstApplication.class)
     */
    private CollisionDetector cd;

    private int timer;
    private int lifeTime = (int)(300* Math.random()) + 400;

    public Game(SpaceShip ship, CollisionDetector cd) {
		// Pass the object references received as arguments into
		// the 'shell' objects created earlier to make these objects
		// visible to the entire class.
		this.ship = ship;
		this.cd = cd;
		// Initialise level
		this.initLevel(ship.getXLocation(), ship.getYLocation());
    }

    /**
     * Method to initialise a level. Called whenever the level is
     * incremented.
     * @param a double holding the horizontal location of the
     * goodie ship object.
     * @param a double holding the vertical location of the goodie
     * ship object.
     */
    public void initLevel(double xShip, double yShip) {
		// Reset the array to null
		this.asteroids = new Asteroid[120];
		double xAst, yAst;
		// Boolean to act as a flag indicating whether the random
		// location generated as a starting position for the asteroid
		// is suitable.
		boolean positionOk = false;
		// Generate number of asteroids equal to level reached
		for (int i = 0; i < level; i++) {
	    	// ensure asteroids are not generated on top of ship!
	    	do {
				xAst = 300* Math.random();
				yAst = 300* Math.random();		
				if (!cd.collision(xShip, yShip, xAst, yAst, 30, 30)) {					
		    		positionOk = true;
				}
	    	} while (!positionOk);
			// Create new large asteroid in random location
			this.asteroids[i] = new Asteroid(12, xAst, yAst);
		}
		// Reset the asteroids remaining
		this.asteroidsRemaining = level;
		// Reset the total number of asteroids produced
		this.asteroidCount = level;
    }

    /**
     * Accessor method to get the number of asteroids in the
     * array which should be drawn.
     * @return an int holding the number of remaining asteroids.
     */
    public int getAsteroidsRemaining() {
		// Reset any previous counts
		this.asteroidsRemaining = 0;
		// Count up asteroids in existence
		for (int i = 0; i < this.asteroidCount; i++) {
			if (this.asteroids[i].getExists()) {
				this.asteroidsRemaining++;
			}
		}
		return this.asteroidsRemaining;
    }

    /**
     * Accessor method to get the total number of asteroids that
     * have been created in the current level.
     * @return an int holding the number of asteroids that have
     * been produced in the current level.
     */
    public int getAsteroidCount() {
		return this.asteroidCount;
    }

    /**
     * Accessor method to get the current level.
     * @return an int holding the current level.
     */
    public int getLevel() {
		return this.level;
    }

    /**
     * Method to check whether the level needs incrementing with
     * regards to the number of asteroids remaining.
     */
    public void checkLevel() {
		// If no asteroids left...
		if (getAsteroidsRemaining() == 0) {
			// Level up!
			this.level++;
			// Change to next background
			this.background++;
			// Initialise the next level	    
			initLevel(ship.getXLocation(), ship.getYLocation());
			// If player has reached over level 30...(!)
			if (this.level > 30) {
				// Reset game to beginning level
				this.level = 2;
				// Initialise level
				initLevel(ship.getXLocation(), ship.getYLocation());
			}
			// If backgrounds run out...
			if (this.background > 10) {
				// Go back to first background
				this.background = 1;
			}
		}
    }

    /**
     * Accessor method to get one asteroid from the array.
     * @return one asteroid object from the asteroids array.
     */
    public Asteroid getAsteroid(int i) {
		return this.asteroids[i];
    }

    /**
     * Method to split a large or medium asteroid into smaller
     * asteroids. Updates properties accordingly.
     * @param the asteroid to be split up.
     * @param the radius of the asteroids that result from the
     * split.
     */
    public void splitAsteroid(Asteroid ast, int newRadius) {
		double x = ast.getXLocation();
		double y = ast.getYLocation();
		// Destroy old asteroid
		ast.setExists(false);
		// Create first smaller asteroid
		this.asteroids[asteroidCount] = new Asteroid(newRadius, x, y);
		// Create second smaller asteroid
		this.asteroids[asteroidCount + 1] = new Asteroid(newRadius, x, y);
		// Increase total number of asteroids and asteroids remaining
		this.asteroidCount += 2;
		this.asteroidsRemaining++;
    }

    /**
     * Method to randomly show the enemy ship object.
     * @param the enemy ship object.
     */
    public void showEnemyShip(EnemyShip baddie) {
		// If ship not already displayed and random element is true...
		if ((!baddie.getExists()) && (Math.random() > 0.999)) {
			// Show enemy ship
			baddie.setExists(true);
			baddie.initRandomObject();
		}
    }

    /**
     * Method to randomly show the powerup object.
     * @param the powerup object.
     */
    public void showPowerUp(PowerUp p) {
		// If power up not already displayed...
		if (!p.getExists()) {
            // ...and random element is true...
	    	if (Math.random() > 0.999) {
				// Show power up
				p.setExists(true);
				p.initRandomObject();
            }
		}
		else { // If powerup object is being displayed...
			// Count up
			timer++;
			// Until timer reaches limit
			if (timer > lifeTime) {
				// Get rid of power up
				p.setExists(false);
				// Reset timer and lifetime of next powerup (random)
				timer = 0;
				lifeTime = (int)(300* Math.random()) + 200;
			}
		}
    }

    /**
     * Method to get the file holding the current background.
     * @return a String holding the filename of the current
     * background.
     */
    public String getBackground() {
		// Use background index to switch filename
		switch(background) {
			case 1:
				return "level1.png";
			case 2:
				return "level2.png";	   
			case 3:
				return "level3.png";	   
			case 4:
				return "level4.png";	    
			case 5:
				return "level5.png";
			case 6:
				return "level6.png";    
			case 7:
				return "level7.png";	   
			case 8:
				return "level8.png";   
			case 9:
				return "level9.png";
			case 10:
				return "level10.png";
		}
		return null;
    }	    
}
	
