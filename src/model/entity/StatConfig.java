package model.entity;

/**
 * @param getterName Nombre del método getter en RoundStatisticsEntity
 */ // Clase auxiliar para definir cada columna de la estadística
public record StatConfig(String name, String componentName, String getterName) {
}