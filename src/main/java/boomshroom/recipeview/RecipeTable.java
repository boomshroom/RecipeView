package boomshroom.recipeview;

import boomshroom.recipeview.fallback.FallbackRecipeRegistry;
import boomshroom.recipeview.formatters.RecipeFormatter;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.RecipeRegistry;
import org.spongepowered.api.text.Text;

import java.util.*;
import java.util.concurrent.Callable;

public class RecipeTable implements Callable<RecipeTable> {

    private RecipeRegistry registry;
    private Map<ItemType, Set<Recipe>> recipes;

    public RecipeTable() {
        registry = getRegistry();
        recipes = new HashMap<>();
        RecipeView.getInstance().getLogger().info(registry.toString());
    }

    public RecipeRegistry getRegistry() {
        if (registry != null) {
            return registry;
        }
        try {
            registry = RecipeView.getInstance().getGame().getRegistry().getRecipeRegistry();
        } catch (UnsupportedOperationException e) {
            RecipeView.getInstance().getLogger().info("Recipe Registry not implemented! Using custom fallback.");
            registry = new FallbackRecipeRegistry();
        }

        return registry;
    }

    @Override
    public RecipeTable call() throws Exception {
        RecipeView.getInstance().getLogger().info("Building recipe table");
        for (Recipe recipe : registry.getRecipes()) {
            for (ItemType item : recipe.getResultTypes()) {
                if (recipes.containsKey(item)) {
                    recipes.get(item).add(recipe);
                } else {
                    Set<Recipe> set = new HashSet<>(1);
                    set.add(recipe);
                    recipes.put(item, set);
                }
            }
        }
        return this;
    }

    public Optional<List<Text>> getRecipes(ItemType item, Locale locale) {
        if (!recipes.containsKey(item)) {
            return Optional.empty();
        }
        Set<Recipe> recipeSet = recipes.get(item);

        List<Text> texts = new ArrayList<>(recipeSet.size());
        for (Recipe recipe : recipeSet) {
            Optional<RecipeFormatter> formatter = RecipeView.getInstance().getFormatter(recipe);
            if (formatter.isPresent()) {
                texts.add(formatter.get().formatRecipe(recipe, locale));
            }
        }
        return Optional.of(texts);
    }

}
