package com.napier.teamc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

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
}
