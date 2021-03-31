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
     * Integration Test for getCountryReports method in App.java
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
            assertEquals(6078749450L, worldPopulation.get(i));
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
            assertTrue(continentPopulation.get(i) instanceof Number);
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
            assertTrue(regionPopulation.get(i) instanceof Number);
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
}