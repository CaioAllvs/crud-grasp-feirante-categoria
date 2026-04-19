package feira.graspcrud.repository.json;

import feira.graspcrud.domain.Categoria;
import feira.graspcrud.repository.CategoriaRepository;
import feira.graspcrud.util.JsonMini;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio JSON para categorias.
 *
 * <p>Padrão GRASP: Pure Fabrication + Indirection —
 * encapsula a persistência em JSON fora do domínio.
 */
public class CategoriaRepositoryJson implements CategoriaRepository {

    private final Path arquivo;
    private final List<Categoria> banco;
    private long proximoId;

    /**
     * Cria repositorio carregando dados do arquivo JSON.
     *
     * @param arquivo caminho do arquivo
     */
    public CategoriaRepositoryJson(Path arquivo) {
        this.arquivo = arquivo;
        this.banco = JsonMini.carregarCategorias(arquivo);

        this.proximoId = banco.stream()
                .mapToLong(c -> c.getId() == null ? 0 : c.getId())
                .max()
                .orElse(0) + 1;
    }

    /**
     * Salva ou atualiza uma categoria.
     *
     * @param categoria entidade
     * @return categoria persistida
     */
    @Override
    public Categoria salvar(Categoria categoria) {
        if (categoria.getId() == null) {
            categoria.setId(proximoId++);
            banco.add(categoria);

        } else {
            int idx = indexPorId(categoria.getId());

            if (idx >= 0) {
                banco.set(idx, categoria);
            } else {
                banco.add(categoria);
            }
        }

        JsonMini.salvarCategorias(arquivo, banco);
        return categoria;
    }

    /**
     * Lista todas as categorias.
     *
     * @return copia da lista de categorias
     */
    @Override
    public List<Categoria> listarTodos() {
        return new ArrayList<>(banco);
    }

    /**
     * Busca categoria por id.
     *
     * @param id identificador
     * @return categoria encontrada ou null
     */
    @Override
    public Categoria buscarPorId(long id) {
        for (Categoria c : banco) {
            if (c.getId() != null && c.getId() == id) {
                return c;
            }
        }

        return null;
    }

    /**
     * Verifica duplicidade de nome.
     *
     * @param nome nome a comparar
     * @param ignorarId id que pode ser ignorado
     * @return true quando houver duplicidade
     */
    @Override
    public boolean existeNome(String nome, Long ignorarId) {
        if (nome == null) {
            return false;
        }

        String alvo = nome.trim().toLowerCase();

        for (Categoria c : banco) {
            if (c.getNome() != null
                    && c.getNome().trim().toLowerCase().equals(alvo)) {

                if (ignorarId == null
                        || !ignorarId.equals(c.getId())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Remove categoria por id.
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
        JsonMini.salvarCategorias(arquivo, banco);

        return true;
    }

    private int indexPorId(long id) {
        for (int i = 0; i < banco.size(); i++) {
            Categoria c = banco.get(i);

            if (c.getId() != null && c.getId() == id) {
                return i;
            }
        }

        return -1;
    }
}