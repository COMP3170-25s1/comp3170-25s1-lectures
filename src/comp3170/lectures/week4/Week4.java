package comp3170.lectures.week4;

import static org.lwjgl.opengl.GL15.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL15.glClear;
import static org.lwjgl.opengl.GL15.glClearColor;
import static org.lwjgl.opengl.GL15.glViewport;

import java.io.File;
import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.Window;

public class Week4 implements IWindowListener{
	
	private int screenWidth = 1000;
	private int screenHeight = 500;
	private Vector4f clearColour = new Vector4f(0.0f, 0.0f, 0.5f, 1.0f);
	
	final private File DIRECTORY = new File("src/comp3170/lectures/common/shaders");
	
	private Scene scene;
	private long oldTime;
	private InputManager input;
	private Window window;
	
	// Matricies
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();
	
	public Week4() throws OpenGLException {
		
		// Create a window with a title, a size and a listener (this)
		window = new Window("Week 4", screenWidth, screenHeight, this);
		
		// Start running the window
		window.run();
		window.setResizable(true);
	}

	public static void main(String[] args) throws OpenGLException {
		new Week4();

	}

	@Override
	public void init() {
		new ShaderLibrary(DIRECTORY);
		
		glClearColor(clearColour.x, clearColour.y, clearColour.z, clearColour.w);
		
		scene = new Scene();
		input = new InputManager(window);
		
		oldTime = System.currentTimeMillis();
	}


	@Override
	public void draw() {
		update();
		glClear(GL_COLOR_BUFFER_BIT);
		
		Camera cam = scene.GetCamera();
		cam.GetViewMatrix(viewMatrix);
		cam.GetProjectionMatrix(projectionMatrix);
		mvpMatrix.identity();
		mvpMatrix.mul(viewMatrix).mul(projectionMatrix);
		scene.draw(mvpMatrix);
	}
	
	public void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
		scene.update(input, deltaTime);
		input.clear();
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void close() {
		
	}

}
