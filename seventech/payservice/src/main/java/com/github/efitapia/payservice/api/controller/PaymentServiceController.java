package com.github.efitapia.payservice.api.controller;

import com.github.efitapia.payservice.api.model.TopUpRequest;
import com.github.efitapia.payservice.api.model.TransferRequest;
import com.github.efitapia.payservice.api.model.WithDrawRequest;
import com.github.efitapia.payservice.service.PaymentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentServiceController {

    private final PaymentService service;

    @PostMapping("/transfer")
    @ApiOperation("Transfer money between accounts")
    public void transfer(@RequestBody TransferRequest request) {
        service.transfer(request);
    }

    @PostMapping("/topUp")
    @ApiOperation("Top up money from account")
    public void topUp(@RequestBody TopUpRequest request) {
        service.topUp(request);
    }

    @PostMapping("/withdraw")
    @ApiOperation("Withdraw money from account")
    public void withdraw(@RequestBody WithDrawRequest request) {
        service.withdraw(request);
    }

}
