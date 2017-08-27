<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="nf" uri="/WEB-INF/tld/NumberFormat.tld"%>
<%@ taglib prefix="nf" uri="/WEB-INF/tld/NumberFormat.tld"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tt"%>

<tt:html>
<tt:head title="Hoofdpagina">
</tt:head>
<tt:body cssClass="body" pageName="home">

	<p class="header-text">
		<span style="font-size:26px">Door <strong>Waisda</strong> te spelen</span>, help je ons archief beter doorzoekbaar te maken. Bedankt!
	</p>

	<!--h2>Kanalen</h2-->
	<ul style="margin-top:103px;" class="row equal-cols unstyled clear-both">
		
		<c:forEach var="channel" items="${channels}">
		
		<li class="span13" style="margin-left:0px;padding-right: 21px;margin-bottom:21px;">
			<a href="/start-game/${channel.video.id}" title="${channel.video.title}" rel="nofollow" class="box channel col">
				<div class="img">
					<img src="${channel.video.imageUrl}" />
					<div class="overlay trigger"></div>
				</div>
				<h3 class="h5" style="font-size:16px;font-weight:normal">${channel.video.title}</h3>
				<p class="small">Rondes: ${channel.video.timesPlayed}<br/>Beste score: <nf:format number="${channel.highscore}" /></p>
			</a>
		</li>
		</c:forEach>		
	</ul>
	

	<!--h2 class="spaced">Hoe werkt het?</h2-->
	<div class="row equal-cols">
		<ol class="unstyled pull-left">
			<li class="box rich order span3 col"><span class="index">1</span>Selecteer een spel via de kanalen of via de huidige rondes</li>
			<li class="box rich order span3 col"><span class="index">2</span>Voeg zo veel mogelijk termen in om te beschrijven wat je ziet en hoort</li>
			<li class="box rich order span3 col"><span class="index">3</span>Verdien punten met termen die overeenkomen met termen van andere spelers</li>
			<li class="box rich order span3 col"><span class="index">4</span>Lees de <a href="/spelinstructies">spel instructies</a> en <a href="/registreren">maak een account</a> aan om je scores te bewaren</li>
		</ol>
		<!-- laten we hopen dat dit niet nodig is: <p id="silverlight-message" class="clear-both small">In order to be able to join <a href="http://www.silverlight.net/">Microsoft Silverlight plugin</a> has to be installed.</p> -->
	</div>
	<!--div id="prizebanner">
		<a href="/spelinstructies#wedstrijdregels">
			<img src="/static/img/prijzenbanner.png" alt="" />
		</a>
	</div-->
	
	<div class="row equal-cols">
		<div class="box span4 col">
			<header class="header rich"><h2 class="h3 reset">Beste scores <small>(afgelopen week)</small></h2></header>
			<section class="reset scroll-box">
				<ol class="unstyled reset">
				<c:forEach items="${globalStats.topScores.topTen}" var="u" varStatus="status">
					<li class="chart-entry"><a href="/profiel/${u.user.id}">
					<span class="index pull-left">${status.index + 1}</span>
					<img src="${u.user.smallAvatarUrl}" />
					${fn:escapeXml(u.user.name)}
					<span class="score pull-right h5"><nf:format number="${u.score}" /></span>				
					</a></li>
				</c:forEach>
				</ol>
			</section>
		</div>
	
		<!-- #tagcloud -->
		<div class="box span4 col leading">

			<header class="header rich"><h2 class="h3 reset">Beste termen <small>(afgelopen week)</small></h2></header>
			<section class="reset fixed-low">
				<ul class="tagcloud unstyled">
					<c:forEach items="${globalStats.tagCloud}" var="tag">
						<li class="tag-entry size-${tag.relativeSize}"><a href="/tag/${tag.normalizedTag}">${tag.normalizedTag}</a></li>
					</c:forEach>
				</ul>
			</section>
		</div>
		
		<tt:games-queue dynamic="false"/>
		
	</div>

</tt:body>
</tt:html>
