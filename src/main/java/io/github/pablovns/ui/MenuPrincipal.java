package io.github.pablovns.ui;

import io.github.pablovns.domain.Noticia;
import io.github.pablovns.domain.Usuario;
import io.github.pablovns.services.NoticiaService;
import io.github.pablovns.services.UsuarioService;
import io.github.pablovns.utils.OrdenadorNoticias;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
    }

    private int lerOpcaoValida(int min, int max) {
        System.out.println(ESCOLHA_UMA_OPCAO);
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

    private String lerTextoValido(String mensagem) {
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

        int opcao = lerOpcaoValida(0, 3);
        if (opcao == 0) {
            return;
        }

        Optional<List<Noticia>> optionalNoticias = switch (opcao) {
            case 1 -> noticiaService.buscarPorTitulo(lerTextoValido("Digite o título: "));
            case 2 -> noticiaService.buscarPorPalavrasChave(lerTextoValido("Digite as palavras-chave: "));
            case 3 -> noticiaService.buscarPorData(lerDataValida());
            default -> Optional.empty();
        };

        List<Noticia> noticiasBusca = optionalNoticias.orElse(List.of());

        if (noticiasBusca.isEmpty()) {
            System.out.println("Nenhuma notícia encontrada.");
            return;
        }

        exibirNoticias(noticiasBusca);
    }

    private void exibirNoticiasFavoritas() {
        System.out.println("\n=== Notícias Favoritas ===");
        List<Noticia> favoritas = usuario.listarNoticiasFavoritas();
        if (favoritas == null || favoritas.isEmpty()) {
            System.out.println("Nenhuma notícia favorita.");
            return;
        }
        exibirNoticias(favoritas);
    }

    private void exibirNoticiasLidas() {
        System.out.println("\n=== Notícias Lidas ===");
        List<Noticia> lidas = usuario.listarNoticiasLidas();
        if (lidas == null || lidas.isEmpty()) {
            System.out.println("Nenhuma notícia lida.");
            return;
        }
        exibirNoticias(lidas);
    }

    private void exibirNoticiasParaLerDepois() {
        System.out.println("\n=== Para Ler Depois ===");
        List<Noticia> paraLer = usuario.listarNoticiasParaLerDepois();
        if (paraLer == null || paraLer.isEmpty()) {
            System.out.println("Nenhuma notícia marcada para ler depois.");
            return;
        }
        exibirNoticias(paraLer);
    }

    private void exibirNoticias(List<Noticia> noticias) {
        System.out.println("\nOrdenar por:");
        System.out.println("1. Título");
        System.out.println("2. Data");
        System.out.println("3. Tipo");
        System.out.println("0. Não ordenar");

        int opcao = lerOpcaoValida(0, 3);
        List<Noticia> noticiasOrdenadas = switch (opcao) {
            case 1 -> new ArrayList<>(OrdenadorNoticias.ordenarPorTitulo(noticias));
            case 2 -> new ArrayList<>(OrdenadorNoticias.ordenarPorData(noticias));
            case 3 -> new ArrayList<>(OrdenadorNoticias.ordenarPorTipo(noticias));
            default -> new ArrayList<>(OrdenadorNoticias.ordenarPorId(noticias));
        };

        for (Noticia noticia : noticiasOrdenadas) {
            System.out.printf("%n=== Notícia %d (ID %d) ===%n", noticiasOrdenadas.indexOf(noticia) + 1, noticia.getId());
            System.out.println(noticia);
        }
        
        interagirComNoticias(noticiasOrdenadas);
    }

    private void interagirComNoticias(List<Noticia> noticias) {
        while (true) {
            System.out.println("\nAções disponíveis:");
            System.out.println("1. Marcar/Desmarcar como favorita");
            System.out.println("2. Marcar/Desmarcar como lida");
            System.out.println("3. Marcar/Desmarcar para ler depois");
            System.out.println("0. Voltar");

            int opcao = lerOpcaoValida(0, 3);
            if (opcao == 0) {
                break;
            }

            System.out.printf("Digite o número da notícia (1 a %d): ", noticias.size());
            int indice = lerOpcaoValida(1, noticias.size()) - 1;
            Noticia noticiaTemporaria = noticias.get(indice);

            // Verifica se a notícia já existe na lista do usuário
            Optional<Noticia> noticiaExistente = usuario.obterNoticia(noticiaTemporaria.getId());
            Noticia noticiaParaAtualizar = noticiaExistente.orElse(noticiaTemporaria);

            switch (opcao) {
                case 1 -> {
                    noticiaParaAtualizar.alterarFavorita();
                    System.out.println(noticiaParaAtualizar.isFavorita()
                            ? "Notícia marcada como \"favorita\"."
                            : "Notícia desmarcada como \"favorita\".");
                }
                case 2 -> {
                    noticiaParaAtualizar.alterarLida();
                    System.out.println(noticiaParaAtualizar.isLida()
                            ? "Notícia marcada como \"lida\"."
                            : "Notícia desmarcada como \"lida\".");
                }
                case 3 -> {
                    noticiaParaAtualizar.alterarLerDepois();
                    System.out.println(noticiaParaAtualizar.isParaLerDepois()
                            ? "Notícia marcada como \"para ler depois\"."
                            : "Notícia desmarcada como \"para ler depois\".");
                }
                default -> System.out.println("Opção inválida. Por favor, tente novamente.");
            }

            // Atualiza a notícia na lista do usuário
            usuario.salvarOuAtualizarNoticia(noticiaParaAtualizar);
            usuarioService.salvarUsuario(usuario);

            // Atualiza a notícia na lista temporária para refletir as mudanças
            noticias.set(indice, noticiaParaAtualizar);
        }
    }
} 