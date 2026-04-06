# Feature: Políticas de Aprovação por Papel (Autoaprovação + Aprovação Lateral)

## 1) Objetivo da feature

Evoluir o modelo de aprovação de papéis para sair de uma `flag` única de autoaprovação e suportar políticas explícitas por papel, com foco em:

- **Autoaprovação**: a solicitação já nasce aprovada.
- **Aprovação lateral**: papéis irmãos podem visualizar e atuar (aprovar/rejeitar/revogar) conforme permissões por papel-alvo.

Essa evolução preserva a hierarquia como regra base e adiciona flexibilidade de governança sem acoplar UI em regra de negócio sensível.

---

## 2) Regras funcionais consolidadas

- A hierarquia continua sendo a regra padrão de aprovação.
- Autoaprovação, quando habilitada para o papel solicitado, tem precedência no momento de criação da solicitação.
- Aprovação lateral é **complementar** (não sobrescreve hierarquia).
- Papéis laterais elegíveis são apenas **irmãos**:
  - mesmo `client`,
  - mesmo `parent`,
  - mesmo `level`,
  - ativos,
  - diferente do próprio papel.
- Permissões laterais são configuráveis por papel-alvo:
  - `canApprove`
  - `canReject`
  - `canRevoke`
- Após sair de `PENDING`, a solicitação continua aparecendo em consultas `assigned` para quem tem acesso lateral compatível.

---

## 3) Decisões de modelagem e fundamentos

## 3.1 Por que não manter só `AUTO_APROVAVEL`?

A flag única resolve apenas autoaprovação e não escala para:
- políticas múltiplas;
- configuração por alvo lateral;
- permissões de ação distintas.

Decisão: mover para um modelo de políticas normalizado.

## 3.2 Por que não usar JSON genérico agora?

Foi considerado usar `CONFIGURACAO_JSON`, mas para o cenário atual isso traria:
- baixa consultabilidade SQL;
- maior complexidade de validação de negócio;
- flexibilidade prematura.

Decisão: colunas explícitas para permissões (`PODE_APROVAR`, `PODE_REJEITAR`, `PODE_REVOGAR`), mantendo leitura e validação simples.

## 3.3 Por que não incluir prioridade/rota de aprovação neste momento?

Foi deliberado que:
- autoaprovação já tem prioridade sem precisar de campo de prioridade;
- não é necessário persistir rota (`HIERARCHY|LATERAL|AUTO`) neste ciclo.

Decisão: reduzir escopo para entrega consistente da regra atual e evitar complexidade não usada.

## 3.4 Backend como fonte de verdade

A UI foi ajustada para orientar e reduzir erro de configuração, mas:
- validação final de elegibilidade lateral e autorização de ação é feita no backend;
- assim, mudanças futuras de regra não exigem depender da UI para segurança.

---

## 4) Modelagem de banco implementada

Migrations usadas:
- `V1_098_add_column_auto_approvable_to_tb_role.xml`
- `V1_099_add_column_auto_approvable_to_tb_role_aud.xml`

Autor das changesets: `luan.rodrigues@getinsight.it`.

### 4.1 Estrutura (ANSI)

```text
TB_ROLE
  ID (PK)
  ...
  [sem AUTO_APROVAVEL]

TB_ROLE_POLITICA_APROVACAO
  ID (PK)
  ID_ROLE (FK -> TB_ROLE.ID)
  TIPO_POLITICA (AUTO_APROVACAO | APROVACAO_LATERAL)
  HABILITADA
  ATIVO
  CRIADO_POR / DATA_CRIACAO / ATUALIZADO_POR / DATA_ATUALIZACAO
  UK(ID_ROLE, TIPO_POLITICA)

TB_ROLE_POLITICA_APROVACAO_ALVO
  ID (PK)
  ID_POLITICA (FK -> TB_ROLE_POLITICA_APROVACAO.ID)
  ID_ROLE_ALVO (FK -> TB_ROLE.ID)
  PODE_APROVAR
  PODE_REJEITAR
  PODE_REVOGAR
  ATIVO
  CRIADO_POR / DATA_CRIACAO / ATUALIZADO_POR / DATA_ATUALIZACAO
  UK(ID_POLITICA, ID_ROLE_ALVO)
```

Também foram criadas as tabelas de auditoria:
- `TB_ROLE_POLITICA_APROVACAO_AUD`
- `TB_ROLE_POLITICA_APROVACAO_ALVO_AUD`

---

## 5) Contrato de API (role create/update/read)

### 5.1 Request (create/update)

```json
{
  "name": "ROLE_X",
  "label": "Role X",
  "description": "Descrição",
  "icon": "shield",
  "levelId": 10,
  "approvalPolicies": [
    {
      "type": "AUTO_APROVACAO",
      "enabled": true,
      "targetRoles": []
    },
    {
      "type": "APROVACAO_LATERAL",
      "enabled": true,
      "targetRoles": [
        {
          "roleId": 123,
          "canApprove": true,
          "canReject": false,
          "canRevoke": false
        }
      ]
    }
  ]
}
```

### 5.2 Response (read)

```json
{
  "id": 999,
  "name": "ROLE_X",
  "approvalPolicies": [
    {
      "id": 1,
      "type": "AUTO_APROVACAO",
      "enabled": true,
      "targetRoles": []
    },
    {
      "id": 2,
      "type": "APROVACAO_LATERAL",
      "enabled": true,
      "targetRoles": [
        {
          "roleId": 123,
          "roleName": "ROLE_IRMA",
          "roleLabel": "Irmã",
          "canApprove": true,
          "canReject": false,
          "canRevoke": false
        }
      ]
    }
  ]
}
```

---

## 6) Impactos implementados no backend

## 6.1 Domínio de role

