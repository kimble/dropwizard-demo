package com.developerb.dropwizarddemo;

import io.dropwizard.Application;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.http.client.HttpClient;

import java.net.URI;

/**
 * @author Kim A. Betti
 */
public class DemoApplication extends Application<AppConfig> {

    public static void main(String... args) throws Exception {
        new DemoApplication().run(args);
    }


    @Override
    public void initialize(Bootstrap<AppConfig> bootstrap) {

    }

    @Override
    public void run(AppConfig configuration, Environment environment) throws Exception {
        final HttpClient client = new HttpClientBuilder(environment)
                .using(configuration.getHttpClientConfiguration())
                .build("http-client");

        environment.healthChecks().register("able-to-download-html", new DemoHealthCheck(client, configuration.targetUri));

        environment.jersey().register (
                new DemoResource(client, new URI(configuration.targetUri))
        );
    }

}
