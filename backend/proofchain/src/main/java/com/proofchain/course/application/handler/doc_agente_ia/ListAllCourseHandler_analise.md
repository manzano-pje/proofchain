```markdown
# Relatório técnico do código ListAllCourseHandler.java

## 1. Documentação e Javadoc Estruturado

### Proposta de Javadoc para a classe `ListAllCourseHandler`

```java
/**
 * Classe responsável por listar todos os cursos.
 *
 * @author Arquiteto de Software especialista em Java 21, Clean Code e Clean Architecture
 */
@Component
@AllArgsConstructor
public class ListAllCourseHandler {

    /**
     * Repositório de cursos.
     */
    private CourseRepository courseRepository;

    /**
     * Repositório da instituição.
     */
    private final InstitutionRepository institutionRepository;

    /**
     * Método responsável por listar todos os cursos para um determinado instituto.
     *
     * @return Lista de respostas completas dos cursos encontrados.
     * @throws InstitutionNotFoundException Se o instituto informado não existir.
     * @throws ResourceNotFoundException Se a lista de cursos estiver vazia.
     */
    public List<FullCourseResponse> listAllCourses() {
        // Implementação do método
    }
}
```

### Proposta de Javadoc para os métodos públicos

```java
/**
 * Método público responsável por listar todos os cursos.
 *
 * @return Lista de respostas completas dos cursos encontrados.
 */
public List<FullCourseResponse> listAllCourses() {
    // Implementação do método
}

/**
 * Método público responsável por lançar uma exceção caso o instituto informado não exista.
 *
 * @param institutionId ID da instituição para a qual será buscada a lista de cursos.
 * @return Lista de respostas completas dos cursos encontrados.
 * @throws InstitutionNotFoundException Se o instituto informado não existir.
 */
public List<FullCourseResponse> listAllCourses(Long institutionId) {
    // Implementação do método
}

/**
 * Método público responsável por lançar uma exceção caso a lista de cursos estiver vazia.
 *
 * @return Lista de respostas completas dos cursos encontrados.
 * @throws ResourceNotFoundException Se a lista de cursos estiver vazia.
 */
public List<FullCourseResponse> listAllCourses() {
    // Implementação do método
}
```

### Análise de Padronização de Custom Exceptions

#### 1. Exceções genéricas ou exceções que recebem Strings de mensagens diretamente no construtor

- Não foram encontradas exceções genéricas ou exceções que recebem Strings de mensagens diretamente no construtor.

#### 2. Linhas exatas das ocorrências

- Nenhuma linha foi identificada como lançando uma exceção genérica ou exceção com mensagem de erro direcionada a String.

#### 3. Sugestão para padronização dos erros internos

- Não foram encontradas mensagens de erro diretamente no construtor das exceções.
- Para substituir as mensagens fixas, sugerimos criar uma nova Exception customizada chamada `InstitutionNotFoundException`.

```java
/**
 * Exceção lançada quando a instituição informada não existe.
 *
 * @author Arquiteto de Software especialista em Java 21, Clean Code e Clean Architecture
 */
public class InstitutionNotFoundException extends RuntimeException {
    public InstitutionNotFoundException() {
        super("Instituição informada não encontrada.");
    }
}
```

- Para substituir a mensagem fixa "Não existem cursos cadastrados.", sugerimos criar uma nova Exception customizada chamada `ResourceNotFoundException`.

```java
/**
 * Exceção lançada quando o recurso (neste caso, os cursos) informado não existe.
 *
 * @author Arquiteto de Software especialista em Java 21, Clean Code e Clean Architecture
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

#### 4. Refatoração do código

```java
/**
 * Método público responsável por listar todos os cursos.
 *
 * @return Lista de respostas completas dos cursos encontrados.
 */
public List<FullCourseResponse> listAllCourses() {
    Long institutionId = SecurityUtils.getInstitutionId();
    boolean existInstitution = institutionRepository.existsByIdAndDeletedAtIsNull(institutionId);
    if (!existInstitution) {
        throw new InstitutionNotFoundException();
    }

    List<Course> courseList = courseRepository.findAllByInstitutionId(institutionId);
    if (courseList.isEmpty()) {
        throw new ResourceNotFoundException("Não existem cursos cadastrados.");
    }
    return courseList.stream()
            .map(FullCourseResponse::new)
            .collect(Collectors
                    .toList());
}
```

#### 5. Validação de Nomenclatura

- Variáveis, métodos e classes seguem o padrão camelCase do ecossistema Java.

## Conclusão

O código fornecido está bem estruturado com documentação adequada e padronização de exceções. As exceções propostas para substituição das mensagens fixas são válidas e seguem os padrões sugeridos pelo Clean Architecture.
```