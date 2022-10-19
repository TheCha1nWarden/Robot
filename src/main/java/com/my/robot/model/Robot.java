package com.my.robot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "robot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Robot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Position position;

    private Vector vector;

    private Command lastCommand;

    public Robot(Position position, Vector vector, Command command) {
        this.position = position;
        this.vector = vector;
        this.lastCommand = command;
    }

}
