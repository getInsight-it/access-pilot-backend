package it.getinsight.module.solicitacao.enuns;

public enum SolicitacaoStatus {

    CREATED,
    IN_PROGRESS,
    WAINING_FOR_ROLES_CONFIRMATION,
    REJECTED,
    APPROVED,
    ROLES_CONFIRMED,
    ROLES_ASSIGNED,
    ROLES_NOT_FOUND,
    ROLES_NOT_ASSIGNED,
    COMPLETED,
    CANCELED, APPROVES_SENT;


    public static SolicitacaoStatus fromString(String status) {
        if (isValid(status)) {
            return SolicitacaoStatus.valueOf(status.toUpperCase());
        }
        return null;
    }


    public static boolean isValid(String status) {
        for (SolicitacaoStatus SolicitacaoStatus : SolicitacaoStatus.values()) {
            if (SolicitacaoStatus.name().equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }
}
