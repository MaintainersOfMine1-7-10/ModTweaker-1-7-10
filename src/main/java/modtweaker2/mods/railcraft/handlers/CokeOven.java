package modtweaker2.mods.railcraft.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.mc1710.item.MCItemStack;
import mods.railcraft.api.crafting.ICokeOvenRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import mods.railcraft.common.util.crafting.CokeOvenCraftingManager.CokeOvenRecipe;
import modtweaker2.mods.railcraft.RailcraftHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.railcraft.CokeOven")
public class CokeOven {

	/**
	 * Adds a recipe for the Coke Oven
	 * 
	 * @param itemOutput ItemStack result
	 * @param fluidOutput FluidStack result
	 * @param ingredient item input
	 * @param timePerItem time per item to process
	 */
	@ZenMethod
	public static void addRecipe(IItemStack itemOutput, ILiquidStack fluidOutput, IItemStack ingredient, int timePerItem) {
		boolean matchEmptyNBT;
		if (toStack(ingredient).stackTagCompound == null)
			matchEmptyNBT = false;
		else
			matchEmptyNBT = true;
			
		if (ingredient.getDamage() == OreDictionary.WILDCARD_VALUE)
			MineTweakerAPI.apply(new Add( new CokeOvenRecipe(toStack(ingredient), false, matchEmptyNBT, toStack(itemOutput), toFluid(fluidOutput), timePerItem) ));
		else
			MineTweakerAPI.apply(new Add( new CokeOvenRecipe(toStack(ingredient), true, matchEmptyNBT, toStack(itemOutput), toFluid(fluidOutput), timePerItem) ));
	}
	/**
	 * Adds a recipe for the Coke Oven (Use this, if you want to have an Input strictly without NBT Data)
	 * 
	 * @param itemOutput ItemStack result
	 * @param fluidOutput FluidStack result
	 * @param ingredient item input
	 * @param matchEmptyNBT should the input only match an item with no NBT Data
	 * @param timePerItem time per item to process
	 */
	@ZenMethod
	public static void addRecipe(IItemStack itemOutput, ILiquidStack fluidOutput, IItemStack ingredient, boolean matchEmptyNBT, int timePerItem) {
		if (ingredient.getDamage() == OreDictionary.WILDCARD_VALUE)
			MineTweakerAPI.apply(new Add( new CokeOvenRecipe(toStack(ingredient), false, matchEmptyNBT, toStack(itemOutput), toFluid(fluidOutput), timePerItem) ));
		else
			MineTweakerAPI.apply(new Add( new CokeOvenRecipe(toStack(ingredient), true, matchEmptyNBT, toStack(itemOutput), toFluid(fluidOutput), timePerItem) ));
	}

	/**
	 * Adds a recipe for the Coke Oven
	 * 
	 * @param ingredient item input
	 * @param matchDamage should the recipe compare NBT Data
	 * @param matchNBT should the recipe compare NBT Data
	 * @param itemOutput ItemStack result
	 * @param fluidOutput FluidStack result
	 * @param cookTime time per item to process
	 */
	@Deprecated
	@ZenMethod
	public static void addRecipe(IItemStack input, boolean matchDamage, boolean matchNBT, IItemStack output, ILiquidStack fluidOutput, int cookTime) {
		MineTweakerAPI.apply(new Add( new CokeOvenRecipe(toStack(input), matchDamage, matchNBT, toStack(output), toFluid(fluidOutput), cookTime) ));
	}

	private static class Add extends BaseListAddition {

		public Add(CokeOvenRecipe recipe) {
			super("Railcraft CokeOven", RailcraftCraftingManager.cokeOven.getRecipes(), recipe);
		}
		@Override
		public String getRecipeInfo() {
			return " " + new MCItemStack(((ICokeOvenRecipe) recipe).getInput()) + " (Input)";
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(toStack(output)));
	}

	private static class Remove extends BaseListRemoval {
		public Remove(ItemStack stack) {
			super("Coke Oven", RailcraftHelper.oven, stack);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void apply() {
			for (ICokeOvenRecipe r : RailcraftHelper.oven) {
				if (r.getOutput() != null && r.getOutput().isItemEqual(stack) && ItemStack.areItemStackTagsEqual(r.getOutput(), stack)) {
					recipes.add(r);
				}
			}
			super.apply();
		}

		@Override
		public String getRecipeInfo() {
			return " " + new MCItemStack(stack) + " (Input)";
		}
	}
}
