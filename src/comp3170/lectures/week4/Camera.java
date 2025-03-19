package comp3170.lectures.week4;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import comp3170.SceneObject;
import comp3170.InputManager;

public class Camera extends SceneObject {

	private float zoom = 10.0f;
	
	private Matrix4f projectionMatrix = new Matrix4f();

	public Camera() {
	}
	
	
	public Matrix4f GetViewMatrix(Matrix4f dest) {
		Matrix4f viewMatrix = getMatrix();
		return viewMatrix.invert(dest);
	}
	
	public Matrix4f GetProjectionMatrix(Matrix4f dest) {
		return projectionMatrix.invert(dest);
	}
	
	private final float MOVE_SPEED = 0.5f;
	private final float ZOOM_SPEED = 1.0f;
	
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
			zoom -= ZOOM_SPEED * deltaTime;
		}
		
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			zoom += ZOOM_SPEED * deltaTime;
		}
		
		getMatrix().identity().translate(new Vector3f(xMove,yMove,0));
		projectionMatrix.scaling(zoom,zoom,1.0f);
		
	}
}