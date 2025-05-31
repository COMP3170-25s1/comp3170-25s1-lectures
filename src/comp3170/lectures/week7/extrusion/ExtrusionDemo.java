
package comp3170.lectures.week7.extrusion;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;

import java.io.File;

import comp3170.IWindowListener;
import comp3170.InputManager;
import comp3170.OpenGLException;
import comp3170.ShaderLibrary;
import comp3170.TextureLibrary;
import comp3170.Window;

public class ExtrusionDemo implements IWindowListener {

	public static ExtrusionDemo theWindow;
	private static final File COMMON_DIR = new File("src/comp3170/lectures/common/shaders");
	private static final File SHADER_DIR = new File("src/comp3170/lectures/week7/shaders");
	private static final File TEXTURE_DIR = new File("src/comp3170/lectures/week7/textures");

	private Window window;
	private int screenWidth = 800;
	private int screenHeight = 800;
	private Scene scene;

	private InputManager input;
	private long oldTime;


	public ExtrusionDemo() throws OpenGLException {
		theWindow = this;
		window = new Window("Extrusion demo", screenWidth, screenHeight, this);
		window.setSamples(4);	// set the number of samples or 0 to disable
		window.run();		
	}


	@Override
	public void init() {
		glDisable(GL_PROGRAM_POINT_SIZE);
		glPointSize(5);

		glEnable(GL_CULL_FACE);		// backface culling
		glEnable(GL_DEPTH_TEST);	// depth buffer
		
		new ShaderLibrary(COMMON_DIR).addPath(SHADER_DIR);
		new TextureLibrary(TEXTURE_DIR);
	
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
		glClear(GL_DEPTH_BUFFER_BIT);

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
		new ExtrusionDemo();
	}
}
