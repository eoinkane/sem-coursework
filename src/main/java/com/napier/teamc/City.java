package com.napier.teamc;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a City
 * Added by Joe B: 27/02/2021
 */

public class City {

    public static  ArrayList<String> fieldLengths = new ArrayList<String>(Arrays.asList(
            "50", // The largest City Name value length is 44.
            "50", // The largest District value length is 25.
            "10"  // The largest Population value length is 10.
    ));

    /**
     * City Constructor
     * A public constructor to initialise an instance of a City object with a Name, District and Population
     * Added by Joe B:27/02/21
     *
     * @param name_local:       the name of the city to be initialised (string).
     * @param district_local:   the district of the city to be initialised (district enum).
     * @param population_local: the name of the country to be initialised (integer).
     */

    public City(String name_local, String district_local, int population_local) {

        name = name_local;
        district = district_local;
        population = population_local;

    }

    // City population
    public int population;

    // City name
    public String name;

    // City district
    public String district;

    public String toString()
    {
        return "name " + this.name +  "district" + this.district + " population " + this.population;
    }

    public String toFormattedString()
    {
        String format = "";
        ArrayList<String> arguments = new ArrayList<String>();

        if (this.name != null) {
            format = format.concat("%-" + Country.fieldLengths.get(0) + "s ");
            arguments.add(this.name);
        }

        if (this.district != null) {
            format = format.concat("%-" + Country.fieldLengths.get(2) + "s ");
            arguments.add(this.district);
        }
        if (this.population != -1) {
            format = format.concat("%-" + Country.fieldLengths.get(3) + "s");
            arguments.add(String.valueOf(this.population));
        }

        return format != "" ? String.format(format, arguments.toArray()) : null;
    }
}

