package feira.graspcrud.repository.json;

import feira.graspcrud.domain.Feirante;
import feira.graspcrud.repository.FeiranteRepository;
import feira.graspcrud.util.JsonMini;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio JSON para feirantes.
 *
 * <p>Padrão GRASP: Pure Fabrication + Indirection —
 * concentra a persistência em arquivo fora do domínio.
 */
public class FeiranteRepositoryJson implements FeiranteRepository {

    private final Path arquivo;
    private final List<Feirante> banco;
    private long proximoId;

    /**
     * Cria repositorio carregando os dados do arquivo JSON.
     *
     * @param arquivo caminho do arquivo
     */
    public FeiranteRepositoryJson(Path arquivo) {
        this.arquivo = arquivo;
        this.banco = JsonMini.carregarFeirantes(arquivo);

        this.proximoId = banco.stream()
                .mapToLong(f -> f.getId() == null ? 0 : f.getId())
                .max()
                .orElse(0) + 1;
    }

    /**
     * Salva ou atualiza um feirante.
     *
     * @param feirante entidade
     * @return feirante persistido
     */
    @Override
    public Feirante salvar(Feirante feirante) {
        if (feirante.getId() == null) {
            feirante.setId(proximoId++);
            banco.add(feirante);

        } else {
            int idx = indexPorId(feirante.getId());

            if (idx >= 0) {
                banco.set(idx, feirante);
            } else {
                banco.add(feirante);
            }
        }

        JsonMini.salvarFeirantes(arquivo, banco);
        return feirante;
    }

    /**
     * Lista todos os feirantes.
     *
     * @return copia da lista de feirantes
     */
    @Override
    public List<Feirante> listarTodos() {
        return new ArrayList<>(banco);
    }

    /**
     * Busca feirante por id.
     *
     * @param id identificador
     * @return feirante encontrado ou null
     */
    @Override
    public Feirante buscarPorId(long id) {
        for (Feirante f : banco) {
            if (f.getId() != null && f.getId() == id) {
                return f;
            }
        }

        return null;
    }

    /**
     * Remove feirante por id.
     *
     * @param id identificador
     * @return true se removeu
     */
    @Override
    public boolean remover(long id) {
        int idx = indexPorId(id);

        if (idx < 0) {
            return false;
        }

        banco.remove(idx);
        JsonMini.salvarFeirantes(arquivo, banco);

        return true;
    }

    /**
     * Verifica se existe feirante vinculado a uma categoria.
     *
     * @param categoriaId id da categoria
     * @return true quando existir vínculo
     */
    @Override
    public boolean existePorCategoria(long categoriaId) {
        for (Feirante f : banco) {
            if (f.getCategoriaId() == categoriaId) {
                return true;
            }
        }

        return false;
    }

    private int indexPorId(long id) {
        for (int i = 0; i < banco.size(); i++) {
            Feirante f = banco.get(i);

            if (f.getId() != null && f.getId() == id) {
                return i;
            }
        }

        return -1;
    }
}