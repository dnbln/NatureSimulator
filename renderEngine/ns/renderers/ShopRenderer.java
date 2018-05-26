package ns.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import ns.openglObjects.FBO;
import ns.ui.shop.Shop;
import ns.ui.shop.ShopItem;

public class ShopRenderer {
	private GUIRenderer guiRenderer;
	private FBO fbo;

	public ShopRenderer(GUIRenderer guiRenderer) {
		this.guiRenderer = guiRenderer;
		this.fbo = new FBO(128, 128, (FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER)).create();
	}

	public void render(Shop s) {
		if(!s.open())
			return;
		for (ShopItem item : s.getItems()) {
			fbo.bind();
			GL11.glClearColor(0.5f, 0.5f, 0.5f, 1f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			MasterRenderer.instance.render(item.getEntityBlueprint(), new Vector3f(0, 0, -20f));
			FBO.unbind();
			guiRenderer.bind();
			guiRenderer.batchRenderCall(item.getCenter(), ShopItem.SCALE, fbo.getTex());
			guiRenderer.unbind();
		}
	}
	
	public void cleanUp() {
		fbo.cleanUp();
	}
}