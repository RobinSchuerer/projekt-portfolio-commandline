package de.lv1871.projektportfolio.domain;

import java.time.LocalDate;

/**
 * Created by SchuererR on 16.06.2016.
 */
public class Projekt {

    private String name;
    private ProjektTyp typ;
    private int prioritaet;
    private LocalDate deadLine;

    private Projekt(Builder builder) {
        setName(builder.name);
        setTyp(builder.typ);
        setPrioritaet(builder.prioritaet);
        setDeadLine(builder.deadLine);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjektTyp getTyp() {
        return typ;
    }

    public void setTyp(ProjektTyp typ) {
        this.typ = typ;
    }

    public int getPrioritaet() {
        return prioritaet;
    }

    public void setPrioritaet(int prioritaet) {
        this.prioritaet = prioritaet;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }

    public static final class Builder {
        private String name;
        private ProjektTyp typ;
        private int prioritaet;
        private LocalDate deadLine;

        private Builder() {
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withTyp(ProjektTyp val) {
            typ = val;
            return this;
        }

        public Builder withPrioritaet(int val) {
            prioritaet = val;
            return this;
        }

        public Builder withDeadLine(LocalDate val) {
            deadLine = val;
            return this;
        }

        public Projekt build() {
            return new Projekt(this);
        }
    }
}
