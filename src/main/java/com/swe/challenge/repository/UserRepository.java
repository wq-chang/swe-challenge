package com.swe.challenge.repository;

import com.swe.challenge.repository.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

  int countByUsername(String username);

  Optional<User> findByUsername(String username);

  List<User> findAllByUsernameIn(List<String> usernames);
}
