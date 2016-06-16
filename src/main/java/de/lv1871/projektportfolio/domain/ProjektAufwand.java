package de.lv1871.projektportfolio.domain;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SchuererR on 16.06.2016.
 */
public class ProjektAufwand {

    private Projekt projekt;
    private Map<Team, BigDecimal> aufwaende = new HashMap<>();

    public Projekt getProjekt() {
        return projekt;
    }

    public void setProjekt(Projekt projekt) {
        this.projekt = projekt;
    }

    public Map<Team, BigDecimal> getAufwaende() {
        return aufwaende;
    }

    public void setAufwaende(Map<Team, BigDecimal> aufwaende) {
        this.aufwaende = aufwaende;
    }
}
