package engine.gfx;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

public class FBO {

	public static final int DEPTH_BUFFER_NONE = 0;
	public static final int DEPTH_BUFFER_TEXTURE = 1;
	public static final int DEPTH_BUFFER_RENDER_BUFFER = 2;
	
	private int width = 0;
	private int height = 0;
	
	private int frameBuffer;
	
	private boolean multisample = false;
	
	private int colorTexture;
	private int colorBuffer;
	
	private int depthTexture;
	private int depthBuffer;
	
	public FBO(int width, int height, int depthBufferType)
	{
		this.width = width;
		this.height = height;
		init(depthBufferType);
	}
	
	public FBO(int width, int height)
	{
		this.width = width;
		this.height = height;
		multisample = true;
		init(DEPTH_BUFFER_RENDER_BUFFER);
	}
	
	private void create()
	{
		frameBuffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
	}
	
	private void createColorTextureAttachment()
	{
		colorTexture = GL11.glGenTextures();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, 
						  GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorTexture, 0);
	}
	
	private void createMultisampleColorAttachment()
	{
		colorBuffer = GL30.glGenRenderbuffers();
		
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, colorBuffer);
		GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, 4, GL11.GL_RGBA8, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_RENDERBUFFER, colorBuffer);
	}
	
	private void createDepthTextureAttachment()
	{
		depthTexture = GL11.glGenTextures();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, width, height, 0, 
						  GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthBuffer, 0);
	}
	
	private void createDepthBufferAttachment()
	{
		depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		
		if(!multisample) GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);
		else GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, 4, GL14.GL_DEPTH_COMPONENT24, width, height);
		
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
	}
	
	public void init(int depthBufferType)
	{
		create();
		
		if(multisample) createMultisampleColorAttachment();
		else createColorTextureAttachment();
		
		if(depthBufferType == DEPTH_BUFFER_RENDER_BUFFER) createDepthBufferAttachment();
		else if(depthBufferType == DEPTH_BUFFER_TEXTURE) createDepthTextureAttachment();
		
		unbind();
	}
	
	public void bindToDraw()
	{
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}
	
	public void bindToRead()
	{
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
		GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
	}
	
	public void unbind()
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public void resolve(FBO output)
	{
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, output.frameBuffer);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
		
		GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, output.width, output.height, 
							   GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		
		unbind();
	}
	
	public void resolve()
	{
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
		GL11.glDrawBuffer(GL11.GL_BACK);
		
		GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, Display.getWidth(), Display.getHeight(), 
							   GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
		
		unbind();
	}
	
	public int getColorTexture()
	{
		return colorTexture;
	}
	
	public int getDepthTexture()
	{
		return depthTexture;
	}
	
	public void clear()
	{
		GL11.glDeleteTextures(colorTexture);
		GL30.glDeleteRenderbuffers(colorBuffer);
		
		GL11.glDeleteTextures(depthTexture);
		GL30.glDeleteRenderbuffers(depthBuffer);
		
		GL30.glDeleteFramebuffers(frameBuffer);
	}
}
