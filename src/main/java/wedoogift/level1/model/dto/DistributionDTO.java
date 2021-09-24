package wedoogift.level1.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Getter;
import lombok.Setter;
import wedoogift.level1.common.util.LocalDateSerializer;

import java.time.LocalDate;

@Getter
@Setter
public class DistributionDTO {
    private int id;

    @JsonProperty("wallet_id")
    private int walletId;

    private double amount;

    @JsonProperty("start_date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;

    @JsonProperty("end_date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;

    @JsonProperty("company_id")
    private int companyId;

    @JsonProperty("user_id")
    private int userId;
}
