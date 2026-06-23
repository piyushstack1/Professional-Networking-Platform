package Connection.Service.connection_servicee.repository;

import Connection.Service.connection_servicee.ConnectionServiceeApplication;
import Connection.Service.connection_servicee.entity.Person;
import Connection.Service.connection_servicee.service.ConnectionService;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> findByUserId(Long userId);

    @Query("""
        MATCH (personA:Person)-[:CONNECTED_TO]-(personB:Person)
            WHERE id(personA) = $userId
        RETURN personB
        """)
    List<Person> getfirstdegreeconnection( @Param("userId") Long userId);


    @Query("""
        MATCH (personA:Person)-[:CONNECTED_TO]-(personB:Person)-[:CONNECTED_TO]-(personC:Person)
        WHERE id(personA) = $userId
          AND personC <> personA
          AND NOT (personA)-[:CONNECTED_TO]-(personC)
        RETURN DISTINCT personC
        """)
    List<Person> getseconddegreeconnection(@Param("userId") Long userId);


    @Query("""
MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person)
WHERE id(p1) = $senderId AND id(p2) = $receiverId
RETURN count(r) > 0
""")
    boolean connectionRequestExists(
            @Param("senderId") Long senderId,
            @Param("receiverId") Long receiverId
    );


    @Query("MATCH (p1:Person)-[r:CONNECTED_TO]-(p2:Person) " +
            "WHERE id(p1) = $senderId AND id(p2) = $receiverId " +
            "RETURN count(r) > 0")
    boolean alreadyConnected(  @Param("senderId") Long senderId,
                               @Param("receiverId") Long receiverId);

    @Query("""
    MATCH (p1:Person), (p2:Person)
    WHERE id(p1) = $senderId AND id(p2) = $receiverId
    CREATE (p1)-[:REQUESTED_TO]->(p2)
    """)
    void addConnectionRequest(
            @Param("senderId") Long senderId,
            @Param("receiverId") Long receiverId
    );


    @Query("""
MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person)
WHERE id(p1) = $senderId AND id(p2) = $receiverId
DELETE r
CREATE (p1)-[:CONNECTED_TO]->(p2)
""")
    void acceptConnectionRequest(
            @Param("senderId") Long senderId,
            @Param("receiverId") Long receiverId
    );


    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE id(p1) = $senderId AND id(p2) = $receiverId " +
            "DELETE r")
    void rejectConnectionRequest(  @Param("senderId") Long senderId,
                                   @Param("receiverId") Long receiverId);


}
