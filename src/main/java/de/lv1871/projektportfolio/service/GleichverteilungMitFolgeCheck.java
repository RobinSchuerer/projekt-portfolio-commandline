package de.lv1871.projektportfolio.service;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import de.lv1871.projektportfolio.domain.*;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static de.lv1871.projektportfolio.domain.ProjektTyp.MUSS_PROJEKT;
import static de.lv1871.projektportfolio.domain.ProjektTyp.PRODUKT_PROJEKT;

/**
 * In der ersten Iteration wird alles gleich verteilt. Danach folgt ein Check ob die Restriktionen eingehalten sind.
 * Wenn dieser Check nicht eingehalten wird. So werden die Aufwände entsprechend der #
 */
public class GleichverteilungMitFolgeCheck implements PflichtProjektStrategy {

    private final static Set<ProjektTyp> PFLICHT_PROJEKTE = ImmutableSet.of(MUSS_PROJEKT, PRODUKT_PROJEKT);

    private final static SimplePflichtProjektStrategy SIMPLE_PFLICHT_PROJEKT_STRATEGY = new SimplePflichtProjektStrategy();

    @Nonnull
    @Override
    public ProjektPortfolioVorschlag verarbeite(@Nonnull ProjektPortfolioEingabeDaten portfolioEingabeDaten,
                                                @Nonnull Team team) {

        List<ProjektAufwand> mussProjekte = getMussProjekte(portfolioEingabeDaten, team);
        List<ProjektAufwand> produktProjekte = getProduktProjekte(portfolioEingabeDaten, team);
        Map<ProjektTyp, List<ProjektAufwand>> aufwandByTyp = Maps.newHashMap();
        aufwandByTyp.put(MUSS_PROJEKT, mussProjekte);
        aufwandByTyp.put(PRODUKT_PROJEKT, produktProjekte);

        List<ProjektAufwand> alleProjekte = getAlleProjekte(mussProjekte, produktProjekte);

        ProjektPortfolioVorschlag result = ProjektPortfolioVorschlag.newBuilder().build();

        if (alleProjekte.isEmpty()) {
            return result;
        }

        // letzte Deadline von hier wird rückwärts aufgeteilt
        LocalDate deadLine = getSpaetesteDeadline(alleProjekte);

        Map<Projekt, BigDecimal> todoMap = getTodoMap(team, alleProjekte);

        // für jeden monat
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            LocalDate aktuellerMonat = deadLine.minusMonths(i);

            if(portfolioEingabeDaten.isAusserhalbZeitraum(aktuellerMonat)){
                // TODO: 24.08.2016 overflow handling
                break;
            }

            List<Projekt> projekteFuerDiesenMonat = getRelevanteProjekte(alleProjekte, aktuellerMonat, todoMap);

            // Gleichverteilung: jedes Projekt bekommt den gleichen Anteil
            HashMap<Projekt, BigDecimal> gleichVerteilung =
                    gleichVerteilung(portfolioEingabeDaten, team, todoMap, aktuellerMonat, projekteFuerDiesenMonat);

            Set<ProjektTyp> invalid = PFLICHT_PROJEKTE.stream()
                    .filter(projektTyp -> !check(gleichVerteilung, portfolioEingabeDaten, team, aktuellerMonat, projektTyp))
                    .collect(Collectors.toSet());

            if (invalid.isEmpty()) {
                toErgebnis(result, gleichVerteilung, team, aktuellerMonat);
                updateTodoMap(todoMap, gleichVerteilung);
                continue;
            }

            // ermittle die Verteilung für den Fall "Restriktionen"
            Map<ProjektTyp, BigDecimal> verteilungsMap = getVerteilungen(portfolioEingabeDaten,
                    invalid, team, aktuellerMonat, aufwandByTyp);

            for (ProjektTyp projektTyp : PFLICHT_PROJEKTE) {

                BigDecimal monatsBudget = verteilungsMap.get(projektTyp);

                verteileNachRestriktion(monatsBudget,
                        team,
                        aufwandByTyp,
                        result,
                        todoMap,
                        aktuellerMonat,
                        projektTyp);

            };

        }

