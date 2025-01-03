package elocindev.bossultimatum.config.entries;

import java.util.List;

import elocindev.bossultimatum.BossUltimatum;
import elocindev.necronomicon.api.config.v1.NecConfigAPI;
import elocindev.necronomicon.config.Comment;
import elocindev.necronomicon.config.NecConfig;

public class UltimatumConfig {
    @NecConfig public static UltimatumConfig INSTANCE;


    public static String getFile() {
        return NecConfigAPI.getFile(BossUltimatum.MODID+".json5");
    }

    public class InnerHealingConfig {
        public float health_gain_per_death;
        public int ultimatum_death_count;
        public boolean ultimatum_heals_maxhp;
        public float ultimatum_death_healing;
        public boolean require_difficulty;
        public List<String> difficulty;
        public boolean remove_boss_instead_of_healing;

        public InnerHealingConfig(float health_gain_per_death, int ultimatum_death_count, boolean ultimatum_heals_maxhp, float ultimatum_death_healing, boolean require_difficulty, List<String> difficulty, boolean remove_boss_instead_of_healing) {
            this.health_gain_per_death = health_gain_per_death;
            this.ultimatum_death_count = ultimatum_death_count;
            this.ultimatum_heals_maxhp = ultimatum_heals_maxhp;
            this.ultimatum_death_healing = ultimatum_death_healing;
            this.require_difficulty = require_difficulty;
            this.difficulty = difficulty;
            this.remove_boss_instead_of_healing = remove_boss_instead_of_healing;
        }
    }

    public class Ultimatum {
        public String entity_regex;
        public int minimum_hp;
        public List<InnerHealingConfig> healing_events;

        public Ultimatum(String entity_regex, int minimum_hp, List<InnerHealingConfig> healing_events) {
            this.entity_regex = entity_regex;
            this.minimum_hp = minimum_hp;
            this.healing_events = healing_events;
        }
    }
    @Comment("--------------------------------------------------------------------------------------------------------")
    @Comment("                                   Boss Ultimatum by ElocinDev")
    @Comment("--------------------------------------------------------------------------------------------------------")
    @Comment(" - What does each config do?")
    @Comment("   - entity_regex: The regex to match the entity name. It can be a single entity name (e.g. minecraft:zombie) or a regular expression (e.g. minecraft.*)")
    @Comment("   - minimum_hp: The minimum health the entity must have to be considered a boss. This is useful when doing a regex of all entities from a mod, to avoid applying ultimatums to normal mobs.")
    @Comment("   - healing_events: A list of healing events that will be applied to the boss when it dies.")

    @Comment(" - What does a healing event do?")
    @Comment("  Healing events are a series of actions that will be applied based on a few conditions you set, such as:")
    @Comment("   - health_gain_per_death: The amount of health the boss will gain when a nearby player dies. This happes every time and is affected by the difficulty and minimum_hp conditions, but it's not part of the ultimatum.")
    @Comment("   - ultimatum_death_count: The amount of times the boss must die to perform the ultimatum event.")
    @Comment("   - ultimatum_heals_maxhp: If true, the ultimatum_death_healing value will be set to max health.")
    @Comment("   - ultimatum_death_healing: The amount of health the boss will gain when it dies the amount of times set in ultimatum_death_count. If ultimatum_heals_maxhp is set, then the value should be from 0 to 1, so 0.50 would be 50% of the max health.")
    @Comment("   - require_difficulty: If true, the ultimatum will only be applied if the difficulty is in the list of difficulties.")
    @Comment("   - difficulty: A list of difficulties that the ultimatum will be applied to. The difficulties are: easy, normal, hard. Hardcore is technically hard too, but in hardcore players will die permanently so it's a bit redundant.")
    @Comment("   - remove_boss_instead_of_healing: If true, the boss will be removed instead of healing, so it'd completely disappear, forcing the player to resummon or look for another boss.")
    public List<Ultimatum> ultimatums = List.of(
        new Ultimatum("example_boss_mod:.*", 0, List.of(
            new InnerHealingConfig(0.1f, 3, true, 0.5f, false, List.of("easy", "normal", "hard"), false),
            new InnerHealingConfig(0.0f, 3, true, 0.5f, true, List.of("hard"), true)
        ))
    );
}
