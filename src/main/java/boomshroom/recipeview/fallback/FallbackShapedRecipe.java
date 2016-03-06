package boomshroom.recipeview.fallback;

import com.flowpowered.math.vector.Vector2i;
import net.minecraft.item.crafting.ShapedRecipes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.ShapedRecipe;

import java.util.Optional;

public class FallbackShapedRecipe extends FallbackRecipe<ShapedRecipes> implements ShapedRecipe {

    public FallbackShapedRecipe(ShapedRecipes wrapped) {
        super(wrapped);
    }

    @Override
    public int getWidth() {
        return wrapped.recipeWidth;
    }

    @Override
    public int getHeight() {
        return wrapped.recipeHeight;
    }

    @Override
    public Optional<ItemStack> getIngredient(int x, int y) {
        return Optional.ofNullable((ItemStack) (Object) wrapped.recipeItems[x + y * getWidth()]);
    }

    @Override
    public Optional<ItemStack> getIngredient(Vector2i pos) {
        return getIngredient(pos.getX(), pos.getY());
    }

}
