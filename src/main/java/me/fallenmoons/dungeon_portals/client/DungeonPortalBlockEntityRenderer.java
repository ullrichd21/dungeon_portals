package me.fallenmoons.dungeon_portals.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.fallenmoons.dungeon_portals.blockentity.DungeonPortalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;

public class DungeonPortalBlockEntityRenderer implements BlockEntityRenderer<DungeonPortalBlockEntity> {

    private final BlockEntityRendererProvider.Context context;

    public DungeonPortalBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(@NotNull DungeonPortalBlockEntity blockEntity, float deltaTime, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int pPackedLight, int packedOverlay) {

        ItemStack stack = new ItemStack(Items.DIAMOND, 1);

        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }

        BlockPos pos = blockEntity.getBlockPos().above();

//        this.context.getItemRenderer().renderStatic(stack,
//                ItemDisplayContext.FIXED,
//                LightTexture.pack(
//                        level.getBrightness(LightLayer.BLOCK, pos),
//                        level.getBrightness(LightLayer.SKY, pos)),
//                OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, level, 0);

        int packedLight = LightTexture.pack(
                level.getBrightness(LightLayer.BLOCK, pos),
                level.getBrightness(LightLayer.SKY, pos)
        );

        double relativeGameTime = (double) level.getGameTime() + deltaTime;
        double offset = Math.sin(relativeGameTime / 8.0) / 4.0;
        double rotation = Math.sin(relativeGameTime / 20.0) * 40.0;
        double scale = 0.5;

        poseStack.pushPose();
        poseStack.translate(0.5, 1.5 + offset, 0.5);
        poseStack.scale((float) scale, (float) scale, (float) scale);
        poseStack.mulPose(Axis.YP.rotationDegrees((float) rotation));
        this.context.getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                multiBufferSource,
                level, 0);

        // Clear stack for next frame
        poseStack.popPose();
    }
}
