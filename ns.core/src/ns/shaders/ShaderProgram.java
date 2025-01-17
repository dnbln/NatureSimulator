/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ns.shaders;

import ns.exceptions.ShaderValidationException;
import ns.openglObjects.IOpenGLObject;
import org.lwjgl.opengl.GL20;

public abstract class ShaderProgram implements IOpenGLObject {
	private static final ShaderValidator validator = new ShaderValidator();
	final UniformLocator locator = new UniformLocator(this);
	private final int programId;
	private StringBuffer src;
	private boolean created;
	private Shader[] shaders;

	public ShaderProgram(Shader... shaderStages) {
		if (!validator.validate(shaderStages))
			throw new ShaderValidationException("Internal shader validation error:" + validator.getError());
		programId = GL20.glCreateProgram();
		src = new StringBuffer();
		for (Shader sh : shaderStages) {
			sh.create().bindToProgram(this);
			src.append(sh.getSource());
		}
		shaders = shaderStages;
		preLink();
		GL20.glLinkProgram(programId);
		GL20.glValidateProgram(programId);
		postLink();
		created = true;
	}

	private void preLink() {
	}

	void postLink() {
	}

	protected int variableType(String name) {
		String[] src = this.src.toString().split("\n");
		String declarationLine = null;
		for (String s : src) {
			String[] lineParts = s.split(" ");
			if (lineParts[lineParts.length - 1].equals(name + ";")) {
				declarationLine = s;
				break;
			}
		}
		if (declarationLine == null)
			return -1;
		String type = declarationLine.split(" ")[declarationLine.split(" ").length - 2];
		switch (type) {
			case "float":
				return UniformVar.TYPE_FLOAT;
			case "vec2":
				return UniformVar.TYPE_VEC2;
			case "vec3":
				return UniformVar.TYPE_VEC3;
			case "vec4":
				return UniformVar.TYPE_VEC4;
			case "mat4":
				return UniformVar.TYPE_MAT4;
			case "int":
				return UniformVar.TYPE_INT;
			case "bool":
				return UniformVar.TYPE_BOOL;
			case "sampler2D":
				return UniformVar.TYPE_SAMPLER_2D;
			case "Light":
				return UniformVar.TYPE_LIGHT;
			default:
				return -1;
		}
	}

	int getLocation(String name) {
		return GL20.glGetUniformLocation(programId, name);
	}

//	private int createShader(String shader, int shaderType) {
//		String source = "";
//		String shaderSource = ShaderLib.getSource(shader);
//		source = shaderSource;
//		src.append(shaderSource);
//		int shaderId = GL20.glCreateShader(shaderType);
//		GL20.glShaderSource(shaderId, source);
//		GL20.glCompileShader(shaderId);
//		if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
//			System.err.println("Error compiling shader :" + shader + ", log:");
//			System.err.print(GL20.glGetShaderInfoLog(shaderId, 500));
//			System.exit(-1);
//		}
//		return shaderId;
//	}

	public void start() {
		GL20.glUseProgram(programId);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public void cleanUp() {
		stop();
		for (Shader s : shaders) {
			GL20.glDetachShader(programId, s.getID());
			s.delete();
		}
		GL20.glDeleteProgram(programId);
		created = false;
	}

	public ShaderProgram create() {
		return this;
	}

	public void delete() {
		cleanUp();
	}

	public int getID() {
		return programId;
	}

	public boolean isCreated() {
		return created;
	}

	void storeUniforms(UniformVar... vars) {
		for (UniformVar var : vars) {
			var.loadLocation(locator);
		}
	}

	public String getDeclarationParticleForArray(String name) {
		String[] src = this.src.toString().split("\n");
		for (String s : src) {
			String[] lineParts = s.split(" ");
			String decl = lineParts[lineParts.length - 1];
			if (decl.startsWith(name + "[") && decl.endsWith("];")) {
				return decl;
			}
		}
		return null;
	}

	public String getDeclarationLineForArray(String name) {
		String[] src = this.src.toString().split("\n");
		for (String s : src) {
			String[] lineParts = s.split(" ");
			String decl = lineParts[lineParts.length - 1];
			if (decl.startsWith(name + "[") && decl.endsWith("];")) {
				return s;
			}
		}
		return null;
	}
}
