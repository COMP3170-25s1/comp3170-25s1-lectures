package comp3170.lectures.common.cameras;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public interface ICamera {
	public Matrix4f getViewMatrix(Matrix4f dest);
	public Matrix4f getProjectionMatrix(Matrix4f dest);
	public Vector4f getDirection(Vector4f dest);
}
