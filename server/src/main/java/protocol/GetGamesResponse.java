package protocol;

import model.Game;

import java.util.ArrayList;
import java.util.Collection;

public record GetGamesResponse(Collection<Game> games) {
}
