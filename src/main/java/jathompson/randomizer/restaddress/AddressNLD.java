package jathompson.randomizer.restaddress;

import jathompson.randomizer.restaddress.util.AddressData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;
/**
 * <p>
 * Pseudo-randomly generated address from the Netherlands.
 * Street, city and county will use Dutch names.
 * </p>
 * <p>
 * The Netherlands do not have government entities equivalent to counties, so county will be set to the same
 * value as city.
 * </p>
 * <p>
 * Street will pseudo-randomly have the house number appended at the percentage set by
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
public class AddressNLD extends Address{
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressNLD.class);

    public AddressNLD() {
        super();
        createHouse();
        createStreet();
        createPostalCode();
        createCity();
        //NLD does not have counties/municipalities smaller than provence
        county = city;
        createStateAndCode();
        createCountry();
        countryCode = AddressData.NETHERLANDS;
        LOGGER.debug("Generated address is: {}, {}, {}, {}, {}, {}, {}, {}", house, street, postalCode, city, county, state, stateCode, county);
    }

    private void createStreet(){
        street = AddressData.getRandomStreetNL();
        if (ThreadLocalRandom.current().nextInt(100) < AddressConstants.INCLUDE_HOUSE_IN_STREET_PERCENT){
            appendHouseToStreet();
        }
    }

    private void createPostalCode(){
        StringBuilder tempCode = new StringBuilder(String.format("%04d", ThreadLocalRandom.current().nextInt(10000)));
        tempCode.append(" ");
        postalCode = tempCode.append(AddressData.getRandomLetter()).append(AddressData.getRandomLetter()).toString();
    }

    private void createCity(){
        city = AddressData.getRandomCityNL();
    }

    private void createStateAndCode(){
        int i = ThreadLocalRandom.current().nextInt(AddressData.statesNLD.size());
        if (ThreadLocalRandom.current().nextInt(100) < AddressConstants.STATE_PERCENT) {
            state = AddressData.statesNLD.get(i);
        }
        if (ThreadLocalRandom.current().nextInt(100) < AddressConstants.STATE_CODE_PERCENT) {
            stateCode = AddressData.stateCodesNLD.get(i);
        }
    }

    private void createCountry(){
        if (ThreadLocalRandom.current().nextInt(100) < AddressConstants.COUNTRY_PERCENT) {
            country = AddressData.countryNLD.get(ThreadLocalRandom.current().nextInt(AddressData.countryNLD.size()));
        }
    }
}

