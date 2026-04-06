# Plano (draft) — Convites + pré-preenchimento da Solicitação de Acesso

Contexto: implementar uma feature de **convites** em que o usuário recebe um link, visualiza o convite (mesmo sem login) e, ao prosseguir, é direcionado para a tela **já existente** de “Solicitar acesso” com os campos **pré-preenchidos** a partir do registro do convite.

Este documento é propositalmente detalhado para ser revisado várias vezes antes de implementar.

---

## Objetivos

1) Permitir que um usuário receba um convite por e-mail e consiga:
   - abrir uma **tela pública** (sem autenticação) para visualizar o convite e saber se está válido/expirado/cancelado;
   - clicar em **Continuar** e ser direcionado ao fluxo de autenticação (Keycloak) e, depois, para a tela existente de “Solicitar acesso” já pré-preenchida;
   - concluir a solicitação (descrição/anexos) e enviar normalmente.

2) Permitir que o convidador cancele convites já enviados (por pessoa no MVP; em massa via `protocol_code` no futuro).

3) Garantir segurança e consistência:
   - convite **não é consumido** apenas por visualizar/continuar;
   - convite só é marcado como **CONSUMED** quando uma solicitação é **efetivamente criada**;
   - impedir que um token de convite seja usado por outro e-mail/conta.

---

## Escopo da entrega atual

Nesta etapa, o objetivo é **entregar apenas o backend** (persistência + regras + endpoints + integração com Keycloak) de forma que o agora  consiga implementar a experiência completa depois, sem retrabalho de contrato.

- ✅ Backend (neste repo):
  - Persistência `TB_CONVITE` + status/transições
  - Endpoints públicos para tela do convite e “auth intent”
  - Endpoints autenticados para `request-context` e convites pendentes
  - Ajuste do `POST /v1/requests` para aceitar `invitationToken` e consumir o convite em transação
  - Cancelamento **individual** (`/v1/invitations/{id}/cancel`)
- ⏳ Frontend (não implementar agora):
  - Rota/tela pública do convite + integração com Keycloak
  - Prefill/lock na tela existente “Solicitar acesso”
  - Submissão com `invitationToken`

Plano do frontend (negocial + técnico) está em: `etc/plan-convite-prefill-frontend.md`.

---

## Não-objetivos (por enquanto)

- Não transformar “convite” em um mecanismo geral de RBAC; ele só serve para pré-preencher e vincular a solicitação.
- Não definir UX final de cadastro/login no Keycloak (tema/URLs avançadas) — vamos usar integração padrão.
- Não criar um fluxo de “revogar solicitação criada via convite” (isso é outra feature).
- Não implementar alterações no frontend nesta etapa (ver `etc/plan-convite-prefill-frontend.md`).

---

## Glossário

- **Convite**: registro persistido no backend contendo dados para pré-preencher a solicitação.
- **Token do convite**: string aleatória enviada no link do e-mail. O backend persiste apenas **hash** desse token.
- **Request-context**: dados retornados do backend para a UI preencher/travar a tela existente de solicitação (client/role/esfera + defaults).
- **Consumir**: marcar o convite como usado (`CONSUMED`) e vincular a solicitação criada (`requestId`).

---

## Fluxo (alto nível)

> Observação: este fluxo descreve a experiência completa. Nesta etapa vamos implementar **apenas backend**; as mudanças de frontend estão planejadas em `etc/plan-convite-prefill-frontend.md`.

### A) Usuário abre o convite (sem login)
1) Usuário clica no link recebido.
2) Front chama `GET /v1/public/invitations/{token}`:
   - Se inválido/expirado/cancelado: mostra a tela de status e encerra.
   - Se válido: mostra detalhes mínimos + botão **Continuar** e **Fechar**.

### B) Usuário clica “Continuar”
3) **Não muda status do convite**.
4) Front decide qual ação iniciar no Keycloak:
   - login (usuário já tem conta) ou
   - cadastro (usuário ainda não tem conta).

> Decisão: o backend retorna JSON “auth intent” (ver endpoints) para que o front chame `keycloak.login()` ou `keycloak.register()` sem precisar montar URL manualmente.

5) Após o callback do OIDC, a aplicação redireciona para a tela existente:
   - `/solicitar-acesso?invitationToken=...` (ou o token pode ser guardado em `sessionStorage` e removido da URL).

### C) Tela existente “Solicitar acesso” com request-context
6) Se existe `invitationToken`, a tela chama o backend autenticado:
   - `GET /v1/invitations/{token}/request-context`
7) A UI preenche e trava campos derivados do convite (ex.: `clientId`, `roleId`, `codeItem`) e mantém editáveis os campos normais (ex.: descrição/anexos).

### D) Usuário envia a solicitação
8) Na submissão, se existe `invitationToken`, o front cria a solicitação via `POST /v1/requests` (endpoint já existente), passando o token, e o backend **consome o convite no mesmo fluxo**.

---

## Modelo de dados (Convite)

