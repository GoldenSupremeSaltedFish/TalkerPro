package org.example.payoff.service;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.example.payoff.crd.PayOff;
import org.example.payoff.crd.PayOffList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KubernetesOperatorService {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesOperatorService.class);
    private final KubernetesClient kubernetesClient;
    private SharedIndexInformer<PayOff> informer;

    public KubernetesOperatorService(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
        initInformer();
    }

    private void initInformer() {
        // 初始化 Informer 工厂并注册自定义资源监听器
        SharedInformerFactory sharedInformerFactory = kubernetesClient.informers();
        informer = sharedInformerFactory.sharedIndexInformerFor(
                (customResource, version) -> new PayOff(customResource),
                PayOff.class,
                PayOffList.class,
                0
        );

        informer.addEventHandler(new ResourceEventHandler<PayOff>() {
            @Override
            public void onAdd(PayOff payOff) {
                reconcile(payOff);
            }

            @Override
            public void onUpdate(PayOff oldPayOff, PayOff newPayOff) {
                reconcile(newPayOff);
            }

            @Override
            public void onDelete(PayOff payOff, boolean deletedFinalStateUnknown) {
                cleanup(payOff);
            }
        });

        sharedInformerFactory.startAllRegisteredInformers();
    }

    private void reconcile(PayOff payOff) {
        try {
            String namespace = payOff.getMetadata().getNamespace();
            String name = payOff.getMetadata().getName();

            // 根据 CRD 数据执行业务逻辑
            if ("deploy".equals(payOff.getSpec().getType())) {
                createOrUpdateDeployment(namespace, name, payOff.getSpec().getYamlConfig());
            } else if ("delete".equals(payOff.getSpec().getType())) {
                deleteDeployment(namespace, name);
            }

            logger.info("Reconciled PayOff resource: {}", name);
        } catch (Exception e) {
            logger.error("Failed to reconcile PayOff resource: {}", payOff.getMetadata().getName(), e);
        }
    }

    private void cleanup(PayOff payOff) {
        try {
            String namespace = payOff.getMetadata().getNamespace();
            String name = payOff.getMetadata().getName();
            deleteDeployment(namespace, name);
            logger.info("Cleaned up PayOff resource: {}", name);
        } catch (Exception e) {
            logger.error("Failed to clean up PayOff resource: {}", payOff.getMetadata().getName(), e);
        }
    }

    /**
     * 创建或更新 Deployment
     */
    public void createOrUpdateDeployment(String namespace, String deploymentName, String yamlConfig) {
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