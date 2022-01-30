package com.bouff.oids.automation;

import com.bouff.oids.app.OidApp;
import com.bouff.oids.app.OidAppImpl;
import com.bouff.oids.loader.OidLoader;
import com.bouff.oids.loader.YamlOidLoader;
import com.bouff.oids.validator.BasicOidValidator;
import com.bouff.oids.validator.OidValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.annotations.Optional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Automation tests class to run quick automation based on 2 files.
 * First file provides list of test cases
 * Second file provides with list of allowed OIDs
 *
 * Command line to run Maven test is:
 * mvn clean test -Dtest=OidAppAutomation -DyamlFile=snmp.yaml -DtestSuite=oids.txt
 *
 * -Dtest - is a class with all tests
 * -DyamlFile - path to the yaml file with accepted OIDs
 * -dtestSuite - path to the file with test examples
 *
 * @author Alexander Chadfield
 */
public class OidAppAutomation {

    /**
     * The OID application.
     */
    private OidApp oidApp;
    protected static final Logger LOGGER = LogManager.getLogger(OidAppAutomation.class);

    /**
     * Declaration of objects for tests
     */
    OidValidator oidValidator = new BasicOidValidator();
    OidLoader loader = new YamlOidLoader(oidValidator);
    Set<String> oids = Collections.emptySet();

    /**
     * Map of the test cases.
     */
    Map<String, Boolean> map = new HashMap<>();
    List<String> list = new ArrayList<>();

    /**
     * Initialization of all objects
     */
    public OidAppAutomation() {
    }

    @BeforeTest
    @Parameters({"yamlFile", "testSuite"})
    public void preperation(@Optional String yamlFile, @Optional String testSuite) {

        jsonToMap(testSuite);

        try {
            oids = loader.load(yamlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        oidApp = new OidAppImpl(new HashSet<>(oids));
    }

    /**
     * Regular test executor from file list
     */
    @Test
    public void testRunner() {

        for (int i = 0; i < map.size(); i++) {
            String key = (String) map.keySet().toArray()[i];
            boolean value = map.get(key);
            regularTest(key, value);
        }
        System.out.printf("%d cases where checked\n", map.size());
        LOGGER.info(String.format("%d cases where checked\n", map.size()));
    }

    /**
     * Test runner for list of strings with exceptions
     */
    @Test
    public void testExceptions() {

        for (String key : list) {
            exceptionTest(key);
        }
        System.out.printf("%d cases where checked\n", list.size());
        LOGGER.info(String.format("%d cases where checked\n", list.size()));
    }

    /**
     * This method should have reporting capabilities, send email, report to the server or else...
     */
    @AfterTest
    public void report() {

    }

    /**
     * JSON Object from the file to Map of test cases
     * @param path Path to the JSON file
     */
    private void jsonToMap(String path) {
        try {
            JSONObject jackson = new JSONObject(readFile(path));

            Iterator<String> keys = jackson.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (jackson.get(key).equals(true) || jackson.get(key).equals(false) ) {
                    boolean value = (boolean) jackson.get(key);
                    map.put(key, value);
                } else {
                    list.add(key);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * File reader to a single String
     *
     * @param path Path to the file
     * @return Single String from the file content
     * @throws IOException Throws Exception
     */
    private String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.US_ASCII);
    }

    /**
     * Regular test for checking Correct / Incorrect OID
     */
    public void regularTest(String oid, boolean expected) {
        if (expected) {
            Assert.assertTrue(oidApp.isOidDescendant(oid));
            LOGGER.info(String.format("Input %s \nExpected %s : %b \n Test succeed!\n", oid, oid, expected));
            System.out.printf("Input %s \nExpected %s : %b \n Test succeed!\n", oid, oid, expected);
        } else {
            Assert.assertFalse(oidApp.isOidDescendant(oid));
            LOGGER.info(String.format("Input %s \nExpected %s : %b \n Test succeed!\n", oid, oid, expected));
            System.out.printf("Input %s \nExpected %s : %b \n Test succeed!\n", oid, oid, expected);
        }
    }

    /**
     * Test the schecks for the exception in case String provided instead of OID
     *
     * @throws NumberFormatException
     */
    public void exceptionTest(String oid) throws NumberFormatException {
        try {
            oidApp.isOidDescendant(oid);
            Assert.fail(String.format("Input %s\n Expected NumberFormatException\n Test failed.\n", oid));
        } catch (NumberFormatException e) {
            Assert.assertFalse(false);
            LOGGER.info(String.format("Input %s\n Expected NumberFormatException\n Test succeed.\n", oid));
            System.out.printf("Input %s\n Expected NumberFormatException\n Test succeed.\n", oid);
        }
    }
}
