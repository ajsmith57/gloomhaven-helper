package com.subjecttochange.ghhelper.persistence.model.orm.monster;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subjecttochange.ghhelper.persistence.model.orm.BaseModel;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

@Entity
@Data
public class MonsterActionDeck extends BaseModel {

    private Monster monster;

    @JsonIgnore
    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "monster")
    private List<MonsterAction> actionDeck;
    @JsonIgnore
    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "monster")
    private List<MonsterAction> actionDiscard;
    @OneToOne
    private MonsterAction currentAction;


    public MonsterActionDeck()  {
        super();

        setActionDeck(new Stack<>());
        setActionDiscard(new Stack<>());
    }

    /**
     * Draws new current action and checks for reshuffling
     */
    public void drawNewMonsterAction() {
        Stack<MonsterAction> deck = new Stack<>();
        deck.addAll(getActionDeck());
        Stack<MonsterAction> discard = new Stack<>();
        discard.addAll(getActionDiscard());

        if (currentAction != null) {
            discard.push(currentAction);
            if (currentAction.isShuffleable()) {
                reshuffleActions();
            }
        }
        currentAction = deck.pop();

        setActionDeck(new ArrayList<>(deck));
        setActionDiscard(new ArrayList<>(discard));
    }

    private void reshuffleActions() {
        Stack<MonsterAction> deck = new Stack<>();
        deck.addAll(getActionDeck());
        Stack<MonsterAction> discard = new Stack<>();
        discard.addAll(getActionDiscard());

        while (!discard.empty()) {
            deck.push(discard.pop());
        }
        Collections.shuffle(deck);

        setActionDeck(new ArrayList<>(deck));
        setActionDiscard(new ArrayList<>(discard));
    }

}
