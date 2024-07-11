package it.getinsight.module.erro.service;

import it.getinsight.core.exception.*;
import org.springframework.stereotype.Service;

import static it.getinsight.message.MessageProperty.EXEMPLO_MENSAGEM_COM_PARAMETRO;
import static it.getinsight.message.MessageProperty.EXEMPLO_MENSAGEM_ERRO;

@Service
public class ErroService {

    /**
     * Quando o usuário logado não possuir acesso ao recurso
     */
    public void falharComAccessForbiddenException() {
        throw new AccessForbiddenException("AccessForbiddenException");
    }

    /**
     * Não usar desta forma! Assim, o frontend não recebe mensagem de erro!
     */
    public void falharComBusinessExceptionVazia() {
        throw new BusinessException();
    }

    /**
     * Quando ocorrer violação de regra de negócio com mensagem
     */
    public void falharComBusinessException() {
        throw EXEMPLO_MENSAGEM_ERRO.businessException();
    }

    /**
     * Quando ocorrer violação de regra de negócio com mensagem parametrizada
     */
    public void falharComBusinessExceptionMensagem(String parametro) {
        throw EXEMPLO_MENSAGEM_COM_PARAMETRO
            .bind(parametro)
            .businessException();
    }

    /**
     * Quando ocorrer erro no FeignClient ao evocar serviço externo
     */
    public void falharComFeignIntegrationException() {
        throw new FeignIntegrationException();
    }

    /**
     * Quando o recurso solicitado não for encontrado
     */
    public void falharComResourceNotFoundException() {
        throw new ResourceNotFoundException();
    }

    /**
     * Quando uma validação de segurança for violada (diferente da {@link ErroService#falharComAccessForbiddenException()}
     */
    public void falharComSecurityValidationException() {
        throw new SecurityValidationException();
    }

    /**
     * Quando ocorrer um erro de infraestrutura com mensagem
     */
    public void falharComInfraException() {
        throw EXEMPLO_MENSAGEM_ERRO.infraException();
    }

    /**
     * Quando ocorrer um erro de infraestrutura com mensagem parametrizada
     */
    public void falharComInfraExceptionMensagem(String parametro) {
        throw EXEMPLO_MENSAGEM_COM_PARAMETRO
            .bind(parametro)
            .infraException();
    }


}
