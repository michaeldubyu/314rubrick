import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import de.matthiasmann.twl.utils.PNGDecoder;
import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.opengl.GL11.*;

public class Game{
	private static CubeFace[] faces;
	private static Cube[] cubes;
	
	private static int tex;
	int fps;
	long lastFPS;
	
	public static Color WHITE = new Color(1.0,1.0,1.0);
	public static Color BLACK = new Color(0.0,0.0,0.0);
	public static Color RED = new Color(1.0,0.0,0.0);
	public static Color GREEN = new Color(0.0,1.0,0.0);
	public static Color BLUE = new Color(0.0,0.0,1.0);
	public static Color HALF = new Color(0.5,1.0,1.0);
	public static Color QUARTER = new Color(1.0,0.75,1.0);
	
	static FloatBuffer matDiffuse = BufferUtils.createFloatBuffer(4);
	static FloatBuffer matAmbient = BufferUtils.createFloatBuffer(4);
    static FloatBuffer matSpecular = BufferUtils.createFloatBuffer(4);
	
	static float scale = 0.5f;
	final static float distZ = 20;
	final static float distY = 30;
	static float angleX = 0;
	static float angleY = 0;

	public static void initCube(){
		
		faces = new CubeFace[7];
		cubes = new Cube[27];
		
		Cube c = new Cube(WHITE, QUARTER, RED, GREEN, BLUE, HALF);
		for (int l=0;l<27;l++){
			cubes[l] = c;
		}			
	    tex = setupTextures("res/textures/table.png");
	    
		matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		matAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
		matDiffuse.put(0.2f).put(0.2f).put(0.2f).put(1.0f).flip();
		
	    FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
	    lightPosition.put(0.0f).put(40.0f).put(0.0f).put(0.0f).flip();
	    
	    FloatBuffer spotDirection = BufferUtils.createFloatBuffer(4);
	    lightPosition.put(0.0f).put(0.0f).put(0.0f).put(0.0f).flip();
	    
	    FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
		ambient.put(0.6f).put(0.5f).put(0.5f).put(1.0f).flip();

	    glClearColor (0.0f, 0.0f, 0.0f, 0.0f);
	    
	    glEnable(GL_LIGHTING);
	    glEnable(GL_LIGHT0);
	    glEnable(GL_DEPTH_TEST);
	    
	    glShadeModel (GL_SMOOTH);
	    glEnable(GL_COLOR_MATERIAL);
	    
	    glLight(GL_LIGHT0, GL_POSITION, lightPosition);
	    glLight(GL_LIGHT0, GL_SPOT_DIRECTION, spotDirection);

	    glLightModel(GL_LIGHT_MODEL_AMBIENT, ambient);
	    glLightModeli(GL_LIGHT_MODEL_TWO_SIDE, GL_TRUE);
	    
	}
	
