package comp3170.lectures.week5;

import org.joml.Vector3f;

import comp3170.InputManager;
import comp3170.SceneObject;
import static comp3170.Math.TAU;

public class Scene extends SceneObject {
	
	public static Scene theScene;
	
	// NOTE: In a real project, I'd probably want to create something to hold all these fish. Remember that example code like this is fit for examples and demonstration, not optimised for a full scale project.
	private Fish fish1;
	private Fish fish2;
	private Camera camera;
	private Gem gem;
	
	private Vector3f fish1Offset = new Vector3f(5.0f,0.0f, 0.0f);
	private Vector3f fish1Colour = new Vector3f(0.5f,0.0f,0.0f); // RED
	private Vector3f fish2Offset = new Vector3f(-5.0f,5.0f, 0.0f);
	private Vector3f fish2Colour = new Vector3f(0.0f,0.5f,0.0f); // GREEN
	
	private Vector3f gemColour = new Vector3f(1.0f,1.0f,1.0f);

	public Scene () {
		
		theScene = this;
		
		fish1 = new Fish(fish1Colour);
		fish1.setParent(this);
		fish1.getMatrix().translateLocal(fish1Offset);
		
		fish2 = new Fish(fish2Colour);
		fish2.setParent(this);
		fish2.getMatrix().translateLocal(fish2Offset);
		
		gem = new Gem(gemColour, 1, 8);
		gem.setParent(this);
		
		camera = new Camera();
		camera.setParent(this);
		
	}
	
	public Camera GetCamera() {
		return camera;
	}

	public void update(InputManager input, float deltaTime) {
		fish1.update(deltaTime);
		fish2.update(deltaTime);
		camera.update(input, deltaTime);
		gem.update(input, deltaTime);
	}
}
