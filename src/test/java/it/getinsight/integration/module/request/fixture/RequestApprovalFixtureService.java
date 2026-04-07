package it.getinsight.integration.module.request.fixture;

import it.getinsight.core.test.helper.KeycloakTestHelper;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.entity.ClientStatus;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.level.entity.ItemEntity;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.repository.ItemRepository;
import it.getinsight.module.level.repository.LevelRepository;
import it.getinsight.module.role.entity.ApprovalPolicyEntity;
import it.getinsight.module.role.entity.ApprovalPolicyRoleEntity;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.enuns.ApprovalPolicyType;
import it.getinsight.module.role.repository.ApprovalPolicyRepository;
import it.getinsight.module.role.repository.ApprovalPolicyRoleRepository;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestApprovalFixtureService {

    private final ClientRepository clientRepository;
    private final LevelRepository levelRepository;
    private final ItemRepository itemRepository;
    private final RoleRepository roleRepository;
    private final ApprovalPolicyRepository approvalPolicyRepository;
    private final ApprovalPolicyRoleRepository approvalPolicyRoleRepository;
    private final UserRepository userRepository;
    private final KeycloakTestHelper keycloakHelper;

    private final Map<String, ClientEntity> clientCache = new HashMap<>();
    private final Map<String, LevelEntity> levelCache = new HashMap<>();
    private final Map<String, ItemEntity> itemCache = new HashMap<>();
    private final Map<String, RoleEntity> roleCache = new HashMap<>();

    private ClientEntity resolveClient(String clientCode) {
        ClientEntity cached = clientCache.get(clientCode);
        if (cached != null) {
            return cached;
        }
        return clientRepository.findByClientId(clientCode)
            .map(client -> {
                clientCache.put(clientCode, client);
                return client;
            })
            .orElseThrow(() -> new RuntimeException("Client not found: " + clientCode));
    }

    private Optional<LevelEntity> findLevelByCode(String levelCode) {
        LevelEntity cached = levelCache.get(levelCode);
        if (cached != null) {
            return Optional.of(cached);
        }
        return levelRepository.findAll().stream()
            .filter(level -> Boolean.TRUE.equals(level.getActive()))
            .filter(level -> level.getSigla() != null && levelCode.equalsIgnoreCase(level.getSigla()))
            .findFirst()
            .map(level -> {
                levelCache.put(levelCode, level);
                return level;
            });
    }

    private LevelEntity resolveLevelByCode(String levelCode) {
        return findLevelByCode(levelCode)
            .orElseThrow(() -> new RuntimeException("Level not found: " + levelCode));
    }

    private Optional<RoleEntity> findRole(String clientCode, String roleName) {
        String cacheKey = clientCode + ":" + roleName;
        RoleEntity cached = roleCache.get(cacheKey);
        if (cached != null) {
            return Optional.of(cached);
        }
        ClientEntity client = resolveClient(clientCode);
        return roleRepository.findByNameAndClient(roleName, client)
            .map(role -> {
                roleCache.put(cacheKey, role);
                return role;
            });
    }

    private RoleEntity resolveRole(String clientCode, String roleName) {
        return findRole(clientCode, roleName)
            .orElseThrow(() -> new RuntimeException("Role not found: " + clientCode + ":" + roleName));
    }

    private ClientEntity syncClientWithKeycloak(ClientEntity client, String clientCode) {
        String keycloakClientUuid = keycloakHelper.ensureClientExists(clientCode);
        if (!Objects.equals(client.getClientUUID(), keycloakClientUuid)) {
            client.setClientUUID(keycloakClientUuid);
            client = clientRepository.save(client);
        }
        clientCache.put(clientCode, client);
        return client;
    }

    @Transactional
    public ClientEntity ensureClientPublished(String clientCode) {
        if (clientCache.containsKey(clientCode)) {
            ClientEntity cached = clientCache.get(clientCode);
            return syncClientWithKeycloak(cached, clientCode);
        }

        Optional<ClientEntity> existing = clientRepository.findByClientId(clientCode);

        if (existing.isPresent()) {
            ClientEntity client = existing.get();
            if (client.getStatus() != ClientStatus.PUBLISHED) {
                log.info("Publishing client: {}", clientCode);
                client.setStatus(ClientStatus.PUBLISHED);
                client = clientRepository.save(client);
            }
            return syncClientWithKeycloak(client, clientCode);
        }

        log.info("Creating new client: {}", clientCode);
        ClientEntity client = ClientEntity.builder()
            .clientId(clientCode)
            .name("Test Client: " + clientCode)
            .description("Test client for integration tests")
            .status(ClientStatus.PUBLISHED)
            .build();

        client = clientRepository.save(client);
        client = syncClientWithKeycloak(client, clientCode);
        clientCache.put(clientCode, client);

        log.info("Client {} created with ID: {}", clientCode, client.getId());
        return client;
    }

    @Transactional
    public LevelEntity ensureLevel(String code, String name, LevelType type, String parentCode) {
        String cacheKey = code;
        if (levelCache.containsKey(cacheKey)) {
            return levelCache.get(cacheKey);
        }

        Optional<LevelEntity> existingByCode = findLevelByCode(code);
        if (existingByCode.isPresent()) {
            LevelEntity level = existingByCode.get();
            levelCache.put(cacheKey, level);
            return level;
        }

        Optional<LevelEntity> existing = levelRepository.findByNameIgnoreCaseAndTypeAndActiveTrue(name, type);

        if (existing.isPresent()) {
            LevelEntity level = existing.get();
            if (level.getSigla() == null || !code.equalsIgnoreCase(level.getSigla())) {
                level.setSigla(code);
                level = levelRepository.save(level);
            }
            levelCache.put(cacheKey, level);
            return level;
        }

        LevelEntity parent = null;
        if (parentCode != null) {
            parent = resolveLevelByCode(parentCode);
        }

        log.info("Creating new level: {} (type: {}, parent: {})", code, type, parentCode);
        LevelEntity level = LevelEntity.builder()
            .name(name)
            .sigla(code)
            .type(type)
            .parent(parent)
            .active(true)
            .build();

        level = levelRepository.save(level);
        levelCache.put(cacheKey, level);

        log.info("Level {} created with ID: {}", code, level.getId());
        return level;
    }

    @Transactional
    public ItemEntity ensureLevelItem(String levelCode, String itemCode, String itemName, String parentItemCode) {
        String cacheKey = levelCode + ":" + itemCode;
        if (itemCache.containsKey(cacheKey)) {
            return itemCache.get(cacheKey);
        }

        LevelEntity level = levelCache.get(levelCode);
        if (level == null) {
            level = resolveLevelByCode(levelCode);
        }

        Optional<ItemEntity> existing = itemRepository.findByLevelIdAndExternalCode(level.getId(), itemCode);

        if (existing.isPresent()) {
            ItemEntity item = existing.get();
            itemCache.put(cacheKey, item);
            return item;
        }

        ItemEntity parentItem = null;
        if (parentItemCode != null) {
            String parentCacheKey = levelCode + ":" + parentItemCode;
            parentItem = itemCache.get(parentCacheKey);
            if (parentItem == null) {
                parentItem = itemRepository.findByLevelIdAndExternalCode(level.getId(), parentItemCode)
                    .orElseThrow(() -> new RuntimeException("Parent item not found: " + parentItemCode));
                itemCache.put(parentCacheKey, parentItem);
            }
        }

        log.info("Creating new item: {} in level {}", itemCode, levelCode);
        ItemEntity item = ItemEntity.builder()
            .level(level)
            .externalCode(itemCode)
            .name(itemName)
            .parent(parentItem)
            .active(true)
            .build();

        item = itemRepository.save(item);
        itemCache.put(cacheKey, item);

        log.info("Item {} created with ID: {}", itemCode, item.getId());
        return item;
    }

    @Transactional
    public RoleEntity ensureRole(String clientCode, String roleName, String parentRoleName, String levelCode) {
        String cacheKey = clientCode + ":" + roleName;
        if (roleCache.containsKey(cacheKey)) {
            return roleCache.get(cacheKey);
        }

        ClientEntity client = clientCache.get(clientCode);
        if (client == null) {
            client = resolveClient(clientCode);
        }

        Optional<RoleEntity> existing = roleRepository.findByNameAndClient(roleName, client);

        if (existing.isPresent()) {
            RoleEntity role = existing.get();
            roleCache.put(cacheKey, role);
            return role;
        }

        RoleEntity parentRole = null;
        if (parentRoleName != null) {
            parentRole = resolveRole(clientCode, parentRoleName);
        }

        LevelEntity level = null;
        if (levelCode != null) {
            level = resolveLevelByCode(levelCode);
        }

        log.info("Creating new role: {} in client {} (level: {}, parent: {})",
            roleName, clientCode, levelCode, parentRoleName);
        RoleEntity role = RoleEntity.builder()
            .client(client)
            .name(roleName)
            .label("Test Role: " + roleName)
            .role(parentRole)
            .level(level)
            .roleExternalId(UUID.randomUUID().toString()) // Simula ID do Keycloak
            .active(true)
            .build();

        role = roleRepository.save(role);
        roleCache.put(cacheKey, role);

        log.info("Role {} created with ID: {}", roleName, role.getId());
        return role;
    }

    @Transactional
    public ApprovalPolicyEntity ensureApprovalPolicy(String clientCode, String roleName,
                                                      ApprovalPolicyType type, List<String> approverRoles) {
        String cacheKey = clientCode + ":" + roleName;
        RoleEntity role = resolveRole(clientCode, roleName);

        Optional<ApprovalPolicyEntity> existing = approvalPolicyRepository
            .findByRoleIdAndType(role.getId(), type);

        if (existing.isPresent()) {
            log.info("Approval policy already exists for role {}: {}", roleName, type);
            return existing.get();
        }

        log.info("Creating approval policy for role {}: {} with approvers: {}",
            roleName, type, approverRoles);

        ApprovalPolicyEntity policy = ApprovalPolicyEntity.builder()
            .role(role)
            .type(type)
            .enabled(true)
            .active(true)
            .build();

        policy = approvalPolicyRepository.save(policy);

        if (type == ApprovalPolicyType.LATERAL_APPROVAL && approverRoles != null && !approverRoles.isEmpty()) {
            for (String approverRoleName : approverRoles) {
                String approverCacheKey = clientCode + ":" + approverRoleName;
                RoleEntity approverRole = roleCache.get(approverCacheKey);
                if (approverRole == null) {
                    log.warn("Approver role not found in cache: {}", approverRoleName);
                    continue;
                }

                ApprovalPolicyRoleEntity policyRole = ApprovalPolicyRoleEntity.builder()
                    .policy(policy)
                    .role(approverRole)
                    .canApprove(true)
                    .canReject(true)
                    .canRevoke(false)
                    .active(true)
                    .build();

                approvalPolicyRoleRepository.save(policyRole);
                log.info("Added approver role {} to policy", approverRoleName);
            }
        }

        log.info("Approval policy created for role {}", roleName);
        return policy;
    }

    @Transactional
    public UserEntity ensureUser(String username, String password, String email) {
        Optional<UserEntity> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            log.info("User already exists in database: {}", username);
            return existing.get();
        }

        log.info("Creating user in Keycloak: {}", username);
        String keycloakUserId = keycloakHelper.createUser(username, password, email);

        log.info("Creating user in database: {}", username);
        UserEntity user = UserEntity.builder()
            .externalId(keycloakUserId)
            .username(username)
            .firstName(username)
            .lastName("Test")
            .email(email)
            .build();

        user = userRepository.save(user);
        log.info("User {} created with ID: {}", username, user.getId());

        return user;
    }

    @Transactional
    public String ensureUserRoleWithScope(String username, String clientCode, String roleName, String itemCode) {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));

        RoleEntity role = resolveRole(clientCode, roleName);
        String levelAttribute = null;
        if (role.getLevel() != null) {
            if (itemCode == null || itemCode.isBlank()) {
                throw new RuntimeException("itemCode is required when role has level: " + roleName);
            }
            levelAttribute = String.format("%d:%d:%d:%s",
                role.getClient().getId(),
                role.getId(),
                role.getLevel().getId(),
                itemCode
            );
            log.info("Setting levelAttribute for user {}: {}", username, levelAttribute);
            keycloakHelper.setLevelAttribute(user.getExternalId(), levelAttribute);
        } else {
            log.info("Skipping levelAttribute for user {}: role {} has no level", username, roleName);
        }

        keycloakHelper.ensureClientExists(clientCode);
        keycloakHelper.ensureClientRoleExists(clientCode, roleName);
        keycloakHelper.assignClientRoles(user.getExternalId(), clientCode, List.of(roleName));

        return levelAttribute;
    }

    public void clearCaches() {
        log.info("Clearing fixture caches");
        clientCache.clear();
        levelCache.clear();
        itemCache.clear();
        roleCache.clear();
    }

    public RoleEntity getRole(String clientCode, String roleName) {
        return resolveRole(clientCode, roleName);
    }

    public LevelEntity getLevelFromCache(String levelCode) {
        return levelCache.get(levelCode);
    }

    public ClientEntity getClientFromCache(String clientCode) {
        return clientCache.get(clientCode);
    }


    @Transactional
    public void setupSistema1EmpresaDemo() {
        log.info("=== Setting up Sistema 1: empresa-demo ===");

        ensureClientPublished("empresa-demo");

        ensureLevel("GERENCIA", "Gerência", LevelType.BUILT_IN, null);
        ensureLevel("DIRETORIA", "Diretoria", LevelType.BUILT_IN, "GERENCIA");

        ensureLevelItem("GERENCIA", "FIN", "Financeiro", null);
        ensureLevelItem("GERENCIA", "RH", "Recursos Humanos", null);
        ensureLevelItem("GERENCIA", "COMP", "Compras", null);

        ensureLevelItem("DIRETORIA", "DIR", "Diretoria Geral", null);

        ensureRole("empresa-demo", "GESTOR", null, "GERENCIA");
        ensureRole("empresa-demo", "ANALISTA_FINANCEIRO", "GESTOR", "GERENCIA");
        ensureRole("empresa-demo", "ANALISTA_RH", "GESTOR", "GERENCIA");
        ensureRole("empresa-demo", "ANALISTA_COMPRAS", "GESTOR", "GERENCIA");
        ensureRole("empresa-demo", "AUDITOR", null, "DIRETORIA");

        ensureApprovalPolicy("empresa-demo", "ANALISTA_FINANCEIRO",
            ApprovalPolicyType.LATERAL_APPROVAL,
            List.of("ANALISTA_RH", "ANALISTA_COMPRAS"));

        ensureApprovalPolicy("empresa-demo", "AUDITOR",
            ApprovalPolicyType.AUTO_APPROVAL,
            null);

        log.info("✓ Sistema 1 (empresa-demo) configurado com sucesso");
    }

    @Transactional
    public void setupSistema1Users() {
        log.info("=== Setting up users for Sistema 1: empresa-demo ===");

        ensureUser("gestor.fin", "123456", "gestor.fin@empresa-demo.test");
        ensureUser("gestor.rh", "123456", "gestor.rh@empresa-demo.test");
        ensureUser("analista.fin", "123456", "analista.fin@empresa-demo.test");
        ensureUser("analista.rh", "123456", "analista.rh@empresa-demo.test");
        ensureUser("analista.compras", "123456", "analista.compras@empresa-demo.test");
        ensureUser("auditor", "123456", "auditor@empresa-demo.test");

        ensureUserRoleWithScope("gestor.fin", "empresa-demo", "GESTOR", "FIN");
        ensureUserRoleWithScope("gestor.rh", "empresa-demo", "GESTOR", "RH");
        ensureUserRoleWithScope("analista.fin", "empresa-demo", "ANALISTA_FINANCEIRO", "FIN");
        ensureUserRoleWithScope("analista.rh", "empresa-demo", "ANALISTA_RH", "RH");
        ensureUserRoleWithScope("analista.compras", "empresa-demo", "ANALISTA_COMPRAS", "COMP");
        ensureUserRoleWithScope("auditor", "empresa-demo", "AUDITOR", "DIR");

        log.info("✓ Usuários do Sistema 1 (empresa-demo) configurados com sucesso");
    }

    @Transactional
    public void setupSistema2ProtocoloEletronico() {
        log.info("=== Setting up Sistema 2: protocolo-eletronico-interno ===");

        ensureClientPublished("protocolo-eletronico-interno");

        ensureRole("protocolo-eletronico-interno", "ADMIN_PROTOCOLO", null, null);
        ensureRole("protocolo-eletronico-interno", "ANALISTA_PROTOCOLO", "ADMIN_PROTOCOLO", null);
        ensureRole("protocolo-eletronico-interno", "TRAMITADOR", "ANALISTA_PROTOCOLO", null);

        log.info("✓ Sistema 2 (protocolo-eletronico-interno) configurado com sucesso");
    }

    @Transactional
    public void setupSistema2Users() {
        log.info("=== Setting up users for Sistema 2: protocolo-eletronico-interno ===");

        ensureUser("proto.admin", "123456", "proto.admin@protocolo.test");
        ensureUser("proto.analista", "123456", "proto.analista@protocolo.test");
        ensureUser("proto.tramitador", "123456", "proto.tramitador@protocolo.test");

        ensureUserRoleWithScope("proto.admin", "protocolo-eletronico-interno", "ADMIN_PROTOCOLO", null);
        ensureUserRoleWithScope("proto.analista", "protocolo-eletronico-interno", "ANALISTA_PROTOCOLO", null);
        ensureUserRoleWithScope("proto.tramitador", "protocolo-eletronico-interno", "TRAMITADOR", null);

        log.info("✓ Usuários do Sistema 2 (protocolo-eletronico-interno) configurados com sucesso");
    }

    @Transactional
    public void setupSistema3SistemaEducacional() {
        log.info("=== Setting up Sistema 3: sistema-educacional ===");

        ensureClientPublished("sistema-educacional");

        ensureLevel("FEDERAL", "Federal", LevelType.BUILT_IN, null);
        ensureLevel("ESTADUAL", "Estadual", LevelType.BUILT_IN, "FEDERAL");
        ensureLevel("MUNICIPAL", "Municipal", LevelType.BUILT_IN, "ESTADUAL");

        ensureLevelItem("FEDERAL", "BR", "Brasil", null);

        ensureLevelItem("ESTADUAL", "BR", "Brasil", null);
        ensureLevelItem("ESTADUAL", "SP", "São Paulo", "BR");
        ensureLevelItem("ESTADUAL", "RJ", "Rio de Janeiro", "BR");
        ensureLevelItem("ESTADUAL", "MG", "Minas Gerais", "BR");

        ensureLevelItem("MUNICIPAL", "SP", "Estado de São Paulo", null);
        ensureLevelItem("MUNICIPAL", "RJ", "Estado do Rio de Janeiro", null);
        ensureLevelItem("MUNICIPAL", "MG", "Estado de Minas Gerais", null);
        ensureLevelItem("MUNICIPAL", "SAO_PAULO", "São Paulo", "SP");
        ensureLevelItem("MUNICIPAL", "RIO_DE_JANEIRO", "Rio de Janeiro", "RJ");
        ensureLevelItem("MUNICIPAL", "BH", "Belo Horizonte", "MG");

        ensureRole("sistema-educacional", "DIRETOR_FEDERAL", null, "FEDERAL");
        ensureRole("sistema-educacional", "PROFESSOR_FEDERAL", "DIRETOR_FEDERAL", "FEDERAL");
        ensureRole("sistema-educacional", "ALUNO_FEDERAL", "PROFESSOR_FEDERAL", "FEDERAL");

        ensureRole("sistema-educacional", "DIRETOR_ESTADUAL", "DIRETOR_FEDERAL", "ESTADUAL");
        ensureRole("sistema-educacional", "PROFESSOR_ESTADUAL", "DIRETOR_ESTADUAL", "ESTADUAL");
        ensureRole("sistema-educacional", "ALUNO_ESTADUAL", "PROFESSOR_ESTADUAL", "ESTADUAL");

        ensureRole("sistema-educacional", "DIRETOR_MUNICIPAL", "DIRETOR_ESTADUAL", "MUNICIPAL");
        ensureRole("sistema-educacional", "PROFESSOR_MUNICIPAL", "DIRETOR_MUNICIPAL", "MUNICIPAL");
        ensureRole("sistema-educacional", "ALUNO_MUNICIPAL", "PROFESSOR_MUNICIPAL", "MUNICIPAL");

        log.info("✓ Sistema 3 (sistema-educacional) configurado com sucesso");
    }

    @Transactional
    public void setupSistema3Users() {
        log.info("=== Setting up users for Sistema 3: sistema-educacional ===");

        ensureUser("aluno.mun.sp", "123456", "aluno.mun.sp@educacional.test");
        ensureUser("prof.mun.sp", "123456", "prof.mun.sp@educacional.test");
        ensureUser("dir.mun.sp", "123456", "dir.mun.sp@educacional.test");

        ensureUser("aluno.est.sp", "123456", "aluno.est.sp@educacional.test");
        ensureUser("prof.est.sp", "123456", "prof.est.sp@educacional.test");
        ensureUser("dir.est.sp", "123456", "dir.est.sp@educacional.test");

        ensureUser("dir.est.rj", "123456", "dir.est.rj@educacional.test");

        ensureUser("aluno.fed", "123456", "aluno.fed@educacional.test");
        ensureUser("prof.fed", "123456", "prof.fed@educacional.test");
        ensureUser("dir.fed", "123456", "dir.fed@educacional.test");

        ensureUserRoleWithScope("aluno.mun.sp", "sistema-educacional", "ALUNO_MUNICIPAL", "SAO_PAULO");
        ensureUserRoleWithScope("prof.mun.sp", "sistema-educacional", "PROFESSOR_MUNICIPAL", "SAO_PAULO");
        ensureUserRoleWithScope("dir.mun.sp", "sistema-educacional", "DIRETOR_MUNICIPAL", "SAO_PAULO");

        ensureUserRoleWithScope("aluno.est.sp", "sistema-educacional", "ALUNO_ESTADUAL", "SP");
        ensureUserRoleWithScope("prof.est.sp", "sistema-educacional", "PROFESSOR_ESTADUAL", "SP");
        ensureUserRoleWithScope("dir.est.sp", "sistema-educacional", "DIRETOR_ESTADUAL", "SP");

        ensureUserRoleWithScope("dir.est.rj", "sistema-educacional", "DIRETOR_ESTADUAL", "RJ");

        ensureUserRoleWithScope("aluno.fed", "sistema-educacional", "ALUNO_FEDERAL", "BR");
        ensureUserRoleWithScope("prof.fed", "sistema-educacional", "PROFESSOR_FEDERAL", "BR");
        ensureUserRoleWithScope("dir.fed", "sistema-educacional", "DIRETOR_FEDERAL", "BR");

        log.info("✓ Usuários do Sistema 3 (sistema-educacional) configurados com sucesso");
    }

    @Transactional
    public void setupAccessPilotAdminUser() {
        log.info("=== Setting up Access Pilot admin user ===");

        ensureClientPublished("accesspilot-backend");
        ensureRole("accesspilot-backend", "ADMIN", null, null);
        ensureUser("admin", "123456", "admin@accesspilot.test");
        ensureUserRoleWithScope("admin", "accesspilot-backend", "ADMIN", null);

        log.info("✓ Access Pilot admin user configured successfully");
    }
}
