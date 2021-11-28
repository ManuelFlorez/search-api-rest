package colpatria.searchapirest.api.dto;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DataDTO {

    @SerializedName("count")
    private Integer count;

    @SerializedName("next")
    private String next;

    @SerializedName("previous")
    private String previous;

    @SerializedName("results")
    private List<ResultDto> results;
}
