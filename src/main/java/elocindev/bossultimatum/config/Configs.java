package elocindev.bossultimatum.config;

import elocindev.bossultimatum.config.entries.UltimatumConfig;
import elocindev.necronomicon.api.config.v1.NecConfigAPI;

public class Configs {
    public static UltimatumConfig MAIN;

    public static void loadCommonConfigs() {
        NecConfigAPI.registerConfig(UltimatumConfig.class);
        MAIN = UltimatumConfig.INSTANCE;
    }

    public static void loadClientConfigs() {}
}
