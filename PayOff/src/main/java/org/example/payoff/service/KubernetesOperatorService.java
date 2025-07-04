package org.example.payoff.service;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KubernetesOperatorService {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesOperatorService.class);
    private final KubernetesClient kubernetesClient;

    public KubernetesOperatorService(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    /**
     * 创建或更新 Deployment
     */
    public void reconcileDeployment(String namespace, String deploymentName, String yamlConfig) {
        try {
            Deployment deployment = Serialization.unmarshal(yamlConfig, Deployment.class);
            ObjectMeta metadata = deployment.getMetadata();
            if (metadata == null || metadata.getName() == null) {
                throw new IllegalArgumentException("Invalid YAML: Missing metadata.name");
            }

            // 检查 Deployment 是否已存在
            Deployment existingDeployment = kubernetesClient.apps().deployments().inNamespace(namespace).withName(deploymentName).get();
            if (existingDeployment == null) {
                // 如果不存在，则创建新的 Deployment
                kubernetesClient.apps().deployments().inNamespace(namespace).create(deployment);
                logger.info("Created Deployment: {}", deploymentName);
            } else {
                // 如果存在，则更新 Deployment
                kubernetesClient.apps().deployments().inNamespace(namespace).withName(deploymentName).patch(deployment);
                logger.info("Updated Deployment: {}", deploymentName);
            }
        } catch (Exception e) {
            logger.error("Failed to reconcile Deployment: {}", deploymentName, e);
        }
    }

    /**
     * 删除 Deployment
     */
    public void deleteDeployment(String namespace, String deploymentName) {
        try {
            boolean isDeleted = kubernetesClient.apps().deployments().inNamespace(namespace).withName(deploymentName).delete().size() > 0;
            if (isDeleted) {
                logger.info("Deleted Deployment: {}", deploymentName);
            } else {
                logger.warn("Deployment not found for deletion: {}", deploymentName);
            }
        } catch (Exception e) {
            logger.error("Failed to delete Deployment: {}", deploymentName, e);
        }
    }
}