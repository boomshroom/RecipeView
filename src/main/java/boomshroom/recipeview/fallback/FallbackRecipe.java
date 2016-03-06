package boomshroom.recipeview.fallback;

import boomshroom.recipeview.RecipeView;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.item.recipe.Recipe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FallbackRecipe implements Recipe {

    protected final Object wrapped;

    public FallbackRecipe(Object wrapped){
        this.wrapped = wrapped;
    }

    @Override
    public List<ItemType> getResultTypes() {
        try {
            Class iRecipe = Class.forName("net.minecraft.item.crafting.IRecipe");
            Method output;
            try {
                output = iRecipe.getMethod("getRecipeOutput");
            }catch (NoSuchMethodException e){
                output = iRecipe.getMethod("func_77571_b");
            }
            ItemStack itemStack = (ItemStack) output.invoke(wrapped);
            if (itemStack == null){
                RecipeView.getStaticLogger().error("Null output to recipe: "+ String.valueOf(wrapped));
                return Collections.emptyList();
            }
            List<ItemType> list = new ArrayList<>();
            list.add(itemStack.getItem().getType());
            return list;
        } catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException|ClassNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
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
