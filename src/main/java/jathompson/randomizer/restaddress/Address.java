package jathompson.randomizer.restaddress;

import jathompson.randomizer.restaddress.util.AddressData;

import java.util.concurrent.ThreadLocalRandom;


/**
 *
 * A generic address class containing all the fields required for an address
 *
 * @author Jennifer A Thompson
 * @version 1.0
 */
public abstract class Address implements AddressConstants {
    /**
     * House or street number.
     */
    protected String house;
    /**
     * Street name (in practice may also contain street number).
     */
    protected String street;
    /**
     * An alphanumeric string included in a postal address to facilitate mail sorting (a.k.a. post code, postcode, or ZIP code).
     */
    protected String postalCode;
    /**
     * The name of the primary locality of the place.
     */
    protected String city;
    /**
     * A division of a state; typically a secondary-level administrative division of a country or equivalent.
     */
    protected String county;
    /**
     * A division of a country; typically a first-level administrative division of a country and/or a geographical region.
     */
    protected String state;
    /**
     * A code/abbreviation for the state division of a country.
     */
    protected String stateCode;
    /**
     * A code/abbreviation for the state division of a country.
     */
    protected String country;
    /**
     * A three-letter country code following the ISO 3166-1 alpha-3 code format.
     */
    protected String countryCode;

    public Address(String countryCode) {}

    protected Address() {
    }

    /**
     * Creates a pseudo-random house number consisting of up to X number of digits, where X is configured by
     * <code>AddressConstants.NUM_HOUSE_DIGITS</code>.
     *
     * A letter will be appended at a percentage configured by
     * <code>AddressConstants.HOUSE_APPEND_LETTER_PERCENT</code>.
     *
     * For house numbers consisting of 3 or more digits, a hyphen will be pseudo-randomly inserted into the middle
     * of the house number at a percentage configured by <code>HOUSE_INSERT_HYPHEN_PERCENT</code>.
     */
    protected void createHouse(){
        //vary the number of digits in a house number
        int digits = ThreadLocalRandom.current().nextInt(AddressConstants.NUM_HOUSE_DIGITS) + 1;
        int power10=1;
        for (int i=0; i<digits;i++){
            power10*=10;
        }
        house = String.valueOf(ThreadLocalRandom.current().nextInt(power10)+1);
        if (ThreadLocalRandom.current().nextInt(100) < AddressConstants.HOUSE_APPEND_LETTER_PERCENT){
            house = house.concat(String.valueOf(AddressData.getRandomLetter()));
        }
        if (digits > 2 && ThreadLocalRandom.current().nextInt(100) < AddressConstants.HOUSE_INSERT_HYPHEN_PERCENT){
            StringBuilder temp = new StringBuilder(house);
            house = temp.insert(ThreadLocalRandom.current().nextInt(digits-2) + 1, "-").toString();
        }
    }

    protected void prependHouseToStreet(){
        if (house !=null && street!=null){
            street = new StringBuilder(street).insert(0," ").insert(0, house).toString();
        }

    }

    protected void appendHouseToStreet(){
        if (house !=null && street!=null){
            street = new StringBuilder(street).append(" ").append(house).toString();
        }
    }

    public String getHouse(){
        return house;
    }

    public String getStreet(){
        return street;
    }

    public String getPostalCode(){
        return postalCode;
    }

    public String getCity(){
        return city;
    }

    public String getCounty(){
        return county;
    }

    public String getState(){
        return state;
    }

    public String getStateCode(){
        return stateCode;
    }

    public String getCountry(){
        return country;
    }

    public String getCountryCode(){
        return countryCode;
    }

}
