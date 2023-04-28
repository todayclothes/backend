package com.seungah.todayclothes.controller;

import com.seungah.todayclothes.dto.test.TestRequest;
import com.seungah.todayclothes.dto.test.TestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	@GetMapping("/")
    public ResponseEntity<String> serverTest() {
        return ResponseEntity.ok("health check");
    }

	@GetMapping("/test/{req}")
	public ResponseEntity<TestResponse> frontGetTest(@PathVariable Long req,
		@RequestParam Long req2) {

		return ResponseEntity.ok(new TestResponse(req, req2));
	}

	@PostMapping("/test")
	public ResponseEntity<String> frontPostTest(@RequestBody TestRequest req) {

		return ResponseEntity.ok(req.getTest() + "가 잘 받아졌네요.");
	}

}
