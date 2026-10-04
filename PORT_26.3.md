# Unofficial Minecraft 26.3 (Fabric) port

This branch (`26.3/dev`) is an **unofficial** port of [Flywheel](https://github.com/Engine-Room/Flywheel) by
Jozufozu, PepperCode1 and the Engine Room contributors (MIT), based on their `26.1.2/dev` branch. It exists so
Create can be brought to 26.3; please report problems here, not upstream.

Only the Fabric modules are built (`:flywheel-fabric`, `:vanillin:vanillin-fabric`); NeoForge is excluded.

Main changes for 26.3:
- Render API moved to `com.mojang.renderpearl` (GL backend, GpuDevice, formats, textures).
- **Reversed depth**: 26.3 renders with near = 1 / far = 0. Depth comparisons, depth (de)linearization, the
  depth pyramid reduction and the GPU occlusion test were adapted.
- Level rendering is split into `LevelExtractor` (extraction) and `LevelRenderer#render` (drawing, frame graph).
  Flywheel now draws inside the "main" frame pass, after the solid render pass, instead of during submit
  (which happens before the frame graph clears the targets).
- Fabulous' item-entity target is gone (replaced by vanilla OIT); Flywheel's OIT composites onto the main target.
- Per-draw-buffer blend state, framebuffer cache, `BlockEntityTypes`/`EntityTypes` holders, shade direction
  override instead of the shade flag, misc API renames.
- Flywheel disables its backends when 26.3 runs on the Vulkan backend.

Verified with a client gametest (instancing, indirect and off backends render chests, bells, shulker boxes,
ender chests and minecarts identically to vanilla).
