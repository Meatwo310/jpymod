package io.github.meatwo310.jpymod.command;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.logging.LogUtils;
import io.github.meatwo310.jpymod.JPYMod;
import io.github.meatwo310.jpymod.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;

public class JPYCommand {
    private static final Logger LOGGER = LogUtils.getLogger();
    static final Component PLACEHOLDER = Component
            .empty()
            .append(Component
                    .literal("[JPY] ")
                    .withStyle(ChatFormatting.YELLOW)
            );

    public static final String DEATH_HAMMER_OF_JUSTICE = "death.attack.jpy.hammer_of_justice";
    public static final ResourceKey<DamageType> HAMMER_OF_JUSTICE = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(JPYMod.MODID, "hammer_of_justice")
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("jpy")
                .then(Commands.literal("suffix")
                        .requires(stack -> stack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.literal("online")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("suffix", StringArgumentType.string())
                                                .executes(ctx -> executeCommand(ctx, JPYCommand::setSuffixOnline))
                                        )
                                        .executes(ctx -> executeCommand(ctx, JPYCommand::getSuffixOnline))
                                )
                        )
                        .then(Commands.literal("offline")
                                .then(Commands.argument("playerName", StringArgumentType.string())
                                        .then(Commands.argument("suffix", StringArgumentType.string())
                                                .executes(ctx -> executeCommand(ctx, JPYCommand::setSuffixOffline))
                                        )
                                        .executes(ctx -> executeCommand(ctx, JPYCommand::getSuffixOffline))
                                )
                        )
                )
        );
        dispatcher.register(Commands.literal("hammer")
                .requires(stack -> stack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(ctx -> executeCommand(ctx, JPYCommand::hammer))
                )
        );
    }

    /**
     * コマンド実行をtry-catchで囲むラッパーメソッド
     *
     * @param ctx コマンドコンテキスト
     * @param commandFunc 実行する実際のコマンド関数
     * @return コマンド実行結果
     */
    private static int executeCommand(CommandContext<CommandSourceStack> ctx, CommandExecutor commandFunc) throws CommandSyntaxException {
        try {
            return commandFunc.execute(ctx);
        } catch (Exception e) {
            LOGGER.error("コマンド実行中にエラーが発生しました", e);
            throw new SimpleCommandExceptionType(Component.literal(e.getMessage())).create();
        }
    }

    @FunctionalInterface
    private interface CommandExecutor {
        int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException;
    }

    private static int getSuffixOnline(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
        String playerName = player.getName().getString();
        return getSuffix(ctx, player, playerName);
    }

    private static int getSuffixOffline(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String playerName = StringArgumentType.getString(ctx, "playerName");
        return getSuffix(ctx, null, playerName);
    }

    private static int getSuffix(CommandContext<CommandSourceStack> ctx, @Nullable ServerPlayer player, String playerName) {
        JsonObject suffixPlayersObject = ServerConfig.getSuffixPlayers();
        JsonElement suffixNullable = suffixPlayersObject.get(playerName);
        String suffix = suffixNullable == null ? null : suffixNullable.getAsString();

        MutableComponent result;
        if (suffix == null) {
            result = Component.literal("のsuffixは設定されていません");
        } else {
            result = Component.literal("のsuffixは「" + suffix + "」です");
        }
        ctx.getSource().sendSuccess(() -> PLACEHOLDER.copy()
                        .append(player == null ? Component.literal(playerName) : player.getDisplayName())
                        .append(result),
                false
        );
        return Command.SINGLE_SUCCESS;
    }

    private static int setSuffixOnline(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
        String playerName = player.getName().getString();
        String newSuffix = StringArgumentType.getString(ctx, "suffix");
        return setSuffix(ctx, player, playerName, newSuffix);
    }

    private static int setSuffixOffline(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String playerName = StringArgumentType.getString(ctx, "playerName");
        String newSuffix = StringArgumentType.getString(ctx, "suffix");
        return setSuffix(ctx, null, playerName, newSuffix);
    }

    private static int setSuffix(CommandContext<CommandSourceStack> ctx, @Nullable ServerPlayer player, String playerName, String newSuffix) {
        JsonObject suffixPlayersObject = ServerConfig.getSuffixPlayers();
        JsonElement oldSuffixNullable = suffixPlayersObject.get(playerName);
        String oldSuffix = oldSuffixNullable == null ? "" : oldSuffixNullable.getAsString();

        if (newSuffix.isEmpty()) {
            suffixPlayersObject.remove(playerName);
        } else if (oldSuffix.isEmpty()) {
            suffixPlayersObject.add(playerName, new JsonPrimitive(newSuffix));
        } else {
            suffixPlayersObject.add(playerName, new JsonPrimitive(newSuffix));
        }
        ServerConfig.SUFFIX_PLAYERS.set(suffixPlayersObject.toString());

        MutableComponent result;
        if (oldSuffix.isEmpty() && newSuffix.isEmpty()) {
            result = Component.literal("のsuffixは設定されていません");
        } else if (oldSuffix.equals(newSuffix)) {
            result = Component.literal("のsuffixは「" + newSuffix + "」のまま変更されませんでした");
        } else if (newSuffix.isEmpty()) {
            result = Component.literal("のsuffix「" + oldSuffix + "」は削除されました");
        } else if (oldSuffix.isEmpty()) {
            result = Component.literal("のsuffixを「" + newSuffix + "」に設定しました");
        } else {
            result = Component.literal("のsuffix「" + oldSuffix + "」を「" + newSuffix + "」に変更しました");
        }
        ctx.getSource().sendSuccess(() -> PLACEHOLDER.copy()
                        .append(player == null ? Component.literal(playerName) : player.getDisplayName())
                        .append(result),
                true // notify to all OPs
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int hammer(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
        ServerLevel level = player.serverLevel();

        DamageSource source = new DamageSource(level
                .registryAccess()
                .lookupOrThrow(Registries.DAMAGE_TYPE)
                .getOrThrow(HAMMER_OF_JUSTICE)
        );

//        var curiosInventory = CuriosApi.getCuriosInventory(player);
//        if (!curiosInventory.isPresent()) {
//            kill(player, source, level);
//            return Command.SINGLE_SUCCESS;
//        }
//
//        Optional<ICurioStacksHandler> curiosHandler = curiosInventory
//                .resolve()
//                .orElseThrow()
//                .getStacksHandler("heartamulet");
//
//        if (curiosHandler.isEmpty()) {
//            kill(player, source, level);
//            return Command.SINGLE_SUCCESS;
//        }
//
//        curiosHandler.ifPresent(slotInventory -> {
//            IDynamicStackHandler stacks = slotInventory.getStacks();
//            ItemStack curio = stacks.extractItem(0, 1, false);
//            kill(player, source, level);
//            if (!curio.isEmpty()) stacks.insertItem(0, curio, false);
//        });
//
//        return Command.SINGLE_SUCCESS;

        boolean hasProcessedCurio = CuriosApi
                .getCuriosInventory(player)
                .map(inv -> inv
                        .getStacksHandler("heartamulet")
                        .map(slot -> {
                            var stacks = slot.getStacks();
                            var curio = stacks.extractItem(0, 1, false);
                            kill(player, source, level);
                            if (!curio.isEmpty()) {
                                stacks.insertItem(0, curio, false);
                            }
                            return true;
                        }).orElse(false)
                ).orElse(false);

        if (!hasProcessedCurio) {
            kill(player, source, level);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static void kill(ServerPlayer player, DamageSource source, ServerLevel level) {
        player.setHealth(0F);
        player.getCombatTracker().recordDamage(source, 0.0F);
        player.die(source);

        level.playSound(null, player, SoundEvents.ANVIL_LAND, player.getSoundSource(), 1.0F, 1.0F);
    }
}
