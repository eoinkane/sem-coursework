package com.napier.teamc;

import java.sql.*;
import java.util.ArrayList;

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
                       rset.getString("name"),
                       null,
                       null,
                       rset.getInt("population")
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
                        rset.getString("name"),
                        null,
                        rset.getString("region"),
                        rset.getInt("population")
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
                        rset.getString("name"),
                        Country.Continents.customValueOf(rset.getString("continent")),
                        null,
                        rset.getInt("population")
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
                        rset.getString("name"),
                        null,
                        rset.getString("region"),
                        rset.getInt("population")
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
                        rset.getString("name"),
                        null,
                        null,
                        rset.getInt("population")
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
                        rset.getString("name"),
                        Country.Continents.customValueOf(rset.getString("continent")),
                        null,
                        rset.getInt("population")
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
    /*
     * displayFormattedCountries outputs country details. It automatically hides uninitialised attributes.
     * Removes duplication of display methods. This method can handle results from all get methods.
     * Added by Eoin K:27/02/21
     * @param countries An array of countries, each country should be identical in attribute format.
     */
    public void displayFormattedCountries(ArrayList<Country> countries)
    {
        // Use the first country in the ArrayList to generate the headers
        // This method presumes that all countries in the ArrayList are identical in format.
        Country firstCountry = countries.get(0);
        String format = "";
        ArrayList<String> arguments = new ArrayList<String>();

        // If countries ArrayList contains country names then display a Name heading.
        if (firstCountry.name != null) {
            format = format.concat("%-" + Country.fieldLengths.get(0) + "s ");
            arguments.add("Name");
        }
        // If countries ArrayList contains country continents then display a Continent heading.
        if (firstCountry.continent != null) {
            format = format.concat("%-" + Country.fieldLengths.get(1) + "s ");
            arguments.add("Continent");
        }
        // If countries ArrayList contains country regions then display a Region heading.
        if (firstCountry.region != null) {
            format = format.concat("%-" + Country.fieldLengths.get(2) + "s ");
            arguments.add("Region");
        }
        // If countries ArrayList contains country populations then display a Population heading.
        if (firstCountry.population != -1) {
            format = format.concat("%-" + Country.fieldLengths.get(3) + "s");
            arguments.add("Population");
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

        // Disconnect from database
        a.disconnect();
    }
}