package protocol;

import model.Game;

import java.util.ArrayList;

public record GetGamesResponse(ArrayList<Game> games) {
}
