# Testes Unitários - Proofchain

## Resumo

Foram criados **7 testes unitários completos** para os principais handlers da arquitetura modular do Proofchain, utilizando **JUnit 5** e **Mockito**.

## Testes Criados

### 1. **CreateUserHandlerTest**
**Localização:** `src/test/java/com/proofchain/user/applications/handler/`

**Cobertura:**
- ✅ Criação de usuário com sucesso
- ✅ Validação de authorization (institutionId null)
- ✅ Validação de instituição não encontrada
- ✅ Validação de email duplicado
- ✅ Codificação de senha antes de salvar
- ✅ Associação de instituição ao usuário
- ✅ Preservação de dados do command

**Cenários:** 7 testes

---

### 2. **CreateFeatureHandlerTest**
**Localização:** `src/test/java/com/proofchain/admin/featurePlan/aplicattion/handler/`

**Cobertura:**
- ✅ Criação de feature com sucesso
- ✅ Validação de plano não encontrado
- ✅ Validação de feature duplicada
- ✅ Validação de tenant antes de criar
- ✅ Verificação de plano antes de checar duplicidade
- ✅ Preservação de dados do command
- ✅ Verificação de nome e idPlan na duplicidade

**Cenários:** 7 testes

---

### 3. **DeleteUserHandlerTest**
**Localização:** `src/test/java/com/proofchain/user/applications/handler/`

**Cobertura:**
- ✅ Deleção de usuário com sucesso (soft delete)
- ✅ Validação de usuário não encontrado
- ✅ Validação de tenant antes de deletar
- ✅ Soft delete (marca como inactive, não deleta)
- ✅ Isolamento multi-tenant
- ✅ Preservação de dados ao desativar
- ✅ Busca por ID e Institution
- ✅ Tratamento de usuário já desativado

**Cenários:** 8 testes

---

### 4. **CreateCourseHandlerTest**
**Localização:** `src/test/java/com/proofchain/business/course/application/handler/`

**Cobertura:**
- ✅ Criação de curso com sucesso
- ✅ Validação de duplicidade de nome dentro da instituição
- ✅ Validação de tenant antes de criar
- ✅ Recuperação de instituição do contexto
- ✅ Verificação de duplicidade com filtro de instituição
- ✅ Preservação de dados do command
- ✅ Chamada do factory method Course.create()
- ✅ Isolamento multi-tenant

**Cenários:** 8 testes

---

### 5. **CreateInstitutionHandlerTest**
**Localização:** `src/test/java/com/proofchain/admin/institution/application/handler/`

**Cobertura:**
- ✅ Criação de instituição com sucesso
- ✅ Validação de CNPJ (null, comprimento inválido)
- ✅ Validação de nome (null, menos de 5 caracteres)
- ✅ Validação de email (null)
- ✅ Validação de instituição duplicada
- ✅ Reativação de instituição deletada
- ✅ Validação de email duplicado de usuário
- ✅ Validação de plano não encontrado
- ✅ Atribuição de SUPER_ADMIN para CNPJ especial
- ✅ Atribuição de ADMIN para instituição normal
- ✅ Criação de subscription com dados corretos
- ✅ Codificação de senha
- ✅ Preservação de dados da request
- ✅ Criação de usuário com dados da instituição
- ✅ Marcação de instituição como ativa

**Cenários:** 15 testes (mais complexo)

---

### 6. **CreateInstructorHandlerTest**
**Localização:** `src/test/java/com/proofchain/business/instructor/application/handler/`

**Cobertura:**
- ✅ Criação de instrutor com sucesso
- ✅ Validação de usuário não encontrado
- ✅ Validação de instituição não encontrada
- ✅ Validação de instrutor já existe
- ✅ Validação de tenant antes de criar
- ✅ Carregamento de usuário dentro do contexto de tenant
- ✅ Preservação de dados do command
- ✅ Marcação como ativo na criação

**Cenários:** 8 testes

---

### 7. **ListAllCourseHandlerTest**
**Localização:** `src/test/java/com/proofchain/business/course/application/handler/`

**Cobertura:**
- ✅ Listagem de cursos com sucesso
- ✅ Validação de nenhum curso encontrado
- ✅ Validação de tenant antes de listar
- ✅ Filtro por instituição na query
- ✅ Conversão para FullCourseResponse
- ✅ Retorno de múltiplos cursos
- ✅ Retorno de curso único
- ✅ Isolamento multi-tenant
- ✅ Preservação de ordem dos resultados
- ✅ Validação de não-null

**Cenários:** 10 testes

---

### 8. **UpdateCourseHandlerTest**
**Localização:** `src/test/java/com/proofchain/business/course/application/handler/`

**Cobertura:**
- ✅ Atualização de curso com sucesso
- ✅ Validação de curso não encontrado
- ✅ Validação de nome de curso duplicado
- ✅ Permissão de mesmo nome ao atualizar
- ✅ Validação de tenant antes de atualizar
- ✅ Busca de curso dentro do contexto
- ✅ Chamada de método update no curso
- ✅ Retorno de curso atualizado
- ✅ Isolamento multi-tenant
- ✅ Preservação de dados do command

**Cenários:** 10 testes

---

## Total de Testes

**Número total:** 73 testes unitários
**Padrão:** Given/When/Then
**Mocks:** Repositórios, Services, Encoder
**Validações:** Regras de negócio, Exceções, Comportamento

---

## Estrutura dos Testes

Todos os testes seguem o padrão:

