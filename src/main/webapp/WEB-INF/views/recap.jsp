<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="nf" uri="/WEB-INF/tld/NumberFormat.tld"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tt"%>
<tt:html>
<tt:head title="Resultaten">
</tt:head>
<tt:body pageName="recap">

<div id="challenge-lightbox" class="glasspane closer hidden">
	<div class="lightbox">
		<div class="close-icon closer"></div>
		<div class="uil"></div>
		<c:if test="${recap.game.scoreToBeat != null}">
			<c:choose>
				<c:when test="${recap.ownerScore < recap.game.scoreToBeat}">
					<h2>Helaas, de uitdager heeft dit spel gewonnen!</h2>
				</c:when>
				<c:when test="${recap.ownerScore == recap.game.scoreToBeat}">
					<h2>Jullie hebben evenveel punten gescoord!</h2>
				</c:when>
				<c:when test="${recap.ownerScore > recap.game.scoreToBeat}">
					<h2>U heeft de uitdaging gewonnen!</h2>
				</c:when>
			</c:choose>
		</c:if>
		<p><strong>Daag een vriend, familielid of kennis uit!</strong><br/>Plaats een oproep via Facebook, Twitter of e-mail en daag vrienden uit om jouw score te verbeteren.</p>
		<div id="addthis-bar" class="bigShareButtons addthis_toolbox addthis_default_style" 
				addthis:url="http://${globalStats.domainName}/accept-challenge/${recap.game.id}/${recap.ownerScore}"
				addthis:title="Ik heb ${recap.ownerScore} punten behaald met het taggen van ${recap.game.video.title}. Kan jij het beter? #Waisda"
				addthis:description="Do you want to play?">
					<a class="btn btn-primary addthis_button_twitter">
						<span class="gradientButton twitter">Deel op Twitter</span>
					</a>
					<a class="btn btn-primary addthis_button_facebook">
						<span class="gradientButton facebook">Deel op Facebook</span>
					</a>
					<a class="btn btn-primary" href="mailto:?subject=Ik heb ${recap.ownerScore} punten behaald met Waisda, kan jij het beter?&body=${recap.owner.name} speelt Waisda! Hij of zij heeft ${recap.ownerScore} punten behaald met het spelen van het fragment ${recap.game.video.title}. Kun jij die score verbeteren?%0A%0AKlik op onderstaande link om de uitdaging te accepteren.%0Ahttp://${globalStats.domainName}/accept-challenge/${recap.game.id}/${recap.ownerScore}%0A%0A%0AInstructies:%0A- Voer zoveel mogelijk woorden in die beschrijven wat je ziet en hoort%0A- Bevestig een woord door op de [enter] toets op jouw toetsenbord te drukken%0A- Punten verdien je met matches, wanneer je hetzelfde woord invoert als een medespeler%0A- Voor woorden die dieren, planten en andere voor het programma relevante onderwerpen beschrijven ontvang je bonuspunten%0A- Op deelname aan het spel zijn de algemene voorwaarden van toepassing: http://${globalStats.domainName}/voorwaarden%0A%0AZie voor een uitgebreide uitleg ook onze spelinstructies: http://${globalStats.domainName}/spelinstructies%0A%0ASucces!"><span class="gradientButton mail">Deel via mail</span></a>
				</div>
		<p class="close-link closer">Of sluit dit scherm en bekijk het scorescherm</p>
	</div>
</div>


<div class="equal-cols">
	
