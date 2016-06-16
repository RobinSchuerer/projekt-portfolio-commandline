package de.lv1871.projektportfolio.domain;

import java.math.BigDecimal;

/**
 * Created by SchuererR on 16.06.2016.
 */
public class Beschraenkung {

    private BigDecimal value = new BigDecimal("100.00");
    private ProjektTyp typ;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public ProjektTyp getTyp() {
        return typ;
    }

    public void setTyp(ProjektTyp typ) {
        this.typ = typ;
    }
}
