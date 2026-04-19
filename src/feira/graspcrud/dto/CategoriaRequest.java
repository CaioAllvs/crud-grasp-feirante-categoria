package feira.graspcrud.dto;

/**
 * DTO de entrada para cadastro de categoria.
 */
public class CategoriaRequest {

    private final String nome;
    private final String descricao;

    /**
     * Cria o DTO com os dados vindos do menu.
     *
     * @param nome nome da categoria
     * @param descricao descricao opcional
     */
    public CategoriaRequest(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    /**
     * Retorna o nome informado.
     *
     * @return nome da categoria
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a descricao informada.
     *
     * @return descricao da categoria
     */
    public String getDescricao() {
        return descricao;
    }
}