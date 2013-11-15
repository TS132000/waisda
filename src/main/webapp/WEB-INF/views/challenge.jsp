<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="nf" uri="/WEB-INF/tld/NumberFormat.tld"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tt" %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>

<tt:html>
	<tt:head title="Waisda uitdaging">
	</tt:head>
	<tt:body pageName="challenge">
		<!-- FRANK FIXME :-) -->
		<div class="box colorbox span8">
	 		<header class="header rich"><h2 class="h2">Welkom bij Waisda<span class="boxline boxline-orange"></span></h2></header>
			<div class="box">
				<section>
				<h3>${game.initiator.name} heeft ${scoreToBeat} punten behaald met het taggen van "${game.video.title}" en daagt jou uit om zijn/haar score te verbeteren!</h3>
				<p>Instructies:</p>
				<ul>
					<li>Voer zoveel mogelijk woorden in die beschrijven wat je ziet en hoort</li>
					<li>Bevestig een woord door op de [enter] toets op jouw toetsenbord te drukken</li>
					<li>Punten verdien je met matches, wanneer je hetzelfde woord invoert als een medespeler</li>
					<li>Tags met een hoge inhoudelijke relevantie voor het programma ontvangen bonuspunten</li>
					<li>Op deelname aan het spel zijn de <a href="/voorwaarden">algemene voorwaarden</a> van toepassing</li>
				</ul>
				<p>Zie voor een uitgebreide uitleg ook onze <a href="/spelinstructies">spelinstructies</a>.</p>
				<p>Succes!</p>
				<form method="post" id="challengeForm">
						<fieldset>
							<div class="form-actions center">
								<a href="#" onclick="document.getElementById('challengeForm').submit(); return false" class="button button-terra" tabindex="9">Uitdaging accepteren</a>
							</div>
			
							<input type="submit" value="Start" tabindex="10" style="position:absolute;top:0;left:-10000px;"/>
						</fieldset>  
				</form>
				<p><a href="http://${globalStats.domainName}/">Waisda</a> is het zie, zoek en zeg spel van Vroege Vogels. Meer informatie over Waisda vind je hier: <a href="http://${globalStats.domainName}/over-het-spel">http://${globalStats.domainName}/over-het-spel</a></p>
			</section>
			</div>
		</div>


		<!-- Over Waisda -->
		<div class="colorbox box shadowbox span4 leading">
			<div class="shadowOverlay"></div>
			<header class="header rich">
				<h2 class="h2">Over Waisda <!--small>last 7 days</small--></h2><span class="bird bird-blue"></span><span class="boxline boxline-blue"></span>
			</header>
			<section class="box scroll-box fixed-low">
				<p>Het spel is ontwikkeld om het rijke archief van beeldmateriaal van Vroege Vogels beter toegankelijk te maken door op een speelse manier informatie te verzamelen over video's. Met jouw hulp worden deze video's beter vindbaar en verrijkt met aanvullende gegevens over de video.</p>
			</section>
		</div>





		
	</tt:body>
</tt:html>
