package edu.orangecoastcollege.cs273.rmillett.audiate;

/**
 * Helper class used to handle intervals expressed in either ratios/fractions or cents for the purpose
 * of frequency calculations.
 *
 * From Wikipedia:
 *
 * In music theory, an interval is the difference between two pitches. An interval may be described
 * as horizontal, linear, or melodic if it refers to successively sounding tones, such as two adjacent
 * pitches in a melody, and vertical or harmonic if it pertains to simultaneously sounding tones, such
 * as in a chord.
 *
 * In physical terms, an interval is the ratio between two sonic frequencies. For example, any two
 * notes an octave apart have a frequency ratio of 2:1. This means that successive increments of pitch
 * by the same interval result in an exponential increase of frequency, even though the human ear
 * perceives this as a linear increase in pitch. For this reason, intervals are often measured in
 * cents, a unit derived from the logarithm of the frequency ratio.
 *
 * The cent is a logarithmic unit of measure used for musical intervals. Twelve-tone equal
 * temperament divides the octave into 12 semitones of 100 cents each. Typically, cents are used
 * to express small intervals, or to compare the sizes of comparable intervals in different
 * tuning systems, and in fact the interval of one cent is too small to be heard between
 * successive notes.
 *
 * @author Ryan Millett
 * @version 1.2
 */
public class IntervalHandler {

    /**
     * Converts a decimal (double value) to a string representing the approximate fraction equivalent
     *
     * This method is used to express the relationship between two frequencies as a simple fraction
     *
     * @param decimal a double value representing the decimal form of a whole-number ratio
     * @return A String representing an approximate whole-number ratio
     */
    public static String convertDecimalToRatio(double decimal) {
        if (decimal < 0){
            return "-" + convertDecimalToRatio(-decimal);
        }
        double tolerance = 1.0E-6;
        double m1 = 1; double m2 = 0;
        double n1 = 0; double n2 = 1;
        double b = decimal;
        do {
            double a = Math.floor(b);
            double aux = m1; m1 = a * m1 + m2; m2 = aux;
            aux = n1; n1 = a * n1 + n2; n2 = aux;
            b = 1 / (b - a);
        } while (Math.abs(decimal - m1 / n1) > decimal * tolerance);

        return (int) m1 + "/" + (int) n1;
    }

    /**
     * Converts a String representing a whole-number ratio into a decimal (double value).
     *
     * This ratio value is used to calculate a specified interval above a given fundamental frequency
     *
     * @param ratio A String representing a whole-number ratio
     * @return Converted decimal (double value) representing the decimal form of a whole-number ratio
     */
    public static double convertRatioToDecimal(String ratio) {
        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        }
        else
            return Double.parseDouble(ratio);
    }

    /**
     * Takes a String representing an interval expressed as a whole-number ratio and returns the
     * interval size expressed in cents.
     *
     * Formula used (where m/n is the ratio): 1200*log(m/n)/log(2)
     *
     * @param ratio A String representing a whole-number ratio
     * @return double value of ratio expressed in cents
     */
    public static double convertRatioToCents(String ratio) {
        return 1200 * Math.log(IntervalHandler.convertRatioToDecimal(ratio)) / Math.log(2);
    }

    /**
     * Converts an intervallic distance in cents into a decimal (double value).
     *
     * This method is best used when chained with the <code>convertDecimalToRatio()</code>
     * method or when creating a new interval by multiplying a fundamental frequency by the
     * returned decimal (double) value.
     *
     * @param cents A double value representing an interval in cents
     * @return Converted decimal (double value) representing the decimal form of an interval
     */
    public static double convertCentsToDecimal(double cents) {
        return Math.pow(2, cents / 1200);
    }
}
