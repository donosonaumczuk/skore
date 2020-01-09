package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;

import java.time.LocalDate;
import java.time.Period;

public class GameDto {

    // nombre, creador, tipo, padel dobles, Fecha(Date), Hora, Direccion, cantidad de insctiptos, cantidad total de jugadores, Jugadores
    
    private GameDto(Game game) {

    }

    public static GameDto from(Game game) {
        return new GameDto(game);
    }
}
