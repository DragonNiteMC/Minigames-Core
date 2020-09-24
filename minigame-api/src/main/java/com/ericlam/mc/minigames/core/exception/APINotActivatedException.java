package com.ericlam.mc.minigames.core.exception;

public class APINotActivatedException extends GameRunException {

    public APINotActivatedException() {
        super("The API has not activated yet.", false);
    }
}
