package io.github.meatwo310.jpymod.event;

import io.github.meatwo310.jpymod.JPYMod;
import io.github.meatwo310.jpymod.datagen.LanguageGen;
import io.github.meatwo310.jpymod.datagen.ModelGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JPYMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GatherDataRegisterer {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        boolean includeServer = event.includeServer();
        boolean includeClient = event.includeClient();

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper efh = event.getExistingFileHelper();
//        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        LanguageGen.register(includeClient, generator);
        ModelGen.register(includeClient, generator, output, efh);

//        TagGen.register(includeServer, generator, output, lookupProvider, efh);
//        LootTableGen.register(includeServer, generator, output);
//        RecipeGen.register(includeServer, generator, output);
    }
}