        return result;
    }

    private Map<ProjektTyp, BigDecimal> getVerteilungen(@Nonnull ProjektPortfolioEingabeDaten portfolioEingabeDaten,
                                                        @Nonnull Set<ProjektTyp> invalid,
                                                        @Nonnull Team team,
                                                        @Nonnull LocalDate aktuellerMonat,
                                                        @Nonnull Map<ProjektTyp, List<ProjektAufwand>> aufwandByTyp) {
        HashMap<ProjektTyp, BigDecimal> result = Maps.newHashMap();

        Optional<BigDecimal> kapazitaet = portfolioEingabeDaten.getKapazitaet(team, aktuellerMonat);
        if (!kapazitaet.isPresent()) {
            return result;
        }

        boolean keineMussProjekteVorhanden = aufwandByTyp.get(MUSS_PROJEKT).isEmpty();
        boolean keineProduktProjekteVorhanden = aufwandByTyp.get(PRODUKT_PROJEKT).isEmpty();
        boolean mussProjektVerletzt = invalid.contains(MUSS_PROJEKT);
        boolean produktProjektVerletzt = invalid.contains(PRODUKT_PROJEKT);

        if(keineMussProjekteVorhanden){
            result.put(MUSS_PROJEKT, BigDecimal.ZERO);
        }

        if(keineProduktProjekteVorhanden){
            result.put(PRODUKT_PROJEKT,BigDecimal.ZERO);
        }

        BigDecimal kapazitaetValue = kapazitaet.get();
        if(produktProjektVerletzt && !mussProjektVerletzt){
            // erst alles auf produkt und rest auf muss
            // erst alles auf muss und den rest auf produkt
            BigDecimal mussFaktor = portfolioEingabeDaten.getBeschraenkung(MUSS_PROJEKT).get();
            BigDecimal produktFaktor = portfolioEingabeDaten.getBeschraenkung(PRODUKT_PROJEKT).get();

            BigDecimal produktValue = kapazitaetValue.multiply(produktFaktor);
            result.put(PRODUKT_PROJEKT,produktValue);

            if(!keineMussProjekteVorhanden) {
                BigDecimal mussMax = kapazitaetValue.multiply(kapazitaetValue.multiply(mussFaktor));
                BigDecimal mussValue = kapazitaetValue
                        .subtract(produktValue)
                        .min(mussMax);

                result.put(MUSS_PROJEKT, mussValue);
            }


        }

        if(!produktProjektVerletzt && mussProjektVerletzt){
            // erst alles auf muss und den rest auf produkt
            BigDecimal mussFaktor = portfolioEingabeDaten.getBeschraenkung(MUSS_PROJEKT).get();
            BigDecimal produktFaktor = portfolioEingabeDaten.getBeschraenkung(PRODUKT_PROJEKT).get();

            BigDecimal mussValue = kapazitaetValue.multiply(mussFaktor);
            result.put(MUSS_PROJEKT,mussValue);

            if(!keineProduktProjekteVorhanden) {
                BigDecimal produktMax = kapazitaetValue.multiply(kapazitaetValue.multiply(produktFaktor));
                BigDecimal produktValue = kapazitaetValue
                        .subtract(mussValue)
                        .min(produktMax);

                result.put(PRODUKT_PROJEKT, produktValue);
            }
        }

        if(produktProjektVerletzt && mussProjektVerletzt){
            // beides nach restriktionen
            BigDecimal mussFaktor = portfolioEingabeDaten.getBeschraenkung(MUSS_PROJEKT).get();
            BigDecimal produktFaktor = portfolioEingabeDaten.getBeschraenkung(PRODUKT_PROJEKT).get();

            BigDecimal summeFaktoren = mussFaktor.add(produktFaktor);
            BigDecimal mussValue = kapazitaetValue.multiply(mussFaktor).divide(summeFaktoren, MathContext.DECIMAL32);

            BigDecimal produktValue = kapazitaetValue.multiply(produktFaktor).divide(summeFaktoren, MathContext.DECIMAL32);

            result.put(MUSS_PROJEKT, mussValue);
            result.put(PRODUKT_PROJEKT,produktValue);
        }

        return result;
    }

    private void verteileNachRestriktion(@Nonnull BigDecimal monatsMax,
                                         @Nonnull Team team,
                                         @Nonnull Map<ProjektTyp, List<ProjektAufwand>> aufwandByTyp,
                                         @Nonnull ProjektPortfolioVorschlag result,
                                         @Nonnull Map<Projekt, BigDecimal> todoMap,
                                         @Nonnull LocalDate aktuellerMonat,
                                         @Nonnull ProjektTyp projektTyp) {


        List<ProjektAufwand> aufwandList = aufwandByTyp.get(projektTyp);

        int gesamtAnzahl = aufwandList.size();
        BigDecimal gesamt = BigDecimal.valueOf(monatsMax.doubleValue());

        for (int i = 0; i < gesamtAnzahl; i++) {
            ProjektAufwand projektAufwand = aufwandList.get(i);
            BigDecimal anzahl = new BigDecimal(gesamtAnzahl-i);

            Projekt projekt = projektAufwand.getProjekt();
            BigDecimal proProjekt = gesamt.divide(anzahl, MathContext.DECIMAL32);

            BigDecimal todo = todoMap.get(projekt);
            BigDecimal monatsWert = proProjekt.min(todo);

            gesamt = gesamt.subtract(monatsWert);
            BigDecimal rest = todo.subtract(monatsWert);

            todoMap.put(projekt, rest);

            result.add(team, projekt, aktuellerMonat, monatsWert);
        }
    }

    private void updateTodoMap(@Nonnull Map<Projekt, BigDecimal> todoMap,
                               @Nonnull Map<Projekt, BigDecimal> gleichVerteilung) {

        gleichVerteilung.entrySet().forEach(projektUndAufwand -> {

            Projekt projekt = projektUndAufwand.getKey();
            BigDecimal todo = todoMap.get(projekt);
            BigDecimal rest = todo.subtract(gleichVerteilung.get(projekt));

            todoMap.put(projekt, rest);
        });
    }

    private void toErgebnis(@Nonnull ProjektPortfolioVorschlag result,
                            @Nonnull Map<Projekt, BigDecimal> gleichVerteilung,
                            @Nonnull Team team,
                            @Nonnull LocalDate aktuellerMonat) {

        gleichVerteilung.entrySet().forEach(projektUndAufwand -> {
            result.add(team, projektUndAufwand.getKey(), aktuellerMonat, projektUndAufwand.getValue());
        });


    }

    private boolean check(@Nonnull HashMap<Projekt, BigDecimal> gleichVerteilung,
                          @Nonnull ProjektPortfolioEingabeDaten eingabeDaten,
                          @Nonnull Team team,
                          @Nonnull LocalDate aktuellerMonat,
                          @Nonnull ProjektTyp typ) {
        Optional<BigDecimal> beschraenkung = eingabeDaten.getKapazitaet(team, aktuellerMonat, typ);
        if (!beschraenkung.isPresent()) {
            return true;
        }

        BigDecimal mussProjekteSumme = getSumme(gleichVerteilung, typ);

        return mussProjekteSumme.compareTo(beschraenkung.get()) < 1;
    }

    private BigDecimal getSumme(@Nonnull HashMap<Projekt, BigDecimal> gleichVerteilung,
                                @Nonnull ProjektTyp typ) {
        List<Map.Entry<Projekt, BigDecimal>> projekte = gleichVerteilung.entrySet()
                .stream()
                .filter(entry -> entry.getKey().getTyp() == typ)
                .collect(Collectors.toList());
        if(projekte.isEmpty()){
            return BigDecimal.ZERO;
        }

        return projekte
                .stream()
                .map(entry -> entry.getValue())
                .reduce(BigDecimal::add)
                .get();
    }

    private HashMap<Projekt, BigDecimal> gleichVerteilung(@Nonnull ProjektPortfolioEingabeDaten portfolioEingabeDaten,
                                                          @Nonnull Team team,
                                                          @Nonnull Map<Projekt, BigDecimal> todoMap,
                                                          @Nonnull LocalDate aktuellerMonat,
                                                          @Nonnull List<Projekt> projekteFuerDiesenMonat) {

        if(projekteFuerDiesenMonat.isEmpty()){
            return Maps.newHashMap();
        }

        Optional<BigDecimal> kapazitaet = portfolioEingabeDaten.getKapazitaet(team, aktuellerMonat);

        if (!kapazitaet.isPresent()) {
            // es steht diesen Monat keine Kapazität zur Verfügung
            return Maps.newHashMap();
        }

        int gesamtAnzahl = projekteFuerDiesenMonat.size();
        HashMap<Projekt, BigDecimal> gleichVerteilungsRunde = Maps.newHashMap();
        BigDecimal gesamt = kapazitaet.get();

        for (int i = 0; i < projekteFuerDiesenMonat.size(); i++) {
            Projekt projekt = projekteFuerDiesenMonat.get(i);

            BigDecimal anzahlProjekte = new BigDecimal(gesamtAnzahl - i);

            BigDecimal max = gesamt.divide(anzahlProjekte, BigDecimal.ROUND_HALF_UP);

            BigDecimal todo = todoMap.get(projekt);
            BigDecimal monatsAufwand = todo.min(max);

            gleichVerteilungsRunde.put(projekt, monatsAufwand);

            gesamt = gesamt.subtract(monatsAufwand);
        }

        return gleichVerteilungsRunde;
    }

    @Nonnull
    private Map<Projekt, BigDecimal> getTodoMap(@Nonnull Team team, List<ProjektAufwand> alleProjekte) {
        return alleProjekte
                .stream()
                .collect(Collectors.toMap(
                        ProjektAufwand::getProjekt,
                        projektAufwand -> projektAufwand.getAufwand(team)));
    }

    private LocalDate getSpaetesteDeadline(List<ProjektAufwand> alleProjekte) {
        return alleProjekte.stream().map(ProjektAufwand::getDeadLine).max(Ordering.natural()).get();
    }

    @Nonnull
    private List<ProjektAufwand> getAlleProjekte(@Nonnull List<ProjektAufwand> mussProjekte,
                                                 @Nonnull List<ProjektAufwand> produktProjekte) {
        List<ProjektAufwand> alleProjekte = Lists.newArrayList(mussProjekte);
        alleProjekte.addAll(produktProjekte);
        return alleProjekte;
    }

    private List<ProjektAufwand> getProduktProjekte(@Nonnull ProjektPortfolioEingabeDaten portfolioEingabeDaten,
                                                    @Nonnull Team team) {

        return portfolioEingabeDaten.getProjektAufwaende(
                ProjektAufwand.teamFilter(team).and(ProjektAufwand.PRODUKT_PROJEKT_FILTER),
                Ordering.allEqual());
    }

    private List<ProjektAufwand> getMussProjekte(@Nonnull ProjektPortfolioEingabeDaten portfolioEingabeDaten,
                                                 @Nonnull Team team) {

        return portfolioEingabeDaten.getProjektAufwaende(
                ProjektAufwand.teamFilter(team).and(ProjektAufwand.MUSS_PROJEKT_FILTER),
                Ordering.allEqual());
    }

    private List<Projekt> getRelevanteProjekte(@Nonnull List<ProjektAufwand> projekte,
                                               @Nonnull LocalDate aktuellerMonat,
                                               @Nonnull Map<Projekt, BigDecimal> todoMap) {

        List<Projekt> relevanteProjekte = projekte
                .stream()
                .filter(projektAufwand -> !aktuellerMonat.isAfter(projektAufwand.getDeadLine()))
                .filter(projektAufwand -> todoMap.get(projektAufwand.getProjekt()).doubleValue() > 0)
                .map(ProjektAufwand::getProjekt)
                .sorted((o1, o2) -> todoMap.get(o1).compareTo(todoMap.get(o2)))
                .collect(Collectors.toList());

        return relevanteProjekte;
    }

    private boolean nichtsZuTun(Map<Projekt, BigDecimal> todoMap) {

        return todoMap.values().stream().reduce(BigDecimal::add).get().doubleValue() <= 0;
    }
}