	private static int setupTextures(String filename) {
	    IntBuffer tmp = BufferUtils.createIntBuffer(1);
	    glGenTextures(tmp);
	    tmp.rewind();
	    try {
	        InputStream in = new FileInputStream(filename);
	        PNGDecoder decoder = new PNGDecoder(in);

	        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
	        decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
	        buf.flip();

	        glBindTexture(GL_TEXTURE_2D, tmp.get(0));
	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
	                GL_NEAREST);
	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
	                GL_NEAREST);
	        glPixelStorei(GL_UNPACK_ALIGNMENT, 4);
	        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
	        int unsigned = (buf.get(0) & 0xff);

	    } catch (java.io.FileNotFoundException ex) {
	        System.out.println("Error " + filename + " not found");
	    } catch (java.io.IOException e) {
	        System.out.println("Error decoding " + filename);
	    }
	    tmp.rewind();
	    return tmp.get(0);
	}
	
	public static void drawTable() {
        glEnable(GL_TEXTURE_2D);
	    glBindTexture(GL_TEXTURE_2D, tex);
	    glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);
	    glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);
	    glMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, matAmbient);
	    glBegin(GL_QUADS);
	    	glTexCoord2d(0,0);
	    	glNormal3f(0,1.0f,0);
	    	glVertex3f(-100,0,-100);
	    	
	    	glTexCoord2d(0,1);
	    	glNormal3f(0,1.0f,0);
	    	glVertex3f(100,0,-100);
	    	
	    	glTexCoord2d(1,1);
	    	glNormal3f(0,1.0f,0);
	    	glVertex3f(100,0,100);
	    	
	    	glTexCoord2d(1,0);	 
	    	glNormal3f(0,1.0f,0);
	    	glVertex3f(-100,0,100);
	    glEnd();
	    glDisable(GL_TEXTURE_2D);
	}
	
    public static void drawCube() {

    	//iterate over list of cubes, and draw
    	float spacing = 2.5f * scale;
    	int cc = 0;
    	
    	glTranslatef(-2.5f,0,0);
    	
    	for (int k=0;k<3 && cc < 27;k++){
    		glPushMatrix();
        	for (int i = 0;i < 3 && cc < 27;i++){
        		glPushMatrix();
        		for (int j = 0;j < 3 && cc < 27; j++, cc++){
    	    		//rendering the front 9 cubes
    	    		Cube c = cubes[cc];
    	    		
    	    		Color front = c.getColor0(); 
    	    		Color right = c.getColor1(); 
    	    		Color left = c.getColor2(); 
    	    		Color top = c.getColor3();
    	    		Color bottom = c.getColor4();
    	    		Color back = c.getColor5();
    	    		
    	    		glTranslatef(spacing,0,0);
    	    		glNormal3f(0f,0f,1f);
    	    		drawQuad(front, scale);
    	    		
    	    		glPushMatrix();
    	    		glRotatef(90,0,1,0);
    	    		glNormal3f(1.0f,0,0);
    	    		drawQuad(right, scale);
    	    		glPopMatrix();
    	    		
    	    		glPushMatrix();
    	    		glRotatef(-90,0,1,0);
    	    		glNormal3f(-1.0f,0,0);
    	    		drawQuad(left, scale);
    	    		glPopMatrix();
    	    		
    	    		glPushMatrix();
    	    		glRotatef(90,1,0,0);
    	    		glNormal3f(0,1.0f,0);
    	    		drawQuad(top, scale);
    	    		glPopMatrix();
    	    		
    	    		glPushMatrix();
    	    		glRotatef(-90,1,0,0);
    	    		glNormal3f(0,-1.0f,0);
    	    		drawQuad(bottom,scale);
    	    		glPopMatrix();
    	    		
    	    		glPushMatrix();
    	    		glRotatef(180,0,1,0);
    	    		glNormal3f(0,0,-1.0f);
    	    		drawQuad(back,scale);
    	    		glPopMatrix();
    	    		
        		}
        		glPopMatrix();
        		glTranslatef(0,spacing,0);
        	}
        	glPopMatrix();
        	glTranslatef(0,0,-spacing);
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
        glShadeModel(GL_SMOOTH);              
        glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    
        glClearDepth(1.0f);                      
        glEnable(GL_DEPTH_TEST);              
        glDepthFunc(GL_LEQUAL);               
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);  
 
        lastFPS = getTime(); //init FPS time

        while (!Display.isCloseRequested()) {
	
		    // Clear the screen and depth buffer
		    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	
			
		    // set the color of the quad (R,G,B,A)
		    glMatrixMode(GL_PROJECTION);
	        
		    // set up view here
		    glLoadIdentity();
		    gluPerspective(45, 1, 1, 250 ); //near of 1, far of 100
	      
	        glMatrixMode(GL_MODELVIEW);
	        glLoadIdentity();	

	        glTranslatef(0,-distY,-distZ);
			glRotatef(angleX,1,0,0);
			glRotatef(angleY,0,1,0);
		    
			//draw table
	        glPushMatrix();
	        drawTable();
	        glPopMatrix();
	        
	        //draw updates here
	        glPushMatrix();
	        glTranslatef(0,distY,0);
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
        	angleY += 5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
        	angleY -= 5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
        	angleX += 5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
        	angleX -= 5;
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