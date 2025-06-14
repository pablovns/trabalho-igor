package io.github.pablovns.ui;

import io.github.pablovns.domain.Noticia;
import io.github.pablovns.domain.Usuario;
import io.github.pablovns.services.NoticiaService;
import io.github.pablovns.services.UsuarioService;
import io.github.pablovns.utils.OrdenadorNoticias;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Classe responsável por gerenciar a interface com o usuário.
 */
public class MenuPrincipal {
    public static final String ESCOLHA_UMA_OPCAO = "Escolha uma opção: ";

    private final Scanner scanner;
    private final NoticiaService noticiaService;
    private final UsuarioService usuarioService;
    private Usuario usuario;

    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        this.noticiaService = new NoticiaService();
        this.usuarioService = new UsuarioService();
    }

    public void iniciar() {
        carregarOuCriarUsuario();
        
        while (true) {
            exibirMenu();
            int opcao = lerOpcaoValida(0, 4);
            
            switch (opcao) {
                case 1 -> buscarNoticias();
                case 2 -> exibirNoticiasFavoritas();
                case 3 -> exibirNoticiasLidas();
                case 4 -> exibirNoticiasParaLerDepois();
                case 0 -> {
                    System.out.println("Salvando dados e encerrando...");
                    usuarioService.salvarUsuario(usuario);
                    return;
                }
                default -> {
                    // Caso não seja uma opção válida, apenas continue o loop
                }
            }
        }
    }

    private void carregarOuCriarUsuario() {
        Optional<Usuario> optionalUsuario = usuarioService.carregarUsuario();
        if (optionalUsuario.isEmpty()) {
            System.out.println("Bem-vindo ao Sistema de Notícias do IBGE!");
            String nome;
            do {
                System.out.println("Por favor, digite seu nome ou apelido (mínimo 2 caracteres): ");
                nome = scanner.nextLine().trim();
            } while (nome.length() < 2);
            
            usuario = new Usuario(nome);
            usuarioService.salvarUsuario(usuario);
        } else {
            usuario = optionalUsuario.get();
            System.out.println("Bem-vindo de volta, " + usuario.getNome() + "!");
        }
    }

    private void exibirMenu() {
        System.out.println("\n=== Menu Principal ===");
        System.out.println("1. Buscar Notícias");
        System.out.println("2. Notícias Favoritas");
        System.out.println("3. Notícias Lidas");
        System.out.println("4. Para Ler Depois");
        System.out.println("0. Sair");
        System.out.println(ESCOLHA_UMA_OPCAO);
    }

    private int lerOpcaoValida(int min, int max) {
        while (true) {
            String entrada = scanner.nextLine().trim();
            try {
                int opcao = Integer.parseInt(entrada);
                if (opcao >= min && opcao <= max) {
                    return opcao;
                } else {
                    System.out.println("Opção fora do intervalo permitido.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite apenas números.");
            }
        }
    }

    private LocalDate lerDataValida() {
        final String msgErro = "Data inválida! Use o formato DDMMYYYY (exemplo: 07062025)";

        while (true) {
            System.out.println("Digite a data (DDMMYYYY): ");
            String entrada = scanner.nextLine().trim();

            if (entrada.matches("\\d{8}")) {
                try {
                    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("ddMMyyyy");
                    LocalDate data = LocalDate.parse(entrada, formatador);

                    if (data.isBefore(LocalDate.now())) {
                        return data;
                    } else {
                        System.out.println("A data não pode estar no futuro!");
                    }

                } catch (DateTimeParseException e) {
                    System.out.println(msgErro);
                }
            } else {
                System.out.println(msgErro);
            }
        }
    }

    private String lerTextoNaoVazio(String mensagem) {
        while (true) {
            System.out.println(mensagem);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("O texto não pode estar vazio. Por favor, tente novamente.");
        }
    }

    private void buscarNoticias() {
        System.out.println("\n=== Buscar Notícias ===");
        System.out.println("1. Buscar por título");
        System.out.println("2. Buscar por palavras-chave");
        System.out.println("3. Buscar por data");
        System.out.println("0. Voltar");
        System.out.println(ESCOLHA_UMA_OPCAO);

        int opcao = lerOpcaoValida(0, 3);
        if (opcao == 0) {
            return;
        }

        Optional<List<Noticia>> optionalNoticias = switch (opcao) {
            case 1 -> noticiaService.buscarPorTitulo(lerTextoNaoVazio("Digite o título: "));
            case 2 -> noticiaService.buscarPorPalavrasChave(lerTextoNaoVazio("Digite as palavras-chave: "));
            case 3 -> noticiaService.buscarPorData(lerDataValida());
            default -> Optional.empty();
        };

        List<Noticia> noticias = optionalNoticias.orElse(List.of());

        if (noticias.isEmpty()) {
            System.out.println("Nenhuma notícia encontrada.");
            return;
        }

        exibirNoticias(noticias);
        interagirComNoticias(noticias);
    }

    private void exibirNoticiasFavoritas() {
        System.out.println("\n=== Notícias Favoritas ===");
        List<Noticia> favoritas = usuario.getNoticiasFavoritas();
        if (favoritas.isEmpty()) {
            System.out.println("Nenhuma notícia favorita.");
            return;
        }
        exibirNoticias(favoritas);
        interagirComNoticias(favoritas);
    }

    private void exibirNoticiasLidas() {
        System.out.println("\n=== Notícias Lidas ===");
        List<Noticia> lidas = usuario.getNoticiasLidas();
        if (lidas.isEmpty()) {
            System.out.println("Nenhuma notícia lida.");
            return;
        }
        exibirNoticias(lidas);
        interagirComNoticias(lidas);
    }

    private void exibirNoticiasParaLerDepois() {
        System.out.println("\n=== Para Ler Depois ===");
        List<Noticia> paraLer = usuario.getNoticiasParaLerDepois();
        if (paraLer.isEmpty()) {
            System.out.println("Nenhuma notícia marcada para ler depois.");
            return;
        }
        exibirNoticias(paraLer);
        interagirComNoticias(paraLer);
    }

    private void exibirNoticias(List<Noticia> noticias) {
        System.out.println("\nOrdenar por:");
        System.out.println("1. Título");
        System.out.println("2. Data");
        System.out.println("3. Tipo");
        System.out.println("0. Não ordenar");
        System.out.println(ESCOLHA_UMA_OPCAO);

        int opcao = lerOpcaoValida(0, 3);
        switch (opcao) {
            case 1 -> OrdenadorNoticias.ordenarPorTitulo(noticias);
            case 2 -> OrdenadorNoticias.ordenarPorData(noticias);
            case 3 -> OrdenadorNoticias.ordenarPorTipo(noticias);
            default -> OrdenadorNoticias.ordenarPorId(noticias);
        }

        for (int i = 0; i < noticias.size(); i++) {
            Noticia noticia = noticias.get(i);
            System.out.printf("%n=== Notícia %d ===%n", i + 1);
            System.out.println(noticia);
        }
    }

    private void interagirComNoticias(List<Noticia> noticias) {
        while (true) {
            System.out.println("\nAções:");
            System.out.println("1. Marcar/Desmarcar como favorita");
            System.out.println("2. Marcar como lida");
            System.out.println("3. Marcar/Desmarcar para ler depois");
            System.out.println("0. Voltar");
            System.out.println(ESCOLHA_UMA_OPCAO);

            int opcao = lerOpcaoValida(0, 3);
            if (opcao == 0) {
                break;
            }

            System.out.printf("Digite o número da notícia (1 a %d): ", noticias.size());
            int numeroNoticia = lerOpcaoValida(1, noticias.size()) - 1;
            Noticia noticia = noticias.get(numeroNoticia);

            if (opcao == 1) {
                if (noticia.isFavorita()) {
                    usuario.removerNoticiaFavorita(noticia);
                    System.out.println("Notícia removida dos favoritos.");
                } else {
                    usuario.adicionarNoticiaFavorita(noticia);
                    System.out.println("Notícia adicionada aos favoritos.");
                }
            } else if (opcao == 2) {
                usuario.marcarComoLida(noticia);
                System.out.println("Notícia marcada como lida.");
            } else if (opcao == 3) {
                if (noticia.isParaLerDepois()) {
                    usuario.removerParaLerDepois(noticia);
                    System.out.println("Notícia removida da lista para ler depois.");
                } else {
                    usuario.adicionarParaLerDepois(noticia);
                    System.out.println("Notícia adicionada à lista para ler depois.");
                }
            }

            usuarioService.salvarUsuario(usuario);
        }
    }
} 