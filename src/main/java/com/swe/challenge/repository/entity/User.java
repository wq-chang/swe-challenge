package com.swe.challenge.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "uuid", unique = true, nullable = false)
  UUID uuid;

  @Column(
      name = "username",
      length = User.Attributes.USERNAME_MAX_LENGTH,
      unique = true,
      nullable = false)
  String username;

  @Column(name = "display_name", length = User.Attributes.DISPLAY_NAME_MAX_LENGTH, nullable = false)
  String displayName;

  @OneToMany(
      mappedBy = Session.Fields.OWNER,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  List<Session> sessions;

  @OneToMany(
      mappedBy = Invitation.Fields.INVITEE,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  List<Invitation> invitations;

  public static final class Attributes {

    private Attributes() {}

    public static final int USERNAME_MAX_LENGTH = 50;
    public static final int DISPLAY_NAME_MAX_LENGTH = 50;
  }
}
