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

    // Country population
    public int population;

    // Country name
    public String name;

    // Write output
    public String toString()
    {
        return "name " + this.name + " population " + this.population;
    }
}

