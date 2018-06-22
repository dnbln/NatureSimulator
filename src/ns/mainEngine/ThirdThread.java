package ns.mainEngine;

import ns.camera.Camera;
import ns.camera.ICamera;
import ns.customFileFormat.TexFile;
import ns.derrivedOpenGLObjects.FlareTexture;
import ns.flares.FlareManager;
import ns.openglObjects.Texture;
import ns.parallelComputing.SetRequest;
import ns.renderers.MasterRenderer;
import ns.utils.GU;
import ns.utils.MousePicker;
import ns.world.World;
import ns.world.WorldGenerator;
import ns.worldSave.SaveWorldMaster;
import res.WritingResource;

public class ThirdThread implements Runnable {
	public static boolean READY = false;

	@Override
	public void run() {
		GU.currentThread().waitForDisplayInit();
		ICamera camera = new Camera(GU.creteProjection(70, 0.1f, MasterRenderer.FAR_PLANE));
		GU.sendRequestToMainThread(new SetRequest(camera));
		World world = WorldGenerator.generateWorld();
		GU.sendRequestToMainThread(new SetRequest(world));
		Texture[] flareTextures = new Texture[] {
				new TexFile("textures/lensFlare/tex1.tex").load(),
				new TexFile("textures/lensFlare/tex2.tex").load(),
				new TexFile("textures/lensFlare/tex3.tex").load(),
				new TexFile("textures/lensFlare/tex4.tex").load(),
				new TexFile("textures/lensFlare/tex5.tex").load(),
				new TexFile("textures/lensFlare/tex6.tex").load(),
				new TexFile("textures/lensFlare/tex7.tex").load(),
				new TexFile("textures/lensFlare/tex8.tex").load(),
		};
		GU.sendRequestToMainThread(new SetRequest(new FlareManager(new TexFile("textures/lensFlare/sun.tex").load(),
				new FlareTexture(flareTextures[7], 0.5f, 35f),
				new FlareTexture(flareTextures[5], 0.2f),
				new FlareTexture(flareTextures[1], 0.2f),
				new FlareTexture(flareTextures[2], 0.1f),
				new FlareTexture(flareTextures[0], 0.1f),
				new FlareTexture(flareTextures[5], 0.2f),
				new FlareTexture(flareTextures[6], 0.2f),
				new FlareTexture(flareTextures[3], 0.3f))));
		while (MasterRenderer.instance == null)
			Thread.yield();
		MousePicker.init(camera, world.getTerrain());

		GU.currentThread().finishLoading();
		READY = true;
		while (MainGameLoop.state != GS.CLOSING) {
			Thread.yield();
		}

		SaveWorldMaster.save(world,
				new WritingResource().withLocation(GU.path + "saveData/save0." + GU.WORLD_SAVE_FILE_FORMAT).create());
	}
}