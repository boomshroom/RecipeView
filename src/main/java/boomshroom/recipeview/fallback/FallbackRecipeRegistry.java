package boomshroom.recipeview.fallback;

import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.RecipeRegistry;

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
            try {
                Class managerClass = Class.forName("net.minecraft.item.crafting.CraftingManager");
                Object manager;
                try {
                    manager = managerClass.getMethod("getInstance").invoke(null);
                }catch(NoSuchMethodException e){
                    manager = managerClass.getMethod("func_77594_a").invoke(null);
                }
                Class iRecipeClass = Class.forName("net.minecraft.item.crafting.IRecipe");
                Method getRecipeList;
                try {
                    getRecipeList = managerClass.getMethod("getRecipeList");
                }catch(NoSuchMethodException e){
                    getRecipeList = managerClass.getMethod("func_77592_b");
                }
                for (Object irecipe : (List<Object>) getRecipeList.invoke(manager)) {
                    if (Class.forName("net.minecraft.item.crafting.ShapedRecipes").isInstance(irecipe)){
                        recipes.add(new FallbackShapedRecipe(irecipe));
                    }else if (Class.forName("net.minecraft.item.crafting.ShapelessRecipes").isInstance(irecipe)) {
                        recipes.add(new FallbackShapelessRecipe(irecipe));
                    }else{
                        recipes.add(new FallbackRecipe(irecipe));
                    }
                }
            }catch(ClassNotFoundException|NoSuchMethodException|IllegalAccessException|InvocationTargetException e){
                e.printStackTrace();
                return Collections.emptySet();
            }
        }
        return recipes;
    }
}
