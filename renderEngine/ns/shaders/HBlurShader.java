package ns.shaders;

import org.lwjgl.opengl.GL20;

public class HBlurShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "res/shaders/blur/vhshader.glsl";
	private static final String FRAGMENT_SHADER = "res/shaders/blur/fshader.glsl";
	
	public UniformVec2 size = locator.locateUniformVec2("size");

	public HBlurShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
	}
}