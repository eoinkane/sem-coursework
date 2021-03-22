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
    static void init()
    {
        app = new App();
        app.connect("localhost:33060");
    }

    /**
     * Integration Test for getCountryReports method in App.java
     * Added by Eoin K: 14/03/21
     */
    @Test
    void CountryReportIntegrationTest()
    {
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
    void CityReportIntegrationTest()
    {
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
    void WorldPopulationIntegrationTest()
    {
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
    void ContinentPopulationIntegrationTest()
    {
        Map<String, Number> continentPopulation = app.getContinentPopulation();

        assertEquals(7, continentPopulation.size());
        for (String i : continentPopulation.keySet()) {
            assertTrue(continentPopulation.get(i) instanceof Number);
        }
    }


    @Test
    void CitiesInARegion()
    {
        ArrayList<City> cities = app.getTopNPopulatedCitiesinaRegion(3);

        assertEquals(69, cities.size());

        cities.forEach(C -> {
            assertNotNull(C.name);
            assertNotNull(C.region);
            assertNotEquals(-1, C.population);
        });
    }

    @Test
    void CitiesInARegion1()
    {
        ArrayList<City> cities = app.getTopNPopulatedCitiesinaRegion(1);

        int numofbritresults = 0;

        for (int i = 0; i < cities.size(); i++) {

            City C = cities.get(i);

            if (C.region.equals("British Islands")) {
                ++numofbritresults;

            }
        };

        assertEquals(1, numofbritresults);
    }
}
