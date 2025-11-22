package io.github.meatwo310.jpymod.compat.lightmanscurrency.rules;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.lightman314.lightmanscurrency.api.events.TradeEvent;
import io.github.lightman314.lightmanscurrency.api.misc.icons.IconData;
import io.github.lightman314.lightmanscurrency.api.misc.icons.IconUtil;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.rules.TradeRuleType;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientSubTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientTab;
import io.github.lightman314.lightmanscurrency.common.traders.rules.PriceTweakingTradeRule;
import io.github.meatwo310.jpymod.JPYMod;
import io.github.meatwo310.jpymod.config.ServerConfig;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ForexLink extends PriceTweakingTradeRule {
    public static final TradeRuleType<ForexLink> TYPE = new TradeRuleType<>(
            ResourceLocation.fromNamespaceAndPath(JPYMod.MODID, "forex_link"),
            ForexLink::new
    );

    protected ForexLink() {
        super(TYPE);
    }

    @Override
    public void beforeTrade(TradeEvent.PreTradeEvent event) {
        int rate = ServerConfig.FOREX_EXCHANGE_RATE.get();
        String key = ServerConfig.FOREX_AUTO_UPDATE.get()
                ? "traderule.jpy.forex_link.info.alpha_vantage"
                : "traderule.jpy.forex_link.info";
        event.addHelpful(Component.translatable(key, rate, rate / 100.0));
    }

    @Override
    public void tradeCost(TradeEvent.TradeCostEvent event) {
        int diff = ServerConfig.FOREX_EXCHANGE_RATE.get() - 100;
        if (diff > 0) {
            event.hikePrice(diff);
        } else if (diff < 0) {
            event.giveDiscount(-diff);
        }
    }

    @Override
    public IconData getIcon() {
        return IconUtil.ICON_TAXES;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag) {
    }

    @Override
    public JsonObject saveToJson(JsonObject json) {
        return json;
    }

    @Override
    public void loadFromJson(JsonObject json) throws JsonSyntaxException, ResourceLocationException {
    }

    @Override
    public CompoundTag savePersistentData() {
        return null;
    }

    @Override
    public void loadPersistentData(CompoundTag compoundTag) {
    }

    @Override
    protected void handleUpdateMessage(Player player, LazyPacketData lazyPacketData) {
    }

    @Override
    public TradeRulesClientSubTab createTab(TradeRulesClientTab<?> parent) {
        return new ForexLinkTab(parent);
    }
}
