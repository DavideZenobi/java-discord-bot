package org.example.pokemon;

import org.example.pokemon.ailments.Ailment;
import org.example.pokemon.enums.Ailments;
import org.example.pokemon.enums.Stats;
import org.example.pokemon.enums.Types;
import org.example.pokemon.moves.MoveV2;

import java.util.ArrayList;
import java.util.List;

/**
 *  Accuracy y evasion son valores porcentuales (%)
 */
public class PokemonState {
    
    private String name;
    private List<Types> types;
    private int baseHp; 
    private int currentHp;
    private int baseAttack;
    private int currentAttack;
    private int attackModifier; // from -6 to 6. start at 0
    private int baseDefense;
    private int currentDefense;
    private int defenseModifier;
    private int baseSpecialAttack;
    private int currentSpecialAttack;
    private int specialAttackModifier;
    private int baseSpecialDefense;
    private int currentSpecialDefense;
    private int specialDefenseModifier;
    private int baseSpeed;
    private int currentSpeed;
    private int speedModifier;
    private int baseAccuracy;
    private int currentAccuracy;
    private int accuracyModifier;
    private int baseEvasion;
    private int currentEvasion;
    private int evasionModifier;
    private float baseCrit;
    private float currentCrit;
    private int critModifier;
    private int level;
    private List<Ailment> ailments;
    private List<Ailments> ailmentsEnum;
    private MoveV2 move;

    private boolean flinched;

    public PokemonState(Pokemon pokemon) {
        this.name = pokemon.getName();
        this.types = pokemon.getTypes();
        this.baseHp = pokemon.getHp();
        this.currentHp = pokemon.getHp();
        this.baseAttack = pokemon.getAttack();
        this.currentAttack = pokemon.getAttack();
        this.baseDefense = pokemon.getDefense();
        this.currentDefense = pokemon.getDefense();
        this.baseSpecialAttack = pokemon.getSpecialAttack();
        this.currentSpecialAttack = pokemon.getSpecialAttack();
        this.baseSpecialDefense = pokemon.getSpecialDefense();
        this.currentSpecialDefense = pokemon.getSpecialDefense();
        this.baseSpeed = pokemon.getSpeed();
        this.currentSpeed = pokemon.getSpeed();
        this.baseCrit = 4.17f;
        this.currentCrit = 4.17f;
        this.attackModifier = 0;
        this.defenseModifier = 0;
        this.specialAttackModifier = 0;
        this.specialDefenseModifier = 0;
        this.speedModifier = 0;
        this.baseAccuracy = 100;
        this.accuracyModifier = 0;
        this.baseEvasion = 0;
        this.evasionModifier = 0;
        this.critModifier = 0;
        this.level = 1;
        this.ailments = new ArrayList<>();
        this.ailmentsEnum = new ArrayList<>();
        this.flinched = false;
    }

    public void applyModifiers() {
        int attackModifierPercentage = StageModifier.basicStatsStages(this.attackModifier);
        setCurrentAttack(Math.round((float) this.baseAttack * attackModifierPercentage / 100));
        int defenseModifierPercentage = StageModifier.basicStatsStages(this.defenseModifier);
        setCurrentDefense(Math.round((float) this.baseDefense * defenseModifierPercentage / 100));
        int specialAttackModifierPercentage = StageModifier.basicStatsStages(this.specialAttackModifier);
        setCurrentSpecialAttack(Math.round((float) this.baseSpecialAttack * specialAttackModifierPercentage / 100));
        int specialDefenseModifierPercentage = StageModifier.basicStatsStages(this.specialDefenseModifier);
        setCurrentSpecialDefense(Math.round((float) this.baseSpecialDefense * specialDefenseModifierPercentage / 100));
        int speedModifierPercentage = StageModifier.basicStatsStages(this.speedModifier);
        setCurrentSpeed(Math.round((float) this.baseSpeed * speedModifierPercentage / 100));
        int accuracyModifierPercentage = StageModifier.accuracyStages(this.accuracyModifier);
        setCurrentAccuracy(Math.round((float) this.baseAccuracy * accuracyModifierPercentage / 100));
        int evasionModifierPercentage = StageModifier.evasionStages(this.evasionModifier);
        setCurrentEvasion(Math.round((float) evasionModifierPercentage));
        setCurrentCrit(StageModifier.critStages(this.critModifier));
    }

