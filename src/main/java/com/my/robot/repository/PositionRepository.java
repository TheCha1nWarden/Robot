package com.my.robot.repository;

import com.my.robot.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    @Modifying
    @Query(value = "DELETE FROM positions WHERE id <> 1",
            nativeQuery = true)
    void reset();

}
