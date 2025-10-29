select id,
       status,
       descricao,
       id_role,
       id_usuario_solicitante,
       id_usuario_aprovador
from TB_SOLICITACAO
where 1=1
  AND (:clientIds IS NULL OR id_role IN (SELECT id FROM TB_ROLE WHERE id_cliente = ANY(:clientIds)))
  AND (:levelIds  IS NULL OR esfera_id  = ANY(:levelIds))
  AND (:itemIds   IS NULL OR codigo_item::bigint = ANY(:itemIds))
