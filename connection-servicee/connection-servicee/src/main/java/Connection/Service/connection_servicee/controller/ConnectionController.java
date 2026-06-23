package Connection.Service.connection_servicee.controller;


import Connection.Service.connection_servicee.entity.Person;
import Connection.Service.connection_servicee.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/core")
public class ConnectionController {

    private final ConnectionService connectionService;

    @GetMapping("/{userId}/getFirstDegConnection")
    public ResponseEntity<List<Person>> getfirstdegreeconnection(@PathVariable Long userId ){
        List<Person> persons = connectionService.getfirstdegreeconnection(userId);
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/{userId}/getSecondDegConnection")
    public ResponseEntity<List<Person>> getseconddegreeconnection(@PathVariable Long userId ){
        List<Person> persons = connectionService.getseconddegreeconnection(userId);
        return ResponseEntity.ok(persons);
    }

    @PostMapping("/request/{userId}")
    public ResponseEntity<Void> sendConnectionRequest(@PathVariable Long userId){
        connectionService.sendConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<Void> acceptConnectionRequest(@PathVariable Long userId){
        connectionService.acceptConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<Void> rejectConnectionRequest(@PathVariable Long userId){
        connectionService.rejectConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }
}
