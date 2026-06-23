package Connection.Service.connection_servicee.service;

import Connection.Service.connection_servicee.entity.Person;
import Connection.Service.connection_servicee.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    @Transactional
    public void createPerson(Long userId, String name) {
        log.info("Saving person to Neo4j: {}, {}", userId, name);
        personRepository.save(new Person(userId, name));
    }

}
