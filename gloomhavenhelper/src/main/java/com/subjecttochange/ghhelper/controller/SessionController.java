package com.subjecttochange.ghhelper.controller;

import com.subjecttochange.ghhelper.exception.Errors;
import com.subjecttochange.ghhelper.exception.ResourceNotFoundException;
import com.subjecttochange.ghhelper.persistence.model.orm.Element;
import com.subjecttochange.ghhelper.persistence.model.orm.Room;
import com.subjecttochange.ghhelper.persistence.model.orm.Stat;
import com.subjecttochange.ghhelper.persistence.model.orm.monster.Monster;
import com.subjecttochange.ghhelper.persistence.model.orm.monster.MonsterInstance;
import com.subjecttochange.ghhelper.persistence.model.orm.monster.Status;
import com.subjecttochange.ghhelper.persistence.model.responsebodies.*;
import com.subjecttochange.ghhelper.persistence.repository.MonsterRepository;
import com.subjecttochange.ghhelper.persistence.repository.RoomRepository;
import com.subjecttochange.ghhelper.persistence.repository.StatRepository;
import com.subjecttochange.ghhelper.persistence.repository.StatusRepository;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@ToString
public class SessionController {

    private RoomRepository roomRepository;
    private MonsterRepository monsterRepository;
    private StatusRepository statusRepository;
    private StatRepository statRepository;

    @Autowired
    public SessionController(RoomRepository roomRepository, MonsterRepository monsterRepository,
                             StatusRepository statusRepository, StatRepository statRepository) {
        this.roomRepository = roomRepository;
        this.monsterRepository = monsterRepository;
        this.statusRepository = statusRepository;
        this.statRepository = statRepository;
    }

    @GetMapping("/sessions")
    public @ResponseBody Page<SessionResponseBody>
    getRooms(@RequestParam(value = "hash", required = false) String hash , Pageable pageable) {
        Page<Room> rooms;
        if (hash == null) {
            rooms = roomRepository.findAll(pageable);
        } else {
            Room room = roomRepository.findByHash(hash).orElseThrow(() ->
                    new ResourceNotFoundException(Errors.NO_HASH_ROOM + hash));
            rooms = new PageImpl<>(Collections.singletonList(room));
        }

        List<Status> statuses = statusRepository.findAll();
        List<Stat> stats = statRepository.findAll();

        ArrayList<SessionResponseBody> sessionResponse = new ArrayList<>();

        for (Room room : rooms) {
            List<ElementResponseBody> elementResponses = new ArrayList<>();
            for (Element element : room.getElements()) {
                elementResponses.add(ElementResponseBody.create(element));
            }

            RoomResponseBody roomResponse = RoomResponseBody.create(room, elementResponses);

            sessionResponse.add(SessionResponseBody.create(
                    roomResponse,
                    new ArrayList<>(buildMonsterResponses(room)),
                    statuses,
                    stats
            ));
        }



        return new PageImpl<>(sessionResponse);
    }

    private Collection<MonsterResponseBody> buildMonsterResponses(Room room) {
        Map<Long, MonsterResponseBody> namedMonsterBodies = new HashMap<>();
        List<Monster> monsters = monsterRepository.findAll();

        for (Monster monster : monsters) {
            namedMonsterBodies.put(monster.getId(), MonsterResponseBody.create(monster));
        }

        for (MonsterInstance monsterInstance : room.getMonsterInstances()) {
            Monster monster = monsterInstance.getMonster();

            namedMonsterBodies.get(monster.getId())
                    .getMonsterInstances()
                    .add(MonsterInstanceResponseBody.create(monsterInstance));
        }

        return namedMonsterBodies.values();
    }
}
