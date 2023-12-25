package com.swe.challenge.repository;

import com.swe.challenge.repository.entity.Session;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, Long> {

  int countByOwner_IdAndActive(long ownerId, boolean active);

  @Query(
      "FROM Session s "
          + "JOIN FETCH s.owner o "
          + "LEFT JOIN FETCH s.invitations i "
          + "LEFT JOIN FETCH i.invitee u "
          + "WHERE s.owner.username = :ownerUsername "
          + "AND s.active = :active")
  Optional<Session> findByOwnerUsernameAndActive(String ownerUsername, boolean active);
}
