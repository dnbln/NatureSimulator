package ns.converting;

import org.lwjgl.util.vector.Vector4f;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class XMLRToUIRConverter {
	public static void main(String[] args) throws IOException {
		byte[] buf = new byte[50];
		int len = System.in.read(buf);
		String location = "";
		for (int i = 0; i < len - 1; i++)
			location += (char) buf[i];
		File target = new File("../gameData/uiResources/xml/" +
				location);
		try {
			write(target);
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
	}

	private static void write(File target) throws ParserConfigurationException, IOException, SAXException {
		File f = new File(target.getPath().replace(".xml", ".uir"));
		if (!f.exists())
			f.createNewFile();
		FileOutputStream fout = new FileOutputStream(f);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(target);
		if (document.getDoctype().getName().equals("MainMenu")) {
			List<MainMenuButton> buttons = new ArrayList<>();
			float dnaX = 0, dnaY = 0, dnaZ = 0;
			NodeList nodes = document.getDocumentElement().getChildNodes();
			Vector4f dnaLocation = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeName().equals("MainMenuButton")) {
					NamedNodeMap nodeMap = node.getAttributes();
					float x = Float.parseFloat(nodeMap.getNamedItem("x").getNodeValue());
					float y = Float.parseFloat(nodeMap.getNamedItem("y").getNodeValue());
					float xScale = Float.parseFloat(nodeMap.getNamedItem("xScale").getNodeValue());
					float yScale = Float.parseFloat(nodeMap.getNamedItem("yScale").getNodeValue());
					int ac = Integer.parseInt(nodeMap.getNamedItem("action").getNodeValue());
					String tex = nodeMap.getNamedItem("texture").getNodeValue();
					buttons.add(new MainMenuButton(x, y, xScale, yScale, ac, tex));
				} else if (node.getNodeName().equals("DNAPos")) {
					NamedNodeMap nodeMap = node.getAttributes();
					float x = Float.parseFloat(nodeMap.getNamedItem("x").getNodeValue());
					float y = Float.parseFloat(nodeMap.getNamedItem("y").getNodeValue());
					float xScale = Float.parseFloat(nodeMap.getNamedItem("xScale").getNodeValue());
					float yScale = Float.parseFloat(nodeMap.getNamedItem("yScale").getNodeValue());
					dnaLocation = new Vector4f(x, y, xScale, yScale);
					dnaX = Float.parseFloat(nodeMap.getNamedItem("dnaX").getNodeValue());
					dnaY = Float.parseFloat(nodeMap.getNamedItem("dnaY").getNodeValue());
					dnaZ = Float.parseFloat(nodeMap.getNamedItem("dnaZ").getNodeValue());
				}
			}
			fout.write(new byte[]{
					0, 0
			});
			for (MainMenuButton button : buttons) {
				fout.write(0);
				fout.write(getBytes(button.x));
				fout.write(getBytes(button.y));
				fout.write(getBytes(button.xScale));
				fout.write(getBytes(button.yScale));
				fout.write(getBytes(button.ac));
				fout.write(getBytes(button.tex.length()));
				fout.write(button.tex.getBytes());
			}
			fout.write(1);
			fout.write(getBytes(dnaX));
			fout.write(getBytes(dnaY));
			fout.write(getBytes(dnaZ));
			fout.write(getBytes(dnaLocation.x));
			fout.write(getBytes(dnaLocation.y));
			fout.write(getBytes(dnaLocation.z));
			fout.write(getBytes(dnaLocation.w));
		} else if (document.getDoctype().getName().equals("Options")) {
			NodeList nodes = document.getDocumentElement().getChildNodes();
			GUIButton backButton = null;
			List<Option> options = new ArrayList<>();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeName().equals("BackButton")) {
					NamedNodeMap nodeMap = node.getAttributes();
					float x = Float.parseFloat(nodeMap.getNamedItem("x").getNodeValue());
					float y = Float.parseFloat(nodeMap.getNamedItem("y").getNodeValue());
					float xScale = Float.parseFloat(nodeMap.getNamedItem("xScale").getNodeValue());
					float yScale = Float.parseFloat(nodeMap.getNamedItem("yScale").getNodeValue());
					String tex = nodeMap.getNamedItem("texture").getNodeValue();
					backButton = new GUIButton(x, y, xScale, yScale, tex);
				} else if (node.getNodeName().equals("OnOffOption")) {
					NamedNodeMap nodeMap = node.getAttributes();
					float x = Float.parseFloat(nodeMap.getNamedItem("x").getNodeValue());
					float y = Float.parseFloat(nodeMap.getNamedItem("y").getNodeValue());
					float xScale = Float.parseFloat(nodeMap.getNamedItem("xScale").getNodeValue());
					float yScale = Float.parseFloat(nodeMap.getNamedItem("yScale").getNodeValue());
					Node textNode = node.getChildNodes().item(0);
					NamedNodeMap textMap = textNode.getAttributes();
					float x2 = Float.parseFloat(textMap.getNamedItem("x").getNodeValue());
					float y2 = Float.parseFloat(textMap.getNamedItem("y").getNodeValue());
					String font = textMap.getNamedItem("font").getNodeValue();
					float fontSize = Float.parseFloat(textMap.getNamedItem("fontSize").getNodeValue());
					float maxLen = Float.parseFloat(textMap.getNamedItem("maxLen").getNodeValue());
					Text text = new Text(textMap.getNamedItem("text").getNodeValue(), fontSize, font,
							x2, y2, maxLen, true);
					options.add(new Option(x, y, xScale, yScale, text));
				}
			}
			fout.write(new byte[]{1, 0});
			fout.write(getBytes(backButton.x));
			fout.write(getBytes(backButton.y));
			fout.write(getBytes(backButton.xScale));
			fout.write(getBytes(backButton.yScale));
			fout.write(getBytes(backButton.tex.length()));
			fout.write(backButton.tex.getBytes());

			for (Option o : options) {
				fout.write(0);
				fout.write(getBytes(o.x));
				fout.write(getBytes(o.y));
				fout.write(getBytes(o.xScale));
				fout.write(getBytes(o.yScale));
				fout.write(getBytes(o.text.x));
				fout.write(getBytes(o.text.y));
				fout.write(getBytes(o.text.fontSize));
				fout.write(getBytes(o.text.font.length()));
				fout.write(o.text.font.getBytes());
				fout.write(getBytes(o.text.text.length()));
				fout.write(o.text.text.getBytes());
				fout.write(o.text.center ? 1 : 0);
			}
		}
		fout.close();
	}

	private static class MainMenuButton {
		private final float x;
		private final float y;
		private final float xScale;
		private final float yScale;
		private final String tex;
		private final int ac;

		public MainMenuButton(float x, float y, float xScale, float yScale, int ac, String tex) {
			this.x = x;
			this.y = y;
			this.xScale = xScale;
			this.yScale = yScale;
			this.ac = ac;
			this.tex = tex;
		}
	}

	private static final ByteBuffer buffer = ByteBuffer.allocateDirect(4);

	public static float readFloat(byte b1, byte b2, byte b3, byte b4) {
		float result = ByteBuffer.wrap(new byte[]{b1, b2, b3, b4}).order(ByteOrder.BIG_ENDIAN).getFloat();
		return result;
	}

	public static int readInt(byte b1, byte b2, byte b3, byte b4) {
		int result = ByteBuffer.wrap(new byte[]{b1, b2, b3, b4}).order(ByteOrder.BIG_ENDIAN).getInt();
		return result;
	}

	public static byte[] getBytes(float f) {
		buffer.clear();
		buffer.putFloat(f);
		buffer.flip();
		byte[] bytes = {buffer.get(), buffer.get(), buffer.get(), buffer.get()};
		return bytes;
	}

	public static byte[] getBytes(int i) {
		buffer.clear();
		buffer.putInt(i);
		buffer.flip();
		byte[] bytes = {buffer.get(), buffer.get(), buffer.get(), buffer.get()};
		return bytes;
	}

	private static class GUIButton {
		private final float x;
		private final float y;
		private final float xScale;
		private final float yScale;
		private final String tex;

		public GUIButton(float x, float y, float xScale, float yScale, String tex) {
			this.x = x;
			this.y = y;
			this.xScale = xScale;
			this.yScale = yScale;
			this.tex = tex;
		}
	}

	private static class Option {
		private final float x;
		private final float y;
		private final float xScale;
		private final float yScale;
		private final Text text;

		public Option(float x, float y, float xScale, float yScale, Text text) {
			this.x = x;
			this.y = y;
			this.xScale = xScale;
			this.yScale = yScale;
			this.text = text;
		}
	}

	private static class Text {
		private final String text;
		private final float fontSize;
		private final String font;
		private final float x;
		private final float y;
		private final float maxLen;
		private final boolean center;

		public Text(String text, float fontSize, String font, float x, float y, float maxLen, boolean center) {
			this.text = text;
			this.fontSize = fontSize;
			this.font = font;
			this.x = x;
			this.y = y;
			this.maxLen = maxLen;
			this.center = center;
		}
	}
}
