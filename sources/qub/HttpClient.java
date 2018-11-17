package qub;

/**
 * A class that can send HTTP requests and receive HTTP responses.
 */
public interface HttpClient
{
    /**
     * Send the provided HttpRequest and then wait for the target endpoint to return a HttpResponse.
     * @param request The HttpRequest to send.
     * @return The HttpResponse that the server returned.
     */
    Result<HttpResponse> send(HttpRequest request);

    default Result<HttpResponse> get(String urlString)
    {
        PreCondition.assertNotNullAndNotEmpty(urlString, "urlString");

        final Result<URL> url = URL.parse(urlString);
        Result<HttpResponse> result = url.convertError();
        if (result == null)
        {
            result = get(url.getValue());
        }
        return result;
    }

    default Result<HttpResponse> get(URL url)
    {
        return send(HttpRequest.get(url));
    }
}
