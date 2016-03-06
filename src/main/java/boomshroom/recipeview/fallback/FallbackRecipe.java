package boomshroom.recipeview.fallback;

import boomshroom.recipeview.RecipeView;
import net.minecraft.item.crafting.IRecipe;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.item.recipe.Recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FallbackRecipe<T extends IRecipe> implements Recipe {

    protected final T wrapped;

    public FallbackRecipe(T wrapped){
        this.wrapped = wrapped;
    }

    @Override
    public List<ItemType> getResultTypes() {
        ItemStack itemStack = (org.spongepowered.api.item.inventory.ItemStack) (Object) wrapped.getRecipeOutput();
        if (itemStack == null){
            RecipeView.getStaticLogger().error("Null output to recipe: "+ String.valueOf(wrapped));
            return Collections.emptyList();
        }
        List<ItemType> list = new ArrayList<>();
        list.add(itemStack.getItem().getType());
        return list;
    }

    @Override
    public boolean isValid(GridInventory grid) {
        //wrapped.matches();
        return false;
    }

    @Override
    public Optional<List<ItemStack>> getResults(GridInventory grid) {
        return null;
    }
}
