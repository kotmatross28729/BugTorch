package jss.bugtorch.mixins.early.minecraft.worldgen;

import java.util.Iterator;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import jss.bugtorch.ducks.IOffsetDuck;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;
import net.minecraft.world.gen.structure.StructureStart;

@Mixin(value = StructureStart.class)
public abstract class MixinStructureStart {

	/**
	 * @author Roadhog360
	 * @reason Move Mineshaft air pockets to the intended place.
	 */
	@Inject(method = "markAvailableHeight(Lnet/minecraft/world/World;Ljava/util/Random;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/structure/StructureBoundingBox;offset(III)V", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
	private void offsetCustom(World world, Random rand, int p_75067_3_, CallbackInfo info, int i, int k, int l, Iterator<StructureComponent> iterator, StructureComponent component) {
		if(component instanceof StructureMineshaftPieces.Room) {
			((IOffsetDuck)component).offset(0, l, 0);
		}
	}

}
