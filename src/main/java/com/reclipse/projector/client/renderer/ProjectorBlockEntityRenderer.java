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
        float offsetX = Math.abs(blockEntity.getOffsetX());
        float offsetY = Math.abs(blockEntity.getOffsetY());
        float offsetZ = Math.abs(blockEntity.getOffsetZ());
        float maxOffset = Math.max(offsetX, Math.max(offsetY, offsetZ)) + 2;

        return new AABB(pos).inflate(maxOffset);
    }

    @Override
    public void render(ProjectorBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        String text = blockEntity.getText();
        if (text == null || text.isEmpty()) {
            return;
        }

        // Parse \n escape sequences for newlines
        text = text.replace("\\n", "\n");

        int fontSize = blockEntity.getFontSize();
        float offsetX = blockEntity.getOffsetX();
        float offsetY = blockEntity.getOffsetY();
        float offsetZ = blockEntity.getOffsetZ();
        float rotationX = blockEntity.getRotationX();
        float rotationY = blockEntity.getRotationY();
        float rotationZ = blockEntity.getRotationZ();
        boolean followPlayerX = blockEntity.isFollowPlayerX();
        boolean followPlayerY = blockEntity.isFollowPlayerY();

        poseStack.pushPose();

        // Move to center of block, then apply offset
        poseStack.translate(0.5 + offsetX, 0.5 + offsetY, 0.5 + offsetZ);

        // Calculate effective rotations
        float effectiveRotationX = rotationX;
        float effectiveRotationY = rotationY;
        float effectiveRotationZ = rotationZ;

        Player player = Minecraft.getInstance().player;
        if (player != null) {
            Vec3 textWorldPos = Vec3.atCenterOf(blockEntity.getBlockPos())
                    .add(offsetX, offsetY, offsetZ);
            Vec3 playerPos = player.getEyePosition(partialTick);
            double dx = playerPos.x - textWorldPos.x;
            double dy = playerPos.y - textWorldPos.y;
            double dz = playerPos.z - textWorldPos.z;
            double horizontalDist = Math.sqrt(dx * dx + dz * dz);

            if (followPlayerY) {
                // Yaw: look at player horizontally
                effectiveRotationY = (float) -(Mth.atan2(-dx, dz) * Mth.RAD_TO_DEG);
            }

            if (followPlayerX) {
                // Pitch: look at player vertically
                // Negate when Y follow is also enabled to correct the flip
                float pitch = (float) (Mth.atan2(dy, horizontalDist) * Mth.RAD_TO_DEG);
                effectiveRotationX = followPlayerY ? -pitch : pitch;
            }
        }

        // Apply rotations in Y-X-Z order (yaw-pitch-roll)
        poseStack.mulPose(Axis.YP.rotationDegrees(effectiveRotationY));
        poseStack.mulPose(Axis.XP.rotationDegrees(effectiveRotationX));
        poseStack.mulPose(Axis.ZP.rotationDegrees(effectiveRotationZ));

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

        // Scale for font size
        float scale = 0.05f * fontSize / 16f;

        // Render front side (facing +Z after rotation)
        poseStack.pushPose();
        poseStack.scale(scale, -scale, scale);
        renderTextLines(poseStack, bufferSource, font, lines, lineHeight, maxWidth, totalHeight,
                colorWithAlpha, blockEntity.hasDropShadow());
        poseStack.popPose();

        // Render back side (facing -Z after rotation)
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.scale(scale, -scale, scale);
        renderTextLines(poseStack, bufferSource, font, lines, lineHeight, maxWidth, totalHeight,
                colorWithAlpha, blockEntity.hasDropShadow());
        poseStack.popPose();

        poseStack.popPose();
    }

    private void renderTextLines(PoseStack poseStack, MultiBufferSource bufferSource, Font font,
                                  String[] lines, int lineHeight, float maxWidth, float totalHeight,
                                  int color, boolean dropShadow) {
        // Center the text block
        float centerX = maxWidth / 2f;
        float centerY = totalHeight / 2f;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            float lineWidth = font.width(line);
            float xOffset = -centerX + (maxWidth - lineWidth) / 2f;
            float yPos = -centerY + i * lineHeight;

            font.drawInBatch(
                    line,
                    xOffset, yPos,
                    color,
                    dropShadow,
                    poseStack.last().pose(),
                    bufferSource,
                    Font.DisplayMode.NORMAL,
                    0,
                    LightTexture.FULL_BRIGHT
            );
        }
    }
}
