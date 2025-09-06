package io.github.meatwo310.jpymod.compat.lightmanscurrency;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import io.github.meatwo310.jpymod.JPYMod;
import io.github.meatwo310.jpymod.config.CommonConfig;
import io.github.meatwo310.jpymod.config.ServerConfig;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber(modid = JPYMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForexScheduler {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final HttpClient CLIENT = HttpClient.newHttpClient();
    public static final String ALPHA_VANTAGE_URL =
            "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=USD&to_currency=JPY&apikey=";

    private static ScheduledExecutorService forexScheduler = null;

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        if (!LCCompat.isLCLoaded()) {
            return;
        }

        shutdownScheduler();
        forexScheduler = Executors.newSingleThreadScheduledExecutor();

        // java.time APIを使用して次の実行時間を計算
        ZonedDateTime now = ZonedDateTime.now();

        // 次の正時（例: 14:30 -> 15:00）を計算
        ZonedDateTime nextHour = now.truncatedTo(ChronoUnit.HOURS).plusHours(1);

        // 次の実行時間が奇数時になるように調整
        ZonedDateTime nextRunTime;
        if (nextHour.getHour() % 2 != 0) {
            // 次の時間が奇数時なら、それが実行時間
            nextRunTime = nextHour;
        } else {
            // 次の時間が偶数時なら、さらに1時間後（次の奇数時）が実行時間
            nextRunTime = nextHour.plusHours(1);
        }

        // 現在時刻から次の実行時間までの遅延を計算
        long initialDelay = Duration.between(now, nextRunTime).toMillis();

        LOGGER.info("Scheduling Forex scheduler: fetch in {} min, then every 2 hours", TimeUnit.MILLISECONDS.toMinutes(initialDelay));
        if (!ServerConfig.FOREX_AUTO_UPDATE.get()) {
            LOGGER.info("Note: auto updating will be skipped. See the server config to enable it.");
        } else if (CommonConfig.ALPHA_VANTAGE_API_KEY.get().isEmpty()) {
            LOGGER.error("No Alpha Vantage API key provided. See the *common* config!");
        } else if (ServerConfig.FOREX_UPDATE_ON_STARTUP.get()) {
            LOGGER.info("Startup fetching!");
            run();
        }

        forexScheduler.scheduleAtFixedRate(
                ForexScheduler::run,
                initialDelay,
                TimeUnit.HOURS.toMillis(2),
                TimeUnit.MILLISECONDS
        );
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        shutdownScheduler();
    }

    private static void shutdownScheduler() {
        if (forexScheduler == null || forexScheduler.isShutdown()) {
            return;
        }

        LOGGER.info("Shutting down Forex scheduler");
        forexScheduler.shutdown();
    }

    private static void run() {
        LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER).execute(() -> {
            try {
                if (CommonConfig.ALPHA_VANTAGE_API_KEY.get().isEmpty() || !ServerConfig.FOREX_AUTO_UPDATE.get()) {
                    return;
                }

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(ALPHA_VANTAGE_URL + CommonConfig.ALPHA_VANTAGE_API_KEY.get()))
                        .build();
                HttpResponse<String> response = CLIENT
                        .send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                String exchangeRate = jsonObject
                        .getAsJsonObject("Realtime Currency Exchange Rate")
                        .get("5. Exchange Rate")
                        .getAsString();

                double rate = Double.parseDouble(exchangeRate);
                LOGGER.info("Fetched exchange rate: 1 USD = {} JPY", rate);
                ServerConfig.FOREX_EXCHANGE_RATE.set((int) rate);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        });
    }
}
