package boomshroom.recipeview.formatters;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.ShapedRecipe;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ShapedFormatter implements RecipeFormatter{

    @Override
    public Text formatRecipe(Recipe recipe, Locale locale) {
        ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;

        Text.Builder text = Text.builder("Shaped Recipe\n\n");
        Map<ItemType, Character> ingredients = new HashMap<>();
        for (int y=0;y<shapedRecipe.getHeight();y++){
            for (int x=0;x<shapedRecipe.getWidth();x++){
                if (!shapedRecipe.getIngredient(x,y).isPresent()) {
                    text.append(Text.of("    "));
                }else {
                    ItemType ingredient = shapedRecipe.getIngredient(x, y).get().getItem();
                    char c = ingredient.getName().charAt(ingredient.getName().indexOf(':')+1);
                    if (ingredients.containsValue(c)) {
                        if (!ingredients.containsValue(Character.toUpperCase(c))) {
                            c = Character.toUpperCase(c);
                        } else if (!ingredients.containsValue(Character.toLowerCase(c))) {
                            c = Character.toLowerCase(c);
                        } else {
                            for (; ingredients.containsValue(c); c++) {
                            }
                        }
                    }
                    if(!ingredients.containsKey(ingredient)){
                        ingredients.put(ingredient, c);
                    }else{
                        c = ingredients.get(ingredient);
                    }
                    text.append( Text.of("[" + c + "] "));
                }
            }
            text.append(Text.of("\n"));
        }
        text.append(Text.of("\n"));
        ingredients.forEach((ItemType t, Character c) ->
                text.append(Text.of(c,
                        ": ",
                        FormatHelper.itemName(t),
                        "\n")));

        return text.build();
    }
}
