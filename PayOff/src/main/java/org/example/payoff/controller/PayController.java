package org.example.payoff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/k8s")
public class PayController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private KubernetesOperatorService kubernetesOperatorService;

    @PostMapping("/deploy")
    public String createOrUpdateDeployment(@RequestParam String namespace,
                                           @RequestParam String deploymentName,
                                           @RequestBody String yamlConfig) {
        kubernetesOperatorService.reconcileDeployment(namespace, deploymentName, yamlConfig);
        return "Deployment reconciled successfully";
    }

    @DeleteMapping("/deploy")
    public String deleteDeployment(@RequestParam String namespace,
                                   @RequestParam String deploymentName) {
        kubernetesOperatorService.deleteDeployment(namespace, deploymentName);
        return "Deployment deleted successfully";
    }

    @PostMapping("/pay")
    public String pay() {
        return paymentService.processPayment();
    }
}