- Novo enum: `RoleApprovalPolicyType` com `AUTO_APROVACAO` e `APROVACAO_LATERAL`.
- `RoleEntity` agora possui coleção `approvalPolicies`.
- Novas entidades:
  - `RoleApprovalPolicyEntity`
  - `RoleApprovalPolicyTargetEntity`
- Novos DTOs:
  - request/response de política e de alvo.
- Novo serviço `RoleApprovalPolicyService` para sincronizar e validar políticas.
- Validação forte de alvo lateral (irmã no mesmo nível, mesmo pai, etc.).
- `RoleService` passou a sincronizar políticas no create/update/hierarchy e expor no DTO de resposta.

## 6.2 Fluxo de criação da solicitação

`RequestCreationService`:
- consulta `isAutoApprovalEnabled(role)` no backend;
- se ativo:
  - cria solicitação em `APPROVED`,
  - define `approvingUser` como solicitante,
  - aplica efeitos de confirmação de papel (`confirmRoles`),
  - publica evento de alteração de status;
- se inativo:
  - mantém fluxo tradicional em `PENDING` e notificação de criação.

## 6.3 Autorização e ações (aprovar/rejeitar/revogar/visualizar)

`UserAccessValidationService`:
- regras separadas por ação:
  - hierarquia OU lateral com permissão correspondente.
- visão lateral (`canView`) também habilitada para papéis laterais.
- regras laterais por `canApprove/canReject/canRevoke`.

`RequestManagementService`:
- ações permitidas agora dependem dessas permissões por ação;
- valida transição por `canTransitionToStatus(...)`.

## 6.4 Consultas `assigned`

`RequestQueryBuilderService`:
- inclui `lateralSpec` na especificação de `assigned`;
- mapeia escopos dos papéis-alvo laterais para papéis de origem das solicitações;
- cobre cenários com e sem nível.

Resultado esperado: solicitação continua visível em `assigned` para lateral elegível mesmo após sair de `PENDING`.

## 6.5 Export/Import de client/roles

`ClientExportService`/DTOs/Mapper:
- exporta `approvalPolicies` por papel;
- importa políticas e alvos laterais (com permissões);
- preserva coerência no roundtrip export/import.

---

## 7) Impactos implementados no frontend

## 7.1 Modelo e payload

- Tipos de política adicionados em `role.model.ts`.
- `RoleUpsertInterface` agora envia `approvalPolicies`.
- Formulário de role (`useNewRole.ts`) faz:
  - hidratação das políticas no edit;
  - montagem de payload de políticas no submit.

## 7.2 UX na tela de Role (novo/edição)

Seção “Políticas de aprovação” com 3 cards:
- Hierarquia (somente leitura + link para gerenciar).
- Aprovação lateral (toggle + matriz de papéis irmãos e permissões).
- Autoaprovação (toggle; colocado por último).

### Ajustes UX realizados durante iteração

- Remoção de trechos não desejados:
  - “Fluxo efetivo da role”
  - “IMUTÁVEL NESTA TELA”
- Texto de lateral reforçando que não sobrescreve hierarquia.
- Melhorias visuais de alinhamento:
  - uso do padrão `app-table` já existente no projeto;
  - colunas de ação fixas e centralizadas;
  - checkboxes quadrados (menores) para `Aprovar/Rejeitar/Revogar`.
- Uso de ícones já existentes na lib do projeto (`lucide-react`), sem novas libs.
- `ManageRoles` aceita `?tab=roles_hierarchy` para deep-link a partir do botão de hierarquia.

## 7.3 Regra de irmãos na UI (somente orientação)

No front, lista lateral é filtrada para papéis com:
- mesmo pai
- mesmo nível
- diferente do papel atual

E seleção inválida é limpa do formulário quando o conjunto elegível muda.

Importante: a segurança continua no backend.

---

## 8) Linha do tempo resumida das ações tomadas

1. Evolução do requisito de autoaprovação simples para modelo extensível de políticas.
2. Definição de escopo do ciclo:
   - manter somente `AUTO_APROVACAO` e `APROVACAO_LATERAL`,
   - sem JSON genérico,
   - sem prioridade/rota persistida.
3. Modelagem relacional + migrations (incluindo auditoria).
4. Implementação backend:
   - entidades/repos/services/mappers/DTOs,
   - integrações em role/request/query/export/import.
5. Ajustes de regras de negócio:
   - lateral por irmãs do mesmo nível;
   - permissões por ação (approve/reject/revoke).
6. Implementação frontend:
   - contrato tipado e payload novo,
   - UI de políticas com matriz de permissões.
7. Iterações de UX:
   - simplificação textual,
   - remoção de blocos redundantes,
   - card ordering,
   - alinhamento e padronização visual da tabela lateral.

---

## 9) Validações executadas

- Backend: `mvn -q -DskipTests compile` executado com sucesso.
- Frontend: `npm run build` executado com sucesso.
  - warnings conhecidos de Sass legacy API/chunk size permanecem, sem relação direta com a feature.

---

## 10) Pontos para revisão no Claude

- Coerência das regras de autorização por ação:
  - `canApprove/canReject/canRevoke` lateral
  - fallback/hierarquia sem regressão
- Correção das consultas `assigned` com `lateralSpec` (performance e precisão de escopo).
- Robustez do `syncPolicies` para updates parciais e idempotência.
- Consistência export/import das políticas.
- UX:
  - legibilidade da matriz lateral,
  - clareza de que lateral não sobrescreve hierarquia.

---

## 11) Observação operacional (ambiente local)

Como discutido, as migrations `V1_098`/`V1_099` já estavam aplicadas localmente durante a evolução da feature.  
Se houver divergência de estado local do banco, pode ser necessário ajuste manual local (sem impacto no desenho final adotado).

