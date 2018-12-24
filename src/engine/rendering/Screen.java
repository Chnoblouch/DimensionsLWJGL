package engine.rendering;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.gfx.Font;
import engine.gfx.Sprite;
import engine.gfx.Sprites;
import engine.shader.GUIShader;
import engine.shader.GameObjectShader;
import engine.shader.ShaderProgram;
import engine.shader.ShadowShader;
import utils.Hitbox;

public class Screen {
	
	private int vao;
	private int vbo;
	
	private final float[] positions = { -1, -1, 1, -1, -1, 1, 1, 1 };
	
	private GameObjectShader objShader;
	private GUIShader guiShader;
	private ShadowShader shadowShader;
	
	private ShaderProgram activeShader;
	
	private Camera camera;
	private Matrix4f viewMatrix;
	
	private int shadowMap;
	
	public void create()
	{
		createVAO();
		
		objShader = new GameObjectShader();
		guiShader = new GUIShader();
		shadowShader = new ShadowShader();
		
		objShader.start();
		objShader.spriteTexture.load(0);
		objShader.shadowMap.load(1);
		objShader.stop();
	}
	
	private void createVAO()
	{		
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(positions.length);
		buffer.put(positions);
		buffer.flip();
		
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL30.glBindVertexArray(0);
	}
	
	public void setCamera(Camera camera)
	{
		this.camera = camera;
	}
	
	public void setShadowMap(int shadowMap)
	{
		this.shadowMap = shadowMap;
	}
	
