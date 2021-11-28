package colpatria.searchapirest.api.dto;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto {

    @SerializedName("Tipo_entidad_nombre")
    private Integer tipoEntidadNombre;

    @SerializedName("Tipo_Entidad_cod")
    private Integer tipoEntidadCod;

    @SerializedName("codigo_queja")
    private String codigoQueja;

    @SerializedName("Nombres")
    private String nombres;

    @SerializedName("numero_id_CF")
    private String numeroIdCF;

    @SerializedName("texto_queja")
    private String textoQueja;

    @SerializedName("anexo_queja")
    private Boolean anexoQueja;

    @SerializedName("Fecha_creacion")
    private String fechaCreacion;

    @SerializedName("lgbtiq")
    private String lgbtiq;

    @SerializedName("Producto_digital")
    private String productoDigital;

    @SerializedName("tutela")
    private Boolean tutela;

    @SerializedName("escalamiento_DCF")
    private String escalamientoDFC;

    @SerializedName("queja_expres")
    private Boolean quejaExpres;

    @SerializedName("fecha_actualizacion")
    private String fechaActualizacion;

    @SerializedName("fecha_cierre")
    private String fechaCierre;

    @SerializedName("replica")
    private Boolean replica;

    @SerializedName("argumento_replica")
    private String argumentoReplica;

    @SerializedName("prorroga_queja")
    private Boolean prorrogaQueja;

    @SerializedName("documentacion_rta_final")
    private Boolean documentacionRtaFinal;

    @SerializedName("is_sync")
    private Boolean isSync;

    @SerializedName("codigo_pais")
    private Integer codigoPais;

    @SerializedName("Departamento_codigo")
    private Integer departamentoCodigo;

    @SerializedName("Municipio_codigo")
    private Integer municipioCodigo;

    @SerializedName("tipo_identificacion_CF")
    private Integer tipoIdentificacionCF;

    @SerializedName("Tipo_Persona")
    private Integer tipoPersona;

    @SerializedName("Insta_recepcion")
    private Integer instaRecepcion;

    @SerializedName("pto_recepcion")
    private Integer ptoRecepcion;

    @SerializedName("admision")
    private Integer admision;

    @SerializedName("ente_control")
    private Integer enteControl;

    @SerializedName("Canal_cod")
    private Integer canalCodig;

    @SerializedName("Producto_cod")
    private Integer productoCod;

    @SerializedName("Macro_motivo_cod")
    private Integer macroMotivoCod;

    @SerializedName("sexo")
    private String sexo;

    @SerializedName("condicion_especial")
    private String condicionEspecial;

    @SerializedName("marcacion")
    private String marcacion;

    @SerializedName("A_favor_de")
    private String aFavorDe;

    @SerializedName("aceptacion_queja")
    private String aceptacionQueja;

    @SerializedName("rectificacion_queja")
    private String rectificacionQueja;

    @SerializedName("desistimiento_queja")
    private String desistimientoQueja;

    @SerializedName("Estado_cod")
    private String estadoCod;

    @SerializedName("id")
    private String id;
}
