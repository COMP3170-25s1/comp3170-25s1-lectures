package comp3170.lectures.week1;

import static org.lwjgl.opengl.GL15.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL15.glClear;
import static org.lwjgl.opengl.GL15.glClearColor;
import static org.lwjgl.opengl.GL15.glViewport;

import java.io.File;
import java.io.IOException;

import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.Window;

public class Week1 implements IWindowListener { 

	// Point towards where our shaders are kept
	final private File DIRECTORY = new File("src/comp3170/lectures/week1");
	
	// The actual window. This class has been provided to you in the 3170 library.
	private Window window;

	// Values to determine the width and height of the screen.
	private int screenWidth = 800;
	private int screenHeight = 800;
	
	 // Scenes are used to instantiate, order and draw our objects.
	private Scene scene;
	
	public Week1() throws OpenGLException {
		// Create the window with the specified dimensions
		window = new Window("Hello Triangle!", screenWidth, screenHeight, this);
		
		// Can the window be resized?
		window.setResizable(false);
		
		//Run the window
		window.run();
	}

	
	public void init() { // Run when the window is launched
		new ShaderLibrary(DIRECTORY); // Create a singleton instance of the shader library, pointing to the directory.
		scene = new Scene(screenWidth, screenHeight); // Instantiate the scene.
		
		//Set the clear colour - think of it as the background colour.
		glClearColor(0.13f, 0.21f, 0.33f, 1.0f); // Unity blue
	}		
	
	public void draw() { // draw is called every frame.
				
		//clear the color buffer
		glClear(GL_COLOR_BUFFER_BIT);
		
		scene.draw(); // Call the draw function on the scene
	}
	@Override
	// Control what happens when the screen is resized - generally you just want to adjust the viewport.
	public void resize(int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
		
		// lower left corner x origin, lower left corner y origin, width, height
		glViewport(0,0, width, height);		
	}

	@Override
	public void close() {
		
	}
	
	public static void main(String[] args) throws IOException, OpenGLException {
		new Week1();
	}

}