    public void applyDamage(int damage) {
        if (getCurrentHp() - damage < 0) {
            setCurrentHp(0);
        } else {
            setCurrentHp(getCurrentHp() - damage);
        }
    }

    public void applyHeal(int amountToHeal) {
        if (getCurrentHp() + amountToHeal > getBaseHp()) {
            setCurrentHp(getBaseHp());
        } else if (getCurrentHp() + amountToHeal < 0) {
            setCurrentHp(0);
        } else {
            setCurrentHp(getCurrentHp() + amountToHeal);
        }
    }

    public void updateStatModifier(Stats stat, int statChange) {
        switch (stat) {
            case ATTACK -> {
                if (getAttackModifier() + statChange > 6) {
                    setAttackModifier(6);
                } else if (getAttackModifier() + statChange < -6) {
                    setAttackModifier(-6);
                } else {
                    setAttackModifier(getAttackModifier() + statChange);
                }
            }
            case DEFENSE -> {
                if (getDefenseModifier() + statChange > 6) {
                    setDefenseModifier(6);
                } else if (getDefenseModifier() + statChange < -6) {
                    setDefenseModifier(-6);
                } else {
                    setDefenseModifier(getDefenseModifier() + statChange);
                }
            }
            case SPECIAL_ATTACK -> {
                if (getSpecialAttackModifier() + statChange > 6) {
                    setSpecialAttackModifier(6);
                } else if (getSpecialAttackModifier() + statChange < -6) {
                    setSpecialAttackModifier(-6);
                } else {
                    setSpecialAttackModifier(getSpecialAttackModifier() + statChange);
                }
            }
            case SPECIAL_DEFENSE -> {
                if (getSpecialDefenseModifier() + statChange > 6) {
                    setSpecialDefenseModifier(6);
                } else if (getSpecialDefenseModifier() + statChange < -6) {
                    setSpecialDefenseModifier(-6);
                } else {
                    setSpecialDefenseModifier(getSpecialDefenseModifier() + statChange);
                }
            }
            case SPEED -> {
                if (getSpeedModifier() + statChange > 6) {
                    setSpeedModifier(6);
                } else if (getSpeedModifier() + statChange < -6) {
                    setSpeedModifier(-6);
                } else {
                    setSpeedModifier(getSpeedModifier() + statChange);
                }
            }
            case EVASION -> {
                if (getEvasionModifier() + statChange > 6) {
                    setEvasionModifier(6);
                } else if (getEvasionModifier() + statChange < -6) {
                    setEvasionModifier(-6);
                } else {
                    setEvasionModifier(getEvasionModifier() + statChange);
                }
            }
            case ACCURACY -> {
                if (getAccuracyModifier() + statChange > 6) {
                    setAccuracyModifier(6);
                } else if (getAccuracyModifier() + statChange < -6) {
                    setAccuracyModifier(-6);
                } else {
                    setAccuracyModifier(getAccuracyModifier() + statChange);
                }
            }
        };
    }

    public int getCurrentHp() {
        return currentHp;
    }


    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public int getCurrentAttack() {
        return currentAttack;
    }

    public void setCurrentAttack(int currentAttack) {
        this.currentAttack = currentAttack;
    }

    public int getCurrentDefense() {
        return currentDefense;
    }

    public void setCurrentDefense(int currentDefense) {
        this.currentDefense = currentDefense;
    }

    public int getCurrentSpecialAttack() {
        return currentSpecialAttack;
    }

    public void setCurrentSpecialAttack(int currentSpecialAttack) {
        this.currentSpecialAttack = currentSpecialAttack;
    }

    public int getCurrentSpecialDefense() {
        return currentSpecialDefense;
    }

