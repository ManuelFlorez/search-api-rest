package colpatria.searchapirest.domain.messagedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Anexo {

    private final Integer id;

    private final String next;

    private final String previous;

    private final List<ResultAnexo> resultAnexo;
}
