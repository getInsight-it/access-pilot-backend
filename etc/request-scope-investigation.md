# Request scope – investigação (fev/2025)

Contexto: solicitante com role sem esfera (level nulo) acessando `GET /v1/requests/me/paginated?type=assigned` retornou lista vazia. O log SQL mostrou `where 1=1 and 1<>1`.

### Observações
- O token usado não traz a claim `levelAttributes`, que é a fonte de scopes no `SecurityScopes`.
- Sem `levelAttributes`, `SecurityScopes` fica vazio (`directScopes`, `hierarchicalScopes`, `rolesWithoutDirectAccess` = `[]`).
- No `RequestQueryBuilderService.buildAssignedRequestsSpecification` a spec de segurança vira apenas `rolesWithLevelIsNull([])`, que devolve `disjunction()` → `1<>1`.
- Resultado: `matchCustom` contribui `1=1`, combinado com `1<>1`, e a consulta não retorna nada.

### Comportamento atual relevante
- Scopes expandidos vêm de `SecurityScopesBuilderService` a partir de `levelAttributes`.
- `rolesWithLevelIsNull` só permite match se a lista de roles não for vazia; quando vazia, devolve `disjunction()` para evitar vazamento.

### Pontos de decisão (sem implementar ainda)
1) **Garantir claim**: incluir `levelAttributes` mesmo para roles sem esfera (ex.: `client:role:null:null`), permitindo popular `rolesWithoutDirectAccess`.
2) **Fallback**: se não houver `levelAttributes`, optar por um caminho alternativo (ex.: usar roles do `resource_access`) em vez de retornar vazio.
3) **Ajuste de spec**: avaliar se `rolesWithLevelIsNull` deveria aceitar vazio (hoje retorna `1<>1`), mas isso pode reabrir acesso amplo.
4) **Ideia citada**: “pegar a role filha e fazer um IN e AND level is null para aquele escopo, para não interferir nos demais”. Isso exigiria derivar roles descendentes com `level` nulo quando o scope original não tem level e combinar num `IN` separado, mantendo os filtros de level para os outros scopes.

Conclusão atual: o vazio vem da ausência de `levelAttributes`. Qualquer correção precisa decidir entre exigir a claim, criar fallback ou ajustar a spec para roles sem level.

### Caso ROLE_SU/ROLE_ADMIN sem levelAttributes
- `rolesWithoutDirectAccess` nasce apenas dos scopes extraídos de `levelAttributes`.
- Se o token não tiver `levelAttributes`, nenhuma role (ex.: ROLE_SU/ROLE_ADMIN) entra nessa lista.
- Com `rolesWithoutDirectAccess` vazio, `rolesWithLevelIsNull([])` retorna `disjunction()` (`1<>1`), e o filtro final fica `1=1 AND 1<>1`, retornando zero resultados.
- Portanto, usuários só com roles sem nível (e sem `levelAttributes` no token) não veem nada no endpoint assigned. É preciso chegar pelo menos um scope em `levelAttributes` (ou algum fallback que alimente essa lista).

### Ajuste implementado (fev/2025)
- `rolesWithoutDirectAccess` agora usa o formato `clientId:roleId:*:*` para representar roles sem nível (filtro por client+role).
- As roles sem nível são derivadas de `resource_access`, incluindo descendentes com `level` nulo, e filtradas para remover aquelas já cobertas pela união de `sameLevels` + `hierarchyLevels` (mesmo clientId/roleId).
- Objetivo: permitir que roles sem nível enxerguem suas solicitações sem afetar a lógica de `directScopes`/`hierarchyScopes`.
