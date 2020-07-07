package jathompson.randomizer.restaddress;

import jathompson.randomizer.restaddress.util.AddressData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * Pseudo-randomly generated Canadian address.
 * Addresses in Quebec will provide street, city and county names in French.
 * All other provences will use English values.
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
public class AddressCAN extends Address{

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressCAN.class);

    public AddressCAN(){
        super();
        createHouse();
        //generate state first to determine if Quebec, if so generate remaining fields using FR instead of EN
        createStateAndCode();
        if ((stateCode !=null && stateCode.toUpperCase().equals("QC")) || ( state!=null && state.toUpperCase().equals("QUEBEC"))){
            LOGGER.debug("Provence is QC, using French Data");
            createStreetFR();
            createCityFR();
            createCountyFR();
        } else {
            createStreetEN();
            createCityEN();
            createCountyEN();
        }
        createPostalCode();
        createCountry();
        countryCode = AddressData.CAN;
        LOGGER.debug("Generated address is: {}, {}, {}, {}, {}, {}, {}, {}", house,street, postalCode, city, county, state, stateCode, county);
    }

    private void createStreetFR() {
        street = AddressData.getRandomStreetFR();
        if (ThreadLocalRandom.current().nextInt(100) < AddressConstants.INCLUDE_HOUSE_IN_STREET_PERCENT){
            prependHouseToStreet();
        }
    }

    private void createCityFR() {
        city = AddressData.getRandomCityFR();
    }

    private void createCountyFR(){
        county = AddressData.getRandomCountyFR();
    }

    private void createStreetEN(){
        street = AddressData.getRandomStreetEN();
        if (ThreadLocalRandom.current().nextInt(100) < AddressConstants.INCLUDE_HOUSE_IN_STREET_PERCENT){
            prependHouseToStreet();
        }
    }

    private void createCityEN(){
        city = AddressData.getRandomCityEN();
    }

    private void createCountyEN(){
        county = AddressData.getRandomCountyEN();
    }

    private void createPostalCode(){
        StringBuilder tempCode = new StringBuilder();
        for (int i=0; i<6; i++){
            if (i%2==0){
                tempCode.append(AddressData.getRandomLetter());
            } else {
                tempCode.append(ThreadLocalRandom.current().nextInt(10));
            }
        }
        postalCode = tempCode.insert(3, " ").toString();
    }

    private void createStateAndCode(){
        int i = ThreadLocalRandom.current().nextInt(AddressData.statesCAN.size());
        if (ThreadLocalRandom.current().nextInt(100) < AddressConstants.STATE_PERCENT) {
            state = AddressData.statesCAN.get(i);
        }
        if (ThreadLocalRandom.current().nextInt(100) < AddressConstants.STATE_CODE_PERCENT){
            stateCode = AddressData.stateCodesCAN.get(i);
        }
    }

    private void createCountry(){
        if (ThreadLocalRandom.current().nextInt(100) < AddressConstants.COUNTRY_PERCENT) {
            country = AddressData.countryCAN.get(ThreadLocalRandom.current().nextInt(AddressData.countryCAN.size()));
        }
    }
}
