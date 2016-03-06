package boomshroom.recipeview.formatters;

import boomshroom.recipeview.RecipeView;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.ShapelessRecipe;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ShapelessFormatter implements RecipeFormatter{
    @Override
    public Text formatRecipe(Recipe recipe, Locale locale) {
        ShapelessRecipe shapelessRecipe = (ShapelessRecipe)recipe;
        Text.Builder text = Text.builder("Shapeless Recipe\n\n");
        Map<ItemType, ItemStack> ingredients = new HashMap<>(shapelessRecipe.getIngredients().size());

        for (ItemStack stack: shapelessRecipe.getIngredients()){
            RecipeView.getStaticLogger().info(stack.toString());
            if (ingredients.containsKey(stack.getItem())){
                ingredients.get(stack.getItem()).setQuantity(ingredients.get(stack.getItem()).getQuantity()+stack.getQuantity());
            }else{
                ingredients.put(stack.getItem(), stack);
            }
        }
        ingredients.forEach((ItemType t, ItemStack s) ->
                text.append(Text.of(
                        s.getQuantity(),
                        " X ",
                        FormatHelper.itemName(t), "\n")).build());
        return text.build();
    }
}
