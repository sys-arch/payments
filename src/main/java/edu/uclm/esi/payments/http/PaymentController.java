package edu.uclm.esi.payments.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.payments.services.PaymentService;

@CrossOrigin(
    originPatterns = {
        "http://localhost:*",
        "http://*.swey.net",
        "https://*.swey.net"
    },
    allowCredentials = "true"
)

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/prepay")
    public ResponseEntity<String> prepay(@RequestBody Map<String, Integer> body) {
        try {
            int credits = body.get("credits");
            String clientSecret = paymentService.prepay(credits);
            return ResponseEntity.ok(clientSecret);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }


    @PutMapping("/confirm")
    public void confirm(@RequestBody Integer credits,
            @RequestHeader("Authorization") String token) {
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No se ha proporcionado el token de autorización");
        }
        try {
            this.paymentService.confirm(token, credits);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error procesando el pago");
        }
    }
    

}
