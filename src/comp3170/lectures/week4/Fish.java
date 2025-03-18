package comp3170.lectures.week4;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Matrix4f;


import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import static comp3170.Math.TAU;

import static org.lwjgl.opengl.GL15.glDrawElements;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_UNSIGNED_INT;

public class Fish {
	
	// Define which shaders are to be used
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";
	
	private Vector4f[] vertices; // An array of points that make up the house
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	
	private Vector3f[] colours;
	private int colourBuffer;
	
	private Shader shader;
	
	private Matrix4f modelMatrix = new Matrix4f();
	
	// private Vector3f colour = new Vector3f(1.0f, 0.7f, 1.0f); // LILAC - No longer used now we're doing vertex colours
	
	final private Vector3f OFFSET = new Vector3f(0.25f,0.0f, 0.0f);
	final private float MOVEMENT_SPEED = 1f;
	final private float SIZE = 0.05f;
	final private float ROTATION_RATE = TAU/12;
	
	public Fish() {
		
		// compile shader 
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// vertices as (x,y) pairs
		
		// @formatter:off
		
		vertices = new Vector4f[] {
				new Vector4f(-0.8f, -1.0f, 0.0f, 1.0f), // P0
				new Vector4f(0.8f, -1.0f, 0.0f, 1.0f),  // P1

				new Vector4f(0.1f, -0.3f, 0.0f, 1.0f),   // P2
				new Vector4f(-0.1f, -0.3f, 0.0f, 1.0f),  // P3
				
				new Vector4f(0.8f, 0.2f, 0.0f, 1.0f),   // P4
				new Vector4f(-0.8f, 0.2f, 0.0f, 1.0f),  // P5
				new Vector4f(0.f, 1.0f, 0.0f, 1.0f),    // P6	
			
		};
		
		indices = new int[] {
				0, 1, 2,
				2, 3, 0,
				
				2, 4, 3,
				3, 4, 5,
				4, 5, 6,
		};
		
		colours = new Vector3f[] {
				new Vector3f(1.0f, 0.0f, 0.1f),
				new Vector3f(0.8f, 0.3f, 0.15f),
				
				new Vector3f(1.f, 0.8f, 0.5f),
				new Vector3f(0.75f, 1.0f, 0.0f),
				
				new Vector3f(1.0f, 0.5f, 0.0f),
				
				new Vector3f(1.f, 0.8f, 0.5f),
				new Vector3f(0.75f, 1.0f, 0.0f),
				
				new Vector3f(0.75f, 1.0f, 0.0f),
		};
		// @formatter:on
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		colourBuffer = GLBuffers.createBuffer(colours);
		
		modelMatrix.translate(OFFSET).scale(SIZE); // T (R) S order
	}
	
	public void update(float deltaTime) {
		
		float movement = MOVEMENT_SPEED * deltaTime;
		float rotation = ROTATION_RATE * deltaTime;
		modelMatrix.translate(0.0f,movement,0.0f).rotateZ(rotation);
	}
	
	public void draw() {
		shader.enable();
		
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_modelMatrix", modelMatrix);
		
		shader.setAttribute("a_colour",colourBuffer);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);

		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
}
