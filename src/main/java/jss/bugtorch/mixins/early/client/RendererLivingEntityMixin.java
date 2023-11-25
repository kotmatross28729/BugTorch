
package jss.bugtorch.mixins.early.client;

/*
 * Copyright (c) 2023 FalsePattern, Ven
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import jss.bugtorch.Compat;
import jss.bugtorch.stubpackage.ShaderRenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin extends Render {
    @Redirect(method = "passSpecialRender",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V"),
        require = 1)
    private void disableTexture(int cap) {
        GL11.glDisable(cap);
        if (cap == GL11.GL_TEXTURE_2D && Compat.shadersEnabled())
            ShaderRenderHelper.disableTexturing();
    }

    @Redirect(method = "passSpecialRender",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V"),
        require = 1)
    private void enableTexture(int cap) {
        GL11.glEnable(cap);
        if (cap == GL11.GL_TEXTURE_2D && Compat.shadersEnabled())
            ShaderRenderHelper.enableTexturing();
    }
}

