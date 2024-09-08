package io.github.meatwo310.jpymod;

import com.mojang.logging.LogUtils;
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
public class ModEvent {
    private static void createAndAddPack(AddPackFindersEvent event, String modId, String resourcePathString, String packName, boolean force) {
        Path resourcePath = ModList.get().getModFileById(modId).getFile().findResource(resourcePathString);
        Pack pack = Pack.readMetaAndCreate(
                "builtin/" + resourcePathString,
                Component.literal(packName),
                force,
                (path) -> new PathPackResources(path, resourcePath, false),
                PackType.CLIENT_RESOURCES,
                force ? Pack.Position.TOP : Pack.Position.BOTTOM,
                PackSource.BUILT_IN
        );
        event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) return;
        createAndAddPack(event, JPYMod.MODID, "coin_twister", "LC翻訳+硬貨置き換えリソパ", true);
        createAndAddPack(event, JPYMod.MODID, "coin_chains", "硬貨代替リソパ", false);
        createAndAddPack(event, JPYMod.MODID, "coin_kagamimoti", "硬貨代替リソパ", false);
    }
}
