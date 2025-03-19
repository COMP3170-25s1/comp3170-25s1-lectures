package comp3170.lectures.week4;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.GLBuffers;
import comp3170.InputManager;

import static comp3170.Math.TAU;

public class Camera extends SceneObject {
	
	private String VERTEX_SHADER = "vertex_simple.glsl";
	private String FRAGMENT_SHADER = "fragment_simple.glsl";
	
	private Vector3f position;
	private float angle;
	private float zoom = 10.0f;
	private float aspect; // Coming back t this later
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	
	private Vector3f solidColour = new Vector3f(1.0f,1.0f,1.0f); // SOLID BLACK RGB

	private Shader shader;
	
	private Matrix4f projectionMatrix = new Matrix4f();

	public Camera() {
		createMesh();
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);	
	}
	
	private void createMesh() {
		
		vertices = new Vector4f[] {
				new Vector4f(-1f, -1f, 0f, 1f),
				new Vector4f(1f, -1f, 0f, 1f),
				
				new Vector4f(1f, 1f, 0f, 1f),
				new Vector4f(-1f, 1f, 0f, 1f),
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);

	}
	
	public Matrix4f GetViewMatrix(Matrix4f dest) {
		Matrix4f viewMatrix = getMatrix();
		return viewMatrix.invert(dest);
	}
	
	public Matrix4f GetProjectionMatrix(Matrix4f dest) {
		return projectionMatrix.invert(dest);
	}
	
	
	public void drawSelf(Matrix4f mvpMatrix)
	{
		shader.enable();
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_mvpMatrix", mvpMatrix);

		shader.setUniform("u_colour", solidColour);
	
		// glDrawArrays(GL_LINE_LOOP, 0, vertices.length);
	}
	
	private final float MOVE_SPEED = 0.5f;
	private final float ZOOM_SPEED = 0.25f;
	
	private float xMove = 0f;
	private float yMove = 0f;
	
	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_W)) {
			yMove += MOVE_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			yMove -= MOVE_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_A)) {
			xMove -= MOVE_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			xMove += MOVE_SPEED * deltaTime;
		}
		
		if (input.isKeyDown(GLFW_KEY_UP)) {
			zoom += ZOOM_SPEED * deltaTime;
		}
		
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			zoom -= ZOOM_SPEED * deltaTime;
		}
		
		getMatrix().identity().translate(new Vector3f(xMove,yMove,0));
		projectionMatrix.scaling(zoom,zoom,1.0f);
		
	}
}