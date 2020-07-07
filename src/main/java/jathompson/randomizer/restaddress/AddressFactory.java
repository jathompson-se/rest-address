package jathompson.randomizer.restaddress;

import jathompson.randomizer.restaddress.util.AddressData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Factory class used to instantiate an <code>Address</code>.
 *
 * @author Jennifer A Thompson
 * @version 1.0
 */
public class AddressFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressFactory.class);

    public AddressFactory() {}

    /**
     * Generates a pseudo-random address. The country will be pseudo-randomly selected
     * from a list of countries implemented for this application.
     *
     * @return Address from pseudo-randomly selected country.
     */
    public static Address createAddress(){
        return AddressFactory.createAddress(AddressData.implementedCountries.get(ThreadLocalRandom.current().nextInt(AddressData.implementedCountries.size())));
    }

    /**
     * Generates an address for the specified country.
     *
     * @param country type of address to create
     * @return pseudo-randomly generated address
     */
    public static Address createAddress(String country){
        LOGGER.debug("Country is {}", country);
        Address address;
        switch(country.toUpperCase()){
            case AddressData.DEFAULT:
                address = createAddress();
                break;
            case AddressData.CAN:
                address =  new AddressCAN();
                break;
            case AddressData.MEX:
                address =  new AddressMEX();
                break;
            case AddressData.NETHERLANDS:
                address = new AddressNLD();
                break;
            case AddressData.USA:
                address =  new AddressUSA();
                break;
            default: //since validation done in AddressController this case is next expected
                LOGGER.error("Country {} is not implemented by this REST call", country);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Country is not yet implemented");
        }
        return address;
    }

}