    public void setCurrentSpecialDefense(int currentSpecialDefense) {
        this.currentSpecialDefense = currentSpecialDefense;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public List<Ailment> getEffects() {
        return ailments;
    }

    public void setEffects(List<Ailment> ailments) {
        this.ailments = ailments;
    }

    public MoveV2 getMove() {
        return move;
    }

    public void setMove(MoveV2 move) {
        this.move = move;
    }

    public int getAttackModifier() {
        return attackModifier;
    }

    public void setAttackModifier(int attackModifier) {
        this.attackModifier = attackModifier;
    }

    public int getDefenseModifier() {
        return defenseModifier;
    }

    public void setDefenseModifier(int defenseModifier) {
        this.defenseModifier = defenseModifier;
    }

    public int getSpecialAttackModifier() {
        return specialAttackModifier;
    }

    public void setSpecialAttackModifier(int specialAttackModifier) {
        this.specialAttackModifier = specialAttackModifier;
    }

    public int getSpecialDefenseModifier() {
        return specialDefenseModifier;
    }

    public void setSpecialDefenseModifier(int specialDefenseModifier) {
        this.specialDefenseModifier = specialDefenseModifier;
    }

    public int getSpeedModifier() {
        return speedModifier;
    }

    public void setSpeedModifier(int speedModifier) {
        this.speedModifier = speedModifier;
    }

    public int getAccuracyModifier() {
        return accuracyModifier;
    }

    public void setAccuracyModifier(int accuracyModifier) {
        this.accuracyModifier = accuracyModifier;
    }

    public int getEvasionModifier() {
        return evasionModifier;
    }

    public void setEvasionModifier(int evasionModifier) {
        this.evasionModifier = evasionModifier;
    }

    public List<Types> getTypes() {
        return types;
    }

    public void setTypes(List<Types> types) {
        this.types = types;
    }

    public int getCurrentAccuracy() {
        return currentAccuracy;
    }

    public void setCurrentAccuracy(int currentAccuracy) {
        this.currentAccuracy = currentAccuracy;
    }

    public int getCurrentEvasion() {
        return currentEvasion;
    }

    public void setCurrentEvasion(int currentEvasion) {
        this.currentEvasion = currentEvasion;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getBaseCrit() {
        return baseCrit;
    }

    public void setBaseCrit(float baseCrit) {
        this.baseCrit = baseCrit;
    }

    public float getCurrentCrit() {
        return currentCrit;
    }

    public void setCurrentCrit(float currentCrit) {
        this.currentCrit = currentCrit;
    }

    public int getCritModifier() {
        return critModifier;
    }

    public void setCritModifier(int critModifier) {
        this.critModifier = critModifier;
    }

    public boolean isFlinched() {
        return flinched;
    }

    public void setFlinched(boolean flinched) {
        this.flinched = flinched;
    }

    public List<Ailment> getAilments() {
        return ailments;
    }

    public void setAilments(List<Ailment> ailments) {
        this.ailments = ailments;
    }

    public List<Ailments> getAilmentsEnum() {
        return ailmentsEnum;
    }

    public void setAilmentsEnum(List<Ailments> ailmentsEnum) {
        this.ailmentsEnum = ailmentsEnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public void setBaseHp(int baseHp) {
        this.baseHp = baseHp;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public void setBaseDefense(int baseDefense) {
        this.baseDefense = baseDefense;
    }

    public int getBaseSpecialAttack() {
        return baseSpecialAttack;
    }

    public void setBaseSpecialAttack(int baseSpecialAttack) {
        this.baseSpecialAttack = baseSpecialAttack;
    }

    public int getBaseSpecialDefense() {
        return baseSpecialDefense;
    }

    public void setBaseSpecialDefense(int baseSpecialDefense) {
        this.baseSpecialDefense = baseSpecialDefense;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(int baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public int getBaseAccuracy() {
        return baseAccuracy;
    }

    public void setBaseAccuracy(int baseAccuracy) {
        this.baseAccuracy = baseAccuracy;
    }

    public int getBaseEvasion() {
        return baseEvasion;
    }

    public void setBaseEvasion(int baseEvasion) {
        this.baseEvasion = baseEvasion;
    }

}
