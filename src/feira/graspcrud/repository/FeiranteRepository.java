package feira.graspcrud.repository;

import feira.graspcrud.domain.Feirante;

import java.util.List;

/**
 * Abstracao de persistencia para feirantes.
 *
 * <p>Padrão GRASP: Protected Variations — define uma interface
 * estável para os serviços dependerem por abstração.
 */
public interface FeiranteRepository {

    /**
     * Salva ou atualiza um feirante.
     *
     * @param feirante entidade
     * @return entidade persistida
     */
    Feirante salvar(Feirante feirante);

    /**
     * Lista todos os feirantes.
     *
     * @return lista de feirantes
     */
    List<Feirante> listarTodos();

    /**
     * Busca um feirante por id.
     *
     * @param id identificador
     * @return feirante encontrado ou null
     */
    Feirante buscarPorId(long id);

    /**
     * Remove um feirante por id.
     *
     * @param id identificador
     * @return true se removeu
     */
    boolean remover(long id);

    /**
     * Verifica se existe feirante vinculado a uma categoria.
     *
     * @param categoriaId identificador da categoria
     * @return true se existir vínculo
     */
    boolean existePorCategoria(long categoriaId);
}