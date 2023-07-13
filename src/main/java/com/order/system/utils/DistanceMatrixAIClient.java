package com.order.system.utils;

import com.order.system.exceptions.GetDistanceBetweenLocationException;
import com.order.system.models.DistanceMatrixAIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class DistanceMatrixAIClient {

    private static RestTemplate restTemplate = new RestTemplate();

    public static void setRestTemplate(RestTemplate restTemplate) {
        DistanceMatrixAIClient.restTemplate = restTemplate;
    }

    /**
     * Gets the distance in meters between two locations using the Distance Matrix AI.
     *
     * @param distanceMatrixAIAccessToken the access token for the Distance Matrix AI API
     * @param startLatitude the latitude of the starting location
     * @param startLongitude the longitude of the starting location
     * @param endLatitude the latitude of the ending location
     * @param endLongitude the longitude of the ending location
     * @return the distance in meters between the two locations
     * @throws IllegalArgumentException if any of the input parameters are null or empty, or if the latitude/longitude values are not valid
     * @throws Exception if an error occurs while making the API call or processing the response
     *
     * @see <a href="https://distancematrix.ai/distance-matrix-api#">Documentation of Distance Matrix AI</a>
     */
    public static Integer getDistanceBetweenLocation(String distanceMatrixAIAccessToken, String startLatitude, String startLongitude, String endLatitude, String endLongitude) throws Exception {

        // Set the Request URL for the Distance Matrix AI API
        String url = "https://api.distancematrix.ai/maps/api/distancematrix/json?origins=" + startLatitude + "," + startLongitude + "&destinations=" + endLatitude+ "," + endLongitude+"&key="+ distanceMatrixAIAccessToken;

        try{
            DistanceMatrixAIResponse response = restTemplate.getForObject(url, DistanceMatrixAIResponse.class);

            // check the Top-level Status Codes
            switch (response.getStatus()) {
                case "INVALID_REQUEST":
                    log.error("[getDistanceBetweenLocation] - Received INVALID_REQUEST status from Distance Matrix AI. Retrieved response: {}", response);
                    throw new GetDistanceBetweenLocationException("Distance Matrix AI Response INVALID_REQUEST: provided request was invalid.");
                case "MAX_ELEMENTS_EXCEEDED":
                    log.error("[getDistanceBetweenLocation] - Received MAX_ELEMENTS_EXCEEDED status from Distance Matrix AI. Retrieved response: {}", response);
                    throw new GetDistanceBetweenLocationException("Distance Matrix AI Response INVALID_REQUEST: product of origin and destination exceeds the per-query limit.");
                case "OVER_DAILY_LIMIT":
                    log.error("[getDistanceBetweenLocation] - Received OVER_DAILY_LIMIT status from Distance Matrix AI. Retrieved response: {}", response);
                    throw new GetDistanceBetweenLocationException("Distance Matrix AI Response OVER_DAILY_LIMIT: API key is missing or invalid/Billing has not been enabled/The provided method of payment is no longer valid");
                case "OVER_QUERY_LIMIT":
                    log.error("[getDistanceBetweenLocation] - Received OVER_QUERY_LIMIT status from Distance Matrix AI. Retrieved response: {}", response);
                    throw new GetDistanceBetweenLocationException("Distance Matrix AI Response OVER_QUERY_LIMIT: Distance Matrix AI has received too many requests from the server within the allowed time period.");
                case "REQUEST_DENIED":
                    log.error("[getDistanceBetweenLocation] - Received REQUEST_DENIED status from Distance Matrix AI. Retrieved response: {}", response);
                    throw new GetDistanceBetweenLocationException("Distance Matrix AI Response REQUEST_DENIED: usage denied by the Distance Matrix service");
                case "UNKNOWN_ERROR":
                    log.error("[getDistanceBetweenLocation] - Received UNKNOWN_ERROR status from Distance Matrix AI. Retrieved response: {}", response);
                    throw new GetDistanceBetweenLocationException("Distance Matrix AI Response UNKNOWN_ERROR: Distance Matrix AI maybe server error, please try again.");
                default:
                    // Check Element-level Status Codes
                    switch (response.getRows().get(0).getElements().get(0).getStatus()) {
                        case "NOT_FOUND":
                            log.error("[getDistanceBetweenLocation] - Received NOT_FOUND status from Distance Matrix AI. Retrieved response: {}", response);
                            throw new GetDistanceBetweenLocationException("Distance Matrix AI Response NOT_FOUND for the provided origin and/or destination coordinates: origin and/or destination of this pairing could not be geocoded.");
                        case "ZERO_RESULTS":
                            log.error("[getDistanceBetweenLocation] - Received ZERO_RESULTS status from Distance Matrix AI. Retrieved response: {}", response);
                            throw new GetDistanceBetweenLocationException("Distance Matrix AI Response ZERO_RESULTS for the provided origin and/or destination coordinates: no route could be found between the origin and destination.");
                        case "MAX_ROUTE_LENGTH_EXCEEDED":
                            log.error("[getDistanceBetweenLocation] - Received MAX_ROUTE_LENGTH_EXCEEDED status from Distance Matrix AI. Retrieved response: {}", response);
                            throw new GetDistanceBetweenLocationException("Distance Matrix AI Response MAX_ROUTE_LENGTH_EXCEEDED for the provided origin and/or destination coordinates: the requested route is too long and cannot be processed.");
                        default:
                            Integer distanceValue = response.getRows().get(0).getElements().get(0).getDistance().getValue();
                            return distanceValue;
                    }
            }

        }catch (NullPointerException exception){
            log.error("[getDistanceBetweenLocation] - Null Pointer Exception is found. Exception: {}", exception);
            throw new GetDistanceBetweenLocationException("Null Pointer Exception for getting distance from Distance Matrix API.");
        }catch (GetDistanceBetweenLocationException exception){
            throw new GetDistanceBetweenLocationException(exception.getMessage());
        }catch (Exception exception){
            log.error("[getDistanceBetweenLocation] - Exception is found. Exception: {}", exception);
            throw new Exception(exception.getMessage(), exception);
        }

    }
}
