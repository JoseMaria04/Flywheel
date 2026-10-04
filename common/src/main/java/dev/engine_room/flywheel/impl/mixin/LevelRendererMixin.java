package dev.engine_room.flywheel.impl.mixin;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;

import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.impl.event.RenderContextImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;

/// 26.3 split level rendering: extraction lives in LevelExtractor (see LevelExtractorMixin) and
/// LevelRenderer#render only draws. Entities, block entities and crumbling are submitted in submitFeatures.
@Mixin(value = LevelRenderer.class, priority = 1001) // Higher priority to go after Sodium
abstract class LevelRendererMixin {
	@Shadow
	@Final
	private RenderBuffers renderBuffers;

	@Shadow
	@Final
	private LevelRenderState levelRenderState;

	@Unique
	@Nullable
	private RenderContextImpl flywheel$renderContext;

	@Unique
	@Nullable
	private ClientLevel flywheel$level;

	@Inject(method = "render", at = @At("HEAD"))
	private void flywheel$beginRender(GraphicsResourceAllocator resourceAllocator, boolean renderOutline, CameraRenderState cameraState, GpuBufferSlice terrainFog, Vector4f fogColor, boolean shouldRenderSky, boolean consistentDepthRequired, CallbackInfo ci) {
		flywheel$level = Minecraft.getInstance().level;
		if (flywheel$level == null) {
			return;
		}

		Matrix4f projectionMatrix = RenderContextImpl.PROJECTION_MATRIX_NO_BOB.isBound() ? RenderContextImpl.PROJECTION_MATRIX_NO_BOB.get() : new Matrix4f(cameraState.projectionMatrix);
		flywheel$renderContext = RenderContextImpl.create((LevelRenderer) (Object) this, flywheel$level, renderBuffers, cameraState.viewRotationMatrix, projectionMatrix, cameraState, levelRenderState, levelRenderState.worldPartialTicks);

		VisualizationManager manager = VisualizationManager.get(flywheel$level);
		if (manager != null) {
			manager.renderDispatcher().onStartLevelRender(flywheel$renderContext);
		}
	}

	@Inject(method = "render", at = @At("RETURN"))
	private void flywheel$endRender(CallbackInfo ci) {
		flywheel$renderContext = null;
		flywheel$level = null;
	}

	/// In 26.3 submitFeatures only collects draws; the main color/depth targets are cleared afterwards by the frame
	/// graph. Flywheel draws immediately, so it has to run while the "main" frame pass executes: right after the
	/// solid render pass closes (opaque terrain and features are done, no vanilla render pass is open).
	@Inject(method = "lambda$addMainPass$0", at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/commands/RenderPass;close()V", ordinal = 0, shift = At.Shift.AFTER))
	private void flywheel$renderInMainPass(CallbackInfo ci) {
		if (flywheel$renderContext == null || flywheel$level == null) {
			return;
		}

		VisualizationManager manager = VisualizationManager.get(flywheel$level);
		if (manager != null) {
			manager.renderDispatcher().afterSubmitEntities(flywheel$renderContext);
			manager.renderDispatcher().beforeSubmitCrumbling(flywheel$renderContext, flywheel$level.destructionProgress());
		}
	}
}
