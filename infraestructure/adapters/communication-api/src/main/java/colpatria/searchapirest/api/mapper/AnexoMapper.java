package colpatria.searchapirest.api.mapper;

import colpatria.searchapirest.api.dto.Anexo.AnexoDTO;
import colpatria.searchapirest.api.dto.Anexo.ResultAnexoDTO;
import colpatria.searchapirest.domain.messagedata.Anexo;
import colpatria.searchapirest.domain.messagedata.ResultAnexo;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static reactor.core.publisher.Mono.just;

@Log
@Component
@RequiredArgsConstructor
public class AnexoMapper {

    public Mono<AnexoDTO> map(String anexo) {
        return just(new Gson().fromJson(anexo, AnexoDTO.class));
    }

    public Mono<Anexo> map(AnexoDTO anexoDTO) {
        return mapResultAnexo(anexoDTO.getResults())
                .flatMap(resultAnexos -> just(Anexo.builder()
                        .id(anexoDTO.getId())
                        .next(anexoDTO.getNext())
                        .previous(anexoDTO.getPrevious())
                        .resultAnexo(resultAnexos)
                        .build()));
    }

    private Mono<List<ResultAnexo>> mapResultAnexo(List<ResultAnexoDTO> resultAnexoDTO) {
        return just(resultAnexoDTO.stream()
                .map(resultAnexoDTO1 -> ResultAnexo.builder()
                        .id(resultAnexoDTO1.getId())
                        .file(resultAnexoDTO1.getFile())
                        .type(resultAnexoDTO1.getType())
                        .state(resultAnexoDTO1.getState())
                        .codigoQueja(resultAnexoDTO1.getCodigoQueja())
                        .build())
                .collect(Collectors.toList()));
    }

}
