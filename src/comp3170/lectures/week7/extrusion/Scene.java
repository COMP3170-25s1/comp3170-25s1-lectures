package comp3170.lectures.week7.extrusion;

import org.joml.Matrix4f;

import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.lectures.common.sceneobjects.Axes3D;
import comp3170.lectures.common.sceneobjects.Grid;
import comp3170.lectures.week7.extrusion.sceneobjects.Ring;

public class Scene extends SceneObject {

	public static Scene theScene;
	private Camera camera;
	
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
		
		Ring ring = new Ring();
		ring.setParent(this);
		
		camera = new Camera();
		camera.setParent(this);
	}

	public void update(float deltaTime, InputManager input) {
		camera.update(deltaTime, input);		
	}

	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	@Override
	public void draw() {
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);
		
		projectionMatrix.mul(viewMatrix, mvpMatrix); // MVP = P * V
		draw(mvpMatrix);		
	}
	
	
	
}
