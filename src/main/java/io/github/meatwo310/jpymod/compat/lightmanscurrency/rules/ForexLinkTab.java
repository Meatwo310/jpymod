package io.github.meatwo310.jpymod.compat.lightmanscurrency.rules;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRuleSubTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientTab;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.meatwo310.jpymod.config.CommonConfig;
import io.github.meatwo310.jpymod.config.ServerConfig;
import org.jetbrains.annotations.NotNull;

public class ForexLinkTab extends TradeRuleSubTab<ForexLink> {
    public ForexLinkTab(@NotNull TradeRulesClientTab<?> parent) {
        super(parent, ForexLink.TYPE);
    }

    @Override
    protected void initialize(ScreenArea screenArea, boolean firstOpen) {
    }

    @Override
    public void renderBG(@NotNull EasyGuiGraphics gui) {
        if (this.getRule() == null) {
            return;
        }
        gui.drawString("$1 = ¥%d (Price x%s)".formatted(
                ServerConfig.FOREX_EXCHANGE_RATE.get(),
                ServerConfig.FOREX_EXCHANGE_RATE.get() / 100.0
        ), 10, 9, 0x404040);
        if (CommonConfig.ALPHA_VANTAGE_API_KEY.get().isEmpty()) {
            gui.drawString("Alpha Vantage API enabled", 10, 20, 0x404040);
        }
    }
}
