package feira.graspcrud.controller;

import feira.graspcrud.domain.Categoria;
import feira.graspcrud.domain.Feirante;
import feira.graspcrud.dto.CategoriaRequest;
import feira.graspcrud.dto.FeiranteRequest;
import feira.graspcrud.exception.RegraNegocioException;
import feira.graspcrud.service.CategoriaService;
import feira.graspcrud.service.FeiranteService;

import java.util.List;
import java.util.Scanner;

/**
 * Controller de entrada textual para orquestrar os casos de uso.
 *
 * <p>Padrão GRASP: Controller — recebe as ações do menu
 * e delega a execução aos serviços.
 */
public class FeiranteController {

    private final FeiranteService feiranteService;
    private final CategoriaService categoriaService;
    private final Scanner scanner;

    /**
     * Cria o controller com as dependências necessárias.
     *
     * @param feiranteService servico de feirante
     * @param categoriaService servico de categoria
     * @param scanner leitor do terminal
     */
    public FeiranteController(
            FeiranteService feiranteService,
            CategoriaService categoriaService,
            Scanner scanner
    ) {
        this.feiranteService = feiranteService;
        this.categoriaService = categoriaService;
        this.scanner = scanner;
    }

    /**
     * Inicia o loop principal do menu textual.
     */
    public void iniciarMenu() {
        boolean executando = true;

        while (executando) {
            mostrarMenu();
            String opcao = scanner.nextLine().trim();

            try {
                switch (opcao) {
                    case "1":
                        cadastrarCategoria();
                        break;
                    case "2":
                        listarCategorias();
                        break;
                    case "3":
                        cadastrarFeirante();
                        break;
                    case "4":
                        listarFeirantes();
                        break;
                    case "5":
                        buscarFeirante();
                        break;
                    case "6":
                        atualizarFeirante();
                        break;
                    case "7":
                        excluirFeirante();
                        break;
                    case "8":
                        excluirCategoria();
                        break;
                    case "9":
                        executando = false;
                        break;
                    default:
                        System.out.println("Opcao invalida.");
                        break;
                }

            } catch (RegraNegocioException e) {
                System.out.println(
                        "Regra de negocio: " + e.getMessage()
                );

            } catch (Exception e) {
                System.out.println(
                        "Erro: " + e.getMessage()
                );
            }

            System.out.println();
        }
    }

    /**
     * Exibe o menu principal.
     */
    public void mostrarMenu() {
        System.out.println("==============================");
        System.out.println(" CRUD Feirante / Categoria ");
        System.out.println("==============================");
        System.out.println("1. Cadastrar categoria");
        System.out.println("2. Listar categorias");
        System.out.println("3. Cadastrar feirante");
        System.out.println("4. Listar feirantes");
        System.out.println("5. Buscar feirante por id");
        System.out.println("6. Atualizar feirante");
        System.out.println("7. Excluir feirante");
        System.out.println("8. Excluir categoria");
        System.out.println("9. Sair");
        System.out.print("Escolha uma opcao: ");
    }

    private void cadastrarCategoria() {
        System.out.print("Nome da categoria: ");
        String nome = scanner.nextLine();

        System.out.print("Descricao da categoria: ");
        String descricao = scanner.nextLine();

        Categoria categoria = categoriaService.criar(
                new CategoriaRequest(nome, descricao)
        );

        System.out.println(
                "Categoria cadastrada com ID: "
                        + categoria.getId()
        );
    }

    private void listarCategorias() {
        List<Categoria> categorias =
                categoriaService.listarTodos();

        if (categorias.isEmpty()) {
            System.out.println(
                    "Nenhuma categoria cadastrada."
            );
            return;
        }

        for (Categoria c : categorias) {
            System.out.printf(
                    "- ID: %d | Nome: %s | Descricao: %s%n",
                    c.getId(),
                    c.getNome(),
                    c.getDescricao()
            );
        }
    }

    private void cadastrarFeirante() {
        if (categoriaService.listarTodos().isEmpty()) {
            throw new RegraNegocioException(
                    "Cadastre ao menos uma categoria antes."
            );
        }

        listarCategorias();

        System.out.print("Nome do feirante: ");
        String nome = scanner.nextLine();

        System.out.print("CPF do feirante: ");
        String cpf = scanner.nextLine();

        System.out.print("Feirante ativo? (true/false): ");
        boolean ativo =
                Boolean.parseBoolean(scanner.nextLine());

        System.out.print("ID da categoria: ");
        long categoriaId =
                Long.parseLong(scanner.nextLine());

        Feirante feirante = feiranteService.criar(
                new FeiranteRequest(
                        nome,
                        cpf,
                        ativo,
                        categoriaId
                )
        );

        System.out.println(
                "Feirante cadastrado com ID: "
                        + feirante.getId()
        );
    }

    private void listarFeirantes() {
        List<Feirante> feirantes =
                feiranteService.listarTodos();

        if (feirantes.isEmpty()) {
            System.out.println(
                    "Nenhum feirante cadastrado."
            );
            return;
        }

        for (Feirante f : feirantes) {
            Categoria categoria =
                    categoriaService.buscarPorId(
                            f.getCategoriaId()
                    );

            System.out.printf(
                    "- ID: %d | Nome: %s | CPF: %s | Ativo: %s | Categoria: %s%n",
                    f.getId(),
                    f.getNome(),
                    f.getCpf(),
                    f.isAtivo(),
                    categoria.getNome()
            );
        }
    }

    private void buscarFeirante() {
        System.out.print("ID do feirante: ");
        long id =
                Long.parseLong(scanner.nextLine());

        Feirante feirante =
                feiranteService.buscarPorId(id);

        Categoria categoria =
                categoriaService.buscarPorId(
                        feirante.getCategoriaId()
                );

        System.out.printf(
                "Feirante: ID=%d | Nome=%s | CPF=%s | Ativo=%s | Categoria=%s%n",
                feirante.getId(),
                feirante.getNome(),
                feirante.getCpf(),
                feirante.isAtivo(),
                categoria.getNome()
        );
    }

    private void atualizarFeirante() {
        System.out.print(
                "ID do feirante para atualizar: "
        );

        long id =
                Long.parseLong(scanner.nextLine());

        listarCategorias();

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();

        System.out.print("Novo CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Ativo? (true/false): ");
        boolean ativo =
                Boolean.parseBoolean(scanner.nextLine());

        System.out.print("Novo ID da categoria: ");
        long categoriaId =
                Long.parseLong(scanner.nextLine());

        feiranteService.atualizar(
                id,
                new FeiranteRequest(
                        nome,
                        cpf,
                        ativo,
                        categoriaId
                )
        );

        System.out.println(
                "Feirante atualizado com sucesso."
        );
    }

    private void excluirFeirante() {
        System.out.print(
                "ID do feirante para excluir: "
        );

        long id =
                Long.parseLong(scanner.nextLine());

        feiranteService.remover(id);

        System.out.println(
                "Feirante excluido com sucesso."
        );
    }

    private void excluirCategoria() {
        System.out.print(
                "ID da categoria para excluir: "
        );

        long id =
                Long.parseLong(scanner.nextLine());

        categoriaService.remover(id);

        System.out.println(
                "Categoria excluida com sucesso."
        );
    }
}