package feira.graspcrud.util;

import feira.graspcrud.domain.Categoria;
import feira.graspcrud.domain.Feirante;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilitario simples para leitura e escrita JSON sem bibliotecas externas.
 *
 * <p>Padrão GRASP: Pure Fabrication — concentra a responsabilidade
 * técnica de persistência fora das classes de domínio.
 */
public class JsonMini {

    private static final Pattern OBJ_PATTERN =
            Pattern.compile("\\{([^}]*)}");

    private static final Pattern FIELD_PATTERN =
            Pattern.compile(
                    "\"([^\"]+)\"\\s*:\\s*(\"(?:\\\\.|[^\"])*\"|-?\\d+(?:\\.\\d+)?|true|false|null)"
            );

    /**
     * Carrega lista de categorias.
     *
     * @param arquivo caminho do JSON
     * @return lista carregada
     */
    public static List<Categoria> carregarCategorias(Path arquivo) {
        List<Categoria> categorias = new ArrayList<>();
        String content = lerArquivo(arquivo);

        if (content == null || content.trim().isEmpty()) {
            return categorias;
        }

        for (Map<String, String> m : parseArray(content)) {
            Long id = parseLong(m.get("id"));
            String nome = parseString(m.get("nome"));
            String descricao = parseString(m.get("descricao"));

            categorias.add(new Categoria(id, nome, descricao));
        }

        return categorias;
    }

    /**
     * Carrega lista de feirantes.
     *
     * @param arquivo caminho do JSON
     * @return lista carregada
     */
    public static List<Feirante> carregarFeirantes(Path arquivo) {
        List<Feirante> feirantes = new ArrayList<>();
        String content = lerArquivo(arquivo);

        if (content == null || content.trim().isEmpty()) {
            return feirantes;
        }

        for (Map<String, String> m : parseArray(content)) {
            Long id = parseLong(m.get("id"));
            String nome = parseString(m.get("nome"));
            String cpf = parseString(m.get("cpf"));
            boolean ativo = parseBoolean(m.get("ativo"));
            Long categoriaId = parseLong(m.get("categoriaId"));

            feirantes.add(
                    new Feirante(
                            id,
                            nome,
                            cpf,
                            ativo,
                            categoriaId == null ? 0L : categoriaId
                    )
            );
        }

        return feirantes;
    }

    /**
     * Salva lista de categorias.
     *
     * @param arquivo caminho do JSON
     * @param categorias lista a persistir
     */
    public static void salvarCategorias(Path arquivo, List<Categoria> categorias) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < categorias.size(); i++) {
            Categoria c = categorias.get(i);

            sb.append("  {\"id\": ").append(c.getId())
                    .append(", \"nome\": \"").append(escape(c.getNome())).append("\"")
                    .append(", \"descricao\": \"").append(escape(c.getDescricao())).append("\"}")
            ;

            if (i < categorias.size() - 1) {
                sb.append(",");
            }

            sb.append("\n");
        }

        sb.append("]\n");
        escreverArquivo(arquivo, sb.toString());
    }

    /**
     * Salva lista de feirantes.
     *
     * @param arquivo caminho do JSON
     * @param feirantes lista a persistir
     */
    public static void salvarFeirantes(Path arquivo, List<Feirante> feirantes) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < feirantes.size(); i++) {
            Feirante f = feirantes.get(i);

            sb.append("  {\"id\": ").append(f.getId())
                    .append(", \"nome\": \"").append(escape(f.getNome())).append("\"")
                    .append(", \"cpf\": \"").append(escape(f.getCpf())).append("\"")
                    .append(", \"ativo\": ").append(f.isAtivo())
                    .append(", \"categoriaId\": ").append(f.getCategoriaId())
                    .append("}")
            ;

            if (i < feirantes.size() - 1) {
                sb.append(",");
            }

            sb.append("\n");
        }

        sb.append("]\n");
        escreverArquivo(arquivo, sb.toString());
    }

    private static List<Map<String, String>> parseArray(String json) {
        List<Map<String, String>> list = new ArrayList<>();

        Matcher objMatcher = OBJ_PATTERN.matcher(json);

        while (objMatcher.find()) {
            String body = objMatcher.group(1);

            Map<String, String> map = new LinkedHashMap<>();
            Matcher fieldMatcher = FIELD_PATTERN.matcher(body);

            while (fieldMatcher.find()) {
                map.put(fieldMatcher.group(1), fieldMatcher.group(2));
            }

            list.add(map);
        }

        return list;
    }

    private static String lerArquivo(Path arquivo) {
        try {
            if (!Files.exists(arquivo)) {
                return null;
            }

            return Files.readString(
                    arquivo,
                    StandardCharsets.UTF_8
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao ler JSON: " + arquivo,
                    e
            );
        }
    }

    private static void escreverArquivo(Path arquivo, String content) {
        try {
            Path parent = arquivo.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.writeString(
                    arquivo,
                    content,
                    StandardCharsets.UTF_8
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao salvar JSON: " + arquivo,
                    e
            );
        }
    }

    private static String escape(String s) {
        if (s == null) {
            return "";
        }

        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    private static String unquote(String raw) {
        if (raw == null) {
            return null;
        }

        raw = raw.trim();

        if (raw.equals("null")) {
            return null;
        }

        if (raw.startsWith("\"")
                && raw.endsWith("\"")
                && raw.length() >= 2) {

            String x = raw.substring(1, raw.length() - 1);

            return x.replace("\\n", "\n")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
        }

        return raw;
    }

    private static String parseString(String raw) {
        return unquote(raw);
    }

    private static Long parseLong(String raw) {
        if (raw == null || raw.equals("null")) {
            return null;
        }

        return Long.parseLong(
                raw.replace("\"", "").trim()
        );
    }

    private static boolean parseBoolean(String raw) {
        if (raw == null || raw.equals("null")) {
            return false;
        }

        return Boolean.parseBoolean(
                raw.replace("\"", "").trim()
        );
    }
}