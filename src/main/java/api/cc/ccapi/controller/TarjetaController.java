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

    @PostMapping("/tarjeta/compra")
    public ResponseEntity<HashMap<String,Object>> realizarCompra(
            @RequestBody Transaccion transaccion
    ){
        HashMap<String,Object> responseMap = new HashMap<>();
        Tarjetascredito origen = transaccion.getOrigen();
        BigDecimal cantidad = transaccion.getCantidad();
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
        int verificarOrigen = repositorio.verificarTarjeta(origen.getNumero(), origen.getFechaexpiracion(),origen.getCvv());
        if(verificarOrigen == 0){
            responseMap.put("estado","error");
            responseMap.put("msg","La tarjeta de credito no es válida");
            return new ResponseEntity<>(responseMap,HttpStatus.BAD_REQUEST);
        }

        BigDecimal saldo = repositorio.saldoDeCuenta(origen.getNumero(),origen.getFechaexpiracion(),origen.getCvv());
        if(saldo.compareTo(cantidad)<0){
            responseMap.put("estado","No realizado");
            responseMap.put("msg","No cuenta con los fondos para hacer la transacción con el monto indicado");
            return new ResponseEntity<>(responseMap,HttpStatus.OK);
        }

        Historial historial = new Historial();
        historial.setId(transaccion.getIdcompra());
        historial.setCantidad(cantidad);
        historial.setOrigen(repositorio.findByNumero(origen.getNumero()).get().getId());
        historialRepository.save(historial);
        responseMap.put("estado","exito");

        return  new ResponseEntity<>(responseMap,HttpStatus.OK);
    }

    @GetMapping("/tarjeta/cancelar/{id}")
    public ResponseEntity<HashMap<String,Object>> cancelarCompra(
            @PathVariable("id") String idStr
    ){
        Long id = 0L;
        HashMap<String,Object> responseMap = new HashMap<>();
        try{
            id= (long) Integer.parseInt(idStr);
        }catch (Exception e){
            responseMap.put("estado","error");
            responseMap.put("msg","El id de la compra tiene que ser un valor numérico");
            return  new ResponseEntity<>(responseMap,HttpStatus.BAD_REQUEST);
        }
        Optional<Historial> oHistorial = historialRepository.findById(id);
        if(oHistorial.isEmpty()){
            responseMap.put("estado","error");
            responseMap.put("msg","No se ha encontrado la compra a cancelar");
            return  new ResponseEntity<>(responseMap,HttpStatus.BAD_REQUEST);
        }
        if(oHistorial.get().getEstado().equals("cancelado")){
            responseMap.put("estado","error");
            responseMap.put("msg","La compra indicada ya ha sido cancelada");
            return  new ResponseEntity<>(responseMap,HttpStatus.BAD_REQUEST);
        }
        Historial historial = oHistorial.get();
        historial.setEstado("cancelado");
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
