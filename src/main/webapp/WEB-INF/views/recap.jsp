<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="nf" uri="/WEB-INF/tld/NumberFormat.tld"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tt"%>
<tt:html>
<tt:head title="Resultaten">
</tt:head>
<tt:body pageName="recap">
<div class="equal-cols">
	
<div class="game-equal-cols">
	
	<div id="gameCanvas" class="box span8 relative game-col leading">		
		
		
		<header class="clear extended">
			<h1 class="h4 pull-left reset"><c:out value="${recap.game.video.title}" /></h1>			
			<span id="timer-remaining" class="small pull-right">Ronde gespeeld op <strong class="clear-both">${recap.game.prettyStart}</strong></span>
		</header>
		
		<section>
			<div class="row-fluid">
				<c:if test="${recap.owner.anonymous}">
				<!--div>
					<h3>Log in om uw punten te bewaren!</h3>
					<p>Zorg dat uw <nf:format number="${user.totalScore}" /> behaalde punten niet verloren gaan. 
					<a href="/inloggen">Log in</a> of <a href="/registreren">Registreer uzelf</a></p>
				</div-->
				</c:if>
				<div class="span6">
					<table class="table table-striped table-condensed-ext table-clean">
						<tr>
							<th colspan="2">Jouw toegevoegde termen</th>
							<th class="text-right">punten</th>
						</tr>
						<tr>
							<td></td>
							<td><strong><nf:format number="${recap.summary.countEmptyTags}" /></strong> nieuwe ${recap.summary.countEmptyTags == 1 ? 'term' : 'termen' }<span class="help" title="Als andere spelers een overeenkomende term invoeren ontvangt u alsnog 145 punten">?</span></td>
							<td class="text-right"><nf:format number="${recap.summary.countEmptyTags * 5}" /></td>
						</tr>
						<tr>
							<td></td>
							<td><strong><nf:format number="${recap.summary.countDictionaryMatches}"/></strong> ${recap.summary.countDictionaryMatches == 1 ? 'overeenkomst met woordenboek' : 'overeenkomsten met woordenboek' } <span class="help" title="U ontvangt 25 punten door een overeenkomst met een term uit het woordenboek">?</span></td>
							<td class="text-right"><nf:format number="${recap.summary.countDictionaryMatches * 25}" /></td>
						</tr>
						<tr>
							<td><img src="/static/img/match-social.png" alt="overeenkomst met een andere speler" /></td>
							<td><strong><nf:format number="${recap.summary.countMatchingTags}"/></strong> ${recap.summary.countMatchingTags == 1 ? 'overeenkomst' : 'overeenkomsten' } met termen van andere spelers <span class="help" title="U ontvangt 50 punten als een andere speler deze term ook heeft ingevoerd">?</span></td>
							<td class="text-right"><nf:format number="${recap.summary.countMatchingTags * 50}" /></td>
						</tr>
						<tr>
							<td><img src="/static/img/match-pioneer.png" alt="Eerste overeenkomst" /></td>
							<td><strong><nf:format number="${recap.summary.countPioneerTags}"/></strong> ${recap.summary.countPioneerTags == 1 ? 'eerste overeenkomst' : 'eerste overeenkomsten' } <span class="help" title="U ontvangt 145 punten als u als eerste speler een term invoert dat overeenkomt met een eerder ingevoerde term van een andere speler">?</span></td>
							<td class="text-right"><nf:format number="${recap.summary.countPioneerTags * 100}" /></td>
						</tr>
						<tr>
							<td></td>
							<td><strong>Totale score</strong></td>
							<td class="text-right"><nf:format number="${recap.ownerScore}" /></td>
						</tr>
					</table>
					<p class="spaced-min">Bij 'Jouw ingevoerde termen' vind je een gedetailleerde omschrijving van de gemaakte overeenkomsten</p>
                    <c:if test="${user == null || user.anonymous}">
                        <h3>Wil je de score bewaren?</h3>
                        <p>Als je een <a href="/registreren"><strong>account aanmaakt</strong></a> of <a href="/inloggen"><strong>inlogt</strong></a>, worden
                        de punten die je hebt verdiend opgeslagen. Probeer de hoogste score te halen!</p>
                    </c:if>
					<h3>Wil je méér punten verdienen?</h3>
					<p>Daag je vrienden uit en verhoog je kans op een hogere score <span class="help" title="Door met méér spelers tegelijk te spelen heeft u meer kans om 145 punten per term te verdienen">?</span></p>

					<!-- AddThis Button BEGIN -->
					<div id="addthis-bar" class="addthis_toolbox addthis_default_style spaced reserved-space" 
					addthis:url="http://digibirds.beeldengeluid.nl/start-game/${recap.game.video.id}"
					addthis:title="Ik heb net ${recap.ownerScore} punten verdiend door ${fn:length(recap.tagEntries)} ${fn:length(recap.tagEntries) == 1 ? 'term' : 'termen'} aan &quot;${recap.game.video.title}&quot; toe te voegen"
					addthis:description="Wil je een ronde spelen?">
						<a class="addthis_button_preferred_1"></a>
						<a class="addthis_button_preferred_2"></a>
						<a class="addthis_button_preferred_3"></a>
						<a class="addthis_button_preferred_4"></a>
						<a class="addthis_button_compact"></a>
						<a class="addthis_counter addthis_bubble_style"></a>
					</div>
					<script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=xa-4dca79b617093d25"></script>
				</div>
						
				<div id="rankings" class="box span6">
					<header class="rich">
						<h2 class="h3 pull-left reset">Scores</h2>
					</header>
					<section class="reset">
						<ol class="unstyled reset">
						
						<c:forEach items="${recap.participants}" var="p">
							<li class="${p.user.id == recap.owner.id ? 'chart-entry extended highlight' : 'chart-entry extended'}">
							<tt:profileLink anonymous="${p.user.anonymous}" id="${p.user.id}">
									<span class="index pull-left">${p.position + 1}</span>
									<img src="${p.user.smallAvatarUrl}" />
									${fn:escapeXml(p.user.name)}
									<span class="score h5"><nf:format number="${p.score}" /></span><br />
									<small><nf:format number="${p.countTags}" /> ${p.countTags == 1 ? 'term' : 'termen'} en ${p.countMatches} ${p.countMatches == 1 ? 'overeenkomst' : 'overeenkomsten' }</small>
							</tt:profileLink>
							</li>
						</c:forEach>
						</ol>
					</section>
					
				</div>
			</div>
		</section>
	</div>

	<div id="rightColumn" class="box span4 game-col">
		<header class="rich extended">
			<h1 id="playerSessionScore" class="pull-left board span2"><nf:format number="${recap.ownerScore}"/></h1>				
			<h2 id="playerPosition" class="pull-right reset">
				<small class="h4">Beste scores</small>
				<span id="playerPositionMine">${recap.ownerPosition + 1}</span> / <span id="playerPositionTotal" class="h4">${fn:length(recap.participants)}</span>
			</h2>
		</header>
		<section class="reset">
			<h3 class="h4 sub-header">Jouw ingevoerde termen:</h3>
	
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
						<c:if test="${tag.dictionary != null && !tag.specialMatch}">
							<img src="/static/img/match-dictionary.png" class="icon" />
						</c:if>
						<c:if test="${tag.dictionary != null && tag.specialMatch}">
							<img src="/static/img/match-dictionary-${tag.dictionary}.png" class="icon" />
						</c:if>
						<span><c:out value="${tag.tag}"/></span>
						<span class="matching small">
							<c:if test="${tag.matchingTagEntry != null}">
								overeenkomstig met <strong><c:out value="${tag.matchingTagEntry.tag}"/></strong>

								<c:choose>
								<c:when test="${tag.matchingTagEntry.owner.id == tag.owner.id}">van jou</c:when>
								<c:otherwise>van <em><c:out value="${tag.matchingTagEntry.owner.name}"/></em></c:otherwise>
								</c:choose>

								<c:if test="${tag.pioneer}">. Je bent de eerste die deze term heeft ingevoerd</c:if>
							</c:if>
						</span>
					</div>
				</c:forEach>
			</div>

		</section>
	</div>
</div>
</div>
<div style="margin-top: 1em"><h2 style="text-align: center; color: #666666">De video's in dit spel komen uit de collectie <a target="_blank" href="http://www.natuurbeelden.nl">Natuurbeelden</a>, een unieke verzameling ruwe, ongemonteerde beelden van de Nederlandse natuur.</h2></div>

</tt:body>
</tt:html>
