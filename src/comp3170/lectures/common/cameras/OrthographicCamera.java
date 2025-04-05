package comp3170.lectures.common.cameras;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;

import comp3170.InputManager;
import comp3170.SceneObject;

public class OrthographicCamera extends SceneObject implements ICamera {

	private float width;
	private float height;
	private float near;
	private float far;

	private float defaultZoom;
	private final float ZOOM_CHANGE = 2f;

	public OrthographicCamera(float width, float height, float near, float far) {
		this.width = width;
		this.height = height;
		this.near = near;
		this.far = far;

		defaultZoom = width;
	}

	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		return dest.setOrtho(-width / 2, width / 2, -height / 2, height / 2, near, far);
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

	public void update(float deltaTime, InputManager input) {

		if (input.isKeyDown(GLFW_KEY_PAGE_UP)) {
			width += ZOOM_CHANGE * deltaTime;
			height += ZOOM_CHANGE * deltaTime;
		}

		if (input.isKeyDown(GLFW_KEY_PAGE_DOWN)) {
			width -= ZOOM_CHANGE * deltaTime;
			height -= ZOOM_CHANGE * deltaTime;
		}
	}


}
