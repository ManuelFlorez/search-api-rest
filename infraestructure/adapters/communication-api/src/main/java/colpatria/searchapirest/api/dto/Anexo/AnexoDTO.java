package colpatria.searchapirest.api.dto.Anexo;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AnexoDTO {

    @SerializedName("count")
    private Integer id;

    @SerializedName("next")
    private String next;

    @SerializedName("previous")
    private String previous;

    @SerializedName("results")
    private List<ResultAnexoDTO> results;

}
