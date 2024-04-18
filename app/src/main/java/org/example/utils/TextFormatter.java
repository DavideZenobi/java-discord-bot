package org.example.utils;

public class TextFormatter {

    public static String formatPokemonName(String pokemonName) {
        String updatedName = pokemonName.substring(0, 1).toUpperCase() + pokemonName.substring(1);

        if (pokemonName.contains("-")) {
            updatedName = updatedName.replace("-", " ");
        }

        return updatedName;
    }

    public static String convertToBold(String text) {
        return "**" + text + "**";
    }
    public static String convertToItalic(String text) {
        return "*" + text + "*";
    }
}
