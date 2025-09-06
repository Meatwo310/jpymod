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
import java.util.Calendar;
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

        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);

        Calendar nextRun = (Calendar) now.clone();
        nextRun.set(Calendar.MINUTE, 0);
        nextRun.set(Calendar.SECOND, 0);
        nextRun.set(Calendar.MILLISECOND, 0);
        nextRun.add(Calendar.HOUR_OF_DAY, (currentHour % 2 == 0) ? 1 : 2); // Schedule to the next odd hour

        long initialDelay = nextRun.getTimeInMillis() - now.getTimeInMillis();

        LOGGER.info("Scheduling Forex scheduler: fetch in {} min, then every 2 hours", TimeUnit.MILLISECONDS.toMinutes(initialDelay));
        if (CommonConfig.ALPHA_VANTAGE_API_KEY.get() == "") {
            LOGGER.warn("No Alpha Vantage API key provided. See the common config!");
        } else if (ServerConfig.FOREX_REFRESH_ON_STARTUP.get()) {
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
            JsonObject jsonObject = null;
            try {
                if (CommonConfig.ALPHA_VANTAGE_API_KEY.get().isEmpty()) {
                    return;
                }

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(ALPHA_VANTAGE_URL + CommonConfig.ALPHA_VANTAGE_API_KEY.get()))
                        .build();
                HttpResponse<String> response = CLIENT
                        .send(request, HttpResponse.BodyHandlers.ofString());
                jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                String exchangeRate = jsonObject
                        .getAsJsonObject("Realtime Currency Exchange Rate")
                        .get("5. Exchange Rate")
                        .getAsString();

                double rate = Double.parseDouble(exchangeRate);
                LOGGER.info("Forex exchange rate: 1 USD = {} JPY", rate);
                ServerConfig.FOREX_EXCHANGE_RATE.set((int) rate);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        });
    }
}
