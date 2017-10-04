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
            <div class="waisda-logo"><a href="/"> <img src="/static/img/waisda-logo.png" alt="Waisda" width="320"/></a></div>
            <c:if test="${globalStats != null}">
                <c:if test="${user != null || user.playerBarVisible}">
                <div class="stats pull-right">
                    <c:choose>
                        <c:when test="${globalStats.currentlyPlaying > 0}">
                            <c:if test="${user == null || user.anonymous}">
                                <p ><strong><nf:format number="${globalStats.totalTags}" /></strong> <!-- ${globalStats.totalTags} --> termen ingevoerd en <strong><nf:format number="${globalStats.totalMatches}" /></strong> <!-- ${globalStats.totalMatches} --> overeenkomsten gemaakt</p>
                            </c:if>
                            <c:if test="${user != null && !user.anonymous}">
                                <p>Je hebt <strong><nf:format number="${user.totalTags}"/></strong> van de <strong ><nf:format number="${globalStats.totalTags}" /></strong> termen en <strong><nf:format number="${user.totalMatches}" /></strong> van de <strong><nf:format number="${globalStats.totalMatches}"  /></strong> overeenkomsten verzorgt</p>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${user == null || user.anonymous}">
                                <p style="margin-top:-17px;"><strong><nf:format number="${globalStats.totalTags}" /></strong> <!-- ${globalStats.totalTags} --> termen ingevoerd en <strong><nf:format number="${globalStats.totalMatches}" /></strong> <!-- ${globalStats.totalMatches} --> overeenkomsten gemaakt</p>
                            </c:if>
                            <c:if test="${user != null && !user.anonymous}">
                                <p style="margin-top:-17px;">Je hebt <strong><nf:format number="${user.totalTags}"/></strong> van de <strong ><nf:format number="${globalStats.totalTags}" /></strong> termen en <strong><nf:format number="${user.totalMatches}" /></strong> van de <strong><nf:format number="${globalStats.totalMatches}"  /></strong> overeenkomsten verzorgt</p>
                            </c:if>
                        </c:otherwise>
                    </c:choose>                
                </div>
                </c:if>
            </c:if>
    	</div>
    </div>

    <header class="site-header">
  		<div class="container row relative equal-cols">

            <c:if test="${globalStats != null}">
                <c:if test="${user == null || !user.playerBarVisible}">
                    <div class="stats offset1">
                        <c:if test="${user == null || user.anonymous}">
                            <p><strong><nf:format number="${globalStats.totalTags}" /></strong> <!-- ${globalStats.totalTags} --> termen ingevoerd en <strong><nf:format number="${globalStats.totalMatches}" /></strong> <!-- ${globalStats.totalMatches} --> overeenkomsten gemaakt</p>
                        </c:if>
                        <c:if test="${user != null && !user.anonymous}">
                            <p>Je hebt <strong><nf:format number="${user.totalTags}"/></strong> van de <strong ><nf:format number="${globalStats.totalTags}" /></strong> termen en <strong><nf:format number="${user.totalMatches}" /></strong> van de <strong><nf:format number="${globalStats.totalMatches}"  /></strong> overeenkomsten verzorgt</p>
                        </c:if>
                    </div>
                </c:if>
            </c:if>

            <c:if test="${user != null && user.playerBarVisible}">
                <div class="span3 box media col">
                    <tt:profileLink anonymous="${user.anonymous}" id="${user.id}">
                        <c:if test="${user.anonymous}">
                            <img src="${user.avatarUrl}" class="pull-left" />
                            <h2 class="h5 reset">Hallo</h2>
                        </c:if>
                        <c:if test="${!user.anonymous}">
                            <img src="${user.avatarUrl}"class="pull-left" />
                            <h2 class="h5 reset">Hallo, <c:out value="${user.name}"/>!</h2>
                        </c:if>
                        <p class="reset"><strong class="h1"><nf:format number="${user.totalScore}" /></strong></p>
                        <p class="reset small">Je score</p>
                    </tt:profileLink>
                </div>
                <c:if test="${user != null && user.countNewPioneerMatches > 0}">
                    <div class="span3 box media col">
                        <a href="/profiel/${user.id}#pionier" class="unstyled">
                            <img src="/static/img/match-pioneer-xl.png" class="pull-left" />
                            <p class="reset">uitleg &raquo;</p>
                            <p class="reset"><strong class="h1">+ <nf:format number="${user.countNewPioneerMatches * 150}" /></strong></p>
                            <p class="reset small">sinds je laatst gespeelde ronde</p>
                        </a>
                    </div>
                </c:if>
            </c:if>

            <div class="login-box">
                <ul class="span3 box unstyled reset text-right col pull-right">
                    <c:if test="${user != null && !user.anonymous}">
                        <div style="margin-top:24px"><li><a class="link-block" href="/uitloggen">uitloggen</a></li></div>
                    </c:if>
                    <c:if test="${user == null || user.anonymous}">
                        <li><a class="link-block" href="/inloggen">inloggen</a></li>
                        <li><a class="link-block" href="/registreren">account maken</a></li>
                    </c:if>
                </ul>
            </div>
            <div style="margin-top:-99px;position:absolute;margin-left:740px;width:200px;">
            <c:if test="${globalStats.currentlyPlaying > 0}">
                <p class="online">
                    momenteel <strong>${globalStats.currentlyPlaying} ${globalStats.currentlyPlaying == 1 ? 'speler' : 'spelers'}</strong> online
                </p>
            </c:if>
            </div>
        </div>
   	</header>

	<section class="main container">
		<jsp:doBody/>
	</section>

	<div class="site-footer">
	<br>
	 	<div class="container">
            <ul class="unstyled horizontal pull-left">
                <li><a href="/">Hoofdpagina</a></li>
            </ul>
		</div>
		<div class="container spaced-ext">
			<p class="pull-left bold"  style="margin-top:20px;">
				Met steun van<br><br>
				<a style="padding-right:20px" href="http://beeldengeluid.nl" target="_blank"> <img height="40px" src="/static/img/logo1.png" title="Beeld en Geluid /"></a>

				<a style="padding-right:20px" href="http://vu.nl" target="_blank"><img height="40px" src="/static/img/logo2.png" title="Vrije Universiteit" /></a>
							<a href="http://www.natuurbeelden.nl/" target="_blank">
							<img height="40px" src="/static/img/logo3.png" title="Natuurbeelden" /></a>
		        <br><br>
			</p>
		</div>		
	</div>

	<c:if test="${cssClass != 'body' && cssClass != 'game'}">
		<tt:games-queue dynamic="true"/>
	</c:if>

</body>
