package com.napier.teamc;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AppTest
{
    /**
     * Country Test - toFormattedString method - empty input
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryToFormattedStringEmpty()
    {
        Country cntry = new Country(
                null,
                null,
                null,
                null,
                -1,
                null
        );

        String formattedString = cntry.toFormattedString();

        assertNull(formattedString);
    }

    /**
     * Country Test - toFormattedString method - full input
     * Added by Eoin K:14/03/21
     */
    @Test
    void countryToFormattedStringFull()
    {
        City capital_city = new City(
                "Oranjestad",
                null,
                -1,
                null,
                "Aruba",
                null
        );

        Country cntry = new Country(
                "ABW",
                "Aruba",
                Country.Continents.customValueOf("North America"),
                "Caribbean",
                103000,
                capital_city
        );

        String formattedString = cntry.toFormattedString();
        String expected = "ABW          Aruba                                        North America Caribbean       "
                + "          103000     Oranjestad                        ";

        assertEquals(expected, formattedString);
    }

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

    /** displayFormattedCountriesTest
     * Test app.displayFormattedCountries with a fully populated ArrayList<country>
     *  This test will not assert anything as no values are returned
     *  Added by Eoin K:01/04/21
     */
    @Test
    void displayFormattedCountriesFullCountryTest()
    {
        // Test setup
        App app = new App();

        // Mock Objects
        City capital_city = new City(
                "Oranjestad",
                null,
                -1,
                null,
                "Aruba",
                null
        );
        Country cntry = new Country(
                "ABW",
                "Aruba",
                Country.Continents.customValueOf("North America"),
                "Caribbean",
                103000,
                capital_city
        );
        ArrayList<Country> countries = new ArrayList<>(Arrays.asList(cntry));

        // Test
        app.displayFormattedCountries(countries);
    }

    /** displayFormattedCountriesTest
     * Test app.displayFormattedCountries with a half populated ArrayList<country>
     *  This test will not assert anything as no values are returned
     *  Added by Eoin K:01/04/21
     */
    @Test
    void displayFormattedCountriesHalfCountryTest()
    {
        // Test setup
        App app = new App();

        // Mock Objects
        Country cntry = new Country(
                null,
                "Aruba",
                null,
                "Caribbean",
                103000,
                null
        );
        ArrayList<Country> countries = new ArrayList<>(Arrays.asList(cntry));

        // Test
        app.displayFormattedCountries(countries);
    }

    /** displayFormattedCountriesTest
     * Test app.displayFormattedCountries with a ArrayList<country> that does contain one object but the object isn't populated
     *  This test will not assert anything as no values are returned
     *  Added by Eoin K:01/04/21
     */
    @Test
    void displayFormattedCountriesEmptyCountryTest()
    {
        // Test setup
        App app = new App();

        // Mock Objects
        Country cntry = new Country(
                null,
                null,
                null,
                null,
                -1,
                null
        );
        ArrayList<Country> countries = new ArrayList<>(Arrays.asList(cntry));

        // Test
        app.displayFormattedCountries(countries);
    }

    /** displayFormattedCountriesTest
     * Test app.displayFormattedCountries with an empty ArrayList<country>
     *  This test will not assert anything as no values are returned
     *  Added by Eoin K:01/04/21
     */
    @Test
    void displayFormattedCountriesEmptyArrayTest()
    {
        // Test setup
        App app = new App();

        // Mock Objects
        ArrayList<Country> countries = new ArrayList<>();

        // Test
        app.displayFormattedCountries(countries);
    }

    /** displayFormattedCitiesFullCityTest
     * Test app.displayFormattedCities with a fully populated ArrayList<City>
     *  This test will not assert anything as no values are returned
     *  Added by Eoin K:01/04/21
     */
    @Test
    void displayFormattedCitiesFullCityTest()
    {
        // Test setup
        App app = new App();

        // Mock Objects
        City city = new City(
                "Edinburgh",
                "Scotland",
                450180,
                "Europe",
                "United Kingdom",
                "British Islands"
        );
        ArrayList<City> cities = new ArrayList<>(Arrays.asList(city));

        // Test
        app.displayFormattedCities(cities);
    }

    /** displayFormattedCitiesTest
     * Test app.displayFormattedCities with a half populated ArrayList<City>
     *  This test will not assert anything as no values are returned
     *  Added by Eoin K:01/04/21
     */
    @Test
    void displayFormattedCitiesHalfCityTest()
    {
        // Test setup
        App app = new App();

        // Mock Objects
        City city = new City(
                "Edinburgh",
                "Scotland",
                450180,
                null,
                null,
                null
        );
        ArrayList<City> cities = new ArrayList<>(Arrays.asList(city));

        // Test
        app.displayFormattedCities(cities);
    }

    /** displayFormattedCitiesEmptyCityTest
     * Test app.displayFormattedCities with a ArrayList<City> that does contain one object but the object isn't populated
     *  This test will not assert anything as no values are returned
     *  Added by Eoin K:01/04/21
     */
    @Test
    void displayFormattedCountriesEmptyCityTest()
    {
        // Test setup
        App app = new App();

        // Mock Objects
        City city = new City(
                null,
                null,
                -1,
                null,
                null,
                null
        );
        ArrayList<City> cities = new ArrayList<>(Arrays.asList(city));

        // Test
        app.displayFormattedCities(cities);
    }

    /** displayFormattedCitiesEmptyArrayTest
     * Test app.displayFormattedCities with an empty ArrayList<City>
     *  This test will not assert anything as no values are returned
     *  Added by Eoin K:01/04/21
     */
    @Test
    void displayFormattedCitiesEmptyArrayTest()
    {
        // Test setup
        App app = new App();

        // Mock Objects
        ArrayList<City> cities = new ArrayList<>();

        // Test
        app.displayFormattedCities(cities);
    }

    /** displayFormattedPopulationsPopulatedTest
     * Test app.displayFormattedPopulations with a populated HashMap<String, Number>
     *  This test will not assert anything as no values are returned
     *  Added by Eoin K:01/04/21
     */
    @Test
    void displayFormattedPopulationsPopulatedTest()
    {
        // Test setup
        App app = new App();

        // Mock Objects
        HashMap<String, Number> locations = new HashMap<String, Number>();
        locations.put("World", 1);

        // Test
        app.displayFormattedPopulations(locations);
    }

    /** displayFormattedPopulationsEmptyTest
     * Test app.displayFormattedPopulations with a empty HashMap<String, Number>
     *  This test will not assert anything as no values are returned
     *  Added by Eoin K:01/04/21
     */
    @Test
    void displayFormattedPopulationsEmptyTest()
    {
        // Test setup
        App app = new App();

        // Mock Objects
        HashMap<String, Number> locations = new HashMap<String, Number>();

        // Test
        app.displayFormattedPopulations(locations);
    }

    /** displayFormattedReportsPopulatedTest
     * Test app.displayFormattedPopulations with a populated HashMap<String, Number>
     *  This test will not assert anything as no values are returned
     *  Added by Eoin K:10/04/21
     */
    @Test
    void displayFormattedReportsPopulatedTest()
    {
        // Test setup
        App app = new App();

        // Mock Objects
        ArrayList<String[]> report = new ArrayList<>();
        report.add(new String[]{"Header 1", "Header 2"});
        report.add(new String[]{"Value 1", "Value 2"});

        // Test
        app.displayFormattedReports(report);
    }

    /** displayFormattedReportsEmptyWithoutHeadersTest
     * Test app.displayFormattedPopulations with a populated ArrayList<String[]> that has not been populated at all.
     * This test will not assert anything as no values are returned
     * Added by Eoin K:10/04/21
     */
    @Test
    void displayFormattedReportsEmptyWithoutHeadersTest()
    {
        // Test setup
        App app = new App();

        // Mock Objects
        ArrayList<String[]> report = new ArrayList<>();

        // Test
        app.displayFormattedReports(report);
    }

    /** displayFormattedReportsEmptyWithHeadersTest
     * Test app.displayFormattedPopulations with a populated ArrayList<String[]> that has only been populated,
     *   with a header row.
     * This test will not assert anything as no values are returned
     * Added by Eoin K:10/04/21
     */
    @Test
    void displayFormattedReportsEmptyWithHeadersTest()
    {
        // Test setup
        App app = new App();

        // Mock Objects
        ArrayList<String[]> report = new ArrayList<>();
        report.add(new String[]{"Header 1", "Header 2"});

        // Test
        app.displayFormattedReports(report);
    }
}