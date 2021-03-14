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
     * SELECT MAX(LENGTH(Code)), MAX(LENGTH(Name)), MAX(LENGTH(Continent)), MAX(LENGTH(Region)), MAX(LENGTH(Population)) FROM country;
     * Added by Eoin K:27/02/21
     * Updated by Eoin K:14/03/21 (added Code value length)
     */
    public static  ArrayList<String> fieldLengths = new ArrayList<String>(Arrays.asList(
            "3", // The largest Country Code value length is 3.
            "44", // The largest Country Name value length is 44.
            "13", // The largest Country Continent value length is 13.
            "25", // The largest Country Region value length is 25.
            "10"  // The largest Country Population value length is 10.
    ));

    /**
     * Default Country Constructor
     * This constructor takes no parameters. Properties can be set manually.
     * Added by Eoin K:25/02/21
     * Updated by Eoin K:27/02/21 (population initialisation)
     */
    public Country() {
        /*
         * As int is a primitive data type it cannot be null. This makes detecting uninitialised values difficult.
         * To allow distinction between an uninitialised Country.population attribute,
         *      and a Country.population attribute with a value of 0 set population to -1.
         */
        population = -1;
    }

    /**
     * Country Constructor
     * A public constructor to initialise an instance of a Country object with a Name, Continent, Region and Population
     * Added by Eoin K:25/02/21
     * Modified by Eoin K:14/03/21 (Added capital_city_local parameter)
     * @param country_code_local: the code of the country to be initialised (string).
     * @param name_local: the name of the country to be initialised (string).
     * @param continent_local: the continent of the country to be initialised (continents enum).
     * @param region_local: the name of the country to be initialised (string).
     * @param population_local: the name of the country to be initialised (integer).
     * @param capital_city_local: the capital city of the country to be initialised (city).
     */
    public Country(
            String country_code_local,
            String name_local,
            Continents continent_local,
            String region_local,
            int population_local,
            City capital_city_local
    )
    {
        country_code = country_code_local;
        name = name_local;
        continent = continent_local;
        region = region_local;
        population = population_local;
        capital_city = capital_city_local;
    }

    /**
     * public attribute to store a Country's code
     * Added by Eoin K:14/03/21
     */
    public String country_code;

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

    /**
     * public attribute to store a Country's capital city
     * Added by Eoin K:14/03/21
     */
    public City capital_city;

    // Write output
    public String toString()
    {
        return "name " + this.name + " population " + this.population;
    }

    /**
     * toFormattedString concatenates the initialised attributes in the current Country instance to a string.
     * toFormattedString uses the static attribute fieldLengths to space the values out by maximum field length.
     * Added by Eoin K:27/02/21
     * @return a string containing all the initialised attributes of the Country instance.
     */
    public String toFormattedString()
    {
        String format = "";
        ArrayList<String> arguments = new ArrayList<String>();

        if (this.country_code != null) {
            format = format.concat("%-" + Country.fieldLengths.get(0) + "s ");
            arguments.add(this.country_code);
        }
        if (this.name != null) {
            format = format.concat("%-" + Country.fieldLengths.get(1) + "s ");
            arguments.add(this.name);
        }
        if (this.continent != null) {
            format = format.concat("%-" + Country.fieldLengths.get(2) + "s ");
            arguments.add(this.continent.toString());
        }
        if (this.region != null) {
            format = format.concat("%-" + Country.fieldLengths.get(3) + "s ");
            arguments.add(this.region);
        }
        if (this.population != -1) {
            format = format.concat("%-" + Country.fieldLengths.get(4) + "s ");
            arguments.add(String.valueOf(this.population));
        }
        if (this.capital_city != null && this.capital_city.name != null) {
            format = format.concat("%-" + City.fieldLengths.get(0) + "s");
            arguments.add(String.valueOf(this.capital_city.name));
        }

        return format != "" ? String.format(format, arguments.toArray()) : null;
    }
}

