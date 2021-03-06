package com.subjecttochange.ghhelper.persistence.model.orm.monster;

import com.subjecttochange.ghhelper.persistence.model.orm.BaseModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString
public class Status extends BaseModel {

    @Transient
    public static final String[] tempStatuses = {
            "Immobilize",
            "Disarm",
            "Stun",
            "Muddle",
            "Invisible",
            "Strengthen"
    };

    @Id
    @GeneratedValue(generator = "status_generator")
    @SequenceGenerator(
            name = "status_generator",
            sequenceName = "status_sequence"
    )
    private Long id;
    private String name;
    private String tooltip;

    public Status() {
        super();
    }

    public static Status create(String name, String tooltip) {
        Status status = new Status();
        status.setName(name);
        status.setTooltip(tooltip);
        return status;
    }

    public Status updateStatus(Status statusRequest){
        if (statusRequest.getName() != null) {
            setName(statusRequest.getName());
        }
        if (statusRequest.getTooltip() != null) {
            setTooltip(statusRequest.getTooltip());
        }
        return this;
    }
}
