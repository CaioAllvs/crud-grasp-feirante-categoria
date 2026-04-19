package feira.graspcrud;

import feira.graspcrud.controller.FeiranteController;
import feira.graspcrud.repository.CategoriaRepository;
import feira.graspcrud.repository.FeiranteRepository;
import feira.graspcrud.repository.json.CategoriaRepositoryJson;
import feira.graspcrud.repository.json.FeiranteRepositoryJson;
import feira.graspcrud.service.CategoriaService;
import feira.graspcrud.service.FeiranteService;

import java.nio.file.Path;
import java.util.Scanner;

/**
 * Ponto de entrada da aplicacao sem Maven.
 *
 * <p>Padrão GRASP: Creator — instancia e conecta
 * manualmente as dependências do sistema.
 */
public class Main {

    private static final Path CATEGORIAS_FILE =
            Path.of("data", "categorias.json");

    private static final Path FEIRANTES_FILE =
            Path.of("data", "feirantes.json");

    /**
     * Inicializa as dependências e executa o menu textual.
     *
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {

        CategoriaRepository categoriaRepository =
                new CategoriaRepositoryJson(CATEGORIAS_FILE);

        FeiranteRepository feiranteRepository =
                new FeiranteRepositoryJson(FEIRANTES_FILE);

        CategoriaService categoriaService =
                new CategoriaService(
                        categoriaRepository,
                        feiranteRepository
                );

        FeiranteService feiranteService =
                new FeiranteService(
                        feiranteRepository,
                        categoriaRepository
                );

        try (Scanner scanner = new Scanner(System.in)) {

            FeiranteController controller =
                    new FeiranteController(
                            feiranteService,
                            categoriaService,
                            scanner
                    );

            controller.iniciarMenu();
        }

        System.out.println(
                "Aplicacao finalizada."
        );
    }
}