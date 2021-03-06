package water.basicWater;

import base.objects.renderer.RendererBase;
import base.objects.renderer.rendererDecorators.CullFaceRendererDecorator;
import base.objects.renderer.scene.light.Light;
import base.views.GLView;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL4;
import org.joml.Vector3f;
import org.joml.Vector4f;
import skybox.Skybox;
import terrain.perlinNoiseTerrain.PerlinNoiseTerrain;
import tests.texturePreviewRenderer.TexturePreviewRenderer;
import tests.texturePreviewRenderer.rgbaTexturePreviewRenderer.RGBATexturePreviewRenderer;
import water.basicWater.rendererDecorators.ClippingPlaneRenderDecorator;
import water.basicWater.waterTerrain.basicWaterTerrainScene.BasicWaterTerrainScene;
import water.basicWater.waterTerrain.basicWaterTerrainScene.basicWaterTerrainShaderProgram.BasicWaterTerrainShaderProgram;
import water.basicWater.rendererDecorators.DistortionRenderDecorator;
import water.basicWater.rendererDecorators.LightRenderDecorator;
import water.basicWater.rendererDecorators.NormalMapRenderDecorator;
import water.basicWater.textureRenderer.ReflectionTextureRenderer;
import water.basicWater.textureRenderer.RefractionTextureRenderer;
import water.basicWater.waterTerrain.causticsWaterTerrain.CausticsWaterTerrainScene;
import water.basicWater.waterTerrain.causticsWaterTerrain.causticsWaterTerrainShaderProgram.CausticsWaterTerrainShaderProgram;
import water.basicWater.waterTile.WaterTileModel;
import water.basicWater.waterTile.WaterTileRenderer;
import water.basicWater.waterTile.waterTileShaderProgram.WaterTileShaderProgram;

import java.util.ArrayList;

public class BasicWaterView extends GLView {
	
	private static class Constants {
		public static final String imagesDirectory = "images/";
		
		public static class BasicWaterCamera {
			public static final float distance          = 10;
			public static final float fieldOfView       = (float) Math.toRadians(45);
			public static final float aspectRation      = 1;
			public static final float nearClippingPlane = 0.1f;
			public static final float farClippingPlane  = 100;
			public static final float pitch             = -30;
			public static final float yaw               = 0;
			public static final float roll              = 0;
			public static final float zStep             = 0.5f;
		}
		
		public static class Skybox {
			public static final String name            = "skybox";
			public static final float  size            = 100;
			public static final String imagesDirectory = Constants.imagesDirectory + "skyboxImages/";
			public static final String positiveX       = Skybox.imagesDirectory + "right.png";
			public static final String negativeX       = Skybox.imagesDirectory + "left.png";
			public static final String positiveY       = Skybox.imagesDirectory + "top.png";
			public static final String negativeY       = Skybox.imagesDirectory + "bottom.png";
			public static final String positiveZ       = Skybox.imagesDirectory + "back.png";
			public static final String negativeZ       = Skybox.imagesDirectory + "front.png";
		}
		
		public static class PerlinNoiseTerrain {
			public static final String   name            = "perlinNoiseTerrain";
			public static final Vector3f topLeft         = new Vector3f(-5, 0, -5);
			public static final float    width           = 10;
			public static final float    height          = 10;
			public static final int      rows            = 400;
			public static final int      columns         = 400;
			public static final int      numberOfOctaves = 4;
			public static final float    persistence     = 0.5f;
			public static final float    scaleX          = 2.0f / PerlinNoiseTerrain.width;
			public static final float    scaleY          = 2.0f / PerlinNoiseTerrain.height;
			public static final float    amplitude       = 5;
			public static final float    power           = 1;
		}
		
		public static class BasicWaterGrassTerrainScene {
			public static final String name            = "basicWaterGrassTerrainScene";
			public static final String imagesDirectory = Constants.imagesDirectory + "terrainImages/";
			public static final String textureFileName = BasicWaterGrassTerrainScene.imagesDirectory + "grass.png";
			
			public static final class ClippingPlaneRenderer {
				public static final String   name          = BasicWaterGrassTerrainScene.name + "clippingPlaneRenderer";
				public static final Vector4f clippingPlane = new Vector4f(0, 1, 0, 0);
			}
			
			public static final class CullFaceRendererDecorator {
				public static final String name = BasicWaterGrassTerrainScene.name + "cullFaceRendererDecorator";
				public static final int    face = GL4.GL_BACK;
			}
		}
		
		public static class BasicWaterSandTerrainScene {
			public static final String name            = "basicWaterSandTerrainScene";
			public static final String imagesDirectory = Constants.imagesDirectory + "terrainImages/";
			public static final String textureFileName = BasicWaterSandTerrainScene.imagesDirectory + "sand.png";
			
