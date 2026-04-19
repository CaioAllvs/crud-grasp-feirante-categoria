# CRUD com Padroes GRASP - Feira Livre

Este projeto consiste em um sistema de gestão para feira livre, focado no cadastro de feirantes e suas respectivas categorias. O objetivo principal é a aplicação prática dos padrões GRASP (General Responsibility Assignment Software Patterns) em uma arquitetura desacoplada e sem o uso de frameworks externos.

## Integrantes do Grupo
* Caio Alves
* Antonio Leanderson

## Tema Selecionado
* Grupo: F
* Entidade Principal: Feirante
* Classificador: CategoriaFeirante

## Tecnologias Utilizadas
* Java Puro (JDK 11 ou superior)
* Persistência em arquivo JSON local
* Interface via terminal (Console)

## Instruções para Compilação e Execução

Como o projeto não utiliza Maven ou Gradle, a compilação e execução devem ser feitas manualmente via terminal a partir da raiz do projeto:

1. Compilação:
Abra o terminal na pasta raiz do projeto e execute o seguinte comando para compilar todas as classes para a pasta 'out':
javac -d out -sourcepath src src/feira/graspcrud/Main.java

2. Execução:
Após a compilação, execute o sistema com o comando:
java -cp out feira.graspcrud.Main

Nota: Certifique-se de que a pasta 'data' exista no diretório de execução para que os arquivos JSON sejam lidos e salvos corretamente.

## Padrões GRASP Aplicados

Abaixo estão listados os padrões obrigatórios e as classes onde foram implementados para garantir a separação de responsabilidades.

| Padrão GRASP | Classe Correspondente | Justificativa |
| :--- | :--- | :--- |
| Information Expert | Feirante, Categoria | As entidades validam suas próprias regras de negócio e estado. |
| Creator | Main | Responsável por instanciar e conectar manualmente as dependências do sistema. |
| Controller | FeiranteController | Recebe as entradas do menu textual e delega as ações para os serviços. |
| Low Coupling | FeiranteService, CategoriaService | Os serviços dependem de interfaces de repositório, não de implementações concretas. |
| High Cohesion | Todas as classes | Cada pacote (dto, domain, service, repository) possui uma única responsabilidade clara. |
| Pure Fabrication | JsonMini, RepositoryJson | Classes criadas para lidar com detalhes técnicos de persistência, mantendo o domínio limpo. |
| Indirection | Service -> Repository (Interface) | O serviço não acessa o JSON diretamente; ele utiliza uma interface mediadora. |
| Protected Variations | CategoriaRepository, FeiranteRepository | O uso de interfaces protege o sistema contra mudanças na forma de armazenamento (JSON para Banco). |

## Regras de Negócio Implementadas
* O campo nome do feirante e da categoria é obrigatório e deve ter no mínimo 3 caracteres.
* O CPF do feirante deve conter exatamente 11 dígitos numéricos.
* Não é permitido remover uma categoria que possua feirantes vinculados a ela.
* O nome da categoria deve ser único no sistema.
