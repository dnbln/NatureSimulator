package ns.entities;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Light {
	public final Vector3f dir;
	public final Vector3f color;
	public final Vector2f bias;

	public Light(Vector3f dir, Vector3f color, Vector2f bias) {
		this.dir = dir;
		this.color = color;
		this.bias = bias;
	}

	public Vector3f getDir() {
		return dir;
	}

	public Vector3f getColor() {
		return color;
	}

	public Vector2f getBias() {
		return bias;
	}
}