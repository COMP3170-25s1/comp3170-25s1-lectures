package comp3170.lectures.week6;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import comp3170.SceneObject;
import comp3170.InputManager;

import static comp3170.Math.TAU;

public class Camera extends SceneObject {

	// Perspective camera values
	private static final float ASPECT = 1;
	private static final float FOVY = TAU / 4; // Not final so it can be manipulated in demo
	
	// Orthographic camera values
	private float width = 10.0f;
	private float height = 10.0f;
	
	// Generic values
	private static final float NEAR = 0.1f;
	private static final float FAR = 30f;
	private float distance = 5.0f;
	private boolean perspectiveMode = false; 
	
	private Matrix4f viewMatrix = new Matrix4f();
	
	public Matrix4f GetViewMatrix(Matrix4f dest) {
		viewMatrix = getMatrix();
		return viewMatrix.invert(dest);
	}
	
	public Matrix4f GetProjectionMatrix(Matrix4f dest) {
		if (perspectiveMode) {
		return dest.setPerspective(FOVY, ASPECT, NEAR, FAR); 
		}
		return dest.setOrtho(-width / 2, width / 2, -height / 2, height / 2, NEAR, FAR);
	}
	
	private final float ROTATE_RATE = TAU/12;
	private final float MOVE_SPEED = 0.5f;
	private final float ZOOM = 5.0f;
	
	private float yAngle = 0f;
	private float xAngle = 0f;

	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_UP)) {
			distance -= MOVE_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			distance += MOVE_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_W)) {
			xAngle += ROTATE_RATE * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_S)) {
			xAngle -= ROTATE_RATE * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_A)) {
			yAngle -= ROTATE_RATE * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_D)) {
			yAngle += ROTATE_RATE * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_O)) {
			width += ZOOM * deltaTime;
			height += ZOOM * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_I)) {
			width -= ZOOM * deltaTime;
			height -= ZOOM * deltaTime;			
		}
		
		if (input.wasKeyPressed(GLFW_KEY_P))
		{
			perspectiveMode = !perspectiveMode;
		}
		
		getMatrix().identity().rotateY(yAngle).rotateX(xAngle).translate(0.0f,0.0f,distance);		
	}
}