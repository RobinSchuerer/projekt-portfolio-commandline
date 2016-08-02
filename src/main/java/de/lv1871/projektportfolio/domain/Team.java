package de.lv1871.projektportfolio.domain;

public class Team {

    private String name;

    private Team(Builder builder) {
        setName(builder.name);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        return name != null ? name.equals(team.name) : team.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public static final class Builder {
        private String name;

        private Builder() {
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Team build() {
            return new Team(this);
        }
    }
}
