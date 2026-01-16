package com.reclipse.projector.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.reclipse.projector.content.blockentities.ProjectorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProjectorBlockEntityRenderer implements BlockEntityRenderer<ProjectorBlockEntity> {

    public ProjectorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public AABB getRenderBoundingBox(ProjectorBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        int offset = blockEntity.getOffset();
        Direction facing = blockEntity.getFacing();

        return new AABB(pos).expandTowards(
                facing.getStepX() * (offset + 1),
                facing.getStepY() * (offset + 1),
                facing.getStepZ() * (offset + 1)
        );
    }

    @Override
    public void render(ProjectorBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        String text = blockEntity.getText();
        if (text == null || text.isEmpty()) {
            return;
        }

        int fontSize = blockEntity.getFontSize();
        int padding = blockEntity.getPadding();
        int offset = blockEntity.getOffset();
        int rotation = blockEntity.getRotation();
        boolean followPlayer = blockEntity.isFollowPlayer();
        Direction facing = blockEntity.getFacing();

        float offsetHoriz = padding * 5f;
        float blockOffset = -0.01f - offset;

        poseStack.pushPose();

        // Always use block facing direction for positioning
        alignRendering(poseStack, facing);
        // Position on block face
        poseStack.translate(0, 1, 1 - blockOffset);

        // Calculate effective Z rotation
        float effectiveRotation = rotation;
        if (followPlayer) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                // Calculate text's world position
                Vec3 textWorldPos = Vec3.atCenterOf(blockEntity.getBlockPos()).add(0, 0.5, 0);

                Vec3 playerPos = player.getEyePosition(partialTick);
                double dx = playerPos.x - textWorldPos.x;
                double dz = playerPos.z - textWorldPos.z;

                // Calculate angle to player and adjust based on block facing
                float angleToPlayer = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG);
                float facingAngle = facing.toYRot();

                // Z rotation to point toward player (adjusted for block facing)
                effectiveRotation = angleToPlayer + facingAngle + 90f;
            }
        }

        // Scale to texture coordinates
        poseStack.scale(1f / 16f, -1f / 16f, 0.00005f);

        // Apply horizontal offset
        poseStack.translate(offsetHoriz, 0, 0);

        // Scale for font size
        float scale = 0.05f * fontSize;
        poseStack.scale(scale, scale, 1);

        // Render text with FULL BRIGHTNESS
        Font font = Minecraft.getInstance().font;
        int color = blockEntity.getColor();
        int colorWithAlpha = 0xFF000000 | color;

        // Split text into lines
        String[] lines = text.split("\n", -1);
        int lineHeight = font.lineHeight;

        // Calculate max width and total height for centering
        float maxWidth = 0;
        for (String line : lines) {
            float lineWidth = font.width(line);
            if (lineWidth > maxWidth) {
                maxWidth = lineWidth;
            }
        }
        float totalHeight = lines.length * lineHeight;

        // Calculate center point for rotation pivot
        float centerX = maxWidth / 2f;
        float centerY = totalHeight / 2f;

        // Render front side
        renderTextLines(poseStack, bufferSource, font, lines, lineHeight, maxWidth, totalHeight,
                centerX, centerY, effectiveRotation, colorWithAlpha, blockEntity.hasDropShadow(), false);

        // Render back side (mirrored)
        renderTextLines(poseStack, bufferSource, font, lines, lineHeight, maxWidth, totalHeight,
                centerX, centerY, effectiveRotation, colorWithAlpha, blockEntity.hasDropShadow(), true);

        poseStack.popPose();
    }

    private void renderTextLines(PoseStack poseStack, MultiBufferSource bufferSource, Font font,
                                  String[] lines, int lineHeight, float maxWidth, float totalHeight,
                                  float centerX, float centerY, float rotation, int color,
                                  boolean dropShadow, boolean backSide) {
        poseStack.pushPose();

        if (backSide) {
            // Flip for back side - mirror on X axis and offset slightly in Z
            poseStack.translate(maxWidth, 0, -1);
            poseStack.scale(-1, 1, 1);
        }

        // Translate to center, apply rotation, translate back
        poseStack.translate(centerX, centerY, 0);
        if (rotation != 0) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(rotation));
        }
        poseStack.translate(-centerX, -centerY, 0);

        // Render each line centered
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            float lineWidth = font.width(line);
            float xOffset = (maxWidth - lineWidth) / 2f;
            float yPos = i * lineHeight;

            font.drawInBatch(
                    line,
                    xOffset, yPos,
                    color,
                    dropShadow,
                    poseStack.last().pose(),
                    bufferSource,
                    Font.DisplayMode.SEE_THROUGH,
                    0,
                    LightTexture.FULL_BRIGHT
            );
        }

        poseStack.popPose();
    }

    private void alignRendering(PoseStack poseStack, Direction side) {
        poseStack.translate(0.5, 0.5, 0.5);

        switch (side) {
            case NORTH:
                break;
            case SOUTH:
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
                break;
            case WEST:
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
                break;
            case EAST:
                poseStack.mulPose(Axis.YP.rotationDegrees(-90));
                break;
            default:
                break;
        }

        poseStack.translate(-0.5, -0.5, -0.5);
    }
}
