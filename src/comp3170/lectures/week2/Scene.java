package comp3170.lectures.week2;

public class Scene {
	private House house;
	
	public Scene () {
		house = new House();
	}
	
	public void draw() {
		house.draw();
	}

}
