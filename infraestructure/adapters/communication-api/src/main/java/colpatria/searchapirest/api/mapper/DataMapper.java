package colpatria.searchapirest.api.mapper;

import colpatria.searchapirest.api.dto.DataDTO;
import colpatria.searchapirest.api.dto.ResultDto;
import colpatria.searchapirest.domain.messagedata.Data;
import colpatria.searchapirest.domain.messagedata.Result;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static reactor.core.publisher.Mono.just;

@Log
@Component
@RequiredArgsConstructor
public class DataMapper {


    public Mono<Data> map(String data) {
        return just(new Gson().fromJson(data, DataDTO.class))
                .flatMap(this::toMap);
    }

    private Mono<Data> toMap(DataDTO dataDTO) {
        return just(Data.builder()
                .count(dataDTO.getCount())
                .next(dataDTO.getNext())
                .previous(dataDTO.getPrevious())
                .results(mapResultList(dataDTO.getResults()))
                .build());
    }

    private List<Result> mapResultList(List<ResultDto> resultDtos) {
        return resultDtos.stream()
                .map(resultDto -> Result.builder()
                        .tipoEntidadNombre(resultDto.getTipoEntidadNombre())
                        .tipoEntidadCod(resultDto.getTipoEntidadCod())
                        .codigoQueja(resultDto.getCodigoQueja())
                        .nombres(resultDto.getNombres())
                        .numeroIdCF(resultDto.getNumeroIdCF())
                        .textoQueja(resultDto.getTextoQueja())
                        .anexoQueja(resultDto.getAnexoQueja())
                        .fechaCreacion(resultDto.getFechaCreacion())
                        .lgbtiq(resultDto.getLgbtiq())
                        .productoDigital(resultDto.getProductoDigital())
                        .tutela(resultDto.getTutela())
                        .escalamientoDFC(resultDto.getEscalamientoDFC())
                        .quejaExpres(resultDto.getQuejaExpres())
                        .fechaActualizacion(resultDto.getFechaActualizacion())
                        .fechaCierre(resultDto.getFechaCierre())
                        .replica(resultDto.getReplica())
                        .argumentoReplica(resultDto.getArgumentoReplica())
                        .prorrogaQueja(resultDto.getProrrogaQueja())
                        .documentacionRtaFinal(resultDto.getDocumentacionRtaFinal())
                        .isSync(resultDto.getIsSync())
                        .codigoPais(resultDto.getCodigoPais())
                        .departamentoCodigo(resultDto.getDepartamentoCodigo())
                        .municipioCodigo(resultDto.getMunicipioCodigo())
                        .tipoIdentificacionCF(resultDto.getTipoIdentificacionCF())
                        .tipoPersona(resultDto.getTipoPersona())
                        .instaRecepcion(resultDto.getInstaRecepcion())
                        .ptoRecepcion(resultDto.getPtoRecepcion())
                        .admision(resultDto.getAdmision())
                        .enteControl(resultDto.getEnteControl())
                        .canalCodig(resultDto.getCanalCodig())
                        .productoCod(resultDto.getProductoCod())
                        .macroMotivoCod(resultDto.getMacroMotivoCod())
                        .sexo(resultDto.getSexo())
                        .condicionEspecial(resultDto.getCondicionEspecial())
                        .marcacion(resultDto.getMarcacion())
                        .aFavorDe(resultDto.getAFavorDe())
                        .aceptacionQueja(resultDto.getAceptacionQueja())
                        .rectificacionQueja(resultDto.getRectificacionQueja())
                        .desistimientoQueja(resultDto.getDesistimientoQueja())
                        .estadoCod(resultDto.getEstadoCod())
                        .id(resultDto.getId())
                        .build() )
                .collect(Collectors.toList());
    }

}
