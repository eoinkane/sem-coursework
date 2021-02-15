package com.napier.teamc;

import java.sql.*;

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

        // Disconnect from database
        a.disconnect();
    }
}