package boomshroom.recipeview.fallback;

import net.minecraft.item.crafting.ShapelessRecipes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.ShapelessRecipe;

import java.util.Collection;
import java.util.Collections;

public class FallbackShapelessRecipe extends FallbackRecipe<ShapelessRecipes> implements ShapelessRecipe {

    public FallbackShapelessRecipe(ShapelessRecipes wrapped) {
        super(wrapped);
    }

    @Override
    public Collection<ItemStack> getIngredients() {
        return Collections.singleton((ItemStack) (Object) wrapped.getRecipeOutput());
    }
}
