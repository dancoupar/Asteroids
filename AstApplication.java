
/**
 * 08235 Computer Graphics and UID
 * Asteroids ACW
 * @version 1.1
 * 15/04/2003
 *
 * OpenGL rendering class based on an OpenGL Java Shell.
 * GLAnimCanvas will allow the canvas to be redrawn continuously.
 *
 * @author Dan Coupar
 */

/**
 * Import Abstract Window Toolkit and applet libraries.
 */
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Import OpenGL Java binding libraries
 */
import gl4java.GLContext;
import gl4java.awt.GLAnimCanvas;
import gl4java.utils.glut.*;
import gl4java.utils.glut.fonts.*;
import gl4java.utils.textures.*;

/**
 * The main class which renders the various objects on screen and
 * also manages keyboard input.
 */
public class AstApplication extends Applet {

    renderCanvas canvas = null;

    private double PI = 3.1415926535;

    /**
     * Declare and instantiate the goodie ship object. Starting
     * position is centre of screen.
     */
    private SpaceShip ship = new SpaceShip(150.0, 150.0);

    /**
     * Declare and instantiate the baddie ship. Baddie ship appears
     * at random times.
     */
    private EnemyShip baddie = new EnemyShip();

    /**
     * Declare and instantiate power up object. Power up object
     * appears at random times and takes different forms.
     */
    private PowerUp p = new PowerUp();

    /**
     * Declare and instantiate collision detector, which is polled
     * to check for relevant object collisions.
     */
    private CollisionDetector cd = new CollisionDetector();

    /**
     * Declare and instantiate laser object which appears when the
     * goodie ship fires.
     */
    private Laser goodieLaser = new Laser(0.0, 0.0);

    /**
     * Declare and instantiate baddie laser object which appears when
     * the baddie ship fires.
     */
    private Laser baddieLaser = new Laser(0.0, 0.0);

    /**
     * Create the game object which creates the asteroids at the
     * beginning of each level, and also manages the appearance
     * of random space objects.
     */
    private Game game = new Game(ship, cd);

    /**
     * Float to hold the amount of red to display in glColor commands.
     * Useful if this value needs to be updated during runtime.
     */
    private float _red = 1.0f;

    /**
     * Float to hold the amount of green to display in glColor commands.
     */
    private float _green = 1.0f;

    /**
     * Float to hold the amount of blue to display in glColor commands.
     */
    private float _blue = 1.0f;

    /**
     * Int to hold the current level the player has reached.
     */
    private int _level = game.getLevel();

    /**
     * Int to hold the size of the blast radius when an object is
     * destroyed.
     */
    private int _blastRadius;

    /**
     * Int to hold the size of the waves emitted when the ship is
     * in hyperspace mode. (Powerup)
     */
    private int _hyperRadius;

    /**
     * Int to hold the size of the shock wave when the ship has
     * seismic lasers. (Powerup)
     */
    private int _shockRadius;

    /**
     * Standard init() routine for getting things going.
     * create the canvas and add it to the current applet.
     */
    public void init() {

	setLayout(new BorderLayout());
	canvas = new renderCanvas(getSize().width, getSize().height);
	add("Center", canvas);
    }

    // Now simply start the canvas going
    public void start() {

        // Start animating the canvas
        canvas.start();
    }

    // Call this when the canvas is to be stopped
    public void stop() {

        // Stop animating the canvas
        canvas.stop();
    }

    // Destroy the applet, the last thing to do
    public void destroy() {

        //Stop animating the canvas
        canvas.stop();

        //Destroy the canvas
        canvas.destroy();
    }

    private class renderCanvas extends GLAnimCanvas implements KeyListener {

	GLUTFunc glut = null;

	/*
	 * Storage for current background texture.
	 */
	int[] texture = new int[1];

	/*
	 * Storage for the asteroid texture.
	 */
	int[] asteroidTex = new int[1];

        public renderCanvas(int w, int h) {

            super(w, h);
	    setAnimateFps(30);
	    addKeyListener(this);
        }

	public void keyPressed(KeyEvent e) {

	    switch (e.getKeyCode()) {

	    // Rotate ship right
	    case KeyEvent.VK_RIGHT: // Check up key is not being pressed
				    if (!ship.getAccelerating()) {

					ship.setRotateDirection(1);
				    }
					
		break;

	    // Rotate ship left
	    case KeyEvent.VK_LEFT: // Check up key is not being pressed
				   if (!ship.getAccelerating()) {

				       ship.setRotateDirection(-1);
				   }

		break;

	    // Move ship forward
	    case KeyEvent.VK_UP: // Check left or right keys are not being pressed
				 if (ship.getRotateDirection() == 0) {
				
				     ship.setAccelerating(true);
				 }
		break;

	    // Fire laser
	    case KeyEvent.VK_SPACE: // Check laser is not already being fired
				    if (!goodieLaser.getExist()) {

					goodieLaser.setExist(true);

					// Point laser in direction of ship
					goodieLaser.setRotation(ship.getRotation());
					goodieLaser.laserFire(ship.getLocation_x(), 									    ship.getLocation_y());

					if (ship.getLaserUpgraded()) {

					    // Reduce number of seismic lasers remaining
					    ship.setShotsRemaining(ship.getShotsRemaining() - 1);
					}
				    }
		break;

	    case KeyEvent.VK_B: if (ship.getBrakesUsed() < 2) {
		
				    // Avoids wasting emergency brake if ship is not moving
				    if ((ship.getVelocity_x() == 0) &&
					(ship.getVelocity_y() == 0)) {
					
					// Do nothing					
				    }
					
				    else {	

					// Stop ship dead
					ship.setVelocity_x(0);
					ship.setVelocity_y(0);
					ship.setBrakesUsed(ship.getBrakesUsed()+1);
				    }
				}
		break;

	    case KeyEvent.VK_C: ship.setLocation_x(300* Math.random());
				ship.setLocation_y(300* Math.random());
				ship.setVelocity_x(0);
				ship.setVelocity_y(0);
		break;

	    }	    
	}

