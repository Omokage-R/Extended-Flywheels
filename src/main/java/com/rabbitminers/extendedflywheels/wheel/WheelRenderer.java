package com.rabbitminers.extendedflywheels.wheel;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.content.contraptions.components.flywheel.FlywheelBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementContext;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionMatrices;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;

import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class WheelRenderer extends KineticTileEntityRenderer {

    public WheelRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(KineticTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        super.renderSafe(te, partialTicks, ms, buffer, light, overlay);

        if (Backend.canUseInstancing(te.getLevel()))
            return;

        BlockState blockState = te.getBlockState();
        WheelTileEntity wte = (WheelTileEntity) te;

        float speed = wte.visualSpeed.getValue(partialTicks) * 3 / 10f;
        float angle = wte.angle + speed * partialTicks;

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        renderFlywheel(te, ms, light, blockState, angle, vb);
    }

    private void renderFlywheel(KineticTileEntity te, PoseStack ms, int light, BlockState blockState, float angle,
                                VertexConsumer vb) {
        SuperByteBuffer wheel = CachedBufferer.block(blockState);
        kineticRotationTransform(wheel, te, getRotationAxisOf(te), AngleHelper.rad(angle), light);
        wheel.renderInto(ms, vb);
    }

    @Override
    protected BlockState getRenderedBlockState(KineticTileEntity te) {
        return shaft(getRotationAxisOf(te));
    }

}
