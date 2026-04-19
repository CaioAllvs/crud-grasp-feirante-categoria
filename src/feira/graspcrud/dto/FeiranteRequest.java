package feira.graspcrud.dto;

/**
 * DTO de entrada para cadastro e atualizacao de feirante.
 */
public class FeiranteRequest {

    private final String nome;
    private final String cpf;
    private final boolean ativo;
    private final long categoriaId;

    /**
     * Cria o DTO com os dados vindos do menu.
     *
     * @param nome nome do feirante
     * @param cpf cpf do feirante
     * @param ativo status do feirante
     * @param categoriaId id da categoria
     */
    public FeiranteRequest(String nome, String cpf, boolean ativo, long categoriaId) {
        this.nome = nome;
        this.cpf = cpf;
        this.ativo = ativo;
        this.categoriaId = categoriaId;
    }

    /**
     * Retorna o nome informado.
     *
     * @return nome do feirante
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o CPF informado.
     *
     * @return cpf do feirante
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Retorna o status informado.
     *
     * @return true quando ativo
     */
    public boolean isAtivo() {
        return ativo;
    }

    /**
     * Retorna o id da categoria informada.
     *
     * @return id da categoria
     */
    public long getCategoriaId() {
        return categoriaId;
    }
}