package jathompson.randomizer.restaddress;

import jathompson.randomizer.restaddress.util.AddressData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Class used by SpringBoot for REST calls.
 *
 * @author Jennifer A Thompson
 * @version 1.0
 */
@RestController
public class AddressController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);

    /**
     * Creates an <code>Address</code> for a randomly selected implemented country unless country is specified as a
     * parameter.
     *
     * @param country Type of address to generate
     * @return pseudo-randomly generated Address
     * @throws org.springframework.web.server.ResponseStatusException if the country is invalid
     */
    @GetMapping("/randomizer/address")
    public Address createRandomAddress(@RequestParam(value = "country", defaultValue = AddressData.DEFAULT) String country) {
        LOGGER.debug("Country is {}", country);
        LOGGER.debug("Implemented countries are: {}", AddressData.implementedCountries);
        if ( !(country.toUpperCase().equals(AddressData.DEFAULT) || AddressData.implementedCountries.contains(country.toUpperCase()))){
              LOGGER.error("Country {} is not supported by this REST call", country);
              throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Country is not supported");
        }
        return AddressFactory.createAddress(country);
    }
}
