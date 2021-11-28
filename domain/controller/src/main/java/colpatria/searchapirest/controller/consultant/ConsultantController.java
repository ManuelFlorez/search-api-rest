package colpatria.searchapirest.controller.consultant;

import colpatria.searchapirest.domain.canonical.CanonicoFactory;
import colpatria.searchapirest.domain.canonico.Canonico;
import colpatria.searchapirest.domain.common.CustomEventsGateway;
import colpatria.searchapirest.domain.common.EventsGateway;
import colpatria.searchapirest.domain.messagedata.Anexo;
import colpatria.searchapirest.domain.messagedata.Data;
import colpatria.searchapirest.domain.messagedata.Result;
import colpatria.searchapirest.domain.messagedata.gateway.CommunicationApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log
@RequiredArgsConstructor
public class ConsultantController implements CanonicoFactory {

    private final CustomEventsGateway customEventsGateway;
    private final EventsGateway eventsGateway;
    private final CommunicationApi communicationApi;

    private final String OPERATION_SEARCH_API = "search-api";

    public Mono<Void> searchApiRest() {
        return communicationApi.searchApi()
                .flatMap(data -> Mono.just(createGenericCanonico(data, UUID.randomUUID().toString(), "1")))
                .flatMap(this::processAnexoQueja)
                .then();
    }

    private Mono<Canonico> processAnexoQueja(Canonico<Data> canonico) {
        return filtrarAnexoQueja(canonico)
                .flatMap(this::searchAnexo)
                .flatMap(this::downloadFiles)
                .thenReturn(canonico);
    }

    private Mono<Anexo> searchAnexo(List<Result> results) {
        return communicationApi.SearchAnexos(
                results.stream()
                        .map(result -> result.getId())
                        .collect(Collectors.toList())
        );
    }

    private Mono<List<Result>> filtrarAnexoQueja(Canonico<Data> canonico) {
        return Mono.just(canonico.getData().getResults()
                .stream().filter(result -> result.getAnexoQueja())
                .collect(Collectors.toList()));
    }

    private Mono<Void> downloadFiles(Anexo anexo) {
        System.out.println("size lista ResultAnexo: " + anexo.getResultAnexo().size());
        return Flux.fromIterable(anexo.getResultAnexo())
                .flatMap(resultAnexo -> communicationApi.downloadFile(resultAnexo), 3)
                .doOnNext(System.out::println)
                .then();
    }

}
