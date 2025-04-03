
package comp3170.lectures.week7;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.io.File;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.Window;

public class Week7 implements IWindowListener {

	public static Week7 theWindow;
	private static final File SHADER_DIR = new File("src/comp3170/lectures/week7");

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;
	private Scene scene;

	private InputManager input;
	private long oldTime;


	public Week7() throws OpenGLException {
		theWindow = this;
		window = new Window("Week 7", screenWidth, screenHeight, this);
		window.setSamples(4);	// set the number of samples or 0 to disable
		window.run();		
	}


	@Override
	public void init() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		new ShaderLibrary(SHADER_DIR);
		scene = new Scene();
		
		// initialise oldTime
		input = new InputManager(window);
		oldTime = System.currentTimeMillis();
	}

	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
		
		scene.update(deltaTime, input);
		input.clear();
	}

	@Override
	public void draw() {
		
		update();
		
		glClear(GL_COLOR_BUFFER_BIT);		
		scene.draw();
	}

	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
	
	@Override
	public void resize(int width, int height) {
		screenWidth = width;
		screenHeight = height;		
	}

	@Override
	public void close() {

	}

	public static void main(String[] args) throws OpenGLException {
		new Week7();
	}
}
