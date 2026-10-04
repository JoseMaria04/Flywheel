package dev.engine_room.flywheel.lib.vertex;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.vertex.VertexFormat;

public class FlywheelVertexFormats {
	/// Basically the same as {@link DefaultVertexFormat#BLOCK} but with the normals included
	public static final VertexFormat BLOCK_VERTEX_FORMAT = VertexFormat.builder(0)
			.addAttribute(DefaultVertexFormat.POSITION_SEMANTIC_NAME, GpuFormat.RGB32_FLOAT)
			.addAttribute(DefaultVertexFormat.COLOR_SEMANTIC_NAME, GpuFormat.RGBA8_UNORM)
			.addAttribute(DefaultVertexFormat.UV0_SEMANTIC_NAME, GpuFormat.RG32_FLOAT)
			.addAttribute(DefaultVertexFormat.UV2_SEMANTIC_NAME, GpuFormat.RG16_SINT)
			// RGBA8_SNORM: three normal components plus one byte of padding, same layout as before
			.addAttribute(DefaultVertexFormat.NORMAL_SEMANTIC_NAME, GpuFormat.RGBA8_SNORM)
			.build();
}
