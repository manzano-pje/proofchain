package com.proofchain.user.interfaces.controller;

import com.proofchain.user.applications.command.CreateUserCommand;
import com.proofchain.user.applications.command.UpdateUserCommand;
import com.proofchain.user.applications.handler.CreateUserHandler;
import com.proofchain.user.applications.handler.DeleteUserHandler;
import com.proofchain.user.applications.handler.ListAllUserHandler;
import com.proofchain.user.applications.handler.ListOneUserHandler;
import com.proofchain.user.applications.handler.UpdateUserHandler;
import com.proofchain.user.interfaces.dto.request.UserRequestDto;
import com.proofchain.user.interfaces.dto.request.UserUpdateDto;
import com.proofchain.user.interfaces.dto.response.UserReturn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController
 *
 * Função no sistema:
 * Expõe os endpoints REST responsáveis pelo gerenciamento de usuários da
 * plataforma ProofChain, delegando toda a lógica de negócio para os respectivos
 * Handlers da camada de aplicação.
 *
 * Estrutura atual:
 * Controller pertencente à camada de interfaces da arquitetura do ProofChain,
 * responsável apenas por receber requisições HTTP, validar os dados de entrada
 * e encaminhar a execução para os casos de uso.
 *
 * Fluxo:
 * 1. Recebe a requisição HTTP
 * 2. Executa validações estruturais (Bean Validation)
 * 3. Verifica autorização através do Spring Security
 * 4. Constrói o Command correspondente
 * 5. Delega o processamento ao Handler
 * 6. Retorna a resposta HTTP ao cliente
 *
 * Integração no sistema:
 * Integrado aos Handlers da camada de aplicação, Spring Security,
 * Bean Validation e documentação OpenAPI.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(
        name = "Usuários",
        description = "Endpoints responsáveis pelo gerenciamento de usuários da plataforma."
)
public class UserController {

    /*
     * =========================================================
     * DEPENDÊNCIAS
     * =========================================================
     */

    private final CreateUserHandler createUser;
    private final DeleteUserHandler deleteUser;
    private final ListAllUserHandler listAllUser;
    private final ListOneUserHandler listOneUser;
    private final UpdateUserHandler updateUser;

    /*
     * =========================================================
     * CADASTRO
     * =========================================================
     */

    /**
     * Realiza o cadastro de um novo usuário.
     *
     * Nota de decisão:
     * A instituição do usuário não é enviada na requisição.
     * Ela será obtida futuramente através do token JWT, garantindo
     * o isolamento entre tenants.
     *
     * @param user dados do usuário a ser cadastrado
     * @return HTTP 201 quando o cadastro for realizado com sucesso
     */
    @Operation(
            summary = "Cadastrar usuário",
            description = "Realiza o cadastro de um novo usuário na instituição autenticada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "409", description = "Usuário já cadastrado")
    })
    @PreAuthorize("hasRole('SUPER_ADMIN','ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserRequestDto user) {

        CreateUserCommand command = new CreateUserCommand(user);

        createUser.createUser(command);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*
     * =========================================================
     * CONSULTA
     * =========================================================
     */

    /**
     * Consulta um usuário pelo e-mail.
     *
     * @param email e-mail do usuário
     * @return usuário localizado
     */
    @Operation(
            summary = "Consultar usuário",
            description = "Retorna um usuário pertencente à instituição autenticada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário localizado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER','ADMIN')")
    @GetMapping("/get/{email}")
    public ResponseEntity<UserReturn> listOneUser(@PathVariable String email) {

        UserReturn user = listOneUser.listOneUser(email);

        return ResponseEntity.ok(user);
    }

    /**
     * Lista todos os usuários pertencentes à instituição autenticada.
     *
     * @return lista de usuários
     */
    @Operation(
            summary = "Listar usuários",
            description = "Retorna todos os usuários pertencentes à instituição autenticada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado")
    })

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER','ADMIN')")
    @GetMapping("/list")
    public List<UserReturn> listAllUser() {

        List<UserReturn> lista =  listAllUser.listAllUser();
        return lista;
    }

    /*
     * =========================================================
     * ATUALIZAÇÃO
     * =========================================================
     */

    /**
     * Atualiza os dados de um usuário.
     *
     * Nota de decisão:
     * Apenas administradores podem alterar usuários da instituição.
     *
     * @param id identificador do usuário
     * @param userUpdateDto novos dados do usuário
     * @return usuário atualizado
     */
    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza as informações de um usuário da instituição autenticada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PreAuthorize("hasRole('SUPER_ADMIN','ADMIN')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<UserReturn> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDto userUpdateDto) {

        UpdateUserCommand command = new UpdateUserCommand(userUpdateDto);
        UserReturn user = updateUser.updateUser(id, command);

        return ResponseEntity.ok(user);
    }

    /*
     * =========================================================
     * EXCLUSÃO
     * =========================================================
     */

    /**
     * Remove um usuário da instituição autenticada.
     *
     * Nota de decisão:
     * Atualmente a exclusão é física.
     * Futuramente poderá ser substituída por exclusão lógica,
     * preservando o histórico de auditoria.
     *
     * @param id identificador do usuário
     * @return mensagem de confirmação
     */
    @Operation(
            summary = "Excluir usuário",
            description = "Remove um usuário pertencente à instituição autenticada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário removido"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PreAuthorize("hasRole('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        deleteUser.deleteUser(id);

        return ResponseEntity.ok("Usuário apagado com sucesso.");
    }
}