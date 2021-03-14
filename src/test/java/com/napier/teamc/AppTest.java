package com.napier.teamc;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class AppTest
{
    /**
     * Country Test - Continents Enum method - customValueOf Asia
     * Test the method customValueOf in Country.Continents
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryContinentsCustomValueOfAsia()
    {
        Country.Continents continent = Country.Continents.customValueOf("Asia");

        assertSame(Country.Continents.ASIA, continent);
    }
    /**
     * Country Test - Continents Enum method - customValueOf Europe
     * Test the method customValueOf in Country.Continents
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryContinentsCustomValueOfEurope()
    {
        Country.Continents continent = Country.Continents.customValueOf("Europe");

        assertSame(Country.Continents.EUROPE, continent);
    }
    /**
     * Country Test - Continents Enum method - customValueOf North America
     * Test the method customValueOf in Country.Continents
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryContinentsCustomValueOfNorthAmerica()
    {
        Country.Continents continent = Country.Continents.customValueOf("North America");

        assertSame(Country.Continents.NORTH_AMERICA, continent);
    }
    /**
     * Country Test - Continents Enum method - customValueOf Africa
     * Test the method customValueOf in Country.Continents
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryContinentsCustomValueOfAfrica()
    {
        Country.Continents continent = Country.Continents.customValueOf("Africa");

        assertSame(Country.Continents.AFRICA, continent);
    }
    /**
     * Country Test - Continents Enum method - customValueOf Oceania
     * Test the method customValueOf in Country.Continents
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryContinentsCustomValueOfOceania()
    {
        Country.Continents continent = Country.Continents.customValueOf("Oceania");

        assertSame(Country.Continents.OCEANIA, continent);
    }
    /**
     * Country Test - Continents Enum method - customValueOf Antarctica
     * Test the method customValueOf in Country.Continents
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryContinentsCustomValueOfAntarctica()
    {
        Country.Continents continent = Country.Continents.customValueOf("Antarctica");

        assertSame(Country.Continents.ANTARCTICA, continent);
    }
    /**
     * Country Test - Continents Enum method - customValueOf South America
     * Test the method customValueOf in Country.Continents
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryContinentsCustomValueOfSouthAmerica()
    {
        Country.Continents continent = Country.Continents.customValueOf("South America");

        assertSame(Country.Continents.SOUTH_AMERICA, continent);
    }

    /**
     * Country Test - Continents Enum method - ToString Asia
     * Test the method ToString in Country.Continents
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryContinentsToStringAsia()
    {
        String continent_string = Country.Continents.customValueOf("Asia").toString();

        assertSame("Asia", continent_string);
    }
    /**
     * Country Test - Continents Enum method - ToString Europe
     * Test the method ToString in Country.Continents
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryContinentsToStringEurope()
    {
        String continent_string = Country.Continents.customValueOf("Europe").toString();

        assertSame("Europe", continent_string);
    }
    /**
     * Country Test - Continents Enum method - ToString North America
     * Test the method ToString in Country.Continents
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryContinentsToStringNorthAmerica()
    {
        String continent_string = Country.Continents.customValueOf("North America").toString();

        assertSame("North America", continent_string);
    }
    /**
     * Country Test - Continents Enum method - ToString Africa
     * Test the method ToString in Country.Continents
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryContinentsToStringAfrica()
    {
        String continent_string = Country.Continents.customValueOf("Africa").toString();

        assertSame("Africa", continent_string);
    }
    /**
     * Country Test - Continents Enum method - ToString Oceania
     * Test the method ToString in Country.Continents
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryContinentsToStringOceania()
    {
        String continent_string = Country.Continents.customValueOf("Oceania").toString();

        assertSame("Oceania", continent_string);
    }
    /**
     * Country Test - Continents Enum method - customValueOf Antarctica
     * Test the method customValueOf in Country.Continents
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryContinentsToStringAntarctica()
    {
        String continent_string = Country.Continents.customValueOf("Antarctica").toString();

        assertSame("Antarctica", continent_string);
    }
    /**
     * Country Test - Continents Enum method - ToString South America
     * Test the method ToString in Country.Continents
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryContinentsToStringSouthAmerica()
    {
        String continent_string = Country.Continents.customValueOf("South America").toString();

        assertSame("South America", continent_string);
    }

}