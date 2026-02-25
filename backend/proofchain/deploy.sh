#!/bin/bash

# ProofChain API Deployment Script for AWS EC2 (Ubuntu/Amazon Linux)
# Este script deve ser executado dentro da instância EC2.

set -e

echo "--- Iniciando o processo de Deployment ---"

# 1. Update system
echo "Atualizando o sistema..."
sudo apt-get update -y || sudo yum update -y

# 2. Check for Docker
if ! [ -x "$(command -v docker)" ]; then
  echo "Docker não encontrado. Instalando Docker..."
  if [ -x "$(command -v apt-get)" ]; then
    sudo apt-get install -y docker.io
    sudo systemctl start docker
    sudo systemctl enable docker
    sudo usermod -aG docker $USER
  elif [ -x "$(command -v yum)" ]; then
    sudo amazon-linux-extras install docker -y || sudo yum install docker -y
    sudo service docker start
    sudo chkconfig docker on
    sudo usermod -aG docker $USER
  fi
  echo "Docker instalado. IMPORTANTE: Você pode precisar sair e entrar novamente na sessão para usar docker sem sudo."
fi

# 3. Check for Docker Compose
if ! [ -x "$(command -v docker-compose)" ]; then
  echo "Docker Compose não encontrado. Instalando..."
  sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  sudo chmod +x /usr/local/bin/docker-compose
fi

# 4. Prepare Environment
if [ ! -f .env ]; then
  echo "Criando arquivo .env padrão..."
  cat <<EOT >> .env
DB_USER=postgres
DB_PASSWORD=$(openssl rand -base64 12)
JWT_SECRET=$(openssl rand -base64 32)
EOT
  echo ".env criado. Por favor, revise os valores se necessário."
fi

# 5. Build and Run
echo "Construindo imagens e subindo containers..."
docker-compose up -d --build

echo "--- Deployment concluído com sucesso! ---"
echo "API disponível em: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):8080"
