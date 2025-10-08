# AccessPilot - Documentação do Sistema

**Versão 1.0** | **Última atualização: 04/10/2025**

---

## Visão Geral

O AccessPilot é uma plataforma de gerenciamento de acessos e permissões desenvolvida para organizações que necessitam de controle rigoroso sobre quem pode acessar seus sistemas e recursos. A solução oferece um fluxo completo de solicitação, aprovação e auditoria de acessos, com rastreabilidade total de todas as operações.

Projetado especialmente para atender às demandas da Administração Pública, o AccessPilot garante conformidade com políticas de segurança, facilita auditorias e proporciona transparência em todos os processos de concessão de acessos.

### Características Principais

O sistema é construído sobre uma arquitetura modular que permite flexibilidade e extensibilidade. Cada módulo é responsável por um aspecto específico do gerenciamento de acessos, desde a configuração de clientes até a notificação de usuários. A integração com sistemas de autenticação externos (Keycloak) garante que as permissões sejam aplicadas de forma consistente em toda a infraestrutura.

A plataforma suporta três tipos de usuários: solicitantes que precisam de acessos, aprovadores que analisam e autorizam solicitações, e administradores que configuram e gerenciam o sistema. Cada perfil tem acesso a funcionalidades específicas, garantindo segregação de funções e segurança.

---

## Arquitetura do Sistema

### Módulos Principais

O AccessPilot é organizado em módulos independentes e coesos, cada um responsável por uma área funcional específica. Esta arquitetura modular facilita a manutenção, permite evolução independente de cada componente e garante baixo acoplamento entre as partes do sistema.

#### 1. Módulo de Clientes (Client)

O módulo de clientes representa os sistemas ou aplicações que serão gerenciados pelo AccessPilot. Cada cliente é uma entidade independente com suas próprias configurações, papéis e políticas de acesso.

**Funcionalidades:**
- Cadastro e gerenciamento de sistemas integrados
- Configuração de políticas específicas por cliente
- Definição de documentos obrigatórios para cada tipo de acesso
- Personalização de fluxos de aprovação

Um cliente pode ser, por exemplo, um Sistema Financeiro, um Portal de RH ou um Sistema de Processos. Cada um terá suas próprias regras sobre quem pode solicitar acessos, quais documentos são necessários e quem pode aprovar.

#### 2. Módulo de Configuração (Configuration)

Este é um dos módulos mais poderosos do sistema. Ele permite que administradores configurem dinamicamente os requisitos de cada cliente, sem necessidade de alterações no código.

**Configuração Dinâmica de Anexos:**

Uma das funcionalidades mais importantes é a capacidade de definir, para cada cliente, quais documentos devem ser anexados obrigatoriamente nas solicitações. Por exemplo:

- Para o cliente "Sistema Financeiro", pode ser obrigatório anexar:
  - Portaria de nomeação (PDF)
  - Termo de responsabilidade assinado (PDF)
  - Certificado de treinamento (PDF ou imagem)

- Para o cliente "Sistema de RH", pode ser suficiente apenas:
  - Termo de responsabilidade (PDF)

O sistema valida automaticamente se todos os documentos obrigatórios foram anexados e se estão nos formatos aceitos. Isso garante que nenhuma solicitação seja processada sem a documentação adequada, facilitando auditorias e garantindo conformidade.

**Configurações Disponíveis:**
- Documentos obrigatórios por cliente
- Tamanho máximo de arquivos
- Formatos aceitos (PDF, DOCX, JPG, PNG, etc.)
- Regras de validação

#### 3. Módulo de Papéis (Role)

O módulo de papéis gerencia os perfis de acesso disponíveis no sistema. Cada papel define um conjunto de permissões e pode estar organizado em uma hierarquia.

**Hierarquia de Papéis:**

Os papéis podem ter relações pai-filho, criando uma estrutura hierárquica que define tanto a organização lógica quanto o fluxo de aprovação. Por exemplo:

