package obj;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class Material {
	private static BytesFunc btsFunc;
	private final byte index;
	private Vector3f color;
	private Vector3f indicators;
	private String name;
	private Vector4f data;

	protected Material(byte index) {
		this.index = index;
	}

	public static void init(BytesFunc func) {
		btsFunc = func;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getIndicators() {
		return indicators;
	}

	public void setIndicators(Vector3f indicators) {
		this.indicators = indicators;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector4f getData() {
		return data;
	}

	public void setData(Vector4f data) {
		this.data = data;
	}

	public byte getIndex() {
		return index;
	}

	public List<Byte> getBytes() {
		byte[][] data = new byte[][]{bytesFunc(this.color.x), bytesFunc(this.color.y),
				bytesFunc(this.color.z)};
		List<Byte> bytes = new ArrayList<>();
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 4; j++)
				bytes.add(data[i][j]);
		data = new byte[][]{bytesFunc(this.indicators.x), bytesFunc(this.indicators.y),
				bytesFunc(this.indicators.z)};
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 4; j++)
				bytes.add(data[i][j]);
		data = new byte[][]{bytesFunc(this.data.x)};
		for (int i = 0; i < 1; i++)
			for (int j = 0; j < 4; j++)
				bytes.add(data[i][j]);
		return bytes;
	}

	private byte[] bytesFunc(float f) {
		return btsFunc.getBytes(f);
	}

	public interface BytesFunc {
		byte[] getBytes(float f);
	}
}