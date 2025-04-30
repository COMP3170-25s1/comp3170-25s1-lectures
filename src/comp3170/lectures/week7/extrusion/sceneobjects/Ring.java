package comp3170.lectures.week7.extrusion.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import static comp3170.Math.TAU;
import static comp3170.Math.cross;

public class Ring extends SceneObject {

	static final private String VERTEX_SHADER = "colourVertex.glsl";
	static final private String FRAGMENT_SHADER = "colourFragment.glsl";

	static final private int NSIDES = 100;
	static final private float RADIUS = 1;
	static final private float SCALE = 0.5f;
	
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector4f[] colours;
	private int colourBuffer;
	private Vector4f[] crossSection;
	private Vector4f[] crossSectionColour;
	
	
	public Ring() {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		// create the curve
		
		Vector4f[] curve = new Vector4f[NSIDES];
		Vector4f[] tangent = new Vector4f[NSIDES];
		vertices = new Vector4f[NSIDES * 2];
		
		for (int i = 0; i < NSIDES; i++) {
			float angle = i * TAU / NSIDES;
			
			curve[i] = new Vector4f(RADIUS, 0, 0, 1).rotateY(angle);
			tangent[i] = new Vector4f(0, 0, 1, 0).rotateY(angle);			
		}
		
		// square crosssection
		crossSection = new Vector4f[] {
			new Vector4f(-1, -1, 0, 1),
			new Vector4f( 1, -1, 0, 1),
			new Vector4f( 1,  1, 0, 1),
			new Vector4f(-1,  1, 0, 1),				
		};		

		crossSectionColour = new Vector4f[] {
			new Vector4f(0, 0.5f, 0, 1),
			new Vector4f(0, 0.5f, 0, 1),
			new Vector4f(1, 1, 0, 1),
			new Vector4f(1, 1, 0, 1),				
		};		
			

		// extrude cross-section along curve		
		createVertexBuffer(curve, tangent);
		
		// create index buffer
		createIndexBuffer(crossSection);
		
	}

	private void createIndexBuffer(Vector4f[] crossSection) {
		int n = crossSection.length;
		indices = new int[NSIDES * n * 2 * 3];
		
		int k = 0;
		for (int i = 0; i < NSIDES; i++) {
			for (int j = 0; j < n; j++) {

				int iNext = (i+1) % NSIDES;
				int jNext = (j+1) % n;
				
				indices[k++] = i * n + j;
				indices[k++] = iNext * n + j;
				indices[k++] = i * n + jNext;

				indices[k++] = iNext * n + jNext;
				indices[k++] = i * n + jNext;
				indices[k++] = iNext * n + j;			
			}
		}		

		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	private void createVertexBuffer(Vector4f[] curve, Vector4f[] tangent) {
		int nVertices = NSIDES * crossSection.length;
		vertices = new Vector4f[nVertices];
		colours = new Vector4f[nVertices];
		
		int k = 0;
		
		Vector4f vUp = new Vector4f(0,1,0,0);
		
		Vector4f point;
		Vector4f iAxis = new Vector4f();
		Vector4f jAxis = new Vector4f();
		Vector4f kAxis;
		
		Matrix4f matrix = new Matrix4f();
		
		for (int i = 0; i < NSIDES; i++) {
			point = curve[i];
			kAxis = tangent[i];
			
			cross(vUp, kAxis, iAxis); 	// i = vUP x k
			cross(kAxis, iAxis, jAxis); // j = k x i
			
			matrix.setColumn(0, iAxis);
			matrix.setColumn(1, jAxis);
			matrix.setColumn(2, kAxis);
			matrix.setColumn(3, point);
			matrix.scale(SCALE);
//			matrix.rotateZ(i * TAU / NSIDES);
			
			for (int j = 0; j < crossSection.length; j++) {
				vertices[k] = crossSection[j].mul(matrix, new Vector4f());
				colours[k] = crossSectionColour[j];
				k++;
			}
		}
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		colourBuffer = GLBuffers.createBuffer(colours);
	}

	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();

		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_colour", colourBuffer);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);		
//		glDrawArrays(GL_POINTS, 0, vertices.length);
		
	}
	
	
	
}
