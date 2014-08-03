dropwizard-demo
===============
Just a dead simple demo to demonstrate what Dropwizard / Metrics has to offer ops teams in terms of instrumentation and monitoring.


Running the application
-----------------------
All this application does is to download a page over http (uri configured in `sample-config.yml`) using an instrumented http client. 

    git clone git@github.com:kimble/dropwizard-demo.git
    cd dropwizard-demo
    ./gradlew run 
    
Make some requests by reloading http://localhost:8080/


Instrumentation
---------------
Dropwizard comes with a built-in instrumentation for Jetty, Logback and the JVM itself. Have a look at the metrics by navigating to
`http://localhost:8081/metrics?pretty=true`. 

Please consult the [Metrics documentation](http://metrics.codahale.com/manual/core/#man-core-gauges) in order to learn more about the different kinds of metrics available. 



Monitoring (health checks)
---------------------------
Health checks are automated tests intended to verify that conditions necessary for the application to run correctly are in fact OK.
Dropwizard exposes a http based service to run these tests on demand. 

Just a few examples:

 * Can connect to database
 * Enough free disk space 
 * Writable storage folder
 * Remote services can be reached


Navigate to http://localhost:8081/healthcheck to have a look at what this looks like. 