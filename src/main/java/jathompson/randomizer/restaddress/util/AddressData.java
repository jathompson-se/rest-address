package jathompson.randomizer.restaddress.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * Contains the data and methods used to populate the address fields of an <code>Address</code>.
 *
 * The current implementation reads the specified files in UTF-8 encoding from <code>resources/data</code> and retains the data in memory for fast access.
 *
 * </p>
 *
 * @author Jennifer A Thompson
 * @version 1.0
 */
public class AddressData {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressData.class);

    /**
     * ISO 3166-1 alpha-3 code representing Canada
     */
    public static final String CAN = "CAN";
    /**
     * ISO 3166-1 alpha-3 code representing Mexico
     */
    public static final String MEX = "MEX";
    /**
     * ISO 3166-1 alpha-3 code representing the Netherlands
     */
    public static final String NETHERLANDS = "NLD";
    /**
     * ISO 3166-1 alpha-3 code representing the United States
     */
    public static final String USA = "USA";
    /**
     * Placeholder to indicate no country was specified
     */
    public static final String DEFAULT = "NO_VALUE";

    /**
     * Uppercase English alphabet
     */
    public static final char[] ALPHABET = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z'};

    /**
     * Countries currently implemented as specified in <code>/data/ImplementedCountries.txt</code>
     */
    public static final ArrayList<String> implementedCountries = new ArrayList<>();
    static {
        Resource resource = new ClassPathResource("/data/ImplementedCountries.txt");
        try {
            Scanner fileScanner = new Scanner(resource.getInputStream());
            while (fileScanner.hasNext()) {
                implementedCountries.add(fileScanner.nextLine().trim());
            }
            LOGGER.debug("implementedCountries initialized to {}", implementedCountries);
            fileScanner.close();

        } catch (IOException ioException) {
            LOGGER.error("Failed to populate implementedCountries");
            ioException.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to populate implementedCountries", ioException);
        }

    }

    //Variables holding address data
    //US & English
    public static final ArrayList<String> streetsEN = new ArrayList<>();
    public static final ArrayList<String> citysEN = new ArrayList<>();
    public static final ArrayList<String> countysEN = new ArrayList<>();
    public static final ArrayList<String> statesUSA = new ArrayList<>();
    public static final ArrayList<String> stateCodesUSA = new ArrayList<>();
    public static final ArrayList<String> countryUSA = new ArrayList<>();
    //Canadian & French
    public static final ArrayList<String> streetsFR = new ArrayList<>();
    public static final ArrayList<String> citysFR = new ArrayList<>();
    public static final ArrayList<String> countysFR = new ArrayList<>();
    public static final ArrayList<String> statesCAN = new ArrayList<>();
    public static final ArrayList<String> stateCodesCAN = new ArrayList<>();
    public static final ArrayList<String> countryCAN = new ArrayList<>();
    //Mexico and Spanish
    public static final ArrayList<String> streetsES = new ArrayList<>();
    public static final ArrayList<String> citysES = new ArrayList<>();
    public static final ArrayList<String> countysES = new ArrayList<>();
    public static final ArrayList<String> statesMEX = new ArrayList<>();
    public static final ArrayList<String> stateCodesMEX = new ArrayList<>();
    public static final ArrayList<String> countryMEX = new ArrayList<>();
    //Netherlands and Dutch
    public static final ArrayList<String> streetsNL = new ArrayList<>();
    public static final ArrayList<String> citysNL = new ArrayList<>();
    //Larger municipalities (counties) do not exist in the Netherlands - will repeat city value
    public static final ArrayList<String> statesNLD = new ArrayList<>();
    public static final ArrayList<String> stateCodesNLD = new ArrayList<>();
    public static final ArrayList<String> countryNLD = new ArrayList<>();


    static {
        HashMap<String, ArrayList<String>> sourceList = new HashMap<>();
        //BEGIN Populate data for US-CAN/English
        sourceList.put("/data/StreetNames_EN.txt", streetsEN);
        sourceList.put("/data/CityNames_EN.txt", citysEN);
        sourceList.put("/data/CountyNames_EN.txt", countysEN);
        sourceList.put("/data/StateNames_USA.txt", statesUSA);
        sourceList.put("/data/StateCodes_USA.txt", stateCodesUSA);
        sourceList.put("/data/CountryNameVariations_USA.txt", countryUSA);
        //END - Populate data for US-CAN/English
        //BEGIN - Populate data for Canada/French - Canadian Only data
        sourceList.put("/data/StreetNames_FR.txt", streetsFR);
        sourceList.put("/data/CityNames_FR.txt", citysFR);
        sourceList.put("/data/CountyNames_FR.txt", countysFR);
        sourceList.put("/data/StateNames_CAN.txt", statesCAN);
        sourceList.put("/data/StateCodes_CAN.txt", stateCodesCAN);
        sourceList.put("/data/CountryNameVariations_CAN.txt", countryCAN);
        //END - Populate data for Canada/FR - Canadian Only data
        //BEGIN - Populate data for Mexico/ES - Mexican Only data
        sourceList.put("/data/StreetNames_ES.txt", streetsES);
        sourceList.put("/data/CityNames_ES.txt", citysES);
        sourceList.put("/data/CountyNames_ES.txt", countysES);
        sourceList.put("/data/StateNames_MEX.txt", statesMEX);
        sourceList.put("/data/StateCodes_MEX.txt", stateCodesMEX);
        sourceList.put("/data/CountryNameVariations_MEX.txt", countryMEX);
        //END - Populate data for Mexico/ES - Mexican Only data
        //BEGIN - Populate data for Netherlands/Dutch - Netherlands Only data
        sourceList.put("/data/StreetNames_NL.txt", streetsNL);
        sourceList.put("/data/CityNames_NL.txt", citysNL);
        sourceList.put("/data/StateNames_NLD.txt", statesNLD);
        sourceList.put("/data/StateCodes_NLD.txt", stateCodesNLD);
        sourceList.put("/data/CountryNameVariations_NLD.txt", countryNLD);
        //END - Populate data for Netherlands/NL - Netherlands Only data

        sourceList.forEach((filepath, list) -> {
            Resource resource = new ClassPathResource(filepath);
            try {
                Scanner fileScanner = new Scanner(resource.getInputStream());
                while (fileScanner.hasNext()) {
                    list.add(fileScanner.nextLine().trim());
                }
                LOGGER.debug("{} populated to ArrayList, values are:\n{}", filepath, list);
                fileScanner.close();

            } catch (IOException ioException) {
                LOGGER.error("Failed to populate from {}", filepath);
                ioException.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to populate from " + filepath, ioException);
            }

        });
    }

    public AddressData() {}


    public static char getRandomLetter(){
        return ALPHABET[ThreadLocalRandom.current().nextInt(ALPHABET.length)];
    }

    public static String getRandomStreetEN(){
        return AddressData.streetsEN.get(ThreadLocalRandom.current().nextInt(AddressData.streetsEN.size()));
    }

    public static String getRandomCityEN(){
        return AddressData.citysEN.get(ThreadLocalRandom.current().nextInt(AddressData.citysEN.size()));
    }

    public static String getRandomCountyEN(){
        return AddressData.countysEN.get(ThreadLocalRandom.current().nextInt(AddressData.countysEN.size()));
    }

    public static String getRandomStreetES(){
        return AddressData.streetsES.get(ThreadLocalRandom.current().nextInt(AddressData.streetsES.size()));
    }

    public static String getRandomCityES(){
        return AddressData.citysES.get(ThreadLocalRandom.current().nextInt(AddressData.citysES.size()));
    }

    public static String getRandomCountyES(){
        return AddressData.countysES.get(ThreadLocalRandom.current().nextInt(AddressData.countysES.size()));
    }
    public static String getRandomStreetFR() {
        return AddressData.streetsFR.get(ThreadLocalRandom.current().nextInt(AddressData.streetsFR.size()));
    }

    public static String getRandomCityFR() {
        return AddressData.citysFR.get(ThreadLocalRandom.current().nextInt(AddressData.citysFR.size()));
    }

    public static String getRandomCountyFR(){
        return AddressData.countysFR.get(ThreadLocalRandom.current().nextInt(AddressData.countysFR.size()));
    }

    public static String getRandomStreetNL(){
        return AddressData.streetsNL.get(ThreadLocalRandom.current().nextInt(AddressData.streetsNL.size()));
    }

    public static String getRandomCityNL(){
        return AddressData.citysNL.get(ThreadLocalRandom.current().nextInt(AddressData.citysNL.size()));
    }

}

