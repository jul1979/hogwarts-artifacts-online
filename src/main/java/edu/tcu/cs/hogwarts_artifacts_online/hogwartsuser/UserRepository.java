package edu.tcu.cs.hogwarts_artifacts_online.hogwartsuser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<HogwartsUser, Integer> {
}
