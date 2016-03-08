package boomshroom.recipeview;

import boomshroom.recipeview.formatters.RecipeFormatter;
import boomshroom.recipeview.formatters.ShapedFormatter;
import boomshroom.recipeview.formatters.ShapelessFormatter;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.ShapedRecipe;
import org.spongepowered.api.item.recipe.ShapelessRecipe;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.SpongeExecutorService.SpongeFuture;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Plugin(id = "boomshroom.recipeview", name = "Recipe Viewer", version = "0.1", description = "A simple chat based recipe viewer.")
public class RecipeView {

    private static RecipeView instance;
    @Inject
    private Logger logger;
    @Inject
    private Game game;
    private PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
    private SpongeFuture<RecipeTable> buildTask;
    private Map<Class<? extends Recipe>, RecipeFormatter> formatters = new HashMap<>();
    public RecipeView() {
        if (instance == null) {
            instance = this;
        }
    }
    //private RecipeTable table;

    public static RecipeView getInstance() {
        return instance;
    }

    public static Logger getStaticLogger() {
        return getInstance().getLogger();
    }

    public Logger getLogger() {
        return logger;
    }

    public Game getGame() {
        return game;
    }

    public <T extends Recipe> void registerFormatter(Class<T> cl, RecipeFormatter formatter) {
        formatters.put(cl, formatter);
    }

    public Optional<RecipeFormatter> getFormatter(Recipe r) {
        for (Class cl : formatters.keySet()) {
            if (cl.isInstance(r)) {
                return Optional.of(formatters.get(cl));
            }
        }
        return Optional.empty();
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        registerFormatter(ShapedRecipe.class, new ShapedFormatter());
        registerFormatter(ShapelessRecipe.class, new ShapelessFormatter());
    }

    @Listener
    public void init(GameInitializationEvent event) {
        //paginationService.setPaginationCalculator(CommandSource.class, paginationService.getFixedLinesCalculator(1));

        CommandSpec cmd = CommandSpec.builder()
                .description(Text.of("Prints the recipe of an item to the chat."))
                .arguments(GenericArguments.onlyOne(GenericArguments.catalogedElement(Text.of("item"), CatalogTypes.ITEM_TYPE)))
                .executor((CommandSource src, CommandContext args) -> {
                    try {
                        ItemType type = args.<ItemType>getOne("item").get();
                        Locale locale = src.getLocale();
                        Optional<List<Text>> recipes = buildTask.get().getRecipes(type, locale);
                        if (recipes.isPresent()) {
                            try {
                                if (Sponge.getPlatform().getApi().getVersion().orElse("").startsWith("4.")) {
                                    paginationService.builder().contents(recipes.get()).title(Text.of("Recipes for ", type.getTranslation())).linesPerPage(1).sendTo(src);
                                } else {
                                    src.sendMessages(recipes.get());
                                }
                            }catch(NoSuchMethodError e){
                                // Not on version 4.X
                                src.sendMessages(recipes.get());
                            }
                            return CommandResult.success();
                        } else {
                            src.sendMessage(Text.of("There are no recipes for ", type.getTranslation()));
                            return CommandResult.empty();
                        }

                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                        return CommandResult.empty();
                    }
                })
                .build();
        getGame().getCommandManager().register(this, cmd, "recipes", "recipesfor", "howdoimake");
    }

    @Listener
    public void postInit(GamePostInitializationEvent event) {
        buildTask = game.getScheduler().createAsyncExecutor(this).schedule(new RecipeTable(), 0, TimeUnit.SECONDS);
        /*try {
            table = new RecipeTable().call();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

}