```
Gestor Financeiro (Pai)
├── Coordenador Financeiro (Filho)
│   ├── Analista Financeiro Sênior (Neto)
│   └── Analista Financeiro Júnior (Neto)
└── Auditor Financeiro (Filho)
```

**Como a Hierarquia Funciona:**

A hierarquia de papéis é fundamental para o fluxo de aprovação. Quando um usuário solicita um papel, o sistema automaticamente identifica quem pode aprovar baseado nesta estrutura:

- **Regra de Aprovação:** Um usuário que possui um papel "pai" pode aprovar solicitações para qualquer papel "filho" direto
- **Exemplo Prático:** Se João tem o papel "Coordenador Financeiro", ele pode aprovar solicitações para "Analista Financeiro Sênior" ou "Analista Financeiro Júnior", mas não pode aprovar solicitações para "Coordenador Financeiro" (seu próprio nível) ou "Gestor Financeiro" (nível acima)
- **Múltiplos Aprovadores:** Se houver vários usuários com o papel pai, qualquer um deles pode aprovar a solicitação

**Atributos de um Papel:**
- Nome e descrição
- Cliente ao qual pertence
- Papel pai (se houver)
- Papéis filhos (derivados da hierarquia)

#### 4. Módulo de Esferas (Level)

As esferas (ou níveis organizacionais) definem o escopo geográfico ou organizacional onde um acesso é válido. Este é um conceito fundamental que diferencia o AccessPilot de sistemas simples de controle de acesso.

**Tipos de Esferas:**

**Built-in (Padrão):** Esferas pré-configuradas que representam a estrutura federativa brasileira:
- Federal: Acesso em âmbito nacional
- Estadual: Acesso limitado a um estado específico
- Municipal: Acesso limitado a um município
- Regional: Acesso a uma região (Norte, Nordeste, Sul, Sudeste, Centro-Oeste)

**Negocial (Customizada):** Esferas criadas pelo administrador para representar a estrutura organizacional específica:
- Departamentos e suas subdivisões
- Secretarias e coordenações
- Programas e projetos
- Unidades de atendimento

**Externa (Integrada):** Esferas que buscam dados de sistemas externos via API:
- Centros de custo de um ERP
- Projetos de um sistema de gestão
- Unidades organizacionais de outros sistemas

**Hierarquia e Herança de Esferas:**

As esferas suportam hierarquia completa, permitindo organização em múltiplos níveis. A herança funciona de forma descendente: se um usuário tem acesso a uma esfera pai, automaticamente tem acesso a todas as esferas filhas.

**Exemplo de Hierarquia:**

```
Secretaria de Fazenda (Nível 1)
├── Subsecretaria de Receita (Nível 2)
│   ├── Coordenação de Arrecadação (Nível 3)
│   └── Coordenação de Fiscalização (Nível 3)
├── Subsecretaria do Tesouro (Nível 2)
│   ├── Coordenação de Contabilidade (Nível 3)
│   └── Coordenação de Orçamento (Nível 3)
└── Subsecretaria de Planejamento (Nível 2)
```

**Como a Herança Funciona:**

- **Acesso ao Pai = Acesso aos Filhos:** Se Maria tem acesso à "Secretaria de Fazenda" (nível 1), ela automaticamente tem acesso a todas as Subsecretarias (nível 2) e todas as Coordenações (nível 3)
- **Acesso ao Filho ≠ Acesso ao Pai:** Se João tem acesso apenas à "Coordenação de Contabilidade" (nível 3), ele NÃO tem acesso à "Subsecretaria do Tesouro" (nível 2) nem às outras coordenações
- **Controle Granular:** Isso permite tanto acessos amplos (níveis superiores) quanto acessos muito específicos (níveis inferiores)

**Relação entre Hierarquia de Papéis e Esferas:**

Quando um usuário faz uma solicitação, ele escolhe:
1. **Papel:** Define O QUÊ ele pode fazer (ex: Analista Financeiro)
2. **Esfera:** Define ONDE ele pode fazer (ex: Coordenação de Contabilidade)

