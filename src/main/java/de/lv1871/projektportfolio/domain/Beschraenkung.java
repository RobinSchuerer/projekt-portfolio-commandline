package de.lv1871.projektportfolio.domain;

import java.math.BigDecimal;

/**
 * Created by SchuererR on 16.06.2016.
 */
public class Beschraenkung {

    private BigDecimal value = new BigDecimal("100.00");
    private ProjektTyp typ;

    private Beschraenkung(Builder builder) {
        setValue(builder.value);
        setTyp(builder.typ);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public BigDecimal getValue() {
        if(value == null){
            return null;
        }

        return value.setScale(2,BigDecimal.ROUND_HALF_UP);
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

    public static final class Builder {
        private BigDecimal value;
        private ProjektTyp typ;

        private Builder() {
        }

        public Builder withValue(BigDecimal val) {
            value = val;
            return this;
        }

        public Builder withTyp( ProjektTyp val) {
            typ = val;
            return this;
        }

        public Beschraenkung build() {
            return new Beschraenkung(this);
        }
    }
}
