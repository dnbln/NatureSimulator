package ns.ui.loading;

import ns.components.BlueprintCreator;
import ns.customFileFormat.TexFile;
import ns.entities.Entity;
import ns.fontMeshCreator.FontType;
import ns.fontMeshCreator.GUIText;
import ns.interfaces.Action;
import ns.interfaces.UIMenu;
import ns.mainMenu.MainMenu;
import ns.mainMenu.MainMenuButton;
import ns.openglObjects.Texture;
import ns.options.OnOffOption;
import ns.options.Option;
import ns.options.Options;
import ns.ui.GUIButton;
import ns.utils.GU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UILoader {
	private static Action[] actions;

	public static void init(Action[] actions) {
		UILoader.actions = actions;
	}

	public static UIMenu load(InputStream in) {
		Document document = GU.getDocument(in);
		UIMenu menu = null;
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
					Action action = null;
					int ac = Integer.parseInt(nodeMap.getNamedItem("action").getNodeValue());
					action = actions[ac - 1];
					Texture texture = new TexFile(nodeMap.getNamedItem("texture").getNodeValue()).load();
					buttons.add(new MainMenuButton(new Vector2f(x, y), new Vector2f(xScale, yScale), action, texture));
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
			menu = new MainMenu(buttons,
					new Entity(BlueprintCreator.createModelBlueprintFor("menuDNA"), new Vector3f(dnaX, dnaY, dnaZ)),
					dnaLocation);
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
					Texture texture = new TexFile(nodeMap.getNamedItem("texture").getNodeValue()).load();
					backButton = new GUIButton(new Vector2f(x, y), new Vector2f(xScale, yScale), texture);
				} else if (node.getNodeName().equals("OnOffOption")) {
					NamedNodeMap nodeMap = node.getAttributes();
					float x = Float.parseFloat(nodeMap.getNamedItem("x").getNodeValue());
					float y = Float.parseFloat(nodeMap.getNamedItem("y").getNodeValue());
					float xScale = Float.parseFloat(nodeMap.getNamedItem("xScale").getNodeValue());
					float yScale = Float.parseFloat(nodeMap.getNamedItem("yScale").getNodeValue());
					Node textNode = node.getChildNodes().item(0);
					NamedNodeMap textMap = textNode.getAttributes();
					Vector2f p = new Vector2f(x, y);
					x = Float.parseFloat(textMap.getNamedItem("x").getNodeValue());
					y = Float.parseFloat(textMap.getNamedItem("y").getNodeValue());
					String font = textMap.getNamedItem("font").getNodeValue();
					float fontSize = Float.parseFloat(textMap.getNamedItem("fontSize").getNodeValue());
					float maxLen = Float.parseFloat(textMap.getNamedItem("maxLen").getNodeValue());
					FontType realFont = (font.equals("Z003") ? GU.Z003 : GU.caladea);
					GUIText text = new GUIText(textMap.getNamedItem("text").getNodeValue(), fontSize, realFont,
							new Vector2f(x, y), maxLen, true);
					options.add(new OnOffOption(p, new Vector2f(xScale, yScale), text));
				}
			}
			menu = new Options(options, backButton, actions[actions.length - 1]);
		}
		return menu;
	}

	public static UIMenu loadBinaryUIR(InputStream in) {
		UIMenu menu = null;
		int type = 0, version = 0;
		try {
			type = in.read();
			version = in.read();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if (type == 0) {
			List<MainMenuButton> buttons = new ArrayList<>();
			float dnaX = 0;
			float dnaY = 0;
			float dnaZ = 0;
			Vector4f dnaLocation = new Vector4f();
			if (version == 0) {
				int r = 0;
				try {
					while ((r = in.read()) == 0) {
						byte[] bts = new byte[24];
						in.read(bts);
						float x = GU.readFloat(bts[0], bts[1], bts[2], bts[3]);
						float y = GU.readFloat(bts[4], bts[5], bts[6], bts[7]);
						float xScale = GU.readFloat(bts[8], bts[9], bts[10], bts[11]);
						float yScale = GU.readFloat(bts[12], bts[13], bts[14], bts[15]);
						int ac = GU.readInt(bts[16], bts[17], bts[18], bts[19]);
						int strlen = GU.readInt(bts[20], bts[21], bts[22], bts[23]);
						String str = "";
						for (int i = 0; i < strlen; i++)
							str += (char) in.read();
						buttons.add(new MainMenuButton(new Vector2f(x, y), new Vector2f(xScale, yScale),
								actions[ac - 1],
								new TexFile(str).load()));
					}
					if (r == 1) {
						byte[] bts = new byte[28];
						in.read(bts);

						dnaX = GU.readFloat(bts[0], bts[1], bts[2], bts[3]);
						dnaY = GU.readFloat(bts[4], bts[5], bts[6], bts[7]);
						dnaZ = GU.readFloat(bts[8], bts[9], bts[10], bts[11]);
						float x = GU.readFloat(bts[12], bts[13], bts[14], bts[15]);
						float y = GU.readFloat(bts[16], bts[17], bts[18], bts[19]);
						float xScale = GU.readFloat(bts[20], bts[21], bts[22], bts[23]);
						float yScale = GU.readFloat(bts[24], bts[25], bts[26], bts[27]);
						dnaLocation.x = x;
						dnaLocation.y = y;
						dnaLocation.z = xScale;
						dnaLocation.w = yScale;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			menu = new MainMenu(buttons,
					new Entity(BlueprintCreator.createModelBlueprintFor("menuDNA"), new Vector3f(dnaX, dnaY, dnaZ)),
					dnaLocation);
		} else if (type == 1) {
//			menu = new Options(options, backButton, actions[actions.length - 1]);
		}
		return menu;
	}
}