A combinação de ambos determina o escopo exato do acesso:
- "Analista Financeiro" na "Secretaria de Fazenda" = pode analisar dados de toda a secretaria
- "Analista Financeiro" na "Coordenação de Contabilidade" = pode analisar dados apenas desta coordenação

A hierarquia de papéis determina quem aprova, enquanto a hierarquia de esferas determina o escopo do acesso concedido.

#### 5. Módulo de Solicitações (Request)

Este é o módulo central do sistema, onde todo o fluxo de solicitação e aprovação acontece.

**Ciclo de Vida de uma Solicitação:**

1. **Criação:** Usuário preenche formulário com:
   - Cliente (sistema) desejado
   - Papel solicitado
   - Esfera e item específico
   - Justificativa detalhada
   - Documentos obrigatórios

2. **Validação:** Sistema verifica automaticamente:
   - Se todos os campos obrigatórios foram preenchidos
   - Se os documentos necessários foram anexados
   - Se os formatos dos arquivos são válidos
   - Se o usuário pode solicitar aquele papel

3. **Identificação do Aprovador:** Com base na hierarquia de papéis, o sistema identifica automaticamente quem deve aprovar a solicitação

4. **Notificação:** Aprovador é notificado por múltiplos canais (e-mail e notificação in-app)

5. **Análise:** Aprovador revisa:
   - Dados do solicitante
   - Justificativa apresentada
   - Documentos anexados
   - Histórico de acessos do solicitante

6. **Decisão:** Aprovador pode:
   - Aprovar (com comentários opcionais)
   - Rejeitar (com justificativa obrigatória)

7. **Execução:** Se aprovado:
   - Acesso é concedido automaticamente no Keycloak
   - Permissões são adicionadas ao token do usuário
   - Matriz de acesso é atualizada
   - Solicitante é notificado

8. **Auditoria:** Tudo é registrado permanentemente:
   - Quem solicitou
   - Quando solicitou
   - Por que solicitou
   - Quem aprovou
   - Quando aprovou
   - Por que aprovou

**Estados Possíveis:**
- `CREATED`: Solicitação criada, processamento inicial
- `PENDING`: Aguardando análise do aprovador
- `APPROVED`: Aprovada e acesso concedido
- `REJECTED`: Rejeitada com justificativa
- `CANCELED`: Cancelada pelo solicitante

#### 6. Módulo de Notificações

O sistema possui um módulo robusto de notificações que garante que nenhuma ação importante passe despercebida.

**Canais de Notificação:**

**E-mail (Email Module):**
- Notificações enviadas via SMTP
- Templates HTML com variáveis dinâmicas
- Informações contextuais sobre a solicitação

**Notificações In-App (Web Notification Module):**
- Notificações exibidas dentro do sistema
- Badge com contador de notificações não lidas
- Histórico completo de notificações
- Marcação de lidas/não lidas

**Eventos que Geram Notificações:**
- Nova solicitação criada (para o aprovador)
- Solicitação aprovada (para o solicitante)
- Solicitação rejeitada (para o solicitante)
- Solicitação cancelada (para o aprovador)

#### 7. Módulo de Usuários (User)

Gerencia os usuários do sistema e sua integração com o Keycloak.

**Sincronização com Keycloak:**

O AccessPilot não mantém senhas ou credenciais. Toda autenticação é delegada ao Keycloak. O módulo de usuários:
- Sincroniza dados básicos (nome, e-mail) do Keycloak
- Gerencia o vínculo entre usuários e suas solicitações
- Mantém referência ao ID externo do Keycloak

#### 8. Módulo de Armazenamento (Storage)

Gerencia o armazenamento seguro de documentos anexados às solicitações.

**Funcionalidades:**
- Armazenamento de documentos em nuvem (Amazon S3)
- Controle de acesso aos arquivos
- Validação de tipo e tamanho de arquivo
- Preservação permanente para auditoria

#### 9. Módulo de Integração com Keycloak

