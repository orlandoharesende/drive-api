Drive API
====================================

 Projeto destinado a realizar integração com outras APIs de armazenamento de arquivo e disponibilizá-los através de uma interface única.

 Nesse momento, realizamos a integração com o Google Drive.

Configuração:

Variáveis de ambiente:

- PATH_ARQUIVO_CREDENCIAIS
  - Caminho do arquivo que contém as credenciais do usuário de sistema utilizado para integração com o google drive.
- TEMPO_EXPIRACAO_URL
  - Tempo em que uma URL temporária para acesso ao arquivo demora para expirar.
  - Medida: Em segundos
  - Valor padrão: 60
- QTD_UTILIZACAO_URL
  - Define quantas vezes uma temporária pode ser acessada.
  - Valor padrão: 2
- SECRET_URL
  - Chave utilizada para a criptografia da URL temporária.
- SECRET_AUTH
  - Chave compartilhada com o PJe utilizada para a geração dos tokens JWT.
