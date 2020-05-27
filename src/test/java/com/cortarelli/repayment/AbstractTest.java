package com.cortarelli.repayment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.text.StringContainsInOrder;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractTest {

    protected ObjectMapper mapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
            .registerModule(new JavaTimeModule())
            ;

    protected String BASE_URL = "/";

    public <T> T fromJson(JsonNode json, TypeReference<T> type) {
        try {
            return mapper.readValue(json.toString(), type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Matcher<T> hasToStringContainingInAnyOrder(Object... items) {
        List<String> strings = Stream.of(items).map(Object::toString).collect(Collectors.toList());
        return Matchers.hasToString(StringContainsInOrder.stringContainsInOrder(strings));
    }

}
