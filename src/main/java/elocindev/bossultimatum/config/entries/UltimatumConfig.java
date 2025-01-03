package elocindev.bossultimatum.config.entries;

import elocindev.bossultimatum.BossUltimatum;
import elocindev.necronomicon.api.config.v1.NecConfigAPI;
import elocindev.necronomicon.config.NecConfig;

public class UltimatumConfig {
    @NecConfig public static UltimatumConfig INSTANCE;


    public static String getFile() {
        return NecConfigAPI.getFile(BossUltimatum.MODID+".json5");
    }

    public String myString = "example";
    public int myNumber = 1;
    public boolean myBoolean = true;
    public double myDouble = 1.0;
}
