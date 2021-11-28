package colpatria.searchapirest.domain.messagedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class ResultAnexo {

    private final String id;

    private final String file;

    private final String type;

    private final Integer state;

    private final String codigoQueja;

}
