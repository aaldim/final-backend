package com.example.demo.controller;

import com.example.demo.dto.TipoCambio;
import com.example.demo.model.Solicitud;
import com.example.demo.service.TipoCambioSoapService;
import com.example.demo.repository.SolicitudRepository;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TipoCambioController {

    @Autowired
    private TipoCambioSoapService tipoCambioSoapService;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @GetMapping(value = "/tipoCambioDia", produces = "application/json")
    public ResponseEntity<Object> obtenerTipoCambioDia() {
        String tipoCambio = tipoCambioSoapService.obtenerTipoCambioDia();

        // Verificar si la respuesta contiene errores
        if (tipoCambio.contains("Error")) {
            return ResponseEntity.status(500).body("Error: " + tipoCambio);
        }

        try {
            // Convertir el XML a JSON
            JSONObject xmlJSONObj = XML.toJSONObject(tipoCambio);

            // Datos específicos del JSON
            JSONObject varDolar = xmlJSONObj.getJSONObject("soap:Envelope")
                    .getJSONObject("soap:Body")
                    .getJSONObject("TipoCambioDiaResponse")
                    .getJSONObject("TipoCambioDiaResult")
                    .getJSONObject("CambioDolar")
                    .getJSONObject("VarDolar");

            // Traer los valores que te interesan
            String fecha = varDolar.getString("fecha");
            double referencia = varDolar.getDouble("referencia");

            // Crear la respuesta como un objeto map o DTO
            Map<String, Object> response = new HashMap<>();
            response.put("fecha", fecha);
            response.put("referencia", referencia);

            // Devolver la respuesta como JSON
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al procesar el XML.");
        }
    }

    @GetMapping(value = "/historial", produces = "application/json")
    public ResponseEntity<List<Solicitud>> obtenerHistorial() {
        try {
            // Consultar el historial de solicitudes desde la base de datos
            List<Solicitud> historial = solicitudRepository.findAll();
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}