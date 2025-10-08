# Documentação Técnica - AccessPilot Backend

---

## 📋 Índice
1. [Introdução](#1-introdução)
2. [Visão Geral do Sistema](#2-visão-geral-do-sistema)
3. [Integrações](#3-integrações)
4. [Jornadas do Sistema](#4-jornadas-do-sistema)
5. [Arquitetura Técnica](#5-arquitetura-técnica)
6. [Configuração e Desenvolvimento](#6-configuração-e-desenvolvimento)

---

## 1. Introdução

### 1.1 O que é o AccessPilot?

O **AccessPilot** é uma plataforma corporativa de gestão de acessos baseada em papéis (RBAC - Role-Based Access Control) que automatiza e controla o processo de solicitação, aprovação e concessão de acessos a sistemas e recursos organizacionais.

### 1.2 Problema que Resolve

Em ambientes corporativos complexos, o gerenciamento manual de acessos apresenta diversos desafios:
- **Falta de rastreabilidade**: Dificuldade em auditar quem aprovou cada acesso
- **Processos lentos**: Aprovações manuais via e-mail ou planilhas
- **Riscos de segurança**: Concessão inadequada de privilégios
- **Falta de padronização**: Processos inconsistentes entre departamentos
- **Compliance**: Dificuldade em atender requisitos regulatórios

### 1.3 Solução Proposta

O AccessPilot centraliza e automatiza todo o fluxo de gestão de acessos através de:
- **Workflow automatizado** de solicitação e aprovação
- **Hierarquia de papéis** com herança de permissões
- **Auditoria completa** de todas as operações
- **Notificações em tempo real** para todas as partes envolvidas
- **Integração com sistemas de identidade** (Keycloak)
- **Gestão de documentos** com armazenamento seguro

### 1.4 Atores do Sistema

O sistema possui três perfis principais de usuários:

#### 1.4.1 Solicitante
- **Responsabilidade**: Solicitar acesso a papéis/recursos específicos
- **Permissões**: 
  - Criar novas solicitações
  - Visualizar suas próprias solicitações
  - Anexar documentos comprobatórios
  - Cancelar solicitações pendentes
  - Acompanhar status das solicitações

#### 1.4.2 Aprovador
- **Responsabilidade**: Analisar e decidir sobre solicitações de acesso
- **Permissões**:
  - Visualizar solicitações relacionadas aos papéis que gerencia
  - Aprovar ou rejeitar solicitações
  - Adicionar justificativas às decisões
  - Visualizar histórico de solicitações
  - Receber notificações de novas solicitações

#### 1.4.3 Administrador/Licenciador
- **Responsabilidade**: Configurar e gerenciar o sistema
- **Permissões**:
  - Gerenciar clientes e suas configurações
  - Criar e configurar papéis (roles)
  - Definir hierarquias de papéis
  - Configurar requisitos de documentação
  - Gerenciar usuários e suas permissões
  - Visualizar relatórios e métricas
  - Configurar integrações

---

## 2. Visão Geral do Sistema

### 2.1 Arquitetura Conceitual

O AccessPilot é construído seguindo uma arquitetura modular baseada em domínios (Domain-Driven Design), onde cada módulo representa um contexto de negócio específico.

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend (React)                          │
└─────────────────────────────────────────────────────────────┘
                            ↓ REST API
┌─────────────────────────────────────────────────────────────┐
│                   API Gateway / Controllers                  │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                    Camada de Serviços                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │ Request  │  │   User   │  │   Role   │  │  Client  │   │
│  │ Service  │  │ Service  │  │ Service  │  │ Service  │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              Camada de Persistência (JPA/Hibernate)         │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                   Banco de Dados PostgreSQL                  │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Módulos Principais

#### 2.2.1 Módulo de Solicitações (`request`)
**Responsabilidade**: Gerenciar todo o ciclo de vida das solicitações de acesso.

**Componentes**:
- `RequestEntity`: Entidade principal que representa uma solicitação
- `RequestService`: Lógica de negócio para criação e gestão de solicitações
- `RequestNotificationService`: Gerenciamento de notificações
- `RequestFileService`: Gestão de anexos
- `RequestController`: Endpoints REST

**Atributos principais**:
- `id`: Identificador único
- `protocolCode`: Código de protocolo único gerado automaticamente
- `status`: Status atual (CREATED, PENDING, APPROVED, REJECTED, CANCELED)
- `description`: Descrição/justificativa da solicitação
- `requestingUser`: Usuário solicitante
- `approvingUser`: Usuário que aprovou/rejeitou
- `role`: Papel solicitado
- `level`: Nível/esfera organizacional
- `codeItem`: Código do item específico (quando aplicável)
- `finalReason`: Justificativa final da aprovação/rejeição

#### 2.2.2 Módulo de Usuários (`user`)
**Responsabilidade**: Gerenciar informações e operações relacionadas aos usuários.

**Componentes**:
- `UserEntity`: Entidade que representa um usuário
- `UserService`: Operações de usuário e integração com Keycloak
- `UserRepository`: Acesso a dados

**Funcionalidades**:
- Importação automática de usuários do Keycloak
- Sincronização de dados de usuários
- Verificação de permissões
- Identificação de aprovadores

#### 2.2.3 Módulo de Papéis (`role`)
**Responsabilidade**: Gerenciar papéis e suas hierarquias.

**Componentes**:
- `RoleEntity`: Entidade que representa um papel
- `RoleService`: Lógica de negócio para gestão de papéis

**Atributos principais**:
- `id`: Identificador único
- `name`: Nome técnico do papel
- `label`: Nome amigável para exibição
- `description`: Descrição do papel
- `role`: Papel pai (hierarquia)
- `client`: Cliente ao qual o papel pertence
- `level`: Nível organizacional associado
- `active`: Indicador de ativação

**Hierarquia de Papéis**:
```
ADMIN (Papel Pai)
  └── GERENTE (Papel Filho)
       └── ANALISTA (Papel Neto)
```
- Usuários com papel pai podem aprovar solicitações de papéis filhos
- Herança de permissões: papéis filhos herdam permissões dos pais

#### 2.2.4 Módulo de Clientes (`client`)
**Responsabilidade**: Gerenciar clientes (organizações/sistemas) e suas configurações.

**Funcionalidades**:
- Configuração de requisitos de documentação
- Definição de políticas de aprovação
- Status de publicação (DRAFT, PUBLISHED)

#### 2.2.5 Módulo de Níveis (`level`)
**Responsabilidade**: Gerenciar níveis organizacionais (esferas).

**Tipos de Nível**:
- `BUILT_IN`: Níveis internos do sistema
- `BUSINESS`: Níveis de negócio customizados
- `EXTERNAL`: Níveis integrados de sistemas externos

#### 2.2.6 Módulo de Notificações (`notification`)
**Responsabilidade**: Envio de notificações por diversos canais.

**Tipos de Notificação**:
- E-mail
- Notificações web (in-app)
- Webhooks (futuramente)

#### 2.2.7 Módulo de Armazenamento (`storage`)
**Responsabilidade**: Gerenciar upload e armazenamento de arquivos.

**Funcionalidades**:
- Upload para Amazon S3
- Validação de tipos e tamanhos de arquivo
- Controle de acesso a documentos
- Versionamento de arquivos

#### 2.2.8 Módulo de Configuração (`configuration`)
**Responsabilidade**: Gerenciar configurações de anexos e validações.

**Funcionalidades**:
- Definir tipos de documentos obrigatórios
- Configurar validações de formato e tamanho
- Políticas de retenção

### 2.3 Fluxo de Dados

```
1. Usuário autentica via Keycloak
2. Token JWT é validado pelo Spring Security
3. Usuário cria solicitação via API REST
4. Sistema valida dados e permissões
5. Arquivos são enviados para S3
6. Solicitação é salva no banco de dados
7. Sistema identifica aprovadores
8. Notificações são enviadas
9. Aprovador toma decisão
10. Sistema atualiza status e notifica solicitante
11. Se aprovado, papel é concedido no Keycloak
```

### 2.4 Estados de uma Solicitação

```
CREATED → PENDING → APPROVED
                  ↓
                REJECTED
                  ↓
                CANCELED
```

**Descrição dos Estados**:
- **CREATED**: Solicitação criada, aguardando envio para aprovação
- **PENDING**: Solicitação enviada, aguardando análise do aprovador
- **APPROVED**: Solicitação aprovada, acesso concedido
- **REJECTED**: Solicitação rejeitada pelo aprovador
- **CANCELED**: Solicitação cancelada pelo solicitante

---

## 3. Integrações

### 3.1 Keycloak (Identity and Access Management)

**Propósito**: Gerenciamento centralizado de identidade e autenticação.

**Funcionalidades Integradas**:
- **Autenticação SSO**: Single Sign-On para todos os sistemas
- **Gerenciamento de Usuários**: Sincronização de dados de usuários
- **Gerenciamento de Papéis**: Atribuição de roles após aprovação
- **Tokens JWT**: Geração e validação de tokens de acesso
- **Federação de Identidade**: Suporte a múltiplos provedores de identidade

**Endpoints Utilizados**:
- `GET /admin/realms/{realm}/users`: Buscar usuários
- `GET /admin/realms/{realm}/clients/{client-id}/roles`: Listar papéis
- `POST /admin/realms/{realm}/users/{user-id}/role-mappings/clients/{client-id}`: Atribuir papel

**Fluxo de Integração**:
```
1. Usuário faz login no Keycloak
2. Keycloak retorna token JWT
3. Frontend envia token em cada requisição
4. Backend valida token e extrai informações do usuário
5. Ao aprovar solicitação, backend atribui papel no Keycloak
```

**Configuração**:
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://keycloak.example.com/realms/accesspilot
          jwk-set-uri: https://keycloak.example.com/realms/accesspilot/protocol/openid-connect/certs
```

### 3.2 Amazon S3 (Simple Storage Service)

**Propósito**: Armazenamento seguro e escalável de documentos anexados às solicitações.

**Funcionalidades Integradas**:
- **Upload de Arquivos**: Armazenamento de documentos comprobatórios
- **Download Seguro**: URLs pré-assinadas com expiração
- **Organização**: Estrutura de pastas por UUID de solicitação
- **Criptografia**: Dados criptografados em repouso

**Bucket Utilizado**:
- `private-getinsight-accesspilot-docs`: Bucket privado para documentos

**Estrutura de Armazenamento**:
```
private-getinsight-accesspilot-docs/
  └── {request-uuid}/
      ├── documento-identidade.pdf
      ├── comprovante-residencia.pdf
      └── termo-responsabilidade.pdf
```

**Políticas de Acesso**:
- Acesso restrito via IAM roles
- URLs pré-assinadas com validade de 1 hora
- Versionamento habilitado
- Lifecycle policy: retenção de 7 anos

### 3.3 Sistema de E-mail (SMTP)

**Propósito**: Envio de notificações por e-mail.

**Tipos de E-mails Enviados**:
- **Nova Solicitação**: Notifica aprovadores sobre nova solicitação
- **Solicitação Aprovada**: Notifica solicitante sobre aprovação
- **Solicitação Rejeitada**: Notifica solicitante sobre rejeição
- **Solicitação Pendente**: Lembretes para aprovadores
- **Status Atualizado**: Mudanças de status

**Templates de E-mail**:
- `request.html`: Template para notificação de nova solicitação
- `approval.html`: Template para notificação de aprovação
- `rejection.html`: Template para notificação de rejeição

**Variáveis Disponíveis nos Templates**:
- `{requesterName}`: Nome do solicitante
- `{approverName}`: Nome do aprovador
- `{roleName}`: Nome do papel solicitado
- `{protocolCode}`: Código de protocolo
- `{requestDate}`: Data da solicitação
- `{status}`: Status atual
- `{finalReason}`: Justificativa final

### 3.4 Sistemas Externos (Level Client)

**Propósito**: Integração com sistemas externos para validação de itens organizacionais.

**Funcionalidades**:
- **Validação de Itens**: Verificar existência de códigos de itens externos
- **Sincronização**: Buscar informações atualizadas de itens
- **Hierarquia**: Obter estrutura organizacional externa

**Tipos de Integração**:
- **API REST**: Chamadas HTTP para sistemas externos
- **Autenticação**: API Key ou OAuth2
- **Cache**: Resultados cacheados para performance

---

## 4. Jornadas do Sistema

### 4.1 Jornada do Solicitante

#### 4.1.1 Criar Nova Solicitação de Acesso

**Objetivo**: Solicitar acesso a um papel específico no sistema.

**Pré-condições**:
- Usuário autenticado no sistema
- Papel desejado está ativo e publicado
- Cliente associado ao papel está com status PUBLISHED

**Passo a Passo**:

1. **Autenticação**
   - Usuário acessa o sistema
   - Sistema redireciona para Keycloak
   - Usuário faz login (credenciais ou SSO)
   - Keycloak retorna token JWT
   - Sistema valida token e carrega perfil do usuário

2. **Seleção do Papel**
   - Usuário navega pela lista de papéis disponíveis
   - Sistema exibe papéis filtrados por:
     - Cliente/organização
     - Nível organizacional
     - Permissões do usuário
   - Usuário seleciona o papel desejado

3. **Preenchimento do Formulário**
   - Sistema exibe formulário com campos:
     - **Descrição/Justificativa** (obrigatório): Motivo da solicitação
     - **Código do Item** (condicional): Código específico se o papel requer
     - **Documentos** (condicional): Anexos obrigatórios configurados
   
4. **Validação de Item** (se aplicável)
   - Sistema verifica se o papel requer código de item
   - Valida existência do item conforme tipo de nível:
     - **BUILT_IN/BUSINESS**: Consulta banco de dados interno
     - **EXTERNAL**: Consulta API externa
   - Exibe erro se item não encontrado

5. **Upload de Documentos**
   - Sistema lista documentos obrigatórios (configurados no cliente)
   - Usuário faz upload de cada documento
   - Sistema valida:
     - Formato do arquivo (PDF, JPG, PNG, DOCX)
     - Tamanho máximo (10MB por arquivo)
     - Quantidade de arquivos por tipo
   - Arquivos são enviados para S3

6. **Submissão da Solicitação**
   - Usuário clica em "Enviar Solicitação"
   - Sistema executa:
     ```java
     // Validações
     - Verifica duplicidade de solicitação ativa
     - Valida todos os campos obrigatórios
     - Confirma upload de documentos obrigatórios
     
     // Criação
     - Gera código de protocolo único
     - Cria registro no banco com status CREATED
     - Associa arquivos à solicitação
     - Identifica aprovadores (usuários com papel pai)
     - Muda status para PENDING
     
     // Notificações
     - Envia e-mail para cada aprovador
     - Cria notificação web para aprovadores
     - Envia confirmação para solicitante
     ```

7. **Confirmação**
   - Sistema exibe mensagem de sucesso
   - Mostra código de protocolo gerado
   - Redireciona para página de acompanhamento

**Endpoint Utilizado**:
```http
POST /v1/requests
Content-Type: multipart/form-data

{
  "request": {
    "description": "Necessito acesso para realizar análises financeiras",
    "roleId": 123,
    "codeItem": "DEPT-001"
  },
  "attachments": {
    "documento_identidade": [file],
    "comprovante_vinculo": [file]
  }
}
```

**Resposta de Sucesso**:
```http
HTTP/1.1 201 Created
Location: /v1/requests/456

{
  "id": 456,
  "protocolCode": "REQ-2025-0001234",
  "status": "PENDING",
  "createdAt": "2025-10-04T15:30:00Z"
}
```

**Regras de Negócio**:
- ✅ Usuário não pode ter solicitação PENDING para o mesmo papel
- ✅ Código de protocolo é único e sequencial
- ✅ Status inicial é sempre CREATED, muda para PENDING após notificações
- ✅ Pelo menos um aprovador deve existir para o papel pai
- ✅ Todos os documentos obrigatórios devem ser anexados

**Possíveis Erros**:
- `400 Bad Request`: Dados inválidos ou documentos faltando
- `404 Not Found`: Papel não encontrado ou inativo
- `409 Conflict`: Solicitação duplicada ativa
- `422 Unprocessable Entity`: Item não encontrado ou cliente não publicado

---

#### 4.1.2 Acompanhar Solicitações

**Objetivo**: Visualizar status e histórico das solicitações realizadas.

**Passo a Passo**:

1. **Acesso à Lista**
   - Usuário acessa "Minhas Solicitações"
   - Sistema carrega solicitações do usuário autenticado

2. **Visualização**
   - Lista paginada com informações:
     - Código de protocolo
     - Papel solicitado
     - Data de criação
     - Status atual
     - Aprovador (se houver)
   
3. **Filtros Disponíveis**:
   - Status (PENDING, APPROVED, REJECTED, CANCELED)
   - Data de criação
   - Papel solicitado
   - Código de protocolo

4. **Detalhes da Solicitação**
   - Usuário clica em uma solicitação
   - Sistema exibe:
     - Informações completas
     - Histórico de mudanças de status
     - Documentos anexados
     - Justificativa final (se aprovada/rejeitada)

**Endpoint Utilizado**:
```http
GET /v1/requests/me/paginated?pageIndex=1&pageSize=10&status=PENDING
```

---

#### 4.1.3 Cancelar Solicitação

**Objetivo**: Cancelar uma solicitação pendente.

**Pré-condições**:
- Solicitação deve estar com status PENDING
- Usuário deve ser o solicitante

**Passo a Passo**:

1. **Seleção**
   - Usuário acessa detalhes da solicitação
   - Clica em "Cancelar Solicitação"

2. **Confirmação**
   - Sistema exibe modal de confirmação
   - Usuário pode adicionar motivo do cancelamento

3. **Processamento**
   - Sistema atualiza status para CANCELED
   - Registra motivo do cancelamento
   - Notifica aprovadores sobre cancelamento

**Endpoint Utilizado**:
```http
PUT /v1/requests/{id}
Content-Type: application/json

{
  "status": "CANCELED",
  "finalReason": "Não preciso mais deste acesso"
}
```

**Regras de Negócio**:
- ✅ Apenas solicitações PENDING podem ser canceladas
- ✅ Solicitações APPROVED ou REJECTED não podem ser canceladas
- ✅ Aprovadores são notificados sobre o cancelamento

---

### 4.2 Jornada do Aprovador

#### 4.2.1 Receber Notificação de Nova Solicitação

**Objetivo**: Ser notificado quando uma nova solicitação requer sua aprovação.

**Passo a Passo**:

1. **Identificação de Aprovadores**
   - Sistema identifica papel solicitado
   - Busca papel pai na hierarquia
   - Identifica usuários com o papel pai no Keycloak
   - Importa/sincroniza dados desses usuários

2. **Envio de Notificações**
   - **E-mail**:
     - Assunto: "Nova solicitação de acesso - {protocolCode}"
     - Corpo: Template HTML com detalhes da solicitação
     - Link direto para análise
   - **Notificação Web**:
     - Badge de notificação no sistema
     - Lista de solicitações pendentes

3. **Conteúdo da Notificação**:
   - Nome do solicitante
   - Papel solicitado
   - Código de protocolo
   - Data da solicitação
   - Justificativa
   - Link para análise

**Exemplo de E-mail**:
```
Olá, João Silva!

Você recebeu uma nova solicitação de acesso para análise:

Protocolo: REQ-2025-0001234
Solicitante: Maria Santos
Papel: Analista Financeiro
Data: 04/10/2025 15:30

Justificativa:
"Necessito acesso para realizar análises financeiras do departamento."

[Analisar Solicitação]

---
AccessPilot - Sistema de Gestão de Acessos
```

---

#### 4.2.2 Analisar Solicitação

**Objetivo**: Revisar detalhes de uma solicitação e tomar decisão.

**Pré-condições**:
- Usuário deve ter papel pai do papel solicitado
- Solicitação deve estar com status PENDING

**Passo a Passo**:

1. **Acesso à Lista de Solicitações**
   - Aprovador acessa "Solicitações para Aprovar"
   - Sistema lista solicitações dos papéis que ele gerencia
   - Filtros disponíveis:
     - Status
     - Papel solicitado
     - Data
     - Solicitante

2. **Visualização de Detalhes**
   - Aprovador clica na solicitação
   - Sistema exibe:
     - **Dados do Solicitante**:
       - Nome completo
       - E-mail
       - Departamento/unidade
     - **Dados da Solicitação**:
       - Papel solicitado
       - Código de protocolo
       - Data de criação
       - Justificativa
       - Código do item (se aplicável)
     - **Documentos Anexados**:
       - Lista de arquivos
       - Botão para visualizar/baixar cada arquivo
     - **Histórico**:
       - Mudanças de status
       - Ações anteriores

3. **Análise de Documentos**
   - Aprovador clica para visualizar cada documento
   - Sistema gera URL pré-assinada do S3
   - Documento é exibido/baixado
   - Aprovador verifica conformidade

4. **Validações do Aprovador**:
   - Justificativa é adequada?
   - Documentos estão corretos e válidos?
   - Solicitante tem necessidade legítima?
   - Não há conflito de interesse?
   - Atende políticas de segurança?

**Endpoint Utilizado**:
```http
GET /v1/requests/paginated-by-roles?roles=ROLE_GERENTE&pageIndex=1&pageSize=10
```

---

#### 4.2.3 Aprovar Solicitação

**Objetivo**: Conceder acesso ao solicitante.

**Passo a Passo**:

1. **Decisão de Aprovação**
   - Aprovador clica em "Aprovar"
   - Sistema exibe modal de confirmação
   - Aprovador pode adicionar comentários (opcional)

2. **Processamento da Aprovação**
   - Sistema executa:
     ```java
     // Validações
     - Verifica se aprovador tem permissão
     - Confirma que solicitação está PENDING
     - Valida que cliente está PUBLISHED
     
     // Atualização
     - Muda status para APPROVED
     - Registra aprovador e data/hora
     - Salva justificativa (se fornecida)
     
     // Concessão de Acesso
     - Chama Keycloak API
     - Atribui papel ao usuário solicitante
     - Registra concessão no banco
     
     // Notificações
     - Envia e-mail para solicitante
     - Cria notificação web
     - Registra em log de auditoria
     ```

3. **Confirmação**
   - Sistema exibe mensagem de sucesso
   - Solicitação sai da lista de pendentes
   - Aprovador pode ver no histórico

**Endpoint Utilizado**:
```http
PUT /v1/requests/{id}
Content-Type: application/json

{
  "status": "APPROVED",
  "finalReason": "Solicitação aprovada. Documentação em conformidade."
}
```

**Resposta de Sucesso**:
```http
HTTP/1.1 202 Accepted

{
  "message": "Solicitação aprovada com sucesso",
  "requestId": 456,
  "status": "APPROVED"
}
```

**Regras de Negócio**:
- ✅ Aprovador deve ter papel pai do papel solicitado
- ✅ Solicitação deve estar PENDING
- ✅ Cliente deve estar PUBLISHED
- ✅ Papel é atribuído automaticamente no Keycloak
- ✅ Solicitante é notificado imediatamente
- ✅ Ação é registrada em log de auditoria

**Notificação ao Solicitante**:
```
Olá, Maria Santos!

Sua solicitação de acesso foi APROVADA!

Protocolo: REQ-2025-0001234
Papel: Analista Financeiro
Aprovador: João Silva
Data de Aprovação: 04/10/2025 16:45

Comentários do Aprovador:
"Solicitação aprovada. Documentação em conformidade."

Seu acesso já está ativo e você pode começar a utilizar o sistema.

[Acessar Sistema]
```

---

#### 4.2.4 Rejeitar Solicitação

**Objetivo**: Negar acesso ao solicitante com justificativa.

**Passo a Passo**:

1. **Decisão de Rejeição**
   - Aprovador clica em "Rejeitar"
   - Sistema exibe modal de confirmação
   - **Justificativa é obrigatória** para rejeição

2. **Preenchimento de Justificativa**
   - Campo de texto para motivo da rejeição
   - Exemplos de motivos:
     - "Documentação incompleta ou inválida"
     - "Solicitante não atende pré-requisitos"
     - "Necessidade não justificada adequadamente"
     - "Conflito de interesse identificado"

3. **Processamento da Rejeição**
   - Sistema executa:
     ```java
     // Validações
     - Verifica se aprovador tem permissão
     - Confirma que solicitação está PENDING
     - Valida que justificativa foi fornecida
     
     // Atualização
     - Muda status para REJECTED
     - Registra aprovador e data/hora
     - Salva justificativa obrigatória
     
     // Notificações
     - Envia e-mail para solicitante
     - Cria notificação web
     - Registra em log de auditoria
     ```

4. **Confirmação**
   - Sistema exibe mensagem de sucesso
   - Solicitação sai da lista de pendentes
   - Fica disponível no histórico

**Endpoint Utilizado**:
```http
PUT /v1/requests/{id}
Content-Type: application/json

{
  "status": "REJECTED",
  "finalReason": "Documentação incompleta. Favor anexar comprovante de vínculo atualizado."
}
```

**Regras de Negócio**:
- ✅ Justificativa é obrigatória para rejeição
- ✅ Solicitante pode criar nova solicitação após rejeição
- ✅ Documentos da solicitação rejeitada são mantidos
- ✅ Histórico completo é preservado para auditoria

**Notificação ao Solicitante**:
```
Olá, Maria Santos!

Sua solicitação de acesso foi REJEITADA.

Protocolo: REQ-2025-0001234
Papel: Analista Financeiro
Aprovador: João Silva
Data de Rejeição: 04/10/2025 16:45

Motivo da Rejeição:
"Documentação incompleta. Favor anexar comprovante de vínculo atualizado."

Você pode criar uma nova solicitação após corrigir os problemas apontados.

[Nova Solicitação]
```

---

### 4.3 Jornada do Administrador

#### 4.3.1 Configurar Cliente

**Objetivo**: Criar e configurar um novo cliente (organização/sistema).

**Passo a Passo**:

1. **Criação do Cliente**
   - Acessa módulo de administração
   - Clica em "Novo Cliente"
   - Preenche dados:
     - Nome
     - UUID do cliente no Keycloak
     - Descrição
     - Status (DRAFT/PUBLISHED)

2. **Configuração de Documentos**
   - Define documentos obrigatórios:
     - Chave identificadora
     - Nome amigável
     - Descrição
     - Obrigatório (sim/não)
     - Formatos aceitos
     - Tamanho máximo

3. **Publicação**
   - Após configuração completa
   - Muda status para PUBLISHED
   - Cliente fica disponível para solicitações

---

#### 4.3.2 Gerenciar Hierarquia de Papéis

**Objetivo**: Criar papéis e definir hierarquia de aprovação.

**Passo a Passo**:

1. **Criação de Papel**
   - Acessa "Gerenciar Papéis"
   - Clica em "Novo Papel"
   - Preenche:
     - Nome técnico
     - Label (nome amigável)
     - Descrição
     - Cliente associado
     - Nível organizacional
     - Papel pai (para hierarquia)

2. **Definição de Hierarquia**
   ```
   Exemplo:
   ADMIN (sem pai)
     └── GERENTE (pai: ADMIN)
          └── ANALISTA (pai: GERENTE)
               └── ESTAGIARIO (pai: ANALISTA)
   ```

3. **Sincronização com Keycloak**
   - Sistema sincroniza papéis com Keycloak
   - Mantém mapeamento entre IDs

**Regras de Negócio**:
- ✅ Papel pai pode aprovar solicitações de papéis filhos
- ✅ Hierarquia pode ter múltiplos níveis
- ✅ Papel sem pai é considerado raiz
- ✅ Alteração de hierarquia não afeta solicitações em andamento

---

#### 4.3.3 Visualizar Relatórios e Métricas

**Objetivo**: Acompanhar métricas e gerar relatórios do sistema.

**Métricas Disponíveis**:
- Total de solicitações por período
- Taxa de aprovação/rejeição
- Tempo médio de aprovação
- Solicitações por papel
- Solicitações por departamento
- Aprovadores mais ativos
- Documentos mais rejeitados

**Relatórios**:
- Auditoria completa de acessos
- Histórico de solicitações por usuário
- Papéis atribuídos por período
- Conformidade e compliance

---

## 5. Arquitetura Técnica

### 5.1 Stack Tecnológico

**Backend**:
- Java 17+
- Spring Boot 3.x
- Spring Security (OAuth2 Resource Server)
- Spring Data JPA
- Hibernate (ORM)
- Hibernate Envers (Auditoria)
- Maven (Gerenciamento de dependências)

**Banco de Dados**:
- PostgreSQL 13+
- Flyway (Migrations)

**Segurança**:
- Keycloak (Identity Provider)
- JWT (JSON Web Tokens)
- OAuth 2.0 / OpenID Connect

**Armazenamento**:
- Amazon S3 (Arquivos)

**Comunicação**:
- REST API
- SMTP (E-mail)

### 5.2 Estrutura de Pacotes

```
it.getinsight/
├── core/                          # Componentes core reutilizáveis
│   ├── dynamicquery/             # Queries dinâmicas
│   ├── exception/                # Exceções customizadas
│   ├── helper/                   # Classes auxiliares
│   ├── message/                  # Mensagens i18n
│   ├── model/                    # Modelos base
│   └── pagination/               # Paginação
│
├── module/                        # Módulos de negócio
│   ├── request/                  # Solicitações
│   │   ├── controller/v1/        # Controllers REST
│   │   ├── dto/                  # Data Transfer Objects
│   │   ├── entity/               # Entidades JPA
│   │   ├── enuns/                # Enumerações
│   │   ├── mapper/               # Mappers (DTO <-> Entity)
│   │   ├── repository/           # Repositórios JPA
│   │   ├── service/              # Lógica de negócio
│   │   └── util/                 # Utilitários
│   │
│   ├── user/                     # Usuários
│   ├── role/                     # Papéis
│   ├── client/                   # Clientes
│   ├── level/                    # Níveis organizacionais
│   ├── notification/             # Notificações
│   ├── storage/                  # Armazenamento
│   ├── configuration/            # Configurações
│   ├── keycloak/                 # Integração Keycloak
│   └── email/                    # E-mail
│
└── Application.java               # Classe principal
```

### 5.3 Padrões de Projeto Utilizados

- **Repository Pattern**: Acesso a dados
- **Service Layer**: Lógica de negócio
- **DTO Pattern**: Transferência de dados
- **Mapper Pattern**: Conversão entre camadas
- **Specification Pattern**: Queries dinâmicas
- **Builder Pattern**: Construção de objetos complexos

### 5.4 Segurança

**Autenticação**:
- OAuth 2.0 com Keycloak
- Tokens JWT validados em cada requisição
- Refresh tokens para renovação

**Autorização**:
- RBAC (Role-Based Access Control)
- Validação de permissões em cada endpoint
- Verificação de hierarquia de papéis

**Auditoria**:
- Hibernate Envers para auditoria de entidades
- Log de todas as operações sensíveis
- Rastreabilidade completa de mudanças

### 5.5 Performance

**Caching**:
- Cache de usuários do Keycloak
- Cache de papéis e hierarquias

**Otimizações**:
- Lazy loading de relacionamentos JPA
- Paginação de resultados
- Índices no banco de dados
- Connection pooling

---

## 6. Configuração e Desenvolvimento

### 6.1 Pré-requisitos

- Java 17 ou superior
- Maven 3.8+
- PostgreSQL 13+
- Keycloak 18+
- Conta AWS (para S3)
- Git

### 6.2 Configuração do Ambiente

#### 1. Clonar o Repositório
```bash
git clone [URL_DO_REPOSITORIO]
cd backend-accesspilot
```

#### 2. Configurar Banco de Dados
```sql
CREATE DATABASE accesspilot;
CREATE USER accesspilot_user WITH PASSWORD 'senha_segura';
GRANT ALL PRIVILEGES ON DATABASE accesspilot TO accesspilot_user;
```

#### 3. Configurar application.yml
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/accesspilot
    username: accesspilot_user
    password: senha_segura
  
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://keycloak.example.com/realms/accesspilot

aws:
  s3:
    bucket: private-getinsight-accesspilot-docs
    region: us-east-1
    access-key: ${AWS_ACCESS_KEY}
    secret-key: ${AWS_SECRET_KEY}

mail:
  host: smtp.example.com
  port: 587
  username: ${MAIL_USERNAME}
  password: ${MAIL_PASSWORD}
```

#### 4. Executar Aplicação
```bash
mvn spring-boot:run
```

### 6.3 Testes

```bash
# Executar todos os testes
mvn test

# Executar com cobertura
mvn test jacoco:report

# Executar testes de integração
mvn verify
```

### 6.4 Build e Deploy

```bash
# Gerar JAR
mvn clean package

# Executar JAR
java -jar target/accesspilot-backend-1.0.0.jar

# Build Docker
docker build -t accesspilot-backend .

# Run Docker
docker run -p 8080:8080 accesspilot-backend
```

### 6.5 Documentação da API

Após iniciar a aplicação, acesse:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### 6.6 Git Flow

- `main`: Produção
- `develop`: Desenvolvimento
- `feature/*`: Novas funcionalidades
- `bugfix/*`: Correções de bugs
- `hotfix/*`: Correções urgentes em produção

### 6.7 Convenções de Código

- Seguir Java Code Conventions
- Nomes em inglês para código
- Comentários e documentação em português
- Cobertura de testes mínima: 80%
- Code review obrigatório

---

## 📞 Suporte e Contato

Para dúvidas, problemas ou sugestões:
- **E-mail**: suporte@getinsight.it
- **Documentação**: [Wiki Interna]
- **Issues**: [GitLab Issues]

---

**Última atualização**: 04/10/2025  
**Versão do documento**: 1.0  
**Mantido por**: Equipe GetInsight
