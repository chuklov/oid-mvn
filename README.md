# oid-mvn
Test application with automation test in it

## Prerequisites
The Java JDK/JRE (minimum version 8) must be installed.
Maven installed for command line run otherwise run from the class itself.

## Usage
Using the Maven command line surefire:
Where :
  -Dtest - name of the class that holds all tests
  -DyamlFile - path and name of the file that have all allowed OIDs
  -DtestSuite - path and name of the file with test examples in JSON format
```
$ mvn clean test -Dtest=OidAppAutomation -DyamlFile=snmp.yaml -DtestSuite=oids.txt
```

Running from the class itself need to open OidAppAutomation.class and run from there with the following parameters:
```
testSuite=oids.txt
yamlFile=snmp.yaml
```

### Expected output

```
........
Input 6.1.4.1.6101.1.8.8.7.13
Expected 6.1.4.1.6101.1.8.8.7.13 : false
 Test succeed!
373 cases where checked
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.782 s - in com.bouff.oids.automation.OidAppAutomation
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.435 s
[INFO] Finished at: 2022-01-29T22:32:29-08:00
[INFO] ------------------------------------------------------------------------


```
