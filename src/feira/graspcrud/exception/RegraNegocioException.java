package feira.graspcrud.exception;

/**
 * Excecao para representar violacoes de regra de negocio.
 *
 * <p>Usada para exibir mensagens claras no terminal quando alguma regra
 * do sistema for violada, como CPF invalido, categoria inexistente
 * ou tentativa de remover categoria em uso.
 */
public class RegraNegocioException extends RuntimeException {

    /**
     * Cria a excecao com mensagem amigavel ao usuario.
     *
     * @param message mensagem da violacao
     */
    public RegraNegocioException(String message) {
        super(message);
    }
}