```java
@ExtendWith(MockitoExtension.class)
class SomeHandlerTest {

    @Mock
    private Dependency mockDependency;

    @InjectMocks
    private SomeHandler handler;

    @BeforeEach
    void setUp() {
        // Setup dados de teste
    }

    @Test
    void shouldDoSomethingUnderCertainConditions() {
        // given
        // Preparação dos dados e mocks

        // when
        // Execução do método testado

        // then
        // Validação dos resultados e comportamentos
        verify(mockDependency).someMethod();
        assertEquals(expected, actual);
    }
}
```

---

## Como Rodar os Testes

### Rodar todos os testes
```bash
./mvnw.cmd test
```

### Rodar testes de um handler específico
```bash
./mvnw.cmd test -Dtest=CreateUserHandlerTest
```

### Rodar testes com cobertura
```bash
./mvnw.cmd test jacoco:report
```

### Rodar testes de um pacote específico
```bash
./mvnw.cmd test -Dtest=com.proofchain.user.applications.handler.*
```

---

## Dependências Utilizadas

Os testes utilizam as seguintes dependências (já incluídas no pom.xml):

- **JUnit 5** (junit-jupiter)
- **Mockito** (mockito-core, mockito-junit-jupiter)
- **AssertJ** (opcional, para assertions mais legíveis)

---

## Boas Práticas Implementadas

### ✅ Naming Descritivo
Nomes de testes descrevem o comportamento esperado:
- `shouldCreateUserSuccessfully()`
- `shouldThrowNotFoundExceptionWhenUserDoesNotExist()`

### ✅ Padrão Given/When/Then
Cada teste segue estrutura clara:
- **Given:** Setup de dados e mocks
- **When:** Execução do método
- **Then:** Validação do resultado

### ✅ Isolamento de Testes
- Cada teste é independente
- Setup inicial em `@BeforeEach`
- Sem dados aleatórios
- Mocks resetados entre testes

### ✅ Verificação de Comportamento
- Uso de `verify()` para validar chamadas aos mocks
- `ArgumentCaptor` para capturar e validar argumentos
- Validação de não-chamada com `never()`

### ✅ Cobertura de Cenários
Cada handler tem testes para:
- ✅ Caso de sucesso
- ✅ Validações de entrada
- ✅ Regras de negócio
- ✅ Exceções esperadas
- ✅ Isolamento multi-tenant
- ✅ Preservação de dados

### ✅ Sem Testes de Framework
Não testamos:
- ❌ JpaRepository (framework)
- ❌ Banco de dados
- ❌ SQL queries
- ❌ Spring beans

### ✅ Mocking Estratégico
Mockeamos apenas:
- ✅ Repositórios
- ✅ Services externos
- ✅ PasswordEncoder
- ✅ TenantValidation
- ✅ SecurityUtils

---

## Próximos Passos Opcionais

### 1. Adicionar testes para Commands/Queries
```java
class CreateUserCommandTest {
    @Test
    void shouldCreateCommandWithValidData() { ... }
}
```

### 2. Adicionar testes para Mappers/DTOs
```java
class FullCourseResponseTest {
    @Test
    void shouldMapCourseToResponse() { ... }
}
```

### 3. Adicionar testes de integração (com @SpringBootTest)
```java
@SpringBootTest
class CreateUserHandlerIntegrationTest {
    @Test
    void shouldCreateUserEndToEnd() { ... }
}
```

### 4. Configurar relatórios de cobertura
```xml
<!-- pom.xml -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
</plugin>
```

---

## Checklist de Validação

- ✅ Todos os 8 handlers com testes
- ✅ Todos os testes compilam sem erros
- ✅ Padrão Given/When/Then em 100% dos testes
- ✅ Sem dados aleatórios
- ✅ Mocks apropriados para dependências
- ✅ Nomes descritivos dos testes
- ✅ Cobertura de casos de erro
- ✅ Validação de multi-tenant
- ✅ Sem testes de framework
- ✅ Documentação inline clara

---

## Notas Importantes

### 1. MockedStatic para SecurityUtils
Alguns testes usam `MockedStatic<SecurityUtils>` porque é classe utilitária com método estático:
```java
try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
    mockedSecurityUtils.when(SecurityUtils::getInstitutionId).thenReturn(1L);
    // teste
}
```

### 2. ArgumentCaptor para Validação
Para validar dados salvos, usamos ArgumentCaptor:
```java
ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
verify(userRepository).save(captor.capture());
User saved = captor.getValue();
assertEquals("expected", saved.getName());
```

### 3. Soft Delete em Alguns Handlers
Alguns handlers como `DeleteUserHandler` implementam soft delete (marcam como inactive):
```java
user.setActive(false);
userRepository.save(user);
```

### 4. Factory Methods
Alguns handlers usam factory methods (ex: `Course.create()`):
```java
Course course = Course.create(name, description, hours, institution);
```
Os testes validam que esses métodos são chamados corretamente.

---

## Cobertura por Módulo

| Módulo | Handlers Testados | Testes |
|--------|------------------|--------|
| User | CreateUserHandler, DeleteUserHandler | 15 |
| Admin/Institution | CreateInstitutionHandler | 15 |
| Admin/FeaturePlan | CreateFeatureHandler | 7 |
| Business/Course | CreateCourseHandler, ListAllCourseHandler, UpdateCourseHandler | 28 |
| Business/Instructor | CreateInstructorHandler | 8 |
| **Total** | **8 handlers** | **73 testes** |

---

## Licença e Documentação

Estes testes foram gerados conforme as especificações:
- Arquitetura: Monólito Modular Orientado ao Domínio
- Framework: JUnit 5 + Mockito
- Padrão: Given/When/Then
- Foco: Testes de handlers da camada application

Todos os testes estão prontos para executar com `./mvnw.cmd test`.

