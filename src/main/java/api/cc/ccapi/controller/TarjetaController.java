package api.cc.ccapi.controller;

import api.cc.ccapi.entity.Historial;
import api.cc.ccapi.entity.Informacion;
import api.cc.ccapi.entity.Tarjetascredito;
import api.cc.ccapi.entity.Transaccion;
import api.cc.ccapi.repository.HistorialRepository;
import api.cc.ccapi.repository.InformacionRepository;
import api.cc.ccapi.repository.TarjetasCreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

@RestController
public class TarjetaController {
    @Autowired
    TarjetasCreditoRepository repositorio;
    @Autowired
    InformacionRepository informacionRepository;
    @Autowired
    HistorialRepository historialRepository;

    @PostMapping("/tarjeta/verificar")
    public ResponseEntity<HashMap<String,Object>> verificarTarjeta(
        @RequestBody Tarjetascredito tarjeta,
                @RequestParam(value = "fetchId",required = false) boolean fetchID){
        HashMap<String,Object> responseMap= new HashMap<>();
        int verificado = repositorio.verificarTarjeta(tarjeta.getNumero(), tarjeta.getFechaexpiracion(), tarjeta.getCvv());
        System.out.println(tarjeta.getNumero());
        System.out.println(tarjeta.getFechaexpiracion());
        System.out.println(tarjeta.getCvv());
        System.out.println(verificado);
        if(verificado==1){
            Informacion informacion = informacionRepository.findById(tarjeta.getNumero().substring(0,6)).get();
            responseMap.put("estado","Verificado");
            responseMap.put("informacion",informacion);
        } else {
            responseMap.put("estado","No existe");
            responseMap.put("msg","No se ha encontrado la tarjeta de credito con los datos indicados");
        }
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/tarjeta/transacción")
    public ResponseEntity<HashMap<String,Object>> realizarTransaccion(
            @RequestBody Transaccion transaccion
    ){
        HashMap<String,Object> responseMap = new HashMap<>();
        Tarjetascredito origen = transaccion.getOrigen();
        String destino = transaccion.getDestino();
        BigDecimal cantidad = transaccion.getCantidad();
        int verificarOrigen = repositorio.verificarTarjeta(origen.getNumero(), origen.getFechaexpiracion(),origen.getCvv());
        Optional<Tarjetascredito> tarjetaDestinoO = repositorio.findByNumero(destino);
        if (cantidad.compareTo(BigDecimal.ZERO)<0) {
            responseMap.put("estado","error");
            responseMap.put("msg","La cantidad a abonar no puede ser negativa");
            return new ResponseEntity<>(responseMap,HttpStatus.BAD_REQUEST);
        }
        if (cantidad.compareTo(new BigDecimal(10))<0) {
            responseMap.put("estado","error");
            responseMap.put("msg","La cantidad a abonar no puede ser menor que S/10.00");
            return new ResponseEntity<>(responseMap,HttpStatus.BAD_REQUEST);
        }
        if(origen.getNumero().equals(destino)){
            responseMap.put("estado","error");
            responseMap.put("msg","No es posible abonar a la misma cuenta");
            return new ResponseEntity<>(responseMap,HttpStatus.BAD_REQUEST);
        }

        if(verificarOrigen == 0 || tarjetaDestinoO.isEmpty()){
            responseMap.put("estado","error");
            responseMap.put("msg","Los datos de las cuentas son erroneos");
            return new ResponseEntity<>(responseMap,HttpStatus.BAD_REQUEST);
        }

        BigDecimal saldo = repositorio.saldoDeCuenta(origen.getNumero(),origen.getFechaexpiracion(),origen.getCvv());
        if(saldo.compareTo(cantidad)<0){
            responseMap.put("estado","No realizado");
            responseMap.put("msg","No cuenta con los fondos para hacer la transacción con el monto indicado");
            return new ResponseEntity<>(responseMap,HttpStatus.OK);
        }

        Historial historial = new Historial();
        historial.setCantidad(cantidad);
        historial.setOrigen(repositorio.findByNumero(origen.getNumero()).get().getId());
        historial.setDestino(tarjetaDestinoO.get().getId());
        historialRepository.save(historial);
        responseMap.put("estado","exito");

        return  new ResponseEntity<>(responseMap,HttpStatus.OK);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String,Object>> gestionExcepcion(HttpServletRequest request){
        HashMap<String,Object> responseMap = new HashMap<>();
        if(request.getMethod().equals("POST")){
            responseMap.put("estado","error");
            responseMap.put("msg","No se han enviado los datos de forma correcta");
        }
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

}
