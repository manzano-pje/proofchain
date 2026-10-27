# ProofChain --- Checklist de Testes Unitários por Módulo

## Objetivo

Checklist de planejamento e acompanhamento dos testes unitários do
backend ProofChain, organizado por módulo e caso de uso. Os itens devem
ser marcados somente após a implementação e a execução do teste
correspondente.

**Referencial de qualidade:** família ISO/IEC 25000 (SQuaRE), com foco
nas características de qualidade relevantes da ISO/IEC 25010. Este
checklist não representa certificação nem declaração de conformidade
integral com as normas.

## Como utilizar

-   `[ ]` Pendente
-   `[x]` Executado e aprovado, com evidência registrada
-   `[!]` Executado e reprovado; registrar defeito
-   `[-]` Não aplicável, com justificativa
-   `[B]` Bloqueado por funcionalidade ainda não implementada

Não marcar um item como aprovado apenas porque o código existe ou porque
a aplicação inicia. Registrar o resultado real do teste automatizado.

## Critérios gerais para cada teste unitário

-   [ ] Nome descritivo e identificador único conforme a convenção de
    testes do projeto.
-   [ ] Estrutura Given / When / Then.
-   [ ] Cenário de sucesso coberto quando o comportamento está
    implementado.
-   [ ] Cenários de falha e exceções relevantes cobertos.
-   [ ] Limites e entradas inválidas cobertos quando aplicáveis.
-   [ ] Dependências externas isoladas por mocks apropriados.
-   [ ] Resultado, exceção e efeitos colaterais verificados.
-   [ ] Chamadas indevidas verificadas com `never()` quando aplicável.
-   [ ] Dados salvos verificados com `ArgumentCaptor` quando necessário.
-   [ ] Contexto multi-tenant validado quando o caso de uso opera sobre
    dados de uma instituição.
-   [ ] Teste independente, determinístico e sem dependência de dados
    aleatórios.
-   [ ] Teste compilado e executado; resultado registrado.

------------------------------------------------------------------------

## 1. Usuários (`User`)

### `CreateUserHandler`

-   [ ] Criar usuário com dados válidos.
-   [ ] Rejeitar instituição ausente no contexto de segurança.
-   [ ] Rejeitar instituição inexistente ou inválida.
-   [ ] Rejeitar e-mail duplicado conforme a regra implementada.
-   [ ] Codificar a senha antes de persistir.
-   [ ] Associar o usuário à instituição correta.
-   [ ] Preservar os dados válidos recebidos no command.
-   [ ] Verificar que não ocorre persistência quando uma validação
    falha.

### `UpdateUserHandler`

-   [ ] Atualizar usuário existente com dados válidos.
-   [ ] Rejeitar usuário inexistente.
-   [ ] Rejeitar tentativa de atualizar usuário de outra instituição.
-   [ ] Validar os campos que podem ou não ser alterados.
-   [ ] Verificar o retorno ou estado atualizado conforme o contrato do
    handler.
-   [ ] Verificar que falhas de validação não persistem alterações.

### `DeleteUserHandler`

-   [ ] Executar a exclusão conforme o comportamento real do handler.
-   [ ] Rejeitar usuário inexistente.
-   [ ] Rejeitar contexto de tenant inválido.
-   [ ] Impedir exclusão de usuário pertencente a outra instituição.
-   [ ] Verificar exclusão lógica ou física conforme a implementação e o
    requisito aprovado.
-   [ ] Verificar repetição da operação para usuário já desativado ou
    excluído.
-   [ ] Verificar preservação dos dados que não devem ser alterados.

### `ListAllUserHandler`

-   [ ] Retornar usuários da instituição autenticada.
-   [ ] Retornar lista vazia quando não houver usuários.
-   [ ] Não retornar usuários de outra instituição.
-   [ ] Converter os registros para o DTO esperado.
-   [ ] Tratar instituição ou contexto inválido conforme o contrato.

### `ListOneUserHandler`

-   [ ] Retornar usuário existente pertencente à instituição
    autenticada.
-   [ ] Rejeitar usuário inexistente.
-   [ ] Impedir consulta de usuário de outra instituição.
-   [ ] Validar os campos retornados no DTO.
-   [ ] Verificar comportamento para usuário inativo, se aplicável.

