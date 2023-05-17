package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import domain.Instance;
import domain.Item;

/**
 * A parser that reads a MLULSP instance from a file and transforms it into an {@link Instance} object.
 * @author 
 */
public class InstanceReader {

    private File input;
    private static final Pattern headerPattern = Pattern.compile("(\\d+)\\s+(\\d+)\\s+(\\d+)\\s*");
    private static final Pattern costPattern = Pattern.compile("(\\d+)\\s+([\\d\\.]+)\\s*");
    private static final Pattern demandPattern = Pattern.compile("(\\d+)\\s+(\\d+)\\s*");
    private BufferedReader reader;

    /**
     * Create a new instance that reads from the specified file.
     * @param fileName the fileName of the input file
     * @throws RuntimeException if the input file could not be found.
     */
    public InstanceReader(File input) {
        this.input = input;
        if (!input.exists()) {
            throw new RuntimeException("File does not exist: " + input.getAbsolutePath());
        }
    }

    /**
     * Parse the file and transform the contained information.
     * @return an {@link Instance} object which represents the data in the input.
     */
    public Instance parse() {

        reader = null;
        Instance target = null;

        try {
            reader = new BufferedReader(new FileReader(input));
            target = new Instance(input.getName().replace(".req", ""));

            parseHeader(target);
            parseHoldingCosts(target);
            parseProductionCosts(target);
            parseDemand(target);
            parseTree(target);

        } catch (FileNotFoundException ex) {
            //should not really happen, so wrap it and let it crash the app
            throw new RuntimeException(ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(InstanceReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return target;
    }


    /**
     * Parse the header fields.
     * The header fields are item, period and final good count.
     * @param target the Instance to which the values are written to
     */
    private void parseHeader(Instance target) {
        Matcher matcher = matchNextLineOrFail(headerPattern);
        target.setItemCount(Integer.parseInt(matcher.group(1)));
        target.setPeriodCount(Integer.parseInt(matcher.group(2)));
        target.setFinalGoodCount(Integer.parseInt(matcher.group(3)));
        getNextLine(); //consume trailing blank line
    }

    /**
     * Parse the holding costs for each item from the input.
     * Items are created on the fly.
     * @param target the Instance to which the values are written to
     */
    private void parseHoldingCosts(Instance target) {
        for (int itemNo = 1; itemNo <= target.getItemCount(); itemNo++) {
            Matcher matcher = matchNextLineOrFail(costPattern);
            Item item = new Item(Integer.parseInt(matcher.group(1)), target.getPeriodCount());
            item.setHoldingCosts(Double.parseDouble(matcher.group(2)));
            target.addItem(item);
        }
        getNextLine();
    }

    /**
     * Parse the production costs for each item from the input.
     * @param target the Instance to which the values are written to
     */
    private void parseProductionCosts(Instance target) {
        for (int itemNo = 1; itemNo <= target.getItemCount(); itemNo++) {
            Matcher matcher = matchNextLineOrFail(costPattern);
            int key = Integer.parseInt(matcher.group(1));
            Item item = target.getItem(key);
            item.setProductionCosts(Double.parseDouble(matcher.group(2)));
        }
        getNextLine();
    }

    /**
     * Parse the demand for the end products.
     */
    private void parseDemand(Instance target) {
        for (int itemNo = 1; itemNo <= target.getFinalGoodCount(); itemNo++) {
            for (int period = 1; period <= target.getPeriodCount(); period++) {
                Matcher matcher = matchNextLineOrFail(demandPattern);
                int demand = Integer.parseInt(matcher.group(2));
                Item item = target.getItem(itemNo);
                item.setDemandForPeriod(demand, period);
            }
            getNextLine();
        }
    }

    /**
     * 
     * @param target
     */
    private void parseTree(Instance target) {
        for (int itemNo = 1; itemNo <= target.getItemCount(); itemNo++) {
            Item item = target.getItem(itemNo);
            String line = getNextLine();
            String[] nos = line.split("\\s+", -1);
            for (int i = 1; i < nos.length; i++) {
                try {
                    Item pre = target.getItem(Integer.parseInt(nos[i]));
                    item.addPredecessor(pre);
                    pre.addSuccessor(item);
                } catch (NumberFormatException numberFormatException) {
                    continue;
                }
            }
        }
        getNextLine();
    }

    /**
     * Emergency exit for parsing failure.
     * Let the program die with a not-so-helpful error message.
     * @param near
     */
    private void failOnInvalidInput(String near) {
        throw new RuntimeException(input.getName() + ":  malformed near: " + near);
    }

    /**
     * Get the next line in the input.
     * @return the next line if there is one, null otherwise
     */
    private String getNextLine() {
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException ex) {
            System.err.println("IO failure in getNextLine()");
        }
        return line;
    }

    /**
     * Match the next line in the input against the specified pattern.
     * Die if it does not match the pattern.
     * @param pattern the pattern to match against
     * @return the resulting matcher object
     */
    private Matcher matchNextLineOrFail(Pattern pattern) {
        String line = getNextLine();
        Matcher matcher = pattern.matcher(line);
        if (!matcher.matches()) {
            failOnInvalidInput(line);
        }
        return matcher;
    }
    
}
