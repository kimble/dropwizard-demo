package com.developerb.dropwizarddemo;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kim A. Betti
 */
@Path("/")
@Produces("application/json")
public class DemoResource {

    private final static Logger log = LoggerFactory.getLogger(DemoResource.class);

    private final static Pattern headerPattern = Pattern.compile("<h2>(.*)</h2>");


    private final HttpClient client;
    private final URI targetUri;

    DemoResource(HttpClient client, URI targetUri) {
        this.client = client;
        this.targetUri = targetUri;
    }

    @GET
    public List<String> fetchTargetResource() throws Exception {
        HttpGet httpGet = new HttpGet(targetUri);
        Optional<String> html = getResponseAsString(httpGet);
        Matcher matcher = headerPattern.matcher(html.get());

        List<String> headers = Lists.newArrayList();

        while (matcher.find()) {
            String header = matcher.group(1);
            String cleaned = header.replaceAll("<[^>]*>", "");

            headers.add(cleaned);
        }

        return headers;
    }

    Optional<String> getResponseAsString(HttpGet httpGet) {
        Optional<String> result = Optional.absent();

        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            result = Optional.of(client.execute(httpGet, responseHandler));
        }
        catch (HttpResponseException httpResponseException) {
            log.error("getResponseAsString(): caught 'HttpResponseException' while processing request <" + httpGet.toString() + "> :=> <" + httpResponseException.getMessage() + ">");
        }
        catch (IOException ioe) {
            log.error("getResponseAsString(): caught 'IOException' while processing request <" + httpGet.toString() + "> :=> <" + ioe.getMessage() + ">");
        }
        finally {
            httpGet.releaseConnection();
        }

        return result;
    }

}
