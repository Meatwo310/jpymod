package io.github.meatwo310.jpymod.event;

import io.github.meatwo310.jpymod.JPYMod;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = JPYMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AddPackFindersRegisterer {
    private static void createAndAddPack(AddPackFindersEvent event, String resourcePathString, String packName, boolean force) {
        Path resourcePath = ModList.get().getModFileById(JPYMod.MODID).getFile().findResource(resourcePathString);
        Pack pack = Pack.readMetaAndCreate(
                "builtin/" + resourcePathString,
                Component.literal(packName),
                force,
                (path) -> new PathPackResources(path, resourcePath, false),
                PackType.CLIENT_RESOURCES,
                Pack.Position.TOP,
                PackSource.BUILT_IN
        );
        event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) return;

        createAndAddPack(event, "coin_twister", "LC日本円化テクスチャ", true);
        createAndAddPack(event, "coin_hardsmoothy", "LCユーロテクスチャ", true);
        createAndAddPack(event, "lc_jpy", "LC日本円化翻訳", true);

        createAndAddPack(event, "lc_alt/coin_chains", "LC硬貨代替パック", false);
        createAndAddPack(event, "lc_alt/coin_kagamimoti", "LC硬貨代替パック", false);
    }
}
