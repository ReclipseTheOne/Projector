package com.reclipse.projector.networking;

import com.reclipse.projector.Projector;
import com.reclipse.projector.content.blockentities.ProjectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ProjectorUpdatePayload(
        BlockPos pos,
        String text,
        Metadata metadata
) implements CustomPacketPayload {
    // Wrapper cuh there are no big enough composite()'s :(
    public record Metadata(int color, int fontSize, int padding, int offset, int rotation, boolean dropShadow, boolean followPlayer) {
        public static StreamCodec<RegistryFriendlyByteBuf, Metadata> STREAM_CODEC = NeoForgeStreamCodecs.composite(
                ByteBufCodecs.INT, Metadata::color,
                ByteBufCodecs.INT, Metadata::fontSize,
                ByteBufCodecs.INT, Metadata::padding,
                ByteBufCodecs.INT, Metadata::offset,
                ByteBufCodecs.INT, Metadata::rotation,
                ByteBufCodecs.BOOL, Metadata::dropShadow,
                ByteBufCodecs.BOOL, Metadata::followPlayer,
                Metadata::new
        );
    }

    public static final CustomPacketPayload.Type<ProjectorUpdatePayload> TYPE =
            new CustomPacketPayload.Type<>(Projector.rl("projector_update"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ProjectorUpdatePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ProjectorUpdatePayload::pos,
            ByteBufCodecs.STRING_UTF8, ProjectorUpdatePayload::text,
            Metadata.STREAM_CODEC, ProjectorUpdatePayload::metadata,
            ProjectorUpdatePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ProjectorUpdatePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.level().getBlockEntity(payload.pos()) instanceof ProjectorBlockEntity be) {
                    if (serverPlayer.blockPosition().distSqr(payload.pos()) < 64) {
                        be.setText(payload.text());
                        be.setColor(payload.metadata().color());
                        be.setFontSize(payload.metadata().fontSize());
                        be.setPadding(payload.metadata().padding());
                        be.setOffset(payload.metadata().offset());
                        be.setRotation(payload.metadata().rotation());
                        be.setDropShadow(payload.metadata().dropShadow());
                        be.setFollowPlayer(payload.metadata().followPlayer());
                    }
                }
            }
        });
    }
}
