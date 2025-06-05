# Sistema de Notícias do IBGE

Este é um sistema de consulta de notícias do IBGE que permite aos usuários buscar, favoritar e gerenciar notícias de seu interesse.

## Funcionalidades

- Busca de notícias por:
  - Título
  - Palavras-chave
  - Data
- Gerenciamento de notícias:
  - Favoritar/desfavoritar notícias
  - Marcar notícias como lidas
  - Adicionar notícias para ler depois
- Ordenação de notícias por:
  - Título (ordem alfabética)
  - Data de publicação
  - Tipo/categoria
- Persistência de dados do usuário em formato JSON

## Requisitos

- Java 21
- Maven
- Conexão com a internet para acessar a API do IBGE

## Como executar

1. Clone o repositório:
```bash
git clone https://github.com/pablovns/trabalho-igor.git
cd trabalho-igor
```

2. Compile o projeto:
```bash
mvn clean package
```

3. Execute o programa:
```bash
java -jar target/trabalho-igor-1.0-SNAPSHOT.jar
```

## Estrutura do Projeto

- `domain`: Classes de domínio (Noticia, Usuario)
- `services`: Serviços de negócio (NoticiaService, UsuarioService)
- `utils`: Classes utilitárias (OrdenadorNoticias)
- `ui`: Interface com o usuário (MenuPrincipal)

## Armazenamento de Dados

Os dados do usuário são armazenados no diretório `dados` no formato JSON. O sistema cria automaticamente este diretório quando necessário.

## API do IBGE

O sistema utiliza a [API de Notícias do IBGE v3](https://servicodados.ibge.gov.br/api/docs/noticias?versao=3) para buscar as notícias.

## Autor

Pablo Vinícius
