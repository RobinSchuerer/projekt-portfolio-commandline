package de.lv1871.projektportfolio.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by SchuererR on 02.08.2016.
 */
public class AufwandProMonat {

    private BigDecimal aufwand;
    private LocalDate monat;

    private AufwandProMonat(Builder builder) {
        aufwand = builder.aufwand;
        monat = builder.monat;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public BigDecimal getAufwand() {
        return aufwand.setScale(2);
    }

    public LocalDate getMonat() {
        return monat;
    }


    public static final class Builder {
        private BigDecimal aufwand;
        private LocalDate monat;

        private Builder() {
        }

        public Builder withAufwand(BigDecimal val) {
            aufwand = val;
            return this;
        }

        public Builder withMonat(LocalDate val) {
            monat = val;
            return this;
        }

        public AufwandProMonat build() {
            return new AufwandProMonat(this);
        }
    }
}