------------------------------------------------------------------------

## 2. Instituições (`Institution`)

### `CreateInstitutionHandler`

-   [ ] Criar instituição com dados válidos.
-   [ ] Rejeitar CNPJ nulo ou inválido conforme as regras implementadas.
-   [ ] Rejeitar nome nulo ou inválido conforme as regras implementadas.
-   [ ] Rejeitar e-mail obrigatório ausente ou inválido.
-   [ ] Tratar CNPJ duplicado.
-   [ ] Tratar reativação de instituição excluída, se prevista.
-   [ ] Tratar e-mail duplicado do usuário associado.
-   [ ] Tratar plano inexistente, quando o cadastro exigir plano.
-   [ ] Aplicar papel/perfil correto conforme a regra de negócio.
-   [ ] Codificar a senha do usuário associado.
-   [ ] Criar os vínculos entre instituição, usuário e assinatura
    conforme o fluxo implementado.
-   [ ] Verificar que falhas de validação não deixam registros parciais.

### `UpdateInstitutionHandler`

-   [ ] Atualizar instituição autorizada com dados válidos.
-   [ ] Rejeitar instituição inexistente.
-   [ ] Impedir alteração de instituição fora do escopo autorizado.
-   [ ] Preservar campos não enviados ou não alteráveis conforme o
    contrato.
-   [ ] Verificar tratamento de imagem/logo quando houver upload.
-   [ ] Tratar falha no serviço de armazenamento.
-   [ ] Verificar consistência do estado da instituição após falha.
-   [ ] Confirmar que a dependência de upload é corretamente injetada e
    disponível.

### `DeleteInstitutionHandler`

-   [ ] Excluir ou desativar instituição conforme o comportamento
    definido.
-   [ ] Rejeitar instituição inexistente.
-   [ ] Impedir operação sobre instituição fora do escopo autorizado.
-   [ ] Verificar preenchimento de `deletedAt` e/ou mudança de estado,
    se aplicável.
-   [ ] Verificar comportamento ao repetir a exclusão.
-   [ ] Verificar efeitos sobre recursos associados conforme as regras
    do sistema.

### Consultas de instituição

-   [ ] Listar instituições conforme os filtros e permissões
    implementados.
-   [ ] Tratar lista vazia.
-   [ ] Consultar instituição existente.
-   [ ] Rejeitar instituição inexistente.
-   [ ] Validar conversão para DTO e tratamento de registros excluídos.

------------------------------------------------------------------------

## 3. Cursos (`Course`)

### `CreateCourseHandler`

-   [ ] Criar curso com dados válidos.
-   [ ] Rejeitar nome ou descrição vazios, quando validados pelo
    domínio.
-   [ ] Rejeitar carga horária menor ou igual a zero.
-   [ ] Rejeitar instituição ausente ou inválida.
-   [ ] Tratar nome duplicado dentro da mesma instituição.
-   [ ] Permitir o mesmo nome em instituições diferentes, se essa for a
    regra de negócio.
-   [ ] Verificar uso correto do método de criação de domínio
    (`Course.create()`).
-   [ ] Verificar que falhas não persistem o curso.

### `UpdateCourseHandler`

-   [ ] Atualizar curso existente com dados válidos.
-   [ ] Rejeitar curso inexistente.
-   [ ] Impedir atualização de curso pertencente a outra instituição.
-   [ ] Tratar duplicidade de nome dentro da instituição.
-   [ ] Permitir manter o próprio nome sem falso conflito.
-   [ ] Validar campos obrigatórios e carga horária.
-   [ ] Verificar persistência e retorno dos dados atualizados.

### `DeleteCourseHandler`

-   [ ] Executar exclusão conforme a regra definida.
-   [ ] Rejeitar curso inexistente.
-   [ ] Impedir exclusão de curso de outra instituição.
-   [ ] Verificar exclusão lógica, `deletedAt` e estado ativo, se
    aplicáveis.
-   [ ] Verificar que o curso não é removido fisicamente quando a regra
    exigir exclusão lógica.
