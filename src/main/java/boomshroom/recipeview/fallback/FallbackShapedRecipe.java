package boomshroom.recipeview.fallback;

import com.flowpowered.math.vector.Vector2i;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.ShapedRecipe;

import java.lang.reflect.Field;
import java.util.Optional;

public class FallbackShapedRecipe extends FallbackRecipe implements ShapedRecipe{

    private static Class c;
    private static Field recipeWidth;
    private static Field recipeHeight;
    private static Field recipeItems;

    public FallbackShapedRecipe(Object wrapped) throws ClassNotFoundException{
        super(wrapped);
        if (c==null){
            c = Class.forName("net.minecraft.item.crafting.ShapedRecipes");
            for (Field field: c.getDeclaredFields()) {
                if (field.getName().equals("field_77576_b") || field.getName().equals("recipeWidth")){
                    field.setAccessible(true);
                    recipeWidth = field;
                }else if (field.getName().equals("field_77577_c") || field.getName().equals("recipeHeight")){
                    field.setAccessible(true);
                    recipeHeight = field;
                }else if (field.getName().equals("field_77574_d") || field.getName().equals("recipeItems")){
                    field.setAccessible(true);
                    recipeItems = field;
                }
            }
        }
    }

    @Override
    public int getWidth() {
        try {
            return recipeWidth.getInt(wrapped);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int getHeight() {
        try {
            return recipeHeight.getInt(wrapped);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Optional<ItemStack> getIngredient(int x, int y) {
        try {
            ItemStack[] items = (ItemStack[]) recipeItems.get(wrapped);
            return Optional.ofNullable(items[x + y*getWidth()]);
        }catch (IllegalAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<ItemStack> getIngredient(Vector2i pos) {
        return getIngredient(pos.getX(), pos.getY());
    }

}
