package org.example.pokemon.ailments;

import org.example.pokemon.enums.Ailments;

public interface Ailment {
    int id();
    Ailments name();
    int accuracy();
}
