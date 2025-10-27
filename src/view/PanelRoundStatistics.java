package view;

import model.entity.RoundStatisticsEntity;
import model.entity.StatConfig;
import utils.ComponentBuilder;
import utils.TDKScoreUtils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PanelRoundStatistics extends JPanel {
    private Map<String, JLabel> statLabelsMap;
    private List<StatConfig> commonStats;

    public PanelRoundStatistics(RoundStatisticsEntity roundStat, int roundNumber) {
        setLayout(new GridBagLayout());
        Dimension statSize = new Dimension(50, 40);
        GridBagConstraints gbc = new GridBagConstraints();
        createCommonStatsList();

        setOpaque(false);
        gbc.fill = GridBagConstraints.BOTH;
        final double gridWeightx = 1.0 / commonStats.size();

        gbc.gridy = 0;
        gbc.weighty = 0.2;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;

        ComponentBuilder.createAndAddLabel(
                this,
                gbc,
                "Round " + roundNumber,
                JLabel.LEFT,
                false,
                statSize,
                null);


        buildStatRows(commonStats, gridWeightx, statSize, gbc);
        getLabelAndValueToUpDate(commonStats, roundStat);
    }

    private void createCommonStatsList() {
        commonStats = List.of(
                new StatConfig("Gam-Jeon", "gamJeomCount", "getGamJeomFouls"),
                new StatConfig("Técnica alta", "headKickCount", "getHeadKicks"),
                new StatConfig("Puntos", "baseScoreCount", "getBaseScore")
        );
    }


    private void getLabelAndValueToUpDate(List<StatConfig> commonStats, RoundStatisticsEntity stats) {
        for (StatConfig config : commonStats) {
            if (config.componentName() != null) {
                try {
                    // 1. Uso de Reflection para invocar el getter dinámicamente
                    Method getter = RoundStatisticsEntity.class.getMethod(config.getterName());

                    // 2. Obtener el valor del campo
                    int newValue = (int) getter.invoke(stats);

                    // 3. Crear el ID del JLabel y actualizar el mapa de UI
                    String componentId = config.componentName();

                    // Asumiendo que tienes el método 'updateLabelValue'
                    updateLabelValue(componentId, String.valueOf(newValue));

                } catch (Exception e) {
                    // Manejar errores de Reflection
                    System.err.println("Error al obtener valor con getter: " + config.getterName());
                }
            }
        }
    }

    private void updateLabelValue(String componentId, String newValue) {
        JLabel labelToUpdate = statLabelsMap.get(componentId);

        // 3. Actualizar el texto si se encuentra la etiqueta
        if (labelToUpdate != null) {
            labelToUpdate.setText(TDKScoreUtils.formatTextColor(newValue));
        } else {
            System.err.println("Error: No se encontró la etiqueta con ID: " + componentId);
        }
    }

    private void buildStatRows(List<StatConfig> statsConfig, double gridWeightx, Dimension statSize, GridBagConstraints gbc) {
        statLabelsMap = new HashMap<>();
        // Configuración general para la fila de Nombres (gridy = 3)
        gbc.gridy = 1;
        gbc.weighty = 0.4;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;

        // Bucle para crear las etiquetas de Nombres
        for (int i = 0; i < statsConfig.size(); i++) {
            StatConfig config = statsConfig.get(i);

            gbc.gridx = i; // La columna es simplemente el índice 'i'
            gbc.weightx = gridWeightx;

            ComponentBuilder.createAndAddLabel(
                    this,
                    gbc,
                    config.name(), // Texto del nombre de la estadística
                    JLabel.CENTER,
                    false,
                    statSize,
                    null // No necesitamos un nombre para el componente de nombre
            );
        }

        // Configuración general para la fila de Contadores (gridy = 4)
        gbc.gridy = 2;
        // (gridx se redefine en el bucle)

        // Bucle para crear las etiquetas de Contadores
        for (int i = 0; i < statsConfig.size(); i++) {
            StatConfig config = statsConfig.get(i);

            gbc.gridx = i;
            gbc.weightx = gridWeightx;

            String componentId = config.componentName() != null ? config.componentName() : null;

            JLabel counterLabel = ComponentBuilder.createAndAddLabel(
                    this,
                    gbc,
                    "0", // Valor inicial siempre es "0"
                    JLabel.CENTER,
                    false,
                    statSize,
                    componentId // Usamos el componentTag como ID único
            );
            if (componentId != null) {
                statLabelsMap.put(componentId, counterLabel);
            }
        }
    }
}
