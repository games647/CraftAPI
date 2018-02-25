package com.github.games647.craftapi;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class NamePredicate implements Predicate<String> {

    //this includes a-zA-Z1-9_
    //compile the pattern only on plugin enable -> and this have to be thread-safe
    private final Pattern validNameMatcher = Pattern.compile("^\\w{2,16}$");

    @Override
    public boolean test(String playerName) {
        return validNameMatcher.matcher(playerName).matches();
    }
}