-   [ ] Verificar repetição da operação.

### `ListAllCourseHandler`

-   [ ] Retornar cursos da instituição autenticada.
-   [ ] Retornar lista vazia quando não houver cursos.
-   [ ] Não incluir cursos de outra instituição.
-   [ ] Respeitar exclusão lógica.
-   [ ] Validar conversão para o DTO de resposta.
-   [ ] Verificar ordenação apenas se estiver definida como parte do
    contrato.

### `ListOneCourseHandler`

-   [ ] Retornar curso existente no tenant atual.
-   [ ] Rejeitar curso inexistente.
-   [ ] Impedir consulta de curso de outra instituição.
-   [ ] Validar campos do DTO retornado.

------------------------------------------------------------------------

## 4. Participantes (`Participant`)

### `CreateParticipantHandler`

-   [ ] Criar participante com dados válidos.
-   [ ] Rejeitar instituição inexistente.
-   [ ] Tratar CPF duplicado na mesma instituição.
-   [ ] Definir explicitamente a regra para CPF igual em instituições
    diferentes.
-   [ ] Validar campos obrigatórios conforme o contrato.
-   [ ] Associar participante à instituição correta.
-   [ ] Verificar que falhas não persistem o participante.

### `UpdateParticipantHandler`

-   [ ] Atualizar participante existente com dados válidos.
-   [ ] Rejeitar participante inexistente.
-   [ ] Impedir atualização de participante de outra instituição.
-   [ ] Permitir manter o próprio CPF sem falso conflito.
-   [ ] Rejeitar CPF já utilizado por outro participante da mesma
    instituição.
-   [ ] Validar a lógica de duplicidade: CPF já cadastrado não deve ser
    confundido com CPF inexistente.
-   [ ] Verificar quais campos podem ser alterados, inclusive o estado
    ativo.
-   [ ] Verificar que falhas não persistem alterações.

### `DeleteParticipantHandler`

-   [ ] Executar exclusão lógica conforme a regra definida.
-   [ ] Rejeitar participante inexistente.
-   [ ] Impedir exclusão de participante de outra instituição.
-   [ ] Verificar `deletedAt` e desativação.
-   [ ] Preservar dados que não devem ser alterados.
-   [ ] Verificar repetição da operação.

### Consultas de participantes

-   [ ] Listar participantes no tenant correto.
-   [ ] Respeitar paginação e tamanho de página, quando implementados.
-   [ ] Não listar participantes excluídos logicamente.
-   [ ] Retornar lista vazia quando aplicável.
-   [ ] Consultar participante existente.
-   [ ] Rejeitar participante inexistente ou de outra instituição.
-   [ ] Validar mapeamento para DTO de resumo e DTO completo, conforme o
    caso de uso.

------------------------------------------------------------------------

## 5. Instrutores (`Instructor`)

### `CreateInstructorHandler`

-   [ ] Criar instrutor com dados válidos.
-   [ ] Rejeitar usuário associado inexistente.
-   [ ] Rejeitar instituição inexistente ou inválida.
-   [ ] Tratar instrutor duplicado conforme a regra implementada.
-   [ ] Garantir que o usuário associado pertence ao tenant correto.
-   [ ] Verificar associação correta com usuário e instituição.
-   [ ] Verificar estado inicial do instrutor.
-   [ ] Verificar que falhas não persistem o instrutor.

### `UpdateInstructorHandler`

-   [ ] Atualizar instrutor existente.
-   [ ] Rejeitar instrutor inexistente.
-   [ ] Impedir atualização de instrutor de outra instituição.
-   [ ] Validar os campos alteráveis.
-   [ ] Verificar tratamento de instrutor inativo.
-   [ ] Verificar que a atualização persiste somente as alterações
    esperadas.

### `DeleteInstructorHandler`

-   [ ] Rejeitar instrutor inexistente.
-   [ ] Rejeitar instrutor inativo, se essa for a regra.
-   [ ] Impedir exclusão de instrutor de outra instituição.
-   [ ] Confirmar se a regra exige exclusão lógica ou física.
-   [ ] Verificar a chamada ao repositório e seus argumentos.
-   [ ] Verificar comportamento ao repetir a operação.

