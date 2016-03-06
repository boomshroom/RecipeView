package boomshroom.recipeview.fallback;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.ShapelessRecipe;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FallbackShapelessRecipe extends FallbackRecipe implements ShapelessRecipe{

    private static Class c;
    private static Field recipeItems;

    public FallbackShapelessRecipe(Object wrapped) throws ClassNotFoundException{
        super(wrapped);
        if (c==null){
            c = Class.forName("net.minecraft.item.crafting.ShapelessRecipes");
            for (Field field: c.getDeclaredFields()) {
                if (field.getName().equals("field_77579_b") || field.getName().equals("recipeItems")){
                    field.setAccessible(true);
                    recipeItems = field;
                }
            }
        }
    }

    @Override
    public Collection<ItemStack> getIngredients() {
        try {
            return (List<ItemStack>) recipeItems.get(wrapped);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
