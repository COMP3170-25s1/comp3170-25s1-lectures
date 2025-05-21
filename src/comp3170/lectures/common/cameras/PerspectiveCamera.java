package comp3170.lectures.common.cameras;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_END;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.InputManager;
import comp3170.SceneObject;

public class PerspectiveCamera extends SceneObject implements ICamera {

	private float fovy;
	private float aspect;
	private float near;
	private float far;

	private float defaultFovy = TAU / 6;
	private final float FOVY_CHANGE = TAU / 6;

	public PerspectiveCamera(float fovy, float aspect, float near, float far) {
		this.fovy = fovy;
		this.aspect = aspect;
		this.near = near;
		this.far = far;
		
		defaultFovy = fovy;
	}

	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		return dest.setPerspective(fovy, aspect, near, far);
	}

	private Matrix4f modelToWorldMatrix = new Matrix4f();
	
	@Override
	public Matrix4f getViewMatrix(Matrix4f dest) {

		// invert the model matrix and remove scale
		getModelToWorldMatrix(modelToWorldMatrix);
		modelToWorldMatrix.invert(dest);
		dest.normalize3x3();
		
		return dest;
	}

	@Override
	public Vector4f getDirection(Vector4f dest) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public void update(float deltaTime, InputManager input) {
		if (input.isKeyDown(GLFW_KEY_PAGE_DOWN)) {
			fovy += FOVY_CHANGE * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_END)) {
			fovy = defaultFovy;
		}
		if (input.isKeyDown(GLFW_KEY_PAGE_UP)) {
			fovy -= FOVY_CHANGE * deltaTime;
		}

	}



}