### Consultas de instrutores

-   [ ] Consultar instrutor existente.
-   [ ] Rejeitar instrutor inexistente.
-   [ ] Listar instrutores da instituição correta.
-   [ ] Tratar lista vazia.
-   [ ] Respeitar estado ativo/inativo conforme o contrato.
-   [ ] Validar DTOs de resposta.

------------------------------------------------------------------------

## 6. Turmas (`CourseClass`)

### `CreateCourseClassHandler`

-   [ ] Criar turma com dados válidos.
-   [ ] Rejeitar instituição inexistente ou inválida.
-   [ ] Rejeitar curso inexistente.
-   [ ] Impedir associação a curso de outra instituição.
-   [ ] Validar usuário ou instrutor responsável conforme o contrato
    real.
-   [ ] Impedir associação a usuário/instrutor de outra instituição.
-   [ ] Tratar duplicidade conforme a regra implementada.
-   [ ] Verificar vínculos corretos entre turma, curso e instituição.
-   [ ] Verificar que falhas não persistem turma parcialmente válida.

### `ListAllCourseClassHandler`

-   [ ] Listar turmas da instituição autenticada.
-   [ ] Retornar lista vazia quando não houver turmas.
-   [ ] Não retornar turmas de outra instituição.
-   [ ] Validar conversão para o DTO de resposta.
-   [ ] Verificar paginação ou ordenação somente se fizerem parte do
    contrato.

### Entidade `CourseClass`

-   [ ] Validar regras de domínio efetivamente implementadas.
-   [ ] Validar consistência dos vínculos com curso, instituição,
    responsável e participantes.
-   [ ] Validar estados e datas, se existirem regras próprias.

**Lacuna de escopo:** não criar testes de atualização, exclusão ou
consulta individual enquanto esses casos de uso não estiverem
implementados e especificados.

------------------------------------------------------------------------

## 7. Planos (`Plans`)

### `CreatePlansHandler`

-   [ ] Criar plano com dados válidos.
-   [ ] Validar nome obrigatório.
-   [ ] Tratar duplicidade de nome segundo a regra definida (global ou
    por instituição).
-   [ ] Validar preço, duração e limites conforme as regras
    implementadas.
-   [ ] Validar tipo de cobrança.
-   [ ] Validar estado inicial do plano.
-   [ ] Verificar persistência dos dados corretos.
-   [ ] Verificar que falhas não persistem o plano.

### Consultas de planos

-   [ ] Consultar plano existente.
-   [ ] Rejeitar plano inexistente.
-   [ ] Listar planos.
-   [ ] Tratar lista vazia.
-   [ ] Respeitar estado ativo/inativo e escopo de acesso conforme o
    contrato.

### Atualização e exclusão

-   [ ] \[B\] Testes de atualização --- confirmar implementação do
    handler e contrato antes de escrever.
-   [ ] \[B\] Testes de exclusão --- confirmar implementação do handler
    e regra de exclusão antes de escrever.

------------------------------------------------------------------------

## 8. Funcionalidades de planos (`FeaturePlan`)

### `CreateFeatureHandler`

-   [ ] Criar funcionalidade com dados válidos.
-   [ ] Rejeitar plano inexistente.
-   [ ] Tratar duplicidade dentro do plano correto.
-   [ ] Verificar que a duplicidade considera os campos definidos pela
    regra de negócio.
-   [ ] Validar nome e quantidade/limite, quando aplicáveis.
-   [ ] Verificar associação correta ao plano.
-   [ ] Verificar validação de tenant antes da persistência.
-   [ ] Verificar que falhas não persistem a funcionalidade.

### Atualização e exclusão

-   [ ] \[B\] Testes de atualização --- handler marcado como pendente no
    código consultado; especificar após implementação.
-   [ ] Testar exclusão conforme o comportamento real do
    `DeleteFeatureHandler`.
-   [ ] Confirmar se a exclusão deve ser física ou lógica.
-   [ ] Verificar tratamento de funcionalidade inexistente.
-   [ ] Verificar que não é possível alterar/excluir funcionalidade fora
    do escopo autorizado.

------------------------------------------------------------------------

