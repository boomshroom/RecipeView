package boomshroom.recipeview.fallback;

import net.minecraft.item.crafting.ShapelessRecipes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.ShapelessRecipe;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FallbackShapelessRecipe extends FallbackRecipe<ShapelessRecipes> implements ShapelessRecipe{

    private static Class c;
    private static Field recipeItems;

    public FallbackShapelessRecipe(ShapelessRecipes wrapped) {
        super(wrapped);
        if (c==null){
            c = ShapelessRecipes.class;
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