<div class="game-equal-cols">
	
	<div id="gameCanvas" class="span8 relative game-col leading">		
		
		<div class="row">
			<header class="clear extended">
				<h1 class="h4 pull-left reset"><c:out value="${recap.game.video.title}" /></h1>			
				<span id="timer-remaining" class="small pull-right">Game played on <strong class="clear-both">${recap.game.prettyStart}</strong></span>		
			</header>
		</div>
		
		<div class="row-fluid">
	
				<c:if test="${recap.owner.anonymous}">
				<!--div>
					<h3>Log in om uw punten te bewaren!</h3>
					<p>Zorg dat uw <nf:format number="${user.totalScore}" /> behaalde punten niet verloren gaan. 
					<a href="/inloggen">Log in</a> of <a href="/registreren">Registreer uzelf</a></p>
				</div-->
				</c:if>
				<div class="box span6">
					<table class="table table-striped table-condensed-ext table-clean">
						<tr>
							<th colspan="2">Your contribution to the game</th>
							<th class="text-right">points</th>
						</tr>
						<tr>
							<td></td>
							<td><strong><nf:format number="${recap.summary.countEmptyTags}" /></strong> ${recap.summary.countEmptyTags == 1 ? 'tag' : 'tags' } unmatched <span class="help" title="If another player matches this word later on this you will earn 145 points">?</span></td>
							<td class="text-right"><nf:format number="${recap.summary.countEmptyTags * 5}" /></td>
						</tr>
						<tr>
							<td></td>
							<td><strong><nf:format number="${recap.summary.countDictionaryMatches}"/></strong> ${recap.summary.countDictionaryMatches == 1 ? 'dictionary match' : 'dictionary matches' } <span class="help" title="A dictionary match will earn you 25 points">?</span></td>
							<td class="text-right"><nf:format number="${recap.summary.countDictionaryMatches * 25}" /></td>
						</tr>
						<tr>
							<td><img src="/static/img/match-social.png" alt="match with other player" /></td>
							<td><strong><nf:format number="${recap.summary.countMatchingTags}"/></strong> ${recap.summary.countMatchingTags == 1 ? 'match' : 'matches' } with other players <span class="help" title="Entering the same tag as one of your co-players will earn you 50 points">?</span></td>
							<td class="text-right"><nf:format number="${recap.summary.countMatchingTags * 50}" /></td>
						</tr>
						<tr>
							<td><img src="/static/img/match-pioneer.png" alt="Pioneer match" /></td>
							<td><strong><nf:format number="${recap.summary.countPioneerTags}"/></strong> ${recap.summary.countPioneerTags == 1 ? 'pioneer match' : 'pioneer matches' } <span class="help" title="If you were the first to enter a word then the first match will earn you 145 points">?</span></td>
							<td class="text-right"><nf:format number="${recap.summary.countPioneerTags * 100}" /></td>
						</tr>
						<tr>
							<td></td>
							<td><strong>Total score</strong></td>
							<td class="text-right"><nf:format number="${recap.ownerScore}" /></td>
						</tr>
					</table>
					<p class="small spaced-min">On the right, below 'Your tags', you can find a more detailed description of each match.</p>
				</div>

				<div class="box span6">
					<section>
						<h3>Want to earn even more points?</h3>
						<p>Challenge your friends and improve your chances at a higher score <span class="help" title="If you were the first to enter a word then the first match will earn you 145 points">?</span></p>

						<!-- AddThis Button BEGIN -->
						<div id="addthis-bar" class="addthis_toolbox addthis_default_style spaced reserved-space" 
						addthis:url="http://waisda.nl/start-game/${recap.game.video.id}"
						addthis:title="I just scored ${recap.ownerScore} points by adding ${fn:length(recap.tagEntries)} ${fn:length(recap.tagEntries) == 1 ? 'word' : 'words'} to &quot;${recap.game.video.title}&quot; #woordentikkertje"
						addthis:description="Do you want to play?">
							<a class="addthis_button_preferred_1"></a>
							<a class="addthis_button_preferred_2"></a>
							<a class="addthis_button_preferred_3"></a>
							<a class="addthis_button_preferred_4"></a>
							<a class="addthis_button_compact"></a>
							<a class="addthis_counter addthis_bubble_style"></a>
						</div>
						<script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=xa-4dca79b617093d25"></script>
					</section>
				</div>

		</div>

		<div class="row">
			<div class="box colorbox span8 leading shadowbox">
				<div class="shadowOverlay"></div>
				<header class="rich">
					<h2 class="h3">Daag een vriend, familielid of kennis uit!</h2>
					<span class="boxline boxline-terra"></span>
				</header>
				<section class="box">
					<p>Plaats een oproep via Facebook, Twitter of e-mail en daag vrienden uit om jouw score te verbeteren.</p>
					<input type="text" value="http://${globalStats.domainName}/accept-challenge/${recap.game.id}/${recap.ownerScore}" class="gameShare"></input>

					<div id="addthis-bar" class="bigShareButtons addthis_toolbox addthis_default_style" 
				addthis:url="http://${globalStats.domainName}/accept-challenge/${recap.game.id}/${recap.ownerScore}"
				addthis:title="Ik heb ${recap.ownerScore} punten behaald met het taggen van ${recap.game.video.title}. Kan jij het beter? #Waisda"
				addthis:description="Do you want to play?">
					<a class="btn btn-primary addthis_button_twitter">
						<span class="gradientButton twitter">Deel op Twitter</span>
					</a>
					<a class="btn btn-primary addthis_button_facebook">
						<span class="gradientButton facebook">Deel op Facebook</span>
					</a>
					<a class="btn btn-primary" href="mailto:?subject=Ik heb ${recap.ownerScore} punten behaald met Waisda, kan jij het beter?&body=${recap.owner.name} speelt Waisda! Hij of zij heeft ${recap.ownerScore} punten behaald met het spelen van het fragment ${recap.game.video.title}. Kun jij die score verbeteren?%0A%0AKlik op onderstaande link om de uitdaging te accepteren.%0Ahttp://${globalStats.domainName}/accept-challenge/${recap.game.id}/${recap.ownerScore}%0A%0A%0AInstructies:%0A- Voer zoveel mogelijk woorden in die beschrijven wat je ziet en hoort%0A- Bevestig een woord door op de [enter] toets op jouw toetsenbord te drukken%0A- Punten verdien je met matches, wanneer je hetzelfde woord invoert als een medespeler%0A- Voor woorden die dieren, planten en andere voor het programma relevante onderwerpen beschrijven ontvang je bonuspunten%0A- Op deelname aan het spel zijn de algemene voorwaarden van toepassing: http://${globalStats.domainName}/voorwaarden%0A%0AZie voor een uitgebreide uitleg ook onze spelinstructies: http://${globalStats.domainName}/spelinstructies%0A%0ASucces!"><span class="gradientButton mail">Deel via mail</span></a>
				</div>
				<script type="text/plain" class="npo_cc_social" src="https://s7.addthis.com/js/250/addthis_widget.js#pubid=xa-4dca79b617093d25"></script>
				</section>
			</div>
		</div>

	</div>



	<div id="rightColumn" class="box span4 game-col">
		<header class="rich extended">
				<div style="clear:both">
				<span class="pull-left"><strong>Your score:</strong></span>
				<h1 id="playerSessionScore" class="pull-right board span2"><nf:format number="${recap.ownerScore}"/></h1>
				</div>
		</header>
		<section class="reset">
			<h3 class="h4 sub-header">Your tags:</h3>		
	
			<div id="tagList" class="tag-list scroll-box">
				<c:forEach items="${recap.tagEntries}" var="tag">
					<div class="tag" id="tagEntry3">
						<span class="points">+<nf:format number="${tag.score}" /></span>
						<c:if test="${tag.matchingTagEntry != null && tag.pioneer}">
							<img src="/static/img/match-pioneer.png" class="icon" />
						</c:if>
						<c:if test="${tag.matchingTagEntry != null && !tag.pioneer}">
							<img src="/static/img/match-social.png" class="icon" />
						</c:if>
						<c:choose>
							<c:when test="${tag.dictionary != null && tag.matchingTagEntry}">
								<img src="/static/img/match-dictionary.png" class="icon" />
							</c:when>
							<c:when test="${tag.dictionary != null}">
								<img src="/static/img/match-dictionary.png" class="icon notMatchingWithOtherPlayer" />
							</c:when>
						</c:choose>
						<span><c:out value="${tag.tag}"/></span>
						<span class="matching small">
							<c:if test="${tag.matchingTagEntry != null}">
								match with <strong><c:out value="${tag.matchingTagEntry.tag}"/></strong>

								<c:choose>
								<c:when test="${tag.matchingTagEntry.owner.id == tag.owner.id}">from you</c:when>
								<c:otherwise>from <em><c:out value="${tag.matchingTagEntry.owner.name}"/></em></c:otherwise>
								</c:choose>

								<c:if test="${tag.pioneer}">. You introduced this word</c:if>
							</c:if>
						</span>
					</div>
				</c:forEach>
			</div>

		</section>
	</div>



</div>
	
</tt:body>
</tt:html>