Este módulo é responsável pela integração com o Keycloak, garantindo que as permissões aprovadas sejam efetivamente aplicadas.

**Processo de Confirmação de Papéis:**

Quando uma solicitação é aprovada, o sistema automaticamente:
1. Conecta ao Keycloak
2. Localiza o usuário
3. Adiciona o papel aprovado no contexto do cliente específico
4. Atualiza as permissões no token do usuário

**Token de Acesso:**

Após a aprovação, as permissões do usuário são incluídas em seu token de autenticação. Este token contém:
- Identificação do usuário
- Papéis que possui em cada sistema
- Esferas onde pode atuar

Quando o usuário acessa um sistema integrado, esse sistema valida o token e libera o acesso conforme as permissões concedidas. O usuário pode precisar fazer logout e login novamente para que as novas permissões sejam carregadas.

---

## Fluxos de Trabalho

### Fluxo de Solicitação de Acesso

O processo de solicitação de acesso foi desenhado para ser simples para o usuário, mas robusto em termos de validação e auditoria.

**Passo 1: Iniciação**

O usuário acessa o sistema e clica em "Nova Solicitação". O sistema apresenta um formulário onde ele deve:
- Selecionar o cliente (sistema) ao qual deseja acesso
- Escolher o papel desejado
- Selecionar a esfera e item específico
- Fornecer uma justificativa detalhada
- Anexar os documentos obrigatórios

O sistema exibe claramente quais documentos são obrigatórios para aquele papel específico, incluindo os formatos aceitos e tamanho máximo.

**Passo 2: Validação**

Ao submeter o formulário, o sistema executa uma série de validações:
- Todos os campos obrigatórios foram preenchidos?
- Todos os documentos obrigatórios foram anexados?
- Os arquivos estão nos formatos aceitos?
- O tamanho dos arquivos está dentro do limite?
- A esfera selecionada existe e está ativa?
- O usuário já possui este acesso?

Se alguma validação falhar, o usuário recebe uma mensagem clara sobre o que precisa corrigir.

**Passo 3: Identificação do Aprovador**

O sistema consulta a hierarquia de papéis para identificar quem pode aprovar. A lógica é:
- Se o papel solicitado tem um papel pai na hierarquia, usuários que possuem o papel pai podem aprovar
- Se houver múltiplos usuários com o papel pai, qualquer um deles pode aprovar

**Passo 4: Notificação**

O aprovador identificado recebe:
- E-mail com resumo da solicitação e link direto
- Notificação in-app com badge de contagem

**Passo 5: Análise**

O aprovador acessa o sistema e vê:
- Dados completos do solicitante
- Papel e esfera solicitados
- Justificativa apresentada
- Todos os documentos anexados (pode visualizar diretamente)
- Histórico de solicitações anteriores do usuário
- Acessos atuais do usuário

Com essas informações, o aprovador pode tomar uma decisão informada.

**Passo 6: Decisão**

Se aprovar:
- Pode adicionar comentários (opcional)
- Sistema executa automaticamente a concessão do acesso
- Permissões são adicionadas no Keycloak
- Solicitante é notificado

Se rejeitar:
- Deve fornecer justificativa (obrigatório)
- Solicitante é notificado com o motivo
- Solicitante pode corrigir e criar nova solicitação

**Passo 7: Auditoria**

Independente da decisão, tudo é registrado:
- Timestamp de cada ação
- Usuário que executou cada ação
- Justificativas fornecidas
- Documentos anexados (preservados permanentemente)
- IP de origem das ações

### Fluxo de Aprovação

Do ponto de vista do aprovador, o fluxo é otimizado para eficiência:

1. **Recebimento:** Notificação multi-canal (e-mail + in-app)
2. **Acesso:** Link direto para a solicitação
3. **Revisão:** Todas as informações em uma única tela
4. **Decisão:** Botões claros de Aprovar/Rejeitar
5. **Justificativa:** Campo de texto para comentários
6. **Confirmação:** Feedback imediato da ação

