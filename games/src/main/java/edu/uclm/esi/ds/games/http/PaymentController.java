package edu.uclm.esi.ds.games.http;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import edu.uclm.esi.ds.games.services.UserService;

@RestController
@RequestMapping("payments")
@CrossOrigin("*")
public class PaymentController {
	static {
		Stripe.apiKey = "sk_test_51Mo0XwH4w1q9YTZYdmUtkr8MVbkZrlwAiqIwXoYIWXg453p5ot6YHt5tqhHgU666WmVzuvjZsfS0vQcD0ubB0CSI00cMqGDzf3";
		}

	@RequestMapping("/prepay")
	public String prepay(@RequestParam double amount) {
		long total = (long) Math.floor(amount * 100);
		PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
		.setCurrency("eur")
		.setAmount(total)
		.build();
		PaymentIntent intent;
		try {
			intent = PaymentIntent.create(params);
		JSONObject jso = new JSONObject(intent.toJson());
		String clientSecret = jso.getString("client_secret"); 
		System.out.println(clientSecret);
		return clientSecret;
} catch (StripeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"payment failed");
		}
		}

@PostMapping(name = "/paymentsOK", consumes = "application/json")
public void paymentOK(@RequestBody Map<String, String> info) {
	String token = info.get("token");
	
	
}
}
