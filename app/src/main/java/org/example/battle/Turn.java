package org.example.battle;

import org.example.pokemon.PokemonState;
import org.example.pokemon.ailments.Ailment;
import org.example.pokemon.enums.*;
import org.example.pokemon.moves.*;

import java.util.List;

public class Turn {
    
    private PokemonState pokemon1;
    private PokemonState pokemon2;
    private int currentTurn;

    public Turn(PokemonState pokemon1, PokemonState pokemon2, int currentTurn) {
        this.pokemon1 = pokemon1;
        this.pokemon2 = pokemon2;
        this.currentTurn = currentTurn;
    }

    public void applyInitialEffects(PokemonState pokemon) {
        if (pokemon.getEffects().isEmpty()) {
            return;
        }

        //List<Ailment> ailments = pokemon.getEffects();

        // TODO
        /*switch (pokemon.getPokeMove()) {
            case DamageEffectPokeMove damageEffectPokeMove -> pokeMoveApplier.apply(damageEffectPokeMove, pokemon);
        }*/
    }

    public void applyMove(PokemonState attackingPokemon, PokemonState defendingPokemon) {
        switch (attackingPokemon.getMove()) {
            case AilmentMove move -> applyAilmentMove(attackingPokemon, defendingPokemon);
            case DamageAilmentMove move -> applyDamageAilmentMove(attackingPokemon, defendingPokemon);
            case DamageHealMove move -> applyDamageHealMove(attackingPokemon, defendingPokemon);
            case DamageLowerMove move -> applyDamageLowerMove(attackingPokemon, defendingPokemon);
            case DamageMove move -> applyDamageMove(attackingPokemon, defendingPokemon);
            case DamageRaiseMove move -> applyDamageRaiseMove(attackingPokemon, defendingPokemon);
            case HealMove move -> applyHealMove(attackingPokemon);
            case NetGoodStatsMove move -> applyNetGoodStatsMove(attackingPokemon, defendingPokemon);
            case SwaggerMove move -> applySwaggerMove(attackingPokemon, defendingPokemon);
        }
    }

    /**  */
    public void applyStageModifiers(PokemonState pokemon1, PokemonState pokemon2) {
        pokemon1.applyModifiers();
        pokemon2.applyModifiers();
    }

    // Ejemplo: quemar, veneno
    public void applyFinalEffects(PokemonState pokemon) {
        if (pokemon.getEffects().isEmpty()) {
            return;
        }
    }

    /**
     *  Fases:
     *   1: Comprobar y calcular si el pokemon puede usar el movimiento a causa de algun Ailment(sleep, paralysis, freeze, etc.) o por flinch(retroceso).
     *   2: Comprobar y calcular si el pokemon acierta o falla el ataque.
     *   3: Aplicar ailment
     * */
    private void applyAilmentMove(PokemonState attacker, PokemonState defender) {
        var ailmentMove = (AilmentMove) attacker.getMove(); /**---------------------- ¡¡ IMPORTANTE !!----------------------------------- */

        boolean canHit = canHitByAilments(attacker);
        if (canHit) {

            float probabilityToHit = Calculator.calculateProbabilityToHit(ailmentMove.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
            boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);

            if (hasHit) {

                Ailments ailmentToApply = ailmentMove.ailment();

                // Si ya tiene el ailment, no se puede volver a aplicar
                if (defender.getAilmentsEnum().contains(ailmentToApply)) {
                // TODO
                } else {
                    defender.getAilmentsEnum().add(ailmentToApply);
                }

            } else {

            }

        } else {

        }
    }