	public void keyTyped(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {
	    
	    switch (e.getKeyCode()) {

	    // Stop rotating when the key is released
	    case KeyEvent.VK_RIGHT: ship.setRotateDirection(0);
		break;

	    // Stop rotating when the key is released
	    case KeyEvent.VK_LEFT: ship.setRotateDirection(0);
		break;

	    // Stop accelerating when the key is released
	    case KeyEvent.VK_UP: ship.setAccelerating(false);
		break;
	    }
	}

	/**
	 * Method to load textures. Is called at startup and if the next
	 * level is reached.
	 */
	public void loadGLTextures() {

	    // Get current background texture
	    String background = game.getBackground();

	    /*
	     * Texture loader object for the backgound texture.
	     */
	    PngTextureLoader texLoader = new PngTextureLoader(gl, glu);

	    texLoader.readTexture(getCodeBase(), background);

	    if(texLoader.isOk())  {

		// Create background texture
		gl.glGenTextures(1, texture);
		gl.glBindTexture(GL_TEXTURE_2D, texture[0]);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,GL_LINEAR);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR); 					gl.glTexImage2D( GL_TEXTURE_2D, 0, 3, texLoader.getImageWidth(), 				            texLoader.getImageHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, 
		    texLoader.getTexture());
	    }

	    /*
	     * Texture loader object for the asteroid texture.
	     */
    	    PngTextureLoader astTexLoader = new PngTextureLoader(gl, glu);

	    astTexLoader.readTexture(getCodeBase(), "asteroid.png");

