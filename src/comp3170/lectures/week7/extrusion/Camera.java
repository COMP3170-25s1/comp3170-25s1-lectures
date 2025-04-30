package comp3170.lectures.week7.extrusion;

import org.joml.Matrix4f;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.lectures.common.cameras.ICamera;
import comp3170.lectures.common.cameras.OrbitingArmature;
import comp3170.lectures.common.cameras.OrthographicCamera;
import comp3170.lectures.common.cameras.PerspectiveCamera;
import static comp3170.Math.TAU;

public class Camera extends SceneObject implements ICamera {

	private static final float WIDTH = 3;
	private static final float HEIGHT = 3;
	private static final float NEAR = 0.1f;
	private static final float FAR = 10;
	private static final float DISTANCE = (NEAR + FAR) / 2;
	
	private PerspectiveCamera camera;
//	private OrthographicCamera camera;
	private OrbitingArmature arm;
	
	public Camera() {
		camera = new PerspectiveCamera(TAU / 4, 1, NEAR, FAR);
//		camera = new OrthographicCamera(WIDTH, HEIGHT, NEAR, FAR);
		arm = new OrbitingArmature(DISTANCE);
		
		arm.setParent(this);
		camera.setParent(arm);
	}
	
	@Override
	public Matrix4f getViewMatrix(Matrix4f dest) {
		return camera.getViewMatrix(dest);
	}

	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {
		return camera.getProjectionMatrix(dest);
	}

	public void update(float deltaTime, InputManager input) {
		arm.update(deltaTime, input);
		camera.update(deltaTime, input);		
	}

}
