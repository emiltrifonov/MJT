package bg.sofia.uni.fmi.mjt.show;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;
import bg.sofia.uni.fmi.mjt.show.elimination.EliminationRule;
import bg.sofia.uni.fmi.mjt.show.elimination.LowestRatingEliminationRule;
import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public final class ShowAPIImpl implements ShowAPI {
    private Ergenka[] ergenkas;
    private final EliminationRule[] defaultEliminationRules;

    public ShowAPIImpl(Ergenka[] ergenkas, EliminationRule[] defaultEliminationRules) {
        this.ergenkas = ergenkas;

        if (defaultEliminationRules == null || areAllRulesNull(defaultEliminationRules)) {
            this.defaultEliminationRules = new EliminationRule[1];
            this.defaultEliminationRules[0] = new LowestRatingEliminationRule();
        }  else {
            this.defaultEliminationRules = defaultEliminationRules;
        }
    }

    @Override
    public Ergenka[] getErgenkas() {
        return ergenkas;
    }

    @Override
    public void playRound(DateEvent dateEvent) {
        if (dateEvent == null || ergenkas == null) {
            return;
        }

        for (Ergenka ergenka : ergenkas) {
            if (ergenka != null) {
                organizeDate(ergenka, dateEvent);
            }
        }
    }

    @Override
    public void eliminateErgenkas(EliminationRule[] eliminationRules) {
        if (eliminationRules == null || eliminationRules.length == 0) {
            eliminateErgenkasWithSetOfRules(defaultEliminationRules);
        }  else {
            eliminateErgenkasWithSetOfRules(eliminationRules);
        }
    }

    @Override
    public void organizeDate(Ergenka ergenka, DateEvent dateEvent) {
        ergenka.reactToDate(dateEvent);
    }

    private void eliminateErgenkasWithSetOfRules(EliminationRule[] eliminationRules) {
        if (eliminationRules == null) {
            ergenkas = new LowestRatingEliminationRule().eliminateErgenkas(ergenkas);
            return;
        }

        for (EliminationRule eliminationRule : eliminationRules) {
            if (eliminationRule != null) {
                ergenkas = eliminationRule.eliminateErgenkas(ergenkas);
            }
        }
    }

    private boolean areAllRulesNull(EliminationRule[] eliminationRules) {
        for (EliminationRule rule : eliminationRules) {
            if (rule != null) {
                return false;
            }
        }

        return true;
    }
}
