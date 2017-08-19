<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tt" %>
<%@ taglib prefix="nf" uri="/WEB-INF/tld/NumberFormat.tld"%>

<%@ attribute name="cssClass" required="false" %>
<%@ attribute name="pageName" required="false" %>

<body class="${cssClass}">
	<script>
		jQuery("body").css('opacity', 0);
		var fadeInTimeout = setTimeout(function() {
			jQuery("body").animate({ 'opacity' : 1 }, 350);
		}, 1000);
	</script>
	
    
    <div class="brand-marker">
    
    
    	<div class="container">
    	<div class="waisda-logo"><a href="/"> <img src="/static/img/waisda-logo.png" alt="Waisda" height="44" width="272"/></a></div>
      	
      	
      	<c:if test="${globalStats != null}">
					<c:if test="${user != null || user.playerBarVisible}">
					<div class="stats pull-right">
					<c:if test="${globalStats.currentlyPlaying > 0}">
						<c:if test="${user == null || user.anonymous}">
							<p ><strong><nf:format number="${globalStats.totalTags}" /></strong> <!-- ${globalStats.totalTags} --> tags entered and <strong><nf:format number="${globalStats.totalMatches}" /></strong> <!-- ${globalStats.totalMatches} --> matches made</p>
						</c:if>
						<c:if test="${user != null && !user.anonymous}">
							<p>You contributed <strong><nf:format number="${user.totalTags}"/></strong> of <strong ><nf:format number="${globalStats.totalTags}" /></strong> tags and <strong><nf:format number="${user.totalMatches}" /></strong> of <strong><nf:format number="${globalStats.totalMatches}"  /></strong> matches overall</p>				</c:if>	
						</c:if>		
						
						
						<c:if test="${globalStats.currentlyPlaying < 1}">
						<c:if test="${user == null || user.anonymous}">
							<p style="margin-top:-17px;"><strong><nf:format number="${globalStats.totalTags}" /></strong> <!-- ${globalStats.totalTags} --> tags entered and <strong><nf:format number="${globalStats.totalMatches}" /></strong> <!-- ${globalStats.totalMatches} --> matches made</p>
						</c:if>
						<c:if test="${user != null && !user.anonymous}">
							<p style="margin-top:-17px;">You contributed <strong><nf:format number="${user.totalTags}"/></strong> of <strong ><nf:format number="${globalStats.totalTags}" /></strong> tags and <strong><nf:format number="${user.totalMatches}" /></strong> of <strong><nf:format number="${globalStats.totalMatches}"  /></strong> matches overall</p>				</c:if>	
						</c:if>		
						
					</div>
					</c:if>
				</c:if>
    	</div>
    </div>

    <header class="site-header">
  		<div class="container row relative equal-cols">

	  		
	  		<c:if test="${globalStats != null}">
	  			<c:if test="${user == null || !user.playerBarVisible}">
					<div class="stats span6 offset3">
						<c:if test="${user == null || user.anonymous}">
							<p><strong><nf:format number="${globalStats.totalTags}" /></strong> <!-- ${globalStats.totalTags} --> tags entered and <strong><nf:format number="${globalStats.totalMatches}" /></strong> <!-- ${globalStats.totalMatches} --> matches made</p>
						</c:if>
						<c:if test="${user != null && !user.anonymous}">
							<p>You contributed <strong><nf:format number="${user.totalTags}"/></strong> of <strong ><nf:format number="${globalStats.totalTags}" /></strong> tags and <strong><nf:format number="${user.totalMatches}" /></strong> of <strong><nf:format number="${globalStats.totalMatches}"  /></strong> matches over all</p>	
						</c:if>		
					</div>
					</c:if>
				</c:if>
				
				<c:if test="${user != null && user.playerBarVisible}">
					<div class="span3 box media col">
						<tt:profileLink anonymous="${user.anonymous}" id="${user.id}">
							<c:if test="${user.anonymous}">
								<img src="${user.avatarUrl}" class="pull-left" />
								<h2 class="h5 reset">Hello, anonymous</h2>
							</c:if>
							<c:if test="${!user.anonymous}">
								<img src="${user.avatarUrl}"class="pull-left" />
								<h2 class="h5 reset">Hello, <c:out value="${user.name}"/>!</h2>
							</c:if>								
							<p class="reset"><strong class="h1"><nf:format number="${user.totalScore}" /></strong></p>
							<p class="reset small">Your overall score</p>
						</tt:profileLink>
					</div>
					<c:if test="${user != null && user.countNewPioneerMatches > 0}">
						<div class="span3 box media col">
							<a href="/profiel/${user.id}#pionier" class="unstyled">
								<img src="/static/img/match-pioneer-xl.png" class="pull-left" />
								<p class="reset">explanation &raquo;</p>	
								<p class="reset"><strong class="h1">+ <nf:format number="${user.countNewPioneerMatches * 150}" /></strong></p>
								<p class="reset small">since your last game</p>
							</a>
						</div>
					</c:if>
				</c:if>
				<div class="login-box">
					<ul class="span3 box unstyled reset text-right col pull-right">		
						<c:if test="${user != null && !user.anonymous}">
							<div style="margin-top:24px"><li><a class="link-block" href="/uitloggen">Log out</a></li></div>
						</c:if>
						<c:if test="${user == null || user.anonymous}">		
							<li><a class="link-block" href="/inloggen">Log in</a></li>
							<li><a class="link-block" href="/registreren">Register</a></li>
						</c:if>
					</ul></div>
				<div style="margin-top:-99px;position:absolute;margin-left:740px;width:200px;"><c:if test="${globalStats.currentlyPlaying > 0}">
					<p class="online">
						<strong>${globalStats.currentlyPlaying} ${globalStats.currentlyPlaying == 1 ? 'player' : 'players'}</strong> currently online
					</p>
				</c:if></div>
			
			</div>

			
   	</header>

	<section class="main container">
		<jsp:doBody/>
	</section>

	<div class="site-footer">
	<br>
	 	<div class="container">
				<ul class="unstyled horizontal pull-left">
					<li><a href="/">Home</a></li>
		  		<%--li><a href="/over-het-spel">Example contentpage 1</a></li>
		  		<li><a href="/spelinstructies">Example contentpage 2</a></li--%>
		  	</ul>
	  		<ul class="unstyled horizontal pull-right">
	  			<li>Find us on</li>
		  		<%--li><a href="http://www.facebook.com/pages/Waisda/187799419727">Facebook</a></li--%>
		  		<li><a href="https://twitter.com/EuropeanaTech">Twitter</a></li>
		  		<li><a href="http://pro.europeana.eu/web/network/europeana-tech">Blog</a></li>
		  	</ul>
		</div>
		<div class="container spaced-ext">
			<p class="pull-left" style="margin-top:20px;">
				With support of<br><br>
				<a style="padding-right:20px" href="http://beeldengeluid.nl" target="_nieuw"> <img height="40px" src="/static/img/logo1.png" title="Beeld en Geluid /"></a>

				<a style="padding-right:20px" href="http://vu.nl" target="_nieuw"><img height="40px" src="/static/img/logo2.png" title="Vrije Universiteit" /></a>
							<a href="http://www.natuurbeelden.nl/" target="_nieuw">
							<img height="40px" src="/static/img/logo3.png" title="Natuurbeelden" /></a>
		<br><br>
			</p>
		
		</div>		
	</div>

	<c:if test="${cssClass != 'body' && cssClass != 'game'}">
		<tt:games-queue dynamic="true"/>
	</c:if>

</body>