	    if(astTexLoader.isOk()) {

		//create asteroid texture
		gl.glGenTextures(1, asteroidTex);
		gl.glBindTexture(GL_TEXTURE_2D, asteroidTex[0]);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,GL_LINEAR);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR); 					gl.glTexImage2D( GL_TEXTURE_2D, 0, 3, astTexLoader.getImageWidth(), 				            astTexLoader.getImageHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, 
		    astTexLoader.getTexture());
	    }
	}

	/**
	 * Method to texture map the background texture onto the playing
	 * screen.
	 */
	public void renderBackgroundTexture() { 

	    gl.glBindTexture(GL_TEXTURE_2D, texture[0]);
	    gl.glEnable(GL_TEXTURE_2D);

	    gl.glBegin(GL_QUADS);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3f(0.0f, 0.0f, -1f); // bottom left
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3f(300.0f, 0.0f, -1f); // bottom right
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3f(300.0f, 300.0f, -1f); // top right
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3f(0.0f, 300.0f, -1f); // top left
	    
	    gl.glEnd();

	    gl.glDisable(GL_TEXTURE_2D);
	}

	/**
	 * Method to render a simple empty circle.
	 * @param a double holding the horizontal location of the circle.
	 * @param a double holding the vertical location of the circle.
	 * @param a double holding the radius of the circle.
	 */
	public void renderCircle(double x, double y, double radius) {

	    gl.glBegin(GL_LINES);

		gl.glVertex2d(x + (radius * 1.2d), y + (radius * 0.15d));
		gl.glVertex2d(x + (radius * 1.05d), y + (radius * 0.6d));
		gl.glVertex2d(x + (radius * 1.05d), y + (radius * 0.6d));
		gl.glVertex2d(x + (radius * 0.6d), y + (radius * 1.05d));
		gl.glVertex2d(x + (radius * 0.6d), y + (radius * 1.05d));
		gl.glVertex2d(x + (radius * 0.15d), y + (radius * 1.2d));
		gl.glVertex2d(x + (radius * 0.15d), y + (radius * 1.2d));
		gl.glVertex2d(x - (radius * 0.15d), y + (radius * 1.2d));
		gl.glVertex2d(x - (radius * 0.15d), y + (radius * 1.2d));
		gl.glVertex2d(x - (radius * 0.6d), y + (radius * 1.05d));
		gl.glVertex2d(x - (radius * 0.6d), y + (radius * 1.05d));
		gl.glVertex2d(x - (radius * 1.05d), y + (radius * 0.6d));
		gl.glVertex2d(x - (radius * 1.05d), y + (radius * 0.6d));
		gl.glVertex2d(x - (radius * 1.2d), y + (radius * 0.15d));
		gl.glVertex2d(x - (radius * 1.2d), y + (radius * 0.15d));
		gl.glVertex2d(x - (radius * 1.2d), y - (radius * 0.15d));
		gl.glVertex2d(x - (radius * 1.2d), y - (radius * 0.15d));
		gl.glVertex2d(x - (radius * 1.05d), y - (radius * 0.6d));
		gl.glVertex2d(x - (radius * 1.05d), y - (radius * 0.6d));
		gl.glVertex2d(x - (radius * 0.6d), y - (radius * 1.05d));
		gl.glVertex2d(x - (radius * 0.6d), y - (radius * 1.05d));
		gl.glVertex2d(x - (radius * 0.15d), y - (radius * 1.2d));
		gl.glVertex2d(x - (radius * 0.15d), y - (radius * 1.2d));
		gl.glVertex2d(x + (radius * 0.15d), y - (radius * 1.2d));
		gl.glVertex2d(x + (radius * 0.15d), y - (radius * 1.2d));
		gl.glVertex2d(x + (radius * 0.6d), y - (radius * 1.05d));
		gl.glVertex2d(x + (radius * 0.6d), y - (radius * 1.05d));
		gl.glVertex2d(x + (radius * 1.05d), y - (radius * 0.6d));
		gl.glVertex2d(x + (radius * 1.05d), y - (radius * 0.6d));
		gl.glVertex2d(x + (radius * 1.2d), y - (radius * 0.15d));
		gl.glVertex2d(x + (radius * 1.2d), y - (radius * 0.15d));
		gl.glVertex2d(x + (radius * 1.2d), y + (radius * 0.15d));

	    gl.glEnd();
	}

	/**
	 * Method to render a simple filled circle.
	 * @param a double holding the horizontal location of the circle.
	 * @param a double holding the vertical location of the circle.
	 * @param a double holding the radius of the circle.
	 */
	public void renderFilledCircle(double x, double y, double radius) {

	    gl.glBegin(GL_POLYGON);

		gl.glVertex2d(x + (radius) * 1.2d, y + (radius) * 0.15d);
		gl.glVertex2d(x + (radius) * 1.05d, y + (radius) * 0.6d);
		gl.glVertex2d(x + (radius) * 0.6d, y + (radius) * 1.05d);
		gl.glVertex2d(x + (radius) * 0.15d, y + (radius) * 1.2d);
		gl.glVertex2d(x - (radius) * 0.15d, y + (radius) * 1.2d);
		gl.glVertex2d(x - (radius) * 0.6d, y + (radius) * 1.05d);
		gl.glVertex2d(x - (radius) * 1.05d, y + (radius) * 0.6d);
		gl.glVertex2d(x - (radius) * 1.2d, y + (radius) * 0.15d);	
		gl.glVertex2d(x - (radius) * 1.2d, y - (radius) * 0.15d);
		gl.glVertex2d(x - (radius) * 1.05d, y - (radius) * 0.6d);
		gl.glVertex2d(x - (radius) * 0.6d, y - (radius) * 1.05d);
		gl.glVertex2d(x - (radius) * 0.15d, y - (radius) * 1.2d);
		gl.glVertex2d(x + (radius) * 0.15d, y - (radius) * 1.2d);
		gl.glVertex2d(x + (radius) * 0.6d, y - (radius) * 1.05d);
		gl.glVertex2d(x + (radius) * 1.05d, y - (radius) * 0.6d);
		gl.glVertex2d(x + (radius) * 1.2d, y - (radius) * 0.15d);
		
	    gl.glEnd();
	}

	/**
	 * Method to render the goodie spaceship.
	 */
	public void renderSpaceShip() {

	    double x = ship.getLocation_x();
	    double y = ship.getLocation_y();

	    gl.glBegin(GL_POLYGON);
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glVertex2d(x - 4, y - 7);
		gl.glVertex2d(x - 3, y - 7);
		gl.glVertex2d(x - 3, y - 5);
		gl.glVertex2d(x - 4, y - 5);
	    gl.glEnd();

	    gl.glBegin(GL_POLYGON);	    
		gl.glColor3f(0.8f, 0.0f, 0.0f);
		gl.glVertex2d(x - 3, y - 7);
		gl.glVertex2d(x - 1, y - 7);
		gl.glVertex2d(x - 1, y - 5);
		gl.glVertex2d(x - 3, y - 5);
	    gl.glEnd();

	    gl.glBegin(GL_POLYGON);
		gl.glColor3f(0.6f, 0.0f, 0.0f);
		gl.glVertex2d(x - 1, y - 7);
		gl.glVertex2d(x + 1, y - 7);
		gl.glVertex2d(x + 1, y - 5);
		gl.glVertex2d(x - 1, y - 5);
	    gl.glEnd();

	    gl.glBegin(GL_POLYGON);
		gl.glColor3f(0.8f, 0.0f, 0.0f);
		gl.glVertex2d(x + 1, y - 7);
		gl.glVertex2d(x + 3, y - 7);
		gl.glVertex2d(x + 3, y - 5);
		gl.glVertex2d(x + 1, y - 5);
	    gl.glEnd();

	    gl.glBegin(GL_POLYGON);
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glVertex2d(x + 3, y - 7);
		gl.glVertex2d(x + 4, y - 7);
		gl.glVertex2d(x + 4, y - 5);
		gl.glVertex2d(x + 3, y - 5);
	    gl.glEnd();
	  
	    gl.glBegin(GL_POLYGON);
		gl.glColor3f(1.0f, 0.7f, 0.6f);
		gl.glVertex2d(x - 3, y - 7);
		gl.glVertex2d(x - 3.5d, y - 8);
		gl.glVertex2d(x - 0.5d, y - 8);
		gl.glVertex2d(x - 1, y - 7);
	    gl.glEnd();
	  
	    gl.glBegin(GL_POLYGON);
		gl.glColor3f(1.0f, 0.7f, 0.6f);
		gl.glVertex2d(x + 1, y - 7);
		gl.glVertex2d(x + 0.5d, y - 8);
		gl.glVertex2d(x + 3.5d, y - 8);
		gl.glVertex2d(x + 3, y - 7);
	    gl.glEnd();

	    gl.glBegin(GL_POLYGON);
		gl.glColor3f(0.6f, 0.0f, 0.0f);
		gl.glVertex2d(x + 1, y - 5);
		gl.glVertex2d(x - 1, y - 5);
		gl.glVertex2d(x - 1, y + 4);
		gl.glVertex2d(x + 1, y + 4);
	    gl.glEnd();

	    gl.glBegin(GL_POLYGON);
		gl.glColor3f(1.0f, 0.7f, 0.6f);
		gl.glVertex2d(x - 5.5d, y - 4);
		gl.glVertex2d(x - 5.5d, y - 5);
		gl.glVertex2d(x - 4.5d, y - 5);
		gl.glVertex2d(x - 4.5d, y - 4);
	    gl.glEnd();

	    gl.glBegin(GL_POLYGON);
		gl.glColor3f(1.0f, 0.7f, 0.6f);
		gl.glVertex2d(x + 4.5d, y - 4);
		gl.glVertex2d(x + 4.5d, y - 5);
		gl.glVertex2d(x + 5.5d, y - 5);
		gl.glVertex2d(x + 5.5d, y - 4);

	    gl.glEnd();

	    gl.glBegin(GL_TRIANGLES);
		gl.glColor3f(0.8f, 0.0f, 0.0f);
		gl.glVertex2d(x - 3, y - 5);
		gl.glVertex2d(x - 1, y - 5);
		gl.glVertex2d(x - 1, y + 4);

		gl.glColor3f(0.8f, 0.0f, 0.0f);
		gl.glVertex2d(x + 1, y - 5);
		gl.glVertex2d(x + 3, y - 5);
		gl.glVertex2d(x + 1, y + 4);

		gl.glColor3f(0.9f, 0.0f, 0.0f);
		gl.glVertex2d(x - 1, y + 4);
		gl.glVertex2d(x, y + 10);
		gl.glVertex2d(x + 1, y + 4);

		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glVertex2d(x - 3, y - 4);
		gl.glVertex2d(x - 7, y - 4);
		gl.glVertex2d(x - 1, y + 4);

		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glVertex2d(x + 3, y - 4);
		gl.glVertex2d(x + 7, y - 4);
		gl.glVertex2d(x + 1, y + 4);

		gl.glColor3f(1.0f, 1.0f, 0.0f);
		gl.glVertex2d(x + 3, y - 5);
		gl.glVertex2d(x + 4, y - 5);
		gl.glVertex2d(x + 1, y + 4);

		gl.glColor3f(1.0f, 1.0f, 0.0f);
		gl.glVertex2d(x - 4, y - 5);
		gl.glVertex2d(x - 3, y - 5);
		gl.glVertex2d(x - 1, y + 4);

		gl.glColor3f(0.5f, 0.6f, 1.0f);
		gl.glVertex2d(x - 1.5d, y);
		gl.glVertex2d(x + 1.5d, y);
		gl.glVertex2d(x, y + 6);

		gl.glColor3f(0.7f, 0.8f, 1.0f);
		gl.glVertex2d(x - 0.5d, y + 1.5d);
		gl.glVertex2d(x + 0.5d, y + 1.5d);
		gl.glVertex2d(x, y + 4);
	    gl.glEnd();
	}

	/**
	 * Method to render the shield of the goodie space ship which
	 * appears when the ship is hit. Visibility of the shield
	 * decreases as the shield is damaged.
	 */
	public void renderShield() {

	    if (ship.getShieldsDamaged()) {

		/**
		 * Double to hold the brightness of the shield being rendered.
		 * This value decreases as the shields are damaged.
		 */
		float shieldBrightness = 0;

		/**
		 * Switch statement to set the value of shieldBrightness
		 * depending on the amount of shields the ship has left.
		 */
		switch(ship.getShieldsRemaining()) {

		case 100: case 95: case 90: case 85: shieldBrightness = 1.0f;
		    break;

		case 80: case 75: case 70: case 65: shieldBrightness = 0.95f;
		    break;

		case 60: case 55: case 50: case 45: shieldBrightness = 0.90f;
		    break;

		case 40: case 35: case 30: case 25: shieldBrightness = 0.85f;
		    break;

		case 20: case 15: shieldBrightness = 0.8f;
		    break;

		case 10: case 5: shieldBrightness = 0.7f;
		    break;

		case 0: shieldBrightness = 0.0f;
		    break;
		}

		/**
		 * Multiply colours by the brightness of the shield. Colours
		 * will be less visible if they are darker.
		 */		
		_red = _red * shieldBrightness;
		_green = _green * shieldBrightness;
		_blue = _blue * shieldBrightness;

		gl.glColor3f(_red, _green, _blue);
		
		// render the shield
		renderCircle(ship.getLocation_x(), ship.getLocation_y(), 10);

		_red = _red - 0.05f;
		_green = _green - 0.1f;
		_blue = _blue - 0.1f;

		// reset the colours
		if ((_red < 0.5f) || (_green < 0.0f) || (_blue < 0.0f)) {

		    _red = 1.0f;
		    _green = 1.0f;
		    _blue = 1.0f;

		    // set flag to false so shields can stop being shown
		    ship.setShieldsDamaged(false);
		}
	    }
	}

	/**
	 * Method to render a generic explosion, displayed when a space
	 * object is destroyed.
	 */
	public void renderExplosion() {

	    if (cd.getExplosionExist()) {

		double x = cd.getExplosionLocation_x();
		double y = cd.getExplosionLocation_y();
		int explosionEnd;

		// type 1: asteroid explosion; small, blue
		// type 2: enemy ship explosion; small, red
		// type 3: game over explosion; big, red

		if (cd.getExplosionType() == 1) {

		    gl.glColor3f(0.7f, 0.8f, 1.0f);
		    explosionEnd = 8;
		}

		else {
				    
		    gl.glColor3f(1.0f, 0.2f, 0.2f);
		    explosionEnd = 8;
		
		    if (cd.getExplosionType() == 3) {

			explosionEnd = 30;
		    }
		}

		_blastRadius++;

		// Expand explosion radius
		if (_blastRadius > explosionEnd) {

		    _blastRadius = 0;
		    cd.stopExplosion();
		}

		// Render outer explosion
		renderCircle(x, y, _blastRadius);
		
		if (cd.getExplosionType() == 1) {

		    gl.glColor3f(0.8f, 1.0f, 1.0f);
		}

		else {

		    gl.glColor3f(1.0f, 0.8f, 0.7f);
		}

		// render inner explosion
		renderFilledCircle(x, y, _blastRadius /2);
	    }
	}

	/**
	 * Method to render the shock wave created by seismic lasers when
	 * the relevant power up has been collected. This shockwave
	 * destroys all the asteroids within its range.
	 */
	public void renderShockWave() {

	    if (cd.getShockWaveExist()) {

		gl.glColor3f(0.9f, 0.7f, 1.0f);

		// Render shock wave
	    	renderCircle(cd.getShockWaveLocation_x(), cd.getShockWaveLocation_y(),
		    _shockRadius);
	
		// Increase shock wave radius
		_shockRadius = _shockRadius + 4;

		// Stop shock wave when radius reaches limit
		if (_shockRadius > 30) {

		    _shockRadius = 0;
		    cd.stopShockWave();
		    goodieLaser.setExist(false);
		}

		/**
		 * Shock wave radius is passed to CollisionDetector.class so
	 	 * collisions between shock wave and asteroids can be
	 	 * detected.
	 	 */	
		cd.setShockWaveRadius(_shockRadius);
	    }
	}

	/**
	 * Method to render the 'waves' emitted from the spaceship when in
	 * hyperspace mode. (Powerup)
	 */
	public void renderHyperspace() {

	    gl.glColor3f(1.0f, 1.0f, 1.0f);

	    renderCircle(ship.getLocation_x(), ship.getLocation_y(), _hyperRadius);
	    renderCircle(ship.getLocation_x(), ship.getLocation_y(), _hyperRadius / 2);

	    _hyperRadius++;

	    if (_hyperRadius > 10) {

		_hyperRadius = 0;
	    }
	}

	/**
	 * Method to render powerups that appear randomly. Four different
	 * powerups can appear.
	 */
	public void renderPowerUp() {

	    // Get location of powerup
	    double x = p.getLocation_x();
	    double y = p.getLocation_y();

	    gl.glColor3f(1.0f, 1.0f, 1.0f);

	    // Render powerup 'bubble'
	    renderCircle(x, y, 4);

	    // Health powerup
	    if (p.getPowerUpType() == 1) {

		gl.glColor3f(1.0f, 0.0f, 0.0f);

		// Render a red cross in the centre of the bubble
		gl.glBegin(GL_POLYGON);
		    gl.glVertex2d(x - 1, y + 3);
		    gl.glVertex2d(x - 1, y - 3);
		    gl.glVertex2d(x + 1, y - 3);
		    gl.glVertex2d(x + 1, y + 3);
		gl.glEnd();

		gl.glBegin(GL_POLYGON);
		    gl.glVertex2d(x - 3, y - 1);
		    gl.glVertex2d(x + 3, y - 1);
		    gl.glVertex2d(x + 3, y + 1);
		    gl.glVertex2d(x - 3, y + 1);
		gl.glEnd();
	    }


	    else {

		// Emergency Brake powerup
		if (p.getPowerUpType() == 2) {

		    gl.glColor3f(1.0f, 1.0f, 0.0f);

		    // Yellow letter 'B' in the centre of the bubble
		    renderString("B", p.getLocation_x() - 1.6d, p.getLocation_y() - 1.8d);
		}

	    	else {

		    // Hyperspace powerup
		    if (p.getPowerUpType() == 3) {

			gl.glColor3f(0.5f, 0.8f, 1.0f);

			// Blue letter 'H' in the centre of the bubble
			renderString("H", p.getLocation_x() - 1.7d, p.getLocation_y() - 1.8d);
		    }

		    // Seismic Laser powerup
		    else {

			// Purple letter 'S' in the centre of the bubble
			gl.glColor3f(1.0f, 0.0f, 1.0f);
			renderString("S", p.getLocation_x() - 1.6d, p.getLocation_y() - 1.8d);
		    }
		}
	    }			
	}		

	/**
	 * Method to render the lasers fired by the goodie and baddie ships. 
	 * Lasers disappear after hitting an object or travelling the 
	 * distance of the screen.
	 * @param the laser to be rendered. (Goodie or baddie)
	 */
	public void renderLaser(Laser laser) {

	    double x = laser.getLocation_x();
	    double y = laser.getLocation_y();
	    int rot = laser.getRotation();

	    // Draw laser in direction ship is pointing
	    gl.glBegin(GL_LINES);
		gl.glVertex2d(x, y);
		gl.glVertex2d(x + 6* Math.sin(rot * ((2* PI) / 360)), y 
                    + 6* Math.cos(rot * ((2* PI) / 360)));
	    gl.glEnd();
	}

	/**
	 * Method to display the visible thrust as the ship moves. Is
	 * only displayed if the player is pressing forward ie the ship
	 * is accelerating.
	 */
	public void renderRetroRockets() {

	    double x = ship.getLocation_x();
	    double y = ship.getLocation_y();

	    gl.glBegin(GL_TRIANGLES);
		gl.glColor3f(0.7f, 0.7f, 1.0f);
		gl.glVertex2d(x - 3, y - 7);
		gl.glVertex2d(x - 1, y - 7);
		gl.glVertex2d(x - 2, y - 12);

		gl.glVertex2d(x + 3, y - 7);
	    	gl.glVertex2d(x + 1, y - 7);
		gl.glVertex2d(x + 2, y - 12);

		gl.glVertex2d(x - 5.5d, y - 5);
		gl.glVertex2d(x - 4.5d, y - 5);
		gl.glVertex2d(x - 5, y - 9);

		gl.glVertex2d(x + 4.5d, y - 5);
		gl.glVertex2d(x + 5.5d, y - 5);
		gl.glVertex2d(x + 5, y - 9);
	    gl.glEnd();
	}

	/**
	 * Renders the enemy ship as a pink flying saucer.
	 */
	public void renderEnemyShip() {

	    double x = baddie.getLocation_x();
	    double y = baddie.getLocation_y();

	    gl.glColor3f(1.0f, 0.5f, 0.5f);
	    renderFilledCircle(x, y, 7);

	    gl.glColor3f(1.0f, 0.4f, 0.4f);
	    renderFilledCircle(x, y, 6);

	    gl.glColor3f(1.0f, 0.3f, 0.3f);
	    renderFilledCircle(x, y, 5);
	
	    gl.glColor3f(0.4f, 0.4f, 1.0f);
	    renderFilledCircle(x, y, 3);

	    gl.glColor3f(0.5f, 0.5f, 1.0f);
	    renderFilledCircle(x, y, 2);

	    gl.glColor3f(0.6f, 0.6f, 1.0f);
	    renderFilledCircle(x, y, 1);

	    gl.glColor3f(0.0f, 1.0f, 0.0f);
	    renderFilledCircle(x - 5, y - 5, 0.5);

	    gl.glColor3f(0.0f, 1.0f, 0.0f);
	    renderFilledCircle(x + 5, y + 5, 0.5);

	    gl.glColor3f(0.0f, 1.0f, 0.0f);
	    renderFilledCircle(x - 7.2, y, 0.5);

	    gl.glColor3f(0.0f, 1.0f, 0.0f);
	    renderFilledCircle(x + 7.2, y, 0.5);

	    gl.glColor3f(0.0f, 1.0f, 0.0f);
	    renderFilledCircle(x, y - 7.2, 0.5);

	    gl.glColor3f(0.0f, 1.0f, 0.0f);
	    renderFilledCircle(x, y + 7.2, 0.5);

	    gl.glColor3f(0.2f, 1.0f, 0.2f);
	    renderFilledCircle(x + 5, y - 5, 0.5);

	    gl.glColor3f(0.2f, 1.0f, 0.2f);
	    renderFilledCircle(x - 5, y + 5, 0.5);
	}

	/**
	 * Method to render the asteroids. The shape of the asteroid
	 * is rendered and then texture mapped using a rock texture.
	 * @param the asteroid to be rendered.
	 */
	public void renderAsteroid(Asteroid ast) {

	    // Check asteroid hasn't been destroyed
	    if (ast.getExist()) {

		double x = ast.getLocation_x();
		double y = ast.getLocation_y();
		int rad = ast.getRadius();
		int rot = ast.getRotation();

		// Move and rotate asteroid
		ast.updateLocation_x();
		ast.updateLocation_y();
		ast.updateRotation();

		gl.glPushMatrix();

		    gl.glTranslated(x, y, 0);
		    gl.glRotated(rot, 0, 0, 1);
		    gl.glTranslated(-1* x, -1* y, 0);

		    gl.glBegin(GL_POLYGON);
		
			// Size of asteroid depends on its radius
			gl.glColor3f(0.8f, 0.8f, 0.8f);
			gl.glVertex2d(x - rad, y); 
			gl.glVertex2d(x - rad /2, y + rad);
			gl.glVertex2d(x + rad /2, y + rad);
			gl.glVertex2d(x + rad, y);
			gl.glVertex2d(x + rad /2, y - rad);
			gl.glVertex2d(x - rad /2, y - rad);

		    gl.glEnd();

	    	    gl.glBindTexture(GL_TEXTURE_2D, asteroidTex[0]);
	    	    gl.glEnable(GL_TEXTURE_2D);

		    // Map asteroid texture onto polygon
		    gl.glBegin(GL_QUADS);

			gl.glTexCoord2d(0.0d, 0.0d);
			gl.glVertex2d(x - rad, y);

			gl.glTexCoord2d(1.0d, 0.0d);
			gl.glVertex2d(x - rad /2, y + rad);

			gl.glTexCoord2d(1.0d, 1.0d);
			gl.glVertex2d(x + rad /2, y + rad);

			gl.glTexCoord2d(0.0d, 1.0d);
			gl.glVertex2d(x + rad, y);
		    
	    	    gl.glEnd();

		    gl.glBegin(GL_QUADS);

			gl.glTexCoord2d(0.0d, 1.0d);
			gl.glVertex2d(x + rad, y);

			gl.glTexCoord2d(1.0d, 1.0d);
			gl.glVertex2d(x + rad /2, y - rad);

			gl.glTexCoord2d(1.0d, 0.0d);
			gl.glVertex2d(x - rad /2, y - rad);

			gl.glTexCoord2d(0.0d, 0.0d);
			gl.glVertex2d(x - rad, y);

		    gl.glEnd();

		    gl.glDisable(GL_TEXTURE_2D);

		gl.glPopMatrix();

		// Check if this instance of asteroid is colliding with any objects
		cd.checkCollisions(ship, goodieLaser, ast, game);
	    }
	}

	/**
	 * Method to render text. Can position text anywhere on the screen
	 * when called.
	 * @param a String holding the text to be displayed.
	 * @param a double holding the horizontal location of the text to
	 * be displayed.
	 * @param a double holding the vertical location of the text to be
	 * displayed.
	 */
	public void renderString(String renderStr, double x, double y) {
	 
	    gl.glRasterPos2d(x, y);
	    glut.glutBitmapString(glut.GLUT_BITMAP_HELVETICA_10, renderStr);	
	}

        public void preInit() {

            doubleBuffer = true;
            stereoView = false;
        }

	// Initialise the canvas
        public void init() {

	    gl.glClearColor (0.0f, 0.0f, 0.0f, 0.0f);
	    gl.glMatrixMode(GL_PROJECTION);
	    gl.glLoadIdentity();
	    gl.glOrtho(0.0f, 300.0f, 0.0f, 300.0f, -1.0f, 1.0f);
	    gl.glMatrixMode(GL_MODELVIEW);
	    gl.glLoadIdentity();
	    glut = new GLUTFuncLightImplWithFonts(gl, glu);
	    loadGLTextures();
        }

        public void destroy() {
            cvsDispose();
        }

        /**
	 * Main drawing routine method. Is called continuously during
	 * runtime. Calls methods to render various objects which appear
	 * during the course of the game.
	 */
        public void display() {

            //Ensure GL is initialised correctly
            if (glj.gljMakeCurrent(true) == false) {

            	return;
	    }

            gl.glClear (GL_COLOR_BUFFER_BIT);

	    // Set colour to default (white)
	    gl.glColor3f(1.0f, 1.0f, 1.0f);

	    /**
	     * Continualy check AstApplication.class has the same value for
	     * the level the player is currently on. If the values are different
	     * the player must have advanced a level and the next background is
	     * loaded by calling the loadGLTextures() method.
	     */
	    if (_level != game.getLevel()) {

		loadGLTextures();
		_level = game.getLevel();
	    }

	    // Render the current background
	    renderBackgroundTexture();

	    // Check players ship has not been destroyed
	    if (ship.getExist()) {

		// Update ship movement and rotation
		ship.updateLocation_x();
		ship.updateLocation_y();
		ship.updateRotation();

		// Repeatedly draw new position and rotation of ship
		gl.glPushMatrix();

		    gl.glTranslated(ship.getLocation_x(), ship.getLocation_y(), 0);
	  	    gl.glRotated(-1*ship.getRotation(), 0, 0, 1);
		    gl.glTranslated(-1*ship.getLocation_x(), -1*ship.getLocation_y(), 0);
		    renderSpaceShip();

		gl.glPopMatrix();

		// Render ships shield (only appears briefly after collision)
		renderShield();

		// Check if ship is accelerating
		if (ship.getAccelerating()) {

		    // Draw ships rockets with the same position and rotation as the ship
		    gl.glPushMatrix();

			gl.glTranslated(ship.getLocation_x(), ship.getLocation_y(), 0);
	  		gl.glRotated(-1*ship.getRotation(), 0, 0, 1);
			gl.glTranslated(-1*ship.getLocation_x(), -1*ship.getLocation_y(), 0);
		    	renderRetroRockets();

		    gl.glPopMatrix();
		}

		// Check if hyperspace powerup has been obtained
		if (ship.getHyperspace()) {

		    // Render the hyperspace 'waves'
		    renderHyperspace();
		}

		// Check to see if goodie ship is firing
		if (goodieLaser.getExist()) {
			
		    // Update location of goodie laser
		    goodieLaser.updateLocation_x();
		    goodieLaser.updateLocation_y();

		    // Get rid of laser if it has travelled distance of screen
		    goodieLaser.setExist(goodieLaser.checkDistance());

		    // If seismic laser upgrade has been obtained...
		    if (ship.getLaserUpgraded()) {

			// Render purple lasers
			gl.glColor3f(1.0f, 0.0f, 1.0f);
		    }
		
		    else {

			// Render green lasers
			gl.glColor3f(0.0f, 1.0f, 0.0f);					
		    }

		    renderLaser(goodieLaser);
		}

		// Calls the method which randomly generates enemy ship is Game.class
		game.showEnemyShip(baddie);

		// If baddie ship is generated
		if (baddie.getExist()) {

		    // Update the location of the baddie ship
		    baddie.updateLocation_x();
		    baddie.updateLocation_y();

		    // Spin the baddie ship continuously
		    baddie.updateRotation();

		    gl.glPushMatrix();

			gl.glTranslated(baddie.getLocation_x(), baddie.getLocation_y(), 0);
	  		gl.glRotated(-1*baddie.getRotation(), 0, 0, 1);
			gl.glTranslated(-1*baddie.getLocation_x(), -1*baddie.getLocation_y(), 0);
		    	renderEnemyShip();

		    gl.glPopMatrix();

		    // Calls method in Game.class which randomly generates the baddie laser
		    baddie.generateFiring(baddieLaser, ship);

		    if (baddieLaser.getExist()) {

			// Update the location of the baddie laser
			baddieLaser.updateLocation_x();
			baddieLaser.updateLocation_y();

			// Get rid of laser if it had travelled distance of screen
			baddieLaser.setExist(baddieLaser.checkDistance());

			// Baddie laser is light red
			gl.glColor3f(1.0f, 0.5f, 0.5f);

			renderLaser(baddieLaser);
	 	    }

		    /**
		     * Continously check for all collisions between the goodie ship,
		     * the baddie ship and the two ships' lasers.
		     */
		    cd.checkCollisions(baddie, goodieLaser, baddieLaser, ship);
		}

		// Call the method in Game.class which randomly generates powerups
		game.showPowerUp(p);

		if (p.getExist()) {

		    // Update the location of the powerup
		    p.updateLocation_x();
		    p.updateLocation_y();

		    renderPowerUp();

		    // Continuously check for collision between the ship and the powerup
		    cd.checkCollisions(ship, p);
		}

		// Load current asteroids from Game.class
		for (int i = 0; i < game.getAsteroidCount(); i++) {

		    // Get asteroids one at a time...
		    Asteroid ast = game.getAsteroid(i);

		    // Render each asteroid
		    renderAsteroid(ast);
		}

		// Checks to see if the level should be increased
		game.checkLevel();

		/** 
		 * Render any explosions. Explosions are only rendered
		 * when an object is destroyed.
		 */
	    	renderExplosion();
	    }
	    
	    else { // Ship has been destroyed

		renderString("G  A  M  E    O  V  E  R  !", 125, 150);

		// Explode the ship!
		cd.startExplosion(ship.getLocation_x(), ship.getLocation_y(), 3);
		renderExplosion();

		// stop animating the canvas when the shock wave has finished		
		if (!cd.getExplosionExist()) {

		    stop();
		}
	    }

	    // Shock wave only appears when seismic laser has been obtained
	    renderShockWave();

	    // Render the play information at the top of the screen
	    gl.glColor3f(1.0f, 1.0f, 1.0f);
	    renderString("Emergency Brakes: " + (2 - ship.getBrakesUsed()), 40, 290);
	    renderString("Score: " + ship.getScore(), 265, 290);
	    renderString("Dan Coupar 2003", 255, 5);

	    // Change colour of text to red if shields are down
	    if (ship.getShieldsRemaining() == 0) {

		gl.glColor3f(1.0f, 0.0f, 0.0f);
	    }

	    // Display the amount of shields the ship has remaining
	    renderString("Shields: " + ship.getShieldsRemaining() + "%", 5, 290);
	    
            glj.gljSwap(); // flush the buffers etc
            glj.gljFree();    
	}
    }
}