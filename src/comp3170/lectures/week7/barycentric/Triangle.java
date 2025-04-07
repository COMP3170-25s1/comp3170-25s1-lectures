package comp3170.lectures.week7.barycentric;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Triangle extends SceneObject {

	private static final String VERTEX_SHADER = "vertex.glsl";
	private static final String FRAGMENT_SHADER = "fragment.glsl";
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector3f[] colour;
	private int colourBuffer;
	private int[] indices;
	private int indexBuffer;

	public Triangle() {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		// draw a triangle
		// @formatter:off

		vertices = new Vector4f[] {
			new Vector4f(-0.5f,  0.5f, 0, 1),
			new Vector4f(-0.5f, -0.5f, 0, 1),
			new Vector4f( 0.5f, -0.5f, 0, 1),
		};

		vertexBuffer = GLBuffers.createBuffer(vertices);

		colour = new Vector3f[] {
			new Vector3f(1,0,0),	// Red
			new Vector3f(0,1,0),	// Green
			new Vector3f(0,0,1),	// Blue
		};

		colourBuffer = GLBuffers.createBuffer(colour);

		indices = new int[] {
			0,1,2,
		};
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		// @formatter:on
	}

	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();

		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_colour", colourBuffer);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}

	private Vector2i mousePosition = new Vector2i();
	private Vector4f position = new Vector4f(0, 0, 0, 1);
	private static final float SELECT_DISTANCE = 0.1f;
	private int selectedVertex = -1;

	public void update(float deltaTime, InputManager input) {

		int w = BarycentricDemo.theWindow.getScreenWidth();
		int h = BarycentricDemo.theWindow.getScreenHeight();

		if (input.wasMouseClicked()) {
			// select the vertex closest to the mouse (if within SELECT_DISTANCE)
			input.getCursorPos(mousePosition);
			position.x = 2 * (float) mousePosition.x / w - 1;
			position.y = 1 - 2 * (float) mousePosition.y / h;

			float minDist = SELECT_DISTANCE;

			for (int i = 0; i < vertices.length; i++) {
				float d = position.distance(vertices[i]);

				if (d < minDist) {
					minDist = d;
					selectedVertex = i;
				}
			}
		} 
		
		if (selectedVertex >= 0) {
			if (input.isMouseDown()) {
				// Drag the selected vertex to the new location
				input.getCursorPos(mousePosition);
				vertices[selectedVertex].x = 2 * (float) mousePosition.x / w - 1;
				vertices[selectedVertex].y = 1 - 2 * (float) mousePosition.y / h;

				GLBuffers.updateBuffer(vertexBuffer, vertices);
			} else {
				// release the vertex
				selectedVertex = -1;
			}
		}

	}

}
