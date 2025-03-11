package comp3170.lectures.week3;

import static org.lwjgl.opengl.GL15.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL15.glClear;
import static org.lwjgl.opengl.GL15.glClearColor;
import static org.lwjgl.opengl.GL15.glViewport;

import java.io.File;
import java.io.IOException;

import org.joml.Vector4f;

import comp3170.IWindowListener;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.Window;

public class Week3 implements IWindowListener{
	
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	private Vector4f clearColour = new Vector4f(0.0f, 0.0f, 0.5f, 1.0f);
	
	final private File DIRECTORY = new File("src/comp3170/lectures/week3/shaders");
	
	private Scene scene;
	
	public Week3() throws OpenGLException {
		
		// Create a window with a title, a size and a listener (this)
		Window window = new Window("Week 3", screenWidth, screenHeight, this);
		
		// Start running the window
		window.run();
	}

	public static void main(String[] args) throws OpenGLException {
		new Week3();

	}

	@Override
	public void init() {
		new ShaderLibrary(DIRECTORY);
		
		glClearColor(clearColour.x, clearColour.y, clearColour.z, clearColour.w);
		
		scene = new Scene();
	}

	@Override
	public void draw() {
		glClear(GL_COLOR_BUFFER_BIT);
		scene.draw();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void close() {
		
	}

}
