package colpatria.searchapirest.domain.messagedata.gateway;

import colpatria.searchapirest.domain.messagedata.Anexo;
import colpatria.searchapirest.domain.messagedata.Data;
import colpatria.searchapirest.domain.messagedata.ResultAnexo;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CommunicationApi {
    Mono<Data> searchApi();
    Mono<Anexo> SearchAnexos(List<String> id);
    Mono<String> downloadFile(ResultAnexo resultAnexo);
}
