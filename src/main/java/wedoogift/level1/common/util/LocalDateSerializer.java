package wedoogift.level1.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {
    @Override
    public void serialize(final LocalDate value, final JsonGenerator jgen, final SerializerProvider arg2) throws IOException {
        if (value != null) {
            String formattedDate = value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            jgen.writeString(formattedDate);
        }
    }
}
