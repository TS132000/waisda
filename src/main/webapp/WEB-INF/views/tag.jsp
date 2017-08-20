<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib tagdir="/WEB-INF/tags" prefix="tt" %>

<tt:html>
<tt:head title="Tag ${tagEntryStats.normalizedTag}"></tt:head>
<tt:body pageName="tag" cssClass="body">

<h1>Term <span class="mark">${tagEntryStats.normalizedTag}</span></h1>

<p>Eerst gebruikt door
<a style="color:#e00034" href="/profiel/${tagEntryStats.firstEntry.owner.id}">${tagEntryStats.firstEntry.owner.name}</a>
tijdens het spel <a style="color:#e00034" href="/start-game/${tagEntryStats.firstEntry.game.video.id}">${tagEntryStats.firstEntry.game.video.title}</a>
op datum ${tagEntryStats.firstEntry.prettyCreationDate}</p>

<h2 class="spaced">Andere spellen met term <span class="mark">${tagEntryStats.normalizedTag}</span></h2>
<ul class="row equal-cols unstyled">
	<c:forEach items="${tagEntryStats.videoStats}" var="vs">
		<li class="span2">
			<div class="box channel col">
				<a href="/start-game/${vs.video.id}" rel="nofollow" class="reset">
					<div class="img">
						<img src="${vs.video.imageUrl}" />
						<div class="overlay trigger"></div>
					</div>
					<h3 style="color:#333;"class="h5">${vs.video.title}</h3>
				</a>
				<p class="small" style="color:#333;">
					Andere ingevoerd termen: <c:forEach items="${vs.topTags}" var="topTag" varStatus="topTagStatus"><a style="color:#333;" href="/tag/${topTag}" class="reset">${topTag}</a><c:if test="${topTagStatus.index < fn:length(vs.topTags) - 1}">, </c:if></c:forEach>
				</p>
			</div>
		</li>
	</c:forEach>
</ul>


</tt:body>
</tt:html>