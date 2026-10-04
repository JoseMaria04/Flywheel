package dev.engine_room.flywheel.lib.model.baked;

import net.minecraft.core.Direction;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.util.TriState;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class FabricMeshEmitterManager extends MeshEmitterManager<FabricMeshEmitter> {
	private boolean useAo;
	private boolean defaultAo;

	FabricMeshEmitterManager() {
		super(FabricMeshEmitter::new);
	}

	public void prepareForModel(boolean useAo, boolean defaultAo) {
		this.useAo = useAo;
		this.defaultAo = defaultAo;
	}

	public void prepareForGeometry(QuadView quad) {
		// 26.3 replaced the shade flag with a shade direction override; unshaded faces use UP (full brightness)
		boolean shade = quad.shadeDirectionOverride() != Direction.UP;
		TriState aoMode = quad.ambientOcclusion();
		boolean ao = useAo && aoMode.orElse(defaultAo);

		for (FabricMeshEmitter emitter : emitterMap.values()) {
			emitter.prepareForGeometry(shade, ao);
		}
	}
}
