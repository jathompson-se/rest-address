package jathompson.randomizer.restaddress;

import jathompson.randomizer.restaddress.util.AddressData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * Pseudo-randomly generated address from the United States.
 * Street, city and county will use English names.
 * </p>
 * <p>
 * Street will pseudo-randomly have the house number prepended at the percentage set by
 * <code>AddressConstants.INCLUDE_HOUSE_IN_STREET_PERCENT.</code>
 * </p>
 * <p>
 * Produces an equivalent state and state code as along as the <code>/data</code> files are aligned in index.
 * Since both state and state code are optional, they will be generated at the percentage rate set
 * by <code>AddressConstants.STATE_PERCENT</code> and <code>AddressConstants.STATE_CODE_PERCENT</code> respectively.
 * </p>
 * <p>
 * Generates a country value at the percentage specified in <code>AddressConstants.COUNTRY_PERCENT</code>
 * </p>
 *
 * @author Jennifer Thompson
 * @version 1.0
 */
public class AddressUSA extends Address{

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressUSA.class);

    public AddressUSA(){
        super();
        createHouse();
        createStreet();
        createPostalCode();
        createCity();
        createCounty();
        createStateAndCode();
        createCountry();
        countryCode = AddressData.USA;
        LOGGER.debug("Generated address is: {}, {}, {}, {}, {}, {}, {}, {}", house,street, postalCode, city, county, state, stateCode, county);
    }

    private void createStreet(){
        street = AddressData.getRandomStreetEN();
        if (ThreadLocalRandom.current().nextInt(100) < AddressConstants.INCLUDE_HOUSE_IN_STREET_PERCENT){
            prependHouseToStreet();
        }
    }

    private void createPostalCode(){
        postalCode = String.format("%05d", ThreadLocalRandom.current().nextInt(100000));
    }

    private void createCity(){
        city = AddressData.getRandomCityEN();
    }

    private void createCounty(){
        county = AddressData.getRandomCountyEN();
    }

    private void createStateAndCode(){
        int i = ThreadLocalRandom.current().nextInt(AddressData.statesUSA.size());
        if (ThreadLocalRandom.current().nextInt(100) < AddressConstants.STATE_PERCENT) {
            state = AddressData.statesUSA.get(i);
        }
        if (ThreadLocalRandom.current().nextInt(100) < AddressConstants.STATE_CODE_PERCENT){
            stateCode = AddressData.stateCodesUSA.get(i);
        }
    }

    private void createCountry(){
        if (ThreadLocalRandom.current().nextInt(100) < AddressConstants.COUNTRY_PERCENT) {
            country = AddressData.countryUSA.get(ThreadLocalRandom.current().nextInt(AddressData.countryUSA.size()));
        }
    }

}


