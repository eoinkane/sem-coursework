package com.napier.teamc;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a City
 * Added by Joe B: 27/02/2021
 */

public class City {

    public static ArrayList<String> fieldLengths = new ArrayList<String>(Arrays.asList(
            "50", // The largest City Name value length is 44.
            "50", // The largest District value length is 25.
            "10", // The largest Population value length is 10.
            "13"  //The largest Continent value length is 13.
    ));

    /**
     * City Constructor
     * A public constructor to initialise an instance of a City object with a Name, District, Population and Continent
     * Added by Joe B: 27/02/21
     * Modified by Joe B: 01/03/21
     *
     * @param name_local:       the name of the city to be initialised (string).
     * @param district_local:   the district of the city to be initialised (district enum).
     * @param population_local: the population of the city to be initialised (integer).
     * @param continent_local: the continent of the city to be initialised (string)
     */

    public City(String name_local, String district_local, int population_local, String continent_local) {

        name = name_local;
        district = district_local;
        population = population_local;
        continent = continent_local;

    }

    /**
     * public attribute to store a Cities population
     * Added by Joe B: 27/02/21
     */
    public int population;

    /**
     * public attribute to store a Cities name
     * Added by Joe B: 27/02/21
     */
    public String name;

    /**
     * public attribute to store a Cities district
     * Added by Joe B: 27/02/21
     */
    public String district;

    /**
     * public attribute to store a Cities continent
     * Added by Joe B: 01/03/21
     */
    public String continent;


    public String toString() {
        return "name " + this.name + "district" + this.district + " population " + this.population + " continent " + this.continent;
    }

    /**
     * toFormattedString concatenates the initialised attributes in the current City instance to a string.
     * toFormattedString uses the static attribute fieldLengths to space the values out by maximum field length.
     * Added by Joe B: 28/02/21
     * @return a string containing all the initialised attributes of the City instance.
     */

    public String toFormattedString() {
        String format = "";
        ArrayList<String> arguments = new ArrayList<String>();

        if (this.name != null) {
            format = format.concat("%-" + City.fieldLengths.get(0) + "s ");
            arguments.add(this.name);
        }

        if (this.district != null) {
            format = format.concat("%-" + City.fieldLengths.get(1) + "s ");
            arguments.add(this.district);
        }
        if (this.population != -1) {
            format = format.concat("%-" + City.fieldLengths.get(2) + "s ");
            arguments.add(String.valueOf(this.population));
        }
        if (this.continent != null) {
            format = format.concat("%-" + City.fieldLengths.get(3) + "s");
            arguments.add(this.continent);
        }
            return format != "" ? String.format(format, arguments.toArray()) : null;
        }
    }


