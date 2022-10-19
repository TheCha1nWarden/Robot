package com.my.robot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Vector {
    NORTH(new Position(0, 1)),
    EAST(new Position(1, 0)),
    SOUTH(new Position(0, -1)),
    WEST(new Position(-1, 0));

    private static final String ILLEGAL_ARGUMENT_EXCEPTION_MSG = "Incorrect command";
    private Position position;

    public static Vector doTurn(Command command, Vector currentVector) {
        int position = currentVector.ordinal();
        switch (command) {
            case L:
                if (position == 0) {
                    position = values().length - 1;
                } else {
                    position--;
                }
                break;
            case R:
                if (position == values().length - 1) {
                    position = 0;
                } else {
                    position++;
                }
                break;
            default:
                throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MSG);
        }
        return values()[position];
    }

}
