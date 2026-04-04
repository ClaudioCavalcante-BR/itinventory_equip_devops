# Project Overview

## Nome do projeto

**ITInventory Backend Observability**

## Resumo executivo

Este projeto apresenta uma aplicação backend em **Java/Spring Boot** preparada para execução em **Docker** e **Kubernetes**, com monitoramento estruturado por **Prometheus** e **Grafana**. O objetivo principal é demonstrar uma entrega aderente a práticas modernas de infraestrutura, deploy e observabilidade.

## O que foi implementado

- aplicação backend empacotada em Docker
- publicação da imagem em repositório Docker Hub
- implantação em Kubernetes com 4 réplicas
- backend exposto via NodePort
- MySQL interno via ClusterIP
- probes de readiness e liveness
- Prometheus com PVC para persistência
- Grafana com dashboard de monitoramento

## Principais métricas monitoradas

- memória JVM
- CPU da aplicação
- threads da JVM
- requisições HTTP por endpoint
- conexões do datasource

## Estrutura de monitoramento

- **Prometheus**: coleta das métricas
- **Grafana**: dashboards
- **Dashboard principal**: `ITInventory Backend Observability`
- **Pasta do dashboard**: `ITInventory`

## Execução rápida

### Docker

```bash
cd docker/infra
docker compose up -d
```

### Kubernetes

```bash
minikube start --driver=docker
kubectl apply -f k8s/00-namespace.yaml
kubectl apply -f k8s/config/
kubectl apply -f k8s/mysql/
kubectl apply -f k8s/backend/
kubectl apply -f k8s/prometheus/
kubectl apply -f k8s/grafana/
kubectl apply -f k8s/access/
```

## Estrutura sugerida na raiz do projeto

```text
itinventory_equip/
├── README.md
├── PROJECT_OVERVIEW.md
├── pom.xml
├── Dockerfile
├── k8s/
├── docker/
└── src/
```

## Observação

Este repositório foi estruturado para fins de demonstração técnica e acadêmica, com ênfase em deploy, monitoramento e organização do ambiente.
