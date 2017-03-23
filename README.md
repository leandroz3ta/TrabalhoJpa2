# TrabalhoJpa2
Trabalho de pos graduacao referente ao modulo JPA segunda parte. 

Quais as responsabilidades das anotações abaixo:

-> @MapppedSuperclass : uma classe mapeada com essa anotação faz com que as filhas possam herdar e fazer Override e AssociationOverride. como por exemplo o atributo de Long version.

-> @Version : faz com que o objeto perca a concorrência dele mesmo onde o usuário pode sobre escrever um caso dois objetos do mesmo tipo venham a ser persistidos ao mesmo tempo checando se ele já foi persistido ou não.

-> @Entity : mostra para a JPA que essa classe deixa de ser classe para ser um entidade a ser persistida na base de dados, ou seja, que vai fazer transações com o auxílio do Entity Manager

-> @ Temporal : dizemos que ele atributo do tipo Date ou Calendar ou seja que trabalhe com tempo, podemos manipula-o pegando somente a hora com o uso TemporalType.TIME e vários outros tipos.

-> @Id : faz com que tenhamos uma Primary Key que será o controle de objeto na base de dados dizendo que um objeto não e igual a outro

-> @GeneratedValue : dizemos com essa anotação como será a forma de geração de id para o objeto populado na base de dados, por exemplo dizendo stratey = GeneratedValue.AUTO assim afirmamaos que a chave sera auto incrementada e distinta.

-> @Table : faz com que a JPA entenda que não irá pegar por default o nome da Entity da definir o nome da tabela na base de dados podem assim o desenvolvedor atribuir novos nomes para a mesma.

-> @Column: podemos definir várias configurações para a coluna com por exemplo informar se pode ser nulo , o tamanho do atributo, o nome personalizado e dentro várias outras definições

-> @Basic: e colocada para dizer se o atributo da entidade terá um campo nulo ou não e também para ajudar nas consultas que contenham LAZY ou EAGER.

-> @ManyToOne : ela e anota para dizer que ele aquele atributo dentro da entidade está fazendo uma referência a outra entidade, assim como temos no modelo ORM e muitos para um, que a terá a chave estrangeira da entidade N para entidade 1.

-> OnetoOne : sao mapeadas com essa anotação relações de 1 para 1 no modelo ORM sendo configurada com atributo mappedBy para relacionamento bi direcionais partindo do lado mais fraco para o lado mais forte.

-> @ManytoMany: indica onde num ORM será de muito para muito, assim a será criada uma nova entidade e nova tabela contendo FK de uma tabela e FK de outra tabela .

-> @JoinColumn: aponta para a JPa que tera uma coluna a mais na tabela que será designada para dizer que esta coluna será carregada com chave primária.

-> @JoinTable: quando fazemos uso de Relacionamentos 1 para N , N para N , 1 para 1 precisamos dizer para JPa como será feita esse relacionamento de PK e FK através dessa anotação com os atributos joinColumns, referencedColumnName.

Qual a responsabilidade / objeto dos métodos do EntityManager

-> isOpen : método pelo qual a será aberto a sessão para comunicação com o banco.

-> close: método pelo qual será fechado a sessão com o banco após ter feito o commit e persistindo o objeto.

-> createQuery: método pelo qual dizendo como será feita a consulta uma String contendo o código que realiza a transação com o banco deseja podendo ser feita via sql native , ou JPQL usando o próprio hibernate para tal.

-> find: método no qual é encontrado um objeto ou entidade via ID passando como parâmetro o próprio ID.

-> merge: método para qual e feita atualização de dados da entidade ou objeto passando o objeto root e checando se não existir o mesmo id.

-> remove: método para qual e feita a remoção do objeto da base de dados passando o Id como parâmetro.

-> persist: método pelo qual é feito a inclusão do objeto na base de dados

Como instanciar Criteria do Hibernate através do EntityManager

public Criteria createCriteria(Class<?> clazz) {
	return getSession().createCriteria(clazz);
}

protected EntityManager em;
public Session getSession() {
	return (Session) em.getDelegate();
}

public Criteria createCriteria(Class<?> clazz, String alias) {
	return getSession().createCriteria(clazz, alias);
}
Como abrir uma transação?

protected EntityManager em; em.getTransaction().begin();

Como fechar uma transação?

protected EntityManager em;

em.getTransaction().commit();

public void fecharEntityManager() { if (em.isOpen()) { em.close(); } }

Como criar e executar uma query com JPQL?

StringBuilder jpql = new StringBuilder(); jpql.append(" SELECT COUNT(p.id) "); pql.append(" FROM Venda v "); jpql.append(" INNER JOIN v.produtos p "); jpql.append(" INNER JOIN v.cliente c "); jpql.append(" WHERE c.cpf = :cpf ");

Query query = em.createQuery(jpql.toString()); query.setParameter("cpf", "001.001.001-01");

Long qtdProdutosDaVenda = (Long) query.getSingleResult();

Qual a responsabilidade dos valores FetchType.LAZY e FetchType.EAGER?

Tem como dever carregar de as informações quando forem pedidos ( LAZY ) ou seja de forma preguiçosa. ou de forma total que traz na consulta tudo não importa se a consulta tenha campos específicos.

Qual a responsabilidade dos valores CascadeType.PERSIST e CascadeType.REMOVE?

Ele tem dever após serem anotados a entidades serão persistidas (CascadeType.PERSIST salvas ) em cascata ou seja em forma de onde em sequência. Enquanto que pode ser feito da mesma forma a remoção do dados ( CascadeType.REMOVE ).

Como fazer uma operação BATCH (DELETE ou UPDATE) através do EntityManager?

@AfterClass public static void deveLimparBaseTeste() { EntityManager entityManager = JPAUtil.INSTANCE.getEntityManager();

	entityManager.getTransaction().begin();
	
	Query query = entityManager.createQuery("DELETE FROM Produto p");
	int qtdRegistrosExcluidos = query.executeUpdate();
	
	entityManager.getTransaction().commit();

	assertTrue("certifica que a base foi limpada", qtdRegistrosExcluidos > 0);
}
Qual a explicação para a exception LazyInitializationException?

quando não mais o objeto que contenha um fetch sendo LAZY no escopo do EntityManager e o usuário pede para ser persistido ou consultado um objeto que contém Lazy no seu escopo.
