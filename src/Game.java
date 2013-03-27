import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.opengl.GL11.*;

public class Game{
	private static CubeFace[] faces;
	private static Cube[] cubes;
	int fps;
	long lastFPS;
	
	public static Color WHITE = new Color(1.0,1.0,1.0);
	public static Color BLACK = new Color(0.0,0.0,0.0);
	public static Color RED = new Color(1.0,0.0,0.0);
	public static Color GREEN = new Color(0.0,1.0,0.0);
	public static Color BLUE = new Color(0.0,0.0,1.0);
	public static Color HALF = new Color(0.5,0.5,0.5);
	public static Color QUARTER = new Color(0.75,0.75,0.75);
	
	static float scale = 0.5f;
	final static float distZ = 25;
	static float angleX = 0;
	static float angleY = 0;

	public static void initCube(){
		
		faces = new CubeFace[7];
		cubes = new Cube[27];
		
		for (int l=0;l<9;l++){
			//initialize the front 9 cubes
			Cube c = new Cube();

			c.setColor0(BLUE);
			c.setColor1(WHITE);
			c.setColor2(RED);
			
			if (l==1 || l==4 || l==7){
				c.setColor1(BLACK); 
				c.setColor2(BLACK);
			}
			
			if (l==2 || l==5 || l==8){
				c.setColor2(BLACK);
			}
			
			if (l%3 == 0){
				c.setColor1(BLACK);
			}
	
			cubes[l] = c;
		}	
		
		for (int l=9;l<18;l++){
			//initialize the middle 9 cubes
			Cube c = new Cube();

			c.setColor0(GREEN);
			c.setColor1(HALF);
			c.setColor2(QUARTER);
			
			cubes[l] = c;		
		}
		
		for (int l=18;l<27;l++){
			//initialize the back 9 cubes
			Cube c = new Cube();

			c.setColor0(WHITE);
			c.setColor1(WHITE);
			c.setColor2(WHITE);
			
			cubes[l] = c;		
		}
	}
	
    public static void drawCube() {
    	//designate cube[0...9] to be the first 9 cubes
    	//designate cube[9...18] to be the middle 9 cubes
    	//designate cube[18..27] to be the final 9 cubes
    	//iterate over list of cubes, and draw
    	float spacing = 2.5f * scale;
    	for (int i = 0, cc = 0;i < 3 && cc < 9;i++){
    		glPushMatrix();
    		for (int j = 0;j < 3; j++, cc++){
	    		//rendering the front 9 cubes
	    		Cube c = cubes[cc];
	    		
	    		Color z = c.getColor0();
	    		Color x = c.getColor1();
	    		Color y = c.getColor2();
	    		
	    		glTranslatef(spacing,0,0);
	    		drawQuad(z, scale);
	    		
	    		glPushMatrix();
	    		glRotatef(90,0,1,0);
	    		drawQuad(x, scale);
	    		glPopMatrix();
	    		
	    		glPushMatrix();
	    		glRotatef(-90,0,1,0);
	    		drawQuad(y, scale);
	    		glPopMatrix();
    		}
    		glPopMatrix();
    		glTranslatef(0,spacing,0);
    	}
    	

    }
    
    public static void drawQuad(Color c, float scale){
    	double r = c.getR();
    	double g = c.getG();
    	double b = c.getB();
    	
    	glPushMatrix();
        glBegin(GL_QUADS);                    
	        glColor3d(r,g,b);   	  // set the color
	        glVertex3f(1.0f * scale, 1.0f * scale, 1.0f * scale);   // draw top right
	        glVertex3f(-1.0f * scale, 1.0f * scale, 1.0f * scale);  // Top Left Of The Quad (Top)
	        glVertex3f(-1.0f * scale, -1.0f * scale, 1.0f * scale);   // Bottom Left Of The Quad (Top)
	        glVertex3f(1.0f * scale, -1.0f * scale, 1.0f * scale);    // Bottom Right Of The Quad (Top)
        glEnd();
        glPopMatrix();
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
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);  
 
        lastFPS = getTime(); //init FPS time

        while (!Display.isCloseRequested()) {
	
		    // Clear the screen and depth buffer
		    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	
			
		    // set the color of the quad (R,G,B,A)
		    glColor3f(0.5f,0.5f,1.0f);
		    glMatrixMode(GL_PROJECTION);
	        
		    // set up view here
		    glLoadIdentity();
		    gluPerspective(45, 1, 1, 100 ); //near of 1, far of 100
	        
		    glTranslatef(0,0,-distZ);
		    glRotatef(angleX,1,0,0);
		    glRotatef(angleY,0,1,0);
	       
	        glMatrixMode(GL_MODELVIEW);
	        
	        //draw updates here
	        glPushMatrix();
			drawCube();
			glPopMatrix();

		    pollInput();
		 
		    Display.update();
		    Display.sync(60);
		    updateFPS();
        }

	Display.destroy();
    }

    public void pollInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
        	angleY += 10;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
        	angleY -= 10;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
        	angleX += 10;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
        	angleX -= 10;
        }
    }
    
    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("Rubrick 9000 - FPS: " + fps); 
            fps = 0; //reset the FPS counter
            lastFPS += 1000; //add one second
        }
        fps++;
    }
    
    public long getTime(){
    	return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    
    public static void main(String[] argv) {
    	Game Game = new Game();
    	Game.start();
    }
}