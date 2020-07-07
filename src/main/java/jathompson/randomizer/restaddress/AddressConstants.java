package jathompson.randomizer.restaddress;

/**
 * <p>Constants used to configure address generation behavior.</p>
 *
 * <p><code>PERCENT</code> values will determine the rate the respective field/addition will be generated.
 * All percentage values are X/100, so X equates to roughly X%. A value of 0 will prevent the modification.
 * The modification will olways ooccur when set to 99</p>
 *
 * @author Jennifer A. THompson
 * @version 1.0
 */
public interface AddressConstants {
    int NUM_HOUSE_DIGITS = 8;
    int HOUSE_APPEND_LETTER_PERCENT = 5;
    int HOUSE_INSERT_HYPHEN_PERCENT = 5;

    int INCLUDE_HOUSE_IN_STREET_PERCENT = 20;

    int STATE_PERCENT = 50;
    int STATE_CODE_PERCENT = 50;

    int COUNTRY_PERCENT = 50;
}
