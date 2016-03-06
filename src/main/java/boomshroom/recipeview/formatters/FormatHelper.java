package boomshroom.recipeview.formatters;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

public final class FormatHelper {

    public static Text itemName(ItemStack item) {
        return item.get(Keys.DISPLAY_NAME).orElse(Text.of(item.getTranslation())).
                toBuilder().onClick(TextActions.runCommand("/recipes " + item.getItem().getName())).build();
    }

    public static Text itemName(ItemType item) {
        return Text.of(item.getTranslation()).toBuilder().onClick(TextActions.runCommand("/recipes " + item.getName())).build();
    }

}
