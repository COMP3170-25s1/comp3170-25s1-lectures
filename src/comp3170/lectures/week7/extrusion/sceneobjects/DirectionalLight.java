
package comp3170.lectures.week7.extrusion.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class DirectionalLight extends SceneObject {
	
	static final private String VERTEX_SHADER = "lightVertex.glsl";
	static final private String FRAGMENT_SHADER = "lightFragment.glsl";

	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;

	private Vector3f intensity = new Vector3f(1,1,1);
	
	public DirectionalLight() {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		vertices = new Vector4f[] {
			new Vector4f(0,0,0,1),
			new Vector4f(0,0,1,1),
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);	
	}

	
	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.setStrict(false);
		shader.enable();

		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_intensity", intensity);
		shader.setAttribute("a_position", vertexBuffer);

		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawArrays(GL_LINES, 0, vertices.length);		
	}

	
	/**
	 * Get the direction of the light as a vector in world space
	 */
	
	private Matrix4f modelMatrix = new Matrix4f();
	
	public Vector4f getDirection(Vector4f dest) {
		getModelToWorldMatrix(modelMatrix);
		return dest.set(0, 0, 1, 0).mul(modelMatrix);
	}

	public Vector3f getIntensity(Vector3f dest) {
		return dest.set(intensity);
	}

	
	final static float ROTATION_SPEED = TAU / 4;
	private Vector3f angle = new Vector3f(-TAU / 4, 0, 0);

	public void update(float deltaTime, InputManager input) {

		// key controls to orbit camera around the origin

		if (input.isKeyDown(GLFW_KEY_KP_8)) {
			angle.x -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_KP_5)) {
			angle.x += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_KP_4)) {
			angle.y -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(GLFW_KEY_KP_6)) {
			angle.y += ROTATION_SPEED * deltaTime;
		}
		
		Matrix4f modelMatrix = getMatrix();
		modelMatrix.identity();
		modelMatrix.rotateY(angle.y);	// heading
		modelMatrix.rotateX(angle.x);	// pitch
		modelMatrix.rotateZ(angle.z);	// roll
	}
	
}
