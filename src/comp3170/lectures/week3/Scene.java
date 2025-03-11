package comp3170.lectures.week3;

public class Scene {
	
	private House house;
	
	public Scene () {
		house = new House();
	}
	
	public void draw() {
		house.draw();
	}

}
