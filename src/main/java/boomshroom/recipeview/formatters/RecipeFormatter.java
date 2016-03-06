package boomshroom.recipeview.formatters;

import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.text.Text;

import java.util.Locale;

public interface RecipeFormatter {

    Text formatRecipe(Recipe recipe, Locale locale);

}
