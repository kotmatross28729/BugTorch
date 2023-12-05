package jss.bugtorch.stubpackage;

/*
 * Copyright (c) 2023 FalsePattern, Ven
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import jss.bugtorch.stubpackage.RPLEShaderConstants;
import lombok.experimental.UtilityClass;
import static shadersmod.client.Shaders.setProgramUniform1i;

@UtilityClass
public final class ShaderRenderHelper {
    public static void enableTexturing() {
        setProgramUniform1i(RPLEShaderConstants.TEXTURING_ENABLED_ATTRIB_NAME, 1);
    }

    public static void disableTexturing() {
        setProgramUniform1i(RPLEShaderConstants.TEXTURING_ENABLED_ATTRIB_NAME, 0);
    }
}
