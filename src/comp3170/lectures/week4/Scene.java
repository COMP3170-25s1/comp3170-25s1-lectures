package comp3170.lectures.week4;

public class Scene {
	
	private Fish fish;
	
	public Scene () {
		fish = new Fish();
	}
	
	public void draw() {
		fish.draw();
	}
	
	public void update(float deltaTime) {
		fish.update(deltaTime);
	}
}
