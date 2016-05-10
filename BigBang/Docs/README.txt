(Este ficheiro deve ser removido após aprovação de alterações e release em produção)

Descrevem-se em seguida os passos levados a cabo para actualizar o GWT da versão 2.5.0 para 2.7.0


1. Download e instalação de Google App Engine Java SDK 1.9.34 e Google Web Toolkit SDK 2.7.0, via "Eclipse Install" com site http://dl.google.com/eclipse/plugin/4.2

2. Alterar propriedades do projecto para que use o sdk 2.7.0

3. Em TasksNotificationsManager, remover o isRunning() - na nova versão não pode ser overwriten. Também removi a variavel que manipulava porque já não era usada.

4. Necessário alterar o mosaic para funcionar com o novo GWT (O mosaic foi abandonado)

	a) ir a https://code.google.com/archive/p/gwt-mosaic/downloads e obter GWT Mosaic 0.4.0-rc4 (GWT 2.0.1) 
	b) ir a http://www.gwtproject.org/versions.html e fazer download de gwt 2.0.1 (necessário para compilar mosaic)
	c) Criar projecto de mosaic
	d) editar o build.xml do mosaic inserindo <property name="env.GWT_HOME" value="/projects/gwt-2.0.1"></property> na linha 12 (valor aponta para pasta de gwt 2.0.1)
	e) nas propriedades do projecto, editar GWT20_HOME e "apontar" para nova variável criada com seguintes dados Name: GWT20_HOME Path: <path_gwt_2.0.1>
	f) definir variável ambiente JAVA_HOME como <localização_java>\jdk1.6.0_45
	g) Pôr o projecto compilável com 1.5
	h) trocar getWindowScrollHeight e getWindowScrollHeight de GlassPanelImpl por

			protected static int getWindowScrollHeight() {
				return Document.get().getScrollHeight();
			}
			
			protected static int getWindowScrollWidth() {
				return Document.get().getScrollWidth();
			}
			
	i) Em SheetPanel.java, comentar linhas 86, 101 e 281. Estas linhas davam erros com GWT 2.7.0, e em GWT 2.1.0 (para compilar mosaic) nada faziam, portanto funcionalmente eram "lixo".
	j) build jar de projecto de mosaic
	k) copiar gwt-mosaic-0.4.0-rc4.jar para <pasta de workspace do BigBang>\BigBang\war\WEB-INF\lib
	
5. Por algum motivo não reconhecia os files dos servlets dentro do web.xml do BigBang. Foi preciso copiar o conteúdo dos vários ficheiros para dentro do web.xml. Isto é um fix, mas deve ser analisado o porquê de não funcionar de outra forma, pois ficava muito mais elegante como estava.

6. Apagar gwt-unitCache (e eventualmente a pasta de ficheiros compilados dentro da pasta war)

7. Botão direito na pasta BigBang > Debug as > Super Dev mode. Compila o BB, e lança site em http://127.0.0.1:8888/BigBang.html

8. Em http://127.0.0.1:9876/ há links que devem ser guardados nos marcadores, e que clicados a partir da página do BB permitem ligar ou desligar o debug.

9. Falta testar BB com GWT 2.7.0 em chrome 50, para ver se corrige o problema detectado.

10. Deve-se investigar uma forma de eliminar o mosaic, ou assumir que devemos ser nós a expandir/adaptar o projecto quando for necessário (o que se prevê)