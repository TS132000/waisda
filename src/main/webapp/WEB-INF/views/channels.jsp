<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="nf" uri="/WEB-INF/tld/NumberFormat.tld"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tt" %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>

<ul id="channels" class="row equal-cols unstyled clear-both">

	<c:forEach var="channel" items="${channels}">

		<li class="span2 homeVid"><a href="/start-game/${channel.video.id}"
			title="${channel.video.title}" rel="nofollow"
			class="box channel col reset">
				<div class="img">
					<img src="${channel.video.imageUrl}" class="homeVid"/>
					<div class="overlay trigger"></div>
					<div class="overlay duration">${channel.video.prettyDuration}</div>
				</div>
				<h3 class="h4">${channel.video.title}</h3>
				<p class="small">
					${channel.video.timesPlayed} keer gespeeld<br />hoogste score:
					<nf:format number="${channel.highscore}" />
				</p>
		</a></li>
	</c:forEach>
</ul>
