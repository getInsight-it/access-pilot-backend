select s.id,
       s.status,
       s.descricao,
       s.id_role,
       s.id_usuario_solicitante,
       s.id_usuario_aprovador
from TB_SOLICITACAO s
inner join TB_ROLE r on s.id_role = r.id
where r.nome in (:roles)
  AND (:clientIds IS NULL OR r.id_cliente = ANY(:clientIds))
  AND (:levelIds  IS NULL OR s.esfera_id  = ANY(:levelIds))
  AND (:itemIds   IS NULL OR s.codigo_item::bigint = ANY(:itemIds))
