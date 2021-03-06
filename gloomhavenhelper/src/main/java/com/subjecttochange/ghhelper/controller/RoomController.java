package com.subjecttochange.ghhelper.controller;

import com.subjecttochange.ghhelper.persistence.model.EventType;
import com.subjecttochange.ghhelper.persistence.model.orm.Room;
import com.subjecttochange.ghhelper.persistence.model.responsebodies.DeleteHashResponseBody;
import com.subjecttochange.ghhelper.persistence.service.RoomService;
import com.subjecttochange.ghhelper.persistence.service.SessionService;
import com.subjecttochange.ghhelper.persistence.service.StreamService;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author subjecttochange
 * @version 1
 *
 * Controls restful responses to room objects
 */
@RestController
@ToString
public class RoomController {

    private final RoomService roomService;
    private final StreamService streamService;
    private final SessionService sessionService;

    @Autowired
    public RoomController(RoomService roomService, StreamService streamService, SessionService sessionService) {
        this.roomService = roomService;
        this.streamService = streamService;
        this.sessionService = sessionService;
    }

    /**
     * Returns a list of all rooms
     * @param pageable provides pagination in json response
     * @return json list of rooms
     */
    @GetMapping("/rooms")
    @ResponseBody
    public Page<Room> getRooms(@RequestParam(value = "hash") String hash, Pageable pageable) {
        return roomService.getRooms(hash, pageable);
    }

    /**
     * Creates a new room
     * @return json object of created room
     */
    @PostMapping("/rooms")
    @ResponseBody
    public Room createRoom(@Valid @RequestBody Room request) {
        return roomService.createRoom(request);
    }

    /**
     * Updates a room object with passed parameters
     * @param hash of room to update
     * @param request json parameters passed in request
     * @return updated object as response
     */
    @PutMapping("/rooms")
    @ResponseBody
    public Room updateRoom(@RequestParam(value = "hash") String hash,
                           @Valid @RequestBody(required = false) Room request) {
        Room room = roomService.updateRoom(hash, request);
        streamService.newEvent(EventType.PUT_ROOM, hash, sessionService.getRooms(room.getHash(), null));
        return room;
    }

    /**
     * Deletes the room denoted by the hash
     * @param hash of room to delete
     * @return status code of operation
     */
    @DeleteMapping("/rooms")
    public ResponseEntity<?> deleteRoom(@RequestParam(value = "hash") String hash) {
        ResponseEntity<?> response = roomService.deleteRoom(hash);
        streamService.newEvent(EventType.DELETE_ROOM, hash, DeleteHashResponseBody.create(hash));
        return response;
    }
}
