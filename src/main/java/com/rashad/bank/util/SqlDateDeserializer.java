package com.rashad.bank.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SqlDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        String dateStr = parser.getText().trim();
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            // Try parsing the date with format "d.M.yyyy"
            try {
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("d.M.yyyy"));
            } catch (DateTimeParseException ex) {
                throw new JsonParseException(parser, "Invalid date format: " + dateStr, ex);
            }
        }
    }
}

