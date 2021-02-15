package com.napier.teamc;

import java.sql.*;

/** Application Class
 * Holds the main logic of the program
 * Updated 15/02/21
 */
public class App
{
    /** main method
     * A static method that is run upon execution. Nothing is returned and no parameters are expected in the array.
     * @param args a
     */
    public static void main(String[] args)
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

        // Connection to the database
        Connection con = null;
        // Allow 100 retries as the MySQL DB can take some time to start up
        int retries = 100;
        // A retry loop to connect to the
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database on the world table
                con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                // Wait a bit
                Thread.sleep(10000);
                // Exit for loop
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

        /* An if statement to handle the application shutting down
           The application will only try to disconnect from the DB if a successful connection was made
         */
        if (con != null)
        {
            try
            {
                // Close connection through the Java SQL Connection objects close method
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }
}