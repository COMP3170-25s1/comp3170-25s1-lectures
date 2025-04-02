package comp3170.lectures.week6;

import org.joml.Vector3f;

import comp3170.InputManager;
import comp3170.SceneObject;
import static comp3170.Math.TAU;

public class Scene extends SceneObject {
	
	public static Scene theScene;

	private Camera camera;
	private Gem blueGem;
	private Gem redGem;
	
	private Vector3f blueColour = new Vector3f(0.0f, 0.8f, 0.8f);
	private Vector3f redColour = new Vector3f(0.8f, 0.0f, 0.0f);
	private float gemSize = 1.0f;
	private int gemSides = 12;

	public Scene () {		
		theScene = this;
		
		blueGem = new Gem(blueColour, gemSize, gemSides);
		blueGem.setParent(this);
		blueGem.getMatrix().translate(2.0f,0.0f,0.0f);
		
		redGem = new Gem(redColour, gemSize, gemSides);
		redGem.setParent(this);
		redGem.getMatrix().translate(-2.0f,0.0f,1.0f);
		
		camera = new Camera();
		camera.setParent(this);
	}
	
	public Camera GetCamera() {
		return camera;
	}

	public void update(InputManager input, float deltaTime) {
		camera.update(input, deltaTime);
	}
}