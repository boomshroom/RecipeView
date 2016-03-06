package boomshroom.recipeview.fallback;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.RecipeRegistry;
import org.spongepowered.api.item.recipe.ShapelessRecipe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FallbackRecipeRegistry implements RecipeRegistry{

    private Set<Recipe> recipes;

    @Override
    public void register(Recipe recipe) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Recipe recipe) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Recipe> getRecipes(){
        if (recipes == null) {
            recipes = new HashSet<>();
            for (IRecipe irecipe : CraftingManager.getInstance().getRecipeList()) {
                if (irecipe instanceof ShapedRecipes){
                    recipes.add(new FallbackShapedRecipe((ShapedRecipes) irecipe));
                }else if (irecipe instanceof ShapelessRecipe) {
                    recipes.add(new FallbackShapelessRecipe((ShapelessRecipes)irecipe));
                }else{
                    recipes.add(new FallbackRecipe(irecipe));
                }
            }
        }
        return recipes;
    }
}
