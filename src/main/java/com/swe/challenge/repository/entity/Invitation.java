package com.swe.challenge.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@FieldNameConstants
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "invitations")
public class Invitation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "uuid", unique = true, nullable = false)
  UUID uuid;

  @Column(name = "is_accepted", nullable = false)
  boolean accepted;

  @Column(name = "restaurant", length = Attributes.RESTAURANT_MAX_LENGTH)
  String restaurant;

  @Column(name = "is_picked", nullable = false)
  boolean picked;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "session_id", nullable = false)
  Session session;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "invitee_id", nullable = false)
  User invitee;

  public static final class Attributes {

    private Attributes() {}

    public static final int RESTAURANT_MAX_LENGTH = 100;
  }
}
