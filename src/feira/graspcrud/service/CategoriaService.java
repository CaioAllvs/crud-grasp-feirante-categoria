package feira.graspcrud.service;

import feira.graspcrud.domain.Categoria;
import feira.graspcrud.dto.CategoriaRequest;
import feira.graspcrud.exception.RegraNegocioException;
import feira.graspcrud.repository.CategoriaRepository;
import feira.graspcrud.repository.FeiranteRepository;

import java.util.List;

/**
 * Serviço responsável pelos casos de uso de categoria.
 *
 * <p>Padrão GRASP: Low Coupling — trabalha com interfaces
 * de repositório e mantém as regras fora do controller.
 */
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final FeiranteRepository feiranteRepository;

    /**
     * Cria o serviço com dependências por abstração.
     *
     * @param categoriaRepository repositorio de categorias
     * @param feiranteRepository repositorio de feirantes
     */
    public CategoriaService(
            CategoriaRepository categoriaRepository,
            FeiranteRepository feiranteRepository
    ) {
        this.categoriaRepository = categoriaRepository;
        this.feiranteRepository = feiranteRepository;
    }

    /**
     * Cadastra categoria com validações e unicidade.
     *
     * @param request dados de entrada
     * @return categoria cadastrada
     */
    public Categoria criar(CategoriaRequest request) {
        if (categoriaRepository.existeNome(
                request.getNome(),
                null
        )) {
            throw new RegraNegocioException(
                    "Ja existe categoria com este nome."
            );
        }

        Categoria categoria = new Categoria(
                null,
                request.getNome(),
                request.getDescricao()
        );

        categoria.validar();

        return categoriaRepository.salvar(categoria);
    }

    /**
     * Lista todas as categorias.
     *
     * @return lista de categorias
     */
    public List<Categoria> listarTodos() {
        return categoriaRepository.listarTodos();
    }

    /**
     * Busca categoria por id.
     *
     * @param id identificador
     * @return categoria encontrada
     */
    public Categoria buscarPorId(long id) {
        Categoria categoria =
                categoriaRepository.buscarPorId(id);

        if (categoria == null) {
            throw new RegraNegocioException(
                    "Categoria nao encontrada."
            );
        }

        return categoria;
    }

    /**
     * Remove categoria quando não estiver em uso.
     *
     * @param id identificador
     */
    public void remover(long id) {
        if (feiranteRepository.existePorCategoria(id)) {
            throw new RegraNegocioException(
                    "Nao e permitido remover categoria em uso por feirante."
            );
        }

        if (!categoriaRepository.remover(id)) {
            throw new RegraNegocioException(
                    "Categoria nao encontrada."
            );
        }
    }
}