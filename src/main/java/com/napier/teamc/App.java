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
     * Connect to the MySQL database through the SQL driver.
     */
    public void connect()
    {
        /*  try catch statement to gather the required SQL driver
            application will exit if driver cannot be loaded
         */
        try
        {
            // Load Database driver
            Class.forName("com.mysql.jdbc.Driver");
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
                con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false", "root", "example");
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
               Country cntry = new Country();
               cntry.population = rset.getInt("population");
               cntry.name = rset.getString("name");
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

    /** main method
     * A static method that is run upon execution. Nothing is returned and no parameters are expected in the array.
     * @param args an array that requires no entries
     */
    public static void main(String[] args)
    {
        // Create new Application as the App class holds the connection methods
        App a = new App();

        // Connect to database
        a.connect();

        // #5
        // Call country
        ArrayList<Country> countries = a.getCountryLargestToSmallest();

        // Display
        countries.forEach(C -> {
            System.out.println(C);
        });

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
        // Display population information of the Top N Populated Countries,
        //      in a Region where N is Provided by the User.
        a.displayFormattedCountries(countries19);

        // Disconnect from database
        a.disconnect();
    }
}