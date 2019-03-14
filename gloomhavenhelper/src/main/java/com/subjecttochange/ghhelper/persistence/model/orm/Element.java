package com.subjecttochange.ghhelper.persistence.model.orm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subjecttochange.ghhelper.exception.Errors;
import com.subjecttochange.ghhelper.exception.ResourceNotFoundException;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
public class Element extends BaseModel {

    @Id
    @GeneratedValue(generator = "element_generator")
    @SequenceGenerator(name = "element_generator", sequenceName = "element_sequence", initialValue = 1)
    private Long id;
    private ElementType type;
    private Integer strength;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Room room;

    public Element() {
        super();
    }

    public static Element createElement(ElementType elementType, Integer strength, Room room) {
        Element element = new Element();
        element.setType(elementType);
        element.setStrength(strength);
        element.setRoom(room);
        return element;
    }

    public enum ElementType {
        FIRE,
        ICE,
        AIR,
        EARTH,
        LIGHT,
        DARK
    }

    public Element updateElement(Element elementRequest){
        if (elementRequest.getStrength() != null) {
            setStrength(elementRequest.getStrength());
        }
        return this;
    }

    public static void checkHashMatchesGiven(Element element, String hash, Long id) {
        if (!element.getRoom().getHash().equals(hash)) {
            throw new ResourceNotFoundException(Errors.WRONG_HASH_FOR_ID + id);
        }
    }
}