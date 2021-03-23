package com.napier.teamc;

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
}