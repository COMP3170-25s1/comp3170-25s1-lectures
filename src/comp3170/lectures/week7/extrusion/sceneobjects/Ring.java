package comp3170.lectures.week7.extrusion.sceneobjects;

import static comp3170.Math.TAU;
import static comp3170.Math.cross;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Ring extends SceneObject {

	static final private String VERTEX_SHADER = "colourVertex.glsl";
	static final private String FRAGMENT_SHADER = "colourFragment.glsl";

	static final private String NORMAL_VERTEX_SHADER = "normalVertex.glsl";
	static final private String NORMAL_FRAGMENT_SHADER = "normalFragment.glsl";

	static final private String UV_VERTEX_SHADER = "uvVertex.glsl";
	static final private String UV_FRAGMENT_SHADER = "uvFragment.glsl";

	static final private int NSIDES = 10;
	static final private float RADIUS = 1;
	static final private float SCALE = 0.5f;

	static final private Vector2f UV_MAX = new Vector2f(8, 2);

	private Shader colourShader;
	private Shader normalShader;
	private Shader uvShader;

	private Vector4f[] vertices;
	private Vector4f[] normals;
	private Vector4f[] colours;
	private Vector2f[] uvs;
	private int vertexBuffer;
	private int normalBuffer;
	private int colourBuffer;
	private int uvBuffer;
	private int[] indices;
	private int indexBuffer;
	
	private Vector4f[] crossSection;
	private Vector4f[] crossSectionColour;
	private Vector4f[] crossSectionNormals;
	
	public Ring() {
		colourShader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		normalShader = ShaderLibrary.instance.compileShader(NORMAL_VERTEX_SHADER, NORMAL_FRAGMENT_SHADER);
		uvShader = ShaderLibrary.instance.compileShader(UV_VERTEX_SHADER, UV_FRAGMENT_SHADER);

		// create the curve
		
		Vector4f[] curve = new Vector4f[NSIDES+1];
		Vector4f[] tangent = new Vector4f[NSIDES+1];
		
		for (int i = 0; i <= NSIDES; i++) {
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

		crossSectionNormals = new Vector4f[] {
			new Vector4f( 0,-1, 0, 0),
			new Vector4f( 1, 0, 0, 0),
			new Vector4f( 0, 1, 0, 0),
			new Vector4f(-1, 0, 0, 0),
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
							
				int iNext = i + 1;				
				int side0 = 2 * n * i + 2 * j;
				int side1 = 2 * n * iNext + 2 * j;
				
				indices[k++] = side0;
				indices[k++] = side1;
				indices[k++] = side0 + 1;

				indices[k++] = side1 + 1;
				indices[k++] = side0 + 1;
				indices[k++] = side1;
			}
		}		

		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	private void createVertexBuffer(Vector4f[] curve, Vector4f[] tangent) {
		
		// cross-section
		//
		//  0----1
		//  |    |
		//  |    |
		//  3----2
		//
		// vertex buffer
		//
		//   0----1
		//  7      2		 
		//  |      |
		//  |      |
		//  6      3
		//   5----4

		
		int nVertices = (NSIDES + 1) * crossSection.length * 2;
		vertices = new Vector4f[nVertices];
		normals = new Vector4f[nVertices];
		colours = new Vector4f[nVertices];
		uvs = new Vector2f[nVertices];
		
		int k = 0;
		
		Vector4f vUp = new Vector4f(0,1,0,0);
		
		Vector4f point;
		Vector4f iAxis = new Vector4f();
		Vector4f jAxis = new Vector4f();
		Vector4f kAxis;
		
		Matrix4f matrix = new Matrix4f();
		Matrix4f normalMatrix = new Matrix4f();
		
		Vector4f[] transformedCrossSection = new Vector4f[NSIDES]; 
		Vector4f[] transformedNormals = new Vector4f[NSIDES]; 
		
		for (int i = 0; i <= NSIDES; i++) {
			point = curve[i];
			kAxis = tangent[i];
			
			cross(vUp, kAxis, iAxis); 	// i = vUP x k
			cross(kAxis, iAxis, jAxis); // j = k x i
			
			matrix.setColumn(0, iAxis);
			matrix.setColumn(1, jAxis);
			matrix.setColumn(2, kAxis);
			matrix.setColumn(3, point);
			
//			matrix.scaleLocal(0.5f, 1, 1);
//			matrix.scale(0.5f, 1, 1);			
			matrix.scale(SCALE);
//			matrix.rotateZ(i * TAU / NSIDES);
			
			// convert the matrix to a normal matrix to solve the normal stretching issue
			matrix.normal(normalMatrix);
			
			for (int j = 0; j < crossSection.length; j++) {
				transformedCrossSection[j] = crossSection[j].mul(matrix, new Vector4f());
				transformedNormals[j] = crossSectionNormals[j].mul(normalMatrix, new Vector4f()).normalize();
			}
			
			for (int j = 0; j < crossSection.length; j++) {
				float u = i * UV_MAX.x / NSIDES;
				
				vertices[k] = transformedCrossSection[j];
				normals[k] = transformedNormals[j];
				colours[k] = crossSectionColour[j];
				uvs[k] = new Vector2f(u,0);
				k++;
				
				int j1 = (j+1) % crossSection.length;
				vertices[k] = transformedCrossSection[j1];
				normals[k] = transformedNormals[j];
				colours[k] = crossSectionColour[j1];
				uvs[k] = new Vector2f(u, UV_MAX.y);
				k++;
			}
		}
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		normalBuffer = GLBuffers.createBuffer(normals);
		colourBuffer = GLBuffers.createBuffer(colours);
		uvBuffer = GLBuffers.createBuffer(uvs);
	}

	private Matrix4f modelMatrix = new Matrix4f();
	private Matrix4f normalMatrix = new Matrix4f();
	
	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		Shader shader = uvShader;
		shader.setStrict(false);

		shader.enable();

		// convert the model matrix to a normal matrix to solve the normal stretching issue
		getModelToWorldMatrix(modelMatrix);	// MODEL -> WORLD
		modelMatrix.normal(normalMatrix);
		
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_normalMatrix", normalMatrix);

		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_normal", normalBuffer);
		shader.setAttribute("a_colour", colourBuffer);
		shader.setAttribute("a_texcoord", uvBuffer);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);		
//		glDrawArrays(GL_POINTS, 0, vertices.length);
		
	}

	private static float ROTATION_SPEED = TAU / 4;
	
	public void update(float deltaTime, InputManager input) {
		//getMatrix().rotateX(ROTATION_SPEED * deltaTime);		
	}
	
	
	
}