			public static class ClippingPlaneRenderer {
				public static final String   name          = BasicWaterSandTerrainScene.name + "clippingPlaneRenderer";
				public static final Vector4f clippingPlane = new Vector4f(0, -1, 0, 0);
			}
			
			public static class CullFaceRendererDecorator {
				public static final String name = BasicWaterSandTerrainScene.name + "cullFaceRendererDecorator";
				public static final int    face = GL4.GL_BACK;
			}
		}
		
		public static class ReflectionTextureRenderer {
			public static final String name   = "reflectionTextureRenderer";
			public static final int    width  = 800;
			public static final int    height = 800;
			
			public static final class ClippingPlaneRenderer {
				public static final String   name          = ReflectionTextureRenderer.name + "ClippingPlaneRenderDecorator";
				public static final Vector4f clippingPlane = new Vector4f(0, 1, 0,0.08f);
			}
		}
		
		public static class RefractionTextureRenderer {
			public static final String name   = "refractionTextureRenderer";
			public static final int    width  = 800;
			public static final int    height = 800;
			
			public static final class ClippingPlaneRenderer {
				public static final String   name          = RefractionTextureRenderer.name + "ClippingPlaneRenderDecorator";
				public static final Vector4f clippingPlane = new Vector4f(0, -1, 0,0.08f);
			}
		}
		
		
		public static class WaterTile {
			public static final String   name                     = "waterTile";
			public static final Vector3f topLeft                  = PerlinNoiseTerrain.topLeft;
			public static final float    width                    = PerlinNoiseTerrain.width;
			public static final float    height                   = PerlinNoiseTerrain.height;
		}
		
		public static final class DistortionRenderDecorator {
			public static final String name               = "distortionRenderDecorator";
			public static final String imagesDirectory    = Constants.imagesDirectory + "distortionImages/";
			public static final String dudvTexture        = DistortionRenderDecorator.imagesDirectory + "dudvTexture.png";
			public static final float  distortionSpeed    = 0.05f;
			public static final float  distortionStrength = 0.01f;
		}
		
		public static final class NormalMapRenderDecorator {
			public static final String name                     = "normalMapRenderDecorator";
			public static final String imagesDirectory          = Constants.imagesDirectory + "normalMapImages/";
			public static final String normalMapTexture         = NormalMapRenderDecorator.imagesDirectory + "normalMapTexture.png";
			public static final float  normalEqualizationFactor = 3;
		}
		
		public static final class LightRenderDecorator {
			public static final String name  = "lightRenderDecorator";
			public static final Light  light = new Light(
					new Vector3f(0, 80, 0),
					new Vector4f(1, 1, 1, 1)
			);
		}
		
		public static class WaterTileRenderer {
			public static final String name                     = "waterTileRenderer";
			public static final float  waveStrength             = 0.01f;
			public static final float  waterReflectivity        = 0.5f;
			public static final float  shineDamper              = 40;
			public static final float  lightReflectivity        = 0.6f;
		}
		
		public static class CausticsWaterSandTerrain {
			public static final String name              = "causticsWaterSandTerrain";
			public static final String imagesDirectory   = Constants.imagesDirectory + "terrainImages/";
			public static final String textureFileName   = CausticsWaterSandTerrain.imagesDirectory + "sand.png";
			public static final float  shineDamper       = 1500;
			public static final float  lightReflectivity = 0.4f;
			
			public static class ClippingPlaneRenderer {
				public static final String   name          = BasicWaterSandTerrainScene.name + "clippingPlaneRenderer";
				public static final Vector4f clippingPlane = new Vector4f(0, -1, 0, 0);
			}
			
			public static class CullFaceRendererDecorator {
				public static final String name = BasicWaterSandTerrainScene.name + "cullFaceRendererDecorator";
				public static final int    face = GL4.GL_BACK;
			}
		}
	}
	
	private BasicWaterTerrainShaderProgram basicWaterTerrainShaderProgram;
	private BasicWaterCamera               basicWaterCamera;
	
	private PerlinNoiseTerrain           perlinNoiseTerrain;
	private BasicWaterTerrainScene       basicWaterGrassTerrainScene;
	private ClippingPlaneRenderDecorator basicWaterGrassTerrainClippingPlaneRenderDecorator;
	
	private Skybox skybox;
	
	private ReflectionTextureRenderer    reflectionTextureRenderer;
	private ClippingPlaneRenderDecorator reflectionTextureClippingPlaneRenderDecorator;
	private RefractionTextureRenderer    refractionTextureRenderer;
	private ClippingPlaneRenderDecorator refractionTextureClippingPlaneRenderDecorator;
	
	private DistortionRenderDecorator distortionRenderDecorator;
	
	private NormalMapRenderDecorator normalMapRenderDecorator;
	
	private LightRenderDecorator lightRenderDecorator;
	
