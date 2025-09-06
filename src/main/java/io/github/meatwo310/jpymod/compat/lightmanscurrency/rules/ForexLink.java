package io.github.meatwo310.jpymod.compat.lightmanscurrency.rules;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.lightman314.lightmanscurrency.api.events.TradeEvent;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.rules.TradeRuleType;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientSubTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientTab;
import io.github.lightman314.lightmanscurrency.common.traders.rules.PriceTweakingTradeRule;
import io.github.lightman314.lightmanscurrency.common.util.IconData;
import io.github.lightman314.lightmanscurrency.common.util.IconUtil;
import io.github.meatwo310.jpymod.JPYMod;
import io.github.meatwo310.jpymod.config.CommonConfig;
import io.github.meatwo310.jpymod.config.ServerConfig;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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
        event.addHelpful(Component.literal("$1=¥%d (Price x%s)%s".formatted(
                ServerConfig.FOREX_EXCHANGE_RATE.get(),
                ServerConfig.FOREX_EXCHANGE_RATE.get() / 100.0,
                CommonConfig.ALPHA_VANTAGE_API_KEY.get().isEmpty() ? "" : " - Alpha Vantage API"
        )));
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
    protected void handleUpdateMessage(LazyPacketData lazyPacketData) {
    }

    @Override
    public TradeRulesClientSubTab createTab(TradeRulesClientTab<?> parent) {
        return new ForexLinkTab(parent);
    }
}
