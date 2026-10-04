package dev.engine_room.flywheel.backend.gl;

import java.util.List;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.renderpearl.backend.opengl.GlConst;
import com.mojang.renderpearl.backend.opengl.GlStateManager;
import com.mojang.renderpearl.backend.opengl.GlTexture;

import net.minecraft.client.Minecraft;

/// Helpers for 26.3, where blend state is per draw buffer and framebuffers come from a shared cache.
public final class GlTargets {
	// GlStateManager tracks 8 draw buffers; the old global glEnable(GL_BLEND) affected all of them.
	private static final int DRAW_BUFFERS = 8;

	private GlTargets() {
	}

	public static RenderTarget mainTarget() {
		return Minecraft.getInstance().gameRenderer.mainRenderTarget();
	}

	public static void enableBlend() {
		for (int i = 0; i < DRAW_BUFFERS; i++) {
			GlStateManager._enableBlend(i);
		}
	}

	public static void disableBlend() {
		for (int i = 0; i < DRAW_BUFFERS; i++) {
			GlStateManager._disableBlend(i);
		}
	}

	public static int fbo(RenderTarget target) {
		GlTexture color = (GlTexture) target.getColorTexture();
		GlTexture depth = (GlTexture) target.getDepthTexture();
		var device = GlUtil.getGlDevice();
		return device.frameBufferCache().getFbo(device.directStateAccess(), List.of(color), depth);
	}

	public static void bind(RenderTarget target) {
		GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, fbo(target));
	}
}
