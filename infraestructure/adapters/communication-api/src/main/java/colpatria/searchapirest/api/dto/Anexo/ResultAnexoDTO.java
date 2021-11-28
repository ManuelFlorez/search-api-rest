package colpatria.searchapirest.api.dto.Anexo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResultAnexoDTO {

    @SerializedName("id")
    private String id;

    @SerializedName("file")
    private String file;

    @SerializedName("type")
    private String type;

    @SerializedName("state")
    private Integer state;

    @SerializedName("codigo_queja")
    private String codigoQueja;
}
