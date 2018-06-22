package ns.openglObjects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import ns.parallelComputing.CreateQueryRequest;
import ns.utils.GU;

public class Query implements IOpenGLObject {
	private int id;
	private int type;
	private boolean created;
	private boolean isInUse;

	public Query(int type) {
		this.type = type;
		create();
	}

	public int getResult() {
		isInUse = false;
		return GL15.glGetQueryObjecti(id, GL15.GL_QUERY_RESULT);
	}

	public boolean isResultAvailable() {
		return GL15.glGetQueryObjecti(id, GL15.GL_QUERY_RESULT_AVAILABLE) == GL11.GL_TRUE;
	}

	public void beginQuery() {
		GL15.glBeginQuery(type, id);
		isInUse = true;
	}

	public void endQuery() {
		GL15.glEndQuery(type);
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public Query create() {
		if (GU.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
			this.id = GL15.glGenQueries();
			created = true;
		} else
			GU.sendRequestToMainThread(new CreateQueryRequest(this));
		return this;
	}
	
	public boolean isInUse() {
		return isInUse;
	}

	@Override
	public void delete() {
		GL15.glDeleteQueries(id);
		created = false;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public boolean isCreated() {
		return created;
	}
}