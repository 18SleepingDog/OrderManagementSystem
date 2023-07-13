package com.order.system.utils;

import com.order.system.exceptions.GetDistanceBetweenLocationException;
import com.order.system.models.DistanceMatrixAIResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(MockitoExtension.class)
public class DistanceMatrixAIClientTest {
    @Mock
    private RestTemplate restTemplate;

    private static Stream<Arguments> topLevelStatusCodeExceptionProvider() {
        return Stream.of(
                arguments(DistanceMatrixAIResponse.builder().status("INVALID_REQUEST").build(), new GetDistanceBetweenLocationException("Distance Matrix AI Response INVALID_REQUEST: provided request was invalid.")),
                arguments(DistanceMatrixAIResponse.builder().status("MAX_ELEMENTS_EXCEEDED").build(), new GetDistanceBetweenLocationException("Distance Matrix AI Response INVALID_REQUEST: product of origin and destination exceeds the per-query limit.")),
                arguments(DistanceMatrixAIResponse.builder().status("OVER_DAILY_LIMIT").build(), new GetDistanceBetweenLocationException("Distance Matrix AI Response OVER_DAILY_LIMIT: API key is missing or invalid/Billing has not been enabled/The provided method of payment is no longer valid")),
                arguments(DistanceMatrixAIResponse.builder().status("OVER_QUERY_LIMIT").build(), new GetDistanceBetweenLocationException("Distance Matrix AI Response OVER_QUERY_LIMIT: Distance Matrix AI has received too many requests from the server within the allowed time period.")),
                arguments(DistanceMatrixAIResponse.builder().status("REQUEST_DENIED").build(), new GetDistanceBetweenLocationException("Distance Matrix AI Response REQUEST_DENIED: usage denied by the Distance Matrix service")),
                arguments(DistanceMatrixAIResponse.builder().status("UNKNOWN_ERROR").build(), new GetDistanceBetweenLocationException("Distance Matrix AI Response UNKNOWN_ERROR: Distance Matrix AI maybe server error, please try again."))
        );
    }

    @ParameterizedTest
    @MethodSource("topLevelStatusCodeExceptionProvider")
    public void getDistanceBetweenLocation_distanceMatrixAIResponseTopLevelNonOKStatus_throwGetDistanceBetweenLocationException(DistanceMatrixAIResponse mockResponse, GetDistanceBetweenLocationException expectException) {
        // Set up the mock behavior for the RestTemplate object
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(DistanceMatrixAIResponse.class)))
                .thenReturn(mockResponse);

        // Use the mock RestTemplate object
        DistanceMatrixAIClient.setRestTemplate(restTemplate);
        Exception exception = assertThrows(GetDistanceBetweenLocationException.class, () -> {
            DistanceMatrixAIClient.getDistanceBetweenLocation("access_token", "startLatitude", "startLongitude", "endLatitude", "endLongitude");
        });

        assertEquals(expectException.getMessage(), exception.getMessage());
    }
    private static Stream<Arguments> elementLevelStatusCodeExceptionProvider() {
        return Stream.of(
                arguments(DistanceMatrixAIResponse.builder().status("OK").rows(List.of(DistanceMatrixAIResponse.Row.builder().elements(List.of(DistanceMatrixAIResponse.Element.builder().status("NOT_FOUND").build())).build())).build(), new GetDistanceBetweenLocationException("Distance Matrix AI Response NOT_FOUND for the provided origin and/or destination coordinates: origin and/or destination of this pairing could not be geocoded.")),
                arguments(DistanceMatrixAIResponse.builder().status("OK").rows(List.of(DistanceMatrixAIResponse.Row.builder().elements(List.of(DistanceMatrixAIResponse.Element.builder().status("ZERO_RESULTS").build())).build())).build(), new GetDistanceBetweenLocationException("Distance Matrix AI Response ZERO_RESULTS for the provided origin and/or destination coordinates: no route could be found between the origin and destination.")),
                arguments(DistanceMatrixAIResponse.builder().status("OK").rows(List.of(DistanceMatrixAIResponse.Row.builder().elements(List.of(DistanceMatrixAIResponse.Element.builder().status("MAX_ROUTE_LENGTH_EXCEEDED").build())).build())).build(), new GetDistanceBetweenLocationException("Distance Matrix AI Response MAX_ROUTE_LENGTH_EXCEEDED for the provided origin and/or destination coordinates: the requested route is too long and cannot be processed."))
        );
    }
    @ParameterizedTest
    @MethodSource("elementLevelStatusCodeExceptionProvider")
    public void getDistanceBetweenLocation_distanceMatrixAIResponseTopLevelOKStatusButElementLevelNonOKStatus_throwGetDistanceBetweenLocationException(DistanceMatrixAIResponse mockResponse, GetDistanceBetweenLocationException expectException) {
        // Set up the mock behavior for the RestTemplate object
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(DistanceMatrixAIResponse.class)))
                .thenReturn(mockResponse);

        // Use the mock RestTemplate object
        DistanceMatrixAIClient.setRestTemplate(restTemplate);
        Exception exception = assertThrows(GetDistanceBetweenLocationException.class, () -> {
            DistanceMatrixAIClient.getDistanceBetweenLocation("access_token", "startLatitude", "startLongitude", "endLatitude", "endLongitude");
        });

        assertEquals(expectException.getMessage(), exception.getMessage());
    }

    @Test
    public void getDistanceBetweenLocation_distanceMatrixAIResponseNullDistance_throwGetDistanceBetweenLocationException() {
        DistanceMatrixAIResponse mockResponse = DistanceMatrixAIResponse.builder().status("OK").rows(List.of(DistanceMatrixAIResponse.Row.builder().elements(List.of(DistanceMatrixAIResponse.Element.builder().status("OK").build())).build())).build();
        GetDistanceBetweenLocationException expectException = new GetDistanceBetweenLocationException("Null Pointer Exception for getting distance from Distance Matrix API.");
        // Set up the mock behavior for the RestTemplate object
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(DistanceMatrixAIResponse.class)))
                .thenReturn(mockResponse);

        // Use the mock RestTemplate object
        DistanceMatrixAIClient.setRestTemplate(restTemplate);
        Exception exception = assertThrows(GetDistanceBetweenLocationException.class, () -> {
            DistanceMatrixAIClient.getDistanceBetweenLocation("access_token", "startLatitude", "startLongitude", "endLatitude", "endLongitude");
        });

        assertEquals(expectException.getMessage(), exception.getMessage());
    }

    @Test
    public void getDistanceBetweenLocation_DistanceMatrixAIResponseWithValid_ReturnsCorrectDistance() throws Exception {
        DistanceMatrixAIResponse mockResponse = DistanceMatrixAIResponse.builder().status("OK").rows(List.of(DistanceMatrixAIResponse.Row.builder().elements(List.of(DistanceMatrixAIResponse.Element.builder().status("OK").distance(DistanceMatrixAIResponse.Distance.builder().value(100).build()).build())).build())).build();
        // Set up the mock behavior for the RestTemplate object
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(DistanceMatrixAIResponse.class)))
                .thenReturn(mockResponse);
        // Use the mock RestTemplate object
        DistanceMatrixAIClient.setRestTemplate(restTemplate);
        assertEquals(100, DistanceMatrixAIClient.getDistanceBetweenLocation("access_token", "startLatitude", "startLongitude", "endLatitude", "endLongitude"));
    }
}