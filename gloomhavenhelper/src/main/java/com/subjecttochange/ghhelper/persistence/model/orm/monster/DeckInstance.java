package com.subjecttochange.ghhelper.persistence.model.orm.monster;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subjecttochange.ghhelper.persistence.model.orm.BaseModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@ToString
public class DeckInstance extends BaseModel {

    @Id
    @GeneratedValue(generator = "deck_instance_generator")
    @SequenceGenerator(name = "deck_instance_generator", sequenceName = "deck_instance_sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_deck_id", nullable = false)
    @JsonIgnore
    private ActionDeck deck;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Action> mutatedDeck;
    @OneToOne
    private Action currentAction;

    public DeckInstance() {
        super();
    }

    public static DeckInstance create(ActionDeck deck) {
        DeckInstance deckInstance = new DeckInstance();
        deckInstance.setDeck(deck);
        deckInstance.setMutatedDeck(new ArrayList<>(deck.getDeck()));
        deckInstance.shuffle();
        return deckInstance;
    }

    public void drawAction() {
        shuffle();

        currentAction = mutatedDeck.remove(mutatedDeck.size() - 1);

        if (currentAction.getShuffleable()) {
            mutatedDeck = new ArrayList<>(deck.getDeck());
        }
    }

    private void shuffle() {
        Collections.shuffle(getMutatedDeck());
    }
}