package com.subjecttochange.ghhelper.persistence.model.orm.monster;

import com.google.gson.JsonObject;
import com.subjecttochange.ghhelper.persistence.model.orm.BaseModel;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Monster extends BaseModel {
    @Id
    @GeneratedValue(generator = "monster_generator")
    @SequenceGenerator(
            name = "monster_generator",
            sequenceName = "monster_sequence",
            initialValue = 1
    )
    private Long id;
    private String name;
    private String attributes;
    private String eliteAttributes;
    private Integer level;
    private Integer health;
    private Integer movement;
    private Integer attack;
    private Integer range;
    private Integer eliteHealth;
    private Integer eliteMove;
    private Integer eliteAttack;
    private Integer eliteRange;

    public Monster() {
        super();
    }

    public static Monster create(String name, Integer maxHealth) {
        Monster monster = new Monster();
        monster.setName(name);
        monster.setHealth(maxHealth);
        return monster;
    }

    public static Monster create(JsonObject levelStats, String monsterName) {
        Monster monster = new Monster();
        monster.setLevel(levelStats.get("level").getAsInt());

        monster.setName(monsterName);
        monster.setHealth(levelStats.getAsJsonObject("normal").get("health").getAsInt());
        monster.setEliteHealth(levelStats.getAsJsonObject("elite").get("health").getAsInt());

        monster.setMovement(levelStats.getAsJsonObject("normal").get("move").getAsInt());
        monster.setEliteMove(levelStats.getAsJsonObject("elite").get("move").getAsInt());

        monster.setAttack(levelStats.getAsJsonObject("normal").get("attack").getAsInt());
        monster.setEliteAttack(levelStats.getAsJsonObject("elite").get("attack").getAsInt());

        monster.setRange(levelStats.getAsJsonObject("normal").get("range").getAsInt());
        monster.setEliteRange(levelStats.getAsJsonObject("elite").get("range").getAsInt());

        //monster.setAttributes(levelStats.getAsJsonObject("normal").get("attributes").getAsString());
        //monster.setEliteAttributes(levelStats.getAsJsonObject("elite").get("attributes").getAsString());
        System.out.println(monster.toString());

        return monster;
    }

    public Monster updateMonster(Monster monsterRequest) {
        setName(monsterRequest.getName());
        setAttributes(monsterRequest.getAttributes());
        setEliteAttributes(monsterRequest.getEliteAttributes());
        setLevel(monsterRequest.getLevel());
        setHealth(monsterRequest.getHealth());
        setMovement(monsterRequest.getMovement());
        setAttack(monsterRequest.getAttack());
        setRange(monsterRequest.getRange());
        setEliteHealth(monsterRequest.getEliteHealth());
        setEliteMove(monsterRequest.getEliteMove());
        setEliteAttack(monsterRequest.getEliteAttack());
        setEliteHealth(monsterRequest.getEliteHealth());
        return this;
    }
}
