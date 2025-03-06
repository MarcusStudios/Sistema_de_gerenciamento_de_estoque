import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.*;



public class SistemaEstoque {
    private static ArrayList<Produto> produtos = new ArrayList<>();
    private static ArrayList<Movimento> movimentos = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String ARQUIVO_PRODUTOS = "produtos.dat";
    private static final String ARQUIVO_MOVIMENTOS = "movimentos.dat";

    public static void main(String[] args) {
        carregarDados();

        boolean executando = true;
        while (executando) {
            exibirMenu();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    cadastrarProduto();
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    pesquisarProduto();
                    break;
                case 4:
                    registrarEntrada();
                    break;
                case 5:
                    registrarSaida();
                    break;
                case 6:
                    gerarRelatorioEstoque();
                    break;
                case 7:
                    gerarRelatorioMovimentos();
                    break;
                case 8:
                    gerarRelatorioVendas();
                    break;
                case 9:
                    executando = false;
                    System.out.println("Sistema encerrado. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }

        salvarDados();
    }

    private static void exibirMenu() {
        System.out.println("\n=====SISTEMA DE GERENCIAMENTO=====");
        System.out.println("1 - Cadastrar produto");
        System.out.println("2 - Listar produtos");
        System.out.println("3 - Pesquisar produto");
        System.out.println("4 - Registrar entrada de produtos");
        System.out.println("5 - Registrar saída de produtos");
        System.out.println("6 - Relatório de estoque atual");
        System.out.println("7 - Relatório de movimentações");
        System.out.println("8 - Relatório de vendas");
        System.out.println("9 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void cadastrarProduto() {
        System.out.println("\n----- CADASTRO DE PRODUTO -----");

        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();

        double preco = 0;
        boolean precoValido = false;
        while (!precoValido) {
            System.out.print("Preço unitário: R$ ");
            try {
                preco = Double.parseDouble(scanner.nextLine());
                precoValido = true;
            } catch (NumberFormatException e) {
                System.out.println("Preço inválido. Use formato numérico (ex: 10.50)");
            }
        }

        int quantidade = 0;
        boolean qtdValida = false;
        while (!qtdValida) {
            System.out.print("Quantidade inicial: ");
            try {
                quantidade = Integer.parseInt(scanner.nextLine());
                qtdValida = true;
            } catch (NumberFormatException e) {
                System.out.println("Quantidade inválida. Digite um número inteiro.");
            }
        }

        System.out.print("Categoria do produto: ");
        String categoria = scanner.nextLine();

        // Encontrar próximo ID disponível
        int proximoId = 1;
        for (Produto p : produtos) {
            if (p.getId() >= proximoId) {
                proximoId = p.getId() + 1;
            }
        }

        Produto novoProduto = new Produto(proximoId, nome, preco, quantidade, categoria);
        produtos.add(novoProduto);

        // Registrar movimento de estoque inicial se quantidade > 0
        if (quantidade > 0) {
            Movimento mov = new Movimento(proximoId, "ENTRADA", quantidade,
                    "Estoque inicial", LocalDateTime.now());
            movimentos.add(mov);
        }

        System.out.println("Produto cadastrado com sucesso! ID: " + proximoId);
    }

    private static void listarProdutos() {
        System.out.println("\n----- LISTA DE PRODUTOS -----");

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        System.out.println("ID\tNome\t\tCategoria\tPreço\tQuantidade");
        System.out.println("----------------------------------------------------------");

        for (Produto p : produtos) {
            System.out.printf("%d\t%-15s\t%-10s\tR$%.2f\t%d\n",
                    p.getId(), limitarTexto(p.getNome(), 15),
                    limitarTexto(p.getCategoria(), 10),
                    p.getPreco(), p.getQuantidade());
        }
    }

    private static String limitarTexto(String texto, int tamanhoMax) {
        if (texto.length() <= tamanhoMax) {
            return texto;
        }
        return texto.substring(0, tamanhoMax - 3) + "...";
    }

    private static void pesquisarProduto() {
        System.out.println("\n----- PESQUISA DE PRODUTO -----");
        System.out.println("1 - Pesquisar por ID");
        System.out.println("2 - Pesquisar por nome");
        System.out.println("3 - Pesquisar por categoria");
        System.out.print("Escolha uma opção: ");

        int opcao = lerOpcao();
        boolean encontrou = false;

        switch (opcao) {
            case 1:
                System.out.print("Digite o ID do produto: ");
                try {
                    int id = Integer.parseInt(scanner.nextLine());
                    for (Produto p : produtos) {
                        if (p.getId() == id) {
                            exibirDetalhesProduto(p);
                            encontrou = true;
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido.");
                }
                break;

            case 2:
                System.out.print("Digite o nome do produto: ");
                String nome = scanner.nextLine().toLowerCase();
                for (Produto p : produtos) {
                    if (p.getNome().toLowerCase().contains(nome)) {
                        exibirDetalhesProduto(p);
                        encontrou = true;
                    }
                }
                break;

            case 3:
                System.out.print("Digite a categoria: ");
                String categoria = scanner.nextLine().toLowerCase();
                for (Produto p : produtos) {
                    if (p.getCategoria().toLowerCase().contains(categoria)) {
                        exibirDetalhesProduto(p);
                        encontrou = true;
                    }
                }
                break;

            default:
                System.out.println("Opção inválida!");
                return;
        }

        if (!encontrou) {
            System.out.println("Nenhum produto encontrado com os critérios informados.");
        }
    }

    private static void exibirDetalhesProduto(Produto p) {
        System.out.println("\n--- Detalhes do Produto ---");
        System.out.println("ID: " + p.getId());
        System.out.println("Nome: " + p.getNome());
        System.out.println("Categoria: " + p.getCategoria());
        System.out.println("Preço: R$ " + String.format("%.2f", p.getPreco()));
        System.out.println("Quantidade em estoque: " + p.getQuantidade());
        System.out.println("Valor total em estoque: R$ " +
                String.format("%.2f", p.getPreco() * p.getQuantidade()));

        // Mostrar as últimas 5 movimentações deste produto
        List<Movimento> movsProduto = new ArrayList<>();
        for (Movimento m : movimentos) {
            if (m.getProdutoId() == p.getId()) {
                movsProduto.add(m);
            }
        }

        if (!movsProduto.isEmpty()) {
            System.out.println("\nÚltimas movimentações:");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Ordenar do mais recente para o mais antigo e limitar a 5
            movsProduto.sort((m1, m2) -> m2.getDataHora().compareTo(m1.getDataHora()));
            int limite = Math.min(5, movsProduto.size());

            for (int i = 0; i < limite; i++) {
                Movimento m = movsProduto.get(i);
                System.out.printf("%s - %s: %d unidades - %s\n",
                        m.getDataHora().format(dtf), m.getTipo(),
                        m.getQuantidade(), m.getObservacao());
            }
        }

        System.out.println("------------------------");
    }

    private static void registrarEntrada() {
        System.out.println("\n----- REGISTRAR ENTRADA DE PRODUTOS -----");
        Produto produto = buscarProdutoPorId();

        if (produto == null) {
            return;
        }

        int quantidade = 0;
        boolean qtdValida = false;
        while (!qtdValida) {
            System.out.print("Quantidade a ser adicionada: ");
            try {
                quantidade = Integer.parseInt(scanner.nextLine());
                if (quantidade <= 0) {
                    System.out.println("A quantidade deve ser maior que zero.");
                } else {
                    qtdValida = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Quantidade inválida. Digite um número inteiro.");
            }
        }

        System.out.print("Observação (opcional): ");
        String observacao = scanner.nextLine();
        if (observacao.trim().isEmpty()) {
            observacao = "Entrada de estoque";
        }

        // Atualiza estoque do produto
        produto.setQuantidade(produto.getQuantidade() + quantidade);

        // Registra movimentação
        Movimento mov = new Movimento(produto.getId(), "ENTRADA", quantidade,
                observacao, LocalDateTime.now());
        movimentos.add(mov);

        System.out.println("Entrada registrada com sucesso! Novo estoque: " + produto.getQuantidade());
    }

    private static void registrarSaida() {
        System.out.println("\n----- REGISTRAR SAÍDA DE PRODUTOS -----");
        Produto produto = buscarProdutoPorId();

        if (produto == null) {
            return;
        }

        int quantidade = 0;
        boolean qtdValida = false;
        while (!qtdValida) {
            System.out.print("Quantidade a ser retirada: ");
            try {
                quantidade = Integer.parseInt(scanner.nextLine());
                if (quantidade <= 0) {
                    System.out.println("A quantidade deve ser maior que zero.");
                } else if (quantidade > produto.getQuantidade()) {
                    System.out.println("Quantidade insuficiente em estoque. Disponível: " +
                            produto.getQuantidade());
                } else {
                    qtdValida = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Quantidade inválida. Digite um número inteiro.");
            }
        }

        double valorTotal = quantidade * produto.getPreco();
        System.out.println("Valor total da saída: R$ " + String.format("%.2f", valorTotal));

        System.out.print("Tipo (1-Venda, 2-Descarte, 3-Transferência): ");
        int tipo = lerOpcao();
        String tipoMovimento;
        switch (tipo) {
            case 1:
                tipoMovimento = "VENDA";
                break;
            case 2:
                tipoMovimento = "DESCARTE";
                break;
            case 3:
                tipoMovimento = "TRANSFERÊNCIA";
                break;
            default:
                tipoMovimento = "SAÍDA";
        }

        System.out.print("Observação (opcional): ");
        String observacao = scanner.nextLine();

        // Atualiza estoque do produto
        produto.setQuantidade(produto.getQuantidade() - quantidade);

        // Registra movimentação
        Movimento mov = new Movimento(produto.getId(), tipoMovimento, quantidade,
                observacao, LocalDateTime.now());
        movimentos.add(mov);

        System.out.println("Saída registrada com sucesso! Novo estoque: " + produto.getQuantidade());
    }

    private static Produto buscarProdutoPorId() {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return null;
        }

        System.out.print("Digite o ID do produto (ou 0 para listar todos): ");
        try {
            int id = Integer.parseInt(scanner.nextLine());

            if (id == 0) {
                listarProdutos();
                System.out.print("Digite o ID do produto: ");
                id = Integer.parseInt(scanner.nextLine());
            }

            for (Produto p : produtos) {
                if (p.getId() == id) {
                    System.out.println("Produto: " + p.getNome() +
                            " (Estoque atual: " + p.getQuantidade() + ")");
                    return p;
                }
            }

            System.out.println("Produto não encontrado com o ID informado.");

        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }

        return null;
    }

    private static void gerarRelatorioEstoque() {
        System.out.println("\n----- RELATÓRIO DE ESTOQUE ATUAL -----");

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        double valorTotalEstoque = 0;
        int qtdTotalItens = 0;
        int produtosAbaixoMinimo = 0;

        System.out.println("ID\tNome\t\tCategoria\tPreço\tQuantidade\tValor Total");
        System.out.println("------------------------------------------------------------------------");

        for (Produto p : produtos) {
            double valorTotal = p.getPreco() * p.getQuantidade();
            valorTotalEstoque += valorTotal;
            qtdTotalItens += p.getQuantidade();

            // Considera produtos com menos de 5 unidades como "abaixo do mínimo"
            if (p.getQuantidade() < 5) {
                produtosAbaixoMinimo++;
            }

            System.out.printf("%d\t%-15s\t%-10s\tR$%.2f\t%d\t\tR$%.2f\n",
                    p.getId(), limitarTexto(p.getNome(), 15),
                    limitarTexto(p.getCategoria(), 10),
                    p.getPreco(), p.getQuantidade(), valorTotal);
        }

        System.out.println("------------------------------------------------------------------------");
        System.out.println("Total de produtos: " + produtos.size());
        System.out.println("Total de itens em estoque: " + qtdTotalItens);
        System.out.println("Valor total em estoque: R$ " + String.format("%.2f", valorTotalEstoque));
        System.out.println("Produtos abaixo do estoque mínimo: " + produtosAbaixoMinimo);
    }

    private static void gerarRelatorioMovimentos() {
        System.out.println("\n----- RELATÓRIO DE MOVIMENTAÇÕES -----");

        if (movimentos.isEmpty()) {
            System.out.println("Nenhuma movimentação registrada.");
            return;
        }

        System.out.println("1 - Todas as movimentações");
        System.out.println("2 - Movimentações de um produto específico");
        System.out.println("3 - Movimentações por tipo (entrada/saída)");
        System.out.print("Escolha uma opção: ");

        int opcao = lerOpcao();
        List<Movimento> movimentosFiltrados = new ArrayList<>();

        switch (opcao) {
            case 1:
                movimentosFiltrados.addAll(movimentos);
                break;

            case 2:
                Produto p = buscarProdutoPorId();
                if (p != null) {
                    for (Movimento m : movimentos) {
                        if (m.getProdutoId() == p.getId()) {
                            movimentosFiltrados.add(m);
                        }
                    }
                }
                break;

            case 3:
                System.out.println("1 - Entradas");
                System.out.println("2 - Saídas");
                System.out.print("Escolha uma opção: ");
                int tipoOpcao = lerOpcao();

                String tipoFiltro = null;
                if (tipoOpcao == 1) {
                    tipoFiltro = "ENTRADA";
                } else if (tipoOpcao == 2) {
                    tipoFiltro = "SAÍDA";
                }

                if (tipoFiltro != null) {
                    for (Movimento m : movimentos) {
                        if (m.getTipo().equals(tipoFiltro) ||
                                (!m.getTipo().equals("ENTRADA") && tipoFiltro.equals("SAÍDA"))) {
                            movimentosFiltrados.add(m);
                        }
                    }
                } else {
                    System.out.println("Opção inválida!");
                    return;
                }
                break;

            default:
                System.out.println("Opção inválida!");
                return;
        }

        if (movimentosFiltrados.isEmpty()) {
            System.out.println("Nenhuma movimentação encontrada com os critérios informados.");
            return;
        }

        // Ordenar movimentos por data (do mais recente para o mais antigo)
        movimentosFiltrados.sort((m1, m2) -> m2.getDataHora().compareTo(m1.getDataHora()));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        System.out.println("\nData/Hora\t\tProduto\t\tTipo\t\tQtd\tObservação");
        System.out.println("------------------------------------------------------------------------------");

        for (Movimento m : movimentosFiltrados) {
            String nomeProduto = "Desconhecido";
            for (Produto p : produtos) {
                if (p.getId() == m.getProdutoId()) {
                    nomeProduto = p.getNome();
                    break;
                }
            }

            System.out.printf("%s\t%-15s\t%-10s\t%d\t%s\n",
                    m.getDataHora().format(dtf),
                    limitarTexto(nomeProduto, 15),
                    m.getTipo(), m.getQuantidade(),
                    limitarTexto(m.getObservacao(), 20));
        }
    }

    private static void gerarRelatorioVendas() {
        System.out.println("\n----- RELATÓRIO DE VENDAS -----");

        // Filtrar apenas movimentos de venda
        List<Movimento> vendas = new ArrayList<>();
        for (Movimento m : movimentos) {
            if (m.getTipo().equals("VENDA")) {
                vendas.add(m);
            }
        }

        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda registrada.");
            return;
        }

        // Calcular total de vendas
        int totalItensVendidos = 0;
        double valorTotalVendas = 0;

        for (Movimento v : vendas) {
            totalItensVendidos += v.getQuantidade();

            // Encontrar o produto para obter o preço
            for (Produto p : produtos) {
                if (p.getId() == v.getProdutoId()) {
                    valorTotalVendas += v.getQuantidade() * p.getPreco();
                    break;
                }
            }
        }

        // Mostrar resumo
        System.out.println("Total de vendas: " + vendas.size());
        System.out.println("Total de itens vendidos: " + totalItensVendidos);
        System.out.println("Valor total de vendas: R$ " + String.format("%.2f", valorTotalVendas));

        // Mostrar vendas por produto
        System.out.println("\nVendas por produto:");
        System.out.println("Produto\t\tQuantidade\tValor Total");
        System.out.println("----------------------------------------------");

        // Agrupar vendas por produto
        for (Produto p : produtos) {
            int qtdVendida = 0;

            for (Movimento v : vendas) {
                if (v.getProdutoId() == p.getId()) {
                    qtdVendida += v.getQuantidade();
                }
            }

            if (qtdVendida > 0) {
                double valorTotal = qtdVendida * p.getPreco();
                System.out.printf("%-15s\t%d\t\tR$ %.2f\n",
                        limitarTexto(p.getNome(), 15), qtdVendida, valorTotal);
            }
        }
    }

    private static void carregarDados() {
        // Carregar produtos
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_PRODUTOS))) {
            produtos = (ArrayList<Produto>) ois.readObject();
            System.out.println("Dados de produtos carregados com sucesso!");
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de produtos não encontrado. Será criado ao salvar.");
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados de produtos: " + e.getMessage());
        }

        // Carregar movimentos
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_MOVIMENTOS))) {
            movimentos = (ArrayList<Movimento>) ois.readObject();
            System.out.println("Dados de movimentações carregados com sucesso!");
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de movimentos não encontrado. Será criado ao salvar.");
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados de movimentos: " + e.getMessage());
        }
    }

    private static void salvarDados() {
        // Salvar produtos
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_PRODUTOS))) {
            oos.writeObject(produtos);
            System.out.println("Dados de produtos salvos com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar dados de produtos: " + e.getMessage());
        }

        // Salvar movimentos
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_MOVIMENTOS))) {
            oos.writeObject(movimentos);
            System.out.println("Dados de movimentos salvos com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar dados de movimentos: " + e.getMessage());
        }
    }
}