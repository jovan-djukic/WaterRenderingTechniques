package tests.basicMeshTest;
import base.objects.OpenGLObject;
import base.objects.renderer.scene.Scene;
import base.objects.renderer.scene.SceneShaderProgram;
import base.objects.renderer.scene.camera.Camera;
import base.objects.renderer.scene.sceneModel.SceneModel;
import org.joml.Vector3f;
import tests.basic.basicSceneShaderProgram.BasicSceneShaderProgram;
import tests.basic.BasicCamera;
import tests.basic.BasicView;
import com.jogamp.opengl.GL4;
import shapes.Mesh;

public class BasicMeshTestView extends BasicView {
	private static class Constants {
		public static final String   meshName    = "mesh";
		public static final String   sceneName   = "scene";
		public static final Vector3f bottomLeft  = new Vector3f(-1,  0, +1);
		public static final Vector3f bottomRight = new Vector3f(+1,  0, +1);
		public static final Vector3f topLeft     = new Vector3f(-1,  0, -1);
		public static final Vector3f topRight    = new Vector3f(+1,  0, -1);
		public static final int      rows        = 20;
		public static final int      columns     = 20;
	}
	
	private static class BasicMeshScene extends Scene {
		public BasicMeshScene(String name, SceneShaderProgram sceneShaderProgram, SceneModel sceneModels[], Camera camera, OpenGLObject... openGLObjects) {
			super(name, sceneShaderProgram, camera, sceneModels, openGLObjects);
		}
		
		@Override
		protected void preRender(GL4 gl) {
			super.preRender(gl);
			
			gl.glPolygonMode(GL4.GL_FRONT_AND_BACK, GL4.GL_LINE);
		}
	}
	
	@Override
	protected Scene getScene(BasicCamera basicCamera) {
		BasicSceneShaderProgram basicSceneShaderProgram = new BasicSceneShaderProgram();
		Mesh mesh = new Mesh(
			Constants.meshName,
			Constants.bottomLeft,
			Constants.bottomRight,
			Constants.topLeft,
			Constants.topRight,
			Constants.rows,
			Constants.columns,
			basicSceneShaderProgram.getVertexAttributeLocation()
		);
		
		return new BasicMeshScene(
			Constants.sceneName, basicSceneShaderProgram,
			new SceneModel[] {
					mesh
			}, basicCamera
		);
	}
	
}
