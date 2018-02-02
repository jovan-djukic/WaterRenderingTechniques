package tests.texturePreviewRenderer;

import base.objects.model.ModelBase;
import base.objects.renderer.Renderer;
import base.objects.textures.Texture;
import com.jogamp.opengl.GL4;
import tests.texturePreviewRenderer.texturePreviewShaderProgram.TexturePreviewShaderProgram;

public abstract class TexturePreviewRenderer extends Renderer {
	private static class Constants {
		public static final String texturePreviewRenderer = "texturePreviewRenderer";
		public static final String texturePreviewModel    = Constants.texturePreviewRenderer + "-texturePreviewModel";
	}
	
	private Texture texture;
	private TexturePreviewShaderProgram shaderProgram;
	
	protected TexturePreviewRenderer(TexturePreviewShaderProgram shaderProgram, Texture texture) {
		super(
				Constants.texturePreviewRenderer,
				shaderProgram,
				new ModelBase[] {
					new TexturePreviewModel(Constants.texturePreviewModel)
				}
			);
		
		this.shaderProgram = shaderProgram;
		this.texture = texture;
	}
	
	@Override
	protected void preRender(GL4 gl) {
		super.preRender(gl);
		
		gl.glActiveTexture(GL4.GL_TEXTURE0);
		this.texture.bind(gl);
		gl.glUniform1i(this.shaderProgram.getSampleUniformLocation(), 0);
	}
}