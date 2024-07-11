package it.getinsight.module.usuario.entity;


import it.getinsight.core.model.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_USUARIO")
@SequenceGenerator(name = "UsuarioEntity.sq", sequenceName = "SQ_USUARIO", allocationSize = 1)
public class UsuarioEntity extends BaseEntity<Long> {
    @Serial
    private static final long serialVersionUID = 5287296228628658948L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "UsuarioEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "SOBRENOME")
    private String sobrenome;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ID_USUARIO_EXTERNO")
    private String idUsuarioExterno;

}
