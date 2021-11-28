package colpatria.searchapirest.domain.messagedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Result {
    private final Integer tipoEntidadNombre;
    private final Integer tipoEntidadCod;
    private final String codigoQueja;
    private final String nombres;
    private final String numeroIdCF;
    private final String textoQueja;
    private final Boolean anexoQueja;
    private final String fechaCreacion;
    private final String lgbtiq;
    private final String productoDigital;
    private final Boolean tutela;
    private final String escalamientoDFC;
    private final Boolean quejaExpres;
    private final String fechaActualizacion;
    private final String fechaCierre;
    private final Boolean replica;
    private final String argumentoReplica;
    private final Boolean prorrogaQueja;
    private final Boolean documentacionRtaFinal;
    private final Boolean isSync;
    private final Integer codigoPais;
    private final Integer departamentoCodigo;
    private final Integer municipioCodigo;
    private final Integer tipoIdentificacionCF;
    private final Integer tipoPersona;
    private final Integer instaRecepcion;
    private final Integer ptoRecepcion;
    private final Integer admision;
    private final Integer enteControl;
    private final Integer canalCodig;
    private final Integer productoCod;
    private final Integer macroMotivoCod;
    private final String sexo;
    private final String condicionEspecial;
    private final String marcacion;
    private final String aFavorDe;
    private final String aceptacionQueja;
    private final String rectificacionQueja;
    private final String desistimientoQueja;
    private final String estadoCod;
    private final String id;
}