Tabela sugerida: `TB_CONVITE`.

Decisão de modelagem:
- **1 convite = 1 destinatário** (1 token + 1 status por e-mail).
- “Envio em lote” cria **vários registros** em `TB_CONVITE`, todos com o mesmo `protocol_code` (redundância intencional e aceitável).

Campos mínimos:
- `id` (sequence)
- `uuid`
- `token_hash` (SHA-256 do token base64url; **unique**)
- `email` (destinatário)
- `role_id` (FK `TB_ROLE.ID`)
- `code_item` (string; tratar como `externalCode`)
- `description` (opcional)
- `status` (`PENDING | CONSUMED | EXPIRED | CANCELLED`)
- `expires_at`
- `protocol_code` (string; “protocolo” human-friendly do lote, para exibir na UI e cancelar em massa)
- `request_id` (FK `TB_SOLICITACAO.ID`, opcional)
- auditoria (colunas padrão do projeto: `CRIADO_POR`, `DATA_CRIACAO`, `ATUALIZADO_POR`, `DATA_ATUALIZACAO`)

Índices:
- `token_hash` unique
- `(email, status)`
- `protocol_code`
- `expires_at`

Liquibase:
- 1 migration para tabela + índices
- 1 migration para tabela AUD (se seguir o padrão Envers do repo)

---

## Status e transições

Estados:
- `PENDING`: convite válido e ainda não consumido
- `CANCELLED`: convite cancelado pelo convidador (perfil `INVITE_SENDER`)
- `EXPIRED`: convite expirado (pode ser derivado por `expires_at < now`, mas persistir status facilita)
- `CONSUMED`: solicitação criada via convite e vinculada

Regras:
- Visualizar ou clicar em “Continuar” **não** muda status.
- `request-context` só funciona se:
  - `status == PENDING`
  - `expires_at >= now`
- `consume` só acontece durante a criação da solicitação e deve ser idempotente:
  - se já `CONSUMED`, retornar `requestId` existente (melhor UX)
- Para auditoria de quem/quando consumiu ou cancelou: usar `ATUALIZADO_POR`/`DATA_ATUALIZACAO` (convite deve ser imutável após `CONSUMED`).

---

## Endpoints (proposta)

### Públicos (`/v1/public/...`)

#### 1) Consultar convite (tela pública)
`GET /v1/public/invitations/{token}`

Resposta (exemplo):
```json
{
  "status": "VALID|EXPIRED|CANCELLED|NOT_FOUND",
  "expiresAt": "2026-03-04T12:00:00Z",
  "emailMasked": "f***@getinsight.tech",
  "role": { "id": 123, "name": "ADMIN", "label": "Administrador" },
  "client": { "id": 10, "name": "accesspilot-frontend", "label": "Access Pilot" }
}
```

Notas:
- Nunca retornar o e-mail completo no público (preferir mascarado).
- Para token inválido: retornar `NOT_FOUND` (e opcionalmente HTTP 404).

#### 2) Auth intent (JSON para o front decidir login/cadastro)
`GET /v1/public/invitations/{token}/auth-intent`

Resposta (exemplo):

```json
{
  "status": "VALID|EXPIRED|CANCELLED|NOT_FOUND",
  "nextStep": "REDIRECT_TO_LOGIN|REDIRECT_TO_REGISTER",
  "loginHint": "fulano@getinsight.tech",
  "invitationToken": "<token>"
}
```

Implementação:
- Para decidir `nextStep`, o backend precisa consultar o IdP (Keycloak) por e-mail.
- **Importante**: este endpoint só aceita `{token}` (nunca e-mail como parâmetro), para evitar enumeração fora do contexto do convite.

> Observação: se não quisermos depender de “descobrir automaticamente”, podemos sempre retornar `REDIRECT_TO_LOGIN` e ainda passar `loginHint`. Mas a decisão atual é “auto”.

### Autenticados (`/v1/invitations/...`)

#### 3) Request-context (para a tela existente)
`GET /v1/invitations/{token}/request-context`

Validações:
- convite existe e está `PENDING`
- não expirado
- não cancelado
- e-mail do convite == e-mail do JWT atual (`claim email`)

Resposta (exemplo):
```json
{
  "invitationToken": "<token>",
  "lockedFields": ["clientId", "roleId", "codeItem"],
  "clientId": "accesspilot-frontend",
  "roleId": 123,
  "roleLabel": "Administrador",
  "clientLabel": "Access Pilot",
  "codeItem": "ABC-123",
  "description": "..."
}
```

#### 4) Listar convites pendentes do usuário logado (entrada “sem link”)
`GET /v1/invitations/pending`

Retorna convites `PENDING` para o `email` do JWT.

#### 5) Criar solicitação com token de convite (consome em transação)
`POST /v1/requests` (multipart/form-data) — **endpoint já existente**

