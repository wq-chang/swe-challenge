package com.swe.challenge.repository;

import com.swe.challenge.repository.entity.Invitation;
import com.swe.challenge.service.model.InvitationDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InvitationRepository extends CrudRepository<Invitation, Long> {

  @Query(
      "SELECT new com.swe.challenge.service.model.InvitationDto("
          + "s.uuid, "
          + "i.uuid, "
          + "o.username, "
          + "s.active, "
          + "i.accepted, "
          + "i.restaurant, "
          + "s.pickedRestaurant) "
          + "FROM Invitation i "
          + "JOIN i.session s "
          + "JOIN i.invitee u "
          + "JOIN s.owner o "
          + "WHERE u.id = :invitationId "
          + "AND i.accepted IN (:accepted) "
          + "AND s.active IN (:active)")
  List<InvitationDto> findAllByInvitationAndAcceptedInAndSession_ActiveIn(
      long invitationId, List<Boolean> accepted, List<Boolean> active);

  @Query(
      "FROM Invitation i "
          + "JOIN FETCH i.session s "
          + "JOIN FETCH s.owner o "
          + "JOIN i.invitee u "
          + "WHERE i.uuid = :uuid "
          + "AND u.username = :inviteeUsername "
          + "AND s.active = :active "
          + "AND i.accepted= :accepted")
  Optional<Invitation> findByUuidAndInvitee_UsernameAndSession_ActiveAndAccepted(
      UUID uuid, String inviteeUsername, boolean active, boolean accepted);

  @Query(
      "FROM Invitation i "
          + "JOIN FETCH i.session s "
          + "JOIN FETCH s.invitations si "
          + "JOIN FETCH s.owner o "
          + "JOIN i.invitee u "
          + "WHERE i.uuid = :uuid "
          + "AND u.username = :inviteeUsername "
          + "AND i.accepted = :accepted")
  Optional<Invitation> findByUuidAndInvitee_UsernameAndAccepted(
      UUID uuid, String inviteeUsername, boolean accepted);
}
