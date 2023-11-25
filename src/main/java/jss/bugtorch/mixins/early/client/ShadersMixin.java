package jss.bugtorch.mixins.early.client;


import jss.bugtorch.stubpackage.RPLEShaderConstants;
import lombok.val;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.ARBVertexShader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import shadersmod.client.ShaderLine;
import shadersmod.client.ShaderParser;
import shadersmod.client.Shaders;

@Mixin(value = Shaders.class, remap = false)
public abstract class ShadersMixin {
    @Shadow
    public static void setProgramUniform1i(String name, int value) {}

    @Inject(method = "setProgramUniform1i",
        at = @At("HEAD"),
        require = 1)
    private static void setProgramUniform1iHijack(String name, int value, CallbackInfo ci) {
            if ("texture".equals(name)) {
            setProgramUniform1i(RPLEShaderConstants.TEXTURING_ENABLED_ATTRIB_NAME, 1);
        }
    }
}
