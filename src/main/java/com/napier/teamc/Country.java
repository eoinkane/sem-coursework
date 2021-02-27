package com.napier.teamc;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a Country
 */
public class Country
{
    /**
     * public enum to store the available choices for the continent attribute
     * @attribute DbValue: maps to the database representation of each continent
     * Added by Eoin K:27/02/21
     */
    public enum Continents {
        ASIA("Asia"),
        EUROPE("Europe"),
        NORTH_AMERICA("North America"),
        AFRICA("Africa"),
        OCEANIA("Oceania"),
        ANTARCTICA("Antarctica"),
        SOUTH_AMERICA("South America");

        /**
         * This constructor creates a Continents enum
         * Added by Eoin K:27/02/21
         * @param dbValue: how the database represents the continent (string).
         */
        Continents(String dbValue){
            DbValue = dbValue;
        }

        /**
         * public attribute to store how the database stores the continent
         * Added by Eoin K:27/02/21
         */
        private String DbValue;

        @Override public String toString() { return DbValue; }

        /**
         * customValueOf is a wrapper on enum.ValueOf(String name)
         * customValueOf handles converting database values to enum values
         * Added by Eoin K:27/02/21
         * @param name the name of the continent enum return (string)
         *             Must be one of 'Asia','Europe','North America','Africa','Oceania','Antarctica','South America'
         * @return a Continents enum.
         */
        public static Continents customValueOf(String name) {
            name = name.toUpperCase();
            switch (name)
            {
                case "NORTH AMERICA":
                    return Continents.valueOf("NORTH_AMERICA");
                case "SOUTH AMERICA":
                    return Continents.valueOf("SOUTH_AMERICA");
                default:
                    return Continents.valueOf(name);
            }
        }
    }

    /**
     * fieldLengths is an array that holds the maximum lengths of each Country object field value
     * These fieldLengths can be generated from the world DB using the following query
     * SELECT MAX(LENGTH(Name)), MAX(LENGTH(Continent)), MAX(LENGTH(Region)), MAX(LENGTH(Population)) FROM country;
     * Added by Eoin K:27/02/21
     */
    public static  ArrayList<String> fieldLengths = new ArrayList<String>(Arrays.asList(
            "44", // The largest Country Name value length is 44.
            "13", // The largest Country Continent value length is 13.
            "25", // The largest Country Region value length is 25.
            "10"  // The largest Country Population value length is 10.
    ));

    /**
     * Default Country Constructor
     * This constructor takes no parameters. Properties can be set manually.
     * Added by Eoin K:25/02/21
     */
    public Country() {}

    /**
     * Country Constructor
     * A public constructor to initialise an instance of a Country object with a Name, Continent, Region and Population
     * Added by Eoin K:25/02/21
     * Modified by Eoin K:27/02/21 (Added continent_local parameter)
     * @param name_local: the name of the country to be initialised (string).
     * @param continent_local: the continent of the country to be initialised (continents enum).
     * @param region_local: the name of the country to be initialised (string).
     * @param population_local: the name of the country to be initialised (integer).
     */
    public Country(String name_local, Continents continent_local, String region_local, int population_local)
    {
        name = name_local;
        continent = continent_local;
        region = region_local;
        population = population_local;
    }

    // Country population
    public int population;

    // Country name
    public String name;

    /**
     * public attribute to store a Country's region
     * Added by Eoin K:25/02/21
     */
    public String region;

    /**
     * public attribute to store a Country's continent
     * Added by Eoin K:27/02/21
     */
    public Continents continent;

    // Write output
    public String toString()
    {
        return "name " + this.name + " population " + this.population;
    }
}