	private ClippingPlaneRenderDecorator      causticsWaterSandTerrainClippingPlaneRenderDecorator;
	private CausticsWaterTerrainShaderProgram causticsWaterTerrainShaderProgram;
	private CausticsWaterTerrainScene         causticsWaterTerrainScene;
	
	private WaterTileShaderProgram waterTileShaderProgram;
	private WaterTileRenderer      waterTileRenderer;
	
	
	@Override
	protected ArrayList<RendererBase> getRenderers() {
		ArrayList<RendererBase> rendererBases = super.getRenderers();
		
		this.basicWaterCamera = new BasicWaterCamera(
				Constants.BasicWaterCamera.distance,
				Constants.BasicWaterCamera.pitch,
				Constants.BasicWaterCamera.yaw,
				Constants.BasicWaterCamera.roll,
				Constants.BasicWaterCamera.fieldOfView,
				Constants.BasicWaterCamera.aspectRation,
				Constants.BasicWaterCamera.nearClippingPlane,
				Constants.BasicWaterCamera.farClippingPlane,
				Constants.BasicWaterCamera.zStep
		);
		
		this.skybox = new Skybox(
				Constants.Skybox.name,
				this.basicWaterCamera,
				Constants.Skybox.size,
				this.getClass(),
				Constants.Skybox.positiveX,
				Constants.Skybox.negativeX,
				Constants.Skybox.positiveY,
				Constants.Skybox.negativeY,
				Constants.Skybox.positiveZ,
				Constants.Skybox.negativeZ
		);
		
		this.basicWaterTerrainShaderProgram = new BasicWaterTerrainShaderProgram();
		
		this.perlinNoiseTerrain = new PerlinNoiseTerrain(
				Constants.PerlinNoiseTerrain.name,
				Constants.PerlinNoiseTerrain.topLeft,
				Constants.PerlinNoiseTerrain.width,
				Constants.PerlinNoiseTerrain.height,
				Constants.PerlinNoiseTerrain.rows,
				Constants.PerlinNoiseTerrain.columns,
				this.basicWaterTerrainShaderProgram.getVertexAttributeLocation(),
				Constants.PerlinNoiseTerrain.numberOfOctaves,
				Constants.PerlinNoiseTerrain.persistence,
				Constants.PerlinNoiseTerrain.scaleX,
				Constants.PerlinNoiseTerrain.scaleY,
				Constants.PerlinNoiseTerrain.amplitude,
				Constants.PerlinNoiseTerrain.power,
				this.basicWaterTerrainShaderProgram.getTexelAttributeLocation()
		);
		
		this.basicWaterGrassTerrainScene = new BasicWaterTerrainScene(
				Constants.BasicWaterGrassTerrainScene.name,
				basicWaterCamera,
				this.basicWaterTerrainShaderProgram,
				this.perlinNoiseTerrain,
				this.getClass(),
				Constants.BasicWaterGrassTerrainScene.textureFileName
		);
		
		this.reflectionTextureClippingPlaneRenderDecorator = new ClippingPlaneRenderDecorator(
				Constants.ReflectionTextureRenderer.ClippingPlaneRenderer.name,
				new RendererBase[] {
						this.basicWaterGrassTerrainScene
				},
				Constants.ReflectionTextureRenderer.ClippingPlaneRenderer.clippingPlane,
			this.basicWaterTerrainShaderProgram
		);
		
		this.reflectionTextureRenderer = new ReflectionTextureRenderer(
				Constants.ReflectionTextureRenderer.name,
				new RendererBase[] {
						this.skybox,
						this.reflectionTextureClippingPlaneRenderDecorator
				},
				Constants.ReflectionTextureRenderer.width,
				Constants.ReflectionTextureRenderer.height,
				this.basicWaterCamera,
				Constants.WaterTile.topLeft.y
		);
		
		rendererBases.add(this.reflectionTextureRenderer);
		
		this.causticsWaterTerrainShaderProgram = new CausticsWaterTerrainShaderProgram();
		
		this.causticsWaterTerrainScene = new CausticsWaterTerrainScene(
				Constants.CausticsWaterSandTerrain.name,
				this.basicWaterCamera,
				this.causticsWaterTerrainShaderProgram,
				this.perlinNoiseTerrain,
				this.getClass(),
				Constants.CausticsWaterSandTerrain.textureFileName,
				Constants.WaterTile.topLeft.y,
				Constants.CausticsWaterSandTerrain.shineDamper,
				Constants.CausticsWaterSandTerrain.lightReflectivity
		);
		
		this.refractionTextureClippingPlaneRenderDecorator = new ClippingPlaneRenderDecorator(
				Constants.RefractionTextureRenderer.ClippingPlaneRenderer.name,
				new RendererBase[] {
						this.causticsWaterTerrainScene
				},
				Constants.RefractionTextureRenderer.ClippingPlaneRenderer.clippingPlane,
				this.causticsWaterTerrainShaderProgram
		);
		
		this.refractionTextureRenderer = new RefractionTextureRenderer(
				Constants.RefractionTextureRenderer.name,
				new RendererBase[] {
						this.refractionTextureClippingPlaneRenderDecorator
				},
				Constants.RefractionTextureRenderer.width,
				Constants.RefractionTextureRenderer.height
		);
		
		this.waterTileShaderProgram = new WaterTileShaderProgram();
		
		this.causticsWaterSandTerrainClippingPlaneRenderDecorator = new ClippingPlaneRenderDecorator(
				Constants.CausticsWaterSandTerrain.ClippingPlaneRenderer.name,
				new RendererBase[] {
						new CullFaceRendererDecorator(
								Constants.CausticsWaterSandTerrain.CullFaceRendererDecorator.name,
								new RendererBase[] {
										this.causticsWaterTerrainScene
								},
								Constants.CausticsWaterSandTerrain.CullFaceRendererDecorator.face
						)
				},
				Constants.CausticsWaterSandTerrain.ClippingPlaneRenderer.clippingPlane,
				this.causticsWaterTerrainShaderProgram
		);
		
		
		this.waterTileRenderer = new WaterTileRenderer(
				Constants.WaterTileRenderer.name,
				this.waterTileShaderProgram,
				new WaterTileModel(
						Constants.WaterTile.name,
						Constants.WaterTile.topLeft,
						Constants.WaterTile.width,
						Constants.WaterTile.height,
						this.waterTileShaderProgram.getVertexAttributeLocation(),
						this.waterTileShaderProgram.getTexelAttributeLocation()
				),
				this.basicWaterCamera,
				this.reflectionTextureRenderer.getColorAttachment(),
				this.refractionTextureRenderer.getColorAttachment(),
				Constants.WaterTileRenderer.waveStrength,
				Constants.WaterTileRenderer.waterReflectivity,
				Constants.WaterTileRenderer.shineDamper,
				Constants.WaterTileRenderer.lightReflectivity
		);
		
		this.distortionRenderDecorator = new DistortionRenderDecorator(
				Constants.DistortionRenderDecorator.name,
				new RendererBase[] {
						this.refractionTextureRenderer,
						this.causticsWaterSandTerrainClippingPlaneRenderDecorator,
						this.waterTileRenderer
				},
				this.getClass(),
				Constants.DistortionRenderDecorator.dudvTexture,
				Constants.DistortionRenderDecorator.distortionSpeed,
				Constants.DistortionRenderDecorator.distortionStrength,
				this.waterTileShaderProgram,
				this.causticsWaterTerrainShaderProgram
		);
		
		this.normalMapRenderDecorator = new NormalMapRenderDecorator(
				Constants.NormalMapRenderDecorator.name,
				new RendererBase[] {
						this.distortionRenderDecorator
				},
				this.getClass(),
				Constants.NormalMapRenderDecorator.normalMapTexture,
				Constants.NormalMapRenderDecorator.normalEqualizationFactor,
				this.waterTileShaderProgram,
				this.causticsWaterTerrainShaderProgram
		);
		
		this.lightRenderDecorator = new LightRenderDecorator(
				Constants.LightRenderDecorator.name,
				new RendererBase[] {
						this.normalMapRenderDecorator
				},
				Constants.LightRenderDecorator.light,
				this.waterTileShaderProgram,
				this.causticsWaterTerrainShaderProgram
		);
		
		this.basicWaterGrassTerrainClippingPlaneRenderDecorator = new ClippingPlaneRenderDecorator(
				Constants.BasicWaterGrassTerrainScene.ClippingPlaneRenderer.name,
				new RendererBase[] {
						new CullFaceRendererDecorator(
								Constants.BasicWaterGrassTerrainScene.CullFaceRendererDecorator.name,
								new RendererBase[] {
										this.basicWaterGrassTerrainScene
								},
								Constants.BasicWaterGrassTerrainScene.CullFaceRendererDecorator.face
						)
				},
				Constants.BasicWaterGrassTerrainScene.ClippingPlaneRenderer.clippingPlane,
				this.basicWaterTerrainShaderProgram
		);
		
		rendererBases.add(this.skybox);
		rendererBases.add(this.basicWaterGrassTerrainClippingPlaneRenderDecorator);
		rendererBases.add(this.lightRenderDecorator);
		
		return rendererBases;
	}
	
	@Override
	protected void setListeners(GLWindow window) {
		window.addMouseListener(this.basicWaterCamera);
	}
	
	@Override
	protected void onReshape(int width, int height) {
		this.basicWaterCamera.setAspectRation(width, height);
	}
}
