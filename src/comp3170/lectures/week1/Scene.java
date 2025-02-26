package comp3170.lectures.week1;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL11.glLineWidth;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Scene {

	// Specify the shaders being used
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private Vector4f[] vertices; // Vector4 to store the verticies of our triangle!
	private int vertexBuffer;
	private Shader shader;

	private int screenWidth;
	private int screenHeight;
	
	private float triWidth = 2.0f;
	private float triHeight = 2.0f;

	public Scene(int width, int height) {

		screenWidth = width;
		screenHeight = height;

		/*
		 * compile the shader info on what is going on under the hood in the textbook.
		 * You won't be writing your own compiler in this unit.
		 */
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		// vertices of a triangle as (x,y) pairs
		// @formatter:off
		
		float x = triWidth/2;
		float y = triHeight/2;
		
		vertices = new Vector4f[] {
				new Vector4f(-x,-y,0.0f,1.0f), // Left
				new Vector4f(x,-y,0.0f,1.0f), // Right
				new Vector4f(0.0f,y,0.0f,1.0f), // Top
		};
		// @formatter: On
		
		// copy the data into a Vertex Buffer Object in graphics memory
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		glPointSize(10f); // An optional operation to set the size of points.
		glLineWidth(10f); // An optional operation to set the size of points.
	}

	public void draw() {
		// activate the shader
		shader.enable();

		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);

		// write the colour value into the u_colour uniform
		Vector3f colour = new Vector3f(0.0f,0.0f,0.0f); // Black
		shader.setUniform("u_colour", colour);
		
		// pass the size of the screen into the shader
		Vector2f screenSize = new Vector2f(screenWidth, screenHeight);
		shader.setUniform("u_screenSize", screenSize);
			
		glDrawArrays(GL_TRIANGLES, 0, vertices.length);
	}
}