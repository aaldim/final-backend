package com.example.demo.service;

import com.example.demo.model.Solicitud;
import com.example.demo.repository.SolicitudRepository;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class TipoCambioSoapService {

    private final SolicitudRepository solicitudRepository;

    public TipoCambioSoapService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }
    
    public List<Solicitud> obtenerHistorial() {
        return solicitudRepository.findAll();
    }

    public String obtenerTipoCambioDia() {
        String soapEndpoint = "https://banguat.gob.gt/variables/ws/TipoCambio.asmx?WSDL";
        String soapAction = "http://www.banguat.gob.gt/variables/ws/TipoCambioDia";

        String soapRequest =
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                        "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                        "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<soap:Body>" +
                        "<TipoCambioDia xmlns=\"http://www.banguat.gob.gt/variables/ws/\" />" +
                        "</soap:Body>" +
                        "</soap:Envelope>";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        headers.add("SOAPAction", soapAction);

        HttpEntity<String> request = new HttpEntity<>(soapRequest, headers);

        try {
            String result = restTemplate.exchange(soapEndpoint, HttpMethod.POST, request, String.class).getBody();
            JSONObject xmlJSONObj = XML.toJSONObject(result);

            JSONObject varDolar = xmlJSONObj.getJSONObject("soap:Envelope")
                    .getJSONObject("soap:Body")
                    .getJSONObject("TipoCambioDiaResponse")
                    .getJSONObject("TipoCambioDiaResult")
                    .getJSONObject("CambioDolar")
                    .getJSONObject("VarDolar");

            String fecha = varDolar.getString("fecha");
            double referencia = varDolar.getDouble("referencia");

            // Guardar en base de datos
            Solicitud solicitud = new Solicitud();
            solicitud.setNumeroSolicitud(UUID.randomUUID().toString());
            solicitud.setFecha(fecha);
            solicitud.setReferencia(referencia);
            solicitudRepository.save(solicitud);

            return result;
        } catch (Exception e) {
            return "Error al obtener el tipo de cambio: " + e.getMessage();
        }
    }
}