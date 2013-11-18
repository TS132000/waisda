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
								<a href="/game/${game.id}/recap/${user.id}#challenge" class="btn btn-primary btn-large pull-right">stop</a>
							</c:when>
							<c:otherwise>
								<a href="/game/${game.id}/recap/${user.id}" class="btn btn-primary btn-large pull-right">stop</a>
							</c:otherwise>
							</c:choose>
						</header>
					</div>		
				</div>
			</div>
			
			<div id="videoFrame" class="outside">
				<div id="video" class="video"></div>
			</div>
			<div class="pauze">pause</div>
		</section>

		<footer class="outside">
			<input type="text" maxlength="42" class="input-mega-xxl" id="inputField" />
		</footer>
		
	</div>

	<c:if test="${game.scoreToBeat != null}">
		<div id="rightColumn" class="box fullcolorbox fullcolorbox-green span4 col-game challengedGame">
		<div class="challengerBox">
			<span class="pull-left"><strong>Challenger's score:</strong></span><h1 class="pull-right board span2">${game.scoreToBeat}</h1>
		</div>
	</c:if>
	<c:if test="${game.scoreToBeat == null}">
		<div id="rightColumn" class="box fullcolorbox fullcolorbox-green span4 col-game">
	</c:if>
	
	<div class="playerBox">
			<header class="rich extended">
				<div style="clear:both">
				<span class="pull-left"><strong>Your score:</strong></span><h1 id="playerSessionScore" class="pull-right board span2">0</h1>
				</div>
			</header>
			<h4 class="h4 sub-header light">Your tags:</h4>
			<section class="innerwhitebox">	
				<div id="tagList" class="tag-list">
				</div>
			</section>
			<div class="clear"></div>
			<div class="explanation-bubble hidden"></div>
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