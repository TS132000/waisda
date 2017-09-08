<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<%@ attribute name="dynamic" required="true" %>
<div id="games-queue" class="game-queue box span4 ${dynamic == 'true' ? 'dynamic' : 'col'}">
	<header class="header rich"><h2 class="h3 reset">Speel mee <small>(<span id="games-queue-count">0 spellen</span>)</small></h2></header>
	<section class="reset">
		<!-- Als game-queue class="empty" heeft verschijnt de onderstaande boodschap en wordt de tabel verborgen -->
		<p class="box-inner show">Momenteel zijn er geen spellen om aan deel te nemen. Start zelf een spel door één van de video's op de <a style="color:#e00034" href="/">hoofdpagina</a> te selecteren.</p>
		<div class="hide">
			<div class="row box-inner">
				<h3 class="h5 pull-left">Klik hier om een spel te selecteren</h3>
				<h3 class="h5 pull-right">Start in</h3>
			</div>
			<div class="scroll-box bordered">
				<table class="table table-condensed-ext table-striped table-clean"></table>
			</div>
		</div>
	</section>
	
</div>	
