package it.getinsight.message;

import it.getinsight.core.message.IMessageProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

@Getter
@RequiredArgsConstructor
public enum MessageProperty implements IMessageProperty {

    //ver o arquivo src/main/resources/api/messages_pt_BR.properties

    EXEMPLO_MENSAGEM_ERRO("exemplo.de.erro"),
    SOLICITACAO_NAO_ENCOTRADA_ERRO("solicitacao.nao.encontrada"),
    EXEMPLO_MENSAGEM_COM_PARAMETRO("exemplo.de.erro.com.parametro"),
    APROVADOR_NAO_AUTORIZADO("solicitante.aprovador.nao.autorizado");

    private final String key;

    private String[] args = {};

    @Override
    public String key() {
        return key;
    }

    @Override
    public IMessageProperty bind(String... pArgs) {
       this.args = ArrayUtils.isNotEmpty(pArgs) ? pArgs : null;
       return this;
    }

}
