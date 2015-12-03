# Introdução #

Durante todo o semestre de 2010.1 nos dedicamos à implementação de um compilador escrito na linguagem Java, baseando-nos no projeto descrito no livro-texto da disciplina de CK017 - _Construção de Compiladores_ do curso de Computação da UFC, compilador esse que é o objeto do trabalho final da referida disciplina e cujas funcionalidades implementadas, modificações feitas e limitações encontradas serão aqui reportadas.



# Preliminares #

Antes de iniciarmos, desejamos exaltar alguns pontos.

## Objetivo ##

O objetivo primordial do trabalho reportado neste documento foi aplicar os conceitos e técnicas relacionados à construção de um compilador vistos na disciplina, de forma a podermos sedimentá-los bem e a ajudar sua compreensão.

Focamos, nesta implementação, na linguagem MiniJava, uma linguagem criada para fins didáticos e cujas estruturas são herdadas da linguagem de programação [Java](http://en.wikipedia.org/wiki/Java). Como é de propósito didático, a MiniJava possui apenas construções simples e essenciais a uma linguagem de computação, deixando de lado funcionalidades extras tais como: sobrecarga de métodos, classes anônimas, operações com números reais ou negativos etc. embora ainda faça uso do paradigma de orientação a objetos presente no Java.

## Opções ##

Optamos desde o início pelo [Compiler-Compiler](http://en.wikipedia.org/wiki/Compiler-compiler) [SableCC](http://www.sablecc.org) por sua sintaxe de definição de gramática mais simples e de fácil entendimento, e por suas funcionalidades adicionais que foram de bastante ajuda, como por exemplo a geração automática da [AST](http://en.wikipedia.org/wiki/Abstract_Syntax_Tree) e a implementação também automática do padrão Visitor para percursos na árvore, com o adicional de termos a opção de executar código personalizado antes e/ou após o percurso do nó. Além disso, o SableCC faz uso de parsing LALR(1), o que nos permite simplificar um pouco mais a definição da gramática da linguagem utilizada e nos livra de algumas preocupações típicas de parsers preditivos, como os utilizados pela maioria dos CCs.

Apesar dessas vantagens, sua curva de aprendizagem é atualmente mais baixa que a do [JavaCC](http://javacc.dev.java.net), a outra opção disponível para todas as equipes, pelo fato de este possuir uma quantidade maior de documentação que aquele.

No entanto, ao superarmos a fase de busca de documentação e aprendizagem da sintaxe utilizada, percebemos claramente que a condução das fases subsequentes que dependem diretamente da análise sintática e, consequentemente da árvore de sintaxe, mostrou-se muito mais fluida que as dos projetos que utilizaram JavaCC.

Em face a esses argumentos, decidimos então compilar todas as informações e tutoriais que obtivemos e fizemos uso, além de informações adquiridas com nossa experiência no uso da ferramenta SableCC, em um único documento a ser redigido e publicado neste mesmo projeto, a fim de colaborarmos com a disseminação desse CC e facilitarmos a vida de quem venha a interessar-se por seu uso. Entretanto, devido a limitações de tempo causadas pela vida acadêmica dos participantes deste projeto, não temos prazos em mente para a disponibilização desse material.

# Fase I: Frontend #
## Análise Léxica ##
Nesta fase, definimos os tokens da linguagem no arquivo **MiniJava.sablecc3** disponível na seção de [Downloads](http://code.google.com/p/mmjc/downloads/list) do projeto.

Os tokens são definidos logo após a seção _Tokens_ do arquivo. Na seção _Helpers_ definimos _aliases_ para as expressões regulares mais comumente utilizadas durante a definição dos tokens. Vale ressaltar que as expressões nessa seção não geram tokens para o compilador, pois são meros "apelidos".

Na seção Ignored Tokens, informamos ao SableCC quais dos tokens declarados não devem ser repassados ao analisador sintático. No nosso caso, ignoramos os espaços brancos (caracteres não-imprimíveis de separação) e os comentários em linha e em bloco.

A compilação deste arquivo pelo utilitário do SableCC que gera a implementação do compilador foi realizada sem erros nem avisos, o que nos mostra que as definições estão corretas. Podemos concluir que esta fase foi concluída com êxito, já que todas as unidades léxicas da linguagem estão presentes na definição da linguagem, além de vários testes terem sido executados (cumulativamente com testes das fases subsequentes) e nenhum erro inesperado ocorreu.

Pode-se ver o conjunto de tokens gerados por essa fase para o programa de teste sugerido no Apêndice A.3 do livro-texto na seção de [Downloads](http://code.google.com/p/mmjc/downloads/list) do projeto, sob o nome de **example.0\_lexed.txt** (que possuiu como entrada o arquivo **example.0.java**).

## Análise Sintática ##
Na fase sintática, também de responsabilidade do SableCC, definimos as produções da gramática apresentada no Apêndice A.2 do livro-texto.

As produções da gramática residem na seção _Productions_ do arquivo **MiniJava.sablecc3** na seção de [Downloads](http://code.google.com/p/mmjc/downloads/list) do projeto.

As produções são marcadas pela construção `nonterm = right-side`, onde `nonterm` é um não-terminal da gramática sendo definida e `right-side` é um agrupamento de terminais e/ou não-terminais, representando a derivação equivalente da gramática.

Esta sintaxe é intercalada por instruções entre chaves ( '{' e '}' ), que são as regras de transformação da CST na AST desejada. Neste momento, essas regras são desimportantes.

Algumas modificações na gramática do livro-texto foram necessárias, de forma a retirar as suas ambiguidades. Dado que o SableCC constrói parsers LALR(1), a inserção de não-terminais para marcar a precedência de operadores foi suficiente para eliminar quase a totalidade das ambiguidades, não sendo necessário que nos preocupássemos com eliminação de recursões ou uso de _left-factoring_, por exemplo.

A compilação deste arquivo pelo utilitário do SableCC também se deu sem erros ou avisos, o que nos mostra que as definições das produções estão corretas. Como nenhuma construção mostrada na gramática apresentada pelo livro foi esquecida, acreditamos que esta fase está completa com êxito.

Diversos testes também foram realizados a fim de esgotar esta fase. Todos eles finalizaram com sucesso sob a implementação atual, sem que erros inesperados ocorressem.

Como o resultado da Análise Sintática do SableCC para gramáticas com transformações explícitas para a AST é a própria AST, mostraremos apenas a AST correspondente ao arquivo de testes.

## Árvore de Sintaxe Abstrata ##
Devido às próprias funcionalidades nativas do SableCC, não nos foi necessário programar o código para gerar a AST correspondente ao código-fonte sendo compilado.

Tivemos apenas que definir, no arquivo de entrada para o utilitário do SableCC, as regras de transformação da CST para a AST correspondente, cuja estrutura é definida na seção _Abstract Syntax Tree_ do arquivo **MiniJava.sablecc3** na seção de [Downloads](http://code.google.com/p/mmjc/downloads/list) do projeto.

A partir dessa definição, o próprio SableCC se encarrega de gerar o código citado e as respectivas classes representativas dos nós da árvore. Com isso, através de comparações _instanceof_ do próprio Java, podemos determinar com qual das regras da AST estamos lidando.

Como o código gerado é automático, nenhum teste foi realizado, bastando verificar a corretude das regras de transformação aplicadas. Veja no arquivo **example.10\_parsed.png** a estrutura da AST gerada para o arquivo fonte **example.10.java**.

## Análise Semântica ##
### Tabela de Símbolos ###
Como trata-se de uma linguagem imperativa (já que é uma restrição da linguagem Java), a tabela de símbolos para a MiniJava é mais eficientemente implementada utilizando-se tabelas de dispersão (_hashtables_).

A imagem abaixo mostra um resumo em UML de como implementamos a tabela de símbolos no MMJC.

![http://img33.imageshack.us/img33/8162/symboltableinterface.png](http://img33.imageshack.us/img33/8162/symboltableinterface.png)

Para preenchermos a tabela de símbolos com as informações pertinentes, fazemos uso de um objeto da classe **`TableVisitor`**, que herda de **`DepthFirstAdapter`**, a classe do SableCC que implementa o padrão Visitor.

#### Classes ####
A cada nó de declaração de uma classe, criamos um objeto do tipo **`Class`**, para o qual passamos a sua classe-mãe, seu **`TypeSymbol`** (o objeto que representa o nome da classe) e o seu número de campos (_fields_) e métodos. No caso de uma classe que não herda de nenhuma outra classe, a referência à classe-mãe será nula. Caso contrário, temos duas situações:
  * a classe-mãe já foi definida anteriormente no arquivo (e consequentemente já está na tabela). Neste caso, buscamos a classe-mãe na tabela e a passamos à classe sendo definida.
  * a classe-mãe ainda não foi definida. Nessa situação, procedemos como a seguir:
    * criamos o que chamamos de "classe fantasma" da classe-mãe. Este novo objeto é simplesmente a representação da classe-mãe real, apenas para mantermos a referência.
    * adicionamos a "fantasma" a uma lista especial que abriga todas as classes que já foram referenciadas mas ainda não foram declaradas.
    * passamos essa "classe fantasma" como mãe da classe sendo definida.

Após analisados esses casos, passamos à inclusão dos campos da nova classe, onde também verificamos a existência de referências adiantadas a classes. Para auxiliar a fase de tradução, a fim de sabermos qual o _offset_ de cada campo a partir do endereço do objeto no _heap_ de memória, salvamos também, em uma _hashtable_, a ordem em que os campos da classe foram definidos.

Antes de inserirmos a nova classe na tabela de símbolos, verificamos se há ou não uma representação fantasma dela na lista. Caso haja, fazemos com que aquela referência passe a ser o clone do objeto recém-criado. Com isso, nós "materializamos" a classe fantasma, ou seja, o fantasma agora é realmente o objeto real.

Por fim, adicionamos o objeto que antes era fantasma à tabela e o removemos da lista de fantasmas. Caso não haja fantasma para a classe sendo definida, simplesmente adicionamos o objeto recém-criado à tabela. Se já houver uma classe com o mesmo nome da atualmente definida na tabela, exibimos uma mensagem de erro e substituímos a referência anterior. Salvamos também a referência da classe que acabamos de definir, para que saibamos a qual classe pertencem os próximos métodos a visitar.

Ao final da análise, se a lista de fantasmas não estiver vazia é porque há classes que não foram definidas mas que são referenciadas no código. Com isso, exibimos os erros pertinentes e terminamos a execução do compilador.

Note que, com esse comportamento, não perdemos as referências às classes-mãe das classes que já estão na tabela. Com isso, evitamos um percurso desnecessário para fazer tal atualização.

#### Métodos ####
Ao encontrarmos um nó representante de declaração de método, criamos um novo objeto do tipo **`Method`**, para o qual passamos seu nome, a que classe ele pertence, seu tipo de retorno, e quantos parâmetros e variáveis locais o método possui.

Logo após, percorremos os nós de variáveis locais e parâmetros, adicionando-os ao objeto do método, verificando antes por referências adiantadas a classes.

Ao final, adicionamos o método à classe à qual ele pertence, exibindo uma mensagem de erro caso haja nela algum outro método com o mesmo nome, já que sobrecarga não é permitida em MiniJava.

#### Variáveis ####
Não percorremos os nós de variáveis da AST através do Visitor, mas sim logo na definição de métodos ou classes, pois nossa AST não diferencia entre parâmetros, variáveis locais e campos. Entretanto, tal deficiência não mostrou-se de forma alguma um empecilho para a implementação dessa fase.

### Verificação de Tipos ###
Na fase de verificação de tipos nós percorremos todos os outros nós que não foram percorridos na fase de construção da tabela de símbolos.

Na nossa implementação tivemos que realizar as duas etapas semânticas separadamente, pelo fato de a MiniJava aceitar referências adiantadas a classes, o que nos impede de construir a tabela ao mesmo tempo em que verificamos os tipos das expressões da linguagem.

Aqui, basta dizermos que modificamos a implementação dos nós de Expressão da AST gerada pelo SableCC a fim de adicionarmos formas de guardarmos e descobrirmos o tipo semântico associado a cada nó da AST que fosse ou pudesse ser usado como uma expressão.

Assim, inicialmente determinamos o tipo semântico de cada token (`boolean`, `int`, `int[]` ou a classe correspondente). Com isso, a cada construção de expressão verificamos se as expressões envolvidos têm valores corretos (por exemplo, numa adição devemos ter que ambas as expressões envolvidas têm que ser do tipo `int`) e associamos aos nós visitados os seus valores correspondentes (na adição também temos o tipo `int`) para os nós com valores corretos e `null` para os nós com valores incorretos.

Além disso, exibimos mensagens explicativas do erro ocorrido, aproveitando-nos de informações de linha/coluna no arquivo fonte de onde se encontra cada token. Entretanto, para os nós da AST tivemos novamente que modificar as suas implementação a fim de aumentarmos a precisão da localização do erro.

Para estruturas mais complexas, como a chamada de método, usamos mais fortemente a tabela de símbolos, na qual implementamos métodos para nos responder, por exemplo se uma dada variável é um objeto (se seu tipo é uma classe), se uma classe é subclasse de outra etc.

Com isso, finalizamos a fase de Verificação de Tipos. Realizamos diversos testes positivos, onde dávamos como entrada arquivos que não continham erros semânticos e não obtivemos falsos-negativos, ou seja, nenhum erro inesperado foi gerado. Entretanto, para os testes negativos, os que o compilador deveria alertar o usuário sobre o erro, não foram
tão explorado quanto os positivos. Mesmo assim, acreditamos que não existam erros nesta parte. O máximo que esperamos é que o compilador exiba um erro menos esclarecedor que o erro adequado.

Toda a parte semântica foi perfeitamente integrada com as fases anteriores do compilador.
Isso significa que conseguimos chegar até esta fase a partir da execução contínua do compilador pelas fases anteriores partindo do código-fonte.

Como esta fase é apenas construtiva de uma organização temporária dos tipos dos valores utilizados no programa, esta fase não possui caso de teste a ser mostrado. O caso seria executar o programa **mmjc.jar** que contém a implementação completa deste projeto com qualquer dos arquivos de exempĺo no arquivo de entradas e constatar que nenhum deles gera erro semântico.

## Registros de Ativação ##
Para criar os frames e as funções relacionadas a esta fase da compilação, utilizamos como base o arquivo **MipsFrame.java** cedido pelo professor, retirado dos arquivos restritos ao docente no site do livro-texto.

Como desejávamos gerar o código final para a arquitetura fictícia _Jouette_, fizemos diversas modificações para adequá-lo, mas mantendo o esqueleto _RISC_ da arquitetura _MIPS_.

Pela implementação original, tivemos alguns problemas, como na passagem de parâmetros, que utilizava um registrador cujo padrão solicitava que fosse usado para armazenar valores de retorno em vez de um registrador de passagem de parâmetros.

Foi nesta fase que sentimos maior dificuldade quanto às implementações, pois os padrões de listas utilizadas mudaram com relação ao restante do programa, além de outros problemas quanto à própria lógica utilizada pela implementação, que diferiu do projeto do livro-texto bastante em alguns pontos.

Sanados esses problemas, ainda nos deparamos com dois outros que não conseguimos resolver de forma alguma. Nos convencemos de que a implementação de mudança de contexto (_view shift_) implementadas pelo anterior **`MipsFrame`** não utilizava o temporário reservado para guardar o endereço do _frame pointer_. Além disso, em nenhum momento encontramos referência à atualização do valor do _frame pointer_ e muito menos do _return address_.

A questão das atualizações poderia ser resolvida adicionando instruções necessárias para salvar os novos valores dentro da nova função chamada, por tratarem-se de registradores salvos pelo chamado. Entretanto, não temos informações suficientes para realizar essa operação.

Mesmo com esses empecilhos, nenhum dos vários testes que realizamos gerou erros inesperados, o que nos leva a crer que esta fase está corretamente implementada.

O **`JouetteFrame`** (frame derivado do **`MipsFrame`**) foi integrado às outras fases de compilação sem mais problemas. Esta fase também não possui caso de teste a ser mostrado.

## Tradução para Representação Intermediária ##
Na fase de tradução para código intermediário, fizemos uso da linguagem IR sugerida pelo projeto do livro-texto.

Utilizamos linguagem intermediária para facilitar a modularização do compilador, para o caso em que desejarmos que ele compile várias linguagens-fonte para várias linguagens-alvo (ou arquiteturas) distintas. Fazemos isso com o uso de uma linguagem que não estão nem muito próxima do alto-nível nem do baixo-nível, mas que seja capaz de representar todas as estruturas e construções das diversas linguagens-fonte desejadas, e que seja de fácil (e de preferência simples) conversão para as linguagens-alvo escolhidas.

Nesta fase, usamos novamente um objeto que herda de **`DepthFirstAdapter`** para percorrer os nós da árvore do programa, gerando uma outra árvore de nós IR com a qual iremos trabalhas nas fases seguintes da compilação.

O conjunto de nós disponíveis é o seguinte:

| CONST(int value) <br /> NAME(Label label) <br /> TEMP(Temp.Temp temp) <br /> BINOP(int binop, Exp left, Exp right) <br /> MEM(Exp exp) <br /> CALL(Exp func, ExpList args) <br />  ESEQ(Stm stm, Exp exp) | MOVE(Exp dst, Exp src) <br /> EXP(Exp exp) <br /> JUMP(Exp exp, Temp.LabelList targets) <br /> CJUMP(int relop, Exp left, Exp right, Label iftrue, Label iffalse) <br /> SEQ(Stm left, Stm right) <br /> LABEL(Label label) |
|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|

A cada nó, criamos uma sub-árvore IR utilizando o conjunto de nós acima para representar equivalentemente o nó da AST visitado.

Por exemplo, para alocarmos um novo vetor de inteiros, realizamos uma chamada externa a uma função que denominamos `malloc` e que supomos que existe e aceita exatamente um parâmetro, que é o tamanho em bytes do espaço de memória a alocar. Com isso, calculamos o número de campos da classe à qual o objeto a ser instanciado pertence e passamos essa quantidade de _wordSize_ bytes para a função malloc, inicializando a memória alocada com zeros logo em seguida.

Realizamos uma série de testes com todas as construções permitidas da MiniJava e em nenhuma delas obtivemos comportamentos inesperados.

Esta fase também foi completamente integrada com as fases anteriores.

O arquivo **example.9\_ir.txt" mostra a árvore gerada a partir da execução do programa com o arquivo**example.9.java**como entrada.**


# Fase II: Backend #
## Canonicalização ##
Nesta fase, visamos simplificar a árvore gerada pela tradução para uma mais fácil de converter para a linguagem-alvo escolhida, eliminando nós que não tem representação imediata e substituindo por uma árvore equivalente cuja representação seja mais simples ou mais fácil de ser obtida.

No nosso caso, eliminamos os nós `ESEQ` e `SEQ`, transformando a entrada em uma lista de **`Stm`**, e reorganizamos os nós `CALL` de forma que nenhum nó `CALL` seja descendente direto de outro.

A fase de canonicalização foi implementada pelo autor do livro-texto. Por isso, não realizamos testes desta fase. Entretanto, fizemos a integração com as outras fases do compilador de forma completa.

## Blocos Básicos e Traços ##
A fase de divisão em blocos básicos e geração de traços visa encontrar uma sequência de ordenação das instruções do programa de tal forma que o uso da memória cache do processador seja o mais otimizado que conseguirmos. Além disso, organizamos os nós `CJUMP` de forma que o seu _label_ falso o acompanhe imediatamente, já que não há (se há é muito raro) arquitetura que suporte desvio para dois endereços distintos em uma mesma instrução, e sim vão para um destino quando a condição é verdadeira e ignoram a instrução quando falsa.

Esta fase também foi implementada pelo autor do livro-texto, por isso não fizemos testes. A integração com as outras fases também foi completa e sem limitações.

## Seleção de Instruções ##
Na fase de seleção de instruções tentamos transformar a representação intermediária obtida das fases anteriores num "pseudo-assembler", onde as ocorrências de registradores nessas instruções são substituídas por representações especiais que nos permitirão, após a fase de alocação de registradores, gerar finalmente o código-alvo.

Nós utilizamos o algoritmo _maximalMunch_ para realizar o que chamamos de "ladrilhamento" da árvore, que consiste em escolher instruções da linguagem-alvo que correspondam a uma certa sub-árvore da IR que obtivemos, de forma a não deixarmos nenhum nó "descoberto" e nem o "cubramos" mais de uma vez.

Com isso, temos a garantia de que estamos gerando o código de máquina equivalente àquela representação intermediária que possuíamos.

Por exemplo, se temos a instrução `ADDI d0 <- s0 + c`, onde `d0` é o registrador de destino da operação, `s0` é um registrador que armazena um dos parâmetros da operação e `c` é uma constante natural, podemos ladrilhar, por exemplo, a seguinte sub-árvore IR, caso ela venha a ocorrer no código:
> `BINOP(BINOP.`_`PLUS`_`, e, 4)`
onde e é uma outra expressão e 4 é o valor de c para este caso.

O que devemos fazer é criar um novo temporário para associar o resultado desta operação e retorná-lo ao chamador para que esse registrador possa ser usado em outra operação, assim como o da operação `e` será usado para substituir s0.

A fase de seleção de instruções foi perfeitamente integrada com as fases anteriores de compilação. Alguns testes foram realizados, mas infelizmente nem todos os casos puderam ser analisados.

O "pseudo-assembler" do arquivo **example.9.java** está no arquivo **example.9\_instr.txt**, na na seção de [Downloads](http://code.google.com/p/mmjc/downloads/list) do projeto. A cada pseudo-instrução é relacionado abaixo o conjunto dos registradores que são definidos e os que são usados, e os labels utilizados naquela instrução. Esses serão os valores que irão substituir respectivamente `d`<sub>k</sub>, `s`<sub>k</sub> e `j`<sub>k</sub>, onde k é um valor natural e sequencial.

## Análise de Longevidade ##
Na fase de análise de longevidade determinamos quais dos temporários criados e utilizados ao longo do programa para representar os registradores reais de máquina podem interferir entre si, nos proibindo assim de fazer com que sejam alocados em um mesmo registrador.

Para isso, fazemos uso de um grafo de fluxo de execução, onde cada instrução é um nó no grafo, e existe uma aresta direcionada _uv_ se é possível existir uma execução do programa em que a instrução _v_ é executada imediatamente após a instrução _u_.

De posse desse grafo, podemos então determinar quais temporários estão "vivos" ao mesmo tempo em cada uma de duas arestas. Um temporário está vivo em uma aresta se existe um caminho de algum nó onde ele seja definido para o nó _v_ destino da aresta em questão sem que este caminho passe por um nó (inclusive o nó _v_) que o também defina.

A partir do grafo de fluxo e das informações de quais temporários estão vivos ao mesmo tempo, podemos construir outro grafo, mas desta vez não-direcionado e onde cada vértice representa um temporário. Nesse novo grafo, existe uma aresta _uv_ se o temporário _u_ está vivo em alguma aresta onde o temporário _v_ também esteja. Ou seja, _u_ e _v_ interferem entre si e, por consequência, não podem ser atribuídos a um mesmo registrador.

Este grafo é o grafo manipulado pela última fase de compilação do nosso projeto, a alocação de registradores.

Infelizmente não tivemos tempo de executar os testes pertinentes a esta fase para eliminarmos os erros que viessem a ocorrer. Mesmo assim, o módulo foi integrado corretamente ao programa.

## Seleção de Registradores ##
Na última fase de compilação do nosso projeto, finalmente geramos as instruções assembler da linguagem-alvo.

Utilizamos uma heurística de coloração de grafos que deseja apenas garantir uma coloração própria com k cores do grafo de interferência obtido na fase anterior, onde k é o número de registradores disponíveis na máquina-alvo.

Se supusermos que cada cor nessa coloração obtida é na verdade um registrador, pela definição de coloração própria de vértices de um grafo não-direcionado, temos a alocação de registradores desejada, onde todos os temporários que possuírem a mesma cor podem ser associados sem conflitos a esse mesmo registrador.

No nosso projeto, implementamos a aglutinação de nós relacionados a MOVE utilizando os dois critérios conservadores vistos na disciplina: Briggs e George. Aplicamos primeiramente o critério de George, por ser mais fácil de verificar. Caso suas condições não sejam satisfeitas, verificamos se o critério de Briggs é satisfeito.

Além disso, implementamos o transbordamento e o recomeço, assim como sugerido pelo projeto do livro-texto.

Infelizmente também não pudemos realizar os testes pertinentes a esta fase e, com isso, não eliminamos possíveis problemas que tenham passado despercebidos. Entretanto, também integramos corretamente esta fase de compilação às fases restantes do programa.


# Tabela de Síntese #
A tabela a seguir resume as informações deste relatório quanto ao estado de cada fase da compilação:

| **Fase** | **Implementada** | **Testada** | **Testes satisfatórios** | **Testes de erros** | **Integrada** |
|:---------|:-----------------|:------------|:-------------------------|:--------------------|:--------------|
| Análise Léxica      | sim              | sim          | sim                      | sim                     | sim           |
| Análise Sintática   | sim              | sim          | sim                      | sim                     | sim           |
| AST                 | automática       | -            | -                        | -                       | automática    |
| Análise Semântica   | sim              | sim          | sim                      | faltam testes negativos | sim           |
| Registros de Ativ.  | sim              | sim          | sim                      | -                       | sim           |
| Tradução para IR    | sim              | sim          | sim                      | -                       | sim           |
| Canonicalização     | via site         | -            | -                        | -                       | sim           |
| Blocos e Traços     | via site         | -            | -                        | -                       | sim           |
| Seleção de Instr.   | sim              | faltam casos | sim                      | -                       | sim           |
| Análise de Longev.  | sim              | com erros    | -                        | -                       | sim           |
| Alocação de Regist. | sim              | não          | -                        | -                       | não           |

  * Os campos onde aparece "-" são aqueles cuja resposta não se aplica à fase, seja por que esta não gera erros (caso da coluna **Testes de erros**), por que não foram realizados todos os testes necessários (caso da coluna **Testes satisfatórios**) ou quando os testes são desnecessários (no caso de a implementação ter sido fornecida pelo site do livro-texto ou quando a fase é implementada automaticamente, caso em que aparece na coluna **Testada**).

  * "faltam testes negativos" significa que não testamos todos os casos em que o compilador deveria reconhecer um erro do usuário e informá-lo desse erro. Entretanto, foram testados exaustivamente os casos que não deveriam gerar qualquer erro.

  * "faltam casos" significa que nem todas as construções da linguagem foram aplicadas nos testes para verificar possíveis erros, embora tenhamos convicção de que se houverem, serão muito poucos os erros.

  * "com erros" significa que a fase foi implementada, mas algumas instâncias não conseguem ser executadas devido a erros inesperados.

# Conclusão #
Terminamos aqui o relatório onde nos propusemos a descrever um pouco do projeto e a mostrar a implementação do trabalho final da nossa disciplina de _Construção de Compiladores_.

Foram ao todo em torno de quatro meses de trabalho dos participantes deste projeto, sob a observação do professor e do monitor da disciplina.

Esperamos ter correspondido às expectativas e alcançado as metas traçadas pela parte prática da disciplina, e agradecemos àqueles que se propuseram a ler este documento.


---



Os autores,

<br />
Arthur Rodrigues Araruna

Carlos Vinicius Gomes Costa Lima