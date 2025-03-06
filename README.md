# Sistema de Gerenciamento de Estoque 📦

## Descrição 📝
O Sistema de Gerenciamento de Estoque é uma aplicação Java desenvolvida para auxiliar empresas no controle de produtos, registrando entradas, saídas e fornecendo relatórios detalhados sobre o inventário.

## Funcionalidades ✨

### Gerenciamento de Produtos 🏷️
- Cadastro de produtos com informações como nome, preço, quantidade e categoria
- Listagem de todos os produtos cadastrados
- Pesquisa de produtos por ID, nome ou categoria
- Visualização detalhada de cada produto, incluindo histórico de movimentações

### Controle de Estoque 🔄
- Registro de entrada de produtos no estoque ⬆️
- Registro de saída de produtos (vendas, descartes, transferências) ⬇️
- Verificação automática de disponibilidade de estoque antes de registrar saídas ⚠️

### Relatórios 📊
- Relatório de estoque atual, com valor total e quantidade de itens 📈
- Relatório de movimentações, com filtros por produto e tipo (entrada/saída) 🔍
- Relatório de vendas, com totalizadores e detalhamento por produto 💰

### Persistência de Dados 💾
- Salvamento automático dos dados em arquivos (.dat)
- Carregamento dos dados ao iniciar a aplicação

## Estrutura do Projeto 🏗️

### Classes Principais 📚
- `SistemaEstoque`: Classe principal com o menu e fluxo de interação
- `Produto`: Classe que representa um produto no estoque
- `Movimento`: Classe que representa uma movimentação (entrada/saída) de produto

## Como Executar ▶️
1. Compile todas as classes do projeto:
   ```
   javac SistemaEstoque.java Produto.java Movimento.java
   ```
2. Execute a classe principal:
   ```
   java SistemaEstoque
   ```

## Requisitos 🔧
- Java 8 ou superior (necessário para suporte ao LocalDateTime) ☕
- Aproximadamente 2MB de espaço em disco para a aplicação e arquivos de dados 💿

## Arquivos Gerados 📁
- `produtos.dat`: Armazena os dados de todos os produtos cadastrados
- `movimentos.dat`: Armazena o histórico de todas as movimentações

## Fluxo de Uso Típico 🔄
1. Cadastrar produtos no sistema ➕
2. Registrar entradas iniciais de estoque 📥
3. Registrar saídas conforme as vendas ou outras operações 📤
4. Consultar relatórios para análise e tomada de decisões 📋

## Considerações de Segurança 🔒
- Os arquivos de dados não são criptografados
- Recomenda-se fazer backup regular dos arquivos .dat 🔄

## Limitações Conhecidas ⚠️
- Interface baseada em console
- Não possui autenticação de usuários
- Não implementa níveis de acesso/permissões

## Melhorias Futuras 🚀
- Interface gráfica 🖥️
- Autenticação de usuários 🔐
- Backup automático dos dados 💾
- Exportação de relatórios em diferentes formatos 📑
- Integração com sistemas de venda/ERP 🔄

