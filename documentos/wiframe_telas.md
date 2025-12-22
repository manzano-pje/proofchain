# Wireframes (ASCII Style)

Estes wireframes representam o layout e a densidade de informação para o frontend do **Valida Pro**.

## 1. Login (Público)

Design limpo, focado na conversão (login) e simplicidade.

```text
+---------------------------------------------------------------+
|  [ Logo Valida Pro ]                                          |
|                                                               |
|           Acesse sua conta                                    |
|           ----------------                                    |
|                                                               |
|       +---------------------------------------------+         |
|       |  seu.email@empresa.com                      |         |
|       +---------------------------------------------+         |
|                                                               |
|       [ ENTRAR COM MAGIC LINK ]                               |
|                                                               |
|                                                               |
|  (i) Não usamos senhas. Você receberá um link seguro.         |
|                                                               |
+---------------------------------------------------------------+
```

---

## 2. Dashboard (Logado)

Visão geral para gestores e emissores. Sidebar lateral persistente.

```text
+-------------------+---------------------------------------------------+
| Valida Pro   [PM] |  Dashboard                                        |
+-------------------+                                                   |
|                   |  [ KPI: Total 1250 ]  [ KPI: Valid 98% ]          |
|  MENU             |  [ KPI: R$ 1.200 (MRR) ]                          |
|  ----             |                                                   |
|  > Dashboard      |  +---------------------------------------------+  |
|    Emitir Cert    |  | Últimas Emissões                            |  |
|    Meus Certs     |  |---------------------------------------------|  |
|    Config         |  | # RC-9921 | Ana Silva   | AWS Cloud | 14/12 |  |
|                   |  | # RC-9922 | João Souza  | DevOps    | 14/12 |  |
|                   |  | # RC-9923 | Maria Lima  | Scrum     | 13/12 |  |
|  ---------------- |  | ...                                         |  |
|  Plano: PRO       |  +---------------------------------------------+  |
|  Org: Relvo Inc   |                                                   |
|                   |                                                   |
+-------------------+---------------------------------------------------+
```

---

## 3. Emitir Certificado

Foco na produtividade. Formulário à esquerda, Preview ao vivo à direita.

```text
+-------------------+---------------------------------------------------+
|  ... (Sidebar)    |  Emitir Certificado                               |
|                   |                                                   |
|                   |  +---------------------+  +--------------------+  |
|                   |  | DADOS DO ALUNO      |  | PREVIEW (Ao Vivo)  |  |
|                   |  |                     |  |                    |  |
|                   |  | Nome: [___________] |  |  +--------------+  |  |
|                   |  | Curso: [__________] |  |  | CERTIFICADO  |  |  |
|                   |  |                     |  |  |              |  |  |
|                   |  | Data Ini: [__/__]   |  |  |  João Silva  |  |  |
|                   |  | Data Fim: [__/__]   |  |  |     ...      |  |  |
|                   |  | Horas: [ 40 ]       |  |  | [QR]         |  |  |
|                   |  |                     |  |  +--------------+  |  |
|                   |  | [ LIMPAR ] [EMITIR] |  |                    |  |
|                   |  +---------------------+  +--------------------+  |
|                   |                                                   |
+-------------------+---------------------------------------------------+
```

---

## 4. Validação (Público)

Página acessada por quem recebe o certificado ou via QR Code.

```text
+---------------------------------------------------------------+
|  [ Logo Valida Pro ]                     [ Validar outro ]    |
+---------------------------------------------------------------+
|                                                               |
|        ( V )  CERTIFICADO VÁLIDO                              |
|                                                               |
|   ---------------------------------------------------------   |
|   Aluno:      Ana Maria Silva                                 |
|   Curso:      Arquitetura de Software Avançada                |
|   Emissão:    12/12/2024                                      |
|   Código:     RC-AB12-CD34                                    |
|   Status:     Registrado na Blockchain (Polygon)              |
|   ---------------------------------------------------------   |
|                                                               |
|   [ Ver na Blockchain (Explorer) ]   [ Baixar PDF Original ]  |
|                                                               |
+---------------------------------------------------------------+
```

## 5. Validação (Erro/Não encontrado)

```text
+---------------------------------------------------------------+
|  [ Logo Valida Pro ]                                          |
+---------------------------------------------------------------+
|                                                               |
|        ( X )  CERTIFICADO NÃO ENCONTRADO                      |
|                                                               |
|   O código "RC-0000-FAKE" não consta em nossos registros.     |
|   Verifique se digitou corretamente ou contate a emissora.    |
|                                                               |
|   [ Tentar Novamente ]                                        |
|                                                               |
+---------------------------------------------------------------+
```
