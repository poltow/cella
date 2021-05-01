package com.cella.interview.validators;

import java.util.Set;

import static java.util.Locale.IsoCountryCode.PART1_ALPHA3;
import static java.util.Locale.getISOCountries;

public class CountryCodeValidator {

    private CountryCodeValidator() {
    }

    private static Set<String> isoCountries = getISOCountries(PART1_ALPHA3);

    public static boolean isValid(String code) {
        return (code == null) ? false : isoCountries.contains(code);
    }

}
