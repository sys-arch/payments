package edu.uclm.esi.payments.http;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @GetMapping("/prepay")
    public String prepay() {
        try {
            PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
                    .setCurrency("eur")
                    .setAmount(1200L)
                    .build();
            PaymentIntent intent = PaymentIntent.create(params);
            JSONObject jso = new JSONObject(intent.toJson());
            String clientSecret = jso.getString("client_secret");
            return clientSecret;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Error creating payment intent");
        }
    }

}