## 9. Assinaturas (`Subscription`)

-   [ ] \[B\] Criar assinatura com plano válido após implementação do
    caso de uso.
-   [ ] \[B\] Rejeitar plano inexistente ou inativo.
-   [ ] \[B\] Validar datas de início, vencimento e cancelamento.
-   [ ] \[B\] Validar transições de estado permitidas.
-   [ ] \[B\] Validar tipo de cobrança.
-   [ ] \[B\] Validar cancelamento e repetição da operação.
-   [ ] \[B\] Validar vínculo com a instituição correta.
-   [ ] \[B\] Verificar comportamento diante de falha na persistência.

**Nota:** os handlers de assinatura consultados estavam pendentes. Os
itens acima são requisitos de planejamento e não evidência de
comportamento já implementado.

------------------------------------------------------------------------

## 10. Autenticação e autorização

### `AuthService`

-   [ ] Autenticar credenciais válidas.
-   [ ] Rejeitar senha incorreta.
-   [ ] Rejeitar usuário inexistente.
-   [ ] Tratar usuário inativo ou bloqueado conforme a regra.
-   [ ] Retornar access token e refresh token conforme o contrato.
-   [ ] Retornar identidade, papel, permissões e tenant corretos.
-   [ ] Não expor senha ou hash de senha na resposta.
-   [ ] Tratar falhas do serviço de autenticação sem criar sessão válida
    indevidamente.

### `JwtService`

-   [ ] Gerar token com assinatura e claims esperadas.
-   [ ] Extrair corretamente usuário, instituição e tipo de token.
-   [ ] Rejeitar token expirado.
-   [ ] Rejeitar token malformado.
-   [ ] Rejeitar assinatura inválida.
-   [ ] Rejeitar token de tipo incorreto.
-   [ ] Validar tempo de expiração.
-   [ ] Diferenciar access token de refresh token.

### Refresh token

-   [ ] Aceitar refresh token válido.
-   [ ] Rejeitar access token apresentado como refresh token.
-   [ ] Rejeitar refresh token expirado.
-   [ ] Rejeitar token com assinatura inválida.
-   [ ] Rejeitar usuário inexistente, removido ou inativo.
-   [ ] Garantir que usuário e tenant do novo token correspondem à
    identidade validada.
-   [ ] Não estabelecer autenticação parcial em caso de falha.

### `JwtAuthenticationFilter`, `TenantContext` e `SecurityUtils`

-   [ ] Tratar ausência de header `Authorization`.
-   [ ] Tratar header sem prefixo `Bearer`.
-   [ ] Rejeitar token inválido ou expirado.
-   [ ] Autenticar corretamente token válido.
-   [ ] Definir o contexto de segurança e o tenant esperados.
-   [ ] Limpar o contexto ao terminar a execução.
-   [ ] Não reutilizar tenant de uma requisição anterior.
-   [ ] Tratar contexto ausente ou identificador de instituição
    inválido.

### `TenantValidation`

-   [ ] Aceitar instituição válida.
-   [ ] Rejeitar instituição inexistente.
-   [ ] Rejeitar instituição removida logicamente.
-   [ ] Impedir execução de operações dependentes após falha de
    validação.

**Nota:** regras de rotas HTTP e cadeia completa de filtros precisam
também de testes de integração; testes unitários isolados não comprovam
a configuração de segurança do Spring.

------------------------------------------------------------------------

## 11. Certificados (`Certificate`)

**Lacuna de escopo:** a entidade foi localizada, mas os casos de uso
completos de emissão, revogação e validação pública não foram
confirmados na inspeção. Criar os testes funcionais após localizar ou
implementar os handlers correspondentes.

-   [ ] \[B\] Validar geração e unicidade de hash.
-   [ ] \[B\] Validar associação com participante, curso, turma e
    instituição.
-   [ ] \[B\] Rejeitar emissão duplicada conforme a regra de negócio.
-   [ ] \[B\] Validar datas de emissão e conclusão.
-   [ ] \[B\] Validar revogação e expiração.
-   [ ] \[B\] Rejeitar identificador/hash inexistente ou inválido.
-   [ ] \[B\] Validar integridade dos dados usados na validação pública.
-   [ ] \[B\] Impedir associações incompatíveis entre instituições.
-   [ ] \[B\] Testar separadamente a integração real com blockchain ou
    armazenamento, caso implementada.