Contrato de entrada:
- Mantém o contrato atual (multipart com o JSON `request` + anexos).
- Adicionar ao JSON `request` um campo opcional: `invitationToken`.
- Se `invitationToken` existir, o backend deve **ignorar/sobrescrever** `roleId` e `codeItem` com os valores do convite.

Comportamento:
- Se `invitationToken` não existir, segue o fluxo atual.
- Se `invitationToken` existir:
  - valida convite (`PENDING`, não expirado, não cancelado);
  - valida *email binding* (e-mail do convite == e-mail do JWT);
  - cria a solicitação com `roleId`/`codeItem` do convite (reusando o fluxo atual de criação);
  - marca o convite como `CONSUMED` e salva `requestId`;
  - idempotência: se o convite já estiver `CONSUMED`, responder sucesso apontando para a solicitação já criada (ex.: `200 OK` com `Location: /v1/requests/{id}`).

---

## Cancelamento (arrependi / remover pessoa)

Criar endpoint (perfil `INVITE_SENDER`) (MVP):

- `POST /v1/invitations/{id}/cancel`

Preparado para o futuro (não implementar agora):
- Cancelamento em massa por `protocol_code` (coluna já existe).
- Endpoint futuro: `POST /v1/invitations/protocols/{protocolCode}/cancel`

Regras:
- Cancelar apenas se `status == PENDING` (ou permitir até antes de “CONSUMED”, conforme decisão).
- Se `CONSUMED`, cancelar não desfaz solicitação; no máximo marca convite como “cancelled-after-consumed” (se precisar de auditoria), mas isso é opcional.

---

## Integração com Keycloak (auto login vs cadastro)

### 1) Passar `login_hint`
Sempre que iniciar OIDC, passar `login_hint=<email_do_convite>` para melhorar UX.

### 2) Descobrir se usuário existe (auto)
Necessário:
- Adicionar ao `KeycloakClient` um endpoint de busca de usuários por e-mail (Admin API), ex.:
  - `GET /users?email=<email>&exact=true`
ou `search`.

O backend, no `auth-intent`, decide:
- se encontrou usuário -> `REDIRECT_TO_LOGIN`
- se não encontrou -> `REDIRECT_TO_REGISTER`

O front então chama:
- `keycloak.login({ loginHint, redirectUri: ... })` ou
- `keycloak.register({ loginHint, redirectUri: ... })`

---

## Segurança e consistência

1) **Token não deve ser persistido em claro**: salvar apenas `SHA-256(token)` como `token_hash`.
2) **Tokens são secrets**:
   - não logar token em texto (nem no backend, nem no front).
3) **Não enumeração**:
   - endpoints públicos nunca aceitam e-mail e respondem “user exists”.
   - o “auto” só ocorre dado um token válido.
4) **Email binding**:
   - `request-context` e `create request` exigem que o e-mail do convite seja o mesmo do JWT.
5) **Idempotência na criação**:
   - garantir “no máximo 1 solicitação por convite” (via transação + status + unique).
6) **Rate-limit** (opcional, mas recomendado):
   - evitar brute force de token no endpoint público.

---

## Decisões pendentes (para revisão)

1) O token ficará na URL (`?invitationToken=`) ou só em `sessionStorage`?
2) O `auth-intent` deve retornar sempre `REDIRECT_TO_LOGIN` em caso de falha no lookup do Keycloak, ou devemos falhar duro?
3) Cancelamento permitido apenas em `PENDING` ou também quando “request-context já foi aberto” (se tivermos um status intermediário no futuro)?
4) Onde trafegar `invitationToken` na criação de solicitação: no JSON `request` (recomendado) ou em header?

---

## Plano de implementação (fases)

### Fase 0 — Contratos/decisões
- Fechar as decisões pendentes acima (URL vs sessionStorage; fallback de lookup; regras de cancelamento).
- Alinhar payload do `request-context` com a tela existente.
- Definir formato do `protocol_code` (ex.: `INV-2026-000123`).

### Fase 1 — Persistência
- Criar entidade `InvitationEntity` + repository + enum status.
- Criar migrations Liquibase (tabela + índices + AUD se aplicável).

### Fase 2 — Endpoints públicos
- `GET /v1/public/invitations/{token}`
- `GET /v1/public/invitations/{token}/auth-intent`

### Fase 3 — Endpoints autenticados
- `GET /v1/invitations/{token}/request-context`
- `GET /v1/invitations/pending`
- Ajustar `POST /v1/requests` para aceitar `invitationToken` no JSON `request` e consumir o convite em transação.

### Fase 4 — Cancelamento (individual)
- `POST /v1/invitations/{id}/cancel`

### Fase 5 — Notificações (e-mail)
- Template de e-mail de convite + serviço para enviar para destinatários “sem usuário no DB”.
- Link inclui token.

---

## Checklist de validação (para revisitar quando for implementar)
- Casos felizes: usuário com conta / sem conta (cadastro) / já logado.
- Expirado/cancelado.
- Token válido mas usuário loga com e-mail diferente.
- Idempotência: 2 submits seguidos não criam 2 solicitações.
