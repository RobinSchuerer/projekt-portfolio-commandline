package de.lv1871.projektportfolio.domain;

import java.time.LocalDate;

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

    private void setName(String name) {
        this.name = name;
    }

    public ProjektTyp getTyp() {
        return typ;
    }

    private void setTyp(ProjektTyp typ) {
        this.typ = typ;
    }

    public int getPrioritaet() {
        return prioritaet;
    }

    private void setPrioritaet(int prioritaet) {
        this.prioritaet = prioritaet;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    private void setDeadLine(LocalDate deadLine) {
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

    @Override
    public String toString() {
        return "Projekt{" +
                "name='" + name + '\'' +
                ", typ=" + typ +
                ", prioritaet=" + prioritaet +
                ", deadLine=" + deadLine +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Projekt projekt = (Projekt) o;

        return name.equals(projekt.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