------------------------------------------------------------------------

## 12. Armazenamento de arquivos e imagens

-   [ ] Validar formato e tamanho do arquivo conforme o contrato.
-   [ ] Construir a chave/caminho de armazenamento corretamente.
-   [ ] Tratar falha no upload.
-   [ ] Tratar falha no download.
-   [ ] Tratar arquivo inexistente.
-   [ ] Evitar persistir URL ou metadados incorretos quando o upload
    falhar.
-   [ ] Verificar comportamento ao substituir arquivo existente.
-   [ ] Garantir que a falha na remoção de arquivo antigo não corrompa
    os dados da instituição.
-   [ ] Não utilizar credenciais reais nos testes.
-   [ ] Manter testes unitários isolados do serviço real; validar a
    integração em suíte separada.

------------------------------------------------------------------------

## 13. Checklist transversal de qualidade

### Regras de negócio

-   [ ] Casos de sucesso e falha cobertos por caso de uso implementado.
-   [ ] Limites e entradas inválidas cobertos conforme os contratos.
-   [ ] Exceções esperadas verificadas.
-   [ ] Não ocorre persistência quando uma validação anterior falha.
-   [ ] Campos não relacionados à operação permanecem inalterados.

### Multi-tenancy e autorização

-   [ ] Cada handler com dados de instituição verifica o tenant
    esperado.
-   [ ] Tentativa de acessar recurso de outra instituição é rejeitada.
-   [ ] Repositórios são chamados com os identificadores corretos.
-   [ ] Nenhuma alteração/exclusão indevida ocorre após falha de
    autorização.
-   [ ] Contexto de segurança é limpo corretamente.

### Qualidade dos testes

-   [ ] Testes usam dados determinísticos.
-   [ ] Mocks representam apenas dependências externas ao comportamento
    testado.
-   [ ] Não há testes redundantes de getters/setters sem lógica.
-   [ ] Não há testes que apenas verificam que o método não lançou
    exceção sem validar o resultado.
-   [ ] Nomes dos testes descrevem comportamento esperado.
-   [ ] Identificadores de cenários são únicos e sequenciais conforme o
    padrão adotado.
-   [ ] Cobertura de linhas e branches é medida e analisada.
-   [ ] A suíte completa é executada após alterações relevantes.
-   [ ] Falhas são registradas com módulo, classe, cenário e causa.
-   [ ] Itens bloqueados por falta de implementação não são
    contabilizados como aprovados.

## 14. Checklist final de validação da suíte

-   [ ] Todos os testes previstos para funcionalidades implementadas
    foram escritos.
-   [ ] Todos os testes compilam sem erros.
-   [ ] Todos os testes foram executados no Maven.
-   [ ] Nenhum teste falha.
-   [ ] Nenhum teste é ignorado sem justificativa registrada.
-   [ ] Padrão Given/When/Then aplicado de forma consistente.
-   [ ] Mocks apropriados para repositórios e serviços externos.
-   [ ] Casos de erro e regras multi-tenant cobertos.
-   [ ] Sem testes de framework ou banco real na suíte unitária.
-   [ ] Documentação inline clara e nomes descritivos.
-   [ ] Relatório de cobertura gerado, se a configuração estiver
    disponível.
-   [ ] Defeitos encontrados foram registrados e vinculados aos testes
    de regressão.
-   [ ] Resultados e limitações foram documentados.

## Observações finais

1.  Marcar `[x]` somente depois da execução bem-sucedida e do registro
    de evidência.
2.  Itens `[B]` indicam funcionalidade pendente ou contrato ainda não
    confirmado; não devem ser tratados como testes reprovados nem
    aprovados.
3.  A ISO/IEC 25000 orienta a abordagem de qualidade; a lista concreta
    de cenários deriva do comportamento do ProofChain e dos requisitos
    de negócio.
4.  Este checklist é um instrumento de planejamento e acompanhamento,
    não um laudo de conformidade ISO.
