<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="tt" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="js" uri="/WEB-INF/tld/js.tld"%>
<tt:html>
<tt:head title="${game.video.title}">
	<c:choose>
		<c:when test="${game.video.playerType == 'NPO'}">
			<script src="http://ida.omroep.nl/npoplayer/npoplayer-min.js"></script>
		</c:when>
		<c:when test="${game.video.playerType == 'JW'}">
			<script src="/static/mediaplayer-6.7/jwplayer.js"></script>
		</c:when>
	</c:choose>
	<script src="/static/script/utils.js"></script>
	<script src="/static/script/videoplayer.js"></script>
	<script src="/static/script/taggingHistory.js"></script>
	<script src="/static/script/game.js"></script>
</tt:head>
<tt:body cssClass="game" pageName="game">

<div class="equal-cols-game">
	
	<div id="gameCanvas" class="box span8 relative col-game leading">		
	
		<header class="clear extended">
			<h1 class="h4 pull-left reset"><c:out value="${game.video.title}" /></h1>			
			<span id="timer-remaining" class="pull-right"></span>	
		</header>
		
		<section class="reset">
			<div id="vid-overlay-screen" class="row-fluid show">
				<div id="explanation" class="box clean span6">
					<div id="timer-intro" class="timer-intro"><small>The game starts in</small><strong>00:15</strong></div>
				
					<h2 class="h4">Instructions</h2>
					<ul>
						<li>Try to enter as many words as possible to describe what you see or hear</li>
						<li>Confirm a word by pushing [enter] on your keyboard</li>
						<li>Earn points by <strong>matching</strong>, which happens when you enter the same word as another player</li>
						<li>By joining a game, you submit to the following <a href="/voorwaarden" target="_blank">terms and conditions</a></li>
					</ul>				
					<h3 class="h5">Good luck!</h3>
				</div>
				<div class="box clean span6">
					<div id="playerList" class="box playerlist-box">
						<header class="rich">
							<c:choose>
							<c:when test="${game.scoreToBeat != null}">
								<a href="/game/${game.id}/recap/${user.id}#challenge" class="button button-terra pull-right">stop</a>
							</c:when>
							<c:otherwise>
								<a href="/game/${game.id}/recap/${user.id}" class="button button-terra pull-right">stop</a>
							</c:otherwise>
							</c:choose>
						</header>
					</div>		
				</div>
			</div>
			
			<div id="videoFrame" class="outside">
				<div id="video" class="video"></div>
			</div>
			<div class="pauze">hulplijn / pauze</div>
		</section>

		<footer class="outside">
			<input type="text" maxlength="42" class="input-mega-xxl" id="inputField" />
		</footer>
		
	</div>

	<c:if test="${game.scoreToBeat != null}">
		<div id="rightColumn" class="box fullcolorbox fullcolorbox-green span4 col-game challengedGame">
		<div class="challengerBox">
			<span class="pull-left"><strong>Punten van je uitdager:</strong></span><h1 class="pull-right board span2">${game.scoreToBeat}</h1>
		</div>
	</c:if>
	<c:if test="${game.scoreToBeat == null}">
		<div id="rightColumn" class="box fullcolorbox fullcolorbox-green span4 col-game">
	</c:if>
	
	<div id="rightColumn" class="box span4 col-game">
		<header class="rich extended">
			<h1 id="playerSessionScore" class="pull-left board span2">0</h1>				
			<h2 id="playerPosition" class="pull-right reset">
				<small class="h4">Rank</small>
				<span id="playerPositionMine">-</span> / <span id="playerPositionTotal" class="h4">-</span>
			</h2>
		</header>
		<section class="reset">
			<h3 class="h4 sub-header">Your tags:</h3>		
			<div id="tagList" class="tag-list scroll-box">
			</div>
		</section>
	</div>

</div>

<script type="text/javascript">
	jQuery(function() {
		<c:choose>
			<c:when test="${game.video.playerType == 'NPO'}">
				var video = {
						playerType : 'NPO',
						prid : <js:lit string="${game.video.prid}"/>,
						startTimeWithinEpisode : ${game.video.startTime},
						duration : ${game.video.duration},
						imageUrl : <js:lit string="${game.video.imageUrl}"/>,
						title : <js:lit string="${game.video.title}"/>
					};
			</c:when>
			<c:when test="${game.video.playerType == 'JW'}">
				var video = {
						playerType : 'JW',
						sourceUrl : <js:lit string="${game.video.sourceUrl}"/>,
						imageUrl : <js:lit string="${game.video.imageUrl}"/>
					};
			</c:when>
			<c:otherwise>
			var video = null;
			</c:otherwise>
		</c:choose>
		window.game = new Game(${game.id}, video, ${game.elapsed});
	});
</script>

</tt:body>
</tt:html>