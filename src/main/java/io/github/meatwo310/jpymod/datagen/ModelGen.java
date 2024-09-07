package io.github.meatwo310.jpymod.datagen;

import com.mojang.logging.LogUtils;
import io.github.meatwo310.jpymod.JPYMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;

public class ModelGen {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void register(boolean run, DataGenerator generator, PackOutput packOutput, ExistingFileHelper efh) {
        generator.addProvider(run, new ItemModel(packOutput, JPYMod.MODID, efh));
//        generator.addProvider(run, new BlockModel(packOutput, CompressedCopper.MODID, efh));
    }

    private static class ItemModel extends ItemModelProvider {
        public ItemModel(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
            super(output, modid, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            JPYMod.ITEMS.getEntries().forEach(item -> {
                try {
                    this.getBuilder(item.getId().getPath())
                            .parent(this.getExistingFile(this.mcLoc("item/generated")))
                            .texture("layer0", "item/" + item.getId().getPath());
                } catch (Exception e) {
                    LOGGER.error("Failed to generate item model for: {}", item.getId().getPath());
                    LOGGER.error(e.getMessage());
                }
            });
        }
    }
}
