package comp3170.lectures.week7.extrusion;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

import org.joml.Matrix4f;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.lectures.common.cameras.ICamera;
import comp3170.lectures.common.sceneobjects.Axes3D;
import comp3170.lectures.week7.extrusion.sceneobjects.DirectionalLight;
import comp3170.lectures.week7.extrusion.sceneobjects.RenderTextureQuad;
import comp3170.lectures.week7.extrusion.sceneobjects.Ring;

public class Scene extends SceneObject {

	static final private String EDGE_VERTEX_SHADER = "edgeVertex.glsl";
	static final private String EDGE_FRAGMENT_SHADER = "edgeFragment.glsl";

	public static Scene theScene;
	private Camera camera;
	private Ring ring;
	private DirectionalLight light;
	private RenderTextureQuad quad;
	
	public Scene() {
		theScene = this;		
		
		// scene -+-> grid
		//        |
		//        +-> axes
		//        |
		//        +-> camera -> arm -> camera
		
//		Grid grid = new Grid(10);
//		grid.setParent(this);

		Axes3D axes = new Axes3D();
		axes.setParent(this);
		
		ring = new Ring();
		ring.setParent(this);
		
		light = new DirectionalLight();
		light.setParent(this);
		
		camera = new Camera();
		camera.setParent(this);
		
		int screenWidth = ExtrusionDemo.theWindow.getScreenWidth();
		int screenHeight = ExtrusionDemo.theWindow.getScreenHeight();
		
		Shader edgeShader = ShaderLibrary.instance.compileShader(EDGE_VERTEX_SHADER, EDGE_FRAGMENT_SHADER);
		quad = new RenderTextureQuad(edgeShader, screenWidth, screenHeight);  
	}

	public DirectionalLight getLight() {
		return light;
	}

	public ICamera getCamera() {
		return camera;
	}
	
	public void update(float deltaTime, InputManager input) {
		camera.update(deltaTime, input);
		ring.update(deltaTime, input);
		light.update(deltaTime, input);
	}

	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	@Override
	public void draw() {
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);		
		projectionMatrix.mul(viewMatrix, mvpMatrix); // MVP = P * V
		
		// Pass 1 - Draw the geometry to the render texture 
		
		int frameBuffer = quad.getFrameBuffer();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		glClear(GL_COLOR_BUFFER_BIT);		
		glClear(GL_DEPTH_BUFFER_BIT);
		draw(mvpMatrix, 1);	
		
		// Pass 2 - Draw the scene to the screen using the edge shader 
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glClear(GL_COLOR_BUFFER_BIT);		
		glClear(GL_DEPTH_BUFFER_BIT);
		quad.draw();		
	}
	
	
	
}
