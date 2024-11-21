package li.cil.ocreloaded.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ScreenRenderer implements BlockEntityRenderer<ScreenBlockEntity> {

    private ScreenBorderRenderer borderRenderer = new ScreenBorderRenderer();

    public ScreenRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(
        ScreenBlockEntity blockEntity, float partialTick, PoseStack poseStack,
        MultiBufferSource bufferSource, int combinedLight, int combinedOverlay
    ) {
        borderRenderer.render(blockEntity, partialTick, poseStack, bufferSource, combinedLight, combinedOverlay);
    }
    
}
