package org.example.pokemon.moves;

import org.example.pokemon.enums.Categories;
import org.example.pokemon.enums.DamageClasses;
import org.example.pokemon.enums.Types;

sealed public interface MoveV2 permits DamageMove, DamageAilmentMove, DamageHealMove, DamageLowerMove, DamageRaiseMove,
        AilmentMove, HealMove, NetGoodStatsMove, SwaggerMove {

    int id();
    String name();
    Types type();
    Categories category();
    DamageClasses damageClass();
    Integer accuracy();
    int priority();
}
