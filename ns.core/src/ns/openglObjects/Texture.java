package ns.openglObjects;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Texture implements IOpenGLObject {
	private static List<Texture> textures = new ArrayList<>();

	private int id;
	private int width;
	private int height;

	private boolean created = false;

	public Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	public Texture create() {
		return this;
	}

	public void bindToTextureUnit(int texUnit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + texUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}

	@Override
	public void delete() {
		GL11.glDeleteTextures(id);
		created = false;
	}

	@Override
	public int getID() {
		return id;
	}

	public static void cleanUp() {
		for (Texture tex : textures)
			tex.delete();
	}

	public IntBuffer getAsIntBuffer() {
		IntBuffer pixels = BufferUtils.createIntBuffer(width * height);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return pixels;
	}

	public IntBuffer getAsIntBuffer(int sub, IntBuffer pixels) {
		IntBuffer true_pixels = BufferUtils.createIntBuffer((width - sub) * (height - sub));
		for (int y = sub / 2; y < height - sub / 2; y++) {
			for (int x = sub / 2; x < width - sub / 2; x++) {
				true_pixels.put(pixels.get(y * width + x));
			}
		}
		true_pixels.flip();
		return true_pixels;
	}

	@Override
	public boolean isCreated() {
		return created;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void loadWith(ByteBuffer pixels) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, pixels);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		created = true;
		textures.add(this);
	}
}