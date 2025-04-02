package comp3170.lectures.week2;

import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.ShaderLibrary;

import static org.lwjgl.opengl.GL15.glDrawElements;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_UNSIGNED_INT;

public class House {
	
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
		
	public House() {
		
		// compile shader 
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// @formatter:off
		vertices = new Vector4f[] {
				new Vector4f(-0.8f, -0.8f, 0.0f, 1.0f), // P0
				new Vector4f(0.8f, -0.8f, 0.0f, 1.0f),  // P1
				
				new Vector4f(0.8f, 0.4f, 0.0f, 1.0f),   // P2
				new Vector4f(-0.8f, 0.4f, 0.0f, 1.0f),  // P3
				new Vector4f(0.f, 0.8f, 0.0f, 1.0f),    // P4	
		};
		
		indices = new int[] {
				0, 1, 3, // Tri1
				1, 2, 3, // Tri2
				2, 4, 3,// Tri3
		};
		
		colours = new Vector3f[] {
				new Vector3f(1.0f, 0.0f, 0.1f),
				new Vector3f(0.8f, 0.3f, 0.15f),
				
				new Vector3f(1.f, 0.8f, 0.5f),
				new Vector3f(0.75f, 1.0f, 0.0f),
				
				new Vector3f(1.0f, 0.5f, 0.0f),
		};
		// @formatter:on
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		colourBuffer = GLBuffers.createBuffer(colours);
	}
	
	public void draw() {
		shader.enable();
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_colour",colourBuffer);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
		
	}

}
