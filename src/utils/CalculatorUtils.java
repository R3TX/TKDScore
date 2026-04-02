package utils;

import model.entity.RoundStatisticsEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalculatorUtils {

    public static String calculate(Map<Integer, List<RoundStatisticsEntity>> playersScore){
        if (playersScore == null || playersScore.size() < 2) return null;

        // Extraemos los IDs dinámicamente
        List<Integer> ids = new ArrayList<>(playersScore.keySet());
        Integer playerA_Id = ids.get(0);
        Integer playerB_Id = ids.get(1);

        List<RoundStatisticsEntity> roundsA = playersScore.get(playerA_Id);
        List<RoundStatisticsEntity> roundsB = playersScore.get(playerB_Id);

        int winsA = 0;
        int winsB = 0;

        // Usamos el tamaño de la lista más corta para evitar errores de índice
        int totalRounds = Math.min(roundsA.size(), roundsB.size());

        for (int i = 0; i < totalRounds; i++) {
            int result = compareRound(roundsA.get(i), roundsB.get(i));

            if (result > 0) winsA++;
            else if (result < 0) winsB++;
        }

        // Retornar el ID del ganador final
        if (winsA > winsB) return roundsA.get(0).getCompetitor().getAssignedColor();
        if (winsB > winsA) return roundsB.get(0).getCompetitor().getAssignedColor();

        return null; // Empate global
    }

    private static int compareRound(RoundStatisticsEntity a, RoundStatisticsEntity b) {
        // REGLA 1: Gamjeom (Penalizaciones)
        // Si un jugador llega a 5, pierde el round automáticamente
        boolean aPierdePorFaltas = a.getGamJeomFouls() >= 5;
        boolean bPierdePorFaltas = b.getGamJeomFouls() >= 5;

        if (aPierdePorFaltas && !bPierdePorFaltas) return -1; // Gana B
        if (bPierdePorFaltas && !aPierdePorFaltas) return 1;  // Gana A
        if (aPierdePorFaltas && bPierdePorFaltas) return 0;   // Ambos descalificados en el round

        // REGLA 2: Base Score
        return Integer.compare(a.getBaseScore(), b.getBaseScore());

    }
}
