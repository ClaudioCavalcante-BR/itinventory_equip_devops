# ITInventory Backend Observability

Projeto backend desenvolvido em **Java com Spring Boot**, containerizado com **Docker** e implantado em **Kubernetes**, com suporte a **observabilidade, monitoramento e persistência de métricas** por meio de **Prometheus** e **Grafana**.

## Objetivo do projeto

O objetivo deste projeto é disponibilizar uma aplicação backend preparada para execução em ambiente conteinerizado e orquestrado, com foco em boas práticas de **deploy**, **monitoramento**, **rastreabilidade operacional** e **organização da infraestrutura como código**.

A solução foi estruturada para demonstrar, de forma prática, a integração entre aplicação Java, banco de dados, Docker, Kubernetes, Prometheus e Grafana, permitindo uma visão técnica completa do ciclo de execução da aplicação.

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Maven
- Docker
- Docker Compose
- Kubernetes
- Minikube
- MySQL
- Prometheus
- Grafana
- Elasticsearch
- Kibana

## Estrutura funcional da solução

A solução contempla os seguintes componentes:

- **Backend Spring Boot** executando a aplicação principal
- **MySQL** como banco de dados relacional
- **Prometheus** para coleta e armazenamento de métricas
- **Grafana** para visualização dos dashboards
- **Elasticsearch e Kibana** para apoio à observabilidade local no ambiente Docker
- **Kubernetes** para orquestração da aplicação e dos serviços

## Principais entregas implementadas

### Docker e Docker Hub

A aplicação foi empacotada em imagem Docker e publicada no Docker Hub com tag versionada, permitindo **rastreabilidade, versionamento e reprodutibilidade** do deploy.

### Kubernetes

A aplicação foi implantada em cluster Kubernetes com:

- Deployment do backend com **4 réplicas**
- Service do backend exposto via **NodePort**
- Banco MySQL acessível internamente via **ClusterIP**
- Probes de **readiness** e **liveness** configuradas

### Observabilidade

Foi implementada uma estrutura de monitoramento com:

- **Prometheus** como servidor de métricas
- **Grafana** como ferramenta de dashboards
- **PVC** para persistência dos dados do Prometheus
- Dashboard com métricas da aplicação, incluindo:
  - memória JVM
  - CPU da aplicação
  - threads da JVM
  - requisições HTTP por endpoint
  - conexões do datasource

## Estrutura de diretórios e função dos arquivos

```text
.
├── .idea/                                  # Arquivos de configuração do projeto no IntelliJ IDEA
├── .mvn/                                   # Arquivos auxiliares do Maven Wrapper
├── docker/
│   └── infra/                              # Infraestrutura local com Docker Compose e serviços de apoio
├── docs/                                   # Documentação complementar do projeto
├── k8s/
│   ├── access/
│   │   ├── clusterrolebinding.yaml         # Vincula permissões elevadas à service account no cluster
│   │   └── serviceaccount.yaml             # Cria a service account usada para acesso administrativo/laboratorial
│   ├── backend/
│   │   ├── backend-deployment.yaml         # Define o Deployment do backend com réplicas, imagem, portas e probes
│   │   └── backend-service.yaml            # Expõe o backend via Service, permitindo acesso interno/externo conforme configuração
│   ├── config/
│   │   ├── backend-configmap.yaml          # Centraliza configurações não sensíveis da aplicação backend
│   │   └── backend-secret.yaml             # Armazena variáveis sensíveis do backend, como credenciais e segredos
│   ├── grafana/
│   │   ├── grafana-deployment.yaml         # Define o Deployment do Grafana no cluster Kubernetes
│   │   └── grafana-service.yaml            # Expõe o Grafana para acesso aos dashboards de monitoramento
│   ├── mysql/
│   │   ├── mysql-deployment.yaml           # Define o Deployment do MySQL com parâmetros de execução do banco
│   │   ├── mysql-pvc.yaml                  # Cria o PersistentVolumeClaim para persistência dos dados do MySQL
│   │   └── mysql-service.yaml              # Expõe o MySQL internamente via Service do tipo ClusterIP
│   ├── prometheus/
│   │   ├── prometheus-configmap.yaml       # Armazena a configuração de scrape e monitoramento do Prometheus
│   │   ├── prometheus-deployment.yaml      # Define o Deployment do Prometheus no cluster
│   │   ├── prometheus-pvc.yaml             # Cria o PersistentVolumeClaim para persistência das métricas do Prometheus
│   │   └── prometheus-service.yaml         # Expõe o Prometheus internamente no cluster para consulta e integração
│   └── 00-namespace.yaml                   # Cria o namespace isolado do projeto no Kubernetes
├── src/                                    # Código-fonte principal da aplicação Java/Spring Boot
├── target/                                 # Artefatos gerados no build do Maven
├── .dockerignore                           # Define arquivos e pastas que não devem ir para o build da imagem Docker
├── .gitattributes                          # Define atributos de versionamento e normalização de arquivos no Git
├── .gitignore                              # Define arquivos e diretórios que não devem ser versionados
├── pom.xml                                 # Arquivo principal de build e dependências do Maven
├── Dockerfile                              # Receita de construção da imagem Docker da aplicação
├── README.md                               # Documento principal de apresentação técnica do repositório
└── PROJECT_OVERVIEW.md                     # Visão executiva resumida do projeto
```

