package com.swe.challenge.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.val;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "sessions")
public class Session {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "uuid", unique = true, nullable = false)
  UUID uuid;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "owner_id", nullable = false)
  User owner;

  @Column(name = "is_active", nullable = false)
  boolean active;

  @Column(name = "picked_restaurant", length = Invitation.Attributes.RESTAURANT_MAX_LENGTH)
  String pickedRestaurant;

  @OneToMany(
      mappedBy = Invitation.Fields.SESSION,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  List<Invitation> invitations;

  public void addInvitees(List<Invitation> invitations) {
    for (val invitee : invitations) {
      invitee.setSession(this);
      this.invitations.add(invitee);
    }
  }

  public boolean containsUser(User user) {
    return this.invitations.stream().anyMatch(i -> i.getInvitee().getId().equals(user.getId()));
  }
}
