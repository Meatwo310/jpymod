package io.github.meatwo310.jpymod.datagen;

import io.github.meatwo310.jpymod.JPYMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class LanguageGen {
    public static void register(boolean run, DataGenerator generator) {
        generator.addProvider(run, (DataProvider.Factory<EnUs>) EnUs::new);
//        generator.addProvider(run, (DataProvider.Factory<JaJp>) JaJp::new);
    }

    private static class EnUs extends LanguageProvider {
        public EnUs(PackOutput output) {
            super(output, JPYMod.MODID, "en_us");
        }

        @Override
        protected void addTranslations() {
            add(JPYMod.YEN_1000.get(), "千円札");
            add(JPYMod.YEN_5000.get(), "五千円札");
            add(JPYMod.YEN_10000.get(), "壱万円札");

            add(JPYMod.USD_1_CENT.get(), "1セント硬貨");
            add(JPYMod.USD_5_CENT.get(), "5セント硬貨");
            add(JPYMod.USD_10_CENT.get(), "10セント硬貨");
            add(JPYMod.USD_25_CENT.get(), "25セント硬貨");
            add(JPYMod.USD_1_DOLLAR.get(), "1ドル紙幣");
            add(JPYMod.USD_5_DOLLAR.get(), "5ドル紙幣");
            add(JPYMod.USD_10_DOLLAR.get(), "10ドル紙幣");
            add(JPYMod.USD_20_DOLLAR.get(), "20ドル紙幣");
            add(JPYMod.USD_50_DOLLAR.get(), "50ドル紙幣");
            add(JPYMod.USD_100_DOLLAR.get(), "100ドル紙幣");

            add(JPYMod.CASINO_1_EURO.get(), "1カジノユーロ");
            add(JPYMod.CASINO_5_EURO.get(), "5カジノユーロ");
            add(JPYMod.CASINO_10_EURO.get(), "10カジノユーロ");
            add(JPYMod.CASINO_25_EURO.get(), "25カジノユーロ");
            add(JPYMod.CASINO_50_EURO.get(), "50カジノユーロ");
            add(JPYMod.CASINO_100_EURO.get(), "100カジノユーロ");
            add(JPYMod.CASINO_500_EURO.get(), "500カジノユーロ");
            add(JPYMod.CASINO_1000_EURO.get(), "1000カジノユーロ");
            add(JPYMod.CASINO_5000_EURO.get(), "5000カジノユーロ");
            add(JPYMod.CASINO_10000_EURO.get(), "10000カジノユーロ");
        }
    }
}
