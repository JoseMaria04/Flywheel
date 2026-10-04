package dev.engine_room.flywheel.backend.gl.array;

import org.lwjgl.opengl.GL11C;

/// Vertex attribute component types. Replaces VertexFormatElement.Type, which 26.3 removed in favour of GpuFormat.
public enum GlAttribType {
	FLOAT(4, GL11C.GL_FLOAT),
	UBYTE(1, GL11C.GL_UNSIGNED_BYTE),
	BYTE(1, GL11C.GL_BYTE),
	USHORT(2, GL11C.GL_UNSIGNED_SHORT),
	SHORT(2, GL11C.GL_SHORT),
	UINT(4, GL11C.GL_UNSIGNED_INT),
	INT(4, GL11C.GL_INT);

	private final int size;
	private final int glType;

	GlAttribType(int size, int glType) {
		this.size = size;
		this.glType = glType;
	}

	public int size() {
		return size;
	}

	public int glType() {
		return glType;
	}
}
