package comp3170.lectures.week7.extrusion.sceneobjects;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.OpenGLException;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.TextureLibrary;

public class RenderTextureQuad extends SceneObject {

	private Shader shader;

	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;

	private int[] renderTextures;
	private int[] frameBuffers;

	private Vector2f resolution ;
	
	public RenderTextureQuad(Shader shader, int width, int height) {
		this.shader = shader;
		resolution = new Vector2f(width, height);
		
		createQuad();
		renderTextures = new int[2];
		frameBuffers = new int[2];

		renderTextures[0] = TextureLibrary.instance.createRenderTexture(width, height, GL_RGBA);
		renderTextures[1] = TextureLibrary.instance.createRenderTexture(width, height, GL_RGBA);

		try {
			frameBuffers[0] = GLBuffers.createFrameBuffer(renderTextures[0]);
			frameBuffers[1] = GLBuffers.createFrameBuffer(renderTextures[1]);
		} catch (OpenGLException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public int getRenderTexture(int buffer) {
		return renderTextures[buffer];
	}

	public int getFrameBuffer(int buffer) {
		return frameBuffers[buffer];
	}

	private void createQuad() {

		// Create a quad that fills the sceen in NDC space

		// @formatter:off

		//  2---3
		//  |\  |    y
		//  | * |    |
		//  |  \|    +--x
		//  0---1   /
		//         z (out)

		vertices = new Vector4f[] {
			new Vector4f(-1, -1, 0, 1),	// 0
			new Vector4f( 1, -1, 0, 1), // 1
			new Vector4f(-1,  1, 0, 1),	// 2

			new Vector4f( 1,  1, 0, 1), // 3
			new Vector4f(-1,  1, 0, 1),	// 2
			new Vector4f( 1, -1, 0, 1), // 1
		};

		vertexBuffer = GLBuffers.createBuffer(vertices);

		uvs = new Vector2f[] {
			new Vector2f(0, 0),
			new Vector2f(1, 0),
			new Vector2f(0, 1),

			new Vector2f(1, 1),
			new Vector2f(0, 1),
			new Vector2f(1, 0),
		};

		uvBuffer = GLBuffers.createBuffer(uvs);
		// @formatter:on

	}

	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		shader.setStrict(false);
		
		// no MVP matrix, as the quad is draw directly in NDC space

		// vertex attributes
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_texcoord", uvBuffer);

		// textures
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, renderTextures[0]);
		shader.setUniform("u_colourTexture", 0);

		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, renderTextures[1]);
		shader.setUniform("u_geomTexture", 1);

		shader.setUniform("u_resolution", resolution);
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glDrawArrays(GL_TRIANGLES, 0, vertices.length);
	}

}
