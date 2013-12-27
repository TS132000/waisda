<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="nf" uri="/WEB-INF/tld/NumberFormat.tld"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tt"%>
<tt:html>
<tt:head title="Results">
	<script src="/static/script/taggingHistory.js"></script>
</tt:head>
<tt:body pageName="recap">

<div id="challenge-lightbox" class="glasspane closer hidden">
	<div class="lightbox">
		<div class="close-icon closer"></div>
		<div class="uil"></div>
		<c:if test="${recap.game.scoreToBeat != null}">
			<c:choose>
				<c:when test="${recap.ownerScore < recap.game.scoreToBeat}">
					<h2>Unfortunately, the challenger has won this game</h2>
				</c:when>
				<c:when test="${recap.ownerScore == recap.game.scoreToBeat}">
					<h2>You both scored equal points!</h2>
				</c:when>
				<c:when test="${recap.ownerScore > recap.game.scoreToBeat}">
					<h2>You have won the challenge!</h2>
				</c:when>
			</c:choose>
		</c:if>
		<p><strong>Challenge a friend, family member or acquaintance!</strong><br/>Post a message on Facebook, Twitter or through email and challenge your friends to improve your high score.</p>
		<div id="addthis-bar" class="bigShareButtons addthis_toolbox addthis_default_style" 
				addthis:url="http://${globalStats.domainName}/accept-challenge/${recap.game.id}/${recap.ownerScore}"
				addthis:title="I've scored ${recap.ownerScore} points with tagging ${recap.game.video.title}. Do you think you can beat me? #Waisda"
				addthis:description="Do you want to play?">
					<a class="btn btn-primary addthis_button_twitter">
						<span class="gradientButton twitter">Share on Twitter</span>
					</a>
					<a class="btn btn-primary addthis_button_facebook">
						<span class="gradientButton facebook">Share on Facebook</span>
					</a>
					<a class="btn btn-primary" href="mailto:?subject=I've scored ${recap.ownerScore} points with tagging ${recap.game.video.title}. Do you think you can beat me?&body=${recap.owner.name} plays Waisda?! He or she has scored ${recap.ownerScore} points by playing ${recap.game.video.title}. Can you improve this score?%0A%0AClick on the link below to accept this challenge:%0Ahttp://${globalStats.domainName}/accept-challenge/${recap.game.id}/${recap.ownerScore}%0A%0AInstructions:%0A- Add as many words that describe what you see and what you hear%0A- Confirm you entry by pressing the [enter] key on your keyboard%0A- Scoring points is based on matching entries from other players%0A- Some specific terms relevant to the content will result in bonus points%0A- Terms and conditions apply to playing the game: http://${globalStats.domainName}/terms%0A%0AFor more elaborate instructions, please refer to: http://${globalStats.domainName}/instructions%0A%0AGood luck!"><span class="gradientButton mail">Share through email</span></a>
				</div>
		<p class="close-link closer">Or close this window and view your score</p>
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
					<h2 class="h3">Challenge a friend, family member or acquaintance!</h2>
					<span class="boxline boxline-terra"></span>
				</header>
				<section class="box">
					<p>Post a message on Facebook, Twitter or through email and challenge your friends to improve your high score.</p>
					<input type="text" value="http://${globalStats.domainName}/accept-challenge/${recap.game.id}/${recap.ownerScore}" class="gameShare"></input>

					<div id="addthis-bar" class="bigShareButtons addthis_toolbox addthis_default_style" 
				addthis:url="http://${globalStats.domainName}/accept-challenge/${recap.game.id}/${recap.ownerScore}"
				addthis:title="I've scored ${recap.ownerScore} points with tagging ${recap.game.video.title}. Do you think you can beat me? #Waisda"
				addthis:description="Do you want to play?">
					<a class="btn btn-primary addthis_button_twitter">
						<span class="gradientButton twitter">Share on Twitter</span>
					</a>
					<a class="btn btn-primary addthis_button_facebook">
						<span class="gradientButton facebook">Share on Facebook</span>
					</a>
					<a class="btn btn-primary" href="mailto:?subject=I've scored ${recap.ownerScore} points with tagging ${recap.game.video.title}. Do you think you can beat me?&body=${recap.owner.name} plays Waisda?! He or she has scored ${recap.ownerScore} points by playing ${recap.game.video.title}. Can you improve this score?%0A%0AClick on the link below to accept this challenge:%0Ahttp://${globalStats.domainName}/accept-challenge/${recap.game.id}/${recap.ownerScore}%0A%0AInstructions:%0A- Add as many words that describe what you see and what you hear%0A- Confirm you entry by pressing the [enter] key on your keyboard%0A- Scoring points is based on matching entries from other players%0A- Some specific terms relevant to the content will result in bonus points%0A- Terms and conditions apply to playing the game: http://${globalStats.domainName}/terms%0A%0AFor more elaborate instructions, please refer to: http://${globalStats.domainName}/instructions%0A%0AGood luck!"><span class="gradientButton mail">Share through email</span></a>
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
					<c:choose>
						<c:when test="${tag.pioneer && tag.matchingTagEntry.tag == tag.tag}">
							<div class="tag match pionier" id="tagEntry3">
						</c:when>
						<c:when test="${tag.matchingTagEntry != null && tag.matchingTagEntry.tag != tag.tag}">
							<div class="tag match hierarchy" id="tagEntry3">
						</c:when>
						<c:when test="${tag.matchingTagEntry != null && tag.pioneer == false}">
							<div class="tag match confirmed" id="tagEntry3">
						</c:when>
						<c:otherwise>
							<div class="tag" id="tagEntry3">
						</c:otherwise>
						</c:choose>
						
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
		<div class="explanation-bubble hidden"></div>
	</div>



</div>
	
</tt:body>
</tt:html>
