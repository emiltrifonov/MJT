package bg.sofia.uni.fmi.mjt.show;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;
import bg.sofia.uni.fmi.mjt.show.elimination.EliminationRule;
import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

import java.util.Arrays;

public final class ShowAPIImpl implements ShowAPI{
    private Ergenka[] ergenkas;
    private final EliminationRule[] defaultEliminationRules;

    public ShowAPIImpl(Ergenka[] ergenkas, EliminationRule[] defaultEliminationRules) {
        this.ergenkas = Arrays.copyOf(ergenkas, ergenkas.length);
        this.defaultEliminationRules = Arrays.copyOf
                (defaultEliminationRules, defaultEliminationRules.length);
    }

    @Override
    public Ergenka[] getErgenkas() {
        return Arrays.copyOf(ergenkas, ergenkas.length);
    }

    @Override
    public void playRound(DateEvent dateEvent) {
        for (Ergenka ergenka : ergenkas) {
            organizeDate(ergenka, dateEvent);
        }
    }

    @Override
    public void eliminateErgenkas(EliminationRule[] eliminationRules) {
        if (eliminationRules.length == 0) {
            eliminateErgenkasWithSetOfRules(defaultEliminationRules);
        }
        else {
            eliminateErgenkasWithSetOfRules(eliminationRules);
        }
    }

    @Override
    public void organizeDate(Ergenka ergenka, DateEvent dateEvent) {
        ergenka.reactToDate(dateEvent);
    }

    private void eliminateErgenkasWithSetOfRules(EliminationRule[] eliminationRules) {
        for (EliminationRule eliminationRule : eliminationRules) {
            ergenkas = eliminationRule.eliminateErgenkas(ergenkas);
        }
    }
}
