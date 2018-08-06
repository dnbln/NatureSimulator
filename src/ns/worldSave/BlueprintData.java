package ns.worldSave;

import java.io.IOException;

import ns.components.Blueprint;
import ns.components.BlueprintInputStream;

public class BlueprintData extends Data {
	private static final long serialVersionUID = 8067084958250932904L;
	private Blueprint blueprint;

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		for (int i = 0; i < 4; i++)
			out.writeByte(blueprint.flags(i));
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.blueprint = new BlueprintInputStream(in).read();
	}

	@Override
	public Blueprint asInstance() {
		return blueprint;
	}

	public void setBlueprint(Blueprint blueprint) {
		this.blueprint = blueprint;
	}
}