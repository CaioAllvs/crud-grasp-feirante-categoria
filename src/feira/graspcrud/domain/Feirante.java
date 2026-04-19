package feira.graspcrud.domain;

import feira.graspcrud.exception.RegraNegocioException;

/**
 * Entidade de feirante cadastrada no sistema.
 *
 * <p>Padrão GRASP: Information Expert — a própria classe
 * valida suas regras de negócio.
 */
public class Feirante {

    private Long id;
    private String nome;
    private String cpf;
    private boolean ativo;
    private long categoriaId;

    /**
     * Cria feirante com seus dados principais.
     *
     * @param id identificador (pode ser nulo para novo cadastro)
     * @param nome nome do feirante
     * @param cpf cpf do feirante
     * @param ativo indica se o feirante está habilitado
     * @param categoriaId id da categoria associada
     */
    public Feirante(Long id, String nome, String cpf, boolean ativo, long categoriaId) {
        this.id = id;
        this.nome = nome == null ? null : nome.trim();
        this.cpf = cpf == null ? null : cpf.trim();
        this.ativo = ativo;
        this.categoriaId = categoriaId;
    }

    /**
     * Valida as regras de negócio da entidade.
     */
    public void validar() {
        if (nome == null || nome.length() < 3) {
            throw new RegraNegocioException(
                    "Nome do feirante deve ter ao menos 3 caracteres."
            );
        }

        if (cpf == null || cpf.isBlank()) {
            throw new RegraNegocioException(
                    "CPF do feirante é obrigatório."
            );
        }

        if (!cpf.matches("\\d{11}")) {
            throw new RegraNegocioException(
                    "CPF deve ter exatamente 11 dígitos numéricos."
            );
        }

        if (categoriaId <= 0) {
            throw new RegraNegocioException(
                    "Categoria inválida."
            );
        }
    }

    /**
     * Retorna o id do feirante.
     *
     * @return id do feirante
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o id do feirante.
     *
     * @param id identificador do feirante
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retorna o nome do feirante.
     *
     * @return nome do feirante
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o CPF do feirante.
     *
     * @return cpf do feirante
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Indica se o feirante está ativo.
     *
     * @return true quando ativo
     */
    public boolean isAtivo() {
        return ativo;
    }

    /**
     * Retorna o id da categoria associada.
     *
     * @return id da categoria
     */
    public long getCategoriaId() {
        return categoriaId;
    }
}