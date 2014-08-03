package com.developerb.dropwizarddemo;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Optional;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

/**
 * @author Kim A. Betti
 */
public class DemoHealthCheck extends HealthCheck {

    private final HttpClient client;
    private final String targetUri;

    public DemoHealthCheck(HttpClient client, String targetUri) {
        this.client = client;
        this.targetUri = targetUri;
    }

    @Override
    protected Result check() throws Exception {
        HttpGet httpGet = new HttpGet(targetUri);

        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = client.execute(httpGet, responseHandler);

            return response != null ? Result.healthy("Html downloaded successfully from " + targetUri) : Result.unhealthy("Unable to download html from " + targetUri);
        }
        catch (HttpResponseException httpResponseException) {
            return Result.unhealthy("getResponseAsString(): caught 'HttpResponseException' while processing request <" + httpGet.toString() + "> :=> <" + httpResponseException.getMessage() + ">");
        }
        catch (IOException ioe) {
            return Result.unhealthy("getResponseAsString(): caught 'IOException' while processing request <" + httpGet.toString() + "> :=> <" + ioe.getMessage() + ">");
        }
        finally {
            httpGet.releaseConnection();
        }
    }

}