    private void applyDamageAilmentMove(PokemonState attacker, PokemonState defender) {
        var damageAilmentMove = (DamageAilmentMove) attacker.getMove();

        boolean canHit = canHitByAilments(attacker);
        if (canHit) {

            float probabilityToHit = Calculator.calculateProbabilityToHit(damageAilmentMove.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
            boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);

            if (hasHit) {

                var formula = new Calculator.DamageFormulaData(
                        attacker.getLevel(),
                        damageAilmentMove.power(),
                        damageAilmentMove.type(),
                        damageAilmentMove.damageClass(),
                        damageAilmentMove.damageClass() == DamageClasses.PHYSICAL ? attacker.getCurrentAttack() : attacker.getCurrentSpecialAttack(),
                        attacker.getTypes(),
                        damageAilmentMove.damageClass() == DamageClasses.PHYSICAL ? defender.getCurrentDefense() : defender.getCurrentSpecialDefense(),
                        defender.getTypes(),
                        attacker.getTypes().contains(damageAilmentMove.type()),
                        attacker.getCurrentCrit(),
                        attacker.getAilmentsEnum().contains(Ailments.BURN)
                );

                int damageDone = Calculator.calculateDamage(formula);
                defender.applyDamage(defender.getCurrentHp() - damageDone);

                boolean hasFlinched = Calculator.applyProbability(damageAilmentMove.flinchChance());
                if (hasFlinched) {
                    defender.setFlinched(true);
                }

                boolean hasAppliedAilment = Calculator.applyProbability(damageAilmentMove.ailmentAccuracy());
                if (hasAppliedAilment) {
                    Ailments ailmentToApply = damageAilmentMove.ailment();

                    if (defender.getAilmentsEnum().contains(ailmentToApply)) {
                        // TODO
                    } else {
                        defender.getAilmentsEnum().add(ailmentToApply);
                    }
                }

            } else {
                // Move missed
                // TODO
            }

        } else {
            // Unable to do move
            // TODO
        }
    }

    private void applyDamageHealMove(PokemonState attacker, PokemonState defender) {
        var damageHealMove = (DamageHealMove) attacker.getMove();

        /** Fase 1 */
        boolean canHit = canHitByAilments(attacker);
        if (canHit) {
            /** Fase 2 */
            float probabilityToHit = Calculator.calculateProbabilityToHit(damageHealMove.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
            boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);

            if (hasHit) {
                /** Fase 3 */
                var formula = new Calculator.DamageFormulaData(
                        attacker.getLevel(),
                        damageHealMove.power(),
                        damageHealMove.type(),
                        damageHealMove.damageClass(),
                        damageHealMove.damageClass() == DamageClasses.PHYSICAL ? attacker.getCurrentAttack() : attacker.getCurrentSpecialAttack(),
                        attacker.getTypes(),
                        damageHealMove.damageClass() == DamageClasses.PHYSICAL ? defender.getCurrentDefense() : defender.getCurrentSpecialDefense(),
                        defender.getTypes(),
                        attacker.getTypes().contains(damageHealMove.type()),
                        attacker.getCurrentCrit(),
                        attacker.getAilmentsEnum().contains(Ailments.BURN)
                );

                int damageDone = Calculator.calculateDamage(formula);
                defender.applyDamage(defender.getCurrentHp() - damageDone);

                int healingAmount = Calculator.calculateHealingBasedOnDamage(damageHealMove.drain(), damageDone);
                attacker.applyHeal(healingAmount);

                /** Fase 4 */
                boolean hasFlinched = Calculator.applyProbability(damageHealMove.flinchChance());
                if (hasFlinched) {
                    defender.setFlinched(true);
                }

            } else {
                // Move missed
                // TODO
            }
        } else {
            // Unable to do move
            // TODO
        }
    }

