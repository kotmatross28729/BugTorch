package jss.bugtorch.mixins.early.minecraft.backport;

import cpw.mods.fml.common.registry.GameRegistry;
import jss.bugtorch.listeners.ExplodingItemsRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import jss.bugtorch.BugTorch;

@Mixin(TileEntityFurnace.class)
public abstract class MixinHcgunpowder extends TileEntity {

    @Shadow
    public abstract boolean isBurning();

    @Unique
    public boolean explode = false;

    public float explosionPower = 3f;
/*
    @Inject(method = "setInventorySlotContents", at = @At(value = "TAIL"))
    public void test(int index, ItemStack stack, CallbackInfo ctx) {
        if (index == 0 && this.isBurning() && stack != null && stack.getItem() == Items.gunpowder) {
            this.explode = true;
        }
    }
*/
    /*
@Inject(method = "setInventorySlotContents", at = @At(value = "TAIL"))
public void test(int index, ItemStack stack, CallbackInfo ctx) {
    if (index == 0 && this.isBurning() && stack != null) {
        for (ExplodingItemsRegistry.ItemWithMeta itemWithMeta : ExplodingItemsRegistry.explodingItems) {
            if (stack.getItem() == itemWithMeta.item && stack.getItemDamage() == itemWithMeta.metadata) {
                this.explode = true;
                //BugTorch.logger.fatal("Explosion flag set for item: " + stack.getItem().getUnlocalizedName(stack));
                break;
            }
        }
    }
}*/
@Inject(method = "setInventorySlotContents", at = @At(value = "TAIL"))
public void test(int index, ItemStack stack, CallbackInfo ctx) {
    // Check if the current index is either the top slot (0) or the bottom slot (1)
    if ((index == 0 || index == 1) && this.isBurning() && stack != null) {
        for (ExplodingItemsRegistry.ItemWithMeta itemWithMeta : ExplodingItemsRegistry.explodingItems) {
            if (stack.getItem() == itemWithMeta.item && stack.getItemDamage() == itemWithMeta.metadata) {
                this.explode = true;
                //BugTorch.logger.fatal("Explosion flag set for item: " + stack.getItem().getUnlocalizedName(stack));
                /*
                if (itemWithMeta.item == GameRegistry.findItem("minechem", "minechemMolecule") && itemWithMeta.metadata == 6) {
                    explosionPower = 7f; // Set explosion power to 7f if it's 'minechem:minechemMolecule:6'
                }
                */
                this.explosionPower = itemWithMeta.explosionPower;
                break;
            }
        }
    }
}



    @Inject(method = "updateEntity", at = @At(value = "JUMP", opcode = Opcodes.IFLE, ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    public void nut(CallbackInfo ctx) {
        if (this.explode) {
            ctx.cancel();
            this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, this.explosionPower, true);
        }
    }
}
