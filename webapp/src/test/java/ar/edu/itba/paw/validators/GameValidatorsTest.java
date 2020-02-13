package ar.edu.itba.paw.validators;

import ar.edu.itba.paw.webapp.exceptions.ApiException;
import ar.edu.itba.paw.webapp.utils.JSONUtils;
import ar.edu.itba.paw.webapp.validators.GameValidators;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GameValidatorsTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void whenValidatingCreationIfHasAllKnownAndRequiredFieldsWithValidFormatThenSuccessByDoingNothing() {
        GameValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"title\": \"El juego del siglo\",\n" +
                            "\t\"description\": \"\",\n" +
                            "\t\"sport\": \"futbol5\",\n" +
                            "\t\"date\": {\n" +
                                "\t\t\"year\": 2018,\n" +
                                "\t\t\"monthNumber\": 2,\n" +
                                "\t\t\"dayOfMonth\": 28\n" +
                            "\t},\n" +
                                "\t\"time\": {\n" +
                                "\t\t\"hour\": 10,\n" +
                                "\t\t\"minute\": 9\n" +
                            "\t},\n" +
                            "\t\"minutesOfDuration\": 20,\n" +
                            "\t\"location\": {\n" +
                                "\t\t\"country\": \"Argentina\",\n" +
                                "\t\t\"state\": \"Pcia de Buenos Aires\",\n" +
                                "\t\t\"city\": \"Pellegrini\",\n" +
                                "\t\t\"street\": \"Guarrochena \"\n" +
                            "\t},\n" +
                            "\t\"individual\": true,\n" +
                            "\t\"competitive\": false\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfHasAllRequiredFieldsWithValidFormatThenSuccessByDoingNothing() {
        GameValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"title\": \"El juego del siglo\",\n" +
                            "\t\"sport\": \"futbol5\",\n" +
                            "\t\"date\": {\n" +
                                "\t\t\"year\": 2018,\n" +
                                "\t\t\"monthNumber\": 2,\n" +
                                "\t\t\"dayOfMonth\": 28\n" +
                            "\t},\n" +
                            "\t\"time\": {\n" +
                                "\t\t\"hour\": 10,\n" +
                                "\t\t\"minute\": 9\n" +
                            "\t},\n" +
                            "\t\"minutesOfDuration\": 20,\n" +
                            "\t\"location\": {\n" +
                                "\t\t\"country\": \"Argentina\",\n" +
                                "\t\t\"state\": \"Pcia de Buenos Aires\",\n" +
                                "\t\t\"city\": \"Pellegrini\",\n" +
                                "\t\t\"street\": \"Guarrochena \"\n" +
                            "\t},\n" +
                            "\t\"individual\": true,\n" +
                            "\t\"competitive\": false\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfHasAnUnknownCreationFieldThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'unknown' is unknown or unaccepted");
        GameValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"title\": \"El juego del siglo\",\n" +
                            "\t\"sport\": \"futbol5\",\n" +
                            "\t\"date\": {\n" +
                                "\t\t\"year\": 2018,\n" +
                                "\t\t\"monthNumber\": 2,\n" +
                                "\t\t\"dayOfMonth\": 28\n" +
                            "\t},\n" +
                            "\t\"time\": {\n" +
                                "\t\t\"hour\": 10,\n" +
                                "\t\t\"minute\": 9\n" +
                            "\t},\n" +
                            "\t\"minutesOfDuration\": 20,\n" +
                            "\t\"location\": {\n" +
                                "\t\t\"country\": \"Argentina\",\n" +
                                "\t\t\"state\": \"Pcia de Buenos Aires\",\n" +
                                "\t\t\"city\": \"Pellegrini\",\n" +
                                "\t\t\"street\": \"Guarrochena \"\n" +
                            "\t},\n" +
                            "\t\"individual\": true,\n" +
                            "\t\"competitive\": false,\n" +
                            "\t\"unknown\": \"whatever value\"\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfYearHasInvalidValueThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'year' must belong to [0,Inf]");
        GameValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"title\": \"El juego del siglo\",\n" +
                            "\t\"description\": \"\",\n" +
                            "\t\"sport\": \"futbol5\",\n" +
                            "\t\"date\": {\n" +
                                "\t\t\"year\": -2018,\n" +
                                "\t\t\"monthNumber\": 2,\n" +
                                "\t\t\"dayOfMonth\": 28\n" +
                            "\t},\n" +
                            "\t\"time\": {\n" +
                                "\t\t\"hour\": 10,\n" +
                            "\t\t\"minute\": 9\n" +
                            "\t},\n" +
                            "\t\"minutesOfDuration\": 20,\n" +
                            "\t\"location\": {\n" +
                                "\t\t\"country\": \"Argentina\",\n" +
                                "\t\t\"state\": \"Pcia de Buenos Aires\",\n" +
                                "\t\t\"city\": \"Pellegrini\",\n" +
                                "\t\t\"street\": \"Guarrochena \"\n" +
                            "\t},\n" +
                            "\t\"individual\": true,\n" +
                            "\t\"competitive\": false\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfMonthHasInvalidValueThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'monthNumber' must belong to [1,12]");
        GameValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"title\": \"El juego del siglo\",\n" +
                            "\t\"description\": \"\",\n" +
                            "\t\"sport\": \"futbol5\",\n" +
                            "\t\"date\": {\n" +
                                "\t\t\"year\": 2018,\n" +
                                "\t\t\"monthNumber\": 14,\n" +
                                "\t\t\"dayOfMonth\": 28\n" +
                            "\t},\n" +
                            "\t\"time\": {\n" +
                                "\t\t\"hour\": 10,\n" +
                                "\t\t\"minute\": 9\n" +
                            "\t},\n" +
                            "\t\"minutesOfDuration\": 20,\n" +
                            "\t\"location\": {\n" +
                                "\t\t\"country\": \"Argentina\",\n" +
                                "\t\t\"state\": \"Pcia de Buenos Aires\",\n" +
                                "\t\t\"city\": \"Pellegrini\",\n" +
                                "\t\t\"street\": \"Guarrochena \"\n" +
                            "\t},\n" +
                            "\t\"individual\": true,\n" +
                            "\t\"competitive\": false\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfDayHasInvalidValueThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'dayOfMonth' must belong to [1,29]");
        GameValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"title\": \"El juego del siglo\",\n" +
                            "\t\"description\": \"\",\n" +
                            "\t\"sport\": \"futbol5\",\n" +
                            "\t\"date\": {\n" +
                                "\t\t\"year\": 2020,\n" +
                                "\t\t\"monthNumber\": 2,\n" +
                                "\t\t\"dayOfMonth\": 30\n" +
                            "\t},\n" +
                            "\t\"time\": {\n" +
                                "\t\t\"hour\": 10,\n" +
                                "\t\t\"minute\": 9\n" +
                            "\t},\n" +
                            "\t\"minutesOfDuration\": 20,\n" +
                            "\t\"location\": {\n" +
                                "\t\t\"country\": \"Argentina\",\n" +
                                "\t\t\"state\": \"Pcia de Buenos Aires\",\n" +
                                "\t\t\"city\": \"Pellegrini\",\n" +
                                "\t\t\"street\": \"Guarrochena \"\n" +
                            "\t},\n" +
                            "\t\"individual\": true,\n" +
                            "\t\"competitive\": false\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfTeamName1IsPresentAndIsIndividualThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'teamName1' known but other field values turn it unaccepted");
        GameValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"title\": \"El juego del siglo\",\n" +
                            "\t\"description\": \"\",\n" +
                            "\t\"sport\": \"futbol5\",\n" +
                            "\t\"date\": {\n" +
                                "\t\t\"year\": 2020,\n" +
                                "\t\t\"monthNumber\": 2,\n" +
                                "\t\t\"dayOfMonth\": 29\n" +
                            "\t},\n" +
                            "\t\"time\": {\n" +
                                "\t\t\"hour\": 10,\n" +
                                "\t\t\"minute\": 9\n" +
                            "\t},\n" +
                            "\t\"minutesOfDuration\": 20,\n" +
                            "\t\"location\": {\n" +
                                "\t\t\"country\": \"Argentina\",\n" +
                                "\t\t\"state\": \"Pcia de Buenos Aires\",\n" +
                                "\t\t\"city\": \"Pellegrini\",\n" +
                                "\t\t\"street\": \"Guarrochena \"\n" +
                            "\t},\n" +
                            "\t\"individual\": true,\n" +
                            "\t\"competitive\": false,\n" +
                            "\t\"teamName1\": \"Equipo Uno\"\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfTeamName2IsPresentAndIsIndividualThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'teamName2' known but other field values turn it unaccepted");
        GameValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"title\": \"El juego del siglo\",\n" +
                            "\t\"description\": \"\",\n" +
                            "\t\"sport\": \"futbol5\",\n" +
                            "\t\"date\": {\n" +
                                "\t\t\"year\": 2020,\n" +
                                "\t\t\"monthNumber\": 2,\n" +
                                "\t\t\"dayOfMonth\": 29\n" +
                            "\t},\n" +
                            "\t\"time\": {\n" +
                                "\t\t\"hour\": 10,\n" +
                                "\t\t\"minute\": 9\n" +
                            "\t},\n" +
                            "\t\"minutesOfDuration\": 20,\n" +
                            "\t\"location\": {\n" +
                                "\t\t\"country\": \"Argentina\",\n" +
                                "\t\t\"state\": \"Pcia de Buenos Aires\",\n" +
                                "\t\t\"city\": \"Pellegrini\",\n" +
                                "\t\t\"street\": \"Guarrochena \"\n" +
                            "\t},\n" +
                            "\t\"individual\": true,\n" +
                            "\t\"competitive\": false,\n" +
                            "\t\"teamName2\": \"Equipo Dos\"\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfTeamName1IsNotPresentAndIsNotIndividualThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Missing required 'teamName1' field");
        GameValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"title\": \"El juego del siglo\",\n" +
                            "\t\"description\": \"\",\n" +
                            "\t\"sport\": \"futbol5\",\n" +
                            "\t\"date\": {\n" +
                                "\t\t\"year\": 2020,\n" +
                                "\t\t\"monthNumber\": 2,\n" +
                                "\t\t\"dayOfMonth\": 29\n" +
                            "\t},\n" +
                            "\t\"time\": {\n" +
                                "\t\t\"hour\": 10,\n" +
                                "\t\t\"minute\": 9\n" +
                            "\t},\n" +
                            "\t\"minutesOfDuration\": 20,\n" +
                            "\t\"location\": {\n" +
                                "\t\t\"country\": \"Argentina\",\n" +
                                "\t\t\"state\": \"Pcia de Buenos Aires\",\n" +
                                "\t\t\"city\": \"Pellegrini\",\n" +
                                "\t\t\"street\": \"Guarrochena \"\n" +
                            "\t},\n" +
                            "\t\"individual\": false,\n" +
                            "\t\"competitive\": false\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfTeamName1IsInvalidAndIsNotIndividualThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'teamName1' must be a string containing english alphabetic " +
                "characters, digits, spaces or any of these characters: ¿?¡!_-.ÑÁÉÍÓÚáéíñóöúü");
        GameValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"title\": \"El juego del siglo\",\n" +
                            "\t\"description\": \"\",\n" +
                            "\t\"sport\": \"futbol5\",\n" +
                            "\t\"date\": {\n" +
                                "\t\t\"year\": 2020,\n" +
                                "\t\t\"monthNumber\": 2,\n" +
                                "\t\t\"dayOfMonth\": 29\n" +
                            "\t},\n" +
                            "\t\"time\": {\n" +
                                "\t\t\"hour\": 10,\n" +
                                "\t\t\"minute\": 9\n" +
                            "\t},\n" +
                            "\t\"minutesOfDuration\": 20,\n" +
                            "\t\"location\": {\n" +
                                "\t\t\"country\": \"Argentina\",\n" +
                                "\t\t\"state\": \"Pcia de Buenos Aires\",\n" +
                                "\t\t\"city\": \"Pellegrini\",\n" +
                                "\t\t\"street\": \"Guarrochena \"\n" +
                            "\t},\n" +
                            "\t\"individual\": false,\n" +
                            "\t\"competitive\": false," +
                            "\t\"teamName1\": \"E/quipo Uno\"\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfIsNotIndividualThenSuccessByDoingNothing() {
        GameValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"title\": \"El juego del siglo\",\n" +
                            "\t\"description\": \"\",\n" +
                            "\t\"sport\": \"futbol5\",\n" +
                            "\t\"date\": {\n" +
                                "\t\t\"year\": 2020,\n" +
                                "\t\t\"monthNumber\": 2,\n" +
                                "\t\t\"dayOfMonth\": 29\n" +
                            "\t},\n" +
                            "\t\"time\": {\n" +
                                "\t\t\"hour\": 10,\n" +
                                "\t\t\"minute\": 9\n" +
                            "\t},\n" +
                            "\t\"minutesOfDuration\": 20,\n" +
                            "\t\"location\": {\n" +
                                "\t\t\"country\": \"Argentina\",\n" +
                                "\t\t\"state\": \"Pcia de Buenos Aires\",\n" +
                                "\t\t\"city\": \"Pellegrini\",\n" +
                                "\t\t\"street\": \"Guarrochena \"\n" +
                            "\t},\n" +
                            "\t\"individual\": false,\n" +
                            "\t\"competitive\": false," +
                            "\t\"teamName1\": \"Equipo Uno\"\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingUpdateThenSuccessByDoingNothing() {
        GameValidators.updateValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"title\": \"El juego del siglo\",\n" +
                            "\t\"description\": \"\",\n" +
                            "\t\"date\": {\n" +
                                "\t\t\"year\": 2020,\n" +
                                "\t\t\"monthNumber\": 2,\n" +
                                "\t\t\"dayOfMonth\": 29\n" +
                            "\t},\n" +
                            "\t\"time\": {\n" +
                                "\t\t\"hour\": 10,\n" +
                                "\t\t\"minute\": 9\n" +
                            "\t},\n" +
                            "\t\"minutesOfDuration\": 20,\n" +
                            "\t\"location\": {\n" +
                                "\t\t\"country\": \"Argentina\",\n" +
                                "\t\t\"state\": \"Pcia de Buenos Aires\",\n" +
                                "\t\t\"city\": \"Pellegrini\",\n" +
                                "\t\t\"street\": \"Guarrochena \"\n" +
                            "\t},\n" +
                            "\t\"teamName1\": \"Equipo Uno\",\n" +
                            "\t\"teamName2\": \"Equipo Dos\"\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingUpdateIfHasAnUnknownUpdateFieldThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'unknown' is unknown or unaccepted");
        GameValidators.updateValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"title\": \"El juego del siglo\",\n" +
                            "\t\"description\": \"\",\n" +
                            "\t\"date\": {\n" +
                                "\t\t\"year\": 2020,\n" +
                                "\t\t\"monthNumber\": 2,\n" +
                                "\t\t\"dayOfMonth\": 29\n" +
                            "\t},\n" +
                            "\t\"time\": {\n" +
                                "\t\t\"hour\": 10,\n" +
                                "\t\t\"minute\": 9\n" +
                            "\t},\n" +
                            "\t\"minutesOfDuration\": 20,\n" +
                            "\t\"location\": {\n" +
                                "\t\t\"country\": \"Argentina\",\n" +
                                "\t\t\"state\": \"Pcia de Buenos Aires\",\n" +
                                "\t\t\"city\": \"Pellegrini\",\n" +
                                "\t\t\"street\": \"Guarrochena \"\n" +
                            "\t},\n" +
                            "\t\"unknown\": \"something\"," +
                            "\t\"teamName1\": \"Equipo Uno\",\n" +
                            "\t\"teamName2\": \"Equipo Dos\"\n" +
                        "}"
                )
        );
    }
}
