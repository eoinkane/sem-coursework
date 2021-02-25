package com.napier.teamc;
/**
 * Represents a Country
 */
public class Country
{
    /**
     * Default Country Constructor
     * This constructor takes no parameters. Properties can be set manually.
     * Added by Eoin K:25/02/21
     */
    public Country() {}

    /**
     * Country Constructor
     * A public constructor to initialise an instance of a Country object with a Name, Region and Population
     * @param name_local: the name of the country to be initialised (string).
     * @param region_local: the name of the country to be initialised (string).
     * @param population_local: the name of the country to be initialised (integer).
     * Added by Eoin K:25/02/21
     */
    public Country(String name_local, String region_local, int population_local)
    {
        name = name_local;
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

    // Write output
    public String toString()
    {
        return "name " + this.name + " population " + this.population;
    }
}