    private void applyDamageLowerMove(PokemonState attacker, PokemonState defender) {
        var damageLowerMode = (DamageLowerMove) attacker.getMove();

        /** Fase 1 */
        boolean canHit = canHitByAilments(attacker);
        if (canHit) {
            /** Fase 2 */
            float probabilityToHit = Calculator.calculateProbabilityToHit(damageLowerMode.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
            boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);

            if (hasHit) {
                /** Fase 3 */
                var formula = new Calculator.DamageFormulaData(
                        attacker.getLevel(),
                        damageLowerMode.power(),
                        damageLowerMode.type(),
                        damageLowerMode.damageClass(),
                        damageLowerMode.damageClass() == DamageClasses.PHYSICAL ? attacker.getCurrentAttack() : attacker.getCurrentSpecialAttack(),
                        attacker.getTypes(),
                        damageLowerMode.damageClass() == DamageClasses.PHYSICAL ? defender.getCurrentDefense() : defender.getCurrentSpecialDefense(),
                        defender.getTypes(),
                        attacker.getTypes().contains(damageLowerMode.type()),
                        attacker.getCurrentCrit(),
                        attacker.getAilmentsEnum().contains(Ailments.BURN)
                );

                int damageDone = Calculator.calculateDamage(formula);
                defender.applyDamage(defender.getCurrentHp() - damageDone);

                /** Fase 4 */
                boolean hasFlinched = Calculator.applyProbability(damageLowerMode.flinchChance());
                if (hasFlinched) {
                    defender.setFlinched(true);
                }

                boolean hasDefenderStatsChanged = Calculator.applyProbability(damageLowerMode.statChance());
                if (hasDefenderStatsChanged) {
                    for (Stats stat : damageLowerMode.statsNames()) {
                        defender.updateStatModifier(stat, damageLowerMode.statChange());
                        defender.applyModifiers();
                    }
                }

            } else {
                // Move missed
                // TODO
            }
        } else {
            // Unable to do move
            // TODO
        }
    }

    /**
     * Fases:
     *  1: Comprobar y calcular si el pokemon puede usar el movimiento a causa de algun Ailment(sleep, paralysis, freeze, etc.) o por flinch(retroceso).
     *  2: Comprobar y calcular si el pokemon acierta o falla el ataque.
     *  3: Calcular daño y aplicar daño en el pokemon defensor.
     *  4: Comprobar si el movimiento flinchea al enemigo.
     * */
    private void applyDamageMove(PokemonState attacker, PokemonState defender) {
        var damageMove = (DamageMove) attacker.getMove();

        /** Fase 1 */
        boolean canHit = canHitByAilments(attacker);
        if (canHit) {
            /** Fase 2 */
            float probabilityToHit = Calculator.calculateProbabilityToHit(damageMove.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
            boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);

            if (hasHit) {
                /** Fase 3 */
                var formula = new Calculator.DamageFormulaData(
                        attacker.getLevel(),
                        damageMove.power(),
                        damageMove.type(),
                        damageMove.damageClass(),
                        damageMove.damageClass() == DamageClasses.PHYSICAL ? attacker.getCurrentAttack() : attacker.getCurrentSpecialAttack(),
                        attacker.getTypes(),
                        damageMove.damageClass() == DamageClasses.PHYSICAL ? defender.getCurrentDefense() : defender.getCurrentSpecialDefense(),
                        defender.getTypes(),
                        attacker.getTypes().contains(damageMove.type()),
                        attacker.getCurrentCrit(),
                        attacker.getAilmentsEnum().contains(Ailments.BURN)
                );

                int damageDone = Calculator.calculateDamage(formula);
                defender.applyDamage(defender.getCurrentHp() - damageDone);

                /** Fase 4 */
                boolean hasFlinched = Calculator.applyProbability(damageMove.flinchChance());
                if (hasFlinched) {
                    defender.setFlinched(true);
                }

            } else {
                // Move missed
                // TODO
            }
        } else {
            // Unable to do move
            // TODO
        }

    }

    private void applyDamageRaiseMove(PokemonState attacker, PokemonState defender) {
        var damageRaiseMove = (DamageRaiseMove) attacker.getMove();

        boolean canHit = canHitByAilments(attacker);

        if (canHit) {
            float probabilityToHit = Calculator.calculateProbabilityToHit(damageRaiseMove.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
            boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);

            if (hasHit) {
                var formula = new Calculator.DamageFormulaData(
                        attacker.getLevel(),
                        damageRaiseMove.power(),
                        damageRaiseMove.type(),
                        damageRaiseMove.damageClass(),
                        damageRaiseMove.damageClass() == DamageClasses.PHYSICAL ? attacker.getCurrentAttack() : attacker.getCurrentSpecialAttack(),
                        attacker.getTypes(),
                        damageRaiseMove.damageClass() == DamageClasses.PHYSICAL ? defender.getCurrentDefense() : defender.getCurrentSpecialDefense(),
                        defender.getTypes(),
                        attacker.getTypes().contains(damageRaiseMove.type()),
                        attacker.getCurrentCrit(),
                        attacker.getAilmentsEnum().contains(Ailments.BURN)
                );

                int damageDone = Calculator.calculateDamage(formula);
                defender.applyDamage(defender.getCurrentHp() - damageDone);

                boolean hasFlinched = Calculator.applyProbability(damageRaiseMove.flinchChance());
                if (hasFlinched) {
                    defender.setFlinched(true);
                }

                boolean hasAttackerStatsChanged = Calculator.applyProbability(damageRaiseMove.statChance());
                if (hasAttackerStatsChanged) {
                    for (Stats stat : damageRaiseMove.statsNames()) {
                        attacker.updateStatModifier(stat, damageRaiseMove.statChange());
                        attacker.applyModifiers();
                    }
                }

            } else {

            }

        } else {
            // TODO
        }
    }

