package de.lv1871.projektportfolio.domain;

import com.project.portfolio.domain.Team;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by SchuererR on 16.06.2016.
 */
public class TeamKapazitaet {

    private LocalDate monat;
    private Team team;
    private BigDecimal kapazitaet;

    public LocalDate getMonat() {
        return monat;
    }

    public void setMonat(LocalDate monat) {
        this.monat = monat;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public BigDecimal getKapazitaet() {
        return kapazitaet;
    }

    public void setKapazitaet(BigDecimal kapazitaet) {
        this.kapazitaet = kapazitaet;
    }
}
