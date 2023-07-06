package net.landania.commons;

import java.util.Map;

public record Messages(Map<String, String> messages) {

    static final Messages DEFAULT = new Messages(Map.of(
            "home-created", "<gold>You have successfully created a home called %home%.",
            "home-already-exists", "<red>You already have a home called %home%.",
            "prefix", "<gray>[<gold>Home<gray>]<reset> > ",
            "home-deleted", "<gold>You have successfully deleted your home called %home%.",
            "home-teleported", "<gold>You have been teleported to your home called %home%.",
            "home-not-found", "<red>You do not have a home called %home%.",
            "no-permission", "<red>You do not have permission to do that.",
            "too-many-homes", "<red>You have reached the maximum amount of homes.",
            "all-homes-deleted", "<gold>You have successfully deleted all your homes."
    ));

}


