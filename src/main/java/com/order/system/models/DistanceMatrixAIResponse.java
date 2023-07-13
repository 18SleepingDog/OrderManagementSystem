package com.order.system.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistanceMatrixAIResponse {

    private List<String> destinationAddresses;
    private List<String> originAddresses;
    private List<Row> rows;
    private String status;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Row {
        private List<Element> elements;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Element {
        private Distance distance;
        private Duration duration;
        private String status;
        private String origin;
        private String destination;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Distance {
        private String text;
        private int value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Duration {
        private String text;
        private int value;
    }
}