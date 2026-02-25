# Guia de Deploy - ProofChain API (AWS EC2)

Este guia explica como realizar o deploy da API ProofChain em uma instância AWS EC2 utilizando Docker.

## Pré-requisitos

1. Uma conta AWS ativa.
2. Uma instância EC2 criada (recomendado: Ubuntu Server ou Amazon Linux 2023).
3. Grupo de Segurança configurado para permitir tráfego na porta `8080` (API) e `22` (SSH).
4. Acesso SSH à instância.

---

## Passo 1: Transferir os arquivos

Você precisa mover os arquivos do backend para a instância EC2. Você pode usar `git clone` se o repositório for público/privado (com chave SSH), ou usar `scp`:

```bash
# Exemplo usando SCP (execute da sua máquina local)
scp -r ./backend/proofchain ubuntu@seu-ip-ec2:/home/ubuntu/
```

## Passo 2: Executar o Script de Deploy

Acesse sua instância via SSH e navegue até a pasta do projeto:

```bash
ssh -i sua-chave.pem ubuntu@seu-ip-ec2
cd ~/proofchain
```

Dê permissão de execução ao script e execute-o:

```bash
chmod +x deploy.sh
./deploy.sh
```

O script irá:
- Instalar Docker e Docker Compose (se necessário).
- Criar um arquivo `.env` com senhas aleatórias.
- Construir a imagem Docker da API ProofChain.
- Iniciar os serviços (API e Banco de Dados PostgreSQL).

## Passo 3: Verificação

Após a execução, a API estará rodando. Você pode verificar os logs com:

```bash
docker-compose logs -f api
```

Acesse o Swagger para testar se tudo está ok:
`http://seu-ip-ec2:8080/swagger-ui/index.html`

---

## Notas Importantes

- **Segurança**: As senhas geradas no `.env` pelo script são aleatórias. Se precisar alterá-las, edite o arquivo `.env` e reinicie os containers com `docker-compose up -d`.
- **Persistência**: O banco de dados utiliza volumes Docker, o que significa que os dados não serão perdidos se o container for reiniciado.
- **Produção**: Para um ambiente de alta disponibilidade, considere usar o AWS RDS para o banco de dados em vez de rodar o banco no mesmo container.