    private void applyNetGoodStatsMove(PokemonState attacker, PokemonState defender) {
        var netGoodStatsMove = (NetGoodStatsMove) attacker.getMove();

        boolean canHit = canHitByAilments(attacker);

        if (canHit) {
            float probabilityToHit = Calculator.calculateProbabilityToHit(netGoodStatsMove.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
            boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);
            if (hasHit) {
                // Si el target es USER el movimiento es sobre el.
                // Si no, es sobre el pokemon ENEMIGO.
                if (netGoodStatsMove.target() == Targets.USER) {
                    for (Stats stat : netGoodStatsMove.statsNames()) {
                        attacker.updateStatModifier(stat, netGoodStatsMove.statChange());
                        attacker.applyModifiers();
                    }

                } else {
                    for (Stats stat : netGoodStatsMove.statsNames()) {
                        defender.updateStatModifier(stat, netGoodStatsMove.statChange());
                        defender.applyModifiers();
                    }
                }

            } else {

            }

        } else {

        }
    }

    private void applyHealMove(PokemonState attacker) {
        var healMove = (HealMove) attacker.getMove();

        boolean canHit = canHitByAilments(attacker);

        if (canHit) {
            int healing = Calculator.calculateHealingBasedOnMaxHp(healMove.healing(), attacker.getBaseHp());
            attacker.applyHeal(healing);
        } else {
            
        }
    }

    private void applySwaggerMove(PokemonState attacker, PokemonState defender) {
        var swaggerMove = (SwaggerMove) attacker.getMove();

        boolean canHit = canHitByAilments(attacker);
        if (canHit) {

            float probabilityToHit = Calculator.calculateProbabilityToHit(swaggerMove.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
            boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);

            if (hasHit) {
                for (Stats stat : swaggerMove.statsNames()) {
                    defender.updateStatModifier(stat, swaggerMove.statChange());
                    defender.applyModifiers();
                }

                Ailments ailmentToApply = swaggerMove.ailment();

                if (defender.getAilmentsEnum().contains(ailmentToApply)) {
                    // TODO
                } else {
                    defender.getAilmentsEnum().add(ailmentToApply);
                }

            } else {

            }

        } else {

        }
    }


    /** --UTILITY-- */
    private boolean canHitByAilments(PokemonState attacker) {
        if (attacker.isFlinched()) {
            return false;
        }

        if (attacker.getAilmentsEnum().contains(Ailments.FREEZE)) {
            return false;
        }

        if (attacker.getAilmentsEnum().contains(Ailments.SLEEP)) {
            return false;
        }

        if (attacker.getAilmentsEnum().contains(Ailments.CONFUSION)) {
            boolean canHit = Calculator.applyProbability(67);
            if (!canHit) {
                return false;
            }
        }

        if (attacker.getAilmentsEnum().contains(Ailments.PARALYSIS)) {
            boolean canHit = Calculator.applyProbability(75);
            if (!canHit) {
                return false;
            }
        }

        if (attacker.getAilmentsEnum().contains(Ailments.INFATUATION)) {
            boolean canHit = Calculator.applyProbability(50);
            if (!canHit) {
                return false;
            }
        }

        return true;
    }

    private void clean(PokemonState pokemon1, PokemonState pokemon2) {
        // TODO
        pokemon1.setFlinched(false);
        pokemon2.setFlinched(false);
    }

}
