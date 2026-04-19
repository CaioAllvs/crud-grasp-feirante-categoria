package feira.graspcrud.service;

import feira.graspcrud.domain.Feirante;
import feira.graspcrud.dto.FeiranteRequest;
import feira.graspcrud.exception.RegraNegocioException;
import feira.graspcrud.repository.CategoriaRepository;
import feira.graspcrud.repository.FeiranteRepository;

import java.util.List;

/**
 * Serviço responsável pelos casos de uso de feirante.
 *
 * <p>Padrão GRASP: Low Coupling — depende das interfaces
 * de repositório e não das implementações concretas.
 */
public class FeiranteService {

    private final FeiranteRepository feiranteRepository;
    private final CategoriaRepository categoriaRepository;

    /**
     * Cria o serviço com dependências por abstração.
     *
     * @param feiranteRepository repositorio de feirantes
     * @param categoriaRepository repositorio de categorias
     */
    public FeiranteService(
            FeiranteRepository feiranteRepository,
            CategoriaRepository categoriaRepository
    ) {
        this.feiranteRepository = feiranteRepository;
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Cadastra um novo feirante validando a categoria.
     *
     * @param request dados de entrada
     * @return feirante cadastrado
     */
    public Feirante criar(FeiranteRequest request) {
        validarCategoriaExiste(request.getCategoriaId());

        Feirante feirante = new Feirante(
                null,
                request.getNome(),
                request.getCpf(),
                request.isAtivo(),
                request.getCategoriaId()
        );

        feirante.validar();

        return feiranteRepository.salvar(feirante);
    }

    /**
     * Atualiza um feirante existente.
     *
     * @param id identificador do feirante
     * @param request novos dados
     * @return feirante atualizado
     */
    public Feirante atualizar(
            long id,
            FeiranteRequest request
    ) {
        buscarPorId(id);
        validarCategoriaExiste(request.getCategoriaId());

        Feirante feirante = new Feirante(
                id,
                request.getNome(),
                request.getCpf(),
                request.isAtivo(),
                request.getCategoriaId()
        );

        feirante.validar();

        return feiranteRepository.salvar(feirante);
    }

    /**
     * Busca feirante por id.
     *
     * @param id identificador
     * @return feirante encontrado
     */
    public Feirante buscarPorId(long id) {
        Feirante feirante =
                feiranteRepository.buscarPorId(id);

        if (feirante == null) {
            throw new RegraNegocioException(
                    "Feirante nao encontrado."
            );
        }

        return feirante;
    }

    /**
     * Lista todos os feirantes.
     *
     * @return lista de feirantes
     */
    public List<Feirante> listarTodos() {
        return feiranteRepository.listarTodos();
    }

    /**
     * Remove um feirante por id.
     *
     * @param id identificador
     */
    public void remover(long id) {
        if (!feiranteRepository.remover(id)) {
            throw new RegraNegocioException(
                    "Feirante nao encontrado."
            );
        }
    }

    private void validarCategoriaExiste(long categoriaId) {
        if (categoriaRepository.buscarPorId(categoriaId) == null) {
            throw new RegraNegocioException(
                    "Categoria nao encontrada."
            );
        }
    }
}