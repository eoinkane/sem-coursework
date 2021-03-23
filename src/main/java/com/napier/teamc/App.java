package com.napier.teamc;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/** Application Class
 * Holds the main logic of the program
 * Updated 15/02/21
 */
public class App
{
    /** con (Connection)
     * An object to hold a Connection to MySQL database.
     */
    private Connection con = null;

    /** connect (method)
     * Updated by Eoin K 14/03/21
     * Connect to the MySQL database through the SQL driver.
     * @param location database host section of URL
     */
    public void connect(String location)
    {
        /*  try catch statement to gather the required SQL driver
            application will exit if driver cannot be loaded
         */
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        // Allow 10 retries as the MySQL DB can take some time to start up
        int retries = 10;
        // A retry loop to connect to the DB
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location + "/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /** disconnect (method)
     * To allow the application to safely disconnect from the MySQL database.
     */
    public void disconnect()
    {
        /* An if statement to handle the application shutting down
           The application will only try to disconnect from the DB if a successful connection was made
         */
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * getCountryLargestToSmallest generates populated countries from largest to smallest,
     *      in the world.
     * Added by Jackson A:01/03/21
     * @return An array of countries, each country has a name and population attribute (ArrayList<Country>)
     */
    public ArrayList<Country> getCountryLargestToSmallest()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Population, Name "
                            + "FROM country "
                            + "ORDER BY Population desc";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country if valid.
            // Extract country information.
           ArrayList<Country> countries = new ArrayList<Country>();
           while (rset.next()) {
               Country cntry = new Country(
                       null,
                       rset.getString("name"),
                       null,
                       null,
                       rset.getInt("population"),
                       null
               );
               countries.add(cntry);
           }
            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    /**
     * getCountriesInARegionByPopulation generates all the countries,
     *      in a region organised by largest population to smallest.
     * Added by Eoin K:25/02/21
     * Updated by Eoin K:27/02/21 (Updated Country constructor to use null continent)
     * @return An array of countries, each country has a name, region and population attribute (ArrayList<Country>)
     */
    public ArrayList<Country> getCountriesInARegionByPopulation()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Name, Region, Population FROM country "
                            + "ORDER BY Region, Population DESC;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country if valid.
            // Extract country information.
            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                Country cntry = new Country(
                        null,
                        rset.getString("name"),
                        null,
                        rset.getString("region"),
                        rset.getInt("population"),
                        null
                );
                countries.add(cntry);
            }
            return countries;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get countries in a region by population");
            return null;
        }
    }

    /**
     * getTopNPopulatedCountriesInAContinent generates the top N populated countries,
     *      in a continent where N is given.
     * Added by Eoin K:27/02/21
     * @param n the given number of N populated countries to generate
     * @return An array of countries, each country has a name, continent and population attribute (ArrayList<Country>)
     */
    public ArrayList<Country> getTopNPopulatedCountriesInAContinent(int n)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "WITH grouped_countries AS (SELECT Name, Continent, Population, ROW_NUMBER() OVER "
                            + "(PARTITION BY Continent ORDER BY Population DESC) row_num FROM country) "
                            + "SELECT Name, Continent, Population FROM grouped_countries WHERE row_num <= <N>;";
            strSelect = strSelect.replace("<N>", String.valueOf(n));
            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country if valid.
            // Extract country information.
            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                Country cntry = new Country(
                        null,
                        rset.getString("name"),
                        Country.Continents.customValueOf(rset.getString("continent")),
                        null,
                        rset.getInt("population"),
                        null
                );
                countries.add(cntry);
            }
            return countries;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top N populated countries in a continent");
            return null;
        }
    }

    /**
     * getTopNPopulatedCountriesInARegion generates the top N populated countries,
     *      in a region where N is given.
     * Added by Eoin K:27/02/21
     * @param n the given number of N populated countries to generate
     * @return An array of countries, each country has a name, region and population attribute (ArrayList<Country>)
     */
    public ArrayList<Country> getTopNPopulatedCountriesInARegion(int n)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "WITH grouped_countries AS (SELECT Name, Region, Population, ROW_NUMBER() OVER "
                            + "(PARTITION BY Region ORDER BY Population DESC) row_num FROM country) "
                            + "SELECT Name, Region, Population FROM grouped_countries WHERE row_num <= <N>;";
            strSelect = strSelect.replace("<N>", String.valueOf(n));
            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information.
            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                Country cntry = new Country(
                        null,
                        rset.getString("name"),
                        null,
                        rset.getString("region"),
                        rset.getInt("population"),
                        null
                );
                countries.add(cntry);
            }
            // return results
            return countries;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top N populated countries in a region");
            return null;
        }
    }

    /**
     * getTopNPopulatedCitiesInTheWorld generates the top N populated cities,
     *      in the World where N is given.
     * Added by Joe B: 27/02/21
     * @param n the given number of N populated cities to generate
     * @return An array of cities, each city has a name, district and population attribute (ArrayList<City>)
     */

    public ArrayList<City> getTopNPopulatedCitiesintheWorld(int n)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "WITH grouped_cities AS (SELECT Name, District, Population, ROW_NUMBER() OVER "
                            + "(ORDER BY Population DESC) row_num FROM city) "
                            + "SELECT Name, District, Population FROM grouped_cities WHERE row_num <= <N>;";
            strSelect = strSelect.replace("<N>", String.valueOf(n));
            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City cty = new City(
                        rset.getString("name"),
                        rset.getString("district"),
                        rset.getInt("population"),
                        null,
                        null,
                        null
                );
                cities.add(cty);
            }
            // return results
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top N populated cities in the world.");
            return null;
        }
    }

    /**
     * getTopNPopulatedCountriesInTheWorld generates the top N populated countries,
     *      in the world where N is given.
     * Added by Jackson A:01/03/21
     * @param n the given number of N populated countries to generate
     * @return An array of countries, each country has a name and population attribute (ArrayList<Country>)
     */
    public ArrayList<Country> getTopNPopulatedCountriesInTheWorld(int n)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "WITH all_countries AS (SELECT Name, Population, ROW_NUMBER() OVER "
                            + "(ORDER BY Population DESC) row_num FROM country) "
                            + "SELECT Name, Population FROM all_countries WHERE row_num <= <N>;";
            strSelect = strSelect.replace("<N>", String.valueOf(n));
            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information.
            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                Country cntry = new Country(
                        null,
                        rset.getString("name"),
                        null,
                        null,
                        rset.getInt("population"),
                        null
                );
                countries.add(cntry);
            }
            // return results
            return countries;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top N populated countries in the World.");
            return null;
        }
    }

    /**
     * getCountriesInContinentByPopulation generates all the countries,
     *      in a continent organised by largest population to smallest.
     * Added by Jackson A:01/03/21
     * @return An array of countries, each country has a name, continent and population attribute (ArrayList<Country>)
     */
    public ArrayList<Country> getCountriesInAContinentByPopulation()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Name, Continent, Population FROM country "
                            + "ORDER BY Continent, Population DESC;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country if valid.
            // Extract country information.
            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                Country cntry = new Country(
                        null,
                        rset.getString("name"),
                        Country.Continents.customValueOf(rset.getString("continent")),
                        null,
                        rset.getInt("population"),
                        null
                );
                countries.add(cntry);
            }
            return countries;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get countries in a continent by population");
            return null;
        }
    }

    /**
     * getTopNPopulatedCitiesInAContinent generates the top N populated cities,
     *      in a Continent where N is given.
     * Added by Joe B: 01/03/21
     * @param n the given number of N populated cities to generate
     * @return An array of cities, each city has a name population and continent attribute (ArrayList<City>)
     */

    public ArrayList<City> getTopNPopulatedCitiesinaContinent(int n)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "WITH grouped_cities AS (SELECT city.Name AS city_name, country.Continent AS Continent, city.Population, ROW_NUMBER() OVER "
                            + "(PARTITION BY Continent ORDER BY Population DESC) row_num FROM city "
                            + "JOIN country ON city.CountryCode = country.Code) "
                            + "SELECT city_name, Continent, Population FROM grouped_cities WHERE row_num <= <N>;";
            strSelect = strSelect.replace("<N>", String.valueOf(n));
            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City cty = new City(
                        rset.getString("city_name"),
                        null,
                        rset.getInt("population"),
                        rset.getString("continent"),
                        null,
                        null

                );
                cities.add(cty);
            }
            // return results
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top N populated cities in a continent.");
            return null;
        }
    }

    public ArrayList<City> getAllCitiesinADistrictLargetoSmall()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Name, District, Population FROM city "
                    + "ORDER BY District, Population DESC;";


            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract city information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City cty = new City(
                        rset.getString("name"),
                        rset.getString("district"),
                        rset.getInt("population"),
                        null,
                        null,
                        null
                );
                cities.add(cty);
            }
            // return results
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all cities in a district.");
            return null;
        }
    }
    public ArrayList<City> getAllCitiesInACountry()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name AS city_name, country.name AS country_name, city.Population AS population FROM city "
            + "JOIN country ON city.CountryCode = country.Code "
            + "ORDER BY country_name, population DESC; ";

            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City cty = new City(
                        rset.getString("city_name"),
                        null,
                        rset.getInt("population"),
                        null,
                        rset.getString("country_name"),
                        null

                );
                cities.add(cty);
            }
            // return results
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all cities in a country");
            return null;
        }
    }

    public ArrayList<City> getAllCitiesInAContinent()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name AS city_name, country.continent AS continent, city.Population AS population FROM city "
                            + "JOIN country ON city.CountryCode = country.Code "
                            + "ORDER BY continent, population DESC; ";

            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City cty = new City(
                        rset.getString("city_name"),
                        null,
                        rset.getInt("population"),
                        rset.getString("continent"),
                        null,
                        null

                );
                cities.add(cty);
            }
            // return results
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all cities in a continent");
            return null;
        }
    }

    public ArrayList<City> getAllCitiesInARegion()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name AS city_name, country.region AS region, city.Population AS population FROM city "
                            + "JOIN country ON city.CountryCode = country.Code "
                            + "ORDER BY region, population DESC; ";

            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City cty = new City(
                        rset.getString("city_name"),
                        null,
                        rset.getInt("population"),
                        null,
                        null,
                        rset.getString("region")

                );
                cities.add(cty);
            }
            // return results
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all cities in a region");
            return null;
        }
    }

    public ArrayList<City> getAllCitiesInTheWorld()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name AS city_name, city.Population AS population FROM city "
                            + "JOIN country ON city.CountryCode = country.Code "
                            + "ORDER BY population DESC; ";

            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City cty = new City(
                        rset.getString("city_name"),
                        null,
                        rset.getInt("population"),
                        null,
                        null,
                        null

                );
                cities.add(cty);
            }
            // return results
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all cities in the world");
            return null;
        }
    }

    /**
     * getCountryReports generates all the countries and collates them into an ArrayList<Country>
     * Added by Eoin K:14/03/21
     * @return An array of countries, each country has a country code, name, continent, region, population
     *      and capital city attribute (ArrayList<Country>)
     */
    public ArrayList<Country> getCountryReports()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, country.Name, country.Continent, country.Region, country.Population, "
                            + "capital_city.Name as `capital_city_name` FROM country JOIN city capital_city ON "
                            + "capital_city.ID = country.Capital";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country if valid.
            // Extract country information.
            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next()) {
                City capital_city = new City(
                        rset.getString("capital_city_name"),
                        null,
                        -1,
                        null,
                        rset.getString("name"),
                        null
                );
                Country cntry = new Country(
                        rset.getString("code"),
                        rset.getString("name"),
                        Country.Continents.customValueOf(rset.getString("continent")),
                        rset.getString("region"),
                        rset.getInt("population"),
                        capital_city
                );
                countries.add(cntry);
            }
            return countries;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get countries report");
            return null;
        }
    }

    /**
     * getCityReports generates all the cities and collates them into an ArrayList<City>
     * Added by Eoin K:14/03/21
     * @return An array of countries, each country has a name, country, district and population (ArrayList<City>)
     */
    public ArrayList<City> getCityReports()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, cntry.name as `country`, city.district, city.population FROM city JOIN country "
                            + "cntry ON cntry.Code = city.CountryCode";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country if valid.
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City city = new City(
                        rset.getString("name"),
                        rset.getString("district"),
                        rset.getInt("population"),
                        null,
                        rset.getString("country"),
                        null
                );

                cities.add(city);
            }
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city reports");
            return null;
        }
    }

    /**
     * getWorldPopulation generates the population of the world
     * Added by Eoin K:14/03/21
     * @return An hashmap containing one item, key: "world" value: <Long world population from database>
     */
    public HashMap<String, Number> getWorldPopulation()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT SUM(Population) AS `world_population` FROM country;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country if valid.
            // Extract country information.
            HashMap<String, Number> worldPopulation = new HashMap<String, Number>();
            if (rset.next()) {
                worldPopulation.put("World", rset.getLong("world_population"));
            }
            return worldPopulation;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get world population");
            return null;
        }
    }

    /**
     * getWorldPopulation generates the population of the world
     * Added by Eoin K:14/03/21
     * @return An hashmap containing one item, key: "world" value: <Long world population from database>
     */
    public HashMap<String, Number> getContinentPopulation()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Continent, SUM(Population) AS `continent_population` FROM country GROUP BY Continent;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country if valid.
            // Extract country information.
            HashMap<String, Number> worldPopulation = new HashMap<String, Number>();
            while (rset.next()) {
                Number value;
                if (rset.getLong("continent_population") <= Integer.MAX_VALUE) {
                    value = rset.getInt("continent_population");
                } else {
                    value = rset.getLong("continent_population");
                }
                worldPopulation.put(rset.getString("continent"), value);
            }
            return worldPopulation;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get world population");
            return null;
        }
    }

    /**
     * getTopNPopulatedCitiesInARegion generates the top N populated cities,
     *      in a Region where N is given.
     * Added by Joe B: 20/03/21
     * @param n the given number of N populated cities to generate
     * @return An array of cities, each city has a name population and region attribute (ArrayList<City>)
     */

    public ArrayList<City> getTopNPopulatedCitiesinaRegion(int n)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "WITH grouped_cities AS (SELECT city.Name AS city_name, country.Region AS Region, city.Population, ROW_NUMBER() OVER "
                            + "(PARTITION BY Region ORDER BY Population DESC) row_num FROM city "
                            + "JOIN country ON city.CountryCode = country.Code) "
                            + "SELECT city_name, Region, Population FROM grouped_cities WHERE row_num <= <N>;";
            strSelect = strSelect.replace("<N>", String.valueOf(n));
            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City cty = new City(
                        rset.getString("city_name"),
                        null,
                        rset.getInt("population"),
                        null,
                        null,
                        rset.getString("region")

                );
                cities.add(cty);
            }
            // return results
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top N populated cities in a region.");
            return null;
        }
    }

    /**
     * getTopNPopulatedCitiesInACountry generates the top N populated cities,
     *      in a Country where N is given.
     * Added by Joe B: 21/03/21
     * @param n the given number of N populated cities to generate
     * @return An array of cities, each city has a name population and country attribute (ArrayList<City>)
     */
    public ArrayList<City> getTopNPopulatedCitiesinaCountry(int n)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "WITH grouped_cities AS (SELECT city.Name AS city_name, c.name AS Name, city.Population, ROW_NUMBER() OVER "
                            + "(PARTITION BY Name ORDER BY Population DESC) row_num FROM city "
                            + "JOIN country c ON city.CountryCode = c.Code) "
                            + "SELECT city_name, Name, Population FROM grouped_cities WHERE row_num <= <N>;";
            strSelect = strSelect.replace("<N>", String.valueOf(n));
            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City cty = new City(
                        rset.getString("city_name"),
                        null,
                        rset.getInt("population"),
                        null,
                        rset.getString("name"),
                        null

                );
                cities.add(cty);
            }
            // return results
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top N populated cities in a country.");
            return null;
        }
    }

    /**
     * getRegionPopulation generates the population of each region
     * Added by Jackson A:22/03/21
     * @return An hashmap containing one item, key: "world" value: <Long world population from database>
     */
    public HashMap<String, Number> getRegionPopulation()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT region, SUM(Population) AS `region_population` FROM country GROUP BY region;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new region info if valid.
            // Extract region information.
            HashMap<String, Number> regionPopulation = new HashMap<String, Number>();
            while (rset.next()) {
                Number value;
                if (rset.getLong("region_population") <= Integer.MAX_VALUE) {
                    value = rset.getInt("region_population");
                } else {
                    value = rset.getLong("region_population");
                }
                regionPopulation.put(rset.getString("region"), value);
            }
            return regionPopulation;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get region population");
            return null;
        }
    }
    /**
     * getTopNCapitalCitiesintheWorld generates all the capital cities in the world and collates them into an ArrayList<Country>
     * Added by Joe B: 22/03/21
     * @return An array of capital cities, each capital city has a country, city name and population
     */
    public ArrayList<City> getTopNCapitalCitiesintheWorld(int n)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "WITH grouped_cities AS (SELECT country.Name AS Name, capital_city.Name AS `capital_city_name`, capital_city.Population AS `capital_city_population`, ROW_NUMBER() OVER "
                            + "(ORDER BY capital_city.Population DESC) row_num FROM country "
                            + "JOIN city capital_city ON capital_city.ID = country.Capital) "
                            + "SELECT Name, capital_city_name, capital_city_population FROM grouped_cities WHERE row_num <= <N>;";

            strSelect = strSelect.replace("<N>", String.valueOf(n));
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country if valid.
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City capital_city = new City(
                        rset.getString("capital_city_name"),
                        null,
                        rset.getInt("capital_city_population"),
                        null,
                        rset.getString("name"),
                        null
                );

                cities.add(capital_city);
            }
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get top n populated cities in the world.");
            return null;
        }
    }

    /**
     * getTopNPopulatedCitiesInACountry generates the top N populated cities,
     *      in a Country where N is given.
     * Added by Jackson A: 22/03/21
     * @return An array of strings, each string stores data of that which cannot be stored in any object such as specific data passed from the database.
     */
    public ArrayList<String> getPopulatedAndUnpopulatedCities() {

        String x = null;

        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Code AS 'country_code', country.Name AS 'country_name', country.Population AS 'population', in_cities.population_in_cities, (country.Population - in_cities.population_in_cities) AS 'population_not_in_cities' FROM country "
                            + "JOIN (SELECT CountryCode, COUNT(*) AS 'no_of_cities', SUM(Population) AS 'population_in_cities' FROM city "
                            + "GROUP BY CountryCode) in_cities ON in_cities.CountryCode = country.Code;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            ArrayList<String> info = new ArrayList<String>();
            // Display
            while (rset.next()) {
                // Generate string
                x = ("Name " + rset.getString("country_name") + " Total population " + rset.getString("population") + " Population in cities: " + rset.getInt("population_in_cities") + " Population not in a city: " + rset.getString("population_not_in_cities"));
                info.add(x);
            }
            return info;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the population information in and out of cities.");
            return null;
        }
    }

    /**
     * getTopNPopulatedCitiesForContinent() find the population of people living in and out of cities,
     *      For each Continent
     * Added by Jackson A: 23/03/21
     * @return An array of strings, each string stores data of that which cannot be stored in any object such as specific data passed from the database.
     */
    public ArrayList<String> getPopulatedAndUnpopulatedCitiesForContinent() {

        String x = null;

        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT in_continents.continent AS 'continent_name', in_continents.continent_population, in_cities.population_in_cities, (in_continents.continent_population - in_cities.population_in_cities) AS 'population_not_in_cities' FROM (SELECT Continent AS 'continent' , SUM(Population) AS 'continent_population' FROM country GROUP BY Continent) in_continents "
                            + "LEFT JOIN (SELECT country.Continent AS 'continent', SUM(city.Population) AS 'population_in_cities' FROM city "
                            + "JOIN country ON city.CountryCode = country.Code GROUP BY Continent) in_cities ON in_cities.continent = in_continents.continent;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            ArrayList<String> ContinentInfo = new ArrayList<String>();
            // Display
            while (rset.next()) {
                // Generate string
                x = ("Name " + rset.getString("continent_name") + " Total population " + rset.getString("continent_population") + " Population in cities: " + rset.getInt("population_in_cities") + " Population not in a city: " + rset.getString("population_not_in_cities"));
                ContinentInfo.add(x);
            }
            return ContinentInfo;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the population information in and out of cities for each Continent.");
            return null;
        }
    }


    /* displayBasicStringArray takes a list of strings and outputs them.
     * Added by Jackson A:22/03/21
     */
    public void displayBasicStringArray(ArrayList<City> populations)
    {
        populations.forEach(singleString -> System.out.println(singleString));
    }

    public ArrayList<City> getTopNCapitalCitiesinaRegion(int n)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "WITH grouped_cities AS (SELECT country.Region AS Region, capital_city.Name AS `capital_city_name`, capital_city.Population AS `capital_city_population`, ROW_NUMBER() OVER "
                            + "(PARTITION BY Region ORDER BY capital_city.Population DESC) row_num FROM country "
                            + "JOIN city capital_city ON capital_city.ID = country.Capital) "
                            + "SELECT Region, capital_city_name, capital_city_population FROM grouped_cities WHERE row_num <= <N>;";

            strSelect = strSelect.replace("<N>", String.valueOf(n));
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country if valid.
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City capital_city = new City(
                        rset.getString("capital_city_name"),
                        null,
                        rset.getInt("capital_city_population"),
                        null,
                        null,
                        rset.getString("region")
                );

                cities.add(capital_city);
            }
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get top n populated cities in the world.");
            return null;
        }
    }
    /**
     * getTopNPopulatedCitiesInAContinent generates the top N populated cities,
     *      in a Country where N is given.
     * Added by Robbie M: 22/03/21
     * @return An array of strings, each string stores data of that which cannot be stored in any object such as specific data passed from the database.
     */
    public ArrayList<City> getTopNCapitalCitiesinAContinent(int n)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "WITH grouped_cities AS (SELECT country.Continent AS Continent, capital_city.Name AS `capital_city_name`, capital_city.Population AS `capital_city_population`, ROW_NUMBER() OVER \n"
                            + "(PARTITION BY Continent ORDER BY capital_city.Population DESC) row_num FROM country \n"
                            + "JOIN city capital_city ON capital_city.ID = country.Capital) \n"
                            + "SELECT Continent, capital_city_name, capital_city_population FROM grouped_cities WHERE row_num <= <N>;";

            strSelect = strSelect.replace("<N>", String.valueOf(n));
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new country if valid.
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City capital_city = new City(
                        rset.getString("capital_city_name"),
                        null,
                        rset.getInt("capital_city_population"),
                        rset.getString("continent"),
                        null,
                        null
                );

                cities.add(capital_city);
            }
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get top n populated cities in ta continent");
            return null;
        }
    }

    public ArrayList<String> getPopulatedAndUnpopulatedCitiesForRegion() {

        String x = null;

        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT in_regions.region AS 'region_name', in_regions.region_population, in_cities.population_in_cities, (in_regions.region_population - in_cities.population_in_cities) AS 'population_not_in_cities' FROM (SELECT Region AS 'region' , SUM(Population) AS 'region_population' \n" +
                            "FROM country GROUP BY Region) in_regions "
                            +"LEFT JOIN (SELECT country.Region AS 'region', SUM(city.Population) AS 'population_in_cities' "
                            +"FROM city JOIN country ON city.CountryCode = country.Code GROUP BY Region) in_cities ON in_cities.region = in_regions.region;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            ArrayList<String> ContinentInfo = new ArrayList<String>();
            // Display
            while (rset.next()) {
                // Generate string
                x = ("Name " + rset.getString("region_name") + " Total population " + rset.getString("region_population") + " Population in cities: " + rset.getInt("population_in_cities") + " Population not in a city: " + rset.getString("population_not_in_cities"));
                ContinentInfo.add(x);
            }
            return ContinentInfo;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the population information in and out of cities for each Continent.");
            return null;
        }
    }

    public ArrayList<City> getAllCapitalCitiesInTheWorld()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Name AS Name, capital_city.Name AS `capital_city_name`, capital_city.Population AS `capital_city_population`"
                            + "FROM country "
                            +"JOIN city capital_city ON capital_city.ID = country.Capital\n"
                            +"ORDER BY capital_city_population DESC ";

            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City capital_city = new City(
                        rset.getString("capital_city_name"),
                        null,
                        rset.getInt("capital_city_population"),
                        null,
                        null,
                        null
                );
                cities.add(capital_city);
            }
            // return results
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all capital cities");
            return null;
        }
    }

    public ArrayList<City> getAllCapitalCitiesInAContinent()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Name AS NAME, country.Continent AS Continent ,capital_city.Name AS `capital_city_name`, capital_city.Population AS `capital_city_population` "
                            + "FROM country "
                            + "JOIN city capital_city ON capital_city.ID = country.Capital "
                            + "ORDER BY Continent, capital_city_population DESC ";

            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City capital_city = new City(
                        rset.getString("capital_city_name"),
                        null,
                        rset.getInt("capital_city_population"),
                        rset.getString("Continent"),
                        null,
                        null
                );
                cities.add(capital_city);
            }
            // return results
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all capital cities in a continent");
            return null;
        }
    }

    public ArrayList<City> getAllCapitalCitiesInARegion()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Name AS NAME, country.Region AS Region ,capital_city.Name AS `capital_city_name`, capital_city.Population AS `capital_city_population`"
                            +"FROM country "
                            +"JOIN city capital_city ON capital_city.ID = country.Capital "
                            +"ORDER BY Region, capital_city_population DESC ";

            // Execute SQL
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract country information.
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next()) {
                City capital_city = new City(
                        rset.getString("capital_city_name"),
                        null,
                        rset.getInt("capital_city_population"),
                        null,
                        null,
                        rset.getString("Region")
                );
                cities.add(capital_city);
            }
            // return results
            return cities;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all capital cities in a continent");
            return null;
        }
    }

    /*
     * displayFormattedCountries outputs country details. It automatically hides uninitialised attributes.
     * Removes duplication of display methods. This method can handle results from all get methods.
     * Added by Eoin K:27/02/21
     * Updated by Eoin K:14/03/21
     * @param countries An array of countries, each country should be identical in attribute format.
     */
    public void displayFormattedCountries(ArrayList<Country> countries)
    {
        // Use the first country in the ArrayList to generate the headers
        // This method presumes that all countries in the ArrayList are identical in format.
        Country firstCountry = countries.get(0);
        String format = "";
        ArrayList<String> arguments = new ArrayList<String>();

        // If countries ArrayList contains country codes then display a Country Code heading.
        if (firstCountry.country_code != null) {
            format = format.concat("%-" + Country.fieldLengths.get(0) + "s ");
            arguments.add("Country Code");
        }
        // If countries ArrayList contains country names then display a Name heading.
        if (firstCountry.name != null) {
            format = format.concat("%-" + Country.fieldLengths.get(1) + "s ");
            arguments.add("Name");
        }
        // If countries ArrayList contains country continents then display a Continent heading.
        if (firstCountry.continent != null) {
            format = format.concat("%-" + Country.fieldLengths.get(2) + "s ");
            arguments.add("Continent");
        }
        // If countries ArrayList contains country regions then display a Region heading.
        if (firstCountry.region != null) {
            format = format.concat("%-" + Country.fieldLengths.get(3) + "s ");
            arguments.add("Region");
        }
        // If countries ArrayList contains country populations then display a Population heading.
        if (firstCountry.population != -1) {
            format = format.concat("%-" + Country.fieldLengths.get(4) + "s ");
            arguments.add("Population");
        }
        // If countries ArrayList contains country populations then display a Capital City heading.
        if (firstCountry.capital_city != null && firstCountry.capital_city.name != null) {
            format = format.concat("%-" + City.fieldLengths.get(0) + "s");
            arguments.add("Capital City");
        }

        // Print the headers
        System.out.println(String.format(format, arguments.toArray()));
        // Print the values
        countries.forEach(C -> {
            System.out.println(String.format(C.toFormattedString()));
        });
    }

    /*
     * displayFormattedCities outputs city details. It automatically hides uninitialised attributes.
     * Removes duplication of display methods. This method can handle results from all get methods.
     * Added by Joe B: 28/02/21
     * Modified by Joe B: 01/03/21
     * @param countries An array of cities, each city should be identical in attribute format.
     */
    public void displayFormattedCities(ArrayList<City> cities)
    {
        // Use the first city in the ArrayList to generate the headers
        // This method presumes that all cities in the ArrayList are identical in format.
        City firstCity = cities.get(0);
        String format = "";
        ArrayList<String> arguments = new ArrayList<String>();

        // If cities ArrayList contains city names then display a Name heading.
        if (firstCity.name != null) {
            format = format.concat("%-" + City.fieldLengths.get(0) + "s ");
            arguments.add("Name");
        }
        // If cities ArrayList contains city districts then display a District heading.
        if (firstCity.district != null) {
            format = format.concat("%-" + City.fieldLengths.get(1) + "s ");
            arguments.add("District");
        }
        // If cities ArrayList contains cities populations then display a Population heading.
        if (firstCity.population != -1) {
            format = format.concat("%-" + City.fieldLengths.get(2) + "s ");
            arguments.add("Population");
        }
        // If cities ArrayList contains cities continent then display a Continent heading.
        if (firstCity.continent != null) {
            format = format.concat("%-" + City.fieldLengths.get(3) + "s ");
            arguments.add("Continent");
        }
        // If cities ArrayList contains cities country then display a Country heading.
        if (firstCity.country!= null) {
            format = format.concat("%-" + City.fieldLengths.get(4) + "s ");
            arguments.add("Country");
        }
        // If cities ArrayList contains cities regions then display a Region heading.
        if (firstCity.region!= null) {
            format = format.concat("%-" + City.fieldLengths.get(5) + "s");
            arguments.add("Region");
        }

        // Print the headers
        System.out.println(String.format(format, arguments.toArray()));
        // Print the values
        cities.forEach(CI -> {
            System.out.println(String.format(CI.toFormattedString()));
        });
    }

    /**
     * displayFormattedCities outputs location populations. It automatically formats the output.
     * This method can handle populations at multiple levels, from world down to region.
     * Added by Eoin K: 14/03/21
     * @param locations An HashMap of locations, each location should have a name and a population.
     */
    public void displayFormattedPopulations(HashMap<String, Number> locations)
    {
        if(locations.size() == 0) {
            return;
        }

        String format = "";
        ArrayList<String> arguments = new ArrayList<String>();

        // I
        format = format.concat("%-44s ");
        arguments.add("Place");

        //
        format = format.concat("%-10s");
        arguments.add("Population");

        // Print the headers
        System.out.println(String.format(format, arguments.toArray()));
        // Print the values
        for (String i : locations.keySet()) {
            String locationFormat = "";
            ArrayList<String> locationArguments = new ArrayList<String>();

            // The largest place value length is 44 from the largest country name
            locationFormat = locationFormat.concat("%-44s ");
            locationArguments.add(i);

            // the largest population value is 10 digits long as the world population is 10 digits long
            locationFormat = locationFormat.concat("%-10s");
            locationArguments.add(String.valueOf(locations.get(i)));

            System.out.println(String.format(locationFormat, locationArguments.toArray()));
        }
    }

    /** main method
     * A static method that is run upon execution. Nothing is returned and no parameters are expected in the array.
     * @param args an array that requires no entries
     */
    public static void main(String[] args)
    {
        // Create new Application as the App class holds the connection methods
        App a = new App();

        // Connect to database
        if (args.length < 1)
        {
            a.connect("localhost:3306");
        }
        else
        {
            a.connect(args[0]);
        }

        // # 5 - Added by Jackson A 23/02/2021
        // Refactored by Jackson A 02/03/2021
        // Generate population information of Countries In the World ordered by population.
        ArrayList<Country> countries5 = a.getCountryLargestToSmallest();
        // Display amount of population information of Countries In the World ordered by population
        // Full Information can be displayed by uncommenting the line below
        //a.displayFormattedCountries(countries5);
        System.out.println(countries5.size()); // Should be 239

        // # 9
        // Generate population information of Countries In A Region Ordered By Population
        ArrayList<Country> countries9 = a.getCountriesInARegionByPopulation();
        // Display amount of population information of Countries In A Region Ordered By Population
        // Full Information can be displayed by uncommenting the line below
        // a.displayFormattedCountries(countries9);
        System.out.println(countries9.size()); // Should be 239

        // # 15
        // Generate population information of the Top N Populated Countries,
        //      in a Continent where N is Provided by the User.
        ArrayList<Country> countries15 = a.getTopNPopulatedCountriesInAContinent(3);
        // Display amount of population information of the Top N Populated Countries,
        //      in a Continent where N is Provided by the User.
        // Full Information can be displayed by uncommenting the line below
        // a.displayFormattedCountries(countries15);
        System.out.println(countries15.size()); // Should be 21

        // # 19
        // Generate population information of the Top N Populated Countries,
        //      in a Region where N is Provided by the User.
        ArrayList<Country> countries19 = a.getTopNPopulatedCountriesInARegion(2);
        // Display amount of population information of the Top N Populated Countries,
        //      in a Region where N is Provided by the User.
        // Full Information can be displayed by uncommenting the line below
        // a.displayFormattedCountries(countries19);
        System.out.println(countries19.size()); // Should be 49

        // # 26 - Added by Joe B 27/02/2021
        // Generate population information of the Top N Populated Cities,
        //      in the World where N is Provided by the User.
        ArrayList<City> cities26 = a.getTopNPopulatedCitiesintheWorld(6);
        // Display amount of population information of the Top N Populated Cities,
        //      in the World where N is Provided by the User.
        // Full information can be displayed by uncommenting the line below
        // a.displayFormattedCities(cities26);
        System.out.println(cities26.size()); // Should be 6

        // # 12 - Added by Jackson A 01/03/2021
        // Generate population information of the Top N Populated Countries,
        //      in the World where N is Provided by the User.
        ArrayList<Country> countries12 = a.getTopNPopulatedCountriesInTheWorld(5);
        // Display amount of population information of the Top N Populated Countries,
        //      in the World where N is Provided by the User.
        // Full information can be displayed by uncommenting the line below
        //a.displayFormattedCountries(countries12);
        System.out.println(countries12.size()); // Should be N

        // # 7 - Added by Jackson A 01/03/21
        // Generate population information of Countries In A Continent Ordered By Population
        ArrayList<Country> countries7 = a.getCountriesInAContinentByPopulation();
        // Display amount of population information of Countries In A Continent Ordered By Population
        // Full Information can be displayed by uncommenting the line below
        //a.displayFormattedCountries(countries7);
        System.out.println(countries7.size()); // Should be 239

        // # 25 - Added by Joe B 01/03/21
        // Generate population information of the Top N Populated Cities,
        //      in a Continent where N is Provided by the User.
        ArrayList<City> cities25 = a.getTopNPopulatedCitiesinaContinent(5);
        // Display amount of population information of the Top N Populated Cities,
        //      in a Continent where N is Provided by the User.
        // Full information can be displayed by uncommenting the line below
        // a.displayFormattedCities(cities25);
        System.out.println(cities25.size()); //Should be 6 x n (6 Continents x the number of cities to be displayed)

        // #27 - Added by Joe B: 01/03/21
        // Generate population information of all the cities,
        //      in a District from largest to smallest population.
        ArrayList<City> cities27 = a.getAllCitiesinADistrictLargetoSmall();
        // Display amount of population information of all the cities,
        //      in a District from largest to smallest population.
        // Full information can be displayed by uncommenting the line below
        //a.displayFormattedCities(cities27);
        System.out.println(cities27.size()); //should be 4079

        // #29 - Added by Robbie M: 01/03/21
        // Generating population information of all the cities,
        //  in a Country from largest to smallest population.
        ArrayList<City> cities29 = a.getAllCitiesInACountry();
        // Display amount of population information of all the cities,
        // in a Country from largest to smallest population.
        // Full information can be displayed by uncommenting the line below
        //a.displayFormattedCities(cities29);
         System.out.println(cities29.size()); //should be 4079

        // #24 - Added by Robbie M: 01/03/21
        // Generating population information of all the cities,
        //  in a Continent from largest to smallest population.
        ArrayList<City> cities24 = a.getAllCitiesInAContinent();
        // Display amount of population information of all the cities,
        // in a Continent from largest to smallest population.
        // Full information can be displayed by uncommenting the line below
        //a.displayFormattedCities(cities24);
        System.out.println(cities24.size()); //should be 4079

        // #28 - Added by Robbie M: 01/03/21
        // Generating population information of all the cities,
        //  in a region from largest to smallest population.
        ArrayList<City> cities28 = a.getAllCitiesInARegion();
        // Display amount of population information of all the cities,
        // in a region from largest to smallest population.
        // Full information can be displayed by uncommenting the line below
        //a.displayFormattedCities(cities28);
        System.out.println(cities28.size()); //should be 4079

        // #22 - Added by Robbie M: 01/03/21
        // Generating population information of all the cities,
        //  in the world from largest to smallest population.
        ArrayList<City> cities22 = a.getAllCitiesInTheWorld();
        // Display amount of population information of all the cities,
        // in the world from largest to smallest population.
        // Full information can be displayed by uncommenting the line below
        //a.displayFormattedCities(cities22);
        System.out.println(cities22.size()); //should be 4079

        // # 41 - Added by Eoin K: 14/03/21
        // Generate country report of all the countries in the world
        ArrayList<Country> countries41 = a.getCountryReports();
        // Display size of country report generated.
        // Full Information can be displayed by uncommenting the line below
        // a.displayFormattedCountries(countries41);
        System.out.println(countries41.size()); // Should be 232

        // # 42 - Added by Eoin K: 14/03/21
        // Generate city report of all the cities in the world
        ArrayList<City> countries42 = a.getCityReports();
        // Display size of city report generated.
        // Full Information can be displayed by uncommenting the line below
        // a.displayFormattedCities(countries42);
        System.out.println(countries42.size()); // Should be 4079

        // # 32 - Added by Eoin K: 14/03/21
        // Generate the world population
        HashMap<String, Number> locations32 = a.getWorldPopulation();
        // Display the raw value world population.
        // Formatted Information can be displayed by uncommenting the line below
        // a.displayFormattedPopulations(locations32);
        System.out.println(locations32.get("World"));

        // # 35 - Added by Eoin K: 14/03/21
        // Generate the continents population
        HashMap<String, Number> locations35 = a.getContinentPopulation();
        // Display the size of continents population information.
        // Formatted Information can be displayed by uncommenting the line below
        // a.displayFormattedPopulations(locations35);
        System.out.println(locations35.size()); // Should be 7

        // # 23 - Added by Joe B: 20/03/21
        // Generate population information of all the cities,
        //      in a Region from largest to smallest population.
        ArrayList<City> cities23 = a.getTopNPopulatedCitiesinaRegion(3);
        // Display amount of population information of all the cities,
        //      in a Region from largest to smallest population.
        // Full information can be displayed by uncommenting the line below
        // a.displayFormattedCities(cities23);
        System.out.println(cities23.size()); //Should be 69 - 3 cities per region.

        // # 21 - Added by Joe B: 21/03/21
        // Generate population information of all the cities,
        //      in a Country from largest to smallest population.
        ArrayList<City> cities21 = a.getTopNPopulatedCitiesinaCountry(3);
        // Display amount of population information of all the cities,
        //      in a Country from largest to smallest population.
        // Full information can be displayed by uncommenting the line below
        // a.displayFormattedCities(cities21);
        System.out.println(cities21.size()); //Should be  - 3 cities per country

        // # 37 - Added by Jackson A: 22/03/21
        // Generate the regions population
        HashMap<String, Number> regions37 = a.getRegionPopulation();
        // Display the size of regions population information.
        // Formatted Information can be displayed by uncommenting the line below
        // a.displayFormattedPopulations(regions37);
        System.out.println(regions37.size()); // Should be 25

        // # 6 - Added by Jackson A: 22/03/21
        // Generate the population of people living in and out of cities.
        ArrayList<String> cities6 = a.getPopulatedAndUnpopulatedCities();
        // Display the size of regions population information.
        // Formatted Information can be displayed by uncommenting the line below
        //a.displayBasicStringArray(cities6);
        System.out.println(cities6.size()); // Should be 232

       // # 14 - Added by Joe B 22/03/2021
        // Generate population information of the Top N Populated Capital Cities,
        //      in the World where N is Provided by the User.
        ArrayList<City> cities14 = a.getTopNCapitalCitiesintheWorld(5);
        // Display amount of population information of the Top N Populated Capital Cities,
        //      in the World where N is Provided by the User.
        // Full information can be displayed by uncommenting the line below
        // a.displayFormattedCities(cities14);
        System.out.println(cities14.size()); // Should be N (5) - e.g top 5 most populated capitals in the world

        // # 11 - Added by Joe B 22/03/2021
        // Generate population information of the Top N Populated Capital Cities,
        //      in the a Region where N is Provided by the User.
        ArrayList<City> cities11 = a.getTopNCapitalCitiesinaRegion(3);
        // Display amount of population information of the Top N Populated Capital Cities,
        //      in a Region where N is Provided by the User.
        // Full information can be displayed by uncommenting the line below
        // a.displayFormattedCities(cities11);
        System.out.println(cities11.size()); // Should be N (3) - 3 highest populated capital cities per region

        // # 13 - Added by Robbie M 22/03/2021
        // Generate population information of the Top N Populated Capital Cities,
        //      in the a Continent where N is Provided by the User.
        ArrayList<City> cities13 = a.getTopNCapitalCitiesinAContinent(3);
        // Display amount of population information of the Top N Populated Capital Cities,
        //      in the a Continent where N is Provided by the User.
        // Full information can be displayed by uncommenting the line below
        //a.displayFormattedCities(cities13);
        System.out.println(cities13.size()); // Should be N (3) - e.g top 5 most populated capitals in the world

        // # 10 - Added by Jackson A: 23/03/21
        // Generate the population of people living in and out of cities for each Continent..
        ArrayList<String> cities10 = a.getPopulatedAndUnpopulatedCitiesForContinent();
        // Display the city info.
        // Formatted Information can be displayed by uncommenting the line below
        //a.displayBasicStringArray(cities10);
        System.out.println(cities10.size()); //7

        // # 8 - Added by Robbie M: 23/03/21
        // Generate the population of people living in and out of cities for each Region..
        ArrayList<String> cities8 = a.getPopulatedAndUnpopulatedCitiesForRegion();
        // Display the city info.
        // Formatted Information can be displayed by uncommenting the line below
        //a.displayBasicStringArray(cities8);
        System.out.println(cities8.size()); //7

        // # 18 - Added by Robbie M: 23/03/21
        // Generate All the capital cities in the world organised by largest population to smallest.
        ArrayList<City> cities18 = a.getAllCapitalCitiesInTheWorld();
        // Display the city info.
        // Formatted Information can be displayed by uncommenting the line below
        //a.displayFormattedCities(cities18);
        System.out.println(cities18.size()); //232

        // # 17 - Added by Robbie M: 23/03/21
        // Generate All the capital cities in a Continent organised by largest population to smallest.
        ArrayList<City> cities17 = a.getAllCapitalCitiesInAContinent();
        // Display the city info.
        // Formatted Information can be displayed by uncommenting the line below
        //a.displayFormattedCities(cities17);
        System.out.println(cities17.size()); //232

        // # 16 - Added by Robbie M: 23/03/21
        // Generate All the capital cities in a Region organised by largest population to smallest.
        ArrayList<City> cities16 = a.getAllCapitalCitiesInARegion();
        // Display the city info.
        // Formatted Information can be displayed by uncommenting the line below
        a.displayFormattedCities(cities16);
        System.out.println(cities16.size()); //232

        // Disconnect from database
        a.disconnect();
    }
}

