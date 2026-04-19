package feira.graspcrud.domain;

import feira.graspcrud.exception.RegraNegocioException;

/**
 * Entidade de classificacao para feirantes.
 *
 * <p>Padrão GRASP: Information Expert — a própria classe
 * valida suas regras de negócio.
 */
public class Categoria {

    private Long id;
    private String nome;
    private String descricao;

    /**
     * Cria categoria com validacao de estado.
     *
     * @param id identificador (pode ser nulo para novo cadastro)
     * @param nome nome da categoria
     * @param descricao descricao opcional
     */
    public Categoria(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome == null ? null : nome.trim();
        this.descricao = descricao == null ? "" : descricao.trim();
    }

    /**
     * Valida as regras de negocio da categoria.
     */
    public void validar() {
        if (nome == null || nome.length() < 3) {
            throw new RegraNegocioException(
                    "Nome da categoria deve ter ao menos 3 caracteres."
            );
        }
    }

    /**
     * Retorna o id da categoria.
     *
     * @return id da categoria
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o id da categoria.
     *
     * @param id identificador da categoria
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retorna o nome da categoria.
     *
     * @return nome da categoria
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a descricao da categoria.
     *
     * @return descricao da categoria
     */
    public String getDescricao() {
        return descricao;
    }
}