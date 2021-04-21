package com.napier.teamc;

import java.sql.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest {
    static App app;

    @BeforeAll
    static void init() {
        app = new App();
        app.connect("localhost:33060");
    }


    /** AppQueryWhileConnectedIntegrationTest
     *  This test tests the App.query method.
     *  It should executes a given SQL Select query on the connected database.
     *  Added by Eoin K:30/03/21
     */
    @Test
    void AppQueryWhileConnectedIntegrationTest() {
        String query = "SELECT * FROM country;";
        ResultSet rset = app.query(query, "error message");

        try
        {
            // If the result set does not contain any records then app.query() has failed tests.
            if (!rset.next()) {
                fail("app.query() did not return any results. Check data is available or test has failed.");
            }

        }
        // If an SQLException is thrown then handle the error.
        catch (SQLException e)
        {
            fail("Should not have thrown an SQL Exception");
        }
    }

    /** AppQueryWhileDisconnectedIntegrationTest
     *  This test tests the App.query method when the App is not connected to the database.
     *  It should throw an SQL Exception.
     *  Added by Eoin K:30/03/21
     */
    @Test
    void AppQueryWhileDisconnectedIntegrationTest1() {
        // Test setup as each test automatically connects to the database
        app.disconnect();

        // Call app.query() with a query and error message
        ResultSet rset = app.query("SELECT * FROM country;", "error message");

        // Assert that the method returns null as the database connection has been closed
        assertEquals(null, rset);

        // To allow the rest of the tests to run successfully, reconnect to the database
        app.connect("localhost:33060");
    }

    /** AppQueryWhileDisconnectedIntegrationTest
     *  This test tests the App.query method when the App is not connected to the database.
     *  It should throw an SQL Exception.
     *  Added by Eoin K:30/03/21
     */
    @Test
    void AppQueryWhileDisconnectedIntegrationTest2() {
        // Test setup as each test automatically connects to the database
        app = new App();

        // Call app.query() with a query and error message
        ResultSet rset = app.query("SELECT * FROM country;", "error message");

        // Assert that the method returns null as the database connection has not been opened
        assertEquals(null, rset);

        // To allow the rest of the tests to run successfully, reconnect to the database
        app.connect("localhost:33060");
    }

    /** handleResultSetCountryWithNameAndPopulationWhileConnectedIntegrationTest
     *  This test tests the App.handleResultSetCountryWithNameAndPopulation method.
     *  The method should take the result of a query (ResultSet) and populates an array list of country objects
     *  Added by Eoin K:01/04/21
     */
    @Test
    void handleResultSetCountryWithNameAndPopulationWhileConnectedIntegrationTest() {
        // Call app.query() with a query and error message
        ResultSet rset = app.query("SELECT Name, Population FROM country LIMIT 5;", "error message");

        ArrayList<Country> countries = app.handleResultSetCountryWithNameAndPopulation(rset, "error message");

        assertEquals(5, countries.size());

        countries.forEach(C -> {
            assertNull(C.country_code);
            assertNotNull(C.name);
            assertNull(C.continent);
            assertNull(C.region);
            assertNull(C.capital_city);
            assertNotEquals(-1, C.population);
        });
    }

    /** handleResultSetCountryWithNameAndPopulationErrorIntegrationTest
     *  This test tests the App.handleResultSetCountryWithNameAndPopulation method.
     *  The method should return null when an error occurs
     *  Added by Eoin K:01/04/21
     */
    @Test
    void handleResultSetCountryWithNameAndPopulationErrorIntegrationTest() {
        // Call app.query() with a query and error message to get a mock result set
        ResultSet rset = app.query("SELECT Name FROM country LIMIT 5;", "error message");

        ArrayList<Country> countries = app.handleResultSetCountryWithNameAndPopulation(rset, "error message");

        assertNull(countries);
    }

    /** handleResultSetCountryWithNamePopulationAndRegionWhileConnectedIntegrationTest
     *  This test tests the App.handleResultSetCountryWithNamePopulationAndRegion method.
     *  The method should take the result of a query (ResultSet) and populates an array list of country objects
     *  Added by Eoin K:01/04/21
     */
    @Test
    void handleResultSetCountryWithNamePopulationAndRegionWhileConnectedIntegrationTest() {
        // Call app.query() with a query and error message
        ResultSet rset = app.query("SELECT Name, Region, Population FROM country LIMIT 5;", "error message");

        ArrayList<Country> countries = app.handleResultSetCountryWithNamePopulationAndRegion(rset, "error message");

        assertEquals(5, countries.size());

        countries.forEach(C -> {
            assertNull(C.country_code);
            assertNotNull(C.name);
            assertNull(C.continent);
            assertNotNull(C.region);
            assertNull(C.capital_city);
            assertNotEquals(-1, C.population);
        });
    }

    /** handleResultSetCountryWithNamePopulationAndRegionErrorIntegrationTest
     *  This test tests the App.handleResultSetCountryWithNamePopulationAndRegion method.
     *  The method should return null when an error occurs
     *  Added by Eoin K:01/04/21
     */
    @Test
    void handleResultSetCountryWithNamePopulationAndRegionErrorIntegrationTest() {
        // Call app.query() with a query and error message to get a mock result set
        ResultSet rset = app.query("SELECT Name, Population FROM country LIMIT 5;", "error message");

        ArrayList<Country> countries = app.handleResultSetCountryWithNamePopulationAndRegion(rset, "error message");

        assertNull(countries);
    }

    /** handleResultSetCountryWithNamePopulationAndContinentWhileConnectedIntegrationTest
     *  This test tests the App.handleResultSetCountryWithNamePopulationAndContinent method.
     *  The method should take the result of a query (ResultSet) and populates an array list of country objects
     *  Added by Eoin K:01/04/21
     */
    @Test
    void handleResultSetCountryWithNamePopulationAndContinentWhileConnectedIntegrationTest() {
        // Call app.query() with a query and error message
        ResultSet rset = app.query("SELECT Name, Continent, Population FROM country LIMIT 5;", "error message");

        ArrayList<Country> countries = app.handleResultSetCountryWithNamePopulationAndContinent(rset, "error message");

        assertEquals(5, countries.size());

        countries.forEach(C -> {
            assertNull(C.country_code);
            assertNotNull(C.name);
            assertNotNull(C.continent);
            assertNull(C.region);
            assertNull(C.capital_city);
            assertNotEquals(-1, C.population);
        });
    }

    /** handleResultSetCountryWithNamePopulationAndContinentErrorIntegrationTest
     *  This test tests the App.handleResultSetCountryWithNamePopulationAndContinent method.
     *  The method should return null when an error occurs
     *  Added by Eoin K:01/04/21
     */
    @Test
    void handleResultSetCountryWithNamePopulationAndContinentErrorIntegrationTest() {
        // Call app.query() with a query and error message to get a mock result set
        ResultSet rset = app.query("SELECT Name, Population FROM country LIMIT 5;", "error message");

        ArrayList<Country> countries = app.handleResultSetCountryWithNamePopulationAndContinent(rset, "error message");

        assertNull(countries);
    }

    /** handleResultSetCityWithNameDistrictAndPopulationWhileConnectedIntegrationTest
     *  This test tests the App.handleResultSetCityWithNameDistrictAndPopulation method.
     *  The method should take the result of a query (ResultSet) and populates an array list of city objects
     *  Added by Eoin K:01/04/21
     */
    @Test
    void handleResultSetCityWithNameDistrictAndPopulationWhileConnectedIntegrationTest() {
        // Call app.query() with a query and error message
        ResultSet rset = app.query("SELECT Name, Population, District FROM city LIMIT 5;", "error message");

        ArrayList<City> cities = app.handleResultSetCityWithNameDistrictAndPopulation(rset, "error message");

        assertEquals(5, cities.size());

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNull(C.continent);
            assertNull(C.region);
            assertNotNull(C.district);
            assertNotEquals(-1, C.population);
            assertNull(C.country);
        });
    }

    /** handleResultSetCityWithNameDistrictAndPopulationErrorIntegrationTest
     *  This test tests the App.handleResultSetCityWithNameDistrictAndPopulation method.
     *  The method should return null when an error occurs
     *  Added by Eoin K:01/04/21
     */
    @Test
    void handleResultSetCityWithNameDistrictAndPopulationErrorIntegrationTest() {
        // Call app.query() with a query and error message to get a mock result set
        ResultSet rset = app.query("SELECT Name, Population FROM city LIMIT 5;", "error message");

        ArrayList<City> cities = app.handleResultSetCityWithNameDistrictAndPopulation(rset, "error message");

        assertNull(cities);
    }

    /** handleResultSetCityWithNameContinentAndPopulationWhileConnectedIntegrationTest
     *  This test tests the App.handleResultSetCityWithNameContinentAndPopulation method.
     *  The method should take the result of a query (ResultSet) and populates an array list of city objects
     *  Added by Eoin K:01/04/21
     */
    @Test
    void handleResultSetCityWithNameContinentAndPopulationWhileConnectedIntegrationTest() {
        // Call app.query() with a query and error message
        ResultSet rset = app.query("SELECT city.Name AS city_name, country.continent AS continent, " +
                "city.Population AS population FROM city JOIN country ON city.CountryCode = country.Code LIMIT 5;",
                "error message"
        );

        ArrayList<City> cities = app.handleResultSetCityWithNameContinentAndPopulation(rset, "error message");

        assertEquals(5, cities.size());

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNotNull(C.continent);
            assertNull(C.region);
            assertNull(C.district);
            assertNotEquals(-1, C.population);
            assertNull(C.country);
        });
    }

    /** handleResultSetCityWithNameContinentAndPopulationErrorIntegrationTest
     *  This test tests the App.handleResultSetCityWithNameContinentAndPopulation method.
     *  The method should return null when an error occurs
     *  Added by Eoin K:01/04/21
     */
    @Test
    void handleResultSetCityWithNameContinentAndPopulationErrorIntegrationTest() {
        // Call app.query() with a query and error message to get a mock result set
        ResultSet rset = app.query("SELECT Name, Population FROM city LIMIT 5;", "error message");

        ArrayList<City> cities = app.handleResultSetCityWithNameContinentAndPopulation(rset, "error message");

        assertNull(cities);
    }

    /** handleResultSetCityWithNameRegionAndPopulationWhileConnectedIntegrationTest
     *  This test tests the App.handleResultSetCityWithNameRegionAndPopulation method.
     *  The method should take the result of a query (ResultSet) and populates an array list of city objects
     *  Added by Eoin K:01/04/21
     */
    @Test
    void handleResultSetCityWithNameRegionAndPopulationWhileConnectedIntegrationTest() {
        // Call app.query() with a query and error message
        ResultSet rset = app.query("SELECT city.Name AS city_name, country.region AS region, " +
                        "city.Population AS population FROM city JOIN country ON city.CountryCode = country.Code LIMIT 5;",
                "error message"
        );

        ArrayList<City> cities = app.handleResultSetCityWithNameRegionAndPopulation(rset, "error message");

        assertEquals(5, cities.size());

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNull(C.continent);
            assertNotNull(C.region);
            assertNull(C.district);
            assertNotEquals(-1, C.population);
            assertNull(C.country);
        });
    }

    /** handleResultSetCityWithNameRegionAndPopulationErrorIntegrationTest
     *  This test tests the App.handleResultSetCityWithNameRegionAndPopulation method.
     *  The method should return null when an error occurs
     *  Added by Eoin K:01/04/21
     */
    @Test
    void handleResultSetCityWithNameRegionAndPopulationErrorIntegrationTest() {
        // Call app.query() with a query and error message to get a mock result set
        ResultSet rset = app.query("SELECT Name, Population FROM city LIMIT 5;", "error message");

        ArrayList<City> cities = app.handleResultSetCityWithNameRegionAndPopulation(rset, "error message");

        assertNull(cities);
    }

    /** generateCountryReportWhileConnectedIntegrationTest
     *  This test tests the App.generateCountryReport method.
     *  Should test that the method takes the result of a query (ResultSet) and fully populates an array list of country objects
     *  Added by Eoin K:21/04/21
     */
    @Test
    void generateCountryReportWhileConnectedIntegrationTest() {
        // Call app.query() with a query and error message
        String query = "SELECT Code, country. Name, Continent, Region, country.Population, "
                + "city.Name AS capital_city_name FROM country JOIN city ON country.Capital = city.ID LIMIT 1;";
        ResultSet rset = app.query(query,"error message");
        ResultSet testRset = app.query(query,"error message");;

        ArrayList<Country> countries = app.generateCountryReport(rset, "error message");

        assertEquals(1, countries.size());


        countries.forEach(C -> {
            try {
                testRset.next();
                assertEquals(testRset.getString("code"), C.country_code);
                assertEquals(testRset.getString("name"), C.name);
                assertEquals(Country.Continents.customValueOf(testRset.getString("continent")), C.continent);
                assertEquals(testRset.getString("region"), C.region);
                assertEquals(testRset.getInt("population"), C.population);
                assertEquals(testRset.getString("capital_city_name"), C.capital_city.name);
            } catch (SQLException error) {
                fail(error);
            }
        });
    }

    /** generateCountryReportErrorIntegrationTest
     *  This test tests the App.generateCountryReport method.
     *  The method should return null when an error occurs
     *  Added by Eoin K:21/04/21
     */
    @Test
    void generateCountryReportErrorIntegrationTest() {
        // Call app.query() with a query and error message to get a mock result set
        ResultSet rset = app.query("SELECT Name FROM city LIMIT 1;", "error message");

        ArrayList<Country> countries = app.generateCountryReport(rset, "error message");

        assertNull(countries);
    }

    /** test the getCountryLargestToSmallest method in App.java
     *  Should test that the method returns an array of countries and each country has a name and population attribute.
     */
    @Test
    void getCountryLargestToSmallestIntegrationTest() {
        ArrayList<Country> countries = app.getCountryLargestToSmallest();

        assertTrue(countries.size() >= 1);

        countries.forEach(C -> {
            assertNull(C.country_code);
            assertNotNull(C.name);
            assertNull(C.continent);
            assertNull(C.region);
            assertNull(C.capital_city);
            assertNotEquals(-1, C.population);
        });
    }

    /**
     * Integration Test for getCountriesInARegionByPopulation method in App.java
     * Should test that the method generates all the countries in a region organised by largest population to smallest.
     * Added by Eoin K: 31/03/21
     */
    @Test
    void getCountriesInARegionByPopulationIntegrationTest() {
        ArrayList<Country> countries = app.getCountriesInARegionByPopulation();

        assertTrue(countries.size() >= 1);

        countries.forEach(C -> {
            assertNull(C.country_code);
            assertNotNull(C.name);
            assertNull(C.continent);
            assertNotNull(C.region);
            assertNull(C.capital_city);
            assertNotEquals(-1, C.population);
        });
    }

    /**
     * Integration Test for getTopNPopulatedCountriesInAContinent method in App.java
     * Should test that the method generates the top N populated countries in a continent.
     * The method should return countries with a name, continent and population attribute.
     * Added by Eoin K: 31/03/21
     */
    @Test
    void getTopNPopulatedCountriesInAContinentValidInputIntegrationTest() {
        ArrayList<Country> countries = app.getTopNPopulatedCountriesInAContinent(1);

        assertTrue(countries.size() >= 1);

        countries.forEach(C -> {
            assertNull(C.country_code);
            assertNotNull(C.name);
            assertNotNull(C.continent);
            assertNull(C.region);
            assertNull(C.capital_city);
            assertNotEquals(-1, C.population);
        });
    }

    /**
     * Integration Test for getTopNPopulatedCountriesInAContinent method in App.java with invalid input.
     * Should test that the method handles invalid given numbers.
     * Added by Eoin K: 31/03/21
     */
    @Test
    void getTopNPopulatedCountriesInAContinentInvalidInputIntegrationTest() {
        ArrayList<Country> countries = app.getTopNPopulatedCountriesInAContinent(-1);

        assertNull(countries);
    }

    /**
     * Integration Test for getTopNPopulatedCountriesInARegion method in App.java
     * Should test that the method generates the top N populated countries in a region.
     * The method should return countries with a name, region and population attribute.
     * Added by Eoin K: 31/03/21
     */
    @Test
    void getTopNPopulatedCountriesInARegionValidInputIntegrationTest() {
        ArrayList<Country> countries = app.getTopNPopulatedCountriesInARegion(1);

        assertTrue(countries.size() >= 1);

        countries.forEach(C -> {
            assertNull(C.country_code);
            assertNotNull(C.name);
            assertNull(C.continent);
            assertNotNull(C.region);
            assertNull(C.capital_city);
            assertNotEquals(-1, C.population);
        });
    }

    /**
     * Integration Test for getTopNPopulatedCountriesInARegion method in App.java with invalid input.
     * Should test that the method handles invalid given numbers.
     * Added by Eoin K: 31/03/21
     */
    @Test
    void getTopNPopulatedCountriesInARegionInvalidInputIntegrationTest() {
        ArrayList<Country> countries = app.getTopNPopulatedCountriesInARegion(-1);

        assertNull(countries);
    }

    /**
     * Integration Test for getTopNPopulatedCitiesintheWorld method in App.java
     * Should test that the method generates the top N populated cities in the world.
     * The method should return cities with a name, district and population attribute.
     */
    @Test
    void getTopNPopulatedCitiesintheWorldValidInputIntegrationTest() {
        ArrayList<City> cities = app.getTopNPopulatedCitiesintheWorld(1);

        assertTrue(cities.size() >= 1);

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNotNull(C.district);
            assertNotEquals(-1, C.population);
            assertNull(C.continent);
            assertNull(C.country);
            assertNull(C.region);
        });
    }

    /**
     * Integration Test for getTopNPopulatedCitiesintheWorld method in App.java with invalid input.
     * Should test that the method handles invalid given numbers.
     */
    @Test
    void getTopNPopulatedCitiesintheWorldInvalidInputIntegrationTest() {
        ArrayList<City> cities = app.getTopNPopulatedCitiesintheWorld(-1);

        assertNull(cities);
    }

    /**
     * Integration Test for getTopNPopulatedCountriesInTheWorld method in App.java
     * Should test that the method generates the top N populated countries in the world where N is given.
     * The method should return countries with a name and population attribute.
     */
    @Test
    void getTopNPopulatedCountriesInTheWorldValidInputIntegrationTest() {
        ArrayList<Country> countries = app.getTopNPopulatedCountriesInTheWorld(1);

        assertTrue(countries.size() >= 1);

        countries.forEach(C -> {
            assertNull(C.country_code);
            assertNotNull(C.name);
            assertNull(C.continent);
            assertNull(C.region);
            assertNull(C.capital_city);
            assertNotEquals(-1, C.population);
        });
    }

    /**
     * Integration Test for getTopNPopulatedCountriesInTheWorld method in App.java with invalid input.
     * Should test that the method handles invalid given numbers.
     */
    @Test
    void getTopNPopulatedCountriesInTheWorldInvalidInputIntegrationTest() {
        ArrayList<Country> countries = app.getTopNPopulatedCountriesInTheWorld(-1);

        assertNull(countries);
    }

    /**
     * Integration Test for getCountriesInAContinentByPopulation method in App.java
     * Should test that the method generates all the countries in a continent organised by largest population to smallest.
     * The method should return countries with a name, continent and population attribute.
     */
    @Test
    void getCountriesInAContinentByPopulationIntegrationTest() {
        ArrayList<Country> countries = app.getCountriesInAContinentByPopulation();

        assertTrue(countries.size() >= 1);

        countries.forEach(C -> {
            assertNull(C.country_code);
            assertNotNull(C.name);
            assertNotNull(C.continent);
            assertNull(C.region);
            assertNull(C.capital_city);
            assertNotEquals(-1, C.population);
        });
    }

    /**
     * Integration Test for getTopNPopulatedCitiesinaContinent method in App.java
     * Should test that the method generates the top N populated cities in a continent.
     * The method should return cities with a name, continent and population attribute.
     */
    @Test
    void getTopNPopulatedCitiesinaContinentValidInputIntegrationTest() {
        ArrayList<City> cities = app.getTopNPopulatedCitiesinaContinent(1);

        assertTrue(cities.size() >= 1);

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNull(C.district);
            assertNotEquals(-1, C.population);
            assertNotNull(C.continent);
            assertNull(C.country);
            assertNull(C.region);
        });
    }

    /**
     * Integration Test for getTopNPopulatedCitiesinaContinent method in App.java with invalid input.
     * Should test that the method handles invalid given numbers.
     */
    @Test
    void getTopNPopulatedCitiesinaContinentInvalidInputIntegrationTest() {
        ArrayList<City> cities = app.getTopNPopulatedCitiesinaContinent(-1);

        assertNull(cities);
    }

    /**
     * Integration Test for getAllCitiesinADistrictLargetoSmall method in App.java
     * Should test that the method generates all the cities in a district organised by largest population to smallest.
     * The method should return countries with a name, district and population attribute.
     */
    @Test
    void getAllCitiesinADistrictLargetoSmallIntegrationTest() {
        ArrayList<City> cities = app.getAllCitiesinADistrictLargetoSmall();

        assertTrue(cities.size() >= 1);

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNotNull(C.district);
            assertNotEquals(-1, C.population);
            assertNull(C.continent);
            assertNull(C.country);
            assertNull(C.region);
        });
    }

    /**
     * Integration Test for getAllCitiesInACountry method in App.java
     * Should test that the method generates all the cities in a country organised by largest population to smallest.
     * The method should return countries with a name, district and population attribute.
     */
    @Test
    void getAllCitiesInACountryIntegrationTest() {
        ArrayList<City> cities = app.getAllCitiesInACountry();

        assertTrue(cities.size() >= 1);

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNull(C.district);
            assertNotEquals(-1, C.population);
            assertNull(C.continent);
            assertNotNull(C.country);
            assertNull(C.region);
        });
    }

    /**
     * Integration Test for getAllCitiesInAContinent method in App.java
     * Should test that the method generates all the cities in a continent organised by largest population to smallest.
     * The method should return countries with a name, continent and population attribute.
     */
    @Test
    void getAllCitiesInAContinentIntegrationTest() {
        ArrayList<City> cities = app.getAllCitiesInAContinent();

        assertTrue(cities.size() >= 1);

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNull(C.district);
            assertNotEquals(-1, C.population);
            assertNotNull(C.continent);
            assertNull(C.country);
            assertNull(C.region);
        });
    }

    /**
     * Integration Test for getAllCitiesInARegion method in App.java
     * Should test that the method generates all the cities in a region organised by largest population to smallest.
     * The method should return countries with a name, region and population attribute.
     */
    @Test
    void getAllCitiesInARegionIntegrationTest() {
        ArrayList<City> cities = app.getAllCitiesInARegion();

        assertTrue(cities.size() >= 1);

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNull(C.district);
            assertNotEquals(-1, C.population);
            assertNull(C.continent);
            assertNull(C.country);
            assertNotNull(C.region);
        });
    }

    /**
     * Integration Test for getAllCitiesInTheWorld method in App.java
     * Should test that the method generates all the cities in the world organised by largest population to smallest.
     * The method should return countries with a name and population attribute.
     */
    @Test
    void getAllCitiesInTheWorldIntegrationTest() {
        ArrayList<City> cities = app.getAllCitiesInTheWorld();

        assertTrue(cities.size() >= 1);

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNull(C.district);
            assertNotEquals(-1, C.population);
            assertNull(C.continent);
            assertNull(C.country);
            assertNull(C.region);
        });
    }

    /**
     * Integration Test for getCountryReports method in App.java
     * Should test that each country has a country code, name, continent, region and population attribute
     * Added by Eoin K: 14/03/21
     */
    @Test
    void CountryReportIntegrationTest() {
        ArrayList<Country> countries = app.getCountryReports();

        assertEquals(232, countries.size());

        countries.forEach(C -> {
            assertNotNull(C.country_code);
            assertNotNull(C.name);
            assertNotNull(C.continent);
            assertNotNull(C.region);
            assertNotEquals(-1, C.population);
            assertTrue(C.capital_city instanceof City);
        });
    }

    /**
     * Integration Test for getCityReports method in App.java
     * Should test that the method returns cities with a name, country, district and population.
     * Added by Eoin K: 14/03/21
     */
    @Test
    void CityReportIntegrationTest() {
        ArrayList<City> cities = app.getCityReports();

        assertEquals(4079, cities.size());

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNotNull(C.country);
            assertNotNull(C.district);
            assertNotEquals(-1, C.population);
        });
    }

    /**
     * Integration Test for getWorldPopulation method in App.java
     * Added by Eoin K: 14/03/21
     */
    @Test
    void WorldPopulationIntegrationTest() {
        Map<String, Number> worldPopulation = app.getWorldPopulation();

        assertEquals(1, worldPopulation.size());
        for (String i : worldPopulation.keySet()) {
            assertEquals("World", i);
            assertNotNull(worldPopulation.get(i));
        }
    }

    /**
     * Integration Test for getContinentPopulation method in App.java
     * Added by Eoin K: 14/03/21
     */
    @Test
    void ContinentPopulationIntegrationTest() {
        Map<String, Number> continentPopulation = app.getContinentPopulation();

        assertEquals(7, continentPopulation.size());
        for (String i : continentPopulation.keySet()) {
            assertNotNull(continentPopulation.get(i));
        }
    }

    /**
     * Integration Test for getRegionPopulation() method in App.java
     * Added by Jackson A: 22/03/21
     */
    @Test
    void RegionPopulationIntegrationTest() {
        Map<String, Number> regionPopulation = app.getRegionPopulation();

        assertEquals(25, regionPopulation.size());
        for (String i : regionPopulation.keySet()) {
            assertNotNull(regionPopulation.get(i));
        }
    }

    /**
     * #23 Integration Test for getTopNPopulatedCitiesinaRegion() method in App.java
     * Added by Joe B: 20/03/21
     */
    @Test
    void CitiesInARegion() {
        ArrayList<City> cities = app.getTopNPopulatedCitiesinaRegion(3);

        assertEquals(69, cities.size());

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNotNull(C.region);
            assertNotEquals(-1, C.population);
        });
    }

    /**
     * #23 Integration Test for getTopNPopulatedCitiesinaRegion() method in App.java
     * Testing to see if British Islands is shown when the method is called.
     * Added by Joe B: 20/03/21
     */
    @Test
    void CitiesInARegion1() {
        ArrayList<City> cities = app.getTopNPopulatedCitiesinaRegion(1);

        int numofbritresults = 0;

        for (int i = 0; i < cities.size(); i++) {

            City C = cities.get(i);

            if (C.region.equals("British Islands")) {
                ++numofbritresults;

            }
        }
        ;

        assertEquals(1, numofbritresults);
    }

    /**
     * Integration Test for getPopulatedAndUnpopulatedCities() method in App.java
     * Added by Jackson A: 22/03/21
     */
    @Test
    void CityAndNonCityPopulationIntegrationTest() {
        // populate a test list
        ArrayList<String> testLst = app.getPopulatedAndUnpopulatedCities();

        // Size must be 232
        assertEquals(232, testLst.size());

        // Make sure the string is not null when returned.
        testLst.forEach(C -> {
            assertNotNull(C);
        });
    }

    /**
     * #14 Integration Test for getTopNCapitalCitiesintheWorld() method in App.java
     * Added by Joe B: 21/03/21
     */

    @Test
    void CapitalCitiesInTheWorld() {
        ArrayList<City> cities = app.getTopNCapitalCitiesintheWorld(5);

        assertEquals(5, cities.size());

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNotNull(C.country);
            assertNotEquals(-1, C.population);
        });
    }

    /**
     * #14 Integration Test for getTopNCapitalCitiesintheWorld() method in App.java
     * Testing to see if Seoul is displayed when the method is called.
     * Added by Joe B: 21/03/21
     */
    @Test
    void CapitalCitiesInTheWorld1() {
        ArrayList<City> cities = app.getTopNCapitalCitiesintheWorld(1);

        int numofseoul = 0;

        for (int i = 0; i < cities.size(); i++) {

            City C = cities.get(i);

            if (C.name.equals("Seoul")) {
                ++numofseoul;

            }
        }
        assertEquals(1, numofseoul);

    }

    /**
     * #11 Integration Test for getTopNCapitalCitiesinaRegion() method in App.java
     * Added by Joe B: 22/03/21
     */
    @Test
    void CapitalCitiesInARegion() {
        ArrayList<City> cities = app.getTopNCapitalCitiesinaRegion(3);

        assertEquals(68, cities.size());

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNotNull(C.region);
            assertNotEquals(-1, C.population);
        });
    }

    /**
     * #11 Integration Test for getTopNCapitalCitiesinaRegion() method in App.java
     * Testing to see if the region Western Africa is displayed when the method is called.
     * Added by Joe B: 22/03/21
     */
    @Test
    void CapitalCitiesInARegion1() {
        ArrayList<City> cities = app.getTopNCapitalCitiesinaRegion(1);

        int numofregionname = 0;

        for (int i = 0; i < cities.size(); i++) {

            City C = cities.get(i);

            if (C.region.equals("Western Africa")) {
                ++numofregionname;

            }
        }
        assertEquals(1, numofregionname);
    }

    /**
     * Integration Test for getPopulatedAndUnpopulatedCities() method in App.java
     * Added by Jackson A: 22/03/21
     */
    @Test
    void CityAndNonCityPopulationIntegrationTestForContinent() {
        // populate a test list
        ArrayList<String> testLst = app.getPopulatedAndUnpopulatedCitiesForContinent();

        // Size must be 7
        assertEquals(7, testLst.size());

        // Make sure the string is not null when returned.
        testLst.forEach(C -> {
            assertNotNull(C);
        });
    }


    @Test
    void CapitalCitiesInAContinent() {
        ArrayList<City> continents = app.getTopNCapitalCitiesinAContinent(3);

        assertEquals(18, continents.size());

        continents.forEach(C -> {
            assertNotNull(C.name);
            assertNotNull(C.continent);
            assertNotEquals(-1, C.population);
        });
    }

    @Test
    void CapitalCitiesInAContinent1() {
        ArrayList<City> cities = app.getTopNCapitalCitiesinAContinent(1);

        int numofafrica = 0;

        for (int i = 0; i < cities.size(); i++) {

            City C = cities.get(i);
            if (C.continent.equals("Africa")) {
                ++numofafrica;


            }

        }
        assertEquals(1, numofafrica);
    }


    @Test
    void CityAndNonCityPopulationIntegrationTestForRegion() {
        // populate a test list
        ArrayList<String> testLst = app.getPopulatedAndUnpopulatedCitiesForRegion();

        // Size must be 25
        assertEquals(25, testLst.size());

        // Make sure the string is not null when returned.
        testLst.forEach(C -> {
            assertNotNull(C);
        });
    }


    @Test
    void CapitalCitiesInTheWorld18() {
        ArrayList<City> cities = app.getAllCapitalCitiesInTheWorld();

        assertEquals(232, cities.size());

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNotEquals(-1, C.population);
        });
    }


    @Test
    void CapitalCitiesInAContinent17() {
        ArrayList<City> cities = app.getAllCapitalCitiesInAContinent();

        assertEquals(232, cities.size());

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNotNull(C.continent);
            assertNotEquals(-1, C.population);
        });
    }

    @Test
    void CapitalCitiesInARegion16() {
        ArrayList<City> cities = app.getAllCapitalCitiesInARegion();

        assertEquals(232, cities.size());

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNotNull(C.region);
            assertNotEquals(-1, C.population);
        });
    }

    /** getLanguagesPopulationReportIntegrationTest
     *  This test tests the App.getLanguagesPopulationReport method.
     *  The method should generate the number of people who speak certain languages in number form and percentage form.
     *  This test should test that each row does not have more values than the number of headers, and the size of the report.
     *  Added by Eoin K:10/04/21
     */
    @Test
    void getLanguagesPopulationReportIntegrationTest() {
        ArrayList<String[]> report = app.getLanguagesPopulationReport();

        // The report is based off 5 languages, so 5 plus 1 row for headers is 6.
        assertEquals(6, report.size());

        // Test that each sub array does not have more values than the first
        int numOfHeaders = report.get(0).length;

        // Iterate over the report but start from counter 1 as the first sub array is the headers.
        for (int i = 1; i < report.size() ; i++) {
            String[] currentRecord = report.get(i);

            // Assert that the number of value from the first sub array is equal to the number of values in,
            //  the current sub array.
            assertEquals(numOfHeaders,currentRecord.length);
        }
    }
}