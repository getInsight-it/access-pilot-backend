SELECT r.ID,
       ID_ROLE_EXTERNO,
       NOME,
       r.DESCRICAO,
       ID_ROLE_PARENT,
       ID_CLIENTE,
       CLIENT_ID
FROM TB_ROLE as r
         inner join TB_CLIENTE as c on r.ID_CLIENTE = c.ID
where c.CLIENT_ID = :clientId
