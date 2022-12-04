<h1>Explorando Padrões de Projetos na Prática com Java</h1>

<h3>Sobre:</h3>

<p>Especificamente, este projeto explorou alguns Padrões de Projetos usando o Spring Framework, são eles: 

<b> Singleton:</b> como a classe  <i>ClienteServiceImpl</i> é um @Service, ela será tratada como um Singleton. 

<b>Strategy:</b> a interface <i>ClienteService</i> define o padrão Strategy no domínio de cliente. Assim, se necessário, podemos ter múltiplas implementações dessa mesma interface. 

<b>Facade:</b> a camada Resource representa este Padrão de Projeto, pois abstrai toda a complexidade de integrações com o Banco de Dados H2 e com a API do ViaCep em uma interface simples e coesa.</b></p>

Além disso, o projeto oferece como serviço o controle de clientes, onde é possível: criar, ler, atualizar e excluir clientes do Banco de Dados. Para salvar ou atualizar informamos apenas o nome do cliente e o cep, assim o sistema consome a API externa do ViaCep para completar o restante das informações do endereço.

<h3>Pré-requisitos com Docker:</h3>

- Docker instalado

<h3>Como executar:</h3>

- execute o comando:
docker run -p 8080:8080 --name padroesprojetospring -it silviacristinaa/padroesprojetospring:latest

<h3>Pré-requisitos sem Docker:</h3>

- Java 11

<h3>Para clonar o repositório:</h3> 

- git clone https://github.com/silviacristinaa/padroesprojetospring.git

<h3>Como executar:</h3>

- Será executado na porta 8080

- Dentro da pasta do projeto, execute o comando: 
./mvnw spring-boot:run

<h3>Tecnologias Utilizadas:</h3>

 - Java 11, Spring Boot, Maven, Lombok, Banco de Dados H2, Spring Cloud OpenFeign, Docker, Testes Unitários e de Integração

<h3>Autor:</h3>

<a href="https://www.linkedin.com/in/silvia-cristina-alexandre">
 <img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/114493777?v=4" width="100px;" alt=""/>
 <br />
 <sub><b>Silvia Cristina Alexandre</b></sub></a>

[![Linkedin Badge](https://img.shields.io/badge/-Silvia-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/silvia-cristina-alexandre)](https://www.linkedin.com/in/silvia-cristina-alexandre)
[![Gmail Badge](https://img.shields.io/badge/-silviacristinaalexandre1@gmail.com-c14438?style=flat-square&logo=Gmail&logoColor=white&link=mailto:silviacristinaalexandre1@gmail.com)](mailto:silviacristinaalexandre1@gmail.com)
<hr>
