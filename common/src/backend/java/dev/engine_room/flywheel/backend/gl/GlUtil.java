package dev.engine_room.flywheel.backend.gl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.backend.api.GpuDeviceBackend;
import com.mojang.renderpearl.backend.opengl.GlDevice;

import dev.engine_room.flywheel.backend.mixin.GpuDeviceAccessor;

public class GlUtil {
	private static GpuDeviceBackend backend() {
		return ((GpuDeviceAccessor) RenderSystem.getDevice()).flywheel$getBackend();
	}

	/// 26.3 can run on Vulkan; Flywheel's backends only work on the OpenGL device.
	public static boolean isOpenGl() {
		return backend() instanceof GlDevice;
	}

	public static GlDevice getGlDevice() {
		return (GlDevice) backend();
	}
}