O sistema também oferece:
- Visualização em lote de solicitações pendentes
- Filtros por data, solicitante, papel
- Ordenação por prioridade ou antiguidade
- Delegação temporária de aprovações

### Fluxo de Auditoria

Para auditores e administradores, o sistema oferece visibilidade completa:

**Matriz de Acesso em Tempo Real:**
- Quem tem acesso a quê
- Quando foi concedido
- Por quem foi aprovado
- Qual a justificativa
- Status atual (ativo/revogado)

**Relatórios Disponíveis:**
- Acessos por usuário
- Acessos por sistema
- Acessos por departamento/esfera
- Solicitações por período
- Taxa de aprovação/rejeição
- Tempo médio de aprovação
- Acessos próximos ao vencimento
- Acessos órfãos (usuários desligados)

**Exportação:**
- Excel (XLSX)
- PDF
- CSV
- JSON (para integração com outras ferramentas)

---

## Segurança e Conformidade

O AccessPilot foi desenvolvido seguindo as melhores práticas de segurança da informação:

**Segurança:**
- Autenticação delegada ao Keycloak (sem armazenamento de senhas)
- Controle de acesso baseado em papéis (RBAC)
- Segregação de funções
- Criptografia de dados em trânsito e repouso
- Auditoria completa de todas as operações

**Conformidade:**
- Rastreabilidade total de concessões de acesso
- Registro permanente de justificativas
- Relatórios para auditorias
- Aderência a princípios de proteção de dados

---

## Configuração e Administração

### Configuração Inicial

**1. Configuração de Cliente:**

Para adicionar um novo sistema ao AccessPilot:
- Cadastrar o cliente com nome e descrição
- Configurar documentos obrigatórios
- Definir políticas de acesso

**2. Configuração de Papéis:**

Para cada papel no cliente:
- Definir nome e descrição
- Estabelecer hierarquia (papel pai, se houver)

**3. Configuração de Esferas:**

Dependendo do tipo:
- Built-in: Já vem pré-configurado
- Negocial: Criar estrutura hierárquica
- Externa: Configurar endpoint da API e autenticação

---

## Integração

### Integração com Keycloak

O AccessPilot integra-se com o Keycloak para autenticação e gestão de permissões:
- Autenticação de usuários
- Adição/remoção de papéis
- Sincronização de dados

### Integração com Sistemas Externos

O sistema suporta integração com sistemas externos através de:
- APIs REST para consulta de dados
- Esferas externas que buscam informações via API
- Sincronização de estruturas organizacionais

---

## Casos de Uso

### Caso 1: Onboarding de Novo Servidor

**Contexto:** João foi contratado como Analista Financeiro na Secretaria de Fazenda.

**Fluxo:**
1. João recebe credenciais de acesso ao AccessPilot
2. Faz login pela primeira vez
3. Cria solicitação para "Analista Financeiro" no "Sistema Financeiro"
4. Seleciona esfera "Secretaria de Fazenda > Subsecretaria do Tesouro"
5. Anexa portaria de nomeação e termo de responsabilidade
6. Justifica: "Contratado para atuar na área de contabilidade pública"
7. Submete a solicitação
8. Coordenador da Subsecretaria recebe notificação
9. Revisa documentos e aprova
10. João recebe notificação de aprovação
11. Faz login no Sistema Financeiro e já tem acesso

**Tempo total:** 24-48 horas (dependendo da disponibilidade do aprovador)

### Caso 2: Mudança de Departamento

**Contexto:** Maria era Analista no Departamento de RH e foi transferida para o Departamento Financeiro.

**Fluxo:**
1. Maria solicita novo acesso para Departamento Financeiro
2. Anexa portaria de transferência
3. Coordenador do Financeiro aprova
4. Coordenador do RH revoga acesso antigo
5. Maria agora tem acesso apenas ao Financeiro

**Segregação garantida:** Maria não acumula acessos de ambos os departamentos.

---

**Desenvolvido por GetInsight**  
**Versão 1.0 - Outubro 2025**
