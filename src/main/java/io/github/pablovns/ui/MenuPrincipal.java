package io.github.pablovns.ui;

import io.github.pablovns.domain.Noticia;
import io.github.pablovns.domain.Usuario;
import io.github.pablovns.services.NoticiaService;
import io.github.pablovns.services.UsuarioService;
import io.github.pablovns.utils.OrdenadorNoticias;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Classe responsável por gerenciar a interface com o usuário.
 */
public class MenuPrincipal {
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
            int opcao = lerOpcao();

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
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void carregarOuCriarUsuario() {
        Optional<Usuario> optionalUsuario = usuarioService.carregarUsuario();
        if (optionalUsuario.isEmpty()) {
            System.out.println("Bem-vindo ao Sistema de Notícias do IBGE!");
            System.out.println("Por favor, digite seu nome ou apelido: ");
            String nome = scanner.nextLine();
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
        System.out.println("Escolha uma opção: ");
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void buscarNoticias() {
        System.out.println("\n=== Buscar Notícias ===");
        System.out.println("1. Buscar por título");
        System.out.println("2. Buscar por palavras-chave");
        System.out.println("3. Buscar por data");
        System.out.println("Escolha uma opção: ");

        int opcao = lerOpcao();
        List<Noticia> noticias = switch (opcao) {
            case 1 -> {
                System.out.println("Digite o título: ");
                yield noticiaService.buscarPorTitulo(scanner.nextLine());
            }
            case 2 -> {
                System.out.println("Digite as palavras-chave: ");
                yield noticiaService.buscarPorPalavrasChave(scanner.nextLine());
            }
            case 3 -> {
                System.out.println("Digite a data (AAAAMMDD): ");
                yield noticiaService.buscarPorData(scanner.nextLine());
            }
            default -> null;
        };

        if (noticias == null || noticias.isEmpty()) {
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
        System.out.println("Escolha uma opção (ou pressione ENTER para não ordenar): ");

        String opcao = scanner.nextLine();
        switch (opcao) {
            case "1" -> OrdenadorNoticias.ordenarPorTitulo(noticias);
            case "2" -> OrdenadorNoticias.ordenarPorData(noticias);
            case "3" -> OrdenadorNoticias.ordenarPorTipo(noticias);
        }

        for (int i = 0; i < noticias.size(); i++) {
            Noticia noticia = noticias.get(i);
            System.out.printf("\n=== Notícia %d ===\n", i + 1);
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
            System.out.println("Escolha uma opção: ");

            int opcao = lerOpcao();
            if (opcao == 0) break;

            System.out.println("Digite o número da notícia: ");
            try {
                int numeroNoticia = Integer.parseInt(scanner.nextLine()) - 1;
                if (numeroNoticia < 0 || numeroNoticia >= noticias.size()) {
                    System.out.println("Número de notícia inválido!");
                    continue;
                }

                Noticia noticia = noticias.get(numeroNoticia);
                switch (opcao) {
                    case 1 -> {
                        if (noticia.isFavorita()) {
                            usuario.removerNoticiaFavorita(noticia);
                            System.out.println("Notícia removida dos favoritos.");
                        } else {
                            usuario.adicionarNoticiaFavorita(noticia);
                            System.out.println("Notícia adicionada aos favoritos.");
                        }
                    }
                    case 2 -> {
                        usuario.marcarComoLida(noticia);
                        System.out.println("Notícia marcada como lida.");
                    }
                    case 3 -> {
                        if (noticia.isParaLerDepois()) {
                            usuario.removerParaLerDepois(noticia);
                            System.out.println("Notícia removida da lista para ler depois.");
                        } else {
                            usuario.adicionarParaLerDepois(noticia);
                            System.out.println("Notícia adicionada à lista para ler depois.");
                        }
                    }
                    default -> System.out.println("Opção inválida!");
                }

                usuarioService.salvarUsuario(usuario);
            } catch (NumberFormatException e) {
                System.out.println("Número inválido!");
            }
        }
    }
} 