	public void clear()
	{
		GL11.glClearColor(1, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	public void prepare(ShaderProgram shader)
	{
		shader.start();
		activeShader = shader;
		
		GL30.glBindVertexArray(vao);
		GL20.glEnableVertexAttribArray(0);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
				
		if(shader instanceof GameObjectShader || shader instanceof ShadowShader) viewMatrix = createViewMatrix();
	}
	
	public void finish()
	{
		GL11.glDisable(GL11.GL_BLEND);
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		
		activeShader.stop();
	}
	
	public void render(Sprite sprite, float x, float y, float width, float height, float rot, float alpha)
	{				
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, sprite.getID());
		
//		GL13.glActiveTexture(GL13.GL_TEXTURE1);
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, shadowMap);
					
		objShader.texCoordsLeftTop.load(getTextureCoordsLeftTop(sprite));
		objShader.texCoordsRightBottom.load(getTextureCoordsBottomRight(sprite));
		
		Matrix4f matrix = createTransformationMatrix(new Vector2f(x * 2 + width - Display.getWidth(), 
															      -(y * 2 + height - Display.getHeight())), 
												     rot, 
												     new Vector2f(width, height));
		objShader.transformationMatrix.load(matrix);
						
		Matrix4f screenMatrix = new Matrix4f();
		screenMatrix.setIdentity();
		Matrix4f.scale(new Vector3f(1.0f / 1920.0f, 1.0f / 1080.0f, 1.0f), screenMatrix, screenMatrix);
		objShader.screenMatrix.load(screenMatrix);
		
		objShader.viewMatrix.load(viewMatrix);
				
		objShader.textureAlpha.load(alpha);
		objShader.colorOverlay.load(sprite.getRedOverlay(), sprite.getGreenOverlay(), sprite.getBlueOverlay(), sprite.getOverlayAlpha());
		
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, positions.length);
	}
		
	public void renderGUI(Sprite sprite, float x, float y, float width, float height, float rot, float alpha)
	{		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, sprite.getID());
						
		guiShader.texCoordsLeftTop.load(getTextureCoordsLeftTop(sprite));
		guiShader.texCoordsRightBottom.load(getTextureCoordsBottomRight(sprite));
				
		Matrix4f matrix = createTransformationMatrix(new Vector2f(x * 2 + width - Display.getWidth(), 
			      												  -(y * 2 + height - Display.getHeight())), 
													 rot, 
													 new Vector2f(width, height));
		guiShader.transformationMatrix.load(matrix);
		
		Matrix4f screenMatrix = new Matrix4f();
		screenMatrix.setIdentity();
		Matrix4f.scale(new Vector3f(1.0f / 1920.0f, 1.0f / 1080.0f, 1.0f), screenMatrix, screenMatrix);
		guiShader.screenMatrix.load(screenMatrix);
		
		guiShader.textureAlpha.load(alpha);
		guiShader.colorOverlay.load(0, 0, 0, 0);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, positions.length);
	}
		
	public void renderFont(String text, float x, float y, float size, String col, boolean inCam)
	{
		Font.render(this, text, inCam ? x - camera.getX() : x, inCam ? y - camera.getY() : y, size, col);
	}
	
	public void renderHitbox(Hitbox hb)
	{
		render(Sprites.hitbox, hb.x, hb.y, hb.w, hb.h, 0, 0.5f);
	}
	
	public void renderShadow(Sprite sprite, float x, float y, float width, float height, float rot, float alpha)
	{				
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, sprite.getID());
					
		shadowShader.texCoordsLeftTop.load(getTextureCoordsLeftTop(sprite));
		shadowShader.texCoordsRightBottom.load(getTextureCoordsBottomRight(sprite));	
		
		Matrix4f matrix = createTransformationMatrix(new Vector2f(inScreenWidth(x * 2 + width - Display.getWidth()), 
													 -inScreenHeight(y * 2 + height - Display.getHeight())), 
													 rot, 
													 new Vector2f(inScreenWidth(width), inScreenHeight(height)));
		shadowShader.transformationMatrix.load(matrix);
		shadowShader.viewMatrix.load(viewMatrix);
		
		shadowShader.textureAlpha.load(alpha);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, positions.length);
	}
		
	public void delete()
	{
		GL30.glDeleteVertexArrays(vao);
		GL15.glDeleteBuffers(vbo);
		
		objShader.delete();
		guiShader.delete();
		shadowShader.delete();
	}
	
	public static float inPixelWidth(float size)
	{
		return size * (float) Display.getWidth();
	}
	
	public static float inPixelHeight(float size)
	{
		return size * (float) Display.getHeight();
	}
	
	public static float inScreenWidth(float size)
	{
		return 1.0f / ((float) Display.getWidth() / size);
	}
	
	public static float inScreenHeight(float size)
	{
		return 1.0f / ((float) Display.getHeight() / size);
	}
	
	private Matrix4f createTransformationMatrix(Vector2f pos, float rot, Vector2f scale)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		Matrix4f.translate(pos, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1.0f), matrix, matrix);
		Matrix4f.rotate((float) -Math.toRadians(rot), new Vector3f(0, 0, 1), matrix, matrix);
		
		return matrix;
	}
	
	private Matrix4f createViewMatrix()
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.scale(new Vector3f(camera.getZoom(), camera.getZoom(), 1.0f), matrix, matrix);
		Matrix4f.translate(new Vector2f(-(camera.getX() * 2), (camera.getY() * 2)), matrix, matrix);
		
		return matrix;
	}
	
	public static Vector2f getTextureCoordsLeftTop(Sprite sprite)
	{
		int x = sprite.getX();
		int y = sprite.getY();
		
		int fw = sprite.getFullWidth();
		int fh = sprite.getFullHeight();
		
		float sx = 1.0f / ((float) fw / x);
		float sy = 1.0f / ((float) fh / y);
		
		return new Vector2f(sx, sy);
	}
	
	public static Vector2f getTextureCoordsBottomRight(Sprite sprite)
	{
		int x = sprite.getX();
		int y = sprite.getY();
		int w = sprite.getWidth();
		int h = sprite.getHeight();
		
		int fw = sprite.getFullWidth();
		int fh = sprite.getFullHeight();
		
		float sx = 1.0f / ((float) fw / x);
		float sy = 1.0f / ((float) fh / y);
		float sw = 1.0f / ((float) fw / w);
		float sh = 1.0f / ((float) fh / h);
						
		return new Vector2f(sx + sw, sy + sh);
	}
	
	public boolean isInside(Hitbox hitbox)
	{
		return isInside(hitbox.x, hitbox.y, hitbox.w, hitbox.h);
	}
	
	public boolean isInside(double x, double y, double w, double h)
	{
		return x + w / 2 + w >= camera.getX() - Display.getWidth() / 2 && 
			   y + h / 2 + h >= camera.getY() - Display.getHeight() / 2 &&
			   x - w / 2 <= camera.getX() + Display.getWidth() / camera.getZoom() &&
			   y - h / 2 <= camera.getY() + Display.getHeight() / camera.getZoom();
	}
	
	public GameObjectShader getObjectShader()
	{
		return objShader;
	}
	
	public GUIShader getGUIShader()
	{
		return guiShader;
	}
	
	public ShadowShader getShadowShader()
	{
		return shadowShader;
	}
}
