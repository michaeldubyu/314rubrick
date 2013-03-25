import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.opengl.GL11.*;

public class Game{
	private static CubeFace[] faces;
	private static Cube[] cubes;
	public static Color WHITE = new Color(1.0,1.0,1.0);
	public static Color BLACK = new Color(0.0,0.0,0.0);
	public static Color RED = new Color(1.0,0.0,0.0);
	
	final static float distZ = 10;
	static float angleX = 0;
	static float angleY = 0;

	public static void initCube(){
		
		faces = new CubeFace[7];
		cubes = new Cube[27];
		for (int k=0;k<27;k++){
			Cube c = new Cube();

			c.setColor0(BLACK);
			c.setColor1(WHITE);
			c.setColor2(RED);
			
			cubes[k] = c;
		}		
	}
	
    public void start() {
        try {
		    Display.setDisplayMode(new DisplayMode(800, 600));
		    Display.create();
		} catch (LWJGLException e) {
		    e.printStackTrace();
		    System.exit(0);
		}

        // init OpenGL here
        initCube();
        glShadeModel(GL_SMOOTH);              // Enable Smooth Shading
        glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // Black Background
        glClearDepth(1.0f);                      // Depth Buffer Setup
        glEnable(GL_DEPTH_TEST);              // Enables Depth Testing
        glDepthFunc(GL_LEQUAL);               // The Type Of Depth Testing To Do
        // Really Nice Perspective Calculations
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);  
  

        while (!Display.isCloseRequested()) {
	
		    // Clear the screen and depth buffer
		    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	
			
		    // set the color of the quad (R,G,B,A)
		    glColor3f(0.5f,0.5f,1.0f);
		    glMatrixMode(GL_PROJECTION);
	        
		    // set up view here
		    glLoadIdentity();
		    gluPerspective(45, 1, 1, 100 );
	        
		    glTranslatef(0,0,-distZ);
		    glRotatef(angleX,1,0,0);
		    glRotatef(angleY,0,1,0);
	       
	        glMatrixMode(GL_MODELVIEW);
		    // draw stuff here
	        solidCube();
	        
		    pollInput();
		    Display.update();
        }

	Display.destroy();
    }

    public void pollInput() {
			
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
		        	angleY += 10;
		        }
		        if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
		        	angleY -= 10;
		        }
		        if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
		        	angleX += 10;
		        }
		        if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
		        	angleX -= 10;
		        }
		    }
		}
    }

    public static void solidCube() {
        glBegin(GL_QUADS);            // Draw A Quad
	        glColor3f(0.0f, 1.0f, 0.0f);     // Set The Color To Green
	        glVertex3f(1.0f, 1.0f, -1.0f);   // Top Right Of The Quad (Top)
	        glVertex3f(-1.0f, 1.0f, -1.0f);  // Top Left Of The Quad (Top)
	        glVertex3f(-1.0f, 1.0f, 1.0f);   // Bottom Left Of The Quad (Top)
	        glVertex3f(1.0f, 1.0f, 1.0f);    // Bottom Right Of The Quad (Top)
	
	        glColor3f(1.0f, 0.5f, 0.0f);     // Set The Color To Orange
	        glVertex3f(1.0f, -1.0f, 1.0f);   // Top Right Of The Quad (Bottom)
	        glVertex3f(-1.0f, -1.0f, 1.0f);  // Top Left Of The Quad (Bottom)
	        glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Bottom)
	        glVertex3f(1.0f, -1.0f, -1.0f);  // Bottom Right Of The Quad (Bottom)
	
	        glColor3f(1.0f, 0.0f, 0.0f);     // Set The Color To Red
	        glVertex3f(1.0f, 1.0f, 1.0f);    // Top Right Of The Quad (Front)
	        glVertex3f(-1.0f, 1.0f, 1.0f);   // Top Left Of The Quad (Front)
	        glVertex3f(-1.0f, -1.0f, 1.0f);  // Bottom Left Of The Quad (Front)
	        glVertex3f(1.0f, -1.0f, 1.0f);   // Bottom Right Of The Quad (Front)
	
	        glColor3f(1.0f, 1.0f, 0.0f);     // Set The Color To Yellow
	        glVertex3f(1.0f, -1.0f, -1.0f);  // Bottom Left Of The Quad (Back)
	        glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad (Back)
	        glVertex3f(-1.0f, 1.0f, -1.0f);  // Top Right Of The Quad (Back)
	        glVertex3f(1.0f, 1.0f, -1.0f);   // Top Left Of The Quad (Back)
	
	        glColor3f(0.0f, 0.0f, 1.0f);     // Set The Color To Blue
	        glVertex3f(-1.0f, 1.0f, 1.0f);   // Top Right Of The Quad (Left)
	        glVertex3f(-1.0f, 1.0f, -1.0f);  // Top Left Of The Quad (Left)
	        glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Left)
	        glVertex3f(-1.0f, -1.0f, 1.0f);  // Bottom Right Of The Quad (Left)
	
	        glColor3f(1.0f, 0.0f, 1.0f);     // Set The Color To Violet
	        glVertex3f(1.0f, 1.0f, -1.0f);   // Top Right Of The Quad (Right)
	        glVertex3f(1.0f, 1.0f, 1.0f);    // Top Left Of The Quad (Right)
	        glVertex3f(1.0f, -1.0f, 1.0f);   // Bottom Left Of The Quad (Right)
	        glVertex3f(1.0f, -1.0f, -1.0f);  // Bottom Right Of The Quad (Right)
        glEnd();
    }
    
    public static void main(String[] argv) {
    	Game Game = new Game();
    	Game.start();
    }
}