
package comp3170.lectures.week7.barycentric;

import comp3170.InputManager;
import comp3170.SceneObject;

public class Scene extends SceneObject {

	public static Scene theScene;
	private Triangle triangle;

	public Scene() {
		theScene = this;
		
		triangle = new Triangle();
		triangle.setParent(this);
	}

	public void update(float deltaTime, InputManager input) {
		triangle.update(deltaTime, input);
	}


	
}
