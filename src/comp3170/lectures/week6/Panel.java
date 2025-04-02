package comp3170.lectures.week6;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Matrix4f;


import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import static comp3170.Math.TAU;

import static org.lwjgl.opengl.GL15.glDrawElements;
import static org.lwjgl.opengl.GL15.glDrawArrays;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glPolygonMode;

import java.util.Arrays;

import static org.lwjgl.opengl.GL15.glPointSize;
import static org.lwjgl.opengl.GL15.glLineWidth;
import static org.lwjgl.opengl.GL15.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL15.GL_LINE;
import static org.lwjgl.opengl.GL15.GL_POINT;
import static org.lwjgl.opengl.GL15.GL_POINTS;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_UNSIGNED_INT;

public class Panel extends SceneObject {
	
	final private String VERTEX_SHADER = "vertex_simple.glsl";
	final private String FRAGMENT_SHADER = "fragment_simple.glsl";
	
	private Vector4f[] vertices; // An array of points that make up the house
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	// Paramaters
	private Vector3f baseColour = new Vector3f(1.0f,1.0f,1.0f);
	
	private Shader shader;
	
	public Panel(Vector3f baseColour) {
		this.baseColour = baseColour;
		
		// compile shader 
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		// @formatter:off
		
		vertices = new Vector4f[] {
				new Vector4f (-1.0f, -1.0f, 0.1f, 1.0f),  // 0
				new Vector4f (1.0f, -1.0f, 0.1f, 1.0f),	  // 1
				new Vector4f (1.0f, 1.0f, 0.1f, 1.0f),	  // 2
				new Vector4f (-1.0f, 1.0f, 0.1f, 1.0f),   // 3
				
				new Vector4f (-1.0f, -1.0f, -0.1f, 1.0f), // 4
				new Vector4f (1.0f, -1.0f, -0.1f, 1.0f),  // 5
				new Vector4f (1.0f, 1.0f, -0.1f, 1.0f),   // 6
				new Vector4f (-1.0f, 1.0f, -0.1f, 1.0f),  // 7
		};
		
		indices = new int[] {
				
				// Front
				0, 1, 2,
				0, 2, 3,
				
				// Back
				4, 5, 6,
				4, 6, 7,
				
				// Left Side
				0, 3, 7,
				0, 7, 4,
				
				// Right Side
				1, 5, 6,
				1, 6, 3,
						
		};
		// @formatter:on
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}
	
	
	public void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_colour", baseColour);
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
}
