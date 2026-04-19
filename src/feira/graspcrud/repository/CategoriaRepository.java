package feira.graspcrud.repository;

import feira.graspcrud.domain.Categoria;

import java.util.List;

/**
 * Abstracao de persistencia para categorias.
 *
 * <p>Padrão GRASP: Protected Variations — define uma interface
 * estável para os serviços dependerem por abstração.
 */
public interface CategoriaRepository {

    /**
     * Salva ou atualiza uma categoria.
     *
     * @param categoria entidade
     * @return entidade persistida
     */
    Categoria salvar(Categoria categoria);

    /**
     * Lista todas as categorias.
     *
     * @return lista de categorias
     */
    List<Categoria> listarTodos();

    /**
     * Busca categoria por id.
     *
     * @param id identificador
     * @return categoria encontrada ou null
     */
    Categoria buscarPorId(long id);

    /**
     * Verifica se já existe uma categoria com o mesmo nome.
     *
     * @param nome nome a buscar
     * @param ignorarId id que deve ser ignorado na comparação
     * @return true quando houver duplicidade
     */
    boolean existeNome(String nome, Long ignorarId);

    /**
     * Remove categoria por id.
     *
     * @param id identificador
     * @return true se removeu
     */
    boolean remover(long id);
}