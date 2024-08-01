SELECT s.id, s.status, s.id_role, s.id_usuario_solicitante, s.id_usuario_aprovador
FROM TB_SOLICITACAO s
WHERE status = :status
    AND
    s.id_role IN (
    SELECT child.id
    FROM public.tb_role AS parent
             LEFT JOIN public.tb_role AS child ON child.id_role_parent = parent.id
    WHERE parent.nome IN (:rolesParent)
)