## Como executar localmente com Docker Compose

A partir da estrutura de infraestrutura local:

```bash
cd docker/infra
docker compose up -d --build
```

Para validar os containers:

```bash
docker compose ps
```

## Como executar no Kubernetes com Minikube

Na raiz do projeto:

```bash
minikube start --driver=docker
kubectl config use-context minikube
kubectl get nodes
```

Aplicação dos manifests:

```bash
kubectl apply -f k8s/00-namespace.yaml
kubectl apply -f k8s/config/
kubectl apply -f k8s/mysql/
kubectl apply -f k8s/backend/
kubectl apply -f k8s/prometheus/
kubectl apply -f k8s/grafana/
kubectl apply -f k8s/access/
```

Validação do ambiente:

```bash
kubectl get pods -n itinventory-devops
kubectl get svc -n itinventory-devops
kubectl get deployments -n itinventory-devops
kubectl get pvc -n itinventory-devops
```

## Acessos principais

### Backend

```text
http://localhost:8082/actuator/health
```

### Grafana

```text
http://localhost:3000
```

### Prometheus

```text
http://localhost:9090
```

## Dashboard de observabilidade

O projeto possui dashboard salvo no Grafana com foco em métricas da aplicação, permitindo visualizar de forma centralizada o comportamento do backend em execução.

**Nome do dashboard:**

```text
ITInventory Backend Observability
```

**Pasta:**

```text
ITInventory
```

## Boas práticas adotadas

- uso de imagem versionada no Docker Hub
- separação entre serviços internos e externos no Kubernetes
- persistência de métricas do Prometheus com PVC
- organização de dashboards em pasta própria no Grafana
- utilização de probes para maior confiabilidade operacional
- segregação entre configurações sensíveis e não sensíveis
- estrutura YAML separada por domínio técnico

## Próximas evoluções

- pipeline de entrega contínua com GitHub Actions
- stress test com evidência visual no Grafana
- exportação versionada do dashboard em JSON
- evolução da camada de observabilidade e automação

## Autor

Projeto mantido por **Claudio Almeida**.

## Aviso de autoria, uso e segurança

© **Claudio Almeida**. Todos os direitos reservados.

Este repositório foi estruturado para fins técnicos, acadêmicos e demonstrativos, contemplando código-fonte, artefatos de infraestrutura, manifests Kubernetes, documentação e configurações de observabilidade. A reprodução total ou parcial deste material deve respeitar a autoria do projeto e suas referências técnicas.

Por segurança, informações sensíveis como credenciais, segredos, tokens, senhas e dados de ambiente não devem ser expostas publicamente em repositórios, prints, documentação ou pipelines. Recomenda-se utilizar variáveis de ambiente, mecanismos de secret management e revisar continuamente os arquivos versionados antes de qualquer publicação.

O uso indevido, a cópia sem referência de autoria ou a exposição de configurações sensíveis é de responsabilidade exclusiva de quem reutilizar este conteúdo fora do contexto originalmente proposto.
