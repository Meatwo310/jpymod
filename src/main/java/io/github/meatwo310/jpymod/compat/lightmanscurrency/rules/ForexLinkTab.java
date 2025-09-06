package io.github.meatwo310.jpymod.compat.lightmanscurrency.rules;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRuleSubTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientTab;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.meatwo310.jpymod.config.ServerConfig;
import net.minecraft.network.chat.Component;
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

        int rate = ServerConfig.FOREX_EXCHANGE_RATE.get();
        gui.drawString(
                Component.translatable("gui.jpy.trade_rule.forex_link.info", rate, rate / 100.0),
                10, 9, 0x404040
        );

        if (ServerConfig.FOREX_AUTO_UPDATE.get()) {
            gui.drawString(
                    Component.translatable("gui.jpy.trade_rule.forex_link.alpha_vantage"),
                    10, 20, 0x404040
            );
        }
    }
}
