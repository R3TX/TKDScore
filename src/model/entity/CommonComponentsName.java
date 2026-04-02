package model.entity;

import java.util.List;

public class CommonComponentsName {

    public static List<StatConfig> commonStats = List.of(
            new StatConfig("Gam-Jeon", "gamJeomCount", "getGamJeomFouls"),
            new StatConfig("Técnica alta", "headKickCount", "getHeadKicks"),
            new StatConfig("Puntos", "baseScoreCount", "getBaseScore")
    );
}
