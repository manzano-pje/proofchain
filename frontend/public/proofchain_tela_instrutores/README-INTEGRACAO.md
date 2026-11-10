# Tela de Instrutores — ProofChain

## Arquivos

Copie os arquivos para estas pastas do frontend:

- `views/InstructorPage.vue` → `frontend/src/modules/business/views/admin/instructor/InstructorPage.vue`
- `views/InstructorPage.css` → `frontend/src/modules/business/views/admin/instructor/InstructorPage.css`
- `services/Instructor.service.ts` → `frontend/src/modules/business/services/Instructor.service.ts`
- `types/Instructor.types.ts` → `frontend/src/modules/business/types/Instructor.types.ts`

## Rota

No `frontend/src/router/index.ts`, altere a constante `instructorRoute` para:

```ts
const instructorRoute = () =>
  import('@/modules/business/views/admin/instructor/InstructorPage.vue')
```

A rota `instrutores` está registrada nos grupos `admin` e `institutionAdmin`, utiliza essa constante e permite somente usuários com role `Admin`.

## Contrato considerado

- `POST /api/v1/instructor/register`
- `GET /api/v1/instructor/list`
- `GET /api/v1/instructor/get/{id}`
- `PATCH /api/v1/instructor/update/{id}`
- `DELETE /api/v1/instructor/delete/{id}`

## Limitações identificadas no backend

1. `GET /list` retorna somente instrutores ativos.
2. O cadastro exige `id_User` e `id_course`. O formulário solicita esses IDs porque não foi identificado, no contrato do módulo, endpoint para carregar opções de usuários e cursos.
3. O backend deriva a instituição da sessão autenticada; o frontend não envia `id_Institution`, que não deve ser tratado como mecanismo de autorização.
4. O handler de atualização aplica atualmente apenas o campo `active`. Por isso, esta tela não oferece edição de especialidade ou data de contratação.

## Antes do deploy

1. Confira se `VITE_API_URL` termina em `/api/v1`, conforme os demais serviços.
2. Execute o build do frontend.
3. Teste cadastro, consulta, alteração de situação e exclusão com dados de homologação.
4. Para exibir usuários e cursos por nome em seletores, será necessário integrar os endpoints de listagem correspondentes ou criar endpoints específicos.
