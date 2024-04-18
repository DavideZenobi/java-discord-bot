package org.example.battle;

import org.example.discord.DiscordHandler;
import org.example.pokemon.PokemonState;
import org.example.pokemon.TypeEffectiveness;
import org.example.pokemon.ailments.*;
import org.example.pokemon.enums.*;
import org.example.pokemon.moves.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Turn {

    UUID uuid;
    private PokemonState pokemon1;
    private PokemonState pokemon2;
    private final LoggerV2 loggerV2;
    private final DiscordHandler discordHandler;
    private final int currentTurn;

    public Turn(UUID uuid, PokemonState pokemon1, PokemonState pokemon2, LoggerV2 loggerV2, DiscordHandler discordHandler, int currentTurn) {
        this.uuid = uuid;
        this.pokemon1 = pokemon1;
        this.pokemon2 = pokemon2;
        this.loggerV2 = loggerV2;
        this.discordHandler = discordHandler;
        this.currentTurn = currentTurn;
    }

    public void applyMove(PokemonState attacker, PokemonState defendingPokemon) {

        boolean canHit = canHitByAilments(attacker);
        if (canHit) {
            switch (attacker.getMove()) {
                case AilmentMove move -> applyAilmentMove(attacker, defendingPokemon);
                case DamageAilmentMove move -> applyDamageAilmentMove(attacker, defendingPokemon);
                case DamageHealMove move -> applyDamageHealMove(attacker, defendingPokemon);
                case DamageLowerMove move -> applyDamageLowerMove(attacker, defendingPokemon);
                case DamageMove move -> applyDamageMove(attacker, defendingPokemon);
                case DamageRaiseMove move -> applyDamageRaiseMove(attacker, defendingPokemon);
                case HealMove move -> applyHealMove(attacker);
                case NetGoodStatsMove move -> applyNetGoodStatsMove(attacker, defendingPokemon);
                case SwaggerMove move -> applySwaggerMove(attacker, defendingPokemon);
            }
        }
    }

    private void applyAilmentMove(PokemonState attacker, PokemonState defender) {
        var ailmentMove = (AilmentMove) attacker.getMove(); /**---------------------- ¡¡ IMPORTANTE !!----------------------------------- */

        float probabilityToHit = Calculator.calculateProbabilityToHit(ailmentMove.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
        boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);

        if (hasHit) {

            Ailments ailmentToApply = ailmentMove.ailment();

            //Logger.saveMoveWithoutDamage(this.uuid, attacker.getName(), ailmentMove.name());

            this.loggerV2.saveMoveWithoutDamage(attacker.getName(), ailmentMove.name());
            this.loggerV2.saveMoveWithoutDamage(this.currentTurn, attacker.getName(), ailmentMove.name());
            // Si ya tiene el ailment, no se puede volver a aplicar
            if (defender.getAilmentsEnum().contains(ailmentToApply)) {
                //Logger.saveRepeatedAilmentApplied(this.uuid, defender.getName(), ailmentToApply.toString());
                this.loggerV2.saveRepeatedAilmentApplied(defender.getName(), ailmentToApply.toString());
                this.loggerV2.saveRepeatedAilmentApplied(this.currentTurn, defender.getName(), ailmentToApply.toString());
            } else {
                Ailment ailment = switch (ailmentToApply) {
                    case BURN -> new Burn(
                            ailmentMove.minTurns() == null ? 99 : ailmentMove.minTurns(),
                            ailmentMove.maxTurns() == null ? 99 : ailmentMove.maxTurns(),
                            1);
                    case CONFUSION -> new Confusion(
                            ailmentMove.minTurns() == null ? 99 : ailmentMove.minTurns(),
                            ailmentMove.maxTurns() == null ? 99 : ailmentMove.maxTurns(),
                            1);
                    case FREEZE -> new Freeze(
                            ailmentMove.minTurns() == null ? 99 : ailmentMove.minTurns(),
                            ailmentMove.maxTurns() == null ? 99 : ailmentMove.maxTurns(),
                            1);
                    case PARALYSIS -> new Paralysis(
                            ailmentMove.minTurns() == null ? 99 : ailmentMove.minTurns(),
                            ailmentMove.maxTurns() == null ? 99 : ailmentMove.maxTurns(),
                            1);
                    case SLEEP -> new Sleep(
                            ailmentMove.minTurns() == null ? 99 : ailmentMove.minTurns(),
                            ailmentMove.maxTurns() == null ? 99 : ailmentMove.maxTurns(),
                            1);
                    case POISON -> new Poison(
                            ailmentMove.minTurns() == null ? 99 : ailmentMove.minTurns(),
                            ailmentMove.maxTurns() == null ? 99 : ailmentMove.maxTurns(),
                            1);
                    case TRAP -> new Trap(
                            ailmentMove.minTurns() == null ? 99 : ailmentMove.minTurns(),
                            ailmentMove.maxTurns() == null ? 99 : ailmentMove.maxTurns(),
                            1);
                    default -> throw new IllegalStateException("Unexpected value: " + ailmentToApply);
                };

                defender.getAilmentsEnum().add(ailmentToApply);
                defender.getAilments().add(ailment);
                defender.getAilmentsV2().put(ailment, 0);
                //Logger.saveAilmentApplied(this.uuid, defender.getName(), ailmentToApply.toString());
                this.loggerV2.saveAilmentApplied(defender.getName(), ailmentToApply.toString());
                this.loggerV2.saveAilmentApplied(this.currentTurn, defender.getName(), ailmentToApply.toString());
            }

        } else {
            //Logger.saveMissedMove(this.uuid, attacker.getName(), ailmentMove.name());
            this.loggerV2.saveMissedMove(attacker.getName(), ailmentMove.name());
            this.loggerV2.saveMissedMove(this.currentTurn, attacker.getName(), ailmentMove.name());
        }
    }

    private void applyDamageAilmentMove(PokemonState attacker, PokemonState defender) {
        var damageAilmentMove = (DamageAilmentMove) attacker.getMove();

        float probabilityToHit = Calculator.calculateProbabilityToHit(damageAilmentMove.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
        boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);

        if (hasHit) {

            boolean hasHitCritical = Calculator.applyDecimalProbability(attacker.getCurrentCrit());
            float typeEffectivenessMultiplier = getEffectivenessMultiplier(damageAilmentMove.type(), defender.getTypes());

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
                    hasHitCritical,
                    attacker.getAilmentsEnum().contains(Ailments.BURN)
            );

            int damageDone = Calculator.calculateDamage(formula);
            int counter = 1;
            do {
                if (counter > damageAilmentMove.minHits() && counter <= damageAilmentMove.maxHits()) {
                    boolean stop = Calculator.applyProbability(50);
                    if (stop) {
                        break;
                    } else {
                        defender.applyDamage(damageDone);
                    }

                } else {
                    defender.applyDamage(damageDone);
                }
                //Logger.saveDamageMoveApplied(this.uuid, attacker.getName(), defender.getName(), damageAilmentMove.name(), damageDone);
                this.loggerV2.saveDamageMoveApplied(attacker.getName(), defender.getName(), damageAilmentMove.name(), damageDone);
                this.loggerV2.saveDamageMoveApplied(this.currentTurn, attacker.getName(), defender.getName(), damageAilmentMove.name(), damageDone);
                counter++;
            } while (counter <= damageAilmentMove.maxHits());

            if (hasHitCritical) {
                //Logger.saveCriticalHit(this.uuid);
                this.loggerV2.saveCriticalHit();
                this.loggerV2.saveCriticalHit(this.currentTurn);
            }

            if (typeEffectivenessMultiplier > 1) {
                this.loggerV2.saveSuperEffectiveMove();
                this.loggerV2.saveSuperEffectiveMove(this.currentTurn);
            } else if (typeEffectivenessMultiplier < 1) {
                this.loggerV2.saveLowEffectiveMove();
                this.loggerV2.saveLowEffectiveMove(this.currentTurn);
            }

            boolean hasFlinched = Calculator.applyProbability(damageAilmentMove.flinchChance());
            if (hasFlinched) {
                defender.setFlinched(true);
                //Logger.savePokemonFlinched(this.uuid, defender.getName());
                this.loggerV2.savePokemonFlinched(defender.getName());
                this.loggerV2.savePokemonFlinched(this.currentTurn, defender.getName());
            }

            boolean hasAilmentApplied= Calculator.applyProbability(damageAilmentMove.ailmentAccuracy());
            if (hasAilmentApplied) {
                Ailments ailmentToApply = damageAilmentMove.ailment();

                if (defender.getAilmentsEnum().contains(ailmentToApply)) {
                    //Logger.saveRepeatedAilmentApplied(this.uuid, defender.getName(), ailmentToApply.name());
                    this.loggerV2.saveRepeatedAilmentApplied(defender.getName(), ailmentToApply.name());
                    this.loggerV2.saveRepeatedAilmentApplied(this.currentTurn, defender.getName(), ailmentToApply.name());
                } else {
                    Ailment ailment = switch (ailmentToApply) {
                        case BURN -> new Burn(
                                damageAilmentMove.minTurns() == null ? 99 : damageAilmentMove.minTurns(),
                                damageAilmentMove.maxTurns() == null ? 99 : damageAilmentMove.maxTurns(),
                                1);
                        case CONFUSION -> new Confusion(
                                damageAilmentMove.minTurns() == null ? 99 : damageAilmentMove.minTurns(),
                                damageAilmentMove.maxTurns() == null ? 99 : damageAilmentMove.maxTurns(),
                                1);
                        case FREEZE -> new Freeze(
                                damageAilmentMove.minTurns() == null ? 99 : damageAilmentMove.minTurns(),
                                damageAilmentMove.maxTurns() == null ? 99 : damageAilmentMove.maxTurns(),
                                1);
                        case PARALYSIS -> new Paralysis(
                                damageAilmentMove.minTurns() == null ? 99 : damageAilmentMove.minTurns(),
                                damageAilmentMove.maxTurns() == null ? 99 : damageAilmentMove.maxTurns(),
                                1);
                        case SLEEP -> new Sleep(
                                damageAilmentMove.minTurns() == null ? 99 : damageAilmentMove.minTurns(),
                                damageAilmentMove.maxTurns() == null ? 99 : damageAilmentMove.maxTurns(),
                                1);
                        case POISON -> new Poison(
                                damageAilmentMove.minTurns() == null ? 99 : damageAilmentMove.minTurns(),
                                damageAilmentMove.maxTurns() == null ? 99 : damageAilmentMove.maxTurns(),
                                1);
                        case TRAP -> new Trap(
                                damageAilmentMove.minTurns() == null ? 99 : damageAilmentMove.minTurns(),
                                damageAilmentMove.maxTurns() == null ? 99 : damageAilmentMove.maxTurns(),
                                1);
                        default -> throw new IllegalStateException("Unexpected value: " + ailmentToApply);
                    };

                    defender.getAilments().add(ailment);
                    defender.getAilmentsEnum().add(ailmentToApply);
                    defender.getAilmentsV2().put(ailment, 0);
                    //Logger.saveAilmentApplied(this.uuid, defender.getName(), ailmentToApply.toString());
                    this.loggerV2.saveAilmentApplied(defender.getName(), ailmentToApply.name());
                    this.loggerV2.saveAilmentApplied(this.currentTurn, defender.getName(), ailmentToApply.name());
                }
            }

        } else {
            //Logger.saveMissedMove(this.uuid, attacker.getName(), damageAilmentMove.name());
            this.loggerV2.saveMissedMove(attacker.getName(), damageAilmentMove.name());
            this.loggerV2.saveMissedMove(this.currentTurn, attacker.getName(), damageAilmentMove.name());
        }
    }

    private void applyDamageHealMove(PokemonState attacker, PokemonState defender) {
        var damageHealMove = (DamageHealMove) attacker.getMove();

        float probabilityToHit = Calculator.calculateProbabilityToHit(damageHealMove.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
        boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);

        if (hasHit) {

            boolean hasHitCritical = Calculator.applyDecimalProbability(attacker.getCurrentCrit());
            float typeEffectivenessMultiplier = getEffectivenessMultiplier(damageHealMove.type(), defender.getTypes());

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
                    hasHitCritical,
                    attacker.getAilmentsEnum().contains(Ailments.BURN)
            );

            int damageDone = Calculator.calculateDamage(formula);
            int counter = 1;
            do {
                if (counter > damageHealMove.minHits() && counter <= damageHealMove.maxHits()) {
                    boolean stop = Calculator.applyProbability(50);
                    if (stop) {
                        break;
                    } else {
                        defender.applyDamage(damageDone);
                    }

                } else {
                    defender.applyDamage(damageDone);
                }
                //Logger.saveDamageMoveApplied(this.uuid, attacker.getName(), defender.getName(), damageHealMove.name(), damageDone);
                this.loggerV2.saveDamageMoveApplied(attacker.getName(), defender.getName(), damageHealMove.name(), damageDone);
                this.loggerV2.saveDamageMoveApplied(this.currentTurn, attacker.getName(), defender.getName(), damageHealMove.name(), damageDone);
                counter++;
            } while (counter <= damageHealMove.maxHits());

            if (hasHitCritical) {
                //Logger.saveCriticalHit(this.uuid);
                this.loggerV2.saveCriticalHit();
                this.loggerV2.saveCriticalHit(this.currentTurn);
            }

            if (typeEffectivenessMultiplier > 1) {
                this.loggerV2.saveSuperEffectiveMove();
                this.loggerV2.saveSuperEffectiveMove(this.currentTurn);
            } else if (typeEffectivenessMultiplier < 1) {
                this.loggerV2.saveLowEffectiveMove();
                this.loggerV2.saveLowEffectiveMove(this.currentTurn);
            }

            int healingAmount = Calculator.calculateHealingBasedOnDamage(damageHealMove.drain(), damageDone);
            attacker.applyHeal(healingAmount);
            //Logger.saveHealingApplied(this.uuid, attacker.getName(), healingAmount);
            this.loggerV2.saveHealingApplied(attacker.getName(), healingAmount);
            this.loggerV2.saveHealingApplied(this.currentTurn, attacker.getName(), healingAmount);

            boolean hasFlinched = Calculator.applyProbability(damageHealMove.flinchChance());
            if (hasFlinched) {
                defender.setFlinched(true);
            }

        } else {
            //Logger.saveMissedMove(this.uuid, attacker.getName(), damageHealMove.name());
            this.loggerV2.saveMissedMove(attacker.getName(), damageHealMove.name());
            this.loggerV2.saveMissedMove(this.currentTurn, attacker.getName(), damageHealMove.name());
        }
    }

    private void applyDamageLowerMove(PokemonState attacker, PokemonState defender) {
        var damageLowerMode = (DamageLowerMove) attacker.getMove();

        float probabilityToHit = Calculator.calculateProbabilityToHit(damageLowerMode.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
        boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);

        if (hasHit) {

            boolean hasHitCritical = Calculator.applyDecimalProbability(attacker.getCurrentCrit());
            float typeEffectivenessMultiplier = getEffectivenessMultiplier(damageLowerMode.type(), defender.getTypes());

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
                    hasHitCritical,
                    attacker.getAilmentsEnum().contains(Ailments.BURN)
            );

            int damageDone = Calculator.calculateDamage(formula);
            int counter = 1;
            do {
                if (counter > damageLowerMode.minHits() && counter <= damageLowerMode.maxHits()) {
                    boolean stop = Calculator.applyProbability(50);
                    if (stop) {
                        break;
                    } else {
                        defender.applyDamage(damageDone);
                    }

                } else {
                    defender.applyDamage(damageDone);
                }
                //Logger.saveDamageMoveApplied(this.uuid, attacker.getName(), defender.getName(), damageLowerMode.name(), damageDone);
                this.loggerV2.saveDamageMoveApplied(attacker.getName(), defender.getName(), damageLowerMode.name(), damageDone);
                this.loggerV2.saveDamageMoveApplied(this.currentTurn, attacker.getName(), defender.getName(), damageLowerMode.name(), damageDone);
                counter++;
            } while (counter <= damageLowerMode.maxHits());

            if (hasHitCritical) {
                //Logger.saveCriticalHit(this.uuid);
                this.loggerV2.saveCriticalHit();
                this.loggerV2.saveCriticalHit(this.currentTurn);
            }

            if (typeEffectivenessMultiplier > 1) {
                this.loggerV2.saveSuperEffectiveMove();
                this.loggerV2.saveSuperEffectiveMove(this.currentTurn);
            } else if (typeEffectivenessMultiplier < 1) {
                this.loggerV2.saveLowEffectiveMove();
                this.loggerV2.saveLowEffectiveMove(this.currentTurn);
            }

            boolean hasFlinched = Calculator.applyProbability(damageLowerMode.flinchChance());
            if (hasFlinched) {
                defender.setFlinched(true);
            }

            boolean hasDefenderStatsChanged = Calculator.applyProbability(damageLowerMode.statChance());
            if (hasDefenderStatsChanged) {
                for (Stats stat : damageLowerMode.statsNames()) {
                    defender.updateStatModifier(stat, damageLowerMode.statChange());
                    defender.applyModifiers();
                    //Logger.saveStatChangeMoveApplied(this.uuid, defender.getName(), stat.name(), damageLowerMode.statChange() > 0);
                    this.loggerV2.saveStatChangeMoveApplied(defender.getName(), stat.name(), damageLowerMode.statChange() > 0);
                    this.loggerV2.saveStatChangeMoveApplied(this.currentTurn, defender.getName(), stat, damageLowerMode.statChange() > 0);
                }
            }

        } else {
            //Logger.saveMissedMove(this.uuid, attacker.getName(), damageLowerMode.name());
            this.loggerV2.saveMissedMove(attacker.getName(), damageLowerMode.name());
            this.loggerV2.saveMissedMove(this.currentTurn, attacker.getName(), damageLowerMode.name());
        }
    }

    private void applyDamageMove(PokemonState attacker, PokemonState defender) {
        var damageMove = (DamageMove) attacker.getMove();

        float probabilityToHit = Calculator.calculateProbabilityToHit(damageMove.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
        boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);

        if (hasHit) {

            boolean hasHitCritical = Calculator.applyDecimalProbability(attacker.getCurrentCrit());
            float typeEffectivenessMultiplier = getEffectivenessMultiplier(damageMove.type(), defender.getTypes());

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
                    hasHitCritical,
                    attacker.getAilmentsEnum().contains(Ailments.BURN)
            );

            int damageDone = Calculator.calculateDamage(formula);
            int counter = 1;
            do {
                if (counter > damageMove.minHits() && counter <= damageMove.maxHits()) {
                    boolean stop = Calculator.applyProbability(50);
                    if (stop) {
                        break;
                    } else {
                        defender.applyDamage(damageDone);
                    }

                } else {
                    defender.applyDamage(damageDone);
                }
                //Logger.saveDamageMoveApplied(this.uuid, attacker.getName(), defender.getName(), damageMove.name(), damageDone);
                this.loggerV2.saveDamageMoveApplied(attacker.getName(), defender.getName(), damageMove.name(), damageDone);
                this.loggerV2.saveDamageMoveApplied(this.currentTurn, attacker.getName(), defender.getName(), damageMove.name(), damageDone);
                counter++;
            } while (counter <= damageMove.maxHits());

            if (hasHitCritical) {
                //Logger.saveCriticalHit(this.uuid);
                this.loggerV2.saveCriticalHit();
                this.loggerV2.saveCriticalHit(this.currentTurn);
            }

            if (typeEffectivenessMultiplier > 1) {
                this.loggerV2.saveSuperEffectiveMove();
                this.loggerV2.saveSuperEffectiveMove(this.currentTurn);
            } else if (typeEffectivenessMultiplier < 1) {
                this.loggerV2.saveLowEffectiveMove();
                this.loggerV2.saveLowEffectiveMove(this.currentTurn);
            }

            boolean hasFlinched = Calculator.applyProbability(damageMove.flinchChance());
            if (hasFlinched) {
                defender.setFlinched(true);
            }

        } else {
            //Logger.saveMissedMove(this.uuid, attacker.getName(), damageMove.name());
            this.loggerV2.saveMissedMove(attacker.getName(), damageMove.name());
            this.loggerV2.saveMissedMove(this.currentTurn, attacker.getName(), damageMove.name());
        }

    }

    private void applyDamageRaiseMove(PokemonState attacker, PokemonState defender) {
        var damageRaiseMove = (DamageRaiseMove) attacker.getMove();

        float probabilityToHit = Calculator.calculateProbabilityToHit(damageRaiseMove.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
        boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);

        if (hasHit) {

            boolean hasHitCritical = Calculator.applyDecimalProbability(attacker.getCurrentCrit());
            float typeEffectivenessMultiplier = getEffectivenessMultiplier(damageRaiseMove.type(), defender.getTypes());

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
                    hasHitCritical,
                    attacker.getAilmentsEnum().contains(Ailments.BURN)
            );

            int damageDone = Calculator.calculateDamage(formula);
            int counter = 1;
            do {
                if (counter > damageRaiseMove.minHits() && counter <= damageRaiseMove.maxHits()) {
                    boolean stop = Calculator.applyProbability(50);
                    if (stop) {
                        break;
                    } else {
                        defender.applyDamage(damageDone);
                    }

                } else {
                    defender.applyDamage(damageDone);
                }
                //Logger.saveDamageMoveApplied(this.uuid, attacker.getName(), defender.getName(), damageRaiseMove.name(), damageDone);
                this.loggerV2.saveDamageMoveApplied(attacker.getName(), defender.getName(), damageRaiseMove.name(), damageDone);
                this.loggerV2.saveDamageMoveApplied(this.currentTurn, attacker.getName(), defender.getName(), damageRaiseMove.name(), damageDone);
                counter++;
            } while (counter <= damageRaiseMove.maxHits());

            if (hasHitCritical) {
                //Logger.saveCriticalHit(this.uuid);
                this.loggerV2.saveCriticalHit();
                this.loggerV2.saveCriticalHit(this.currentTurn);
            }

            if (typeEffectivenessMultiplier > 1) {
                this.loggerV2.saveSuperEffectiveMove();
                this.loggerV2.saveSuperEffectiveMove(this.currentTurn);
            } else if (typeEffectivenessMultiplier < 1) {
                this.loggerV2.saveLowEffectiveMove();
                this.loggerV2.saveLowEffectiveMove(this.currentTurn);
            }

            boolean hasFlinched = Calculator.applyProbability(damageRaiseMove.flinchChance());
            if (hasFlinched) {
                defender.setFlinched(true);
            }

            boolean hasAttackerStatsChanged = Calculator.applyProbability(damageRaiseMove.statChance());
            if (hasAttackerStatsChanged) {
                for (Stats stat : damageRaiseMove.statsNames()) {
                    attacker.updateStatModifier(stat, damageRaiseMove.statChange());
                    attacker.applyModifiers();
                    this.loggerV2.saveStatChangeMoveApplied(defender.getName(), stat.name(), damageRaiseMove.statChange() > 0);
                    this.loggerV2.saveStatChangeMoveApplied(this.currentTurn, defender.getName(), stat, damageRaiseMove.statChange() > 0);
                }
            }

        } else {
            //Logger.saveMissedMove(this.uuid, attacker.getName(), damageRaiseMove.name());
            this.loggerV2.saveMissedMove(attacker.getName(), damageRaiseMove.name());
            this.loggerV2.saveMissedMove(this.currentTurn, attacker.getName(), damageRaiseMove.name());
        }

    }

    private void applyNetGoodStatsMove(PokemonState attacker, PokemonState defender) {
        var netGoodStatsMove = (NetGoodStatsMove) attacker.getMove();

        float probabilityToHit = Calculator.calculateProbabilityToHit(netGoodStatsMove.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
        boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);
        if (hasHit) {
            //Logger.saveMoveWithoutDamage(this.uuid, attacker.getName(), netGoodStatsMove.name());
            this.loggerV2.saveMoveWithoutDamage(attacker.getName(), netGoodStatsMove.name());
            this.loggerV2.saveMoveWithoutDamage(this.currentTurn, attacker.getName(), netGoodStatsMove.name());
            // Si el target es USER el movimiento es sobre el.
            // Si no, es sobre el pokemon ENEMIGO.
            if (netGoodStatsMove.target() == Targets.USER || netGoodStatsMove.target() == Targets.USER_AND_ALLIES) {
                for (Stats stat : netGoodStatsMove.statsNames()) {
                    attacker.updateStatModifier(stat, netGoodStatsMove.statChange());
                    attacker.applyModifiers();
                    //Logger.saveStatChangeMoveApplied(this.uuid, attacker.getName(), stat.name(), netGoodStatsMove.statChange() > 0);
                    this.loggerV2.saveStatChangeMoveApplied(attacker.getName(), stat.name(), netGoodStatsMove.statChange() > 0);
                    this.loggerV2.saveStatChangeMoveApplied(this.currentTurn, attacker.getName(), stat, netGoodStatsMove.statChange() > 0);
                }

            } else {
                for (Stats stat : netGoodStatsMove.statsNames()) {
                    defender.updateStatModifier(stat, netGoodStatsMove.statChange());
                    defender.applyModifiers();
                    //Logger.saveStatChangeMoveApplied(this.uuid, defender.getName(), stat.name(), netGoodStatsMove.statChange() > 0);
                    this.loggerV2.saveStatChangeMoveApplied(attacker.getName(), stat.name(), netGoodStatsMove.statChange() > 0);
                    this.loggerV2.saveStatChangeMoveApplied(this.currentTurn, attacker.getName(), stat, netGoodStatsMove.statChange() > 0);
                }
            }

        } else {
            //Logger.saveMissedMove(this.uuid, attacker.getName(), netGoodStatsMove.name());
            this.loggerV2.saveMissedMove(attacker.getName(), netGoodStatsMove.name());
            this.loggerV2.saveMissedMove(this.currentTurn, attacker.getName(), netGoodStatsMove.name());
        }
    }

    private void applyHealMove(PokemonState attacker) {
        var healMove = (HealMove) attacker.getMove();

        float probabilityToHit = Calculator.calculateProbabilityToHitWithoutEvasion(healMove.accuracy(), attacker.getCurrentAccuracy());
        boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);
        if (hasHit) {
            //Logger.saveMoveWithoutDamage(this.uuid, attacker.getName(), healMove.name());
            this.loggerV2.saveMoveWithoutDamage(attacker.getName(), healMove.name());
            this.loggerV2.saveMoveWithoutDamage(this.currentTurn, attacker.getName(), healMove.name());
            int healing = Calculator.calculateHealingBasedOnMaxHp(healMove.healing(), attacker.getBaseHp());
            attacker.applyHeal(healing);
            //Logger.saveHealingApplied(this.uuid, attacker.getName(), healing);
            this.loggerV2.saveHealingApplied(attacker.getName(), healing);
            this.loggerV2.saveHealingApplied(this.currentTurn, attacker.getName(), healing);
        } else {
            //Logger.saveMissedMove(this.uuid, attacker.getName(), healMove.name());
            this.loggerV2.saveMissedMove(attacker.getName(), healMove.name());
            this.loggerV2.saveMissedMove(this.currentTurn, attacker.getName(), healMove.name());
        }

    }

    private void applySwaggerMove(PokemonState attacker, PokemonState defender) {
        var swaggerMove = (SwaggerMove) attacker.getMove();

        float probabilityToHit = Calculator.calculateProbabilityToHit(swaggerMove.accuracy(), attacker.getCurrentAccuracy(), defender.getCurrentEvasion());
        boolean hasHit = Calculator.applyDecimalProbability(probabilityToHit);

        if (hasHit) {
            //Logger.saveMoveWithoutDamage(this.uuid, attacker.getName(), swaggerMove.name());
            this.loggerV2.saveMoveWithoutDamage(attacker.getName(), swaggerMove.name());
            this.loggerV2.saveMoveWithoutDamage(this.currentTurn, attacker.getName(), swaggerMove.name());
            for (Stats stat : swaggerMove.statsNames()) {
                defender.updateStatModifier(stat, swaggerMove.statChange());
                defender.applyModifiers();
                //Logger.saveStatChangeMoveApplied(this.uuid, defender.getName(), stat.name(), swaggerMove.statChange() > 0);
                this.loggerV2.saveStatChangeMoveApplied(defender.getName(), stat.name(), swaggerMove.statChange() > 0);
                this.loggerV2.saveStatChangeMoveApplied(this.currentTurn, defender.getName(), stat, swaggerMove.statChange() > 0);
            }

            Ailments ailmentToApply = swaggerMove.ailment();

            if (defender.getAilmentsEnum().contains(ailmentToApply)) {
                //Logger.saveRepeatedAilmentApplied(this.uuid, defender.getName(), ailmentToApply.toString());
                this.loggerV2.saveRepeatedAilmentApplied(defender.getName(), ailmentToApply.name());
                this.loggerV2.saveRepeatedAilmentApplied(this.currentTurn, defender.getName(), ailmentToApply.name());
            } else {
                Ailment ailment = switch (ailmentToApply) {
                    case BURN -> new Burn(
                            swaggerMove.minTurns() == null ? 99 : swaggerMove.minTurns(),
                            swaggerMove.maxTurns() == null ? 99 : swaggerMove.maxTurns(),
                            1);
                    case CONFUSION -> new Confusion(
                            swaggerMove.minTurns() == null ? 99 : swaggerMove.minTurns(),
                            swaggerMove.maxTurns() == null ? 99 : swaggerMove.maxTurns(),
                            1);
                    case FREEZE -> new Freeze(
                            swaggerMove.minTurns() == null ? 99 : swaggerMove.minTurns(),
                            swaggerMove.maxTurns() == null ? 99 : swaggerMove.maxTurns(),
                            1);
                    case PARALYSIS -> new Paralysis(
                            swaggerMove.minTurns() == null ? 99 : swaggerMove.minTurns(),
                            swaggerMove.maxTurns() == null ? 99 : swaggerMove.maxTurns(),
                            1);
                    case SLEEP -> new Sleep(
                            swaggerMove.minTurns() == null ? 99 : swaggerMove.minTurns(),
                            swaggerMove.maxTurns() == null ? 99 : swaggerMove.maxTurns(),
                            1);
                    case POISON -> new Poison(
                            swaggerMove.minTurns() == null ? 99 : swaggerMove.minTurns(),
                            swaggerMove.maxTurns() == null ? 99 : swaggerMove.maxTurns(),
                            1);
                    case TRAP -> new Trap(
                            swaggerMove.minTurns() == null ? 99 : swaggerMove.minTurns(),
                            swaggerMove.maxTurns() == null ? 99 : swaggerMove.maxTurns(),
                            1);
                    default -> throw new IllegalStateException("Unexpected value: " + ailmentToApply);
                };

                defender.getAilments().add(ailment);
                defender.getAilmentsEnum().add(ailmentToApply);
                defender.getAilmentsV2().put(ailment, 0);
                //Logger.saveAilmentApplied(this.uuid, defender.getName(), ailmentToApply.toString());
                this.loggerV2.saveAilmentApplied(defender.getName(), ailmentToApply.name());
                this.loggerV2.saveAilmentApplied(this.currentTurn, defender.getName(), ailmentToApply.name());
            }

        } else {
            //Logger.saveMissedMove(this.uuid, attacker.getName(), swaggerMove.name());
            this.loggerV2.saveMissedMove(attacker.getName(), swaggerMove.name());
            this.loggerV2.saveMissedMove(this.currentTurn, attacker.getName(), swaggerMove.name());
        }
    }


    /** --UTILITY-- */
    private boolean canHitByAilments(PokemonState attacker) {
        if (attacker.isFlinched()) {
            //Logger.saveUnableToMove(this.uuid, attacker.getName(), "flinched");
            this.loggerV2.saveUnableToMove(attacker.getName(), "flinched");
            this.loggerV2.saveUnableToMove(this.currentTurn, attacker.getName(), "flinched");
            return false;
        }

        if (attacker.getAilmentsEnum().contains(Ailments.FREEZE)) {
            //Logger.saveUnableToMove(this.uuid, attacker.getName(), "freezed");
            this.loggerV2.saveUnableToMove(attacker.getName(), "freezed");
            this.loggerV2.saveUnableToMove(this.currentTurn, attacker.getName(), "freezed");
            return false;
        }

        if (attacker.getAilmentsEnum().contains(Ailments.SLEEP)) {
            //Logger.saveUnableToMove(this.uuid, attacker.getName(), "slept");
            this.loggerV2.saveUnableToMove(attacker.getName(), "slept");
            this.loggerV2.saveUnableToMove(this.currentTurn, attacker.getName(), "slept");
            return false;
        }

        if (attacker.getAilmentsEnum().contains(Ailments.CONFUSION)) {
            boolean canHit = Calculator.applyProbability(67);
            if (!canHit) {
                applyConfusionDamage(attacker);
                return false;
            }
        }

        if (attacker.getAilmentsEnum().contains(Ailments.PARALYSIS)) {
            boolean canHit = Calculator.applyProbability(75);
            if (!canHit) {
                //Logger.saveUnableToMove(this.uuid, attacker.getName(), "paralyzed");
                this.loggerV2.saveUnableToMove(attacker.getName(), "paralyzed");
                this.loggerV2.saveUnableToMove(this.currentTurn, attacker.getName(), "paralyzed");
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

    private void applyConfusionDamage(PokemonState attacker) {
        int damage = Calculator.calculatePercentageDamage(12.5f, attacker.getBaseHp());
        attacker.applyDamage(damage);
        //Logger.saveConfuseDamage(this.uuid, attacker.getName(), damage);
        this.loggerV2.saveConfuseDamage(attacker.getName(), damage);
        this.loggerV2.saveConfuseDamage(this.currentTurn, attacker.getName(), damage);
    }

    public void applyEndOfTurnDamage(PokemonState pokemon) {
        for (Ailments ailment : pokemon.getAilmentsEnum()) {
            if (ailment == Ailments.BURN) {
                int damage = Calculator.calculatePercentageDamage(6.25f, pokemon.getBaseHp());
                pokemon.applyDamage(damage);
                //Logger.saveAilmentDamageApplied(this.uuid, pokemon.getName(), "burned", damage);
                this.loggerV2.saveAilmentDamageApplied(pokemon.getName(), "burned", damage);
                this.loggerV2.saveAilmentDamageApplied(this.currentTurn, pokemon.getName(), "burned", damage);
            }

            if (ailment == Ailments.POISON) {
                int damage = Calculator.calculatePercentageDamage(12.5f, pokemon.getBaseHp());
                pokemon.applyDamage(damage);
                //Logger.saveAilmentDamageApplied(this.uuid, pokemon.getName(), "poisoned", damage);
                this.loggerV2.saveAilmentDamageApplied(pokemon.getName(), "poisoned", damage);
                this.loggerV2.saveAilmentDamageApplied(this.currentTurn, pokemon.getName(), "poisoned", damage);
            }

            if (ailment == Ailments.TRAP) {
                int damage = Calculator.calculatePercentageDamage(6.25f, pokemon.getBaseHp());
                pokemon.applyDamage(damage);
                //Logger.saveAilmentDamageApplied(this.uuid, pokemon.getName(), "trapped", damage);
                this.loggerV2.saveAilmentDamageApplied(pokemon.getName(), "trapped", damage);
                this.loggerV2.saveAilmentDamageApplied(this.currentTurn, pokemon.getName(), "trapped", damage);
            }
        }
    }

    public void updateAilmentsTurns(PokemonState pokemon1) {
        for (var ailment : pokemon1.getAilmentsV2().entrySet()) {
            switch (ailment.getKey().getClass().getSimpleName()) {
                case "Confusion":
                    int currentConfusionTurn = ailment.getValue();

                    if (currentConfusionTurn >= 2 && currentConfusionTurn <= 4) {
                        boolean hasRecoveredFromConfusion = Calculator.applyProbability(50);

                        if (hasRecoveredFromConfusion) {
                            pokemon1.getAilmentsV2().remove(ailment.getKey());
                            pokemon1.getAilmentsEnum().remove(Ailments.CONFUSION);
                            //Logger.saveAilmentRemoved(this.uuid, pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                            this.loggerV2.saveAilmentRemoved(pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                            this.loggerV2.saveAilmentRemoved(this.currentTurn, pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                        } else {
                            ailment.setValue(currentConfusionTurn + 1);
                        }

                    } else if (currentConfusionTurn > 4) {
                        pokemon1.getAilmentsV2().remove(ailment.getKey());
                        //Logger.saveAilmentRemoved(this.uuid, pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                        this.loggerV2.saveAilmentRemoved(pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                        this.loggerV2.saveAilmentRemoved(this.currentTurn, pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                    } else {
                        ailment.setValue(currentConfusionTurn + 1);
                    }

                    break;


                case "Freeze":
                    boolean hasRecoveredFromFreeze = Calculator.applyProbability(20);
                    if (hasRecoveredFromFreeze) {
                        pokemon1.getAilmentsV2().remove(ailment.getKey());
                        pokemon1.getAilmentsEnum().remove(Ailments.FREEZE);
                        //Logger.saveAilmentRemoved(this.uuid, pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                        this.loggerV2.saveAilmentRemoved(pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                        this.loggerV2.saveAilmentRemoved(this.currentTurn, pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                    }

                    break;


                case "Sleep":
                    int currentSleepTurn = ailment.getValue();

                    if (currentSleepTurn >= 1 && currentSleepTurn <= 3) {
                        boolean hasRecoveredFromSleep = Calculator.applyProbability(50);

                        if (hasRecoveredFromSleep) {
                            pokemon1.getAilmentsV2().remove(ailment.getKey());
                            pokemon1.getAilmentsEnum().remove(Ailments.SLEEP);
                            //Logger.saveAilmentRemoved(this.uuid, pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                            this.loggerV2.saveAilmentRemoved(pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                            this.loggerV2.saveAilmentRemoved(this.currentTurn, pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                        } else {
                            ailment.setValue(currentSleepTurn + 1);
                        }

                    } else if (currentSleepTurn > 4) {
                        pokemon1.getAilmentsV2().remove(ailment.getKey());
                        //Logger.saveAilmentRemoved(this.uuid, pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                        this.loggerV2.saveAilmentRemoved(pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                        this.loggerV2.saveAilmentRemoved(this.currentTurn, pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                    } else {
                        ailment.setValue(currentSleepTurn + 1);
                    }
                    break;

                case "Trap":
                    int currentTrapTurn = ailment.getValue();

                    if (currentTrapTurn >= 2 && currentTrapTurn <= 5) {
                        boolean hasRecoveredFromTrap = Calculator.applyProbability(50);

                        if (hasRecoveredFromTrap) {
                            pokemon1.getAilmentsV2().remove(ailment.getKey());
                            pokemon1.getAilmentsEnum().remove(Ailments.TRAP);
                            //Logger.saveAilmentRemoved(this.uuid, pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                            this.loggerV2.saveAilmentRemoved(pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                            this.loggerV2.saveAilmentRemoved(this.currentTurn, pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                        } else {
                            ailment.setValue(currentTrapTurn + 1);
                        }

                    } else if (currentTrapTurn > 5) {
                        pokemon1.getAilmentsV2().remove(ailment.getKey());
                        //Logger.saveAilmentRemoved(this.uuid, pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                        this.loggerV2.saveAilmentRemoved(pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                        this.loggerV2.saveAilmentRemoved(this.currentTurn, pokemon1.getName(), ailment.getKey().getClass().getSimpleName());
                    } else {
                        ailment.setValue(currentTrapTurn + 1);
                    }

                    break;
            }
        }
    }

    private float getEffectivenessMultiplier(Types moveType, List<Types> defenderPokemonTypes) {
        List<Float> multipliers = new ArrayList<>();
        for (Types defenderPokemonType : defenderPokemonTypes) {
            float multiplier = TypeEffectiveness.getMultiplier(moveType, defenderPokemonType);
            multipliers.add(multiplier);
        }
        float result = 1f;
        for (float multiplier: multipliers) {
            result *= multiplier;
        }

        return result;
    }

    public void cleanFlinchStatus(PokemonState pokemon1, PokemonState pokemon2) {
        // TODO
        pokemon1.setFlinched(false);
        pokemon2.setFlinched(false);
    }

}
