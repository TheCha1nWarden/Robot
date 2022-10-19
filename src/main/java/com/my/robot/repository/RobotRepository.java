package com.my.robot.repository;

import com.my.robot.model.Robot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RobotRepository extends JpaRepository<Robot, Long> {

    @Query(value = "SELECT * FROM robot ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Robot getCurrent();

    @Modifying
    @Query(value = "DELETE FROM robot WHERE id <> 1",
            nativeQuery = true)
    void reset();

}
