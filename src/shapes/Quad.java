package shapes;

import base.objects.model.Loader;
import base.objects.model.Model;
import base.objects.model.loaders.VertexPositionLoader;
import com.jogamp.opengl.GL4;
import org.joml.Vector3f;

public class Quad extends Model {
	private static class Constants {
		public final static String quad = "quad";
		public final static String vertexPositionLoader = Constants.quad + "vertexPositionLoader";
		public final static String drawCommandTag = Constants.quad + "drawCommand";
	}
	protected static class QuadVertexPositionLoader extends VertexPositionLoader {
		
		private Vector3f bottomLeft, bottomRight, topLeft, topRight;
		
		protected QuadVertexPositionLoader(String name, Vector3f bottomLeft, Vector3f bottomRight, Vector3f topLeft, Vector3f topRight, int vertexPositionAttributeLocation) {
			super(name, bottomLeft, vertexPositionAttributeLocation);
			
			this.bottomLeft = bottomLeft;
			this.bottomRight = bottomRight;
			this.topLeft = topLeft;
			this.topRight = topRight;
		}
		
		@Override
		protected float[] getVertexData() {
			return new float[] {
					this.bottomLeft.x, this.bottomLeft.y, this.bottomLeft.z,
					this.bottomRight.x, this.bottomRight.y, this.bottomRight.z,
					this.topLeft.x, this.topLeft.y, this.topLeft.z,
					this.topRight.x, this.topRight.y, this.topRight.z
				};
		}
		
	}
	
	protected Quad(String name, Loader... loaders) {
		super(name, loaders);
	}
	
	public Quad(String name, Vector3f bottomLeft, Vector3f bottomRight, Vector3f topLeft, Vector3f topRight, int vertexPositionAttributeLocation) {
		super(
				name,
				new QuadVertexPositionLoader(Constants.vertexPositionLoader, bottomLeft, bottomRight, topLeft, topRight, vertexPositionAttributeLocation)
		);
	}
	
	@Override
	protected void drawCommand(GL4 gl) {
		gl.glDrawArrays(GL4.GL_TRIANGLE_STRIP, 0, 4);
		
		this.checkForErrors(gl, Constants.drawCommandTag);
	}
}